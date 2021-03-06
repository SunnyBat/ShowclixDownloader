/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package showclixdownloader;

/**
 *
 * @author SunnyBat
 */
public class Status extends javax.swing.JFrame {

  /** Creates new form Status */
  public Status() {
    initComponents();
  }

  public void setConnectionAddress(String address) {
    JLConnectionAddress.setText("Connection Address: " + address);
  }

  public void setConnectionStatus(String status) {
    JLConnectionStatus.setText("Connection Status: " + status);
  }
  
  public void setKillConnectionState(boolean enabled) {
    jButton1.setEnabled(enabled);
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
    JLConnectionAddress = new javax.swing.JLabel();
    JLConnectionStatus = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Showclix Downloader");

    JLConnectionAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    JLConnectionAddress.setText("Connection Address");

    JLConnectionStatus.setText("Connection Status");

    jButton1.setText("Disconnect from Server");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
          .addComponent(JLConnectionAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(JLConnectionStatus)
            .addGap(0, 0, Short.MAX_VALUE))
          .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JLConnectionAddress)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(JLConnectionStatus)
        .addGap(18, 18, 18)
        .addComponent(jButton1)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    // TODO add your handling code here:
    ConnectionHandler.sendKillPacket();
    setConnectionStatus("Connection terminated.");
  }//GEN-LAST:event_jButton1ActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel JLConnectionAddress;
  private javax.swing.JLabel JLConnectionStatus;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  // End of variables declaration//GEN-END:variables
}
