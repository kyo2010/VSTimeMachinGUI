/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import vs.time.kkv.connector.connection.VSTimeConnector;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageNewForm;
import vs.time.kkv.connector.MainlPannels.*;
import KKV.DBControlSqlLite.DBModelTest;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import KKV.DBControlSqlLite.Utils.TempFileWrite;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import vs.time.kkv.connector.Race.RaceControlForm;
import vs.time.kkv.connector.Race.RaceList;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.Users.UserControlForm;
import vs.time.kkv.connector.Users.UserList;
import vs.time.kkv.connector.Utils.SpeekUtil;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.models.VS_BANDS;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE_GROUP;

/**
 *
 * @author kyo
 */
public class MainForm extends javax.swing.JFrame implements VSTimeMachineReciver {

  public final static String[] PILOT_TYPES = new String[]{"None-PRO", "PRO", "Freestyle"};
  public final static String[] STAGE_TYPES = new String[]{"Practica", "Qualification", "Race"};  

  public String[] getBands() {
    String[] res = new String[0];
    try {
      List<VS_BANDS> bands = VS_BANDS.dbControl.getList(con, "1=1 ORDER BY POS");
      res = new String[bands.size()];
      int index = 0;
      for (VS_BANDS band : bands) {
        res[index] = band.NAME;
        index++;
      }
    } catch (Exception e) {
      toLog(e);
    }
    return res;
  }

  public static MainForm _mainForm = null;

  public static void _toLog(Exception e) {
    if (_mainForm != null) {
      _mainForm.log.writeFile(e);
    }
  }
  
  public void toLog(Exception e) {
    log.writeFile(e);    
  }

  public VSTimeConnector vsTimeConnector = null;
  public TempFileWrite log = new TempFileWrite("VSTimeMachine.log");
  public TempFileWrite error_log = new TempFileWrite("error.log");
  public Connection con = null;
  public SpeekUtil speaker = new SpeekUtil();
  public VS_RACE activeRace = null;
  public VS_STAGE_GROUP activeGroup = null;
  public RegistrationTab regForm = null;
  public long raceTime = 0;
  public int lastTranponderID = -1;

  public void setActiveRace(VS_RACE race) {
    
    if (activeGroup!=null){
      JOptionPane.showMessageDialog(this, "Please stop race. Group"+activeGroup.GROUP_NUM, "Information", JOptionPane.INFORMATION_MESSAGE);                    
      return;
    }
    
    activeRace = race;
    // Open Tabs
    tabListenerEnabled = false;
    tabbedPanel.removeAll();

    if (race != null) {
      jmAddStageToRace.setVisible(true);
      regForm = new RegistrationTab(MainForm.this);
      tabbedPanel.add("Registration", regForm);

      try {
        List<VS_STAGE> stages = VS_STAGE.dbControl.getList(con, "RACE_ID=? order by STAGE_NUM", race.RACE_ID);
        for (VS_STAGE stage : stages) {
          StageTab p = new StageTab(this, stage);
          tabbedPanel.add(stage.CAPTION, p);
          if (stage.IS_SELECTED == 1) {
            tabbedPanel.setSelectedComponent(p);
          }
        }
        tabListenerEnabled = true;
      } catch (Exception e) {
        toLog(e);
      }
    } else {
      jmAddStageToRace.setVisible(false);
    }
  }

  boolean tabListenerEnabled = false;

  /**
   * Creates new form MainForm
   */
  public MainForm(String caption) {
    _mainForm = this;
    setTitle(caption);
    initComponents();
    setStateMenu(false);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        int res = JOptionPane.showConfirmDialog(MainForm.this, "Do you want to close?", "Exit ?", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
          if (vsTimeConnector != null) {
            vsTimeConnector.disconnect();
          }
          vsTimeConnector = null;
          MainForm.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          //setVisible(false);
          //e.getWindow().dispose();          
        }else{
          
        }
      }
    });

    ports.addItem("WLAN");
    //new ArrayList<String>({ new String[]{"m1","m2"});

    try {
      con = DBModelTest.getConnectionForTest();
    } catch (Exception e) {
      error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Database file is not found. " + DBModelTest.DATABASE, "Error", JOptionPane.ERROR_MESSAGE);
    }
    jmAddStageToRace.setVisible(false);
    try {
      VS_RACE race = VS_RACE.dbControl.getItem(con, "IS_ACTIVE=1");
      if (race != null) {
        setActiveRace(race);
      }
    } catch (Exception e) {
    }

    tabbedPanel.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        if (MainForm.this.activeRace != null && tabListenerEnabled) {
          VS_STAGE.resetSelectedTab(con, MainForm.this.activeRace.RACE_ID);
          try {
            StageTab p = (StageTab) tabbedPanel.getSelectedComponent();
            if (p.stage != null) {
              p.stage.IS_SELECTED = 1;
              VS_STAGE.dbControl.save(MainForm.this.con, p.stage);
            }
          } catch (Exception ex) {
          }
        }
      }
    });
  }

  public void setStateMenu(boolean isConnected) {
    menuDisconnect.setEnabled(isConnected);
    menuParameters.setEnabled(isConnected);
    menuConnect.setEnabled(!isConnected);
    jPanel1.setVisible(!isConnected);
    if (isConnected) {
      if (vsTimeConnector.WIFI) {
        menuParameters.setEnabled(false);
      }
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    ports = new javax.swing.JComboBox();
    connectButton = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    ping = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    tabbedPanel = new javax.swing.JTabbedPane();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    menuWLANSetting = new javax.swing.JMenuItem();
    menuConnect = new javax.swing.JMenuItem();
    menuDisconnect = new javax.swing.JMenuItem();
    menuParameters = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();
    menuAddUser = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();
    menuRace = new javax.swing.JMenu();
    miRaceList = new javax.swing.JMenuItem();
    miAddNewRace = new javax.swing.JMenuItem();
    jmAddStageToRace = new javax.swing.JMenuItem();

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null}
      },
      new String [] {
        "ID", "Name", "Best Lap", "Time", "Rings"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, true, false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jScrollPane1.setViewportView(jTable1);

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    jPanel1.setName("topPanel"); // NOI18N

    ports.setModel(new javax.swing.DefaultComboBoxModel(jssc.SerialPortList.getPortNames()));
    ports.setName("portsBox"); // NOI18N

    connectButton.setText("Connect");
    connectButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectButtonActionPerformed(evt);
      }
    });

    jLabel1.setText("Port");
    jLabel1.setToolTipText("");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(ports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(connectButton)
        .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(ports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(connectButton)
        .addComponent(jLabel1))
    );

    ports.getAccessibleContext().setAccessibleName("portsBox");
    connectButton.getAccessibleContext().setAccessibleName("bConnect");
    jLabel1.getAccessibleContext().setAccessibleName("jPortLabel");

    ping.setText("status:");

    jLabel3.setText("disconnected");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addComponent(ping)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ping)
          .addComponent(jLabel2)
          .addComponent(jLabel3))
        .addGap(0, 2, Short.MAX_VALUE))
    );

    jLabel2.getAccessibleContext().setAccessibleName("statusCaption");
    jLabel3.getAccessibleContext().setAccessibleName("jStatusText");

    jMenu1.setText("Time Machines");

    menuWLANSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/wlan-setting.png"))); // NOI18N
    menuWLANSetting.setText("WLan Setting");
    menuWLANSetting.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuWLANSettingActionPerformed(evt);
      }
    });
    jMenu1.add(menuWLANSetting);

    menuConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/USB_connect.png"))); // NOI18N
    menuConnect.setText("Connect");
    menuConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuConnectActionPerformed(evt);
      }
    });
    jMenu1.add(menuConnect);
    menuConnect.getAccessibleContext().setAccessibleName("jMenuConnect");

    menuDisconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/usb_disconnect.png"))); // NOI18N
    menuDisconnect.setText("Disconnect");
    menuDisconnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuDisconnectActionPerformed(evt);
      }
    });
    jMenu1.add(menuDisconnect);
    menuDisconnect.getAccessibleContext().setAccessibleName("jMenuDisconnect");

    menuParameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/options.png"))); // NOI18N
    menuParameters.setText("Parameter");
    menuParameters.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuParametersActionPerformed(evt);
      }
    });
    jMenu1.add(menuParameters);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("Pilots");

    menuAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/user_add.png"))); // NOI18N
    menuAddUser.setText("Add Pilot");
    menuAddUser.setName(""); // NOI18N
    menuAddUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuAddUserActionPerformed(evt);
      }
    });
    jMenu2.add(menuAddUser);

    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/user_list.png"))); // NOI18N
    jMenuItem2.setText("Global Pilot List");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem2ActionPerformed(evt);
      }
    });
    jMenu2.add(jMenuItem2);

    jMenuBar1.add(jMenu2);

    menuRace.setText("Race");

    miRaceList.setBackground(new java.awt.Color(24, 24, 24));
    miRaceList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/race_add.png"))); // NOI18N
    miRaceList.setText("Race List");
    miRaceList.setPreferredSize(new java.awt.Dimension(42, 50));
    miRaceList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miRaceListActionPerformed(evt);
      }
    });
    menuRace.add(miRaceList);

    miAddNewRace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/race_user_registration.png"))); // NOI18N
    miAddNewRace.setText("Add new Race");
    miAddNewRace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miAddNewRaceActionPerformed(evt);
      }
    });
    menuRace.add(miAddNewRace);

    jmAddStageToRace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
    jmAddStageToRace.setText("Add Stage to Race");
    jmAddStageToRace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jmAddStageToRaceActionPerformed(evt);
      }
    });
    menuRace.add(jmAddStageToRace);

    jMenuBar1.add(menuRace);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
      .addComponent(tabbedPanel)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jPanel1.getAccessibleContext().setAccessibleName("topPanel");

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void menuParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuParametersActionPerformed
    if (vsTimeConnector != null && vsTimeConnector.connected) {
      try {
        VSTMParams form = VSTMParams.getInstanse(this);
      } catch (Exception e) {
      }
    }
  }//GEN-LAST:event_menuParametersActionPerformed

  private void miRaceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRaceListActionPerformed
    RaceList.init(this).setVisible(true);
  }//GEN-LAST:event_miRaceListActionPerformed

  private void menuConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnectActionPerformed
    connectButtonActionPerformed(evt);
  }//GEN-LAST:event_menuConnectActionPerformed

  private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
    // TODO add your handling code here:
    if (vsTimeConnector != null) {
      vsTimeConnector.disconnect();
      vsTimeConnector = null;
    }
    String port = ports.getSelectedItem().toString();
    if (!port.equalsIgnoreCase("")) {
      jLabel3.setText("connecting to port " + port);
      vsTimeConnector = new VSTimeConnector(port);
      try {
        vsTimeConnector.connect(this, WLANSetting.init(this).PORT_LISTING_INT, WLANSetting.init(this).PORT_SENDING_INT);
        setStateMenu(true);
      } catch (Exception e) {
        jLabel3.setText(e.toString());
        System.out.println(e);
        vsTimeConnector = null;
      }
      speaker.speak("connected");
    }
  }//GEN-LAST:event_connectButtonActionPerformed

  private void menuDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDisconnectActionPerformed
    if (vsTimeConnector != null) {
      vsTimeConnector.disconnect();
    }
    vsTimeConnector = null;
    setStateMenu(false);
    jLabel3.setText("disconnected");
    speaker.speak("disconnected");
  }//GEN-LAST:event_menuDisconnectActionPerformed

  private void menuWLANSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuWLANSettingActionPerformed
    WLANSetting.init(this).setVisible(true);
  }//GEN-LAST:event_menuWLANSettingActionPerformed

  private void menuAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddUserActionPerformed
    // TODO add your handling code here:
    UserControlForm.init(this, true, -1).setVisible(true);
  }//GEN-LAST:event_menuAddUserActionPerformed

  private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    UserList.init(this, null).setVisible(true);
  }//GEN-LAST:event_jMenuItem2ActionPerformed

  private void miAddNewRaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAddNewRaceActionPerformed
    RaceControlForm.init(this, -1).setVisible(true);
  }//GEN-LAST:event_miAddNewRaceActionPerformed

  private void jmAddStageToRaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAddStageToRaceActionPerformed
    // TODO add your handling code here:
    if (activeRace != null) {
      StageNewForm.init(this, null).setVisible(true);
    }
  }//GEN-LAST:event_jmAddStageToRaceActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */

    try {
      UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }

    //</editor-fold>
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        MainForm mainForm = new MainForm("VS Time Connector");
        //javax.swing.ImageIcon icon = new javax.swing.ImageIcon( getProperty('fiji.dir')+"/images/vs-logo.png" );
        URL url = getClass().getResource("/images/vs-logo.png");
        ImageIcon icon = new ImageIcon(url);
        mainForm.setIconImage(icon.getImage());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainForm.setSize(dim.width / 2, dim.height / 2);
        mainForm.setLocation(dim.width / 2 - mainForm.getSize().width / 2, dim.height / 2 - mainForm.getSize().height / 2);
        mainForm.setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton connectButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JMenuItem jmAddStageToRace;
  private javax.swing.JMenuItem menuAddUser;
  private javax.swing.JMenuItem menuConnect;
  private javax.swing.JMenuItem menuDisconnect;
  private javax.swing.JMenuItem menuParameters;
  private javax.swing.JMenu menuRace;
  private javax.swing.JMenuItem menuWLANSetting;
  private javax.swing.JMenuItem miAddNewRace;
  private javax.swing.JMenuItem miRaceList;
  private javax.swing.JLabel ping;
  private javax.swing.JComboBox ports;
  private javax.swing.JTabbedPane tabbedPanel;
  // End of variables declaration//GEN-END:variables

  public JTabbedPane getTabbedPanels(){
    return tabbedPanel;
  } 
  
  public void setFormOnCenter(Window form) {
    Point p = this.getLocationOnScreen();
    form.setLocation(p.x + this.getWidth() / 2 - form.getSize().width / 2, p.y + this.getHeight() / 2 - form.getSize().height / 2);
  }

  @Override
  public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap) {
    boolean isPingCommand = false;
    if (commands[0].equalsIgnoreCase("ping")) {
      isPingCommand = true;
    }
    jLabel3.setText(data+"   "+vsTimeConnector.last_error);
    if (!isPingCommand) {
      System.out.print(data);
      log.writeFile(data, true);
    }
    if (lap!=null){
      this.lastTranponderID = lap.transponderID;
    }
  }
}
