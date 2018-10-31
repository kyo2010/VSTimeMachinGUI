/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import ru.nkv.var.StringVar;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.InfoForm;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import vs.time.kkv.models.VS_USERS;

/**
 * тест коммита от ККВ
 *
 * @author kyo
 */
public class StartHttpServlet extends HttpServlet {

  public static boolean USE_CACHE = false;
  MainForm mainForm = null;

  public StartHttpServlet() {
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

  public void addPilot(VS_STAGE_GROUPS pilot, int index, IVar varsPool) {
    String webPhoto = "";
    String webName = "";
    String webChannel = "";
    String webColor = "";
    String webInfo = "";
    String findColor = "";
    if (pilot != null) {
      VS_REGISTRATION reg = pilot.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
      int pWidth = 240;
      if (reg != null && !reg.PHOTO.equalsIgnoreCase("")) {
        webPhoto = "<img src='" + reg.PHOTO.replaceAll("web/", "") + "' width='" + pWidth + "px'>";
      } else {
      }
      if (pilot.color != null) {
        webColor = pilot.color.colorname.toLowerCase();
        if (pilot.CHECK_FOR_RACE==1 && 
            mainForm.lastCheckingGrpup!=null && mainForm.lastInvateGroup!=null &&    
            mainForm.lastCheckingGrpup.GROUP_NUM==mainForm.lastInvateGroup.GROUP_NUM
        ){
          findColor = pilot.color.colorname.toLowerCase();
        }  
      }
      if (webColor.equals("")) {
        webColor = "blue";
      }
      webName = pilot.PILOT;
      if (reg != null) {
        webName = reg.FIRST_NAME + " " + reg.SECOND_NAME + " [" + pilot.PILOT + "]";
      }
      String h1 = "h1";
      webName = "<div class='w3-margin-top w3-container w3-" + webColor + "' style='text-shadow:2px 1px 0 #444'><" + h1 + "><b>" + webName + "</b></" + h1 + "></div>";
      webChannel = "<div class='w3-badge w3-" + webColor + "'><b>" + pilot.CHANNEL + "</b></div>";
    }
    varsPool.addChild(new StringVar("PILOT" + index + "_PHOTO", webPhoto));
    varsPool.addChild(new StringVar("PILOT" + index + "_NAME", webName));
    varsPool.addChild(new StringVar("CH" + index, webChannel));
    varsPool.addChild(new StringVar("COLOR" + index, webColor));
    varsPool.addChild(new StringVar("FIND_COLOR" + index, findColor));        
    varsPool.addChild(new StringVar("INFO" + index, webInfo));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    mainForm = MainForm._mainForm;

    resp.setStatus(HttpStatus.SC_OK);
    resp.setHeader("Content-Type", "text/html; charset=UTF-8");
    String REFRESH_META = "<meta http-equiv=\"refresh\" content=\"3\">";

    try {
      String templ = Tools.getTextFromFile("web" + File.separator + "start-templ.ajax");

      IVar varsPool = new VarPool();
      varsPool.addChild(new StringVar("MENU", ""));
      varsPool.addChild(new StringVar("REFRESH_META", REFRESH_META));
      varsPool.addChild(new StringVar("PAGE_CONTENT", ""));
      
      String message1 = "";
      String message2 = "";
      VS_STAGE_GROUP group = null;
      if (mainForm.lastInvateGroup!=null){
        group = mainForm.lastInvateGroup;
        message1 = "Внимание на старт";
        message2 = "Приглашается группа:"+group.GROUP_NUM;        
      }
      if (mainForm.activeGroup!=null){
        group = mainForm.activeGroup;
        if (mainForm.raceTime==0){
          message1 = "Внимание";
          message2 = "Приготовились";
        }else{
          message1 = "Гонка";
          message2 = "Летит группа:"+group.GROUP_NUM;
        }       
      }        

      for (int i =0; i<4; i++){
        VS_STAGE_GROUPS user = null;
        if (group!=null && group.users.size()>i) user = group.users.get(i);
        addPilot(user, i+1, varsPool);
      }

      varsPool.addChild(new StringVar("MESSAGE1", message1));
      varsPool.addChild(new StringVar("MESSAGE2", message2));
      String html = varsPool.applyValues(templ);
      resp.getWriter().println(html);

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
      resp.getWriter().println("");
    }
  }
}
