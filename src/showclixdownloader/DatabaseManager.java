package showclixdownloader;

import java.io.*;
import java.net.HttpCookie;
import java.sql.*;
import java.util.*;

/**
 *
 * @author SunnyBat
 */
public class DatabaseManager {

  private static String databaseDirectory = "C:\\Users\\SunnyBat\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\febeprof.OldProfile\\";

  /**
   * Sets the directory that the Firefox cookie database is located in. NOTE that if the directory
   * does NOT contain a cookie database, it WILL NOT BE SET and the previous directory will be used
   * instead!
   *
   * @param dir The new directory that the Firefox cookie database is located in
   */
  public static void setDatabaseDirectory(String dir) {
    if (!dir.endsWith("\\")) {
      dir += "\\";
    }
    if (!(new File(dir + "cookies.sqlite").exists())) {
      //ShowclixScanner.println("NOTE: The given database does NOT contain a cookies database!", //ShowclixScanner.LOGTYPE.MINIMUM);
      //ShowclixScanner.println("Eranous path: " + dir, //ShowclixScanner.LOGTYPE.MINIMUM);
      return;
    }
    databaseDirectory = dir;
  }

  /**
   * Returns the current database directory. Straight and simple.
   *
   * @return The current database directory.
   */
  public static String getDatabaseDirectory() {
    return databaseDirectory;
  }

  /**
   * Checks whether or not the cookie database is in use.
   *
   * @return True if it's NOT in use, false if it IS in use.
   */
  public static boolean isDatabaseAvailable() {
    File databaseUseFile1 = new File(databaseDirectory + "cookies.sqlite-wal");
    File databaseUseFile2 = new File(databaseDirectory + "cookies.sqlite-shm");
    if (!databaseUseFile1.exists() && !databaseUseFile2.exists()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Reads ALL cookies currently in the Firefox cookie database and prints them out. Note that this
   * really shouldn't be used very much, as it's preferred to only get cookies from a specific
   * domain using {@link #readCookies(java.lang.String)}.
   */
  public static void readCookies() {
    readCookies("");
  }

  /**
   * Reads the cookies currently in the Firefox cookie database and prints out the ones belonging
   * to the given domain. The domain should be somthing like "example.com"
   *
   * @param filterDomain The domain to search for
   */
  public static void readCookies(String filterDomain) {
    Connection connection;
    ResultSet resultSet;
    Statement statement;
    List<String> nameList = new ArrayList();
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + databaseDirectory + "cookies.sqlite");
      statement = connection.createStatement();
      resultSet = statement.executeQuery("SELECT * FROM moz_cookies");
      //ShowclixScanner.println("Database Column Names:");
      for (int a = 1; a < resultSet.getMetaData().getColumnCount(); a++) {
        //ShowclixScanner.println(resultSet.getMetaData().getColumnName(a));
      }
      int lastID = 1;
      while (resultSet.next()) {
        if (filterDomain != null) {
          if (resultSet.getString("baseDomain").contains(filterDomain)) {
            //ShowclixScanner.println("-------------Cookie Found-------------");
            for (int a = 1; a < resultSet.getMetaData().getColumnCount(); a++) {
              String cN = resultSet.getMetaData().getColumnName(a);
              //ShowclixScanner.println(cN + " = " + resultSet.getString(cN));
            }
            nameList.add(resultSet.getString("name"));
//          //ShowclixScanner.println("Name = " + resultSet.getString("name"));
//          //ShowclixScanner.println("Value = " + resultSet.getString("value"));
//          //ShowclixScanner.println("Expires = " + resultSet.getString("expiry"));
//          //ShowclixScanner.println("isSecure = " + resultSet.getString("isSecure"));
//          //ShowclixScanner.println("Path = " + resultSet.getString("path"));
            //ShowclixScanner.println("-----------End Cookie Output-----------");
          }
        }
        lastID = resultSet.getInt("id");
      }
      resultSet.close();
      statement.close();
      connection.close();
      lastID++;
    } catch (Exception e) {
      //ShowclixScanner.println("ERROR");
      e.printStackTrace();
    }
  }

  /**
   * Writes the given HttpCookies to the Firefox database. Note that the database should be set with
   * {@link #setDatabaseDirectory(java.lang.String)} before this is run.
   *
   * @param cookieList The list of cookies to write to the database
   */
  public static void writeCookies(List<HttpCookie> tempList) {
    List<HttpCookie> cookieList = new ArrayList(); // Apparently the List can be unchangeable, so create a new one and dump into it so it IS changeable
    cookieList.addAll(tempList);
    if (cookieList == null) {
      return;
    }
    makeCookieBackup();
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    HttpCookie currCookie;
    List nameList = new ArrayList();
    Iterator<HttpCookie> iterator = cookieList.iterator();
    while (iterator.hasNext()) {
      currCookie = iterator.next();
      nameList.add(currCookie.getName());
    }
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + databaseDirectory + "cookies.sqlite");
      statement = connection.createStatement();
      resultSet = statement.executeQuery("SELECT * FROM moz_cookies");
      statement = connection.createStatement();
      connection.setAutoCommit(false);
      int lastID = 1;
      while (resultSet.next()) {
        if (nameList.contains(resultSet.getString("name"))) {
          System.out.println("UPDATING A COOKIE!!!");
          currCookie = cookieList.get(nameList.indexOf(resultSet.getString("name")));
          if (currCookie == null) {
            continue;
          }
          String sql = "UPDATE moz_cookies set value = '" + currCookie.getValue() + "' where name='" + currCookie.getName() + "';";
          statement.close();
          statement = connection.createStatement();
          statement.executeUpdate(sql);
          connection.commit();
          cookieList.remove(currCookie);
          nameList.remove(currCookie.getName());
          //resultSet = statement.executeQuery("SELECT * FROM moz_cookies");
        }
        lastID = resultSet.getInt("id");
      }
      iterator = cookieList.iterator();
      while (iterator.hasNext()) {
        currCookie = iterator.next();
        String sql = "INSERT INTO moz_cookies (ID,BASEDOMAIN,APPID,INBROWSERELEMENT,NAME,VALUE,HOST,PATH,EXPIRY,LASTACCESSED,CREATIONTIME,ISSECURE) "
                + "VALUES (" + lastID + ", 'showclix.com', 0, 0, '" + currCookie.getName() + "', '" + currCookie.getValue() + "', '.showclix.com', '"
                + currCookie.getPath() + "', 1464970835, " + (System.currentTimeMillis() * 1000) + ", " + (System.currentTimeMillis() * 1000) + ", '"
                + currCookie.getSecure() + "' );";
        statement.close();
        statement = connection.createStatement();
        statement.executeUpdate(sql);
        connection.commit();
        lastID++;
      }
      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
        resultSet.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * For internal use only. Use to make a backup of the cookie directory before making changes to
   * it. This will allow you to recover most, if not all, of your cookies should something go wrong.
   * This should be called before every database change.
   *
   * @return True if successful, false if not.
   */
  private static boolean makeCookieBackup() {
    try { // Code to make a copy of the current JAR file
      File inputFile = new File(databaseDirectory + "cookies.sqlite");
      InputStream fIn = new BufferedInputStream(new FileInputStream(inputFile));
      File outputFile = new File(databaseDirectory + "cookies.sqlite.bak");
      if (outputFile.exists()) {
        outputFile.delete();
      }
      BufferedOutputStream buffOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
      byte[] buffer = new byte[32 * 1024];
      int bytesRead = 0;
      while ((bytesRead = fIn.read(buffer)) != -1) {
        buffOutputStream.write(buffer, 0, bytesRead);
      }
      buffOutputStream.flush();
      buffOutputStream.close();
      fIn.close();
      //ShowclixScanner.println("File copied.");
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
