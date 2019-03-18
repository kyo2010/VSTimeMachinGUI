/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites;

import KKV.Utils.UserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.RegistrationTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.TimeMachine.VSFlashControl;
import vs.time.kkv.connector.Utils.OSDetector;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;

/**
 *
 * @author kyo
 */
public abstract class IRegSite {

  public static String TMP_PHOTO_NAME = "flash/PHOTO";
  public static String PATH_ONLINE_UPDATE = "online";

  public String REG_SITE_URL = "";
  public String REG_SITE_NAME = "";
  public String REG_SITE_URL_FOR_UPLOAD = "";

  List<VS_RACE> races = new ArrayList<VS_RACE>();

  public String getJSONFileName() {
    return VSFlashControl.flashDir + "/" + REG_SITE_NAME;
  }

  public String getSystemName() {
    return REG_SITE_NAME;
  }

  public boolean isSuportedToWebUpload() {
    return false;
  }

  public boolean uploadToWebSystem(RegistrationTab regTab, StageTab tab, boolean removeAllStages, boolean showMessages) {
    return false;
  }

  public abstract void load() throws UserException;

  public List<VS_RACE> getRaces() {
    return races;
  }

  public List<VS_REGISTRATION> getUsers(long race_id) {
    for (VS_RACE race : races) {
      if (race.RACE_ID == race_id) {
        return race.users;
      }
    }
    return null;
  }

  public String getImageFromWeb(String url) {
    String fileName = "";
    try {
      new File(TMP_PHOTO_NAME).mkdirs();
      OutputStream outStream = null;
      URLConnection connection = null;
      InputStream is = null;
      File targetFile = null;
      URL server = null;
      try {
        server = new URL(url);
        connection = server.openConnection();
        is = connection.getInputStream();

        int pos = url.lastIndexOf("/");
        String newFileName = TMP_PHOTO_NAME + "/" + getSystemName() +"_"+url.substring(pos + 1);

        byte[] buffer = new byte[1000];
        targetFile = new File(newFileName);
        outStream = new FileOutputStream(targetFile);
        int count = 0;
        int m = 0;
        while ((m = is.read(buffer)) > 0) {
          count++;
          outStream.write(buffer, 0, m);
        }
        fileName = newFileName;
      } catch (MalformedURLException e) {
        MainForm._toLog("url error:" + url);
        MainForm._toLog(e);
      } catch (IOException e) {
        MainForm._toLog("url error:" + url);
        MainForm._toLog(e);
      } finally {
        if (outStream != null) {
          try {
            outStream.close();
          } catch (Exception ein) {
          }
        }
      }
    } catch (Exception e) {
    }
    return fileName;
  }

  public void refreshJSON() throws UserException {
    new File(VSFlashControl.flashDir).mkdirs();
    OutputStream outStream = null;
    URLConnection connection = null;
    InputStream is = null;
    File targetFile = null;
    URL server = null;
    try {
      server = new URL(REG_SITE_URL);
      connection = server.openConnection();
      is = connection.getInputStream();

      byte[] buffer = new byte[1000];
      targetFile = new File(VSFlashControl.flashDir + "/" + REG_SITE_NAME + ".tmp");
      outStream = new FileOutputStream(targetFile);
      int count = 0;
      int m = 0;
      while ((m = is.read(buffer)) > 0) {
        count++;
        outStream.write(buffer, 0, m);
      }
      File distFile = new File(getJSONFileName());
      distFile.delete();
      FileUtils.copyFile(targetFile, new File(getJSONFileName()));
      targetFile.renameTo(distFile);
    } catch (MalformedURLException e) {
      MainForm._toLog(e);
      throw new UserException("Load web resourse is error.", e.toString());
    } catch (IOException e) {
      MainForm._toLog(e);
      throw new UserException("Load web resourse is error.", e.toString());
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (Exception ein) {
        }
      }
    }
  }

  public boolean isSameContent(String fileName1, String fileName2) {
    try {
      File f1 = new File(fileName1);
      File f2 = new File(fileName2);
      if (f1.length() != f2.length()) {
        return false;
      }
      FileInputStream fis1 = new FileInputStream(f1);
      FileInputStream fis2 = new FileInputStream(f2);
      try {
        int byte1;
        while ((byte1 = fis1.read()) != -1) {
          int byte2 = fis2.read();
          if (byte1 != byte2) {
            return false;
          }
        }
      } finally {
        fis1.close();
        fis2.close();
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  public static String MD5(String md5) {
   try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = null;
        //if (OSDetector.isMac()){
        //  array = md.digest(md5.getBytes());
        //}else{
          array = md.digest(md5.getBytes("UTF-8"));       
        //}  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
       }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
      MainForm._toLog(e);
    } catch(  Exception ue ){    
      MainForm._toLog(ue);
    }
    return null;
}
}
