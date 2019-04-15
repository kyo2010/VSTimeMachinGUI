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
import vs.time.kkv.connector.SystemOptions;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 * тест коммита от ККВ
 *
 * @author kyo
 */
public class RaceHttpServlet extends HttpServlet {

  public static boolean USE_CACHE = false;
  String templ = null;
  String templ_button = null;

  public static String ACTION_RUN = "run";
  public static String ACTION_STOP = "stop";
  public static String ACTION_CHECK = "check";
  public static String ACTION_STOP_CHECK = "stop_check";
  public static String ACTION_INVATE = "invate";

  public String createMenuButton(String caption, String href) {
    if (templ_button == null || USE_CACHE == false) {
      templ_button = Tools.getTextFromFile("web" + File.separator + "button.template.htm");
    }
    IVar varsPool = new VarPool();
    varsPool.addChild(new StringVar("URL", href));
    varsPool.addChild(new StringVar("CAPTION", caption));
    //return " <button class='w3-button' onclick='location.href=\"" + href + "\"'>" + caption + "</button>\n";
    return varsPool.applyValues(templ_button);
  }

  public RaceHttpServlet() {
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    MainForm mainForm = MainForm._mainForm;

    resp.setStatus(HttpStatus.SC_OK);
    resp.setHeader("Content-Type", "text/html; charset=UTF-8");
    String INFO = "";
    VS_RACE race = null;
    String STAGE_CAPTION = "";
    String PAGE_CONTENT = "";
    String REFRESH_META = "<meta http-equiv=\"refresh\" content=\"3\">";      

    //String path = req.getContextPath();
    //System.out.println("path : "+req.getQueryString());
    //System.out.println("path : "+req.getServletPath());
    boolean is_admin = false;
    boolean is_refresh = false;
    String logout = req.getParameter("logout");
    if (logout != null && logout.equals("1")) {
      Cookie cookie = new Cookie("admin", "");
      cookie.setMaxAge(0);
      resp.addCookie(cookie);
      req.getSession().removeAttribute("admin");
    } else if (req.getSession().getAttribute("admin") != null && "1".equals(req.getSession().getAttribute("admin"))) {
      is_admin = true;
      /*if(mainForm.lastCheckingGrpup == null
         || (mainForm.lastCheckingGrpup!=null && !mainForm.lastCheckingGrpup.stageTab.checkerTimer.isRunning())){
        REFRESH_META = "";
      }*/
    } else {
      Cookie[] cookies = req.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equalsIgnoreCase("admin") && cookie.getValue().equalsIgnoreCase("1")) {

            req.getSession().setAttribute("admin", "1");
            is_admin = true;
          }
        }
      }
    }

    String message_send = "";
    Long group_num_send = null;
    if (is_admin) {
      String action = req.getParameter("action");
      String stage = req.getParameter("stage");
      String message = req.getParameter("message");

      if (message != null && !message.equalsIgnoreCase("")) {
        PAGE_CONTENT += "<div class='w3-panel w3-red'>\n";
        PAGE_CONTENT += "<h3>" + message + "</h3>\n";
        //PAGE_CONTENT+= "<p>"+message+"</p>\n";
        PAGE_CONTENT += "</div>\n";
      }

      /**
       * оброботчик основных событий от группы для админа, приглашение, запуск,
       * стоп
       */
      Long group_num = null;
      try {
        group_num = Long.parseLong(req.getParameter("group_num"));
      } catch (Exception e) {
      }
      if (action == null) {
        action = "";
      }
      if (action.equalsIgnoreCase(ACTION_STOP)) {
        if (mainForm.activeGroup != null) {
          mainForm.activeGroup.stageTab.stopRace(false);
          is_refresh = true;
        }
      } else if (action.equalsIgnoreCase(ACTION_STOP_CHECK)) {
        if (mainForm.lastCheckingGrpup != null) {
          mainForm.lastCheckingGrpup.stageTab.stopSearch();
          is_refresh = true;
        }
      } else if (action.equalsIgnoreCase(ACTION_INVATE)) {
        for (StageTab tab : mainForm.stageTabs) {
          if (("" + tab.stage.ID).equals(stage)) {
            if (group_num != null) {
              message_send = tab.invateAction(group_num, false);
              is_refresh = true;
              //PAGE_CONTENT += "<p>"+message+"</p>";
            }
            break;
          }
        }
      } else if (action.equalsIgnoreCase(ACTION_RUN)) {
        for (StageTab tab : mainForm.stageTabs) {
          if (("" + tab.stage.ID).equals(stage)) {
            if (group_num != null) {
              message_send = tab.startRaceAction(group_num, false);
              is_refresh = true;
              //PAGE_CONTENT += "<p>"+message+"</p>";
            }
            break;
          }
        }
      } else if (action.equalsIgnoreCase(ACTION_CHECK)) {
        for (StageTab tab : mainForm.stageTabs) {
          if (("" + tab.stage.ID).equals(stage)) {
            if (group_num != null) {
              message_send = tab.startSearchAction(group_num, false);
              is_refresh = true;
              //PAGE_CONTENT += "<p>"+message+"</p>";
            }
            break;
          }
        }
      }
      group_num_send = group_num;
    }

    /**
     * Вывод групп
     */
    //resp.getWriter().println("EmbeddedJetty");  
    try {
      if (templ == null || USE_CACHE == false) {
        templ = Tools.getTextFromFile("web" + File.separator + "index.template.htm");
      }

      List<VS_STAGE> stages = null;
      int race_id = -1;

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

      String error = null;
      String mode = req.getParameter("mode");
      if (mode != null && mode.equalsIgnoreCase("admin")) {
        REFRESH_META = "";
        String user = req.getParameter("usr");
        String password = req.getParameter("pwd");
        boolean show_login_form = true;
        if (user != null && password != null) {
          if (user.equalsIgnoreCase("admin") && password.equals(SystemOptions.getPasswordForAdminWeb())) {
            req.getSession().setAttribute("admin", "1");
            Cookie cookie = new Cookie("admin", "1");
            cookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(cookie);
            PAGE_CONTENT = "<div class='w3-container w3-card-4'>\n";
            PAGE_CONTENT += "<p>You are an administrator!</p><p>Welocome to control Drone Racing System!</p>\n";
            PAGE_CONTENT += "<p><a href='index.htm' class='w3-teal w3-button'>Control</a></p>\n";
            PAGE_CONTENT += "</div><p></p>\n";
            show_login_form = false;
          }else{
            error = "Login or Password is incorrect.";
          }
        }

        STAGE_CAPTION = "Admin menu";
        if (show_login_form) {
          PAGE_CONTENT = "";
          if (error!=null) {
            PAGE_CONTENT += "<div class='w3-container w3-red w3-card-4'>\n<p>"+error+"</p></div>\n";
          }
          PAGE_CONTENT += "<form class='w3-container w3-card-4' action='index.htm' method='POST'>\n";
          PAGE_CONTENT += "<input type='hidden' name='mode' value='admin' />\n";
          PAGE_CONTENT += "<p><label>user : </label><input type='edit' name='usr' /></p>\n";
          PAGE_CONTENT += "<p><label>password : </label><input type='password' name='pwd'  /></p>\n";
          PAGE_CONTENT += "<p><input type='submit' value='Login' class='w3-teal w3-button' /></p>\n";
          PAGE_CONTENT += "</form><p></p>\n";
        }
      } else {
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
        INFO = InfoForm.getCurrentInfo();
        if (active_stage == null) {
          STAGE_CAPTION = "Registration";
          PAGE_CONTENT += "<table class='w3-table w3-striped w3-bordered' style='width:300px'>\n";
          PAGE_CONTENT += "<tr class='w3-teal'>\n";
          PAGE_CONTENT += "  <th width='40px'>#</th><th width='260px'>Pilot</th><th width='150px'>Transponder</th>\n";
          PAGE_CONTENT += "</tr>\n";
          try {
            List<VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? order by NUM", race_id);
            int index = 1;
            for (VS_REGISTRATION reg : regs) {
              if (reg.IS_ACTIVE == 1) {
                PAGE_CONTENT += "<tr>\n";
                PAGE_CONTENT += "  <td>" + index + "</td><td>" + reg.VS_USER_NAME + "</td>\n";
                PAGE_CONTENT += "  <td>" + reg.getTransponders() + "</td>";
                PAGE_CONTENT += "</tr>\n";
                index++;
              }
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
            PAGE_CONTENT += "  <th>#</th><th>Pilot</th><th>Channel</th><th>Laps</th><th>Best Lap</th><th>Last Lap</th><th>Race Lap</th><th>Status</th><th>Pos</th>\n";
            PAGE_CONTENT += "</tr>\n";
            for (VS_STAGE_GROUPS user : groups) {
              if (currentGroup != user.GROUP_NUM) {
                PAGE_CONTENT += "<tr class='w3-light-gray w3-xlarge'>\n";
                String control_buttons = "";
                if (is_admin && mainForm.activeGroup == null && active_stage != null
                        && (mainForm.lastCheckingGrpup == null
                        || (mainForm.lastCheckingGrpup != null && !mainForm.lastCheckingGrpup.stageTab.checkerTimer.isRunning()))
                        && (active_stage.STAGE_TYPE == MainForm.STAGE_PRACTICA
                        || active_stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION
                        || active_stage.STAGE_TYPE == MainForm.STAGE_RACE)) {
                  control_buttons += "<a href='index.htm?stage=" + active_stage.ID + "&group_num=" + user.GROUP_NUM + "&action=" + ACTION_RUN + "' class='w3-teal w3-button'>"
                          + "Run Race</a>&nbsp;&nbsp;&nbsp;";
                  control_buttons += "<a href='index.htm?stage=" + active_stage.ID + "&group_num=" + user.GROUP_NUM + "&action=" + ACTION_CHECK + "' class='w3-teal w3-button'>"
                          + "Check</a>&nbsp;&nbsp;&nbsp;";
                  control_buttons += "<a href='index.htm?stage=" + active_stage.ID + "&group_num=" + user.GROUP_NUM + "&action=" + ACTION_INVATE + "' class='w3-teal w3-button'>"
                          + "Invate</a>";
                }
                if (is_admin && mainForm.activeGroup != null && active_stage.ID == mainForm.activeGroup.stage.ID
                        && user.GROUP_NUM == mainForm.activeGroup.GROUP_NUM) {
                  control_buttons = "<a href='index.htm?stage=" + active_stage.ID + "&group_num=" + user.GROUP_NUM + "&action=" + ACTION_STOP + "' class='w3-teal w3-button'>"
                          + "Stop Race</a>";
                }
                if (mainForm.lastCheckingGrpup != null && mainForm.lastCheckingGrpup.stageTab.checkerTimer.isRunning()) {
                  control_buttons = "<a href='index.htm?stage=" + active_stage.ID + "&group_num=" + user.GROUP_NUM + "&action=" + ACTION_STOP_CHECK + "' class='w3-teal w3-button'>"
                          + "Stop Search</a>";
                }
                PAGE_CONTENT += "  <td colspan='9'><a  name='group_" + user.GROUP_NUM + "'></a><b>Group" + user.GROUP_NUM + "</b> " + control_buttons + "</td>\n";
                PAGE_CONTENT += "</tr>\n";
                currentGroup = user.GROUP_NUM;
              }

              VSColor color = VSColor.getColorForChannel(user.CHANNEL, active_stage.CHANNELS, active_stage.COLORS);
              String w3css = "";
              if (color != null) {
                //bgcolor = VSColor.getHTMLColorString(color.color);
                w3css = "w3-" + color.colorname.toLowerCase();
              }

              PAGE_CONTENT += "<tr class='" + w3css + "'>\n";
              String status = "";
              int pos = 0;
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
              if (user.IS_FINISHED == 1 && user.RACE_TIME > 0) {
                int coutFinish = 0;
                for (VS_STAGE_GROUPS user2 : groups) {
                  if (user.GROUP_NUM == user2.GROUP_NUM && user2.IS_FINISHED == 1) {
                    coutFinish++;
                  }
                }
                pos = coutFinish;
                for (VS_STAGE_GROUPS user2 : groups) {
                  if (user.GROUP_NUM == user2.GROUP_NUM
                          && user2.IS_FINISHED == 1
                          && user2.RACE_TIME > 0
                          && user.RACE_TIME < user2.RACE_TIME) {
                    pos--;
                  }
                }
              }
              ///String bgcolor = "#ffffff";           
              PAGE_CONTENT += "  <td>" + user.NUM_IN_GROUP + "</td><td>" + user.PILOT + "</td><td align='center' ><b>" + user.CHANNEL + "</b></td>"
                      + "<td align='center'>" + user.LAPS + "</td>"
                      + "<td>" + (user.BEST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.BEST_LAP,false)) + "</td>"
                      + "<td>" + (user.LAST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.LAST_LAP,false)) + "</td>"
                      + "<td>" + (user.RACE_TIME == 0 ? "" : StageTab.getTimeIntervel(user.RACE_TIME,false)) + "</td><td>"
                      + status + "</td>"
                      + "<td  align='center'>" + (pos == 0 ? "" : (user.RACE_TIME == VS_STAGE_GROUPS.MAX_TIME ? "DNF" : pos)) + "</td>\n";
              PAGE_CONTENT += "</tr>\n";
            }
            PAGE_CONTENT += "</table>\n";
          } catch (Exception e) {
            mainForm.toLog(e);
            e.printStackTrace();
            PAGE_CONTENT += "Groups are formatings";
          }
        }

      }

      IVar varsPool = new VarPool();
      varsPool.addChild(new StringVar("TITLE", "Drone Racing System" + (is_admin ? " [Admin]" : "")));
      varsPool.addChild(new StringVar("RACE_CAPTION", race == null ? "None" : race.RACE_NAME));
      varsPool.addChild(new StringVar("STAGE_CAPTION", STAGE_CAPTION));
      varsPool.addChild(new StringVar("MENU", MENU));
      varsPool.addChild(new StringVar("REFRESH_META", REFRESH_META));
      varsPool.addChild(new StringVar("PAGE_CONTENT", PAGE_CONTENT));
      if (!INFO.equalsIgnoreCase("")) {
        INFO = "<div class='info'>" + INFO + "</div><br/>";
      }
      varsPool.addChild(new StringVar("INFO", INFO));

      if (is_refresh && active_stage != null) {
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "index.htm?stage=" + active_stage.ID
                + ((!message_send.equalsIgnoreCase("")) ? "&message=" + message_send : "")
                + (group_num_send != null ? "#group_" + group_num_send : ""));
      }
      String html = varsPool.applyValues(templ);
      resp.getWriter().println(html);

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }

}
