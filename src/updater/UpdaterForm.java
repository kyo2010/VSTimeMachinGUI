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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
  public static String UPDATE_OLD_PATH = "update_old/";
  ParseIniFile pif = new ParseIniFile("Updater.ini");
  

  String serverUri = "";
  List<UploadFileInfo> filesForUpdate = new ArrayList();

  /**
   * Creates new form UpdaterForm
   */
  public UpdaterForm() {
    initComponents();
    filesForUpdate.clear();
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
      new UploadTimer(serverUri + "/update.list?time="+Calendar.getInstance().getTimeInMillis(), 
                      UPDATE_PATH + "update.list",true).setAction(new UploaderActions() {
        @Override
        public void finishFile(String fileName) {          
          uploadNewFiles();
        }
        public void errorFile(String fileName) { 
          lInfo1.setText("Server don't response. Please check your connection.");    
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
        filesForUpdate.add(newFile);
      }else{
        if (oldFile.version==null || !oldFile.version.equalsIgnoreCase(newFile.version)){        
          countNewFile++;
          filesForUpdate.add(newFile);
        }
      }
    }
    if (countNewFile==0){
      bCancel.setText("Ok");
      lInfo1.setText("Your version is actual. There are no updates.");         
      progressBar.setValue(progressBar.getMaximum());
    }else{
      lInfo1.setText("Finding "+countNewFile+" new components");  
      progressBar.setValue(0);
      progressBar.setMaximum(countNewFile);
      uploadNextNewFile();
    }  
  }
  
  UploadFileInfo nextFile = null;
  
  public void uploadNextNewFile(){    
    if (!UpdaterForm.this.isVisible()) return;
    int count = 0;
    nextFile = null;
    for (UploadFileInfo fi : filesForUpdate){
      count++;
      if (!fi.uploaded){
        nextFile = fi;        
        break;
      }
    }    
    if (nextFile!=null){
      lInfo1.setText("Uploading a new component "+count+" of "+filesForUpdate.size());  
      progressBar.setValue(count-1);
      new UploadTimer(serverUri + "/"+nextFile.name, UPDATE_PATH +nextFile.name,false).setAction(new UploaderActions() {
        @Override
        public void finishFile(String fileName) {   
          nextFile.uploaded = true;
          uploadNextNewFile();
        }
        public void errorFile(String fileName) { 
          lInfo1.setText("Server don't response. Please check your connection.");    
        }
      });
    }else{      
      bCancel.setText("Ok");
      progressBar.setValue(progressBar.getMaximum());
      lInfo1.setText("All components have been uploaded.");  
      boolean updte_is_ok = true;
      for (UploadFileInfo fi : filesForUpdate){
        File currentFile = new File(UPDATE_OLD_PATH+fi.name);
        currentFile.getParentFile().mkdirs();       
        currentFile.delete();
        if (!(new File(fi.name).renameTo(currentFile))) updte_is_ok = false;
        if (!(new File(UPDATE_PATH+fi.name).renameTo(new File(fi.name)))) updte_is_ok = false;
      }
      if (updte_is_ok){
        new File("update.list").renameTo(new File(UPDATE_OLD_PATH + "update.list"));      
        new File(UPDATE_PATH + "update.list").renameTo(new File("update.list"));
      }
      lInfo1.setText("All components have been updated. Please restart the application.");  
    }
  }
  
  interface UploaderActions{
    void finishFile(String fileName);
    void errorFile(String fileName);
  }

  public final class UploadTimer extends Timer {    
    public UploaderActions action = null;
    boolean showProgress = true;

    public UploadTimer setAction(UploaderActions action) {
      this.action = action;
      return this;
    }
    
    public UploadTimer(final String url, final String filename,boolean showProgress) {          
      super(100, null);   
      setRepeats(false);
      this.showProgress = showProgress;
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
            if (UploadTimer.this.showProgress) progressBar.setValue(0);
            server = new URL(url);
            connection = server.openConnection();
            is = connection.getInputStream();
            if (UploadTimer.this.showProgress) progressBar.setMaximum(is.available());
            new File(filename).getParentFile().mkdirs();

            byte[] buffer = new byte[1000];
            targetFile = new File(filename);
            outStream = new FileOutputStream(targetFile);
            int count = 0;
            while (is.read(buffer) > 0) {
              count++;
              outStream.write(buffer);
              if (UploadTimer.this.showProgress) progressBar.setValue(1000 * count);
              if (!UpdaterForm.this.isVisible()) return;
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

    setTitle("Updater");
    setAlwaysOnTop(true);
    setResizable(false);

    bCancel.setText("Cancel");
    bCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bCancelActionPerformed(evt);
      }
    });

    lInfo1.setText("   ");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(lInfo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap())
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(0, 0, Short.MAX_VALUE)
        .addComponent(bCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(143, 143, 143))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(lInfo1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
  private javax.swing.JProgressBar progressBar;
  // End of variables declaration//GEN-END:variables
}
