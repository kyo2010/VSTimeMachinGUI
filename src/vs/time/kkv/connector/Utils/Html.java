/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.Thread.State;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import jdk.nashorn.api.scripting.JSObject;

public class Html extends JFrame {

  private JPanel contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Html frame = new Html();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   *
   * @throws IOException
   */
  public Html() throws IOException {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    JButton btnNewButton = new JButton("New button");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {

        final JFXPanel jfxPanel = new JFXPanel();
        JFrame frame2 = new JFrame();
        frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame2.add(jfxPanel);
        frame2.setVisible(true);

        Platform.runLater(new Runnable() {

          @Override
          public void run() {
            WebView webView = new WebView();
            jfxPanel.setScene(new Scene(webView));
            //webView.setZoom(2);           
            //webView.getEngine().load("http://reports.root.panasonic.ru/PCISWebReportServer/webServer/index.jsp");// Грузит страничку с нета, а мне нужно с прэкта
            webView.getEngine().load("http://yandex.ru");
          }

        });
      }

    });
    contentPane.add(btnNewButton, BorderLayout.WEST);

  }

  public static void createHTMLPane(final String uri) {
    final JFXPanel jfxPanel = new JFXPanel();
    Dimension m = new Dimension(800, 600);
    jfxPanel.setPreferredSize(m);
    jfxPanel.setMinimumSize(m);
    JFrame frame2 = new JFrame();
    //frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);        
    frame2.add(jfxPanel);
    frame2.pack();
    frame2.setVisible(true);

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        //webEngine.executeScript("changeBgColor();"); 
        //webView.setWebViewClient();

        jfxPanel.setScene(new Scene(webView));
        //webView.setZoom(2);
        System.out.println("url:" + uri);

        // A Worker load the page
        Worker<Void> worker = webEngine.getLoadWorker();        

        //webView.
        webEngine.load(uri);// Грузит страничку с нета, а мне нужно с прэкта

        //String html = "<html><h1>Hello</h1><h2>Hello</h2></html>";
        // Load HTML String
        //    webEngine.loadContent(html);
      }

    });
  }

}
