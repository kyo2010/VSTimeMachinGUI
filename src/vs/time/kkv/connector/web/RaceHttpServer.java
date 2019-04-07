/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import KKV.Utils.Tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.Configuration;
import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.jsp.JettyJspServlet;

//import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
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
public class RaceHttpServer implements Runnable {

  public boolean connected = false;
  Server server = null;
  Thread thread = null;
  int port;
  MainForm mainForm;

  private static final String WEBROOT_INDEX = "web";

  public void disconnect() {
    if (server != null) {
      server.getServer().setStopAtShutdown(true);
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
      }
      try {
        server.stop();
      } catch (Exception e) {
      }
    }
    connected = false;
    System.out.println("Web has been stopped");
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
      server = new Server();

      ServerConnector connector = new ServerConnector(server);
      connector.setPort(port);
      server.addConnector(connector);

      // Create Servlet context
      ServletContextHandler servletContextHandler = new ServletContextHandler();
      servletContextHandler.setContextPath("/");      
      servletContextHandler.setResourceBase(WEBROOT_INDEX);
      servletContextHandler.setSessionHandler(new SessionHandler());
      servletContextHandler.setWelcomeFiles(new String[]{ "index.html","index.jsp" });

      // Since this is a ServletContextHandler we must manually configure JSP support.
      enableEmbeddedJspSupport(servletContextHandler);

      servletContextHandler.addServlet(RaceHttpServlet.class, "/index.htm");
      servletContextHandler.addServlet(StartHttpServlet.class, "/start.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/tv.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/osd.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/lb16.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/stage.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/group_result.ajax");
      servletContextHandler.addServlet(TVTranslationServlet2.class, "/tv2.ajax");
      servletContextHandler.addServlet(TVTranslationServlet.class, "/pilot.ajax");

      //  pContextHandler.addServlet(TVSocketHandler.class, "/ws");           
      // Default Servlet (always last, always named "default")
      ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
      holderDefault.setInitParameter("resourceBase", WEBROOT_INDEX);
      holderDefault.setInitParameter("dirAllowed", "true");
      servletContextHandler.addServlet(holderDefault, "/");
      
      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[]{
        new TVSocketHandler(mainForm),
        servletContextHandler,});
      server.setHandler(handlers);
      server.start();     
      
    } catch (Exception e) {
      System.out.println(e.toString() + " " + Tools.traceError(e));
      mainForm.toLog(e);
    }
    System.out.println("Web has been started");
  }

  private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException {
    // Establish Scratch directory for the servlet context (used by JSP compilation)
    File tempDir = new File("web.tmpdir");
    tempDir.mkdirs();
    File scratchDir = new File("web.tmpdir/web.jsp");
    scratchDir.mkdirs();

    if (!scratchDir.exists()) {
      if (!scratchDir.mkdirs()) {
        throw new IOException("Unable to create scratch directory: " + scratchDir);
      }
    }
    servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);

    // Set Classloader of Context to be sane (needed for JSTL)
    // JSP requires a non-System classloader, this simply wraps the
    // embedded System classloader in a way that makes it suitable
    // for JSP to use
    ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
    servletContextHandler.setClassLoader(jspClassLoader);

    // Manually call JettyJasperInitializer on context startup
    servletContextHandler.addBean(new JspStarter(servletContextHandler));

    // Create / Register JSP Servlet (must be named "jsp" per spec)
    ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
    holderJsp.setInitOrder(0);
    holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
    holderJsp.setInitParameter("fork", "false");
    holderJsp.setInitParameter("xpoweredBy", "false");
    holderJsp.setInitParameter("compilerTargetVM", "1.8");
    holderJsp.setInitParameter("compilerSourceVM", "1.8");
    holderJsp.setInitParameter("keepgenerated", "true");
    servletContextHandler.addServlet(holderJsp, "*.jsp");
  }

  /*private static final String WEBROOT_INDEX = "web";
   private URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException
    {
        URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
        if (indexUri == null)
        {
            throw new FileNotFoundException("Unable to find resource " + WEBROOT_INDEX);
        }
        // Points to wherever /webroot/ (the resource) is
        return indexUri.toURI();
    }*/
}
