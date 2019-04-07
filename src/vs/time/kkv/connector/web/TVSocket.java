/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.io.IOException;
import java.util.Vector;
 
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
@WebSocket
public class TVSocket {
    public Session session = null;
    
    TVSocketHandler handler = null;
    public TVSocket(TVSocketHandler handler){
      this.handler = handler;
    }
    
    @OnWebSocketConnect
    public void onConnect(Session session) {
        //System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        this.session = session;
        handler.webSockets.add(this);
        //session.getRemote().sendString("Hello !");       
    }
     
    @OnWebSocketMessage
    public void onText(String message) {
        //System.out.println("text: " + message);
        try {
          //message = message.replaceAll("<", "<").replaceAll(">", ">");

          if (message.equalsIgnoreCase("GetBackground")){
            this.session.getRemote().sendString("SetBackground:"+MainForm._mainForm.BACKGROUND_FOR_TV);
          }
           // this.session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
      session = null;   
      handler.webSockets.remove(this);
    }
}
