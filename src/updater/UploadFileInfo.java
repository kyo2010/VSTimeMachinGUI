/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updater;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kyo
 */
public class UploadFileInfo {

  public String version;
  public String name;
  public boolean uploaded = false;

  public UploadFileInfo(String version, String name) {
    this.version = version.toUpperCase();
    this.name = name;
    this.name = this.name.replaceAll("\\\\", "/");
  }    

  public static Map<String, UploadFileInfo> getFileInfo(String fileList) {
    Map<String, UploadFileInfo> res = new HashMap();
    try {
      BufferedReader dataIn
              = new BufferedReader(new InputStreamReader(new FileInputStream(fileList), "UTF-8"));
      String readLine = dataIn.readLine();      
      while (readLine != null) {
        readLine = readLine.trim();
        String[] infos = readLine.split("::");
        if (infos!=null && infos.length==2){
          res.put(infos[1], new UploadFileInfo(infos[0],infos[1]));
        }
        readLine = dataIn.readLine();      
      }
      dataIn.close();
    } catch (Exception e) {
    }
    return res;
  }
}
