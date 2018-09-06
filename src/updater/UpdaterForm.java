/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updater;

import KKV.Utils.ParseIniFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author kyo
 */
public class UpdaterForm extends javax.swing.JFrame {

  public static String UPDATE_PATH = "update/";
  ParseIniFile pif = new ParseIniFile("Updater.ini");

  String serverUri = "";

  /**
   * Creates new form UpdaterForm
   */
  public UpdaterForm() {
    initComponents();
    try {
      serverUri = pif.getParam("httpHost");
      //Setting up proxies
      Properties systemSettings = System.getProperties();
      try{
        if (pif.getParam("proxySet").equalsIgnoreCase("true")){
          systemSettings.put("proxySet",  pif.getParam("proxySet"));
          systemSettings.put("https.proxyHost", pif.getParam("proxyHost"));
          systemSettings.put("https.proxyPort",  pif.getParam("proxyPort"));
        }  
        if (!pif.getParam("useSystemProxies").equalsIgnoreCase("")){
          System.setProperty("java.net.useSystemProxies",  pif.getParam("useSystemProxies"));
        }  
      }catch(Exception e){}
      //The same way we could also set proxy for http            
      System.out.println("Server uri : " + serverUri);
      new UploadTimer(serverUri + "/update.list", UPDATE_PATH + "update.list").setAction(new UploaderActions() {
        @Override
        public void finishFile(String fileName) {          
          uploadNewFiles();
        }
        public void errorFile(String fileName) {                         
        }
      });
    } catch (Exception e) {
    }
  }    
  
  public void uploadNewFiles(){
    Map<String, UploadFileInfo> newFiles = UploadFileInfo.getFileInfo(UPDATE_PATH + "update.list");
    Map<String, UploadFileInfo> oldFiles = UploadFileInfo.getFileInfo("update.list");
    
    int countNewFile = 0;
    for (String fileName : newFiles.keySet()){
      UploadFileInfo newFile =  newFiles.get(fileName);
      UploadFileInfo oldFile = oldFiles.get(fileName);
      if (oldFile==null){
        countNewFile++;
      }else{
        if (oldFile.version==null || !oldFile.version.equalsIgnoreCase(newFile.version)){        
          countNewFile++;
        }
      }
    }
    lInfo1.setText("Finding "+countNewFile+" new components");
    lInfo2.setText(" ");    
  }
  
  interface UploaderActions{
    void finishFile(String fileName);
    void errorFile(String fileName);
  }

  public final class UploadTimer extends Timer {    
    public UploaderActions action = null;

    public UploadTimer setAction(UploaderActions action) {
      this.action = action;
      return this;
    }
    
    public UploadTimer(final String url, final String filename) {          
      super(100, null);   
      setRepeats(false);
      addActionListener(new ActionListener() {        
        @Override
        public void actionPerformed(ActionEvent ae) {
          new File(UPDATE_PATH).mkdirs();
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
            progressBar.setValue(0);
            server = new URL(url);
            connection = server.openConnection();
            is = connection.getInputStream();
            progressBar.setMaximum(is.available());

            byte[] buffer = new byte[1000];
            targetFile = new File(filename);
            outStream = new FileOutputStream(targetFile);
            int count = 0;
            while (is.read(buffer) > 0) {
              count++;
              outStream.write(buffer);
              progressBar.setValue(1000 * count);
            }            
            if (UploadTimer.this.action!=null) UploadTimer.this.action.finishFile(filename);
          } catch (MalformedURLException e) {
            if (UploadTimer.this.action!=null) UploadTimer.this.action.errorFile(filename);
            JOptionPane.showMessageDialog(UpdaterForm.this, "The url is not correct", "Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
          } catch (IOException e) {
            if (UploadTimer.this.action!=null) UploadTimer.this.action.errorFile(filename);
            JOptionPane.showMessageDialog(UpdaterForm.this, "File uploading is error", "Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
          } finally {
            if (outStream != null) {
              try {
                outStream.close();
              } catch (Exception ein) {
              }
            }
          }
        }
      });     
      setRepeats(false);
      start();
    }  
  };
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    bCancel = new javax.swing.JButton();
    progressBar = new javax.swing.JProgressBar();
    lInfo1 = new javax.swing.JLabel();
    lInfo2 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);

    bCancel.setText("Cancel");
    bCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bCancelActionPerformed(evt);
      }
    });

    lInfo1.setText("   ");

    lInfo2.setText("  ");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(159, 159, 159)
            .addComponent(bCancel)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(lInfo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lInfo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(23, 23, 23)
        .addComponent(lInfo1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(lInfo2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bCancel)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    // TODO add your handling code here:
    setVisible(false);
    System.exit(0);
  }//GEN-LAST:event_bCancelActionPerformed

  public void saveUrl(final String filename, final String url) throws IOException {
    //String url="https://raw.githubusercontent.com/bpjoshi/fxservice/master/src/test/java/com/bpjoshi/fxservice/api/TradeControllerTest.java";
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
      server = new URL(url);
      connection = server.openConnection();
      is = connection.getInputStream();
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      targetFile = new File(filename);
      outStream = new FileOutputStream(targetFile);
      outStream.write(buffer);
    } catch (MalformedURLException e) {
      System.out.println("THE URL IS NOT CORRECT ");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Io exception");
      e.printStackTrace();
    } finally {
      if (outStream != null) {
        outStream.close();
      }
    }
  }

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
      java.util.logging.Logger.getLogger(UpdaterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(UpdaterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(UpdaterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(UpdaterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new UpdaterForm().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bCancel;
  private javax.swing.JLabel lInfo1;
  private javax.swing.JLabel lInfo2;
  private javax.swing.JProgressBar progressBar;
  // End of variables declaration//GEN-END:variables
}
