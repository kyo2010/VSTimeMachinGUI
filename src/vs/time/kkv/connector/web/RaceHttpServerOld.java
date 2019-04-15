/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class RaceHttpServerOld implements HttpHandler, Runnable {

  public boolean connected = false;
  HttpServer server = null;
  Thread thread = null;
  int port;
  MainForm mainForm;

  public void disconnect() {
    if (server != null) {
      server.stop(0);
    }
    connected = false;
  }

  public RaceHttpServerOld(MainForm mainForm, int port) {
    this.port = port;
    this.mainForm = mainForm;
    thread = new Thread(this);
    thread.start();
    connected = true;
  }

  public static String createMenuButton(String caption, String href) {
    return " <button class='button' onclick='location.href=\"" + href + "\"'>" + caption + "</button>\n";

  }
  
  String templ = null;
      

  @Override
  public void handle(HttpExchange exchange) {

    try {
      if (templ==null) templ = Tools.getTextFromFile("web"+File.separator+"index.html");
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

      Map<String, Object> parameters = new HashMap<>();
      parseQuery(exchange.getRequestURI().toString(), parameters);
      long stage_id = -1;
      String stage_st = null;;
      if (parameters.get("stage") != null) {
        stage_st = parameters.get("stage").toString();
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

      INFO = InfoForm.getCurrentInfo();
      
      String STAGE_CAPTION = "";
      String PAGE_CONTENT = "";
      if (active_stage == null) {
        STAGE_CAPTION = "Registration";
        PAGE_CONTENT += "<table>\n";
        PAGE_CONTENT += "<tr>\n";
        PAGE_CONTENT += "  <th>#</th><th>Pilot</th>\n";
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
          PAGE_CONTENT += "<table>\n";
          PAGE_CONTENT += "<tr>\n";
          PAGE_CONTENT += "  <th>#</th><th>Pilot</th><th>Channel</th><th>Best Lap</th><th>Race Lap</th><th>Status</th>\n";
          PAGE_CONTENT += "</tr>\n";
          for (VS_STAGE_GROUPS user : groups) {
            if (currentGroup != user.GROUP_NUM) {
              PAGE_CONTENT += "<tr>\n";
              PAGE_CONTENT += "  <td colspan='6'><b>Group" + user.GROUP_NUM + "</b></td>\n";
              PAGE_CONTENT += "</tr>\n";
              currentGroup = user.GROUP_NUM;
            }
            PAGE_CONTENT += "<tr>\n";
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

            String bgcolor = "#ffffff";
            VSColor color = VSColor.getColorForChannel(user.CHANNEL,active_stage.CHANNELS,active_stage.COLORS);
            if (color != null) {
              bgcolor = VSColor.getHTMLColorString(color.color);
            }
            PAGE_CONTENT += "  <td>" + user.NUM_IN_GROUP + "</td><td>" + user.PILOT + "</td><td align='center' bgcolor='" + bgcolor + "' class='ramka'>" + user.CHANNEL + "</td><td>"
                    + (user.BEST_LAP == 0 ? "" : StageTab.getTimeIntervel(user.BEST_LAP,false)) + "</td>"
                    + "<td>" + (user.RACE_TIME == 0 ? "" : StageTab.getTimeIntervel(user.RACE_TIME,false)) + "</td><td>"
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

      //String query = requestedUri.getRawQuery();
      //    parseQuery(query, parameters);
      //    exchange.setAttribute("parameters", parameters);
      //Map<String, Object> params =
      //       (Map<String, Object>)exchange.getAttribute("parameters");
      /*Headers headers = exchange.getRequestHeaders();
    for (String header : headers.keySet()) {
      builder.append("<p>").append(header).append("=")
              .append(headers.getFirst(header)).append("</p>\n");
    }
    builder.append("\n</body>\n</html>");*/
    /*String encoding = "UTF-8";
    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + encoding);
    
    OutputStreamWriter out = new OutputStreamWriter(exchange.getResponseBody(), encoding);
    out.write(html);*/  
      //System.out.println("html:"+html);    
      byte[] bytes = html.getBytes("UTF-8");
      exchange.sendResponseHeaders( 200, bytes.length * 2);
      OutputStream os = exchange.getResponseBody();

      os.write(bytes);

      os.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public void run() {
    try {
      server = HttpServer.create();
      server.bind(new InetSocketAddress(port), 0);
      server.createContext("/", this);
      server.start();
    } catch (Exception e) {
    }
  }

  private void parsePostParameters(HttpExchange exchange)
          throws IOException {

    if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
      @SuppressWarnings("unchecked")
      Map<String, Object> parameters
              = (Map<String, Object>) exchange.getAttribute("parameters");
      InputStreamReader isr
              = new InputStreamReader(exchange.getRequestBody(), "utf-8");
      BufferedReader br = new BufferedReader(isr);
      String query = br.readLine();
      parseQuery(query, parameters);
    }
  }

  @SuppressWarnings("unchecked")
  private void parseQuery(String query, Map<String, Object> parameters)
          throws UnsupportedEncodingException {

    if (query.indexOf("/?") == 0) {
      query = query.substring(2);
    }

    if (query.indexOf("?") == 0) {
      query = query.substring(1);
    }

    if (query != null) {
      String pairs[] = query.split("[&]");

      for (String pair : pairs) {
        String param[] = pair.split("[=]");

        String key = null;
        String value = null;
        if (param.length > 0) {
          key = URLDecoder.decode(param[0],
                  System.getProperty("file.encoding"));
        }

        if (param.length > 1) {
          value = URLDecoder.decode(param[1],
                  System.getProperty("file.encoding"));
        }

        if (parameters.containsKey(key)) {
          Object obj = parameters.get(key);
          if (obj instanceof List<?>) {
            List<String> values = (List<String>) obj;
            values.add(value);
          } else if (obj instanceof String) {
            List<String> values = new ArrayList<String>();
            values.add((String) obj);
            values.add(value);
            parameters.put(key, values);
          }
        } else {
          parameters.put(key, value);
        }
      }
    }
  }

}
