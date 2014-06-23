/*
 * ==========PACKET LIST==========
 * 
 * OUTPUT:
 * 23 = Kill connection, we're done here
 * 
 * INPUT:
 * 2 = Connection successful packet (send first)
 * 12 = Connection accepted
 * 13 = Connection rejected
 * 23 = Kill connection, we're done here
 * 72 = Sending cookie (which sends two Strings at a time, with the String lengths being sent first)
 * 103 = Cookies all sent, save them to Firefox
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
  private static Socket mySocket;
  private static final int port = 9243;
  private static InputStream myInput;
  private static OutputStream myOutput;
  private static List<HttpCookie> cookies = new ArrayList();
  private static long lastConnectionTime;
  private static int failCount;
  private static String showclixLink = "https://www.showclix.com/event/3846764";

  public static void setAddress(String add) {
    address = add;
  }

  public static void connect() {
    try {
      System.out.println("Connecting...");
      ShowclixDownloader.status.setConnectionAddress(address);
      ShowclixDownloader.status.setConnectionStatus("Connecting...");
      mySocket = new Socket(address, port);
      mySocket.setSoTimeout(5000);
      myInput = mySocket.getInputStream();
      myOutput = mySocket.getOutputStream();
      ShowclixDownloader.status.setConnectionStatus("Waiting for connection approval...");
      int a;
      while ((a = readNext()) == 0) {
      }
      if (a == 12) {
        System.out.println("Connection accepted!");
        ShowclixDownloader.status.setConnectionStatus("Connection accepted! Waiting for cookies...");
      } else if (a == 13) {
        System.out.println("Connection REJECTED.");
        ShowclixDownloader.status.setConnectionStatus("Connection rejected. Please try and reconnect.");
        sendKillPacket();
        return;
      } else {
        System.out.println("Unknown response: " + a);
        ShowclixDownloader.status.setConnectionStatus("Unknown response (" + a + "). Please try and reconnect.");
        sendKillPacket();
        return;
      }
      System.out.println("Conected: " + mySocket.getInetAddress().getHostAddress());
      while (readNext() != -1) {
      }
    } catch (Exception e) {
      System.out.println("Unable to connect.");
      ShowclixDownloader.status.setConnectionStatus("Unable to connect :(  Restart and try again?");
      e.printStackTrace();
    }
    ShowclixDownloader.status.setKillConnectionState(false);
  }

  public static int readNext() {
    if (mySocket.isClosed()) {
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
        openLinkInBrowser(showclixLink);
        ShowclixDownloader.status.setConnectionStatus("Finished! You can now close this at any time.");
        sendKillPacket();
      } else if (code == 23) {
        System.out.println("Kill packet found.");
        closeStreams();
        return -1;
      } else if (code == -1) {
        System.out.println("Connection lost.");
        ShowclixDownloader.status.setConnectionStatus("Connection has been lost/denied. To reconnect, restart the program.");
        sendKillPacket();
        closeStreams();
        return -1;
      } else if (code == 86) {
        System.out.println("PAX Website address found.");
        setShowclixLink(readString(myInput.read()));
      }
      return code;
    } catch (IOException e) {
      if (System.currentTimeMillis() - lastConnectionTime < 4000) {
        System.out.println("Connection unavailable.");
        if (++failCount >= 100) {
          closeStreams();
          ShowclixDownloader.status.setConnectionStatus("Connection to server has been lost. To reconnect, restart the program.");
          return -1;
        }
        return 0;
      } else {
        System.out.println("No input found.");
        lastConnectionTime = System.currentTimeMillis();
        return 0;
      }
    } catch (Exception e) {
      System.out.println("ERROR reading input! Terminating program!");
      e.printStackTrace();
    }
    return -1;
  }

  public static void setShowclixLink(String address) {
    showclixLink = address;
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

  public static void sendKillPacket() {
    if (myOutput == null) {
      System.out.println("Unable to send kill packet -- myOutput = null!");
      return;
    }
    System.out.println("Killing connection...");
    try {
      myOutput.write(23);
    } catch (Exception e) {
    }
  }

  public static void closeStreams() {
    System.out.println("Closing streams...");
    if (mySocket != null) {
      try {
        mySocket.close();
      } catch (IOException iOException) {
      }
    }
  }
}
