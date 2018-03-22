/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class RaceHttpServlet extends HttpServlet {

  public static boolean USE_CACHE = false;
  String templ = null;
  String templ_button = null;

  public String createMenuButton(String caption, String href) {
     if (templ_button == null || USE_CACHE == false) {
      templ_button = Tools.getTextFromFile("web\\button.template.htm");
     }  
     IVar varsPool = new VarPool();
     varsPool.addChild(new StringVar("URL", href));
     varsPool.addChild(new StringVar("CAPTION", caption));
    //return " <button class='w3-button' onclick='location.href=\"" + href + "\"'>" + caption + "</button>\n";
    return varsPool.applyValues(templ_button);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    MainForm mainForm = MainForm._mainForm;

    resp.setStatus(HttpStatus.SC_OK);
    resp.setHeader("Content-Type", "text/html; charset=UTF-8");
    //resp.getWriter().println("EmbeddedJetty");  
    try {
      if (templ == null || USE_CACHE == false) {
        templ = Tools.getTextFromFile("web\\index.template.htm");
      }
      VS_RACE race = null;
      List<VS_STAGE> stages = null;
      int race_id = -1;
      String INFO = "";

      try {
        race = VS_RACE.dbControl.getItem(mainForm.con, "IS_ACTIVE=1");
        race_id = race.RACE_ID;
        stages = VS_STAGE.dbControl.getList(mainForm.con, "RACE_ID=? ORDER BY ID", race_id);
      } catch (Exception e) {
        mainForm.toLog(e);
      }

      VS_STAGE active_stage = null;
      String MENU = createMenuButton("Registration", "?stage=reg");

      //Map<String, Object> parameters = new HashMap<>();
      //parseQuery(exchange.getRequestURI().toString(), parameters);
      long stage_id = -1;
      String stage_st = null;;
      if (req.getParameter("stage") != null) {
        stage_st = req.getParameter("stage").toString();
        if (stage_st.equalsIgnoreCase("reg")) {
          stage_id = -2;
        } else {
          try {
            stage_id = Long.parseLong(stage_st);
          } catch (Exception e) {
          }
        }
      }

      if (stages != null) {
        int index = 0;
        for (VS_STAGE stage : stages) {
          //MENU += "<a href='?stage=" + stage.ID + "'>" + stage.CAPTION + "</a>&nbsp;";
          MENU += createMenuButton(stage.CAPTION, "?stage=" + stage.ID);
          index++;
          if (stage.IS_SELECTED == 1 && stage_id == -1 && stage_id != -2) {
            active_stage = stage;
          }
          if (stage_id == stage.ID) {
            active_stage = stage;
          }
        }
      } else {
      }

      INFO = "";

      long time = Calendar.getInstance().getTimeInMillis();
      long activeGroupNum = -1;
      VS_STAGE_GROUP activeGroup = mainForm.activeGroup;
      if (activeGroup != null && activeGroup.stage.ID == active_stage.ID) {
        activeGroupNum = activeGroup.GROUP_NUM;
        if (mainForm.raceTime != 0) {
          INFO = "Race is active. Stage: " + activeGroup.stage.CAPTION + ". Group" + activeGroupNum + ". Race time: " + StageTab.getTimeIntervelForTimer(time - mainForm.raceTime);
        }
      };

      InfoForm infoForm = InfoForm.getCurrentInfo();
      if (infoForm != null) {
        INFO = infoForm.caption;
      }

      String STAGE_CAPTION = "";
      String PAGE_CONTENT = "";
      if (active_stage == null) {
        STAGE_CAPTION = "Registration";
        PAGE_CONTENT += "<table class='w3-table w3-striped w3-bordered' style='width:300px'>\n";
        PAGE_CONTENT += "<tr class='w3-teal'>\n";
        PAGE_CONTENT += "  <th width='40px'>#</th><th width='260px'>Pilot</th>\n";
        PAGE_CONTENT += "</tr>\n";
        try {
          List<VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? order by NUM", race_id);
          for (VS_REGISTRATION reg : regs) {
            PAGE_CONTENT += "<tr>\n";
            PAGE_CONTENT += "  <td>" + reg.NUM + "</td><td>" + reg.VS_USER_NAME + "</td>\n";
            PAGE_CONTENT += "</tr>\n";
          }
        } catch (Exception e) {
          mainForm.toLog(e);
        }
        PAGE_CONTENT += "</table>\n";
      } else {
        STAGE_CAPTION = active_stage.CAPTION;
        try {
          List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? ORDER BY GROUP_NUM, NUM_IN_GROUP", active_stage.ID);
          long currentGroup = 0;
          PAGE_CONTENT += "<table class='w3-table w3-bordered'>\n";
          PAGE_CONTENT += "<tr class='w3-teal'>\n";
          PAGE_CONTENT += "  <th>#</th><th>Pilot</th><th>Channel</th><th>Best Lap</th><th>Race Lap</th><th>Status</th>\n";
          PAGE_CONTENT += "</tr>\n";
          for (VS_STAGE_GROUPS user : groups) {
            if (currentGroup != user.GROUP_NUM) {
              PAGE_CONTENT += "<tr class='w3-light-gray'>\n";
              PAGE_CONTENT += "  <td colspan='6'><b>Group" + user.GROUP_NUM + "</b></td>\n";
              PAGE_CONTENT += "</tr>\n";
              currentGroup = user.GROUP_NUM;
            }
            
            VSColor color = VSColor.getColorForChannel(user.CHANNEL, active_stage.CHANNELS, active_stage.COLORS);
            String w3css = "";
            if (color != null) {
              //bgcolor = VSColor.getHTMLColorString(color.color);
              w3css = "w3-"+color.colorname.toLowerCase();
            }
            
            PAGE_CONTENT += "<tr class='"+w3css+"'>\n";
            String status = "";
            if (user.GROUP_NUM == activeGroupNum) {
              status = "active";
            }
            if (user.GROUP_NUM == activeGroupNum + 1) {
              status = "get ready";
            }
            if (user.IS_FINISHED == 1) {
              status = "finished";
              if (activeGroupNum == -1) {
                activeGroupNum = user.GROUP_NUM;
              }
            }

            ///String bgcolor = "#ffffff";           
            PAGE_CONTENT += "  <td>" + user.NUM_IN_GROUP + "</td><td>" + user.PILOT + "</td><td align='center' >" + user.CHANNEL + "</td><td>"
                    + (user.BEST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.BEST_LAP)) + "</td>"
                    + "<td>" + (user.RACE_TIME == 0 ? "" : StageTab.getTimeIntervel(user.RACE_TIME)) + "</td><td>"
                    + status + "</td>\n";
            PAGE_CONTENT += "</tr>\n";
          }
          PAGE_CONTENT += "</table>\n";
        } catch (Exception e) {
          mainForm.toLog(e);
          PAGE_CONTENT += "Groups are formatings";
        }
      }
      IVar varsPool = new VarPool();
      varsPool.addChild(new StringVar("TITLE", "Drone Racing System"));
      varsPool.addChild(new StringVar("RACE_CAPTION", race == null ? "None" : race.RACE_NAME));
      varsPool.addChild(new StringVar("STAGE_CAPTION", STAGE_CAPTION));
      varsPool.addChild(new StringVar("MENU", MENU));
      varsPool.addChild(new StringVar("PAGE_CONTENT", PAGE_CONTENT));
      if (!INFO.equalsIgnoreCase("")) {
        INFO = "<div class='info'>" + INFO + "</div><br/>";
      }
      varsPool.addChild(new StringVar("INFO", INFO));

      String html = varsPool.applyValues(templ);
      resp.getWriter().println(html);

    } catch (Exception e) {
      System.out.println(e);
    }
  }

}
