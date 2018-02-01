/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.DBModelTest;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_SETTING;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class StageTabTreeEditForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  int tabID = -1;
  StageTab stageTab = null;
  VS_STAGE_GROUP group = null;
  VS_STAGE_GROUPS user = null;
  //VS_RACE race = null;

  /**
   * Creates new form UserControlForm
   */
  private StageTabTreeEditForm(MainForm mainForm) {
    this.mainForm = mainForm;
    initComponents();
  }

  private static StageTabTreeEditForm form = null;

  public static StageTabTreeEditForm init(MainForm mainForm, StageTab stageTab, VS_STAGE_GROUP group, VS_STAGE_GROUPS user) {
    if (form == null) {
      form = new StageTabTreeEditForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.setVisible(false);
    form.stageTab = stageTab;
    form.group = group;
    form.user = user;
    form.prepareForm();

    return form;
  }
  
  public void prepareForm() {
    try{
      List<VS_REGISTRATION> users = VS_REGISTRATION.dbControl.getList(mainForm.con,"VS_RACE_ID=? ORDER BY VS_USER_NAME",stageTab.stage.RACE_ID);      
      VS_REGISTRATION[] st_users = new VS_REGISTRATION[users.size()];
      int sel_index = 0;
      int index = 0;
      for (VS_REGISTRATION reg_user : users){
        jcbPilots.addItem(reg_user);
        if (user!=null && user.PILOT.equals(reg_user.VS_USER_NAME)){
          sel_index = index;
        }
        index++;
      }
      st_users = users.toArray(st_users);
      jcbPilots.setModel( new javax.swing.DefaultComboBoxModel<VS_REGISTRATION>(st_users)  );
      jcbPilots.setSelectedIndex(sel_index);
      
      VS_STAGE_GROUP[] groups = new VS_STAGE_GROUP[stageTab.stage.groups.size()];
      index = 0;
      sel_index = 0;
      for(Integer gtIndex: stageTab.stage.groups.keySet()){
        groups[index] = stageTab.stage.groups.get(gtIndex);
        if (group!=null && groups[index].GROUP_INDEX==group.GROUP_INDEX){
          sel_index = index;
        }
        index++;
      }
      jcbGroups.setModel( new javax.swing.DefaultComboBoxModel<VS_STAGE_GROUP>(groups));
      jcbGroups.setSelectedIndex(sel_index);
          
      if (user!=null) {
        jcbChannel1.setSelectedItem(user.CHANNEL);
        //jcbPilots.setEnabled(false);
      }
      
    }catch(Exception e){
      mainForm.toLog(e);
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
    Caption = new javax.swing.JLabel();
    jcbChannel1 = new javax.swing.JComboBox();
    jLabel1 = new javax.swing.JLabel();
    jcbGroups = new javax.swing.JComboBox<>();
    jcbPilots = new javax.swing.JComboBox<>();
    jlChannel1 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();

    setTitle("Group control");
    setResizable(false);

    Caption.setText("Pilot:");
    Caption.setToolTipText("");

    jcbChannel1.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jLabel1.setText("Group");

    jlChannel1.setText("Channel:");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1)
          .addComponent(Caption)
          .addComponent(jlChannel1))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jcbPilots, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbGroups, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jcbGroups, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(6, 6, 6)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Caption)
          .addComponent(jcbPilots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jlChannel1))
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
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bSave)
          .addComponent(bCancel))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
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
      if (group!=null && user!=null){                
        VS_STAGE_GROUPS edit = new VS_STAGE_GROUPS();
        edit.GID = user.GID;
        edit.STAGE_ID=user.STAGE_ID;
        edit.TRANSPONDER = user.TRANSPONDER;
        edit.CHANNEL = (String)jcbChannel1.getSelectedItem();
        edit.GROUP_NUM = ((VS_STAGE_GROUP)jcbGroups.getSelectedItem()).GROUP_NUM;
        edit.NUM_IN_GROUP = user.NUM_IN_GROUP;
        VS_REGISTRATION reg_user = (VS_REGISTRATION)jcbPilots.getSelectedItem();
        edit.PILOT = reg_user.VS_USER_NAME;
        edit.TRANSPONDER = reg_user.VS_TRANSPONDER;
                
        if (!StageTabTreeTransferHandler.canBeNewUserInserted(stageTab.mainForm.con, edit)){
          JOptionPane.showMessageDialog(stageTab.mainForm, "Group " + edit.GROUP_NUM + " contains " + edit.PILOT +"(" + edit.TRANSPONDER+")", "I cann't do it...", JOptionPane.INFORMATION_MESSAGE);        
          return;
        }
        
        if (edit.GROUP_NUM!=user.GROUP_NUM){
          edit.NUM_IN_GROUP = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, edit.STAGE_ID, edit.GROUP_NUM);        
        }else{
          if (group.users.indexOf(user)>=0)
            group.users.set(group.users.indexOf(user), edit);
        }
        edit.dbControl.update(mainForm.con, edit);
        // refresh laps time
        List<VS_RACE_LAP> laps = VS_RACE_LAP.dbControl.getList(stageTab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? AND TRANSPONDER_ID=?", stageTab.stage.RACE_ID,stageTab.stage.ID,user.GROUP_NUM,user.TRANSPONDER);
        for (VS_RACE_LAP lap: laps){
          lap.GROUP_NUM = edit.GROUP_NUM;
          lap.TRANSPONDER_ID = edit.TRANSPONDER;
          VS_RACE_LAP.dbControl.update(stageTab.mainForm.con, lap);
        }
        //VS_RACE_LAP.dbControl.execSql(stageTab.mainForm.con, "UPDATE "+VS_RACE_LAP.dbControl.tableName+" SET GROUP_NUM=? AND TRANSPONDER_ID=? WHERE RACE_ID=? and STAGE_ID=? and GROUP_NUM=? AND TRANSPONDER_ID=?", edit.GROUP_NUM,edit.);
        
        stageTab.refreshData(true);
        setVisible(false);
      }
      if (group!=null && user==null){
        VS_STAGE_GROUPS edit = new VS_STAGE_GROUPS();
        edit.GID = -1;
        edit.STAGE_ID=stageTab.stage.ID;
        edit.CHANNEL = (String)jcbChannel1.getSelectedItem();
        edit.GROUP_NUM = ((VS_STAGE_GROUP)jcbGroups.getSelectedItem()).GROUP_NUM;
        VS_REGISTRATION reg_user = (VS_REGISTRATION)jcbPilots.getSelectedItem();                
        edit.PILOT = reg_user.VS_USER_NAME;
        edit.TRANSPONDER = reg_user.VS_TRANSPONDER;
        
        if (!StageTabTreeTransferHandler.canBeNewUserInserted(stageTab.mainForm.con, edit)){
          JOptionPane.showMessageDialog(stageTab.mainForm, "Group " + edit.GROUP_NUM + " contains " + edit.PILOT +"(" + edit.TRANSPONDER+")", "I cann't do it...", JOptionPane.INFORMATION_MESSAGE);        
          return;
        }
        
        edit.NUM_IN_GROUP = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, edit.STAGE_ID, edit.GROUP_NUM);                
        edit.dbControl.insert(mainForm.con, edit);        
        stageTab.refreshData(true);
        setVisible(false);
      }
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Saving race is error. " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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
      java.util.logging.Logger.getLogger(StageTabTreeEditForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(StageTabTreeEditForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(StageTabTreeEditForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(StageTabTreeEditForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new StageTabTreeEditForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Caption;
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox jcbChannel1;
  private javax.swing.JComboBox<VS_STAGE_GROUP> jcbGroups;
  private javax.swing.JComboBox<VS_REGISTRATION> jcbPilots;
  private javax.swing.JLabel jlChannel1;
  // End of variables declaration//GEN-END:variables
}
