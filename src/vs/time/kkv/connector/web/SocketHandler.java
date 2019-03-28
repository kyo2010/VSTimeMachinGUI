/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


/**
 *
 * @author kyo
 */
@WebServlet(name = "WebSocket Servlet", urlPatterns = { "/wstest" })
public class SocketHandler extends WebSocketHandler {
  
    @Override
    public void configure(WebSocketServletFactory factory) {
      //factory.getPolicy().setIdleTimeout(10000);
      factory.getPolicy().setIdleTimeout(0);
      factory.register(TVSocket.class);
    }    
    
    
    
    
}
