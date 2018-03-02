/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import com.ibm.icu.util.Calendar;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;
import org.apache.commons.io.FileUtils;
import vs.time.kkv.connector.MainForm;
import static vs.time.kkv.connector.MainlPannels.InfoForm.form;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.connector.TimeMachine.VSFlash;
import vs.time.kkv.connector.TimeMachine.VSFlashControl;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class VSTeamConsole extends javax.swing.JFrame {

  public VSFlashControl flashControl = new VSFlashControl();
  
  /**
   * Creates new form VSTeamConsole
   */
  public VSTeamConsole() {
    initComponents();
    DefaultCaret caret = (DefaultCaret)jText.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
  }
    
  public MainForm mainForm = null;
  public static VSTeamConsole _form = null;
  public static boolean isOpened = false;
  public static boolean showPing = false;
  
  public static VSTeamConsole init(MainForm mainForm) {
    if (_form == null) {
      _form = new VSTeamConsole();      
      if (mainForm != null) {
        //mainForm.setFormOnCenter(_form);        
        _form.jTransFlash.setText(VS_SETTING.getParam(mainForm.con, "LAST_FLASH_ID", ""));
      }
      _form.updateJSONFile();
    }
     if (mainForm != null) _form.mainForm = mainForm;        
    _form.jcbShowPing.setSelected(showPing);
    _form.jtLastTransID.setText( ""+ mainForm.lastTranponderID );
    _form.setVisible(false);    
    _form.jpFlash.setVisible(false);
    return _form;
  }  

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
    isOpened = b;    
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
    jtCommand = new javax.swing.JTextField();
    butSend = new javax.swing.JButton();
    jcbShowPing = new javax.swing.JCheckBox();
    jLabel2 = new javax.swing.JLabel();
    jtLastTransID = new javax.swing.JTextField();
    jbClear = new javax.swing.JButton();
    jcbAutoClear = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();
    bFlash = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jTransFlash = new javax.swing.JTextField();
    jcFlashVersion = new javax.swing.JComboBox<>();
    butLoadFlash = new javax.swing.JButton();
    bHello = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jText = new javax.swing.JTextArea();
    jpFlash = new javax.swing.JProgressBar();

    setTitle("Console");
    setAlwaysOnTop(true);
    setIconImage(MainForm.getWindowsIcon().getImage());
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosed(java.awt.event.WindowEvent evt) {
        formWindowClosed(evt);
      }
    });

    jLabel1.setText("Command :");

    jtCommand.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jtCommandKeyPressed(evt);
      }
    });

    butSend.setText("Send");
    butSend.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butSendActionPerformed(evt);
      }
    });

    jcbShowPing.setText("Show Pings");
    jcbShowPing.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jcbShowPingStateChanged(evt);
      }
    });

    jLabel2.setText("Last TransID:");

    jtLastTransID.setEditable(false);

    jbClear.setText("Clear");
    jbClear.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbClearActionPerformed(evt);
      }
    });

    jcbAutoClear.setToolTipText("Auto clear text after send");

    jButton1.setText("SET RED");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jButton2.setText("SET GREEN");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    jButton3.setText("COLOR OFF");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });

    bFlash.setText("Flash");
    bFlash.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bFlashActionPerformed(evt);
      }
    });

    jLabel3.setText("Transponder ID :");

    butLoadFlash.setText("Get Flash from WEB");
    butLoadFlash.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butLoadFlashActionPerformed(evt);
      }
    });

    bHello.setText("Hello/Send Time");
    bHello.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bHelloActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jcbShowPing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtCommand)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbAutoClear)
                .addGap(8, 8, 8)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(butSend, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
              .addComponent(jtLastTransID)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bHello))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTransFlash, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcFlashVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bFlash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butLoadFlash)))
            .addGap(0, 8, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jcbAutoClear)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel1)
            .addComponent(jtCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(butSend)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jcbShowPing)
          .addComponent(jLabel2)
          .addComponent(jtLastTransID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jbClear))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButton1)
          .addComponent(jButton2)
          .addComponent(jButton3)
          .addComponent(bHello))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bFlash)
          .addComponent(jLabel3)
          .addComponent(jTransFlash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcFlashVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(butLoadFlash))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jText.setEditable(false);
    jText.setColumns(20);
    jText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jText.setRows(5);
    jScrollPane1.setViewportView(jText);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addComponent(jpFlash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
        .addGap(5, 5, 5)
        .addComponent(jpFlash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jbClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearActionPerformed
    // TODO add your handling code here:
    jText.setText("");
  }//GEN-LAST:event_jbClearActionPerformed

  private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    // TODO add your handling code here:
    isOpened = false;
  }//GEN-LAST:event_formWindowClosed

  private void jcbShowPingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jcbShowPingStateChanged
    // TODO add your handling code here:
    showPing = jcbShowPing.isSelected();
  }//GEN-LAST:event_jcbShowPingStateChanged

  private void butSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butSendActionPerformed
    // TODO add your handling code here:    
    if (mainForm.vsTimeConnector!=null){
      try{
        mainForm.vsTimeConnector.sentMessage(jtCommand.getText()+"\r\n");
        if (jcbAutoClear.isSelected()){
          jtCommand.setText("");
        }  
      }catch(Exception e){
        addText(e.toString());
      }  
    }else{
      JOptionPane.showMessageDialog(this, "Please connect a device", "Information", JOptionPane.INFORMATION_MESSAGE);      
    }  
  }//GEN-LAST:event_butSendActionPerformed

  private void jtCommandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtCommandKeyPressed
    // TODO add your handling code here:
    if (evt.getKeyCode()==10){
      butSendActionPerformed(null);      
    }
  }//GEN-LAST:event_jtCommandKeyPressed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    // TODO add your handling code here:
    if (mainForm.vsTimeConnector!=null){
      try{
        mainForm.vsTimeConnector.setColor(0, VSColor.RED.getVSColor() );
      }catch(Exception e){
         addText(e.toString());
      }
    }
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // TODO add your handling code here:
    if (mainForm.vsTimeConnector!=null){
      try{
        mainForm.vsTimeConnector.setColor(0, VSColor.GREEN.getVSColor() );
      }catch(Exception e){
         addText(e.toString());
      }
    }
  }//GEN-LAST:event_jButton2ActionPerformed

  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    // TODO add your handling code here:
    if (mainForm.vsTimeConnector!=null){
      try{
        mainForm.vsTimeConnector.setColor(0, VSColor.OFF.getVSColor() );
      }catch(Exception e){
         addText(e.toString());
      }
    }
  }//GEN-LAST:event_jButton3ActionPerformed

  
  
    public class UploadTimer extends Timer {

    public String url, filename;

    public UploadTimer(final String url, final String filename) {      
      super(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          new File (VSFlashControl.flashDir).mkdirs();
          OutputStream outStream = null;
          URLConnection connection = null;
          InputStream is = null;
          File targetFile = null;
          URL server = null;
          //Setting up proxies
          /*Properties systemSettings = System.getProperties();
            systemSettings.put("proxySet", "true");
            systemSettings.put("https.proxyHost", "https proxy of my organisation");
            systemSettings.put("https.proxyPort", "8080");
            //The same way we could also set proxy for http
            System.setProperty("java.net.useSystemProxies", "true");*/
          //code to fetch file
          try {
            jpFlash.setVisible(true);
            jpFlash.setValue(0);
            server = new URL(url);
            connection = server.openConnection();
            is = connection.getInputStream();
            jpFlash.setMaximum(is.available());
                        
            byte[] buffer = new byte[1000];            
            targetFile = new File(VSFlashControl.flashDir+"/"+filename+".tmp"); 
            outStream = new FileOutputStream(targetFile);
            int count = 0;
            int m=0;
            while ( (m=is.read(buffer))>0){
              count++;                         
              outStream.write(buffer,0,m);
              jpFlash.setValue(1000*count);
            }           
            File distFile = new File(VSFlashControl.flashDir+"/"+filename);
            distFile.delete();
            FileUtils.copyFile(targetFile, distFile);
            targetFile.renameTo(new File(VSFlashControl.flashDir+"/"+filename));
            addText(filename+" has been updated from :"+url);
            updateJSONFile();            
          } catch (MalformedURLException e) {            
            addText(e.toString());
          } catch (IOException e) {
            addText(e.toString());
          } finally {
            if (outStream != null) {
              try{
                outStream.close();
              }catch(Exception ein){}  
            }
          }
          jpFlash.setVisible(false);
        }
      });
      this.filename = filename;
      this.url = url;
      setRepeats(false);
      start();
    }
  };
    
  public void updateJSONFile(){
    flashControl.parseJSON();
    jcFlashVersion.setModel(new javax.swing.DefaultComboBoxModel(flashControl.getVersions()));
  }
  
  private void butLoadFlashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butLoadFlashActionPerformed
    // TODO add your handling code here:
    try{      
      new UploadTimer(VSFlashControl.flahURL,VSFlashControl.flashFile);
    }catch(Exception e){
      addText(e.toString());
    }    
  }//GEN-LAST:event_butLoadFlashActionPerformed

  static int LAST_SKIP_INDEX = 0;  // ��������� ������� �� ����
  
  public FlashTimer flashTimer = null;
    public class FlashTimer extends Timer {    
     public FlashTimer(final int transponderID, final VSFlash flash) {      
      super(20, new ActionListener() {
        int indexData = 0; 
        int waitResponse = 0;
        boolean sended = false;   
        boolean recive_confirmation = true;
        long startTime = Calendar.getInstance().getTimeInMillis();
        @Override
        public void actionPerformed(ActionEvent ae) {         
          try{
            if ((indexData+LAST_SKIP_INDEX)<flash.data.length()){                            
              if (sended==false || waitResponse>15){                
                waitResponse = 0;
                mainForm.vsTimeConnector.sendflash(transponderID, flash.data.getString(indexData));
                
                String ff = "11";
                try{
                  ff = flash.data.getString(indexData).substring(7, 9); 
                }catch(Exception e){}  
                if (ff.equalsIgnoreCase("00")){ // it is data, we need confirm
                  sended = true;
                }else{
                  indexData++;  
                  waitResponse = 0;
                }  
                
              }
            }
            
            if (sended && mainForm.vsTimeConnector.flashResponse.get(transponderID)!=null){
              sended=false;
              waitResponse = 0;
              indexData++;  
              jpFlash.setValue(indexData);
            }else{
              waitResponse++;
              
              if (waitResponse>10 && !recive_confirmation) {
                indexData++;
                sended=false;
                jpFlash.setValue(indexData);
              }
            }                                    
            if (sended==false && (indexData+LAST_SKIP_INDEX)>=flash.data.length()){
              long stop = Calendar.getInstance().getTimeInMillis();
              addText("Flash "+transponderID+" is OK. "+StageTab.getTimeIntervelForTimer(stop-startTime)+" seconds.");
              stopFlash();
            }
          }catch(Exception e){
            addText(e.toString());
          }  
        }
      });      
      setRepeats(true);
      jpFlash.setVisible(true);
      jpFlash.setMaximum(flash.data.length());
      jpFlash.setValue(0);
      start();
    }
  };  
  
  public void stopFlash(){
    jpFlash.setVisible(false);
    flashTimer.stop();
    flashTimer = null;
    jpFlash.setVisible(false);
    bFlash.setText("Flash");
  }
  
  private void bFlashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFlashActionPerformed
    // TODO add your handling code here:        
    if (flashTimer==null){
      if (mainForm.vsTimeConnector==null){
        JOptionPane.showMessageDialog(this, "Please connect a device", "Information", JOptionPane.INFORMATION_MESSAGE);         
        return;
      }
      bFlash.setText("Stop");
      try{
        int transponderID =  Integer.parseInt(jTransFlash.getText());
        if (transponderID!=0){
          VS_SETTING.setParam(mainForm.con, "LAST_FLASH_ID", jTransFlash.getText());
          int index = jcFlashVersion.getSelectedIndex();        
          flashTimer = new FlashTimer(transponderID, flashControl.flashes.get(index));
        };  
      }catch(Exception e){
        addText(e.toString());
      }
    }else{
      stopFlash();
    }
  }//GEN-LAST:event_bFlashActionPerformed

  private void bHelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHelloActionPerformed
    // TODO add your handling code here:
    try{
      mainForm.vsTimeConnector.hello();
      mainForm.vsTimeConnector.setTime();
    }catch(Exception e){
      mainForm._toLog(e);
      addText(e.toString());
    }  
  }//GEN-LAST:event_bHelloActionPerformed

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
      java.util.logging.Logger.getLogger(VSTeamConsole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(VSTeamConsole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(VSTeamConsole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(VSTeamConsole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new VSTeamConsole().setVisible(true);
      }
    });
  }
  
  public static synchronized void addText(String text){
    text = text.trim();
    _form.jText.append(text+"\n");
   /* if (text.charAt(text.length()-1)=='\n'){
      _form.jText.append(text);   
    }else{
      _form.jText.append(text+"\n");   
    }*/  
  }   
  
  public static synchronized void setLastTransID(String text){
    _form.jtLastTransID.setText(text);
  }   

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bFlash;
  private javax.swing.JButton bHello;
  private javax.swing.JButton butLoadFlash;
  private javax.swing.JButton butSend;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextArea jText;
  private javax.swing.JTextField jTransFlash;
  private javax.swing.JButton jbClear;
  private javax.swing.JComboBox<String> jcFlashVersion;
  private javax.swing.JCheckBox jcbAutoClear;
  private javax.swing.JCheckBox jcbShowPing;
  private javax.swing.JProgressBar jpFlash;
  private javax.swing.JTextField jtCommand;
  private javax.swing.JTextField jtLastTransID;
  // End of variables declaration//GEN-END:variables
}
