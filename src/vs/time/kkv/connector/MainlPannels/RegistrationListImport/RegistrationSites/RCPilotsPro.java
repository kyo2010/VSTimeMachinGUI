package vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites;

import KKV.Utils.JDEDate;
import KKV.Utils.UserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kyo
 */
public class RCPilotsPro extends IRegSite {
 
  {
    REG_SITE_URL = "https://rcpilots.pro/rest_api/get_events.php?type=1&hash=read_only";
    REG_SITE_NAME = "rcpilots.pro";
  }
  
  @Override
  public String getSystemName() {
    return REG_SITE_NAME;
  }
 
  @Override
  public void load() throws UserException {
    try{
      refreshJSON();      
    }catch(Exception e){
    }        
    try{
      races.clear();
      FileInputStream json_input = new FileInputStream(new File(getJSONFileName()));      
      String jsonTxt = IOUtils.toString(json_input,"UTF-8");      
      JSONObject obj = new JSONObject(jsonTxt);
      JSONArray json_races = obj.getJSONArray("data");
      for (int i = 0; i < json_races.length(); i++) {
        try{
          JSONObject json_race = json_races.getJSONObject(i);
          VS_RACE race = new VS_RACE();
          race.RACE_ID = Integer.parseInt(json_race.getString("id")); 
          race.RACE_DATE =  new JDEDate();
          race.RACE_DATE.setJDEDateAsDDMMYYYY(json_race.getString("date_event"), "."); 
          race.RACE_NAME = json_race.getString("name"); 
          races.add(race);
          
          JSONArray json_users = obj.getJSONArray("pilots");
          for (int j = 0; j < json_users.length(); j++) {
            JSONObject json_pilot = json_races.getJSONObject(j); 
            VS_REGISTRATION user = new VS_REGISTRATION();
            user.NUM = j+1;
            user.IS_ACTIVE = 1;
            user.VS_SOUND_EFFECT = 1;
            user.PILOT_TYPE = 0;
            user.WEB_SID = json_pilot.getString("id");
            user.WEB_SYSTEM = getSystemName();
            user.VS_USER_NAME = json_pilot.getString("osd_name");
            user.FIRST_NAME= json_pilot.getString("fname");
            user.SECOND_NAME= json_pilot.getString("lname");
            race.users.add(user);
          }           
        }catch(Exception e){
          MainForm._toLog(e);
        }                
      }      
    }catch(Exception e){
      e.printStackTrace();
      MainForm._toLog(e);
    }                
  }
  
  public static void main (String[] args){
    IRegSite site = new RCPilotsPro();
    try{
      System.out.println("Start");
      site.load();      
      for (VS_RACE race : site.getRaces()){
        System.out.println(race.RACE_NAME);
        for (VS_REGISTRATION user : race.users ){
           System.out.println("  "+ user.VS_USER_NAME+" ["+user.FIRST_NAME+" "+user.SECOND_NAME+"]");
        }
      }
      System.out.println("Finish");
    }catch(Exception e){
      e.printStackTrace();
    }
  }
 
}