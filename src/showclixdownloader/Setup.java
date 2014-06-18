/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package showclixdownloader;

import javax.swing.JFileChooser;

/**
 *
 * @author SunnyBat
 */
public class Setup extends javax.swing.JFrame {

  private JFileChooser chooser = new JFileChooser();

  /** Creates new form Setup */
  public Setup() {
    initComponents();
    customComponents();
  }
  
  private void customComponents() {
    setLocationRelativeTo(null);
  }
  
  public void setServerAddress(String address) {
    JTFIP.setText(address);
  }

  public void setCookieDirText(String directory) {
    String newStr = directory.substring(directory.length() / 5, directory.length());
    jLabel6.setText("..." + newStr.substring(newStr.indexOf("\\", 0), newStr.length()));
  }
  
  public void openSelectCookiePrompt() {
    JBSelectCookieDirActionPerformed(null);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    JTFIP = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();
    jLabel6 = new javax.swing.JLabel();
    JBSelectCookieDir = new javax.swing.JButton();
    JBStart = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Download Cookies");

    jLabel2.setText("IP");

    JTFIP.setText("192.168.1.12");

    jLabel6.setText("Firefox Cookies Directory");

    JBSelectCookieDir.setText("Change Cookies Directory");
    JBSelectCookieDir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JBSelectCookieDirActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(JBSelectCookieDir, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 126, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(jLabel6)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JBSelectCookieDir))
    );

    JBStart.setText("CONNECT!");
    JBStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JBStartActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JTFIP))
          .addComponent(JBStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addContainerGap()
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGap(11, 11, 11)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(JTFIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
        .addComponent(JBStart)
        .addContainerGap())
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(128, 128, 128)
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(129, Short.MAX_VALUE)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void JBSelectCookieDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSelectCookieDirActionPerformed
    // TODO add your handling code here:
    chooser.setCurrentDirectory(new java.io.File(System.getenv("APPDATA")));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("All Files", "sqlite"));
    final String reqSub = "Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles";
    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
      @Override // C:\Users\SunnyBat\AppData\Roaming
      public boolean accept(java.io.File f) {
        String name = f.getName().toLowerCase();
        String userSub;
        try {
          userSub = f.getAbsolutePath().substring(f.getAbsolutePath().toLowerCase().indexOf("users", 0));
          if (userSub.length() < 8) {
            userSub = "C:\\Users\\GoodName32481234";
          }
          userSub += "\\";
        } catch (Exception e) {
          try {
            if (f.getAbsolutePath().endsWith(":\\")) {
              return true;
            }
          } catch (Exception ef) {
          }
          userSub = "Not here, Unfortunately\\";
        }
        if (name.length() < 2) {
          return false;
        } else if (name.equals("users")
          || userSub.endsWith(System.getProperty("user.name") + "\\")
            || name.equals("appdata")
            || name.equals("roaming")
            || name.equals("mozilla")
            || name.equals("firefox")
            || name.equals("profiles")
            || name.equals("cookies.sqlite")) {
            return true;
          } else if (userSub.contains("\\Firefox\\Profiles")) {
            if (userSub.substring(0, userSub.lastIndexOf("\\") - 1).substring(0, userSub.substring(0, userSub.lastIndexOf("\\") - 1).lastIndexOf("\\")).equals(reqSub)) {
              return true;
            }
          }
          return false;
        }

        @Override
        public String getDescription() {
          return "Find For Me";
        }
      });
      chooser.setFileHidingEnabled(false);
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setDialogTitle("Select Firefox Cookie Database");
      chooser.setLocation(getX(), getY());
      chooser.setVisible(true);
      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        DatabaseManager.setDatabaseDirectory(chooser.getCurrentDirectory().getAbsolutePath());
        setCookieDirText(DatabaseManager.getDatabaseDirectory());
      } else {
        System.out.println("No Selection ");
      }
  }//GEN-LAST:event_JBSelectCookieDirActionPerformed

  private void JBStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBStartActionPerformed
    // TODO add your handling code here:
    ConnectionHandler.setAddress(JTFIP.getText());
    setVisible(false);
  }//GEN-LAST:event_JBStartActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton JBSelectCookieDir;
  private javax.swing.JButton JBStart;
  private javax.swing.JTextField JTFIP;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  // End of variables declaration//GEN-END:variables
}