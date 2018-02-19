/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import KKV.DBControlSqlLite.Utils.Tools;
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
import java.util.List;
import java.util.Map;
import ru.nkv.var.StringVar;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;

/**
 *
 * @author kyo
 */
public class RaceHttpServer implements HttpHandler, Runnable {
  
  public boolean connected = false;
  HttpServer server = null;
  Thread thread = null;
  int port;
  MainForm mainForm;
  
  public void disconnect(){
    if (server!=null) server.stop(0);
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
  public void handle(HttpExchange exchange) throws IOException {
    
    String templ = Tools.getTextFromFile("web\\index.html");
    VS_RACE race = null;
    List<VS_STAGE> stages = null;
    int race_id = -1;
    
    try{
      race = VS_RACE.dbControl.getItem(mainForm.con,"IS_ACTIVE=1");
      race_id = race.RACE_ID;
      stages = VS_STAGE.dbControl.getList(mainForm.con, "RACE_ID=? ORDER BY ID",race_id);
    }catch(Exception e){
      mainForm.toLog(e);
    }  
    
    String MENU="<a href='?stage=reg'>Registration</a>&nbsp;";   
    if (stages!=null){
      int index = 0;
      for (VS_STAGE stage : stages){
        MENU+="<a href='?stage="+index+"'>"+stage.CAPTION+"</a>&nbsp;";
        index++;
      }
    }else{
    
    }
    
    String STAGE_CAPTION = "";
    String PAGE_CONTENT = "";
    VS_STAGE active_stage = null;
    if (active_stage==null){
      STAGE_CAPTION = "Registration";
      try{
        List<VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? order by NUM",race_id);
        for (VS_REGISTRATION reg: regs){
          PAGE_CONTENT+= reg.NUM+". "+ reg.VS_USER_NAME+"<br/>";
        }
      }catch(Exception e){
        mainForm.toLog(e);
      }            
    }else{
    }
   
    
    IVar varsPool = new VarPool();
    varsPool.addChild(new StringVar("TITLE","Drone Racing System"));
    varsPool.addChild(new StringVar("RACE_CAPTION",race==null?"None":race.RACE_NAME));
    varsPool.addChild(new StringVar("STAGE_CAPTION",STAGE_CAPTION));
    varsPool.addChild(new StringVar("MENU",MENU));
    varsPool.addChild(new StringVar("PAGE_CONTENT",PAGE_CONTENT));
    
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

    
   /* String encoding = "UTF-8";
    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + encoding);
    
    OutputStreamWriter out = new OutputStreamWriter(exchange.getResponseBody(), encoding);
    out.write(html);  */

    System.out.println("html:"+html);    
    
    
      byte[] bytes = html.getBytes("UTF-8");
      exchange.sendResponseHeaders(200, bytes.length*2);
      OutputStream os = exchange.getResponseBody();
      os.write(bytes);
      os.close();
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
            Map<String, Object> parameters =
                (Map<String, Object>)exchange.getAttribute("parameters");
            InputStreamReader isr =
                new InputStreamReader(exchange.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

     @SuppressWarnings("unchecked")
     private void parseQuery(String query, Map<String, Object> parameters)
         throws UnsupportedEncodingException {

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
                     if(obj instanceof List<?>) {
                         List<String> values = (List<String>)obj;
                         values.add(value);
                     } else if(obj instanceof String) {
                         List<String> values = new ArrayList<String>();
                         values.add((String)obj);
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
