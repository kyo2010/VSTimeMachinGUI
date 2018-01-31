/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import javax.swing.JDialog;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class InfoForm extends /*javax.swing.JFrame*/ JDialog {
  
  MainForm mainForm = null;
  static InfoForm form = null;
  String caption = "";

  /**
   * Creates new form InfoForm
   */
  public InfoForm() {
    this.setUndecorated(true);
    com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
    com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.75f);
    
    setBounds(getGraphicsConfiguration().getBounds());
    //getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
    
    initComponents();
    setVisible(false);
    //setFocusPainted(false);
    //setBorderPainted(false);
    //int w = i.getWidth();
	  //int h = i.getHeight();
	  //final Area s = new Area(new Rectangle(w, h));
    //com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
    //com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.75f);
    //com.sun.awt.AWTUtilities.setWindowShape(this, s);
  }
  
  public static InfoForm init(MainForm mainForm, String caption) {
    if (form == null) {
      form = new InfoForm();      
    }
    if (mainForm != null) {
      mainForm.setFormOnCenter(form);
    }
    form.jLabel1.setText(caption);    
    form.setVisible(false);
    return form;
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setAlwaysOnTop(true);
    setFocusable(false);
    setFocusableWindowState(false);
    setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 210)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("1");
    jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jLabel1.setInheritsPopupMenu(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
        .addGap(24, 24, 24))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(InfoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(InfoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(InfoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(InfoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new InfoForm().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  // End of variables declaration//GEN-END:variables
}
