/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.Timer;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class InfoForm extends /*javax.swing.JFrame*/ JDialog {
  
  //MainForm mainForm = null;
  //static InfoForm form = null;
  public String caption = "";
  
  public static InfoForm lastIfoFrom  =null;
  public static void closeLastInfoFrom(){
    if (lastIfoFrom!=null){
      lastIfoFrom.setVisible(false);
      lastIfoFrom = null;
    }
  }
  

  public InfoForm(MainForm mainForm, String caption) {     
    this(mainForm,caption, 210);         
  }
  
  /**
   * Creates new form InfoForm
   */
  public InfoForm(MainForm mainForm, String caption, int fontsize) {          
    this.setUndecorated(true);
    
    //  setOpacity(0.75f);
    closeLastInfoFrom();
    
    setBounds(getGraphicsConfiguration().getBounds());
    
    initComponents();
    jLabel1.setText(caption);   
    jLabel1.setFont(new Font(jLabel1.getFont().getFontName(),Font.BOLD,fontsize));
    
    this.caption = caption;    
    lastIfoFrom = this;
    if (mainForm != null) {
      //com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
      //com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.75f); 
      
      try{
        com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
        com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.75f);    
      }catch(UnsupportedOperationException ex){
        System.out.println("Transperent windows is not supported");
      }
      
      mainForm.setFormOnCenter(this);
    }else{
       Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    }
    setVisible(true);
   }
  
  /*@Override
  public void setVisible(boolean vis){
    if (vis==false){
      form.jLabel1.setText("");   
      captionTimer.stop();
    } else{
      //form.jLabel1.setText("");   
      captionTimer.start();
    } 
    super.setVisible(vis);
  }*/
  
  /*public static InfoForm init(MainForm mainForm, String caption, int fontsize) {
    if (form == null) {
      form = new InfoForm();      
    }
    //form.setVisible(false);
    form.caption = caption;
    form.jLabel1.setText(caption);        
    if (mainForm != null) {
      mainForm.setFormOnCenter(form);
    }    
    form.jLabel1.setFont(new Font(form.jLabel1.getFont().getFontName(),Font.BOLD,fontsize));
    return form;    
  } */ 
  
  public static String getCurrentInfo(){
    if (lastIfoFrom!=null && lastIfoFrom.isVisible()) return lastIfoFrom.caption;
    return "";
  }
  
  /*Timer captionTimer = new Timer(100, new AbstractAction(){
      @Override
      public void actionPerformed(ActionEvent e) {
          form.jLabel1.setText(caption);   
      }
  
  } );
  
  public static InfoForm init(MainForm mainForm, String caption) {
    return init(mainForm,caption,210);
  }*/
  
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        //new InfoForm().setVisible(true);
      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
