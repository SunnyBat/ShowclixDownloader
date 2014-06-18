/*
 * ==========PACKET LIST==========
 * 
 * OUTPUT:
 * 2 = Connection successful packet (send first)
 * 23 = Kill connection, we're done here
 * 65 = Send String to program (general use)
 * 72 = Sending cookie (which sends two Strings at a time, with the String lengths being sent first)
 * 103 = Cookies all sent, save them to Firefox
 * 
 * INPUT:
 * 12 = Connection accepted
 * 13 = Connection rejected
 * 23 = Kill connection, we're done here
 */
package showclixdownloader;

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author SunnyBat
 */
public class ConnectionHandler {

  private static String address;
  private static Socket mySock;
  private static final int port = 9243;
  private static InputStream myInput;
  private static OutputStream myOutput;
  private static List<HttpCookie> cookies = new ArrayList();
  private static long lastConnectionTime;
  private static int failCount;

  public static void setAddress(String add) {
    address = add;
  }

  public static void connect() {
    try {
      System.out.println("Connecting...");
      ShowclixDownloader.status.setConnectionAddress(address);
      ShowclixDownloader.status.setConnectionStatus("Connecting...");
      mySock = new Socket(address, port);
      mySock.setSoTimeout(5000);
      myInput = mySock.getInputStream();
      myOutput = mySock.getOutputStream();
      ShowclixDownloader.status.setConnectionStatus("Waiting for connection confirmation...");
      int a;
      while ((a = readNext()) == 0) {
      }
      if (a == 12) {
        System.out.println("Connection accepted!");
        ShowclixDownloader.status.setConnectionStatus("Connection accepted! Waiting for cookies...");
      } else if (a == 13) {
        System.out.println("Connection REJECTED.");
        ShowclixDownloader.status.setConnectionStatus("Connection rejected. Please try and reconnect.");
        killConnection();
        return;
      } else {
        System.out.println("Unknown response: " + a);
        ShowclixDownloader.status.setConnectionStatus("Connection rejected. Please try and reconnect.");
        killConnection();
        return;
      }
      System.out.println("Conected: " + mySock.getInetAddress().getHostAddress());
      while (readNext() != -1) {
      }
      killConnection();
    } catch (Exception e) {
      System.out.println("Unable to connect.");
      ShowclixDownloader.status.setConnectionStatus("Unable to connect :(  Restart and try again?");
      e.printStackTrace();
    }
    ShowclixDownloader.status.setKillConnectionState(false);
  }

  public static int readNext() {
    if (myInput == null) {
      return -1;
    }
    try {
      int code = myInput.read();
      if (code == 2) {
        ShowclixDownloader.status.setConnectionStatus("Connected! Waiting for cookies...");
      } else if (code == 65) {
        readString(myInput.read());
      } else if (code == 72) {
        ShowclixDownloader.status.setConnectionStatus("Reading cookie!");
        readCookie(myInput.read(), myInput.read());
      } else if (code == 103) {
        ShowclixDownloader.status.setKillConnectionState(false);
        ShowclixDownloader.status.setConnectionStatus("Closing Firefox...");
        ProcessHandler.killFirefox();
        ShowclixDownloader.status.setConnectionStatus("Waiting for cookies databse to close..");
        while (!DatabaseManager.isDatabaseAvailable()) { // Wait for Firefox to save changes to the cookie database
          Thread.sleep(100);
        }
//          ShowclixScanner.println("Database deemed available.", ShowclixScanner.LOGTYPE.NOTES);
        ShowclixDownloader.status.setConnectionStatus("Writing cookies...");
        DatabaseManager.writeCookies(cookies);
        openLinkInBrowser("https://www.showclix.com/event/3776089");
        ShowclixDownloader.status.setConnectionStatus("Finished! You can now close this at any time.");
        killConnection();
      } else if (code == 23) {
        System.out.println("Kill packet found.");
        killConnection();
      } else if (code == -1) {
        System.out.println("Connection lost.");
        ShowclixDownloader.status.setConnectionStatus("Connection has been lost/denied. To reconnect, restart the program.");
        killConnection();
      }
      return code;
    } catch (IOException e) {
      if (System.currentTimeMillis() - lastConnectionTime < 4000) {
        System.out.println("Connection unavailable.");
        if (++failCount >= 100) {
          killConnection();
          ShowclixDownloader.status.setConnectionStatus("Connection to server has been lost. To reconnect, restart the program.");
          return -1;
        }
        return 0;
      } else {
        System.out.println("No input found.");
        lastConnectionTime = System.currentTimeMillis();
        return 0;
      }
    } catch (InterruptedException e) {
    }
    return -1;
  }

  public static void openLinkInBrowser(String link) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(new URI(link));
      } catch (Exception e) {
//        ShowclixScanner.println("Unable to open link in browser window!");
      }
    } else {
//      ShowclixScanner.println("Unable to open link in default browser.");
    }
  }

  public static String readString(int size) {
    try {
      byte[] BTS = new byte[size];
      myInput.read(BTS, 0, size);
      System.out.println("String from NETWORK = " + new String(BTS));
      return new String(BTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void readCookie(int size1, int size2) {
    cookies.add(new HttpCookie(readString(size1), readString(size2)));
  }

  public static void killConnection() {
    System.out.println("Killing connection...");
    try {
      if (myInput != null) {
        myInput.close();
      }
      if (myOutput != null) {
        myOutput.write(23);
        myOutput.close();
      }
    } catch (Exception e) {
    }
    failCount = 100;
  }
}
