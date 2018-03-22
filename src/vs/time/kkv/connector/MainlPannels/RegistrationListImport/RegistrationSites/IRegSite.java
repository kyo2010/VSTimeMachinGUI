/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites;

import KKV.Utils.UserException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.TimeMachine.VSFlashControl;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;

/**
 *
 * @author kyo
 */
public abstract class IRegSite {

  public static String TMP_PHOTO_NAME = "flash/PHOTO";
  
  public String REG_SITE_URL = "";
  public String REG_SITE_NAME = "";
  List<VS_RACE> races = new ArrayList<VS_RACE>();

  public String getJSONFileName() {
    return VSFlashControl.flashDir + "/" + REG_SITE_NAME;
  }

  public String getSystemName() {
    return REG_SITE_NAME;
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
        String newFileName = TMP_PHOTO_NAME + "/" +url.substring(pos+1);
        
        byte[] buffer = new byte[1000];
        targetFile = new File( newFileName );
        outStream = new FileOutputStream(targetFile);
        int count = 0;
        int m = 0;
        while ((m = is.read(buffer)) > 0) {
          count++;
          outStream.write(buffer, 0, m);
        }
        fileName = newFileName;
      } catch (MalformedURLException e) {
        MainForm._toLog(e);
      } catch (IOException e) {
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
}
