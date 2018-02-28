/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author kyo
 */
public class VSFlashControl {

  public static String flashDir = "flash";
  public static String flashFile = "list.json";
  public static String flahURL = "http://hex.vstimemachine.com/list.json";

  public String lastVersion = "";
  public List<VSFlash> flashes = new ArrayList<VSFlash>();

  public VSFlashControl() {
    parseJSON();
  }

  public void parseJSON() {
    try {
      flashes.clear();
      FileInputStream json_input = new FileInputStream(new File(flashDir + "/" + flashFile));      
      String jsonTxt = IOUtils.toString(json_input,"UTF-8");      
      JSONObject obj = new JSONObject(jsonTxt);
      lastVersion = obj.getString("last_version");
      JSONArray firmwares = obj.getJSONArray("firmware");
      for (int i = 0; i < firmwares.length(); i++) {
        JSONObject fimware = firmwares.getJSONObject(i);
        flashes.add(new VSFlash(fimware));        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String[] getVersions(){
    String[] results = new String[flashes.size()];
    int index = 0;
    for (VSFlash flash : flashes){
      results[index] = flash.version;
      index++;
    }
    return results;
  }
  

  public static void main(String[] args) {
    new VSFlashControl();
  }
}
