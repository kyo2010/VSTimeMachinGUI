/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import java.awt.Cursor;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_ESPInfo;
import vs.time.kkv.connector.connection.DroneConnector;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class VSTMParams extends javax.swing.JFrame {

  MainForm mainForm = null;
  VSTM_ESPInfo esp_info = null;
   DroneConnector vsTimeConnector = null;
  
  /**
   * Creates new form VSTMParams
   */
  private VSTMParams(MainForm mainForm) {  
    initComponents();
    this.mainForm = mainForm;
    this.esp_info = esp_info;
    this.setVisible(false);    
    Point p = mainForm.getLocationOnScreen();
    this.setLocation(p.x + mainForm.getWidth() / 2 - this.getSize().width / 2, p.y + mainForm.getHeight() / 2 - this.getSize().height / 2);
    setAlwaysOnTop(true);
    setResizable(false);   
    //panelRouter.setVisible(false);
  }   
  
  private static VSTMParams instance = null;
  public static VSTMParams getInstanse(MainForm mainForm) throws SerialPortException{
    if (instance==null) instance = new VSTMParams(mainForm);
        
    VSTM_ESPInfo esp_info = null;
        
    try{
      mainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));  
      
      for (DroneConnector vsTimeConnector1 : mainForm.droneConnectors) {                
        if (vsTimeConnector1.transport.supportVSTimeMachineExtendMenu()){
         instance.vsTimeConnector = vsTimeConnector1;
         break;
        }
      }      
   
    esp_info = instance.vsTimeConnector.getVSTMParams( instance.vsTimeConnector.baseStationID );           
    if (esp_info==null) throw new Exception("Reading errr. VT Time machine has not been available now");
    instance.esp_info = esp_info;
             
    instance.VSTMID.setText( instance.vsTimeConnector.baseStationID);
    try{
      instance.Sensitivity.setSelectedIndex( instance.vsTimeConnector.sensitivityIndex);
    }catch(Exception e){
      try{
        instance.Sensitivity.setSelectedIndex(0);
      }catch(Exception ex){} 
    }
    instance.CONNECTION_TYPE.setSelectedIndex(esp_info.connectionType);
    instance.SSID.setText(esp_info.SSID);
    instance.SSPWD.setText(esp_info.SSPWD);
    instance.IP1.setText(esp_info.ip1);
    instance.IP2.setText(esp_info.ip2);   
    instance.IP3.setText(esp_info.ip3);
    instance.PORT_RECEIVE.setText(esp_info.port_receiver);
    instance.PORT_SEND.setText(esp_info.port_send);
    
    /*if (esp_info.connectionType==0) {
      instance.panelRouter.setVisible(false);
    }else{
      instance.panelRouter.setVisible(true);
    }*/
      
    instance.setTitle("Time machine "+ instance.vsTimeConnector.baseStationID+" ["+ instance.vsTimeConnector.firmWareVersion+"]");    
    instance.setVisible(true);
    }catch(Exception e){
      //e.printStackTrace();
      instance.esp_info = null;
      JOptionPane.showMessageDialog(mainForm, "Could not get params."+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);  
    }finally{
       mainForm.setCursor(Cursor.getDefaultCursor());
    }
    
    return instance;
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
    jLabel2 = new javax.swing.JLabel();
    VSTMID = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    bTronspnderPower1 = new javax.swing.JButton();
    bTronspnderPower2 = new javax.swing.JButton();
    bTronspnderPower3 = new javax.swing.JButton();
    jPanel3 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    SSID = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    SSPWD = new javax.swing.JTextField();
    panelRouter = new javax.swing.JPanel();
    IP2 = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    IP3 = new javax.swing.JTextField();
    jLabel10 = new javax.swing.JLabel();
    IP4 = new javax.swing.JTextField();
    jLabel11 = new javax.swing.JLabel();
    IP1 = new javax.swing.JTextField();
    PORT_RECEIVE = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    PORT_SEND = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    CONNECTION_TYPE = new javax.swing.JComboBox();
    jLabel3 = new javax.swing.JLabel();
    Sensitivity = new javax.swing.JComboBox();
    jLabel13 = new javax.swing.JLabel();
    jPanel4 = new javax.swing.JPanel();
    saveButton = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();

    setFocusCycleRoot(false);
    setIconImage(MainForm.getWindowsIcon().getImage());
    setModalExclusionType(null);
    setName("VSTMParamsForm"); // NOI18N
    setResizable(false);

    jPanel1.setForeground(new java.awt.Color(51, 51, 255));

    jLabel2.setText("ID");

    VSTMID.setText("jTextField1");

    jLabel4.setText("Transpnder");

    bTronspnderPower1.setText("1 db");
    bTronspnderPower1.setToolTipText("Send power of transponders (Transponders must be located near 1-3 meters) ");
    bTronspnderPower1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bTronspnderPower1ActionPerformed(evt);
      }
    });

    bTronspnderPower2.setText("2 db");
    bTronspnderPower2.setToolTipText("Send power of transponders (Transponders must be located near 1-3 meters) ");
    bTronspnderPower2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bTronspnderPower2ActionPerformed(evt);
      }
    });

    bTronspnderPower3.setText("4 db");
    bTronspnderPower3.setToolTipText("Send power of transponders (Transponders must be located near 1-3 meters) ");
    bTronspnderPower3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bTronspnderPower3ActionPerformed(evt);
      }
    });

    jLabel5.setText("SSID:");

    SSID.setText("jTextField2");

    jLabel6.setText("Password:");

    SSPWD.setText("jTextField3");

    IP2.setText("jTextField1");

    jLabel9.setText(".");

    IP3.setText("jTextField1");

    jLabel10.setText(".");

    IP4.setEditable(false);
    IP4.setText("255");
    IP4.setToolTipText("");

    jLabel11.setText("Receiving port");
    jLabel11.setToolTipText("");

    IP1.setText("jTextField1");

    PORT_RECEIVE.setText("jTextField1");

    jLabel7.setText("IP:");

    jLabel12.setText("Sending port");

    jLabel8.setText(".");

    PORT_SEND.setText("jTextField2");

    javax.swing.GroupLayout panelRouterLayout = new javax.swing.GroupLayout(panelRouter);
    panelRouter.setLayout(panelRouterLayout);
    panelRouterLayout.setHorizontalGroup(
      panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelRouterLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelRouterLayout.createSequentialGroup()
            .addComponent(jLabel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(IP1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(IP2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(IP3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel9))
          .addGroup(panelRouterLayout.createSequentialGroup()
            .addComponent(jLabel11)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(PORT_RECEIVE, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(IP4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(panelRouterLayout.createSequentialGroup()
            .addComponent(jLabel12)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(PORT_SEND, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panelRouterLayout.setVerticalGroup(
      panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelRouterLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(IP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7)
          .addComponent(jLabel8)
          .addComponent(IP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel9)
          .addComponent(IP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(IP4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel10))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(panelRouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel11)
          .addComponent(PORT_RECEIVE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel12)
          .addComponent(PORT_SEND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jLabel1.setText("Type");

    CONNECTION_TYPE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Personal", "Router" }));
    CONNECTION_TYPE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        CONNECTION_TYPEPropertyChange(evt);
      }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panelRouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(CONNECTION_TYPE, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addComponent(SSID, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5))
        .addGap(72, 72, 72)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel6)
          .addComponent(SSPWD, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(jLabel6))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(SSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(SSPWD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(CONNECTION_TYPE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panelRouter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE))
    );

    jLabel3.setText("Sensitivity");

    Sensitivity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<0 dB ", "0 dB", "3 dB", "6 dB", "9 dB", "12 dB", "15 dB", "18 dB", "21 dB", "24 dB", "auto" }));
    Sensitivity.setMinimumSize(new java.awt.Dimension(150, 20));
    Sensitivity.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SensitivityActionPerformed(evt);
      }
    });

    jLabel13.setText("power");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel2)
              .addComponent(jLabel4)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addComponent(jLabel13)))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(bTronspnderPower1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(VSTMID, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(bTronspnderPower2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(bTronspnderPower3)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addComponent(Sensitivity, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(VSTMID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3)
          .addComponent(Sensitivity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(bTronspnderPower2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bTronspnderPower3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bTronspnderPower1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel13)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(25, 25, 25)
        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    VSTMID.getAccessibleContext().setAccessibleName("VSTMID");

    saveButton.setText("Save");
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    jButton2.setText("Cancel");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGap(115, 115, 115)
        .addComponent(saveButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jButton2)
        .addGap(115, 115, 115))
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(saveButton)
          .addComponent(jButton2))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    setVisible(false);
  }//GEN-LAST:event_jButton2ActionPerformed

  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    if (esp_info==null) {
      esp_info = new VSTM_ESPInfo();
    }
    esp_info.SSID = SSID.getText();
    esp_info.SSPWD = SSPWD.getText();
    esp_info.baseID = VSTMID.getText();
    esp_info.sensitivity = Sensitivity.getSelectedIndex();
    esp_info.connectionType = CONNECTION_TYPE.getSelectedIndex();
    esp_info.ip1 = IP1.getText();
    esp_info.ip2 = IP2.getText();
    esp_info.ip3 = IP3.getText();
    esp_info.port_receiver = PORT_RECEIVE.getText();
    esp_info.port_send  = PORT_SEND.getText();
            
    try{
      vsTimeConnector.setVSTMParams(esp_info.SSID, esp_info);
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Could not set params."+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }   
    VS_SETTING.setParam(mainForm.con, "VS_BASE_SENS", ""+Sensitivity.getSelectedIndex());
    setVisible(false);
  }//GEN-LAST:event_saveButtonActionPerformed

  private void bTronspnderPower1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTronspnderPower1ActionPerformed
    try {
      vsTimeConnector.setPower(0);
    } catch (SerialPortException ex) {
     JOptionPane.showMessageDialog(this, "Could not set params."+ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);    
    }
  }//GEN-LAST:event_bTronspnderPower1ActionPerformed

  private void bTronspnderPower2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTronspnderPower2ActionPerformed
    try {
      vsTimeConnector.setPower(1);
    } catch (SerialPortException ex) {
     JOptionPane.showMessageDialog(this, "Could not set params."+ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);    
    }
  }//GEN-LAST:event_bTronspnderPower2ActionPerformed

  private void bTronspnderPower3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTronspnderPower3ActionPerformed
    try {
      vsTimeConnector.setPower(2);
    } catch (SerialPortException ex) {
     JOptionPane.showMessageDialog(this, "Could not set params."+ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);    
    }
  }//GEN-LAST:event_bTronspnderPower3ActionPerformed

  private void SensitivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SensitivityActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_SensitivityActionPerformed

  private void CONNECTION_TYPEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_CONNECTION_TYPEPropertyChange
    if (CONNECTION_TYPE.getSelectedIndex()==0){
      panelRouter.setVisible(false);      
    }else{
      panelRouter.setVisible(true);
    }  
    pack();
  }//GEN-LAST:event_CONNECTION_TYPEPropertyChange
  

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox CONNECTION_TYPE;
  private javax.swing.JTextField IP1;
  private javax.swing.JTextField IP2;
  private javax.swing.JTextField IP3;
  private javax.swing.JTextField IP4;
  private javax.swing.JTextField PORT_RECEIVE;
  private javax.swing.JTextField PORT_SEND;
  private javax.swing.JTextField SSID;
  private javax.swing.JTextField SSPWD;
  private javax.swing.JComboBox Sensitivity;
  private javax.swing.JTextField VSTMID;
  private javax.swing.JButton bTronspnderPower1;
  private javax.swing.JButton bTronspnderPower2;
  private javax.swing.JButton bTronspnderPower3;
  private javax.swing.JButton jButton2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel panelRouter;
  private javax.swing.JButton saveButton;
  // End of variables declaration//GEN-END:variables
}
