/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package showclixdownloader;

/**
 *
 * @author SunnyBat
 */
public class ShowclixDownloader {

  public static Setup setup;
  public static Status status;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic her
    setup = new Setup();
    for (int a = 0; a < args.length; a++) {
      if (args[a].startsWith("server:")) {
        setup.setServerAddress(args[a].substring(7));
      }
    }
    setup.setVisible(true);
    setup.openSelectCookiePrompt();
    while (setup.isVisible()) {
      try {
        Thread.sleep(250);
      } catch (InterruptedException interruptedException) {
      }
    }
    status = new Status();
    status.setVisible(true);
    ConnectionHandler.connect();
  }
}
