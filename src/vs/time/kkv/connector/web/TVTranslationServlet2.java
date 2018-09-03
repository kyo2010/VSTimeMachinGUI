/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import vs.time.kkv.connector.MainlPannels.stage.StageTableData;
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
public class TVTranslationServlet2 extends HttpServlet {

  static final int MAX_TABLE_LINES = 16;
  static final long MAX_TIME_DISPLAY = 5000;

  public static boolean USE_CACHE = false;
  String templ = null;
  String templ_button = null;
  MainForm mainForm = null;

  public static final boolean SHOW_SPEED = true;

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
      try {
        if (pilot != null) {
          //if (pilot.PILOT.equals("RUBOT")){
          //  int y = 0;
          //}
          reg = pilot.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
          int pWidth = 240;
          if (isRace) {
            pWidth = 150;
          }
          if (reg != null && !reg.PHOTO.equalsIgnoreCase("")) {
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
            webName = reg.FIRST_NAME + " " + reg.SECOND_NAME + (isRace? "":"<br />")+" [" + pilot.PILOT + "]";
          }
          String h1 = "h4";
          if (isRace) {
            h1 = "h10";
          }
          webName = "<div class='w3-container w3-" + webColor + "'><" + h1 + (isRace? " ":" style='text-align: center;'")+ "><b>" + webName + "</b></" + h1 + "></div>";

          if (isRace) {
            String bestLap = "";
            String info = "";
            if (pilot.BEST_LAP != 0) {
              bestLap = "&nbsp;&nbsp;&nbsp; Лучший круг : " + StageTab.getTimeIntervel(pilot.BEST_LAP);
            }
            if (pilot.IS_FINISHED == 1) {
              info = "&nbsp;&nbsp;&nbsp; Bремя :  " + StageTab.getTimeIntervel(pilot.RACE_TIME);
            }
            webInfo = "<div class='w3-container w3-white' style='padding:0.01em 8px'><p style='margin: 8px; font-size: 13px;'><b>Кругов : " + pilot.LAPS + " " + bestLap + info + "</b></p></div>";
          }

          webChannel = "<div class='w3-badge w3-" + webColor + "'><b>" + pilot.CHANNEL + "</b></div>";
        }

      } catch (Exception ein) {
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
    //if (isRace) html = Tools.getTextFromFile("web"+File.separator+"tv.template.race.htm");
    //else 
    if(isRace){
        html = Tools.getTextFromFile("web"+File.separator+"tv.template2.htm");
    }else{
        html = Tools.getTextFromFile("web"+File.separator+"tv.template2.invate.htm");
    }

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

  public class DISPLAY {
    boolean byGroup = false;    
    public VS_STAGE_GROUP virtual_group = null;
    public List<Integer> numGroups = new ArrayList();
    public List<VS_STAGE_GROUPS> users = new ArrayList();
    public DISPLAY( boolean byGroup){
      this.byGroup = byGroup;
    }
  }

  public void showStage(HttpServletRequest req, HttpServletResponse resp, VS_STAGE stage) throws ServletException, IOException {
    String html = Tools.getTextFromFile("web"+File.separator+"tv.template.blank.htm");
    String CONTENT = "";

    boolean showGroupNumber = false;
    if (stage.groups.size() > 1) {
      showGroupNumber = true;
    }

    CONTENT = "<div class='w3-container'><p>";
    CONTENT += "<table class='w3-table w3-bordered w3-border w3-xxlarge'>";
    CONTENT += "<tr class='w3-blue'>";
    if (showGroupNumber) {
      CONTENT += "<th>G</th>";
    }
    CONTENT += "<th> </th>";
    CONTENT += "<th>" + mainForm.getLocaleString("Pilot") + "</th>";
    CONTENT += "<th>" + mainForm.getLocaleString("Channel") + "</th>";
    CONTENT += "<th>" + mainForm.getLocaleString("Race Time") + "</th>";
    CONTENT += "<th>" + mainForm.getLocaleString("Best Lap") + "</th>";
    if (SHOW_SPEED) {
      CONTENT += "<th>" + mainForm.getLocaleString("Speed") + "</th>";
    }
    CONTENT += "</tr>";

    long best_lap = VS_STAGE_GROUPS.MAX_TIME;
    long best_time = VS_STAGE_GROUPS.MAX_TIME;
    int lines = 0;
    List<DISPLAY> displays = new ArrayList<>();    

    boolean show_by_group = false;
    if (stage.groups.size() == 1) {
      show_by_group = false;
      VS_STAGE_GROUP group = stage.groups.get(0);
      displays.add(new DISPLAY(show_by_group));
      for (VS_STAGE_GROUPS user : group.users){
        if (lines + 1 <= MAX_TABLE_LINES) {
        } else {
          lines = 0;
          displays.add(new DISPLAY(show_by_group));
        }
        lines++;
        displays.get(displays.size() - 1).users.add(user);
        if (user.BEST_LAP < best_lap && user.BEST_LAP != 0) {
            best_lap = user.BEST_LAP;
          }
          if (user.RACE_TIME < best_time && user.RACE_TIME != 0) {
            best_time = user.RACE_TIME;
          }
      }       
    }else{
      show_by_group = true;
      displays.add(new DISPLAY(show_by_group));
      for (int numGroup : stage.groups.keySet()) {
        VS_STAGE_GROUP group = stage.groups.get(numGroup);
        if (lines + group.users.size() <= MAX_TABLE_LINES) {
        } else {
          lines = 0;
          displays.add(new DISPLAY(show_by_group));
        }
        displays.get(displays.size() - 1).numGroups.add(numGroup);
        lines = lines + group.users.size();
        for (VS_STAGE_GROUPS user : group.users) {
          if (user.BEST_LAP < best_lap && user.BEST_LAP != 0) {
            best_lap = user.BEST_LAP;
          }
          if (user.RACE_TIME < best_time && user.RACE_TIME != 0) {
            best_time = user.RACE_TIME;
          }
        }
      }      
    }
    if (best_time == VS_STAGE_GROUPS.MAX_TIME) {
      best_time++;
    }
    if (best_lap == VS_STAGE_GROUPS.MAX_TIME) {
      best_lap++;
    }

    DISPLAY current_dispaly = null;
    if (displays.size() > 0) {
      long t = Calendar.getInstance().getTimeInMillis();
      long d = t - mainForm.unRaceTime;
      long count_dispalys = d / MAX_TIME_DISPLAY;
      int disp_number = (int) count_dispalys % displays.size();
      if (disp_number >= displays.size()) {
        current_dispaly = displays.get(0);
      } else {
        current_dispaly = displays.get(disp_number);
        //System.out.println("Current TV display number is:"+disp_number);
      }
    }

    /*for (int numGroup : stage.groups.keySet()) {
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
    }*/
    
    
    if (current_dispaly!=null && current_dispaly.byGroup==false){
      VS_STAGE_GROUP v_group = new VS_STAGE_GROUP(null);
      v_group.GROUP_INDEX = 1;
      v_group.GROUP_NUM = -1;
      current_dispaly.numGroups.add(1);
      current_dispaly.virtual_group = v_group;
    }
    
    int next_group = -1;
    if (current_dispaly != null) {           
      for (int numGroup : current_dispaly.numGroups) {
        
        VS_STAGE_GROUP group = null;
        int index = 0;
        List<VS_STAGE_GROUPS> users = null;
        if (current_dispaly.byGroup==true) {
          group = stage.groups.get(numGroup);
          users = group.users;
        }
        if (current_dispaly.byGroup==false) {
          group = current_dispaly.virtual_group;
          users = current_dispaly.users;
        }
        
        for (VS_STAGE_GROUPS user : users) {
          VS_REGISTRATION reg = user.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
          String surname = "";
          if (reg != null) {
            surname = reg.SECOND_NAME + ((reg.FIRST_NAME.length() > 0) ? (" " + reg.FIRST_NAME.substring(0, 1) + ".") : "") + " ";
          }
          CONTENT += "<tr class='w3-white'>";
          if (showGroupNumber && index == 0) {
            String color = "w3-green";
            if (user.IS_FINISHED == 1) {
              color = "w3-red";
              next_group = group.GROUP_INDEX + 1;
            } else {
              if (group.GROUP_INDEX == next_group) {
                color = "w3-yellow";
              }
            }
            CONTENT += "<th rowspan='" + users.size() + "'>"
                    + "<span class=\"w3-badge " + color + " 1w3-large\">&nbsp;"
                    + (group.GROUP_NUM==-1?"":""+group.GROUP_NUM)
                    + "&nbsp;</span>"
                    + "</th>";
          }

          String pilotName = surname + " " + user.PILOT;
          /*if (pilotName.length()>25){
            if (surname.length()>user.PILOT.length()) pilotName = surname;
            else pilotName = user.PILOT;
          }*/

          CONTENT += "<th>" +  (current_dispaly.byGroup?(index + 1):user.NUM_IN_GROUP) + "</th>";
          CONTENT += "<th w3-xlarge >" + pilotName + "</th>";
          String color = "";
          try {
            VSColor vs_color = VSColor.getColorForChannel(user.CHANNEL, stage.CHANNELS, stage.COLORS);
            color = vs_color.w3css;
          } catch (Exception e) {
          }

          CONTENT += "<th>"
                  + "<span class=\"w3-badge " + color + " 1w3-large\">&nbsp;"
                  + user.CHANNEL
                  + "&nbsp;</span>"
                  + "</th>";
          CONTENT += "<th " + (user.RACE_TIME == best_time ? "class='w3-red'" : "") + ">" + (user.RACE_TIME == 0 ? "" : StageTab.getTimeIntervel(user.RACE_TIME)) + "</th>";
          CONTENT += "<th " + (user.BEST_LAP == best_lap ? "class='w3-red'" : "") + ">" + (user.BEST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.BEST_LAP)) + "</th>";
          if (SHOW_SPEED) {
            String speed = StageTab.getFlightSpeed(mainForm.activeRace, user.BEST_LAP);
            if (!speed.equals("")) {
              speed += " " + mainForm.getLocaleString("km/h");
            }
            CONTENT += "<th " + (user.BEST_LAP == best_lap ? "class='w3-red'" : "") + ">" + speed + "</th>";
          }
          CONTENT += "</tr>";
          index++;
        }
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
        String html = Tools.getTextFromFile("web"+File.separator+"tv.template.blank.htm");
        String CONTENT = "";
        IVar varsPool = new VarPool();
        varsPool.addChild(new StringVar("CONTENT", CONTENT));
        varsPool.addChild(new StringVar("BACKGROUND", mainForm.BACKGROUND_FOR_TV));
        String outHtml = varsPool.applyValues(html);
        resp.getWriter().println(outHtml);
      }
    } catch (Exception e) {
      resp.getWriter().println(e.toString() + ". " + Tools.traceError(e));
      System.out.println(e);
    }
  }
}
