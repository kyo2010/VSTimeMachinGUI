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
public class TVTranslationServlet  extends HttpServlet {
  public static boolean USE_CACHE = false;
  String templ = null;
  String templ_button = null;
  MainForm mainForm = null;
  
  public class PilotInfo{
    int index = 0;    
    VS_STAGE_GROUPS pilot = null;
    VS_REGISTRATION reg = null;

    public PilotInfo(int index, VS_STAGE_GROUPS pilot) {
      this.pilot = pilot;
      this.index = index;
    }
    
    public boolean addPilotInfo(IVar varsPool ){
      boolean res = true;
      String webPhoto = "";
      String webName = "";
      String webChannel ="";      
      if (pilot!=null){
        webName = pilot.PILOT;
        reg = pilot.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
        webPhoto = "<img src='"+reg.PHOTO.replaceAll("web/", "")+"' width='200px' class='w3-circle'>";
        webChannel = pilot.CHANNEL;
      }      
      varsPool.addChild(new StringVar("PILOT"+index+"_PHOTO", webPhoto));
      varsPool.addChild(new StringVar("PILOT"+index+"_NAME", webName));
      varsPool.addChild(new StringVar("CH"+index, webChannel));      
    
      return res;
    }
    
  }
  
  public void showInvitation(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
      if (templ == null || USE_CACHE == false) {
        templ = Tools.getTextFromFile("web\\tv.template.htm");
      }          
      
      VS_STAGE_GROUP invateGroup = mainForm.invateGroup;
            
      PilotInfo[] pInfos = new PilotInfo[4];
      for (int i=0; i<pInfos.length; i++){
        VS_STAGE_GROUPS pilot = null;
        if (invateGroup!=null && invateGroup.users!=null && invateGroup.users.size()>i){
          pilot = invateGroup.users.get(i);
        }
        pInfos[i] = new PilotInfo(i+1, pilot);
      }
      
      IVar varsPool = new VarPool();
      varsPool.addChild(new StringVar("TITLE", "Drone Racing System"));
      for (PilotInfo pInfo : pInfos){
        pInfo.addPilotInfo(varsPool);
      }           
                             
      String html = varsPool.applyValues(templ);
      resp.getWriter().println(html);  
  }
  
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    mainForm = MainForm._mainForm;

    resp.setStatus(HttpStatus.SC_OK);
    resp.setHeader("Content-Type", "text/html; charset=UTF-8");    
    try {
      
      showInvitation(req,resp);

    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
