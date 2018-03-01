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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
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
public class RaceHttpServer  implements  Runnable {

  public boolean connected = false;
  Server server = null;
  Thread thread = null;
  int port;
  MainForm mainForm;

  public void disconnect() {
    if (server != null) {
      server.getServer().setStopAtShutdown(true);
    }
    connected = false;
  }

  public RaceHttpServer(MainForm mainForm, int port) {
    this.port = port;
    this.mainForm = mainForm;
    thread = new Thread(this);
    thread.start();
    connected = true;
  } 
  
  @Override
  public void run() {
    try {      
      server = new Server(port); 
      ServletContextHandler handler = new ServletContextHandler(server, "/"); 
      handler.addServlet(RaceHttpServlet.class, "/"); 
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
