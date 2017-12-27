/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.DBModelTest;
import java.awt.Point;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class NewTabForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  int tabID = -1;
  //VS_RACE race = null;

  /**
   * Creates new form UserControlForm
   */
  private NewTabForm(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;
  }

  private static NewTabForm form = null;

  public static NewTabForm init(MainForm mainForm, int tabID) {
    if (form == null) {
      form = new NewTabForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.setVisible(false);    
    form.tabID = tabID;   
    form.prepareForm();

    return form;
  }

  public void prepareForm() {

    /*if (raceID != -1) {
      try {
        race = VS_RACE.dbControl.getItem(mainForm.con, "RACE_ID=?",raceID);        
                           
      } catch (Exception e) {
        mainForm.error_log.writeFile(e);
        JOptionPane.showMessageDialog(this, "Loading user is error. " + getName().toString(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }else{
       race = null;
    }

    if (race==null) race = new VS_RACE();
    jtLapsCount.setText(""+race.COUNT_OF_LAPS);
    jtMinLapTime.setText(""+race.MIN_LAP_TIME);
    jtRaceName.setText(""+race.RACE_NAME);    */    
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    Caption = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jtCaption = new javax.swing.JTextField();
    jtLapsCount = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jtMinLapTime = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    jtCountOfPilots = new javax.swing.JTextField();
    jlChannel1 = new javax.swing.JLabel();
    jcbChannel1 = new javax.swing.JComboBox();
    jlChannel2 = new javax.swing.JLabel();
    jcbChannel2 = new javax.swing.JComboBox();
    jlChannel3 = new javax.swing.JLabel();
    jcbChannel3 = new javax.swing.JComboBox();
    jlChannel4 = new javax.swing.JLabel();
    jcbChannel4 = new javax.swing.JComboBox();
    jlChannel5 = new javax.swing.JLabel();
    jcbChannel5 = new javax.swing.JComboBox();
    jlChannel6 = new javax.swing.JLabel();
    jcbChannel6 = new javax.swing.JComboBox();
    jlChannel7 = new javax.swing.JLabel();
    jcbChannel7 = new javax.swing.JComboBox();
    jlChannel8 = new javax.swing.JLabel();
    jcbChannel8 = new javax.swing.JComboBox();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();

    setTitle("Race Card");
    setResizable(false);

    Caption.setText("Race name:");

    jLabel2.setText("Laps:");

    jLabel3.setText("Min lap time (sec):");

    jtMinLapTime.setText("18");

    jLabel1.setText("Count of pilots in group:");

    jtCountOfPilots.setText("4");

    jlChannel1.setText("Channel 1:");

    jcbChannel1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel2.setText("Channel 2:");

    jcbChannel2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel3.setText("Channel 3:");

    jcbChannel3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel4.setText("Channel 4:");

    jcbChannel4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel5.setText("Channel 5:");

    jcbChannel5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel6.setText("Channel 6:");

    jcbChannel6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel7.setText("Channel 7:");

    jcbChannel7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jlChannel8.setText("Channel 8:");

    jcbChannel8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Caption)
          .addComponent(jLabel2)
          .addComponent(jLabel3)
          .addComponent(jLabel1)
          .addComponent(jlChannel1)
          .addComponent(jlChannel2)
          .addComponent(jlChannel3)
          .addComponent(jlChannel4)
          .addComponent(jlChannel5)
          .addComponent(jlChannel6)
          .addComponent(jlChannel7)
          .addComponent(jlChannel8))
        .addGap(20, 20, 20)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jtCaption)
            .addContainerGap())
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jcbChannel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jtLapsCount, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jtMinLapTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addComponent(jtCountOfPilots, javax.swing.GroupLayout.Alignment.LEADING)))
            .addGap(0, 98, Short.MAX_VALUE))))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Caption)
          .addComponent(jtCaption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(jtLapsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jtMinLapTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel1)
          .addComponent(jtCountOfPilots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel1)
          .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel2)
          .addComponent(jcbChannel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel3)
          .addComponent(jcbChannel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel4)
          .addComponent(jcbChannel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel5)
          .addComponent(jcbChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel6)
          .addComponent(jcbChannel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel7)
          .addComponent(jcbChannel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel8)
          .addComponent(jcbChannel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    bSave.setText("Save");
    bSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bSaveActionPerformed(evt);
      }
    });

    bCancel.setText("Cancel");
    bCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(bSave)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(bCancel)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap(17, Short.MAX_VALUE)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bSave)
          .addComponent(bCancel))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_bCancelActionPerformed

  private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
    /*try {      
      try{
        race.COUNT_OF_LAPS = Integer.parseInt(jtLapsCount.getText());
      }catch(Exception e){}
      try{
        race.MIN_LAP_TIME = Integer.parseInt(jtMinLapTime.getText());
      }catch(Exception e){}  
      race.RACE_NAME = jtRaceName.getText();  
      if (race.RACE_ID!=-1) {
        VS_RACE.dbControl.update(mainForm.con,race);        
      } else { 
        VS_RACE.dbControl.insert(mainForm.con, race);
      }  
      setVisible(false);
      RaceList.init(mainForm).refreshData();
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Saving race is error. " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }*/
  }//GEN-LAST:event_bSaveActionPerformed

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
      java.util.logging.Logger.getLogger(NewTabForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(NewTabForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(NewTabForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(NewTabForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new NewTabForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Caption;
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox jcbChannel1;
  private javax.swing.JComboBox jcbChannel2;
  private javax.swing.JComboBox jcbChannel3;
  private javax.swing.JComboBox jcbChannel4;
  private javax.swing.JComboBox jcbChannel5;
  private javax.swing.JComboBox jcbChannel6;
  private javax.swing.JComboBox jcbChannel7;
  private javax.swing.JComboBox jcbChannel8;
  private javax.swing.JLabel jlChannel1;
  private javax.swing.JLabel jlChannel2;
  private javax.swing.JLabel jlChannel3;
  private javax.swing.JLabel jlChannel4;
  private javax.swing.JLabel jlChannel5;
  private javax.swing.JLabel jlChannel6;
  private javax.swing.JLabel jlChannel7;
  private javax.swing.JLabel jlChannel8;
  private javax.swing.JTextField jtCaption;
  private javax.swing.JTextField jtCountOfPilots;
  private javax.swing.JTextField jtLapsCount;
  private javax.swing.JTextField jtMinLapTime;
  // End of variables declaration//GEN-END:variables
}
