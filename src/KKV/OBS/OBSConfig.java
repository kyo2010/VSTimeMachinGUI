/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.OBS;

import com.alibaba.fastjson.JSONObject;
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
  public boolean useDisconnect = true;
  public boolean sendInfo = true;

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
  
  public class Task extends Thread{
    int TASK_ID = 0;
    String TASK_INFO = "";
    static final int TASK_FOR_RACE = 1;
    static final int TASK_FOR_FINISH = 2;
    static final int TASK_FOR_INVATE = 3;
    
    public Task(int TASK_ID, String TASK_INFO){
      this.TASK_ID = TASK_ID;
      this.TASK_INFO = TASK_INFO;   
      start();
    }
    
    @Override
    public void run(){
      if (TASK_ID==TASK_FOR_RACE){
        __changeSceneForRace(TASK_INFO);
      }
      if (TASK_ID==TASK_FOR_FINISH){
        __changeSceneForFinish(TASK_INFO);
      }
      if (TASK_ID==TASK_FOR_INVATE){
        __changeSceneForInvate(TASK_INFO);
      }
    }
  };

  public void save() {
    VS_SETTING.setParam(con, "OBS_HOST", OBS_HOST);
    VS_SETTING.setParam(con, "OBS_PORT", OBS_PORT);
    VS_SETTING.setParam(con, "OBS_USE_WEB_SOCKET", OBS_USE_WEB_SOCKET ? "1" : "0");
    VS_SETTING.setParam(con, "OBS_AUTO_RECORDING", OBS_AUTO_RECORDING ? "1" : "0");
    VS_SETTING.setParam(con, "OBS_SCENE_RACE", OBS_SCENE_RACE);
    VS_SETTING.setParam(con, "OBS_SCENE_FINISH", OBS_SCENE_FINISH);
    VS_SETTING.getParam(con, "OBS_SCENE_INVATE", OBS_SCENE_INVATE);
  }
  
  public void changeSceneForRace(String info) {
    new Task(Task.TASK_FOR_RACE, info);
  }
  public void changeSceneForFinish(String info) {
    new Task(Task.TASK_FOR_FINISH, info); 
  }
  public void changeSceneForInvate(String info) {
    new Task(Task.TASK_FOR_INVATE, info);
  }

  public void __changeSceneForRace(String info) {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (OBS_AUTO_RECORDING || !OBS_SCENE_RACE.equalsIgnoreCase("")) {
      try {
        System.out.println("OBS Race");
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);        
        if (!OBS_SCENE_RACE.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_RACE, null);
        }
        if (OBS_AUTO_RECORDING) {
          obsSocket.api.StartRecording();
        }        
        if (sendInfo) obsSocket.api.SetText("info", info, func);         
        if (useDisconnect) obsSocket.disconnect();
      } catch (Exception e) {
      }
    }
  }

  public void __changeSceneForFinish(String info) {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (OBS_AUTO_RECORDING || !OBS_SCENE_FINISH.equalsIgnoreCase("")) {
      System.out.println("OBS Finish");
      try {
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);
        if (OBS_AUTO_RECORDING) {
          obsSocket.api.StopRecording();
        }
        if (!OBS_SCENE_FINISH.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_FINISH, null);
        }
        if (sendInfo) obsSocket.api.SetText("info",info, func);   
        if (useDisconnect) obsSocket.disconnect();
      } catch (Exception e) {
      }
    }
  }

  public void __changeSceneForInvate(String info) {
    if (!OBS_USE_WEB_SOCKET) {
      return;
    }
    if (!OBS_SCENE_INVATE.equalsIgnoreCase("")) {
      System.out.println("OBS Invate");
      try {
        OBSSocket obsSocket = new OBSSocket(OBS_HOST, OBS_PORT);
        if (!OBS_SCENE_INVATE.equals("")) {
          obsSocket.api.SetCurrentScene(OBS_SCENE_INVATE, null);
        }
        if (sendInfo) obsSocket.api.SetText("info",info, func);   
        if (useDisconnect) obsSocket.disconnect();
      } catch (Exception e) {
      }
    }

  }
  
  AdapterFunction func = new AdapterFunction(){
     @Override
	public void call(JSONObject json) {
		if(null==json)return;
		String status = json.getString("status");
		String error = json.getString("error");
		StringBuffer sb = new StringBuffer();
    System.out.println("STATUS : "+status);
    System.out.println("ERROR  : "+error);	
        }
  };
}
