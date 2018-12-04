/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import KKV.Utils.UserException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.codehaus.groovy.runtime.AbstractComparator;
import ru.nkv.var.StringVar;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.InfoForm;
import static vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.RCPilotsPro.GROUP_SCORES_COMPARATOR_2;
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
public class TVTranslationServlet extends HttpServlet {

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

    public String addPilotInfo(IVar varsPool, int isRace, String pilotTemplate) {
      boolean res = true;
      String htmlContent = "";
      String webPhoto = "";
      String webName = "";
      String webChannel = "";
      String webColor = "";
      String webColorW3 = "";
      String webInfo = "";

      String lOSD = "";
      String lPHOTO = "";
      String lFIO = "";
      String lBestLap = "";
      String lLaps = "";
      String lPlace = "";
      String lColor = "";
      String lCh = "";
      String lTimeRace = "";

      try {
        if (pilot != null) {
          //if (pilot.PILOT.equals("RUBOT")){
          //  int y = 0;
          //}
          reg = pilot.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
          int pWidth = 240;
          if (isRace == 1) {
            pWidth = 150;
          }
          if (isRace == 3) {
            pWidth = 50;
          }
          if (reg != null && !reg.PHOTO.equalsIgnoreCase("")) {
            webPhoto = "<img src='" + reg.PHOTO.replaceAll("web/", "") + "' width='" + pWidth + "px' class='w3-circle'>";
          } else {

          }

          if (pilot.color != null) {
            webColor = pilot.color.colorname.toLowerCase();
            webColorW3 = pilot.color.w3css.toLowerCase();
          }
          if (webColor.equals("")) {
            webColor = "blue";
          }
          webName = pilot.PILOT;
          if (reg != null) {
            webName = reg.FIRST_NAME + " " + reg.SECOND_NAME + " [" + pilot.PILOT + "]";
          }
          String h1 = "h1";
          if (isRace == 1) {
            h1 = "h2";
          }
          webName = "<div class='w3-margin-top w3-container " + webColorW3 + "' style='text-shadow:2px 1px 0 #444'><" + h1 + "><b>" + webName + "</b></" + h1 + "></div>";

          if (isRace == 1) {
            String bestLap = "";
            String info = "";
            if (pilot.BEST_LAP != 0) {
              bestLap = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + mainForm.getLocaleString("Best Lap") + " : " + StageTab.getTimeIntervel(pilot.BEST_LAP);
            }
            if (pilot.IS_FINISHED == 1) {
              info = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + mainForm.getLocaleString("Race Time") + " :  " + StageTab.getTimeIntervel(pilot.RACE_TIME);
            }
            webInfo = "<div class='w3-container w3-white'><h3><b>" + mainForm.getLocaleString("Laps") + " : " + pilot.LAPS + " " + bestLap + info + "</b></h3></div>";
          }

          webChannel = "<div class='w3-badge " + webColorW3 + "'><b>" + pilot.CHANNEL + "</b></div>";

          lOSD = "";
          lFIO = pilot.PILOT;
          if (reg != null) {
            lOSD = reg.VS_USER_NAME;
            lFIO = reg.FIRST_NAME + " " + reg.SECOND_NAME;
          }
          lBestLap = StageTab.getTimeIntervel(pilot.BEST_LAP);
          lLaps = "" + pilot.LAPS;
          lPlace = "" + /*pilot.NUM_IN_GROUP*/ index;
          lColor = "" + pilot.color.colorname;
          lCh = "" + pilot.CHANNEL;
          lTimeRace = StageTab.getTimeIntervel(pilot.RACE_TIME);
        }

      } catch (Exception ein) {
        res = false;
      }
      if (pilotTemplate != null) {
        if (pilot != null) {
          try {
            String html = Tools.getTextFromFile("web" + File.separator + pilotTemplate);
            IVar varsPool1 = new VarPool();
            varsPool1.addChild(new StringVar("PILOT_PHOTO", webPhoto));
            varsPool1.addChild(new StringVar("PILOT_NAME", webName));
            varsPool1.addChild(new StringVar("CH", webChannel));
            varsPool1.addChild(new StringVar("COLOR", webColor));
            varsPool1.addChild(new StringVar("W3-COLOR", webColorW3));          
            varsPool1.addChild(new StringVar("INFO", webInfo));
            varsPool1.addChild(new StringVar("FIO", lFIO));
            varsPool1.addChild(new StringVar("OSD", lOSD));
            varsPool1.addChild(new StringVar("LAPS", lLaps));
            varsPool1.addChild(new StringVar("PLACE", lPlace));
            varsPool1.addChild(new StringVar("CH_TXT", lCh));
            varsPool1.addChild(new StringVar("BEST_LAP", lBestLap));
            varsPool1.addChild(new StringVar("TIME_RACE", lTimeRace));
            htmlContent = varsPool1.applyValues(html);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      varsPool.addChild(new StringVar("PILOT" + index + "_PHOTO", webPhoto));
      varsPool.addChild(new StringVar("PILOT" + index + "_NAME", webName));
      varsPool.addChild(new StringVar("CH" + index, webChannel));
      varsPool.addChild(new StringVar("COLOR" + index, webColor));
      varsPool.addChild(new StringVar("W3-COLOR"+ index, webColorW3));  
      varsPool.addChild(new StringVar("INFO" + index, webInfo));
      varsPool.addChild(new StringVar("FIO" + index, lFIO));
      varsPool.addChild(new StringVar("OSD" + index, lOSD));
      varsPool.addChild(new StringVar("LAPS" + index, lLaps));
      varsPool.addChild(new StringVar("PLACE" + index, lPlace));
      varsPool.addChild(new StringVar("CH_TXT" + index, lCh));
      varsPool.addChild(new StringVar("BEST_LAP" + index, lBestLap));
      varsPool.addChild(new StringVar("TIME_RACE" + index, lTimeRace));

      {

      }

      return htmlContent;
    }

  }

  public void showGroup(HttpServletRequest req, HttpServletResponse resp, VS_STAGE_GROUP group, int isRace, String pilotTemplate) throws ServletException, IOException {
    String html = "";
    //if (isRace) html = Tools.getTextFromFile("web"+File.separator+"tv.template.race.htm");
    //else 
    html = Tools.getTextFromFile("web" + File.separator + "tv.template.htm");

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

      List<VS_STAGE_GROUPS> sorted_users = group.users;
      Collections.sort(sorted_users, GROUP_SCORES_COMPARATOR_2);

      //if (group != null && group.users != null && group.users.size() > i) {
      for (VS_STAGE_GROUPS usr : sorted_users) {
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
      pInfo.addPilotInfo(varsPool, isRace, pilotTemplate);
    }
    varsPool.addChild(new StringVar("ACTIVE_GROUP_NUMBER", "" + group.GROUP_NUM));

    String outHtml = varsPool.applyValues(html);
    resp.getWriter().println(outHtml);
  }

  public void showResultGroup(HttpServletRequest req, HttpServletResponse resp, VS_STAGE_GROUP group, int isRace, String pilotTemplate, boolean lite) throws ServletException, IOException {
    String html = "";
    //if (isRace) html = Tools.getTextFromFile("web"+File.separator+"tv.template.race.htm");
    //else 
    html = "";
    if (lite){
      html = Tools.getTextFromFile("web" + File.separator + "group-result-lite-templ.ajax.html");
    }else{
      html = Tools.getTextFromFile("web" + File.separator + "group-result-templ.ajax.html");
    }

    String channels_st = "";
    if (group != null) {
      channels_st = group.stage.CHANNELS;
    }
    String[] channels = channels_st.split(";");

    PilotInfo[] pInfos = new PilotInfo[6];
    for (int i = 0; i < pInfos.length; i++) {
      VS_STAGE_GROUPS pilot = null;
      if (group.users.size() > i) {
        pilot = group.users.get(i);
      }
      pInfos[i] = new PilotInfo(i + 1, pilot);
    }

    IVar varsPool = new VarPool();
    varsPool.addChild(new StringVar("TITLE", "Drone Racing System"));
    String pilot_infos = "";
    for (PilotInfo pInfo : pInfos) {
      pilot_infos += pInfo.addPilotInfo(varsPool, isRace, pilotTemplate);
    }

    String group_num = "";
    String message1 = "";
    String message2 = "";
    if (group != null) {
      group_num = "" + group.GROUP_NUM;
      message2 = group.stage.CAPTION + " - " + mainForm.getLocaleString("Group") + group.GROUP_NUM;
    }
    if (mainForm.activeRace != null) {
      message1 = mainForm.activeRace.RACE_NAME;
    }

    varsPool.addChild(new StringVar("ACTIVE_GROUP_NUMBER", group_num));
    varsPool.addChild(new StringVar("MESSAGE1", message1));
    varsPool.addChild(new StringVar("MESSAGE2", message2));
    varsPool.addChild(new StringVar("PILOTS_INFOS", pilot_infos));

    varsPool.addChild(new StringVar("BEST_LAP_LABEL", mainForm.getLocaleString("Best Lap")));
    varsPool.addChild(new StringVar("LAPS_LABEL", mainForm.getLocaleString("Laps")));
    varsPool.addChild(new StringVar("RACE_TIME_LABEL", mainForm.getLocaleString("Race Time")));

    varsPool.addChild(new StringVar("BACKGROUND", mainForm.BACKGROUND_FOR_TV));

    String outHtml = varsPool.applyValues(html);
    resp.getWriter().println(outHtml);
  }

  /**
   * Класс для отображения части групп, так как все группы сразу могут влезть на
   * экран телевизора бьем на группы на дисплеи и меняем дисплеи раз в три
   * секунды
   */
  public class DISPLAY {

    boolean byGroup = false;
    public VS_STAGE_GROUP virtual_group = null;
    public List<Integer> numGroups = new ArrayList();
    public List<VS_STAGE_GROUPS> users = new ArrayList();

    public DISPLAY(boolean byGroup) {
      this.byGroup = byGroup;
    }
  }

  public void showStage(HttpServletRequest req, HttpServletResponse resp, VS_STAGE stage) throws ServletException, IOException {
    String html = Tools.getTextFromFile("web" + File.separator + "tv.template.blank.htm");
    String CONTENT = "";

    boolean showGroupNumber = false;
    if (stage.groups.size() > 1) {
      showGroupNumber = true;
    }

    CONTENT = "<div class='w3-container'><p>";
    CONTENT += "<table class='w3-table w3-bordered w3-border w3-xxlarge w3-card-4'>";
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
      for (VS_STAGE_GROUPS user : group.users) {
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
    } else {
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
    if (current_dispaly != null && current_dispaly.byGroup == false) {
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
        if (current_dispaly.byGroup == true) {
          group = stage.groups.get(numGroup);
          users = group.users;
        }
        if (current_dispaly.byGroup == false) {
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
                    + (group.GROUP_NUM == -1 ? "" : "" + group.GROUP_NUM)
                    + "&nbsp;</span>"
                    + "</th>";
          }

          String pilotName = surname + " " + user.PILOT;
          /*if (pilotName.length()>25){
            if (surname.length()>user.PILOT.length()) pilotName = surname;
            else pilotName = user.PILOT;
          }*/

          CONTENT += "<th>" + (current_dispaly.byGroup ? (index + 1) : user.NUM_IN_GROUP) + "</th>";
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

  public synchronized void showLB16(HttpServletRequest req, HttpServletResponse resp, boolean shortVersion) throws IOException {
    try {
      VS_STAGE stage = VS_STAGE.dbControl.getItem(mainForm.con, "RACE_ID=? and RACE_TYPE=?", mainForm.activeRace.RACE_ID, MainForm.RACE_TYPE_EVERYONE_WITH_EACH_16);
      if (stage == null) {
        throw new UserException("", "");
      }

      String html = "";
      if (!shortVersion) {
        html = Tools.getTextFromFile("web" + File.separator + "lb16.table.htm");
      }else{
        html = Tools.getTextFromFile("web" + File.separator + "lb16.table.s.htm");
      }  
      String CONTENT = "";

      List<VS_STAGE_GROUPS> all_users = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=?", stage.ID);
      List<VS_STAGE_GROUPS> users = new ArrayList<VS_STAGE_GROUPS>();
      for (VS_STAGE_GROUPS usr : all_users) {
        VS_STAGE_GROUPS pilot = null;
        for (VS_STAGE_GROUPS usr1 : users) {
          if (usr1.PILOT.equalsIgnoreCase(usr.PILOT)) {
            pilot = usr1;
            break;
          }
        }
        if (pilot == null) {
          pilot = new VS_STAGE_GROUPS(usr);
          pilot.NUM_IN_GROUP = 0;
          pilot.GROUP_NUM = 0;
          users.add(pilot);
        }
        if (pilot.BEST_LAP == 0 || pilot.BEST_LAP > usr.BEST_LAP) {
          pilot.BEST_LAP = usr.BEST_LAP;
        }
        if (pilot.RACE_TIME == 0 || pilot.RACE_TIME > usr.RACE_TIME) {
          pilot.RACE_TIME = usr.RACE_TIME;
        }
        if (usr.IS_FINISHED == 1) {
          pilot.SCORE += usr.SCORE;
          pilot.LAPS += usr.LAPS;
          pilot.NUM_IN_GROUP++;
          if (!pilot.info.equalsIgnoreCase("")) {
            pilot.info += "-";
          }
          pilot.info += usr.SCORE;
        }
        pilot.GROUP_NUM++;
      }

      Collections.sort(users, new AbstractComparator<VS_STAGE_GROUPS>() {
        @Override
        public int compare(VS_STAGE_GROUPS o1, VS_STAGE_GROUPS o2) {
          if (o1.NUM_IN_GROUP > o2.NUM_IN_GROUP) {
            return -1;
          }
          if (o1.NUM_IN_GROUP < o2.NUM_IN_GROUP) {
            return 1;
          }
          if (o1.SCORE > o2.SCORE) {
            return -1;
          }
          if (o1.SCORE < o2.SCORE) {
            return 1;
          }
          if (o1.RACE_TIME > o2.RACE_TIME) {
            return -1;
          }
          if (o1.RACE_TIME < o2.RACE_TIME) {
            return 1;
          }
          return 0;
        }
      });

      CONTENT = "<div class='w3-container'><p>";
      CONTENT += "<table class='w3-table w3-bordered w3-border w3-card-4'>";
      //CONTENT += "<table>";      
      CONTENT += "<tr>";
      CONTENT += " <th>" + mainForm.getLocaleString("Pos").trim() + "</th>";
      CONTENT += " <th>" + mainForm.getLocaleString("Pilot").trim() + "</th>";
      CONTENT += " <th><center>" + mainForm.getLocaleString("Score") + "</center></th>";
      if (!shortVersion) {
        CONTENT += " <th><center>" + mainForm.getLocaleString("Flights") + "</center></th>";
        CONTENT += " <th><center>" + mainForm.getLocaleString("Laps") + "</center></th>";
        CONTENT += " <th><center>" + mainForm.getLocaleString("Race Time") + "</center></th>";
        CONTENT += " <th><center>" + mainForm.getLocaleString("Best Lap") + "</center></th>";
        CONTENT += " <th><center></center></th>";
      }
      CONTENT += "</tr>";

      int pos = 1;
      String ROWS = "";
      for (VS_STAGE_GROUPS usr : users) {
        String html_row = "";
        if (!shortVersion) {
          html_row = Tools.getTextFromFile("web" + File.separator + "lb16.table.row.htm");
        }else{
          html_row = Tools.getTextFromFile("web" + File.separator + "lb16.table.row.s.htm");
        }         
        CONTENT += "<tr>";
        CONTENT += "<td>" + pos + "</td>";
        CONTENT += "<td>" + usr.PILOT + "</td>";
        CONTENT += "<td><center>" + usr.SCORE + "</center></td>";
        if (!shortVersion) {
          CONTENT += "<td><center>" + usr.NUM_IN_GROUP + "/" + usr.GROUP_NUM + "</center></td>";
          CONTENT += "<td><center>" + usr.LAPS + "</center></td>";
          CONTENT += "<td><center>" + StageTab.getTimeIntervel(usr.RACE_TIME) + "</center></td>";
          CONTENT += "<td><center>" + StageTab.getTimeIntervel(usr.BEST_LAP) + "</center></td>";
          CONTENT += "<td><center>" + usr.info + "</center></td>";
        }
        CONTENT += "</tr>";
        
        IVar varsPool1 = new VarPool();
        varsPool1.addChild(new StringVar("POS", ""+pos));
        varsPool1.addChild(new StringVar("PILOT", usr.PILOT));
        varsPool1.addChild(new StringVar("SCORE", ""+usr.SCORE));
        varsPool1.addChild(new StringVar("RACES", ""+usr.NUM_IN_GROUP));
        varsPool1.addChild(new StringVar("ALL_RACES", ""+usr.GROUP_NUM));
        varsPool1.addChild(new StringVar("LAPS", ""+usr.LAPS));
        varsPool1.addChild(new StringVar("RACE_TIME", StageTab.getTimeIntervel(usr.RACE_TIME)));
        varsPool1.addChild(new StringVar("BEST_LAP", StageTab.getTimeIntervel(usr.BEST_LAP)));
        varsPool1.addChild(new StringVar("info", usr.info));        
        
        ROWS += varsPool1.applyValues(html_row);
        
        pos++;                        
      }

      CONTENT += "</table></p>";
      CONTENT += "</div>";

      IVar varsPool = new VarPool();
      varsPool.addChild(new StringVar("CONTENT", CONTENT));
      varsPool.addChild(new StringVar("ROWS", ROWS));
      varsPool.addChild(new StringVar("BACKGROUND", mainForm.BACKGROUND_FOR_TV));
      String outHtml = varsPool.applyValues(html);
      resp.getWriter().println(outHtml);
    } catch (Exception e) {
      //System.out.println(Tools.traceError(e));
      generateBlank(resp);
    }
  }

  public void generateBlank(HttpServletResponse resp) throws IOException {
    String html = Tools.getTextFromFile("web" + File.separator + "tv.template.blank.htm");
    String CONTENT = "";
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
    if (req.getServletPath().equalsIgnoreCase("/stage.ajax")) {
      if (mainForm.activeStage != null) {
        showStage(req, resp, mainForm.activeStage);
      } else {
        generateBlank(resp);
      }
    } else if (req.getServletPath().equalsIgnoreCase("/lb16.ajax")) {
      if (req.getQueryString().indexOf("short")>=0){
        showLB16(req, resp, true); 
      }else{
        showLB16(req, resp, false);
      }
    } else if (req.getServletPath().equalsIgnoreCase("/osd.ajax")) {
      if (mainForm.activeGroup != null) {
        showGroup(req, resp, mainForm.activeGroup, 1, null);
      } else if (mainForm.invateGroup != null) {
        showGroup(req, resp, mainForm.invateGroup, 0, null);
      } else if (mainForm.lastRaceGroup != null) {
        showGroup(req, resp, mainForm.lastRaceGroup, 1, null);
      } else if (mainForm.lastInvateGroup != null) {
        showGroup(req, resp, mainForm.lastInvateGroup, 0, null);
      } else {
        generateBlank(resp);
      }
    } else if (req.getServletPath().equalsIgnoreCase("/group_result.ajax")) {
      try {
        if (mainForm.lastRaceGroup != null) {
          boolean lite = false;
          String pilot_templ = "group-result-templ.pilot.html";
          if (req.getQueryString().indexOf("lite")>=0){ 
            lite = true; 
            pilot_templ = "group-result-lite-templ.pilot.html";
          }
          showResultGroup(req, resp, mainForm.lastRaceGroup, 3, pilot_templ,lite);
        } else {
          generateBlank(resp);
        }
      } catch (Exception e) {
        resp.getWriter().println(e.toString() + ". " + Tools.traceError(e));
        System.out.println(e);
      }
    } else {
      try {
        if (mainForm.activeGroup != null) {
          showGroup(req, resp, mainForm.activeGroup, 1, null);
        } else if (mainForm.invateGroup != null) {
          showGroup(req, resp, mainForm.invateGroup, 0, null);
        } else if (mainForm.activeStage != null) {
          showStage(req, resp, mainForm.activeStage);
        } else {
          generateBlank(resp);
        }
      } catch (Exception e) {
        resp.getWriter().println(e.toString() + ". " + Tools.traceError(e));
        System.out.println(e);
      }
    }
  }
}
