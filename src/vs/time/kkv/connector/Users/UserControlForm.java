/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Users;

import KKV.DBControlSqlLite.DBModelTest;
import KKV.Utils.Tools;
import java.awt.Point;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.ImageImplement;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class UserControlForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  boolean flagNew = false;
  int ID = -1;
  VS_USERS usr = null;

  /**
   * Creates new form UserControlForm
   */
  private UserControlForm(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;
  }

  private static UserControlForm form = null;

  public static UserControlForm init(MainForm mainForm, boolean flagNew, int ID) {
    if (form == null) {
      form = new UserControlForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.setVisible(false);
    form.flagNew = flagNew;
    form.ID = ID;   
    form.prepareForm();

    return form;
  }

  public void prepareForm() {

    edRegion.setText("");    
    setTitle("Global User");
    if (ID != -1) {
      try {
        usr = VS_USERS.dbControl.getItem(mainForm.con, "ID=?",ID);        
        setTitle("Global User ["+usr.ID+"]");
        edUser.setText(usr.VS_NAME);
        chEnabledSound.setSelected(usr.VS_SOUND_EFFECT==1);                
      } catch (Exception e) {
        mainForm.error_log.writeFile(e);
        JOptionPane.showMessageDialog(this, "Loading user is error. " + getName().toString(), "Error", JOptionPane.ERROR_MESSAGE);
      }
      edRegion.setText(usr.REGION);
    }else{
       usr = new VS_USERS();
    }
    
    PHOTO.setImage(usr.PHOTO);
    PHOTO.isChaged = false;

    edTransponder2.setText("");
    edTransponder3.setText("");
    
    if (usr.VSID2!=0) edTransponder2.setText(""+usr.VSID2);
    if (usr.VSID3!=0) edTransponder3.setText(""+usr.VSID3);
    
    if (usr.VSID1 != -1) {
      edTransponder.setText("" +usr.VSID1);      
    } else {
      edTransponder.setText("");
      edUser.setText("");
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
    edTransponder = new javax.swing.JTextField();
    edUser = new javax.swing.JTextField();
    butUseLastTransponderID = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    edTransponder2 = new javax.swing.JTextField();
    edTransponder3 = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    edUserSecondName = new javax.swing.JTextField();
    edUserFirstName = new javax.swing.JTextField();
    PHOTO = new vs.time.kkv.connector.MainlPannels.ImageImplement();
    jLabel7 = new javax.swing.JLabel();
    edRegion = new javax.swing.JTextField();
    chEnabledSound = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();

    setTitle("Pilot Card");
    setIconImage(MainForm.getWindowsIcon().getImage());
    setResizable(false);

    jLabel1.setText("Transponder ID:");

    jLabel2.setText("OSD name:");

    edTransponder.setText("0000");

    butUseLastTransponderID.setText("Use the last ID");
    butUseLastTransponderID.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butUseLastTransponderIDActionPerformed(evt);
      }
    });

    jLabel3.setText("Transponder ID3:");

    jLabel4.setText("Transponder ID2:");

    edTransponder2.setText("0000");

    edTransponder3.setText("0000");

    jLabel5.setText("First Name");

    jLabel6.setText("Second Name");

    PHOTO.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    PHOTO.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PHOTOMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout PHOTOLayout = new javax.swing.GroupLayout(PHOTO);
    PHOTO.setLayout(PHOTOLayout);
    PHOTOLayout.setHorizontalGroup(
      PHOTOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 130, Short.MAX_VALUE)
    );
    PHOTOLayout.setVerticalGroup(
      PHOTOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 125, Short.MAX_VALUE)
    );

    jLabel7.setText("Region");

    chEnabledSound.setText("sound");
    chEnabledSound.setToolTipText("");
    chEnabledSound.setAutoscrolls(true);

    jButton1.setText("Speek Name");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(edTransponder3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(edTransponder2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addComponent(edTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butUseLastTransponderID)
                .addGap(0, 0, Short.MAX_VALUE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel2)
                  .addComponent(jLabel5)
                  .addComponent(jLabel6)
                  .addComponent(jLabel7))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(edRegion)
                  .addComponent(edUserFirstName)
                  .addComponent(edUserSecondName)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, Short.MAX_VALUE)
                    .addComponent(PHOTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
        .addContainerGap())
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(41, 41, 41)
        .addComponent(chEnabledSound, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel1)
              .addComponent(edTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(butUseLastTransponderID))
            .addGap(4, 4, 4)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(edTransponder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel4))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel3)
              .addComponent(edTransponder3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel2)
              .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(PHOTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(edUserFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6)
          .addComponent(edUserSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(edRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chEnabledSound)
          .addComponent(jButton1))
        .addContainerGap())
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
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(3, 3, 3))
    );

    getAccessibleContext().setAccessibleDescription("");

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_bCancelActionPerformed

  private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
    try {                        
      usr.VSID1 = Integer.parseInt(edTransponder.getText());
      usr.VSID2 = Integer.parseInt(edTransponder3.getText());
      usr.VSID3= Integer.parseInt(edTransponder3.getText());
      
      usr.FIRST_NAME = edUserFirstName.getText();
      usr.SECOND_NAME = edUserSecondName.getText();            
      
      usr.VS_NAME = edUser.getText();
      usr.VS_NAME_UPPER = edUser.getText().toUpperCase();
      usr.VS_SOUND_EFFECT = chEnabledSound.isSelected()?1:0;
      usr.REGION = edRegion.getText();
      boolean isNew = false;
      if (usr.ID!=-1) {
        VS_USERS.dbControl.update(mainForm.con,usr);        
      } else { 
        isNew  =true;
        VS_USERS.dbControl.insert(mainForm.con, usr);
      }        
      setVisible(false);
      
      // PHOTO           
      if (PHOTO.isChaged){                        
        if (!isNew && !usr.PHOTO.equalsIgnoreCase("")) {
          new File(usr.PHOTO).delete();                    
        }        
        if (PHOTO.imgFileName==null || PHOTO.imgFileName.equals("")){
          usr.PHOTO = "";
        }else{
          String fileName = usr.PHOTO;
          if (isNew || fileName==null ||fileName.equalsIgnoreCase("")){
            fileName = VS_REGISTRATION.PHOTO_PATH+"pilot_"+usr.ID+"."+ FilenameUtils.getExtension(PHOTO.imgFileName);
          }
          new File(VS_REGISTRATION.PHOTO_PATH).mkdirs();
          //FileUtils.copyFile(new File(PHOTO.imgFileName), new File(fileName));
          ImageImplement.savePhotoAndResize(PHOTO.imgFileName,fileName);
        }   
        VS_USERS.dbControl.update(mainForm.con,usr);
      }
      
      
      
      UserList.init(mainForm,null).refreshData();
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      //JOptionPane.showMessageDialog(this, "Transponder is used. Please change Transponder Id.");
      int res = JOptionPane.showConfirmDialog(this, "Do you want to rewrite?", "Transponder is used.", JOptionPane.YES_NO_OPTION);
      if (res==JOptionPane.YES_OPTION){
        try{
          VS_USERS.dbControl.delete(mainForm.con, "VSID=?", usr.VSID1);
          bSaveActionPerformed(evt);
        }catch(Exception e1){
        }          
      }
      //JOptionPane.showMessageDialog(this, "Saving user is error. " + getName().toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }//GEN-LAST:event_bSaveActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().msg(edUser.getText()));
  }//GEN-LAST:event_jButton1ActionPerformed

  private void butUseLastTransponderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butUseLastTransponderIDActionPerformed
    // TODO add your handling code here:
    if (mainForm.lastTranponderID!=-1){
      edTransponder.setText(""+mainForm.lastTranponderID);
    }
  }//GEN-LAST:event_butUseLastTransponderIDActionPerformed

  private void PHOTOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PHOTOMouseClicked
    // TODO add your handling code here:
    if (evt.getClickCount() >= 2) {
      JFileChooser fileopen = new JFileChooser();
      fileopen.setAcceptAllFileFilterUsed(false);
      fileopen.setFileFilter(PHOTO.picFilter);
      int ret = fileopen.showDialog(this, "�������� ���� � �����������");
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
      java.util.logging.Logger.getLogger(UserControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(UserControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(UserControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(UserControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new UserControlForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private vs.time.kkv.connector.MainlPannels.ImageImplement PHOTO;
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JButton butUseLastTransponderID;
  private javax.swing.JCheckBox chEnabledSound;
  private javax.swing.JTextField edRegion;
  private javax.swing.JTextField edTransponder;
  private javax.swing.JTextField edTransponder2;
  private javax.swing.JTextField edTransponder3;
  private javax.swing.JTextField edUser;
  private javax.swing.JTextField edUserFirstName;
  private javax.swing.JTextField edUserSecondName;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  // End of variables declaration//GEN-END:variables
}
