/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import vs.time.kkv.connector.MainForm;
//import org.eclipse.jetty.webapp.Configuration;


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
  
  private static final String WEBROOT_INDEX = "/web1/";

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
      
      /*ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
      context.setClassLoader(jspClassLoader);*/
      
      //ServletHolder holderDefault = new ServletHolder("default",DefaultServlet.class);
      //holderDefault.setInitParameter("resourceBase",new File("web").getAbsolutePath());
      //holderDefault.setInitParameter("dirAllowed","true");      
      
     
      
      ResourceHandler resource_handler = new ResourceHandler();
      //resource_handler.setDirectoriesListed(true);
      resource_handler.setWelcomeFiles(new String[]{ "index.html" });
      resource_handler.setResourceBase("web");
      ContextHandler contextHandler= new ContextHandler("/"); 
      contextHandler.setHandler(resource_handler);      
         

     // 
      //handler.addServlet(resource_handler,"/");
      //server.setHandler(resource_handler);
      
      //JspServlet servlet =new 
      ///handler.addServlet(RaceHttpServlet.class, "/index.html"); 
      //handler.
      
      //HandlerList handlers = new HandlerList( );
      //handlers.setHandlers( new Handler[] { ServletHolder } );
      //server.setHandler( handlers );
                        

      
      handler.setWelcomeFiles(new String[]{ "index.htm" });
      handler.addServlet(RaceHttpServlet.class, "/index.htm");
      handler.addServlet(TVTranslationServlet.class, "/tv.htm");
      
     
      //server.setHandler(handler);
      
      
      
      
       HandlerList handlers = new HandlerList( );
       handlers.setHandlers( new Handler[] {                  
         contextHandler,
         handler,         
       } );
       server.setHandler( handlers );       
      
      
      
      //server.setHandler(resource_handler);
      
      
      //WebAppContext ctx = new WebAppContext();      
      //ctx.setResourceBase("web");  
      //ctx.setContextPath("/"); 
      //ctx.addServlet(RaceHttpServlet.class, "/");
      //server.setHandler(ctx);
      
      server.start();
    } catch (Exception e) {
    }
  }

}
