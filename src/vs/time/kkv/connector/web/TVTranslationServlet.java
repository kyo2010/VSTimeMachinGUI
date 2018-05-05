/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
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

/**
 *
 * @author kyo
 */
public class TVTranslationServlet extends HttpServlet {

  public static boolean USE_CACHE = false;
  String templ = null;
  String templ_button = null;
  MainForm mainForm = null;

  public class PilotInfo {

    int index = 0;
    VS_STAGE_GROUPS pilot = null;
    VS_REGISTRATION reg = null;

    public PilotInfo(int index, VS_STAGE_GROUPS pilot) {
      this.pilot = pilot;
      this.index = index;
    }

    public boolean addPilotInfo(IVar varsPool, boolean isRace) {      
      boolean res = true;
      String webPhoto = "";
      String webName = "";
      String webChannel = "";
      String webColor = "";
      String webInfo = "";
      try{      
      if (pilot != null) {
        //if (pilot.PILOT.equals("RUBOT")){
        //  int y = 0;
        //}
        reg = pilot.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
        int pWidth = 240;
        if (isRace) {
          pWidth = 150;
        }
        if (reg!=null && !reg.PHOTO.equalsIgnoreCase("")) {
          webPhoto = "<img src='" + reg.PHOTO.replaceAll("web/", "") + "' width='" + pWidth + "px' class='w3-circle'>";
        } else {

        }

        if (pilot.color != null) {
          webColor = pilot.color.colorname.toLowerCase();
        }
        if (webColor.equals("")) {
          webColor = "blue";
        }
        webName = pilot.PILOT;
        if (reg != null) {
          webName = reg.FIRST_NAME + " " + reg.SECOND_NAME + " [" + pilot.PILOT + "]";
        }
        String h1 = "h1";
        if (isRace) {
          h1 = "h2";
        }
        webName = "<div class='w3-margin-top w3-container w3-" + webColor + "' style='text-shadow:2px 1px 0 #444'><" + h1 + "><b>" + webName + "</b></" + h1 + "></div>";

        if (isRace) {
          String bestLap = "";
          String info = "";
          if (pilot.BEST_LAP != 0) {
            bestLap = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Лучший круг : " + StageTab.getTimeIntervel(pilot.BEST_LAP);
          }
          if (pilot.IS_FINISHED == 1) {
            info = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Общее время :  " + StageTab.getTimeIntervel(pilot.RACE_TIME);
          }
          webInfo = "<div class='w3-container w3-white'><h3><b>Кругов : " + pilot.LAPS + " " + bestLap + info + "</b></h3></div>";
        }

        webChannel = "<div class='w3-badge w3-" + webColor + "'><b>" + pilot.CHANNEL + "</b></div>";
      }
      
      }catch(Exception ein){
        res = false;
      }

      varsPool.addChild(new StringVar("PILOT" + index + "_PHOTO", webPhoto));
      varsPool.addChild(new StringVar("PILOT" + index + "_NAME", webName));
      varsPool.addChild(new StringVar("CH" + index, webChannel));
      varsPool.addChild(new StringVar("COLOR" + index, webColor));
      varsPool.addChild(new StringVar("INFO" + index, webInfo));
      
      return res;
    }

  }

  public void showGroup(HttpServletRequest req, HttpServletResponse resp, VS_STAGE_GROUP group, boolean isRace) throws ServletException, IOException {
    String html = "";
    //if (isRace) html = Tools.getTextFromFile("web\\tv.template.race.htm");
    //else 
    html = Tools.getTextFromFile("web\\tv.template.htm");

    String channels_st = "";
    if (group != null) {
      channels_st = group.stage.CHANNELS;
    }
    String[] channels = channels_st.split(";");

    PilotInfo[] pInfos = new PilotInfo[4];
    for (int i = 0; i < pInfos.length; i++) {
      VS_STAGE_GROUPS pilot = null;
      String channel = "";
      if (channels.length > i) {
        channel = channels[i];
      }

      //if (group != null && group.users != null && group.users.size() > i) {
        for (VS_STAGE_GROUPS usr : group.users) {
          if (usr.CHANNEL.equalsIgnoreCase(channel)) {
            pilot = usr;
            break;
          }
        }
      //}
      pInfos[i] = new PilotInfo(i + 1, pilot);
    }

    IVar varsPool = new VarPool();
    varsPool.addChild(new StringVar("TITLE", "Drone Racing System"));
    for (PilotInfo pInfo : pInfos) {
      pInfo.addPilotInfo(varsPool, isRace);
    }

    String outHtml = varsPool.applyValues(html);
    resp.getWriter().println(outHtml);
  }

  public void showStage(HttpServletRequest req, HttpServletResponse resp, VS_STAGE stage) throws ServletException, IOException {
    String html = Tools.getTextFromFile("web\\tv.template.blank.htm");
    String CONTENT = "";

    boolean showGroupNumber = false;
    if (stage.groups.size() > 1) {
      showGroupNumber = true;
    }

    CONTENT = "<div class='w3-container'><p>";
    CONTENT += "<table class='w3-table w3-bordered w3-border w3-xxxlarge'>";
    CONTENT += "<tr class='w3-blue'>";
    if (showGroupNumber) {
      CONTENT += "<th>G</th>";
    }
    CONTENT += "<th> </th>";
    CONTENT += "<th>"+mainForm.getLocaleString("Pilot")+"</th>";
    CONTENT += "<th>"+mainForm.getLocaleString("Channel")+"</th>";
    CONTENT += "<th>"+mainForm.getLocaleString("Race Time")+"</th>";
    CONTENT += "<th>"+mainForm.getLocaleString("Best Lap")+"</th>";
    CONTENT += "</tr>";

    long best_lap = VS_STAGE_GROUPS.MAX_TIME;
    long best_time = VS_STAGE_GROUPS.MAX_TIME;
    for (int numGroup : stage.groups.keySet()) {
      VS_STAGE_GROUP group = stage.groups.get(numGroup);
      for (VS_STAGE_GROUPS user : group.users) {
        if (user.BEST_LAP < best_lap && user.BEST_LAP != 0) {
          best_lap = user.BEST_LAP;
        }
        if (user.RACE_TIME < best_time && user.RACE_TIME != 0) {
          best_time = user.RACE_TIME;
        }
      }
    }
    if (best_time == VS_STAGE_GROUPS.MAX_TIME) {
      best_time++;
    }
    if (best_lap == VS_STAGE_GROUPS.MAX_TIME) {
      best_lap++;
    }

    for (int numGroup : stage.groups.keySet()) {
      VS_STAGE_GROUP group = stage.groups.get(numGroup);
      int index = 0;
      for (VS_STAGE_GROUPS user : group.users) {
        VS_REGISTRATION reg = user.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
        String surname = "";
        if (reg != null) {
          surname = reg.SECOND_NAME + ((reg.FIRST_NAME.length() > 0) ? (" " + reg.FIRST_NAME.substring(0, 1) + ".") : "") + " ";
        }
        CONTENT += "<tr class='w3-white'>";
        if (showGroupNumber && index == 0) {
          CONTENT += "<th rowspan='" + group.users.size() + "'>" + group.GROUP_NUM + "</th>";
        }
        CONTENT += "<th>" + (index + 1) + "</th>";
        CONTENT += "<th>" + surname + user.PILOT + "</th>";
        CONTENT += "<th>" + user.CHANNEL + "</th>";
        CONTENT += "<th " + (user.RACE_TIME == best_time ? "class='w3-red'" : "") + ">" + (user.RACE_TIME == 0 ? "" : StageTab.getTimeIntervel(user.RACE_TIME)) + "</th>";
        CONTENT += "<th " + (user.BEST_LAP == best_lap ? "class='w3-red'" : "") + ">" + (user.BEST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.BEST_LAP)) + "</th>";
        CONTENT += "</tr>";
        index++;
      }
    }
    CONTENT += "</table></p>";
    CONTENT += "</div>";

    IVar varsPool = new VarPool();
    varsPool.addChild(new StringVar("CONTENT", CONTENT));
    varsPool.addChild(new StringVar("BACKGROUND", mainForm.BACKGROUND_FOR_TV));
    String outHtml = varsPool.applyValues(html);
    resp.getWriter().println(outHtml);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    mainForm = MainForm._mainForm;

    resp.setStatus(HttpStatus.SC_OK);
    resp.setHeader("Content-Type", "text/html; charset=UTF-8");
    try {

      if (mainForm.activeGroup != null) {
        showGroup(req, resp, mainForm.activeGroup, true);
      } else if (mainForm.invateGroup != null) {
        showGroup(req, resp, mainForm.invateGroup, false);
      } else if (mainForm.activeStage != null) {
        showStage(req, resp, mainForm.activeStage);
      } else {
        String html = Tools.getTextFromFile("web\\tv.template.blank.htm");
        String CONTENT = "";
        IVar varsPool = new VarPool();
        varsPool.addChild(new StringVar("CONTENT", CONTENT));
        varsPool.addChild(new StringVar("BACKGROUND", mainForm.BACKGROUND_FOR_TV));
        String outHtml = varsPool.applyValues(html);
        resp.getWriter().println(outHtml);
      }
    } catch (Exception e) {
      resp.getWriter().println(e.toString() + ". " +Tools.traceError(e));
      System.out.println(e);
    }
  }
}
