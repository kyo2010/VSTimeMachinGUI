package vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites;

import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import KKV.Utils.UserException;
import de.dfki.lt.tools.tokenizer.FileTools;
import static java.awt.SystemColor.text;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.RegistrationTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
import static vs.time.kkv.models.VS_STAGE_GROUP.GROUP_QUAL_TIME_COMPARATOR;
import vs.time.kkv.models.VS_STAGE_GROUPS;

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
    REG_SITE_URL = "https://rcpilots.pro/rest_api/get_events.php?type=0&hash=read_only";
    REG_SITE_URL_FOR_UPLOAD = "https://rcpilots.pro/rest_api/make_fly_new.php";
    REG_SITE_NAME = "RCPilots.pro";
  }

  @Override
  public String getSystemName() {
    return REG_SITE_NAME;
  }

  @Override
  public void load() throws UserException {
    try {
      refreshJSON();
    } catch (Exception e) {
    }
    try {
      races.clear();
      FileInputStream json_input = new FileInputStream(new File(getJSONFileName()));
      String jsonTxt = IOUtils.toString(json_input, "UTF-8");
      JSONObject obj = new JSONObject(jsonTxt);
      JSONArray json_races = obj.getJSONArray("data");
      for (int i = 0; i < json_races.length(); i++) {
        try {
          JSONObject json_race = json_races.getJSONObject(i);
          VS_RACE race = new VS_RACE();
          race.RACE_ID = Integer.parseInt(json_race.getString("id"));
          race.WEB_UID = json_race.getString("unic");
          race.RACE_DATE = new JDEDate();
          race.RACE_DATE.setJDEDateAsDDMMYYYY(json_race.getString("date_event"), ".");
          race.RACE_NAME = "[" + race.RACE_DATE.getDateAsYYYYMMDD("-") + "] " + json_race.getString("name");
          races.add(race);

          JSONArray json_users = json_race.getJSONArray("pilots");
          for (int j = 0; j < json_users.length(); j++) {
            JSONObject json_pilot = json_users.getJSONObject(j);
            VS_REGISTRATION user = new VS_REGISTRATION();
            user.NUM = j + 1;
            user.IS_ACTIVE = 1;
            user.VS_SOUND_EFFECT = 1;
            user.PILOT_TYPE = 0;
            //user.WEB_SID = json_pilot.getString("id");
            user.WEB_SID = json_pilot.getString("pilot_id");
            user.WEB_SYSTEM = getSystemName();
            user.VS_USER_NAME = json_pilot.getString("osd_name").trim();
            user.FIRST_NAME = json_pilot.getString("fname");
            user.SECOND_NAME = json_pilot.getString("lname");
            user.E_MAIL = "";//json_pilot.getString("email"ffff);
            if (user.VS_USER_NAME.equalsIgnoreCase("")) {
              String nik = "";
              nik += user.FIRST_NAME.trim();
              if (!nik.equalsIgnoreCase("") && !user.SECOND_NAME.equals("")) {
                nik += " ";
              }
              nik += user.SECOND_NAME.trim();
              user.VS_USER_NAME = nik;
            }

            user.PICTURE_FILENAME = "";
            user.PHOTO = "";
            try {
              // https://rcpilots.pro/pilots_foto/266291517.jpg
              user.PICTURE_FILENAME = json_pilot.getString("photo_file").trim();
              user.WEB_PHOTO_URL = "https://rcpilots.pro/pilots_foto/" + user.PICTURE_FILENAME;
              if (!user.PICTURE_FILENAME.equalsIgnoreCase("")) {
                user.PHOTO = "https://rcpilots.pro/pilots_foto/" + user.PICTURE_FILENAME;
              }
            } catch (Exception e) {
            };

            race.users.add(user);
          }
        } catch (Exception e) {
          MainForm._toLog(e);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      MainForm._toLog(e);
    }
  }

  public static void main(String[] args) {
    IRegSite site = new RCPilotsPro();
    try {
      System.out.println("Start");
      site.load();
      for (VS_RACE race : site.getRaces()) {
        System.out.println(race.RACE_NAME);
        for (VS_REGISTRATION user : race.users) {
          System.out.println("  " + user.VS_USER_NAME + " [" + user.FIRST_NAME + " " + user.SECOND_NAME + "]");
        }
      }
      System.out.println("Finish");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean isSuportedToWebUpload() {
    return true;
  }

  public static Comparator GROUP_SCORES_COMPARATOR_2 = new Comparator<VS_STAGE_GROUPS>() {
    @Override
    public int compare(VS_STAGE_GROUPS o1, VS_STAGE_GROUPS o2) {
      if (o1.GROUP_NUM < o2.GROUP_NUM) {
        return 1;
      }
      if (o1.GROUP_NUM > o2.GROUP_NUM) {
        return -1;
      }
      if (o1.SCORE < o2.SCORE) {
        return 1;
      }
      if (o1.SCORE > o2.SCORE) {
        return -1;
      }
      if (o1.RACE_TIME > o2.RACE_TIME) {
        return 1;
      }
      if (o1.RACE_TIME < o2.RACE_TIME) {
        return -1;
      }
      if (o1.QUAL_TIME > o2.QUAL_TIME) {
        return 1;
      }
      if (o1.QUAL_TIME < o2.QUAL_TIME) {
        return -1;
      }
      if (o1.LAPS < o2.LAPS) {
        return 1;
      }
      if (o1.LAPS > o2.LAPS) {
        return -1;
      }
      if (o1.BEST_LAP > o2.BEST_LAP) {
        return 1;
      }
      if (o1.BEST_LAP < o2.BEST_LAP) {
        return -1;
      }
      return 0;
    }
  };

  @Override
  public boolean uploadToWebSystem(RegistrationTab regTab, StageTab tab, boolean removeAllStages, boolean showMessages) {

    String jsonFileName = "";

    String message = "";
    JSONObject json = new JSONObject();

    if (regTab != null) {
      return true;
    }
    if (tab == null) {
      return true;
    }
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_PRACTICA || tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION
            || tab.stage.STAGE_TYPE == MainForm.STAGE_RACE  || tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      // it's ok
    } else {
      return true;
    }

    jsonFileName = tab.mainForm.activeRace.RACE_ID + "_" + tab.stage.ID;

    //json.put("RACE_ID", regTab.mainForm.activeRace.RACE_ID);
    json.put("RACE_UNIC", tab.mainForm.activeRace.WEB_UID);
    json.put("RACE_NAME", tab.mainForm.activeRace.RACE_NAME);


    /*json.put("RACE_ID", tab.mainForm.activeRace.RACE_ID);
      json.put("WEB_RACE_ID", tab.mainForm.activeRace.WEB_RACE_ID);
      json.put("STAGE_ID", tab.stage.ID);
      json.put("LAPS", tab.stage.LAPS);
      json.put("CAPTION", tab.stage.CAPTION);
      json.put("STAGE_TYPE", tab.stage.STAGE_TYPE);
      json.put("SORT_TYPE", tab.stage.SORT_TYPE);
      json.put("PARENT_STAGE_ID", tab.stage.PARENT_STAGE_ID);
      json.put("PILOTS_FOR_NEXT_ROUND", tab.stage.PILOTS_FOR_NEXT_ROUND);
      json.put("PILOT_TYPE", tab.stage.PILOT_TYPE);*/
    try {
      if (tab.stage.laps_check_reg_id != null) {
        if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
          tab.stage.laps_check_reg_id = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "REG_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
        } else {
          tab.stage.laps_check_reg_id = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
        }
      }
    } catch (Exception e) {
    }

    JSONArray users_array = new JSONArray();
    String salt_vals = tab.mainForm.activeRace.WEB_UID;

    for (Integer groupNum : tab.stage.groups.keySet()) {
      List<VS_STAGE_GROUPS> users = tab.stage.groups.get(groupNum).users;

      List<VS_STAGE_GROUPS> sorted_users = new ArrayList<VS_STAGE_GROUPS>();
      for (VS_STAGE_GROUPS user : users) {
        sorted_users.add(user);
        if (user.BEST_LAP==0) user.BEST_LAP=VS_STAGE_GROUPS.MAX_TIME;
        if (user.RACE_TIME==0) user.RACE_TIME=VS_STAGE_GROUPS.MAX_TIME;
      }
      //Collections.sort(sorted_users, GROUP_TIME_COMPARATOR);
      Collections.sort(sorted_users, GROUP_SCORES_COMPARATOR_2);

      if (users != null) {
        int POS = 1;
        int LAST_GROUP = -1;
        for (VS_STAGE_GROUPS usr : sorted_users) {
          JSONObject jsonObj = new JSONObject();
          VS_REGISTRATION reg = usr.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
          if (reg == null) {
            continue;
          }

          if (reg.WEB_SYSTEM.equalsIgnoreCase(getSystemName())) {
            jsonObj.put("PILOT_ID", reg.WEB_SID);
          } else {
            jsonObj.put("PILOT_ID", "");
          }
          //jsonObj.put("PILOT_ID", reg.WEB_SID);
          jsonObj.put("PILOT_NAME", reg.VS_USER_NAME);
          jsonObj.put("PILOT_EMAIL", reg.E_MAIL);
          salt_vals += reg.VS_USER_NAME;
          JSONArray FLIGHTS = new JSONArray();

          if (LAST_GROUP != -1 && LAST_GROUP != usr.GROUP_NUM) {
            POS = 1;
          }

          /*if (reg.VS_USER_NAME.equalsIgnoreCase("PandaFPV")){          
          //if (usr.GROUP_NUM==1){
            System.out.println(usr.GROUP_NUM + ":" + reg.VS_USER_NAME + ":" + POS+" score:"+usr.SCORE);
          }*/
          JSONObject flight = new JSONObject();
          flight.put("NAME", tab.stage.CAPTION);
          flight.put("PILOT_POSITION", /*usr.NUM_IN_GROUP*/ POS);

          flight.put("PILOT_LAP", usr.LAPS);
          flight.put("BEST_LAP", StageTab.getTimeIntervel(usr.BEST_LAP, "."));
          flight.put("GROUP_NUM", usr.GROUP_NUM);
          flight.put("RACE_LAPS", tab.stage.LAPS);
          flight.put("RACE_DIST", tab.mainForm.activeRace.LAP_DISTANCE);

          salt_vals += tab.stage.CAPTION + POS + StageTab.getTimeIntervel(usr.BEST_LAP, ".") + usr.GROUP_NUM;
          // $flight['NAME'].$flight['PILOT_POSITION'].$flight['BEST_LAP'].$flight['GROUP_NUM'];

          POS++;

          FLIGHTS.put(flight);
          jsonObj.put("FLIGHTS", FLIGHTS);
          users_array.put(jsonObj);

          /*
            JSONArray laps_array = new JSONArray();
            for (int i = 1; i <= tab.stage.LAPS; i++) {
              // lap time
              VS_RACE_LAP lap = null;
              try {
                if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
                  lap = tab.stage.laps_check_reg_id.get("" + usr.GROUP_NUM).get("" + usr.REG_ID).get("" + i);
                } else {
                  lap = tab.stage.laps_check_reg_id.get("" + usr.GROUP_NUM).get("" + usr.VS_PRIMARY_TRANS).get("" + i);
                }
              } catch (Exception e) {
              }
              if (lap != null) {
                JSONObject lap_obj = new JSONObject();
                lap_obj.put("LAP", lap.LAP);
                lap_obj.put("ID", lap.ID);
                lap_obj.put("REG_ID", lap.REG_ID);
                lap_obj.put("TRANSPONDER_TIME", lap.TRANSPONDER_TIME);
                lap_obj.put("TRANSPONDER_ID", lap.TRANSPONDER_ID);
                lap_obj.put("GROUP_NUM", lap.GROUP_NUM);
                lap_obj.put("BASE_ID", lap.BASE_ID);
                lap_obj.put("NUMBER_PACKET", lap.NUMBER_PACKET);
                laps_array.put(lap_obj);
              }
            }

            jsonObj.put("laps", laps_array);
            users_array.put(jsonObj);*/
        }
      }
    }
    json.put("PILOTS", users_array);

    String authCode = Tools.getPreference("AUTORIZE_CODE_" + REG_SITE_NAME);
    
    //JOptionPane.showMessageDialog(null, "мы солим : '" + salt_vals + authCode+"'");
    System.out.println("salt+auth : '" + salt_vals + authCode + "'");
    String hash = MD5((salt_vals /*+ authCode*/));
    System.out.println("md5 : '" + hash + "'");
    json.put("HASH", hash);

    message = json.toString();

    new File(IRegSite.PATH_ONLINE_UPDATE).mkdirs();
    String fileName = IRegSite.PATH_ONLINE_UPDATE + "/" + jsonFileName + ".json";
    String fileNameCashe = IRegSite.PATH_ONLINE_UPDATE + "/" + jsonFileName + "_cashe.json";
    new File(fileName).delete();
    try {
      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);
      out.write(message);
      out.close();
    } catch (Exception e) {
      if (showMessages) {
        JOptionPane.showMessageDialog(null, "JSON creation is eror : " + e.getMessage());
      }
    }

    boolean pleaseUpdate = true;
    if (showMessages == false) { // it's automation job
      if (isSameContent(fileName, fileNameCashe)) {
        pleaseUpdate = false;
      } else {
        System.out.println("Try to update '" + fileName + "' to " + REG_SITE_URL);
      }
    }

    if (pleaseUpdate) {
      try {
        String response = uploadToWeb(fileName, message, authCode, removeAllStages);
        response = response.replaceAll("<br>", "\n");
        FileTools.copyFile(new File(fileName), new File(fileNameCashe));
        if (showMessages) {
          JOptionPane.showMessageDialog(null, "Result:\n" + response);
        }
      } catch (Exception e) {
        if (showMessages) {
          JOptionPane.showMessageDialog(null, "Transmission is error : " + e.getMessage());
        }
      }
    }
    return false;
  }

  private void sendFile(OutputStream out, String name, InputStream in, String fileName) throws UnsupportedEncodingException, IOException {
    String o = "Content-Disposition: form-data; name=\"" + URLEncoder.encode(name, "UTF-8")
            + "\"; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"\r\n\r\n";
    out.write(o.getBytes(StandardCharsets.UTF_8));
    byte[] buffer = new byte[2048];
    for (int n = 0; n >= 0; n = in.read(buffer)) {
      out.write(buffer, 0, n);
    }
    out.write("\r\n".getBytes(StandardCharsets.UTF_8));
  }

  private void sendField(OutputStream out, String name, String field) throws UnsupportedEncodingException, IOException {
    String o = "Content-Disposition: form-data; name=\""
            + URLEncoder.encode(name, "UTF-8") + "\"\r\n\r\n";
    out.write(o.getBytes(StandardCharsets.UTF_8));
    out.write(URLEncoder.encode(field, "UTF-8").getBytes(StandardCharsets.UTF_8));
    out.write("\r\n".getBytes(StandardCharsets.UTF_8));
  }

  public String uploadToWeb(String fileName, String jsonContent, String authCode, boolean removeAllStages) throws MalformedURLException, IOException {

    URL url = new URL(REG_SITE_URL_FOR_UPLOAD);
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("POST"); // PUT is another valid option
    http.setDoOutput(true);
    http.setDoInput(true);

    String boundary = UUID.randomUUID().toString();
    byte[] boundaryBytes
            = ("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8);
    byte[] finishBoundaryBytes
            = ("--" + boundary + "--").getBytes(StandardCharsets.UTF_8);
    /*http.setRequestProperty("Content-Type",
            "multipart/form-data; charset=UTF-8; boundary=" + boundary);

    http.setChunkedStreamingMode(0);

    try (OutputStream out = http.getOutputStream()) {
      //out.write(boundaryBytes);
      out.write( jsonContent.getBytes() ); 
      //out.write(finishBoundaryBytes);
    }*/

    //http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
   // http.
    
    http.connect();
    try (OutputStream os = http.getOutputStream()) {
      os.write(jsonContent.getBytes("UTF-8"));
    }
    
    String response = "";
    int responseCode = http.getResponseCode();

    if (responseCode == HttpsURLConnection.HTTP_OK) {
      String line;
      BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
      while ((line = br.readLine()) != null) {
        response += line;
      }
    } else {
      response = "";

    }
    //System.out.println("\response : " + response);        

    return response;
  }

}
