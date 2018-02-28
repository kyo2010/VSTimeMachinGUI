/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author kyo
 */
public class VSFlash {
  public JSONObject fimware = null;
  public String version = "";
  public String date = "";
  public String description = "";
  public JSONArray data = null;
  
  public VSFlash(JSONObject fimware){
    this.fimware = fimware;
    version = fimware.getString("version");
    date = fimware.getString("date");
    description = fimware.getString("description");
    data = fimware.getJSONArray("data");
    /*for (int i=0; i<data.length(); i++){
      System.out.println(data.getString(i));
    }*/
  }
}
