/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JDialog;
import javax.swing.Timer; 
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;

/**
 *
 * @author kyo
 */
public class TimerForm extends /*javax.swing.JFrame*/ JDialog {
  
  MainForm mainForm = null;
  static TimerForm form = null;
  String caption = "";

  /**
   * Creates new form InfoForm
   */
  public TimerForm() {
    this.setUndecorated(true);
    //com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
    //com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.75f);
    
    //setOpacity(0.75f);
    setOpacity(0.75f);
    
    setBounds(getGraphicsConfiguration().getBounds());
    //getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
    
    initComponents();
    pack();
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
  
  Timer waitTimer = new Timer(1500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (mainForm.unRaceTime==0) return;
      if (mainForm.vsTimeConnector!=null) mainForm.vsTimeConnector.checkConnection();
      if (mainForm.activeGroup!=null){
        infoLabel.setText("");
        return;
      }      
      long t = Calendar.getInstance().getTimeInMillis();
      long d = t - mainForm.unRaceTime;
      //infoLabel.setText("                        ");
      //setVisible(false);
      
      infoLabel.setText(StageTab.getTimeIntervelForTimer(d));
      try{
        
      }catch(Exception ein){}
      setPosition();
      //setVisible(true);
    }
  });
  
  public void resetTimer(){
    mainForm.unRaceTime = Calendar.getInstance().getTimeInMillis();
  }
  
  public void setPosition(){
    if (mainForm!=null){
     Point p = mainForm.getLocationOnScreen();
     form.setLocation(p.x + mainForm.getWidth() - form.getSize().width, p.y + mainForm.getHeight()- (int)(form.getSize().height)); 
    }
  }
  
  public static TimerForm init(MainForm mainForm) {
    if (form == null) {
      form = new TimerForm();      
      form.mainForm = mainForm;
    }
    //form.setPosition();
    form.setVisible(false);
    form.waitTimer.start();
    return form;
  }
  
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(false);
        setFocusableWindowState(false);
        setResizable(false);

        infoLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        infoLabel.setForeground(new java.awt.Color(153, 0, 0));
        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("  ");
        infoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        infoLabel.setInheritsPopupMenu(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
      java.util.logging.Logger.getLogger(TimerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(TimerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(TimerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(TimerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new TimerForm().setVisible(true);
      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel infoLabel;
    // End of variables declaration//GEN-END:variables
}
