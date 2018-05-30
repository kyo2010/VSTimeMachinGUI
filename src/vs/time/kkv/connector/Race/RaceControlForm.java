/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Race;

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
public class RaceControlForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  int raceID = -1;
  VS_RACE race = null;

  /**
   * Creates new form UserControlForm
   */
  private RaceControlForm(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;
  }

  private static RaceControlForm form = null;

  public static RaceControlForm init(MainForm mainForm, int raceID) {
    if (form == null) {
      form = new RaceControlForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.setVisible(false);    
    form.raceID = raceID;   
    form.prepareForm();

    return form;
  }

  public void prepareForm() {

    if (raceID != -1) {
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
    jtMaxRaceTime.setText(""+race.MAX_RACE_TIME);
    edJudge.setText(race.JUDGE);
    edSecretary.setText(race.SECRETARY);
    jtRaceName.setText(""+race.RACE_NAME);    
    chPostponingStart.setSelected(race.POST_START==1);    
    if (race.PLEASE_IGNORE_FIRST_LAP==1){
      jcIgnoreFirstLap.setSelected(true);
    }else{
      jcIgnoreFirstLap.setSelected(false);
    }   
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
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jtRaceName = new javax.swing.JTextField();
    jtLapsCount = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jtMinLapTime = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jtMaxRaceTime = new javax.swing.JTextField();
    jcIgnoreFirstLap = new javax.swing.JCheckBox();
    jLSeconds = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    edJudge = new javax.swing.JTextField();
    edSecretary = new javax.swing.JTextField();
    chPostponingStart = new javax.swing.JCheckBox();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();
    butSelect = new javax.swing.JButton();

    setTitle("Race Card");
    setIconImage(MainForm.getWindowsIcon().getImage());
    setResizable(false);

    jLabel1.setText("Race name:");

    jLabel2.setText("Laps:");

    jLabel3.setText("Min lap time (sec):");

    jtMinLapTime.setText("18");

    jLabel4.setText("Max Race Time");

    jtMaxRaceTime.setText("0");

    jcIgnoreFirstLap.setText("Ignore First Lap");
    jcIgnoreFirstLap.setToolTipText("Please ust the flag if your first gate is the first");
    jcIgnoreFirstLap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jcIgnoreFirstLap.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    jcIgnoreFirstLap.setIconTextGap(22);
    jcIgnoreFirstLap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jcIgnoreFirstLapActionPerformed(evt);
      }
    });

    jLSeconds.setText("seconds");

    jLabel5.setText("Judge");

    jLabel6.setText("Chief Secretary");

    edJudge.setText("edJudge");

    edSecretary.setText("jTextField2");

    chPostponingStart.setText("Postponing Start");
    chPostponingStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    chPostponingStart.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3)
              .addComponent(jLabel4)
              .addComponent(jLabel1)
              .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jtMaxRaceTime, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLSeconds))
              .addComponent(jtMinLapTime, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jtLapsCount, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jtRaceName, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
              .addComponent(jcIgnoreFirstLap, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(chPostponingStart))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel5)
                .addComponent(jLabel6))
              .addGap(32, 32, 32)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(edSecretary, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(edJudge, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addContainerGap(19, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jtRaceName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(jtLapsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jtMaxRaceTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLSeconds))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jtMinLapTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(edJudge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6)
          .addComponent(edSecretary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jcIgnoreFirstLap)
          .addComponent(chPostponingStart))
        .addGap(64, 64, 64))
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

    butSelect.setText("Select w/o Save");
    butSelect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butSelectActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(bSave)
        .addGap(32, 32, 32)
        .addComponent(butSelect)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(bCancel)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addGap(10, 10, 10)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bSave)
          .addComponent(bCancel)
          .addComponent(butSelect))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_bCancelActionPerformed

  private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
    try {      
      try{
        race.COUNT_OF_LAPS = Integer.parseInt(jtLapsCount.getText());
      }catch(Exception e){}
      race.PLEASE_IGNORE_FIRST_LAP = 0;
      if (jcIgnoreFirstLap.isSelected()) race.PLEASE_IGNORE_FIRST_LAP = 1;
      try{
        race.MIN_LAP_TIME = Integer.parseInt(jtMinLapTime.getText());
      }catch(Exception e){}  
      race.RACE_NAME = jtRaceName.getText();  
      
      race.JUDGE = edJudge.getText();
      race.SECRETARY = edSecretary.getText();
      race.POST_START = chPostponingStart.isSelected()?1:0;
      
      race.MAX_RACE_TIME = 0;
      try{
        race.MAX_RACE_TIME = Integer.parseInt(jtMaxRaceTime.getText());
      }catch(Exception e){}  
      
      if (race.RACE_ID!=-1) {
        VS_RACE.dbControl.update(mainForm.con,race);        
        if (mainForm.activeRace!=null && mainForm.activeRace.RACE_ID==race.RACE_ID){
          mainForm.activeRace = race;
        }
      } else { 
        VS_RACE.dbControl.insert(mainForm.con, race);
        mainForm.setActiveRace(race);
      }  
      setVisible(false);
      RaceList.init(mainForm).refreshData();      
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Saving race is error. " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }//GEN-LAST:event_bSaveActionPerformed

  private void jcIgnoreFirstLapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcIgnoreFirstLapActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_jcIgnoreFirstLapActionPerformed

  private void butSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butSelectActionPerformed
    // TODO add your handling code here:
    //mainForm.setActiveRace(race);
    setVisible(false);
    RaceList.init(mainForm).selectRace();
  }//GEN-LAST:event_butSelectActionPerformed

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
      java.util.logging.Logger.getLogger(RaceControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(RaceControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(RaceControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(RaceControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new RaceControlForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JButton butSelect;
  private javax.swing.JCheckBox chPostponingStart;
  private javax.swing.JTextField edJudge;
  private javax.swing.JTextField edSecretary;
  private javax.swing.JLabel jLSeconds;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JCheckBox jcIgnoreFirstLap;
  private javax.swing.JTextField jtLapsCount;
  private javax.swing.JTextField jtMaxRaceTime;
  private javax.swing.JTextField jtMinLapTime;
  private javax.swing.JTextField jtRaceName;
  // End of variables declaration//GEN-END:variables
}
