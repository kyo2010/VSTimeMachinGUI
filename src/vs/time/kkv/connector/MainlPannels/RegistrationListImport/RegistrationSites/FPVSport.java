package vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites;

import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import KKV.Utils.UserException;
import de.dfki.lt.tools.tokenizer.FileTools;
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
import java.util.List;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.RegistrationTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
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
public class FPVSport extends IRegSite {

  {
    REG_SITE_URL = "http://fpvsport.ru/getRaces.php";
    //REG_SITE_URL = "http://jteam.ru/pilots/getRaces.php";
    //REG_SITE_URL_FOR_UPLOAD = "http://localhost/pilots/uploadStage.php";
    REG_SITE_URL_FOR_UPLOAD = "http://fpvsport.ru/uploadStage.php";
    REG_SITE_NAME = "FPVSport.ru";
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
          race.RACE_DATE = new JDEDate();
          race.RACE_DATE.setJDEDateAsDDMMYYYY(json_race.getString("date_event"), ".");
          race.RACE_NAME = json_race.getString("name");
          races.add(race);

          JSONArray json_users = json_race.getJSONArray("pilots");
          for (int j = 0; j < json_users.length(); j++) {
            JSONObject json_pilot = json_users.getJSONObject(j);
            VS_REGISTRATION user = new VS_REGISTRATION();
            user.NUM = j + 1;
            user.IS_ACTIVE = 1;
            user.VS_SOUND_EFFECT = 1;
            user.PILOT_TYPE = 0;
            user.WEB_SID = json_pilot.getString("id");
            user.WEB_SYSTEM = getSystemName();
            user.VS_USER_NAME = json_pilot.getString("osd_name");
            user.E_MAIL = json_pilot.getString("email");
            user.FIRST_NAME = json_pilot.getString("fname");
            user.SECOND_NAME = json_pilot.getString("lname");
            if (user.VS_USER_NAME == null || user.VS_USER_NAME.equals("")) {
              user.VS_USER_NAME = user.FIRST_NAME + " " + user.SECOND_NAME;
            }
            user.PICTURE_FILENAME = "";
            user.PHOTO = "";
            try {
              user.PICTURE_FILENAME = json_pilot.getString("PICTURE").trim();
              user.WEB_PHOTO_URL = user.PICTURE_FILENAME;
              boolean PICTURE_OK = "1".equalsIgnoreCase(json_pilot.getString("PICTURE_OK"));
              //boolean PICTURE_OK  =true;
              if (PICTURE_OK && !user.PICTURE_FILENAME.equalsIgnoreCase("")) {
                user.PHOTO = user.PICTURE_FILENAME;
              }
            } catch (Exception e) {
            };

            if (user.VS_USER_NAME.equalsIgnoreCase("KKV")) {
              int y = 0;
            }

            String trns_st = json_pilot.getString("transID");
            try {
              String[] trans = trns_st.split(";");
              try {
                if (trans != null && trans.length > 0) {
                  user.VS_TRANS1 = Integer.parseInt(trans[0].trim());
                }
              } catch (Exception ein1) {
              }

              try {
                if (trans != null && trans.length > 1) {
                  user.VS_TRANS2 = Integer.parseInt(trans[1].trim());
                }
              } catch (Exception ein1) {
              }

              try {
                if (trans != null && trans.length > 2) {
                  user.VS_TRANS3 = Integer.parseInt(trans[2].trim());
                }
              } catch (Exception ein1) {
              }
              try {
                user.REGION = json_pilot.getString("region");
              } catch (Exception ein1) {
              }
              try {
                user.FAI = json_pilot.getString("FAI");
              } catch (Exception ein1) {
              }

            } catch (Exception ein) {
            }
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
    IRegSite site = new FPVSport();
    try {
      System.out.println("Start");
      site.load();
      for (VS_RACE race : site.getRaces()) {
        System.out.println(race.RACE_NAME);
        for (VS_REGISTRATION user : race.users) {
          System.out.println("  " + user.VS_USER_NAME + " [" + user.FIRST_NAME + " " + user.SECOND_NAME + "] " + user.VS_TRANS1);
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

  @Override
  public boolean uploadToWebSystem(RegistrationTab regTab, StageTab tab, boolean removeAllStages, boolean showMessages) {

    String jsonFileName = "";

    String message = "";
    JSONObject json = new JSONObject();

    if (regTab != null) {
      jsonFileName = regTab.mainForm.activeRace.RACE_ID + "_registration";

      json.put("RACE_ID", regTab.mainForm.activeRace.RACE_ID);
      json.put("WEB_RACE_ID", regTab.mainForm.activeRace.WEB_RACE_ID);
      json.put("STAGE_ID", regTab.mainForm.activeRace.RACE_ID);
      json.put("CAPTION", "Registration");

      JSONObject table_obj = new JSONObject();

      JSONArray table_data = new JSONArray();
      int i = 0;
      table_obj.put("_HEADER_" + i, "Num");
      table_obj.put("HEADER_" + i, "Num");
      table_obj.put("CELLID_" + i, "INT");
      table_obj.put("MIN_WIDTH_" + i, "50");

      i = 1;
      table_obj.put("_HEADER_" + i, "OSD Name");
      table_obj.put("HEADER_" + i, "OSD Name");
      table_obj.put("CELLID_" + i, "TXT");
      table_obj.put("MIN_WIDTH_" + i, "200");

      i = 2;
      table_obj.put("_HEADER_" + i, "Trans");
      table_obj.put("HEADER_" + i, "Trans");
      table_obj.put("CELLID_" + i, "TXT");
      table_obj.put("MIN_WIDTH_" + i, "150");

      List<VS_REGISTRATION> regs = regTab.regModelTable.rows;

      int rows = 0;
      int cols = 3;

      for (VS_REGISTRATION reg : regs) {
        if (reg.IS_ACTIVE == 1) {
          rows++;
        }
      }

      table_obj.put("COLS", cols);
      table_obj.put("ROWS", rows);
      table_obj.put("CAPTION", "Registration");

      int row = 0;
      for (VS_REGISTRATION reg : regs) {
        if (reg.IS_ACTIVE == 1) {
          JSONObject table_cell = new JSONObject();
          table_cell.put("COL", 0);
          table_cell.put("ROW", row);
          table_cell.put("VALUE", (row + 1));
          table_data.put(table_cell);

          table_cell = new JSONObject();
          table_cell.put("COL", 1);
          table_cell.put("ROW", row);
          table_cell.put("VALUE", reg.getFullUserName());
          table_data.put(table_cell);

          table_cell = new JSONObject();
          table_cell.put("COL", 2);
          table_cell.put("ROW", row);
          String val = "";
          table_cell.put("VALUE", reg.getTransponders());
          table_data.put(table_cell);
          row++;
        }
      }
      table_obj.put("TABLE_DATA", table_data);
      json.put("TABLE", table_obj);

    } else {
      jsonFileName = tab.mainForm.activeRace.RACE_ID + "_" + tab.stage.ID;

      json.put("RACE_ID", tab.mainForm.activeRace.RACE_ID);
      json.put("WEB_RACE_ID", tab.mainForm.activeRace.WEB_RACE_ID);
      json.put("STAGE_ID", tab.stage.ID);
      json.put("LAPS", tab.stage.LAPS);
      json.put("CAPTION", tab.stage.CAPTION);
      json.put("STAGE_TYPE", tab.stage.STAGE_TYPE);
      json.put("SORT_TYPE", tab.stage.SORT_TYPE);
      json.put("PARENT_STAGE_ID", tab.stage.PARENT_STAGE_ID);
      json.put("PILOTS_FOR_NEXT_ROUND", tab.stage.PILOTS_FOR_NEXT_ROUND);
      json.put("PILOT_TYPE", tab.stage.PILOT_TYPE);

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

      for (Integer groupNum : tab.stage.groups.keySet()) {
        List<VS_STAGE_GROUPS> users = tab.stage.groups.get(groupNum).users;
        if (users != null) {
          for (VS_STAGE_GROUPS usr : users) {
            JSONObject jsonObj = new JSONObject();
            VS_REGISTRATION reg = usr.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
            if (reg != null) {
              if (reg.WEB_SYSTEM.equalsIgnoreCase(getSystemName())){
                jsonObj.put("WEB_SID", reg.WEB_SID);              
                jsonObj.put("WEB_SYSTEM", reg.WEB_SYSTEM);
              }else{
                jsonObj.put("WEB_SID", "");              
                jsonObj.put("WEB_SYSTEM", "");
              }
              jsonObj.put("FIRST_NAME", reg.FIRST_NAME);
              jsonObj.put("SECOND_NAME", reg.SECOND_NAME);
              jsonObj.put("VS_USER_NAME", reg.VS_USER_NAME);
              jsonObj.put("VS_TRANS1", reg.VS_TRANS1);
              jsonObj.put("VS_TRANS2", reg.VS_TRANS2);
              jsonObj.put("VS_TRANS3", reg.VS_TRANS3);
            } else {
              jsonObj.put("WEB_SID", "");
              jsonObj.put("WEB_SYSTEM", "");
              jsonObj.put("FIRST_NAME", "");
              jsonObj.put("SECOND_NAME", "");
              jsonObj.put("VS_USER_NAME", usr.PILOT);
              jsonObj.put("VS_TRANS1", "");
              jsonObj.put("VS_TRANS2", "");
              jsonObj.put("VS_TRANS3", "");
            }
            jsonObj.put("PILOT", usr.PILOT);
            if (usr.registration!=null){
              jsonObj.put("email", usr.registration.E_MAIL);
            }else{
              jsonObj.put("email", "");
            }
            jsonObj.put("CHANNEL", usr.CHANNEL);
            jsonObj.put("BEST_LAP", usr.BEST_LAP);
            jsonObj.put("RACE_TIME", usr.RACE_TIME);
            jsonObj.put("LAPS", usr.LAPS);
            jsonObj.put("GROUP_NUM", usr.GROUP_NUM);
            jsonObj.put("NUM_IN_GROUP", usr.NUM_IN_GROUP);

            jsonObj.put("GID", usr.GID);
            jsonObj.put("FIRST_LAP", usr.FIRST_LAP);
            jsonObj.put("IS_FINISHED", usr.IS_FINISHED);
            jsonObj.put("LOSE", usr.LOSE);
            jsonObj.put("WIN", usr.WIN);
            jsonObj.put("SCORE", usr.SCORE);
            jsonObj.put("GROUP_FINAL", usr.GROUP_FINAL);
            jsonObj.put("GROUP_HALF_FINAL", usr.GROUP_HALF_FINAL);
            jsonObj.put("GROUP_QUART_FINAL", usr.GROUP_QUART_FINAL);
            jsonObj.put("RACE_TIME_FINAL", usr.RACE_TIME_FINAL);
            jsonObj.put("RACE_TIME_HALF_FINAL", usr.RACE_TIME_HALF_FINAL);
            jsonObj.put("RACE_TIME_QUART_FINAL", usr.RACE_TIME_QUART_FINAL);
            jsonObj.put("QUAL_POS", usr.QUAL_POS);
            jsonObj.put("QUAL_TIME", usr.QUAL_TIME);
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
            users_array.put(jsonObj);
          }
        }
      }
      json.put("users", users_array);

      JSONObject table_obj = new JSONObject();

      int cols = tab.stageTableAdapter.getColumnCount();
      int rows = tab.stageTableAdapter.getRowCount();

      table_obj.put("COLS", cols);
      table_obj.put("ROWS", rows);
      table_obj.put("CAPTION", tab.stage.CAPTION);

      JSONArray table_data = new JSONArray();
      for (int i = 0; i < cols; i++) {
        table_obj.put("_HEADER_" + i, tab.stageTableAdapter.getColumnName(i));
        table_obj.put("HEADER_" + i, tab.stageTableAdapter.getColumnLocaleName(i));
        table_obj.put("CELLID_" + i, tab.stageTableAdapter.getColumnCellID(i));
        table_obj.put("MIN_WIDTH_" + i, tab.stageTableAdapter.getMinWidth(i));
      }

      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          JSONObject table_cell = new JSONObject();
          table_cell.put("COL", col);
          table_cell.put("ROW", row);
          String val = "";
          try {
            val = tab.stageTableAdapter.getValueAt(row, col).toString();
          } catch (Exception e) {
          }
          table_cell.put("VALUE", val);
          table_data.put(table_cell);
        }
      }

      table_obj.put("TABLE_DATA", table_data);
      json.put("TABLE", table_obj);

    }
    message = json.toString();

    new File(IRegSite.PATH_ONLINE_UPDATE).mkdirs();
    String fileName = IRegSite.PATH_ONLINE_UPDATE + "/" + jsonFileName + ".json";
    String fileNameCashe = IRegSite.PATH_ONLINE_UPDATE + "/" + jsonFileName + "_cashe.json";
    new File(fileName).delete();
    try {
      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8);
      out.write(message); 
      out.close();
    } catch (Exception e) {
      if (showMessages) {
        JOptionPane.showMessageDialog(null, "JSON creation is eror : " + e.getMessage());
      }
    }

    boolean pleaseUpdate = true;
    if (showMessages == false) { // it's automation job
      if (isSameContent(fileName,fileNameCashe)){
        pleaseUpdate = false;        
      }else{
        System.out.println("Try to update '"+fileName+"' to "+REG_SITE_URL);      
      }
    }

    if (pleaseUpdate) {
      try {
        String authCode = Tools.getPreference("AUTORIZE_CODE_" + REG_SITE_NAME);
        String response = uploadToWeb(fileName, authCode, removeAllStages);
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

  public String uploadToWeb(String fileName, String authCode, boolean removeAllStages) throws MalformedURLException, IOException {

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
    http.setRequestProperty("Content-Type",
            "multipart/form-data; charset=UTF-8; boundary=" + boundary);

    http.setChunkedStreamingMode(0);

    try {
      OutputStream out = http.getOutputStream();
      out.write(boundaryBytes);
      sendField(out, "mode", "upload");
      out.write(boundaryBytes);
      sendField(out, "auth", authCode);
      out.write(boundaryBytes);
      if (removeAllStages) {
        sendField(out, "remove_all", "1");
        out.write(boundaryBytes);
      }
      // Send our file
      try{
        InputStream file = new FileInputStream(fileName);
        sendFile(out, "JSonFile", file, fileName);
      }catch(Exception e1){
        e1.printStackTrace();
      }

      // Finish the request
      out.write(finishBoundaryBytes);
    }catch(Exception e){
      e.printStackTrace();
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
