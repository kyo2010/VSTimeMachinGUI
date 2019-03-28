/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.io.IOException;
 
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
  private Session session;
     
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            this.session = session;
            session.getRemote().sendString("Hello !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    @OnWebSocketMessage
    public void onText(String message) {
        System.out.println("text: " + message);
        try {
          if (message.equalsIgnoreCase("background")){
            this.session.getRemote().sendString("background:"+MainForm._mainForm.BACKGROUND_FOR_TV);
          }
           // this.session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);       
    }
}
