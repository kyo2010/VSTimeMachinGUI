/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.DBModelTest;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.Race.RaceList;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RegisterPilotlForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  long reg_Id = -1;
  VS_REGISTRATION usr = null;

  /**
   * Creates new form UserControlForm
   */
  private RegisterPilotlForm(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;
  }

  private static RegisterPilotlForm form = null;

  public static RegisterPilotlForm init(MainForm mainForm, long reg_Id) {
    if (form == null) {
      form = new RegisterPilotlForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.reg_Id = reg_Id;
    form.setVisible(false);
    form.prepareForm();

    return form;
  }

  public void prepareForm() {

    if (reg_Id != -1) {
      try {
        usr = VS_REGISTRATION.dbControl.getItem(mainForm.con, "ID=?", reg_Id);
        edUser.setText(usr.VS_USER_NAME);
        chEnabledSound.setSelected(usr.VS_SOUND_EFFECT == 1);
        
        VS_USERS global_user = VS_USERS.dbControl.getItem(mainForm.con, "VS_NAME=?", usr.VS_USER_NAME);
        if (global_user!=null && global_user.PHOTO!=null && !global_user.PHOTO.equals("")){
          usr.PHOTO = global_user.PHOTO;
        }        
      } catch (Exception e) {
        mainForm.error_log.writeFile(e);
        JOptionPane.showMessageDialog(this, "Loading user is error. " + getName().toString(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      usr = new VS_REGISTRATION();
    }        
    
    edFAI.setText(usr.FAI);
    PHOTO.setImage(usr.PHOTO);
    PHOTO.isChaged = false;       

    if (reg_Id != -1) {
      edTransponder.setText("" + usr.VS_TRANS1);
      edTransponder2.setText("" + usr.VS_TRANS2);
      edTransponder3.setText("" + usr.VS_TRANS3);
      if (usr.VS_TRANS2 == 0) {
        edTransponder2.setText("");
      }
      if (usr.VS_TRANS3 == 0) {
        edTransponder3.setText("");
      }
      jcbPilotType.setSelectedIndex(usr.PILOT_TYPE);
      edFirstName.setText(usr.FIRST_NAME);
      edSecondName.setText(usr.SECOND_NAME);
      edRegion.setText(usr.REGION);
    } else {
      edTransponder.setText("");
      edTransponder2.setText("");
      edTransponder3.setText("");
      edUser.setText("");
      edFirstName.setText("");
      edSecondName.setText("");
      edRegion.setText("");
      chEnabledSound.setSelected(true);
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
    edTransponder = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    edUser = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    butPilots = new javax.swing.JButton();
    butUsedLastTransponderID = new javax.swing.JButton();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    edTransponder2 = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    edTransponder3 = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    edFirstName = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    edSecondName = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    PHOTO = new vs.time.kkv.connector.MainlPannels.ImageImplement();
    jLabel8 = new javax.swing.JLabel();
    edRegion = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    jLabel9 = new javax.swing.JLabel();
    edFAI = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jcbPilotType = new javax.swing.JComboBox();
    chEnabledSound = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();

    setTitle("Pilot Card");
    setIconImage(MainForm.getWindowsIcon().getImage());
    setResizable(false);

    jLabel1.setText("Transponder ID:");

    jLabel2.setText("OSD name:");

    edTransponder.setText("0000");

    butPilots.setText("...");
    butPilots.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butPilotsActionPerformed(evt);
      }
    });

    butUsedLastTransponderID.setText("Last Transpnder");
    butUsedLastTransponderID.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butUsedLastTransponderIDActionPerformed(evt);
      }
    });

    jLabel4.setText("Transponder ID:");

    jLabel5.setText("Transponder ID:");

    edTransponder2.setText("0000");

    edTransponder3.setText("0000");

    jLabel6.setText("First Name:");

    jLabel7.setText("Second Name:");

    PHOTO.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    PHOTO.setToolTipText("");
    PHOTO.setAutoscrolls(true);
    PHOTO.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PHOTOMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout PHOTOLayout = new javax.swing.GroupLayout(PHOTO);
    PHOTO.setLayout(PHOTOLayout);
    PHOTOLayout.setHorizontalGroup(
      PHOTOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 140, Short.MAX_VALUE)
    );
    PHOTOLayout.setVerticalGroup(
      PHOTOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 143, Short.MAX_VALUE)
    );

    jLabel8.setText("Region:");

    jLabel9.setText("FAI:");

    edFAI.setText("jTextField1");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel6)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(edTransponder2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(edTransponder3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(edTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butUsedLastTransponderID)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
            .addComponent(PHOTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addGap(54, 54, 54)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(edFirstName, javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(edUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butPilots, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel7)
              .addComponent(jLabel8)
              .addComponent(jLabel9))
            .addGap(33, 33, 33)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(edRegion)
              .addComponent(edSecondName)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(edFAI, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jLabel1)
                  .addComponent(edTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(butUsedLastTransponderID)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel4)
              .addComponent(edTransponder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel5)
              .addComponent(edTransponder3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(13, 13, 13)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel2)
              .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(butPilots)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(PHOTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6)
          .addComponent(edFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(edSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel8)
          .addComponent(edRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel9)
          .addComponent(edFAI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    jLabel3.setText("Pilot type:");

    jcbPilotType.setModel(new javax.swing.DefaultComboBoxModel(MainForm.PILOT_TYPES));

    chEnabledSound.setText("sound");
    chEnabledSound.setToolTipText("");
    chEnabledSound.setAutoscrolls(true);

    jButton1.setText("Speek Name");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel3)
        .addGap(33, 33, 33)
        .addComponent(jcbPilotType, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(chEnabledSound)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jcbPilotType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton1)
          .addComponent(chEnabledSound))
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

      VS_REGISTRATION check1 = null;
      VS_REGISTRATION check2 = null;
      VS_REGISTRATION check3 = null;

      // Check for constarin
      if (usr == null) {
        usr = new VS_REGISTRATION();
      }
      usr.VS_TRANS1 = Integer.parseInt(edTransponder.getText());
      usr.VS_TRANS2 = 0;
      usr.VS_TRANS3 = 0;
      try {
        usr.VS_TRANS2 = Integer.parseInt(edTransponder2.getText());
      } catch (Exception ein) {
      }
      try {
        usr.VS_TRANS3 = Integer.parseInt(edTransponder3.getText());
      } catch (Exception ein) {
      }
      usr.VS_RACE_ID = mainForm.activeRace.RACE_ID;
      usr.IS_ACTIVE = 1;
      usr.FAI = edFAI.getText();
      usr.VS_USER_NAME = edUser.getText();
      usr.FIRST_NAME = edFirstName.getText();
      usr.SECOND_NAME = edSecondName.getText();
      usr.VS_SOUND_EFFECT = chEnabledSound.isSelected() ? 1 : 0;
      usr.PILOT_TYPE = jcbPilotType.getSelectedIndex();
      usr.REGION = edRegion.getText();
      boolean isNew = false;
      if (usr.ID != -1) {

        String sql_check = "VS_RACE_ID=? and (VS_TRANSPONDER=? or VS_TRANS2=? or VS_TRANS3=?) and ID<>?";
        check1 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS1, usr.VS_TRANS1, usr.VS_TRANS1, usr.ID);
        if (usr.VS_TRANS2 != 0 && usr.VS_TRANS2 != -1) {
          check2 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS2, usr.VS_TRANS2, usr.VS_TRANS2, usr.ID);
        }
        if (usr.VS_TRANS3 != 0 && usr.VS_TRANS3 != -1) {
          check3 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS3, usr.VS_TRANS3, usr.VS_TRANS3, usr.ID);
        }
        VS_REGISTRATION checkUserName = VS_REGISTRATION.dbControl.getItem(mainForm.con, "VS_RACE_ID=? and VS_USER_NAME=? and ID<>?", usr.VS_RACE_ID, usr.VS_USER_NAME, usr.ID);
        if (checkUserName != null) {
          JOptionPane.showConfirmDialog(this, "Pilot '" + usr.VS_USER_NAME + "' has been registred.\nPlease change the pilot name?", "Pilot has been registred.", JOptionPane.CLOSED_OPTION);
          return;
        }
        if (check1 != null && usr.VS_TRANS1!=0) {
          //JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS1 + "' has been registred.\nPlease change the transponder ID?\nPilot:" + check1.VS_USER_NAME, "Transponder has been registred.", JOptionPane.CLOSED_OPTION);
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS1 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check1.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS1+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check1.VS_TRANS1 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check1);            
          };
          return;
        }
        if (check2 != null && usr.VS_TRANS2!=0) {
          //JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS2 + "' has been registred.\nPlease change the transponder ID?\nPilot:" + check2.VS_USER_NAME, "Transponder has been registred.", JOptionPane.CLOSED_OPTION);
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS2 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check2.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS2+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check2.VS_TRANS2 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check2);            
          };
          return;
        }
        if (check3 != null && usr.VS_TRANS3!=0) {
          //JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS3 + "' has been registred.\nPlease change the transponder ID?\nPilot:" + check3.VS_USER_NAME, "Transponder has been registred.", JOptionPane.CLOSED_OPTION);
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS3 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check3.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS3+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check3.VS_TRANS3 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check3);            
          };
          return;
        }

        VS_REGISTRATION.dbControl.update(mainForm.con, usr);

        /*// add user to all stages
        for (int s_idx=0; s_idx<mainForm.getTabbedPanels().getComponentCount(); s_idx++){
                  
        }*/
      } else {
        isNew = true;
        usr.NUM = VS_REGISTRATION.maxNum(mainForm.con, usr.VS_RACE_ID) + 1;
        String sql_check = "VS_RACE_ID=? and (VS_TRANSPONDER=? or VS_TRANS2=? or VS_TRANS3=?)";
        check1 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS1, usr.VS_TRANS1, usr.VS_TRANS1);
        if (usr.VS_TRANS2 != 0 && usr.VS_TRANS2 != -1) {
          check2 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS2, usr.VS_TRANS2, usr.VS_TRANS2);
        }
        if (usr.VS_TRANS3 != 0 && usr.VS_TRANS3 != -1) {
          check3 = VS_REGISTRATION.dbControl.getItem(mainForm.con, sql_check, usr.VS_RACE_ID, usr.VS_TRANS3, usr.VS_TRANS3, usr.VS_TRANS3);
        }
        VS_REGISTRATION checkUserName = VS_REGISTRATION.dbControl.getItem(mainForm.con, "VS_RACE_ID=? and VS_USER_NAME=?", usr.VS_RACE_ID, usr.VS_USER_NAME);

        if (checkUserName != null) {
          JOptionPane.showConfirmDialog(this, "Pilot '" + usr.VS_USER_NAME + "' has been registred.\nPlease change the pilot name?", "Pilot has been registred.", JOptionPane.CLOSED_OPTION);
          return;
        }
        if (check1 != null) {
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS1 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check1.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS1+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check1.VS_TRANS1 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check1);            
          };
          return;
        }
        if (check2 != null) {
          //JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS2 + "' has been registred.\nPlease change the transponder ID?\nPilot:" + check2.VS_USER_NAME, "Transponder has been registred.", JOptionPane.CLOSED_OPTION);
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS2 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check2.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS2+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check2.VS_TRANS2 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check2);            
          };
          
          return;
        }
        if (check3 != null) {
          //JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS3 + "' has been registred.\nPlease change the transponder ID?\nPilot:" + check3.VS_USER_NAME, "Transponder has been registred.", JOptionPane.CLOSED_OPTION);
          if (JOptionPane.showConfirmDialog(this, "Transponder '" + usr.VS_TRANS3 + "' has been registred.\nDo you want to change the transponder ID?\nPilot:" + check2.VS_USER_NAME, "Transponder has been registred.\nDo You want unlinck '"+usr.VS_TRANS3+"' transponder from other pilot", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
            check3.VS_TRANS3 = 0;
            VS_REGISTRATION.dbControl.update(mainForm.con, check3);            
          };
          return;
        }
        VS_REGISTRATION.dbControl.insert(mainForm.con, usr);

      }
      
      VS_USERS global_user = VS_USERS.dbControl.getItem(mainForm.con, "VS_NAME=?", usr.VS_USER_NAME);
      
      // Creating global user     
      if (global_user==null){
        global_user = new VS_USERS();
        global_user.FIRST_NAME = usr.FIRST_NAME;
        global_user.SECOND_NAME = usr.SECOND_NAME;
        global_user.VS_NAME = usr.VS_USER_NAME;
        global_user.VS_NAME_UPPER = global_user.VS_NAME.toUpperCase();
        global_user.VSID1 =usr.VS_TRANS1;
        global_user.VSID2 =usr.VS_TRANS2;
        global_user.VSID3 =usr.VS_TRANS3;
        global_user.VS_SOUND_EFFECT = usr.VS_SOUND_EFFECT;
        global_user.WEB_SID = usr.WEB_SID;
        global_user.WEB_SYSTEM = usr.WEB_SYSTEM;
        global_user.PHOTO = usr.PHOTO;
        global_user.REGION = usr.REGION;
        global_user.FAI = usr.FAI;
        VS_USERS.dbControl.insert(mainForm.con, global_user);
      }
      global_user.FAI = usr.FAI;
      
      if (global_user!=null && global_user.PHOTO!=null && !global_user.PHOTO.equals("")){
        usr.PHOTO = global_user.PHOTO;
      }           
      
      // PHOTO           
      if (PHOTO.isChaged){                        
        if (!isNew && !usr.PHOTO.equalsIgnoreCase("") && !usr.PHOTO.equalsIgnoreCase(PHOTO.imgFileName)) {
          new File(usr.PHOTO).delete();                    
        }        
        if (PHOTO.imgFileName==null || PHOTO.imgFileName.equals("")){
          usr.PHOTO = "";
          global_user.PHOTO = "";
        }else{
          //String fileName = usr.PHOTO;
          //if (isNew || fileName==null ||fileName.equalsIgnoreCase("")){
            String fileName = VS_REGISTRATION.PHOTO_PATH+"pilot_"+global_user.ID+"."+ FilenameUtils.getExtension(PHOTO.imgFileName);
         // }
          new File(VS_REGISTRATION.PHOTO_PATH).mkdirs();
          //FileUtils.copyFile(new File(PHOTO.imgFileName), new File(fileName));
          ImageImplement.savePhotoAndResize(PHOTO.imgFileName,fileName);
          usr.PHOTO = fileName;
          global_user.PHOTO = fileName;
        }   
        VS_REGISTRATION.dbControl.update(mainForm.con,usr);    
        VS_USERS.dbControl.update(mainForm.con,global_user);       
      }      
      
      setVisible(false);
      if (mainForm != null && mainForm.regForm != null) {
        mainForm.regForm.refreshData();
      }
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
    }
  }//GEN-LAST:event_bSaveActionPerformed

  private void butUsedLastTransponderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butUsedLastTransponderIDActionPerformed
    // TODO add your handling code here:
    if (mainForm.lastTranponderID != -1) {
      edTransponder.setText("" + mainForm.lastTranponderID);
    }
  }//GEN-LAST:event_butUsedLastTransponderIDActionPerformed

  private void butPilotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butPilotsActionPerformed
    //UserList.init(mainForm).setVisible(true);
    UserList.init(mainForm, new UserList.UserListListener() {
      @Override
      public boolean selectUserAndCloseWindow(VS_USERS user) {
        if (user != null) {
          edUser.setText(user.VS_NAME);
          edTransponder.setText("" + user.VSID1);
          edTransponder2.setText("");
          edTransponder3.setText("");
          edFAI.setText(user.FAI);
          if (user.VSID2!=0) edTransponder2.setText("" + user.VSID2);
          if (user.VSID3!=0) edTransponder3.setText("" + user.VSID3);
          edFirstName.setText("" + user.FIRST_NAME);
          edSecondName.setText("" + user.SECOND_NAME);           
          edRegion.setText(user.REGION);
          PHOTO.setImage(user.PHOTO);
          PHOTO.isChaged = false;
        }
        return true;
      }
    }).setVisible(true);
  }//GEN-LAST:event_butPilotsActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().msg(edUser.getText()));
  }//GEN-LAST:event_jButton1ActionPerformed

  private void PHOTOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PHOTOMouseClicked
    // TODO add your handling code here:
    if (evt.getClickCount() >= 2) {
      JFileChooser fileopen = new JFileChooser();
      fileopen.setFileFilter(PHOTO.picFilter);
      fileopen.setAcceptAllFileFilterUsed(false);
      int ret = fileopen.showDialog(this, "Выберите файл с фотографией");
      if (ret == JFileChooser.APPROVE_OPTION) {
        File file = fileopen.getSelectedFile();
        PHOTO.setImage(file.getAbsolutePath());
      }

    }
  }//GEN-LAST:event_PHOTOMouseClicked

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
      java.util.logging.Logger.getLogger(RegisterPilotlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(RegisterPilotlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(RegisterPilotlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(RegisterPilotlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new RegisterPilotlForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private vs.time.kkv.connector.MainlPannels.ImageImplement PHOTO;
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JButton butPilots;
  private javax.swing.JButton butUsedLastTransponderID;
  private javax.swing.JCheckBox chEnabledSound;
  private javax.swing.JTextField edFAI;
  private javax.swing.JTextField edFirstName;
  private javax.swing.JTextField edRegion;
  private javax.swing.JTextField edSecondName;
  public javax.swing.JTextField edTransponder;
  public javax.swing.JTextField edTransponder2;
  public javax.swing.JTextField edTransponder3;
  private javax.swing.JTextField edUser;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox jcbPilotType;
  // End of variables declaration//GEN-END:variables
}
