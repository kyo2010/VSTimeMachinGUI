/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
@WebServlet(name = "WebSocket Servlet", urlPatterns = {"/wstest"})
public class TVSocketHandler extends WebSocketHandler implements MainForm.IMainFormListener {

  public Set<TVSocket> webSockets = new CopyOnWriteArraySet<TVSocket>();

  MainForm mainForm = null;
  public TVSocketHandler(MainForm mainForm){
    this.mainForm = mainForm;
    mainForm.setMainFormListener(this);    
  }

  @Override
  public void backgroundIsCnanged() {
    for (TVSocket webSocket : webSockets) {
      try{
        webSocket.session.getRemote().sendString("SetBackground:"+MainForm._mainForm.BACKGROUND_FOR_TV);
      }catch(Exception e){}
    }
  }   
  
  @Override
  protected void doStart() throws Exception {
    super.doStart(); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void configure(WebSocketServletFactory factory) {
    //factory.getPolicy().setIdleTimeout(10000);
    factory.getPolicy().setIdleTimeout(0);
    //factory.register(TVSocket.class);
    factory.setCreator(new WebSocketCreator() {
      @Override
      public Object createWebSocket(ServletUpgradeRequest sur, ServletUpgradeResponse sur1) {
        return new TVSocket(TVSocketHandler.this);
      }
    });
  }

}
