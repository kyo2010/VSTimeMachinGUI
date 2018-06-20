/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.RegistrationListImport;

import KKV.Utils.Tools;
import java.awt.Cursor;
import vs.time.kkv.connector.web.RaceHttpServer;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.FPVSport;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.IRegSite;
import vs.time.kkv.connector.MainlPannels.RegistrationTab;
import vs.time.kkv.connector.Utils.TTS.TextToSpeachFactory;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_SETTING;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RegistrationImportForm extends javax.swing.JFrame {

  RegistrationTab regTab = null;

  static List<IRegSite> sites = new ArrayList<>();

  public static IRegSite getSite(String name) {
    for (IRegSite site : sites) {
      if (site.getSystemName().equalsIgnoreCase(name)) {
        return site;
      }
    }
    return null;
  }
  
  static{
    sites.add(new FPVSport());
  }

  public final String PLEASE_SELECT_WEB_SYSTEM = "none";

  /**
   * Creates new form WLANSetting
   */
  public RegistrationImportForm(RegistrationTab regTab) {
    initComponents();
    this.regTab = regTab;
    setVisible(false);    

    String[] list = new String[sites.size() + 1];
    list[0] = PLEASE_SELECT_WEB_SYSTEM;
    int index = 1;
    for (IRegSite site : sites) {
      list[index] = site.getSystemName();
      index++;
    }
    jcbSites.setModel(new javax.swing.DefaultComboBoxModel(list));
    jcbRaces.setModel(new javax.swing.DefaultComboBoxModel(new String[0]));

    jcbSites.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        //JOptionPane.showMessageDialog(null, "sel:"+jcbSites.getSelectedItem()); 
        siteSelected();
      }
    });
  }

  static RegistrationImportForm singelton = null;

  public static RegistrationImportForm init(RegistrationTab regTab) {
    if (singelton == null) {
      singelton = new RegistrationImportForm(regTab);
    }

    RegistrationImportForm th = singelton;
    if (regTab != null) {
      regTab.mainForm.setFormOnCenter(th);
    }

    singelton.prepare();

    return singelton;
  }

  public String setAutoLoadEvent = null;

  public void prepare() {
    jButOk.setEnabled(true);
    jeAutzCode.setText("");
    if (regTab.mainForm.activeRace.WEB_SYSTEM_SID != null && !regTab.mainForm.activeRace.WEB_SYSTEM_SID.equals("")) {
      setAutoLoadEvent = regTab.mainForm.activeRace.WEB_RACE_ID;
      jcbSites.setSelectedItem(regTab.mainForm.activeRace.WEB_SYSTEM_SID);
      siteSelected();
    }
    automaticUpload.setSelected(regTab.mainForm.activeRace.AUTO_WEB_UPDATE==1?true:false);
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
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jcbSites = new javax.swing.JComboBox<>();
    jcbRaces = new javax.swing.JComboBox<>();
    chbUpdatePhoto = new javax.swing.JCheckBox();
    jLabel1 = new javax.swing.JLabel();
    jeAutzCode = new vs.time.kkv.connector.Utils.KKVTreeTable.KKVTextField();
    automaticUpload = new javax.swing.JCheckBox();
    jPanel2 = new javax.swing.JPanel();
    jButOk = new javax.swing.JButton();
    butLink = new javax.swing.JButton();
    jButCancel = new javax.swing.JButton();

    setTitle("Pilots Import from Web");
    setIconImage(MainForm.getWindowsIcon().getImage());
    setResizable(false);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 7, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 65, Short.MAX_VALUE)
    );

    jLabel3.setText("Web Resource");

    jLabel4.setText("Race");

    jcbSites.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jcbSites.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jcbSitesItemStateChanged(evt);
      }
    });
    jcbSites.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jcbSitesActionPerformed(evt);
      }
    });

    jcbRaces.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    chbUpdatePhoto.setSelected(true);
    chbUpdatePhoto.setText("Refresh PHOTO from Web");
    chbUpdatePhoto.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

    jLabel1.setText("Authorization Code");

    automaticUpload.setText("Automatic upload to Web");
    automaticUpload.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    automaticUpload.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

    jButOk.setText("Ok");
    jButOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButOkActionPerformed(evt);
      }
    });

    butLink.setText("Create link to Web Race");
    butLink.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butLinkActionPerformed(evt);
      }
    });

    jButCancel.setText("Cancel");
    jButCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButOk, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(174, 174, 174)
        .addComponent(butLink, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
        .addGap(181, 181, 181)
        .addComponent(jButCancel)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButOk)
          .addComponent(butLink)
          .addComponent(jButCancel))
        .addContainerGap(22, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3)
              .addComponent(jLabel4))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jcbSites, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jcbRaces, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jeAutzCode)
            .addGap(14, 14, 14))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(chbUpdatePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(automaticUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jcbSites, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jcbRaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(chbUpdatePhoto)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jeAutzCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(automaticUpload)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_jButCancelActionPerformed

  private void jButOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButOkActionPerformed
    jButOk.setEnabled(false);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    if (PLEASE_SELECT_WEB_SYSTEM.equalsIgnoreCase(jcbSites.getSelectedItem().toString())) {
    } else {
      try {
        IRegSite site = getSite(jcbSites.getSelectedItem().toString());
        if (site != null) {
          site.load();
          List<VS_RACE> races = site.getRaces();
          int count_export_pilots = 0;
          int count_updated_photos_pilots = 0;
          VS_RACE importRace = null;
          for (VS_RACE race : races) {
            if (race.RACE_NAME.trim().equalsIgnoreCase(jcbRaces.getSelectedItem().toString())) {
              importRace = race;
              if (race.users != null) {
                Connection con = regTab.mainForm.con;
                for (VS_REGISTRATION pilot : race.users) {

                  pilot.VS_USER_NAME = pilot.VS_USER_NAME.trim();
                  pilot.FIRST_NAME = pilot.FIRST_NAME.trim();
                  pilot.SECOND_NAME = pilot.SECOND_NAME.trim();

                  Map<String, VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getMap(con, "VS_USER_NAME", "VS_RACE_ID=?", regTab.mainForm.activeRace.RACE_ID);

                  /*if (pilot.VS_USER_NAME.trim().equalsIgnoreCase("Daniel_orlov")){
                    int y = 0;
                  }*/
                  
                  VS_REGISTRATION reg = null;
                  for (VS_REGISTRATION reg1 : regs.values()) {
                    if (reg1.VS_USER_NAME.trim().equalsIgnoreCase(pilot.VS_USER_NAME.trim())) {
                      reg = reg1;
                      break;
                    }
                    try {
                      if (!"".equalsIgnoreCase(reg1.WEB_SID) && reg1.WEB_SID.trim().equalsIgnoreCase(pilot.WEB_SID.trim())) {
                        reg = reg1;
                        break;
                      }
                    } catch (Exception ein) {
                    }
                  }

                  //VS_REGISTRATION reg = regs.get(pilot.VS_USER_NAME);
                  if (reg == null) {
                    try {
                      pilot.VS_RACE_ID = regTab.mainForm.activeRace.RACE_ID;
                      pilot.NUM = VS_REGISTRATION.maxNum(con, pilot.VS_RACE_ID) + 1;
                      if (pilot.VS_TRANS1 != 0) {
                        for (VS_REGISTRATION reg1 : regs.values()) {
                          if ( (reg1.VS_TRANS1!=0 && reg1.VS_TRANS1 == pilot.VS_TRANS1) || reg1.VS_TRANS2 == pilot.VS_TRANS1
                                  || reg1.VS_TRANS3 == pilot.VS_TRANS1) {
                            JOptionPane.showMessageDialog(this, "Transponder for pilot " + pilot.VS_USER_NAME + " is duplocated.\nPilot " + reg1.VS_USER_NAME + " has the same transponder", "Information", JOptionPane.INFORMATION_MESSAGE);
                            pilot.VS_TRANS1 = 0;
                          }
                        }
                      }
                      if (pilot.PHOTO != null && !pilot.PHOTO.equals("")) {
                        pilot.PHOTO = site.getImageFromWeb(pilot.PHOTO);
                        count_updated_photos_pilots++;
                      }

                      pilot.IS_ACTIVE=0;
                      VS_REGISTRATION.dbControl.insert(con, pilot);
                      VS_USERS global_user = VS_REGISTRATION.updateGlobalUserPHOTO(con, pilot);

                      count_export_pilots++;
                    } catch (Exception e) {

                    }
                  } else {
                    if (pilot.VS_TRANS1 != 0
                            && (reg.VS_TRANS1 != pilot.VS_TRANS1 || reg.VS_TRANS2 != pilot.VS_TRANS2 || reg.VS_TRANS3 != pilot.VS_TRANS3)) {
                      /*pilot.ID = reg.ID;
                      pilot.VS_RACE_ID = reg.VS_RACE_ID;
                      if (pilot.PHOTO != null && !pilot.PHOTO.equals("") && !reg.WEB_PHOTO_URL.equals(pilot.WEB_PHOTO_URL)) {                        
                      }else{
                        pilot.PHOTO = reg.PHOTO;
                        pilot.PHOTO = reg.PHOTO;
                        VS_REGISTRATION.dbControl.update(con, pilot);
                      }*/
                      reg.VS_TRANS1 = pilot.VS_TRANS1;
                      reg.VS_TRANS2 = pilot.VS_TRANS2;
                      reg.VS_TRANS3 = pilot.VS_TRANS3;
                      VS_REGISTRATION.dbControl.update(con, reg);
                    }
                    if (!pilot.FIRST_NAME.equals(reg.FIRST_NAME) || !pilot.SECOND_NAME.equals(reg.SECOND_NAME) || 
                           !pilot.REGION.equals(reg.REGION) || !pilot.FAI.equalsIgnoreCase(reg.FAI) ){
                      if (!pilot.FIRST_NAME.equals("")) reg.FIRST_NAME = pilot.FIRST_NAME;
                      if (!pilot.SECOND_NAME.equals("")) reg.SECOND_NAME = pilot.SECOND_NAME;
                      if (!pilot.REGION.equals(""))reg.REGION = pilot.REGION;
                      if (!pilot.FAI.equals("")) reg.FAI = pilot.FAI;
                      VS_REGISTRATION.dbControl.update(con, reg);
                    }                    
                    if (chbUpdatePhoto.isSelected()) {
                      //if (pilot.PHOTO != null && !pilot.PHOTO.equals("") && !reg.WEB_PHOTO_URL.equals(pilot.WEB_PHOTO_URL)) {
                      pilot.PHOTO = site.getImageFromWeb(pilot.PHOTO);
                      reg.PHOTO = pilot.PHOTO;
                      reg.WEB_PHOTO_URL = pilot.WEB_PHOTO_URL;
                      count_updated_photos_pilots++;
                      VS_REGISTRATION.dbControl.update(con, reg);
                      VS_REGISTRATION.updateGlobalUserPHOTO(con, reg);
                      //}

                    }
                  }
                }
              }
              break;
            }
          }
          regTab.mainForm.activeRace.WEB_SYSTEM_SID = jcbSites.getSelectedItem().toString();
          regTab.mainForm.activeRace.WEB_SYSTEM_CAPTION = jcbRaces.getSelectedItem().toString();          
          if (importRace!=null){
            regTab.mainForm.activeRace.WEB_RACE_ID = ""+importRace.RACE_ID;
          }  
          
          regTab.mainForm.activeRace.AUTO_WEB_UPDATE = 0;
          if (automaticUpload.isSelected()){
            regTab.mainForm.activeRace.AUTO_WEB_UPDATE = 1;
          }
                
          VS_RACE.dbControl.update(regTab.mainForm.con, regTab.mainForm.activeRace);
          Tools.setPreference("AUTORIZE_CODE_"+site.REG_SITE_NAME, jeAutzCode.getText());
          
          regTab.refreshData();
          JOptionPane.showMessageDialog(this, "Export pilots : " + count_export_pilots + "\n"
                  + count_updated_photos_pilots + " photos were updated", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
      } catch (Exception e) {
        MainForm._toLog(e);
      }finally{
        setCursor(Cursor.getDefaultCursor());
      }
    }
    jButOk.setEnabled(true);
    setVisible(false);
  }//GEN-LAST:event_jButOkActionPerformed

  private void jcbSitesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbSitesItemStateChanged
    // TODO add your handling code here:

  }//GEN-LAST:event_jcbSitesItemStateChanged

  private void butLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butLinkActionPerformed
    // TODO add your handling code here:
    try{
      IRegSite site = getSite(jcbSites.getSelectedItem().toString());
        if (site != null) {
          site.load();
          List<VS_RACE> races = site.getRaces();
          for (VS_RACE race : races) {
            if (race.RACE_NAME.trim().equalsIgnoreCase(jcbRaces.getSelectedItem().toString())) {
              if (regTab.mainForm.activeRace!=null){
                regTab.mainForm.activeRace.WEB_RACE_ID = ""+race.RACE_ID;
                regTab.mainForm.activeRace.WEB_SYSTEM_SID = site.REG_SITE_NAME;
                regTab.mainForm.activeRace.WEB_SYSTEM_CAPTION = ""+race.RACE_NAME;
                regTab.mainForm.activeRace.AUTO_WEB_UPDATE = 0;
                if (automaticUpload.isSelected()){
                  regTab.mainForm.activeRace.AUTO_WEB_UPDATE = 1;
                }
                VS_RACE.dbControl.update(regTab.mainForm.con, regTab.mainForm.activeRace);
                break;
              }
            }
          }
        }        
        String auth = jeAutzCode.getText();
        Tools.setPreference("AUTORIZE_CODE_"+site.REG_SITE_NAME, auth);
        setVisible(false);
    }catch(Exception e){
       JOptionPane.showMessageDialog(null, "Create Link to Web Race is Error. " + e.getMessage());
    }    
  }//GEN-LAST:event_butLinkActionPerformed

  private void jcbSitesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbSitesActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_jcbSitesActionPerformed

  public void siteSelected() {
    //jcbRaces.setModel(new javax.swing.DefaultComboBoxModel(new String[0]));
    if (PLEASE_SELECT_WEB_SYSTEM.equalsIgnoreCase(jcbSites.getSelectedItem().toString())) {
    } else {
      try {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        IRegSite site = getSite(jcbSites.getSelectedItem().toString());
        if (site != null) {
          site.load();
          List<VS_RACE> races = site.getRaces();
          String[] list = new String[races.size()];
          int index = 0;
          int selIndex = 0;
          for (VS_RACE race : races) {
            list[index] = race.RACE_NAME.trim();
            if (setAutoLoadEvent != null && setAutoLoadEvent.equals(""+race.RACE_ID)) {
              selIndex = index;
            }
            index++;
          }
          jcbRaces.setModel(new javax.swing.DefaultComboBoxModel(list));
          if (selIndex != 0) {
            jcbRaces.setSelectedIndex(selIndex);
          }
          
          String auth = Tools.getPreference("AUTORIZE_CODE_"+site.REG_SITE_NAME);
          jeAutzCode.setText(auth);
          
          //jcbRaces.setSelectedItem(setAutoLoadEvent);
          /*  if (setAutoLoadEvent!=null){
            jcbRaces.setSelectedItem(setAutoLoadEvent);
          }
          setAutoLoadEvent = null;*/
        }
      } catch (Exception e) {
        MainForm._toLog(e);
        jcbRaces.setModel(new javax.swing.DefaultComboBoxModel(new String[0]));
      } finally{
         this.setCursor(Cursor.getDefaultCursor());
      }
    }
  }

  public void buttnCaptionRefresh() {

  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox automaticUpload;
  private javax.swing.JButton butLink;
  private javax.swing.JCheckBox chbUpdatePhoto;
  private javax.swing.JButton jButCancel;
  private javax.swing.JButton jButOk;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox<String> jcbRaces;
  private javax.swing.JComboBox<String> jcbSites;
  private javax.swing.JTextField jeAutzCode;
  // End of variables declaration//GEN-END:variables
}
