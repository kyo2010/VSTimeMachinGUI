/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.OBS;

import java.sql.Connection;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class OBSConfig {

  public String OBS_HOST = "";
  public String OBS_PORT = "";
  public boolean OBS_USE_WEB_SOCKET = false;
  public boolean OBS_AUTO_RECORDING = false;
  public String OBS_SCENE_RACE = "";
  public String OBS_SCENE_FINISH = "";
  public String OBS_SCENE_INVATE = "";

  Connection con;

  public OBSConfig(Connection con) {
    this.con = con;
    read();
  }

  public void read() {
    OBS_HOST = VS_SETTING.getParam(con, "OBS_HOST", "localhost");
    OBS_PORT = VS_SETTING.getParam(con, "OBS_PORT", "4444");
    OBS_USE_WEB_SOCKET = (VS_SETTING.getParam(con, "OBS_USE_WEB_SOCKET", "0").equalsIgnoreCase("1"));
    OBS_AUTO_RECORDING = (VS_SETTING.getParam(con, "OBS_AUTO_RECORDING", "0").equalsIgnoreCase("1"));
    OBS_SCENE_RACE = VS_SETTING.getParam(con, "OBS_SCENE_RACE", "dwr");
    OBS_SCENE_FINISH = VS_SETTING.getParam(con, "OBS_SCENE_FINISH", "camera");
    OBS_SCENE_INVATE = VS_SETTING.getParam(con, "OBS_SCENE_INVATE", "");
  }

  public void save() {
    VS_SETTING.setParam(con, "OBS_HOST", OBS_HOST);
    VS_SETTING.setParam(con, "OBS_PORT", OBS_PORT);
    VS_SETTING.setParam(con, "OBS_USE_WEB_SOCKET", OBS_USE_WEB_SOCKET ? "1" : "0");
    VS_SETTING.setParam(con, "OBS_AUTO_RECORDING", OBS_AUTO_RECORDING ? "1" : "0");
    VS_SETTING.setParam(con, "OBS_SCENE_RACE", OBS_SCENE_RACE);
    VS_SETTING.setParam(con, "OBS_SCENE_FINISH", OBS_SCENE_FINISH);
    VS_SETTING.getParam(con, "OBS_SCENE_INVATE", OBS_SCENE_INVATE);
  }

  public void changeSceneForRace() {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (OBS_AUTO_RECORDING || !OBS_SCENE_RACE.equalsIgnoreCase("")) {
      try {
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);        
        if (!OBS_SCENE_RACE.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_RACE, null);
        }
        if (OBS_AUTO_RECORDING) {
          obsSocket.api.StartRecording();
        }
        obsSocket.disconnect();
      } catch (Exception e) {
      }
    }
  }

  public void changeSceneForFinish() {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (OBS_AUTO_RECORDING || !OBS_SCENE_FINISH.equalsIgnoreCase("")) {
      try {
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);
        if (OBS_AUTO_RECORDING) {
          obsSocket.api.StopRecording();
        }
        if (!OBS_SCENE_FINISH.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_FINISH, null);
        }
        obsSocket.disconnect();
      } catch (Exception e) {
      }
    }
  }

  public void changeSceneForInvate() {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (!OBS_SCENE_INVATE.equalsIgnoreCase("")) {
      try {
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);
        if (!OBS_SCENE_INVATE.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_INVATE, null);
        }
        obsSocket.disconnect();
      } catch (Exception e) {
      }
    }

  }
}
