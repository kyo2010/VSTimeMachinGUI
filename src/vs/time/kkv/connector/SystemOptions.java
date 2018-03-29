/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import vs.time.kkv.connector.web.RaceHttpServer;
import java.awt.Point;
import vs.time.kkv.connector.Utils.TTS.TextToSpeachFactory;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class SystemOptions extends javax.swing.JFrame {

  MainForm mainForm = null;
  
  /**
   * Creates new form WLANSetting
   */
  public SystemOptions(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;
    setVisible(false);
  }
   
  static SystemOptions singelton  = null;
  public static SystemOptions init(MainForm mainForm){
    if (singelton==null){
      singelton = new SystemOptions(mainForm);
    }
    
    singelton.WEB_PORT.setText(VS_SETTING.getParam(mainForm.con, "WEB_PORT", "80"));      
    singelton.WebServiceStartOnRun.setSelected( VS_SETTING.getParam(mainForm.con, "START_HTTPD_ON_RUN", 0)==1?true:false );
    singelton.jcTTS_API.setSelectedItem(VS_SETTING.getParam(mainForm.con, "TTS_API", ""));
    singelton.checkRaceGroup.setSelected( VS_SETTING.getParam(mainForm.con, "CHECK_RACE_GROUP", 1)==1?true:false );    
    
    SystemOptions th = singelton;
    Point p = mainForm.getLocationOnScreen();
    th.setLocation(p.x + mainForm.getWidth() / 2 - th.getSize().width / 2, p.y + mainForm.getHeight() / 2 - th.getSize().height / 2);
    th.setAlwaysOnTop(true);
    th.setResizable(false);  
    th.buttnCaptionRefresh();
    
    return singelton;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    WEB_PORT = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();
    jButOk = new javax.swing.JButton();
    jButCancel = new javax.swing.JButton();
    WebServiceStartOnRun = new javax.swing.JCheckBox();
    bHTTPServer = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    jcTTS_API = new javax.swing.JComboBox<>();
    checkRaceGroup = new javax.swing.JCheckBox();

    setTitle("System Settings");
    setIconImage(MainForm.getWindowsIcon().getImage());
    setResizable(false);

    jLabel1.setText("HTTP Server Port");

    WEB_PORT.setText("80");

    jButOk.setText("Ok");
    jButOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButOkActionPerformed(evt);
      }
    });

    jButCancel.setText("Cancel");
    jButCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButOk, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jButCancel)
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButOk)
          .addComponent(jButCancel))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    WebServiceStartOnRun.setText("Start HTTP Server automatically");
    WebServiceStartOnRun.setToolTipText("");
    WebServiceStartOnRun.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    WebServiceStartOnRun.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        WebServiceStartOnRunActionPerformed(evt);
      }
    });

    bHTTPServer.setText("Start HTTP Server");
    bHTTPServer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bHTTPServerActionPerformed(evt);
      }
    });

    jLabel2.setText("Text to speach API");

    jcTTS_API.setModel(new javax.swing.DefaultComboBoxModel(TextToSpeachFactory.getTTSNames()));
    jcTTS_API.setName(""); // NOI18N

    checkRaceGroup.setText("Check : Race Group = Invate Group");
    checkRaceGroup.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(checkRaceGroup)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(62, 62, 62)
                .addComponent(WEB_PORT, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(WebServiceStartOnRun))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bHTTPServer))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addComponent(jcTTS_API, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(WEB_PORT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(WebServiceStartOnRun)
          .addComponent(bHTTPServer))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(jcTTS_API, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(checkRaceGroup)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_jButCancelActionPerformed

  private void jButOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButOkActionPerformed
    int WEB_PORT_INT = 80;
    try{
      WEB_PORT_INT = Integer.parseInt(WEB_PORT.getText());
    }catch(Exception e){}
    
    VS_SETTING.setParam(mainForm.con, "WEB_PORT", ""+WEB_PORT_INT);
    VS_SETTING.setParam(mainForm.con, "START_HTTPD_ON_RUN", ""+ (WebServiceStartOnRun.isSelected() ? 1 : 0));
    VS_SETTING.setParam(mainForm.con, "TTS_API", ""+jcTTS_API.getSelectedItem());    
    VS_SETTING.setParam(mainForm.con, "CHECK_RACE_GROUP", ""+ (checkRaceGroup.isSelected() ? 1 : 0));       
    
    
    mainForm.speaker.reset();
    
    setVisible(false);
  }//GEN-LAST:event_jButOkActionPerformed

  private void WebServiceStartOnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WebServiceStartOnRunActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_WebServiceStartOnRunActionPerformed

  public void buttnCaptionRefresh(){
    if (mainForm.httpServer==null || !mainForm.httpServer.connected){
      bHTTPServer.setText("Start HTTP Server");
    }else{
      bHTTPServer.setText("Stop HTTP Server");
    }
  }
  
  public static void runWebServer(MainForm mainForm, boolean runOrStop){
    if (runOrStop){
      mainForm.httpServer = new RaceHttpServer(mainForm, VS_SETTING.getParam(mainForm.con, "WEB_PORT", 80));
    }else{
      mainForm.httpServer.disconnect();
      mainForm.httpServer = null;
    } 
  }
  
  private void bHTTPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHTTPServerActionPerformed
    // TODO add your handling code here:
    if (mainForm.httpServer==null || !mainForm.httpServer.connected){      
       runWebServer(mainForm,true);
    }else{
      runWebServer(mainForm,false);
    }    
    buttnCaptionRefresh();
  }//GEN-LAST:event_bHTTPServerActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField WEB_PORT;
  private javax.swing.JCheckBox WebServiceStartOnRun;
  private javax.swing.JButton bHTTPServer;
  private javax.swing.JCheckBox checkRaceGroup;
  private javax.swing.JButton jButCancel;
  private javax.swing.JButton jButOk;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JComboBox<String> jcTTS_API;
  // End of variables declaration//GEN-END:variables
}
