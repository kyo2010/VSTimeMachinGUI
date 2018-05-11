/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import vs.time.kkv.connector.web.RaceHttpServer;
import vs.time.kkv.connector.connection.VSTimeConnector;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageNewForm;
import vs.time.kkv.connector.MainlPannels.*;
import KKV.DBControlSqlLite.DBModelTest;
import KKV.Utils.UserException;
import KKV.Utils.JDEDate;
import KKV.Utils.ParseIniFile;
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
import KKV.Utils.TempFileWrite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTableAdapter;
import vs.time.kkv.connector.Race.RaceControlForm;
import vs.time.kkv.connector.Race.RaceList;
import static vs.time.kkv.connector.SystemOptions.singelton;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.Users.UserControlForm;
import vs.time.kkv.connector.Users.UserList;
import vs.time.kkv.connector.Utils.Beep;
import vs.time.kkv.connector.Utils.Html;
import vs.time.kkv.connector.Utils.TTS.SpeekUtil;
import static vs.time.kkv.connector.WLANSetting.singelton;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.models.DataBaseStructure;
import vs.time.kkv.models.VS_BANDS;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_SETTING;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class MainForm extends javax.swing.JFrame implements VSTimeMachineReciver, VSTimeConnector.VSSendListener {

  public static final int STAGE_PRACTICA = 0;
  public static final int STAGE_QUALIFICATION = 1;
  public static final int STAGE_QUALIFICATION_RESULT = 2;
  public static final int STAGE_RACE = 3;
  public static final int STAGE_RACE_RESULT = 4;
  public static final int STAGE_RACE_REPORT = 5;

  public static final int STAGE_SORT_BY_RACE_TIME = 0;
  public static final int STAGE_SORT_BY_LAP_TIME = 1;
  public static final int STAGE_SORT_BY_SCORE_DESC = 2;
  public static final int STAGE_SORT_BY_LOSS_DESC = 3;
  public static final int STAGE_SORT_BY_LAPS = 4;
  public static final int STAGE_SORT_BY_LAPS_AND_SCORES = 5;
  public final static String[] STAGE_SORTS = new String[]{"Race time", "Best lap time", "Score", "Loss", "Laps & LAP Time", "Laps & Scores"};

  public final static String[] PILOT_TYPES = new String[]{"None-PRO", "PRO", "Freestyle"};
  public final static String[] PILOT_TYPES_NONE = new String[]{"None-PRO", "PRO", "Freestyle", "None"};
  public final static int PILOT_TYPE_NONE_INDEX = 3;
  public final static String[] STAGE_TYPES = new String[]{"Practica", "Qualification",
    "Qualification Result", "Race",
    "Race Result", "Race Report"};

  public static final int RACE_TYPE_OLYMPIC = 3;
  public static final int RACE_TYPE_OLYMPIC_LOSES = 4;
  public static final int RACE_TYPE_WHOOP = 2;
  public static final int RACE_TYPE_DOUBLE = 1;
  public static final int RACE_TYPE_SINGLE = 0;
  public final static String[] RACE_TYPES = new String[]{"Single elemination", "Double elemenation", "Whoop Race", "Olimpic Single elemenation", "Olimpic Losers"};

  public static final int SCORE_WHOOP_NONE = 0;
  public static final int SCORE_WHOOP_WON = 1;
  public static final int SCORE_WHOOP_LOST = 2;
  public final static String[] SCORES_WHOOP = new String[]{"", "Won", "Lost"};

  public boolean USE_TRANS_FOR_GATE = false;
  public int TRANS_FOR_GATE = 0;
  public VSColor TRANS_FOR_GATE_COLOR = null;
  public boolean TRANS_FOR_GATE_BLINK = false;

  public String BACKGROUND_FOR_TV = "";

  public static boolean PLEASE_CLOSE_ALL_THREAD = false;
  public String activeLang = "EN";
  public ParseIniFile langFile = null;
  public int RFIDLockPassword = 55;

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

  public static void _toLog(String msg) {
    if (_mainForm != null) {
      _mainForm.log.writeFile(msg);
    }
  }

  public void toLog(Exception e) {
    log.writeFile(e);
  }

  public void toLog(String st) {
    log.writeFile(st);
  }

  public VSTimeConnector vsTimeConnector = null;
  public TempFileWrite log = new TempFileWrite("VSTimeMachine.log");
  public TempFileWrite error_log = new TempFileWrite("error.log");
  public TempFileWrite lap_log = new TempFileWrite("lap.log");
  public TempFileWrite race_log = new TempFileWrite("race.csv");
  public Connection con = null;
  public SpeekUtil speaker = null;
  public Beep beep = null;

  public VS_RACE activeRace = null;
  public VS_STAGE_GROUP activeGroup = null;
  public VS_STAGE_GROUP invateGroup = null;
  public VS_STAGE_GROUP lastCheckingGrpup = null;
  public VS_STAGE activeStage = null;

  public RegistrationTab regForm = null;
  public long raceTime = 0;
  public long unRaceTime = Calendar.getInstance().getTimeInMillis();
  public int lastTranponderID = -1;
  public RaceHttpServer httpServer = null;
  public String LOCAL_HOST = "localhost";

  public List<StageTab> stageTabs = new ArrayList<StageTab>();

  public void setActiveRace(VS_RACE race) {

    if (activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop race. Group" + activeGroup.GROUP_NUM, "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    activeRace = race;
    setTitle("Race - " + race.RACE_NAME);
    // Open Tabs
    tabListenerEnabled = false;
    tabbedPanel.removeAll();

    if (race != null) {
      jmAddStageToRace.setVisible(true);
      regForm = new RegistrationTab(MainForm.this);
      tabbedPanel.add("Registration", regForm);

      try {
        List<VS_STAGE> stages = VS_STAGE.dbControl.getList(con, "RACE_ID=? order by STAGE_NUM", race.RACE_ID);
        int index = 1;
        boolean isFound = false;
        StageTab stage1 = null;
        stageTabs.clear();
        for (VS_STAGE stage : stages) {
          stage.race = race;
          StageTab p = new StageTab(this, stage);
          stageTabs.add(p);
          String prefix = "";
          if (stage.STAGE_TYPE > MainForm.STAGE_QUALIFICATION && stage.PILOT_TYPE < MainForm.PILOT_TYPE_NONE_INDEX) {
            try {
              prefix = " [" + MainForm.PILOT_TYPES[stage.PILOT_TYPE] + "]";
            } catch (Exception e) {
            }
          }
          tabbedPanel.add(stage.CAPTION + prefix, p);

          //tabbedPanel.setForegroundAt(index, Color.BLUE);
          //tabbedPanel.setBackgroundAt(index, Color.ORANGE);
          if (stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION) {
            tabbedPanel.setForegroundAt(index, Color.BLUE);
          }
          if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
            tabbedPanel.setForegroundAt(index, Color.RED);
          }
          if (stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
            tabbedPanel.setForegroundAt(index, new Color(00, 99, 00));
          }
          if (stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT || stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT) {
            tabbedPanel.setForegroundAt(index, Color.MAGENTA);
          }
          if (stage.IS_LOCK == 1) {
            tabbedPanel.setForegroundAt(index, Color.CYAN);
          }
          if (stage.IS_SELECTED == 1) {
            tabbedPanel.setSelectedComponent(p);
            isFound = true;
          }
          stage1 = p;
          index++;
        }
        if (!isFound && stage1 != null) {
          tabbedPanel.setSelectedComponent(stage1);
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

    try {
      UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }

    setTitle(caption);
    initComponents();

    bRefreshActionPerformed(null);

    setStateMenu(false);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        int res = JOptionPane.showConfirmDialog(MainForm.this, "Do you want to close?", "Exit ?", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
          PLEASE_CLOSE_ALL_THREAD = true;
          if (vsTimeConnector != null) {
            vsTimeConnector.disconnect();
          }
          vsTimeConnector = null;
          MainForm.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          //setVisible(false);
          //e.getWindow().dispose();          
        } else {

        }
      }
    });

    //ports.addItem("WLAN");
    //new ArrayList<String>({ new String[]{"m1","m2"});
    try {
      con = DBModelTest.getConnectionForTest();

      // check for update
      double db_version = VS_SETTING.getParam(con, "DataBaseVersion", 1.0);
      double db_version_new = DataBaseStructure.executeAddons(db_version, con);
      VS_SETTING.setParam(con, "DataBaseVersion", "" + db_version_new);

    } catch (UserException ue) {
      error_log.writeFile(ue);
      JOptionPane.showMessageDialog(this, ue.details, ue.error, JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Database file is not found. " + DBModelTest.DATABASE, "Error", JOptionPane.ERROR_MESSAGE);
    }
    applayLanguage();

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

    speaker = new SpeekUtil(this);
    beep = new Beep(this);

    //beep.palyAndWait("attention");
    //beep.paly("three");
    //beep.paly("two");
    //beep.paly("one");
    //beep.paly("beep");
    TimerForm.init(this).setVisible(true);

    if (VS_SETTING.getParam(con, "START_HTTPD_ON_RUN", 0) == 1) {
      SystemOptions.runWebServer(this, true);
    }

    try {
      LOCAL_HOST = Inet4Address.getLocalHost().getHostAddress();
      System.out.println("Local IP: " + LOCAL_HOST);
    } catch (UnknownHostException e) {
      toLog(e);
      //e.printStackTrace();
    }
  }

  public void applayLanguage() {
    activeLang = VS_SETTING.getParam(con, "LANG", "EN");
    try {
      langFile = new ParseIniFile("locale/" + activeLang + ".ini");
    } catch (Exception e) {
      langFile = null;
    }

    STAGE_COLUMN.changeLocale(StageTableAdapter.STAGE_COLUMNS_STAGE, this);
    STAGE_COLUMN.changeLocale(StageTableAdapter.STAGE_COLUMNS_STAGE_RACE, this);
    STAGE_COLUMN.changeLocale(StageTableAdapter.STAGE_COLUMNS_RESULT, this);
    STAGE_COLUMN.changeLocale(StageTableAdapter.STAGE_COLUMNS_REPORT, this);
    STAGE_COLUMN.changeLocale(StageTableAdapter.STAGE_COLUMNS_RACE_RESULT, this);
    STAGE_COLUMN.changeLocale(RegistrationModelTable.STAGE_COLUMNS_STAGE, this);

    USE_TRANS_FOR_GATE = VS_SETTING.getParam(con, "USE_TRANS_FOR_GATE", 0) == 1 ? true : false;
    TRANS_FOR_GATE = VS_SETTING.getParam(con, "TRANS_FOR_GATE", 0);

    TRANS_FOR_GATE_BLINK = VS_SETTING.getParam(con, "TRANS_FOR_GATE_BLINK", 0) == 1 ? true : false;
    TRANS_FOR_GATE_COLOR = VSColor.getColor(VS_SETTING.getParam(con, "TRANS_FOR_GATE_COLOR", "RED"));

    BACKGROUND_FOR_TV = VS_SETTING.getParam(con, "TV_BACKGROUND", "chromokey.png");

    if (vsTimeConnector != null && vsTimeConnector.connected) {      
      try {
        if (USE_TRANS_FOR_GATE){
          vsTimeConnector.setColor(TRANS_FOR_GATE, TRANS_FOR_GATE_COLOR.vscolor);
        }else{
          // public void addBlinkTransponder(int transID, int color, VSColor gateColor, boolean isBlink) 
          vsTimeConnector.addBlinkTransponder(-1,0,null,false);
        }  
      } catch (Exception e) {
      }
    }
  }

  public void setColorForGate() {
    if (USE_TRANS_FOR_GATE && vsTimeConnector != null && vsTimeConnector.connected) {
      try {
        vsTimeConnector.setColor(TRANS_FOR_GATE, TRANS_FOR_GATE_COLOR.vscolor);
      } catch (Exception e) {
      }
    }
  }

  public String getLocaleString(String caption) {
    if (langFile != null) {
      try {
        String val = langFile.getParam(caption);
        if (val != null) {
          return val;
        }
      } catch (Exception e) {
      }
    }
    return caption;
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
    bRefresh = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    ping = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    tabbedPanel = new javax.swing.JTabbedPane();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu3 = new javax.swing.JMenu();
    mSystemOptions = new javax.swing.JMenuItem();
    mSystemMonitor = new javax.swing.JMenuItem();
    jMenuItemTVMonitor = new javax.swing.JMenuItem();
    mConsole = new javax.swing.JMenuItem();
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

    ports.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ports.setName("portsBox"); // NOI18N

    connectButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    connectButton.setText("Connect");
    connectButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectButtonActionPerformed(evt);
      }
    });

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel1.setText("Port");
    jLabel1.setToolTipText("");

    bRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/refresh-icon_s.png"))); // NOI18N
    bRefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bRefreshActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ports, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(connectButton)
        .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(ports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(connectButton)
        .addComponent(jLabel1)
        .addComponent(bRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    tabbedPanel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        tabbedPanelStateChanged(evt);
      }
    });

    jMenu3.setText("System");
    jMenu3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

    mSystemOptions.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    mSystemOptions.setText("Options");
    mSystemOptions.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mSystemOptionsActionPerformed(evt);
      }
    });
    jMenu3.add(mSystemOptions);

    mSystemMonitor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    mSystemMonitor.setText("Web Monitor");
    mSystemMonitor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mSystemMonitorActionPerformed(evt);
      }
    });
    jMenu3.add(mSystemMonitor);

    jMenuItemTVMonitor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItemTVMonitor.setText("TV Monitor");
    jMenuItemTVMonitor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItemTVMonitorActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItemTVMonitor);

    mConsole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    mConsole.setText("VS Team Console");
    mConsole.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mConsoleActionPerformed(evt);
      }
    });
    jMenu3.add(mConsole);

    jMenuBar1.add(jMenu3);

    jMenu1.setText("Time Machines");
    jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

    menuWLANSetting.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    menuWLANSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/wlan-setting.png"))); // NOI18N
    menuWLANSetting.setText("WLan Setting");
    menuWLANSetting.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuWLANSettingActionPerformed(evt);
      }
    });
    jMenu1.add(menuWLANSetting);

    menuConnect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    menuConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/USB_connect.png"))); // NOI18N
    menuConnect.setText("Connect");
    menuConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuConnectActionPerformed(evt);
      }
    });
    jMenu1.add(menuConnect);
    menuConnect.getAccessibleContext().setAccessibleName("jMenuConnect");

    menuDisconnect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    menuDisconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/usb_disconnect.png"))); // NOI18N
    menuDisconnect.setText("Disconnect");
    menuDisconnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuDisconnectActionPerformed(evt);
      }
    });
    jMenu1.add(menuDisconnect);
    menuDisconnect.getAccessibleContext().setAccessibleName("jMenuDisconnect");

    menuParameters.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
    jMenu2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

    menuAddUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    menuAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/user_add.png"))); // NOI18N
    menuAddUser.setText("Add Pilot");
    menuAddUser.setName(""); // NOI18N
    menuAddUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuAddUserActionPerformed(evt);
      }
    });
    jMenu2.add(menuAddUser);

    jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
    menuRace.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

    miRaceList.setBackground(new java.awt.Color(24, 24, 24));
    miRaceList.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    miRaceList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/race_add.png"))); // NOI18N
    miRaceList.setText("Race List");
    miRaceList.setPreferredSize(new java.awt.Dimension(42, 50));
    miRaceList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miRaceListActionPerformed(evt);
      }
    });
    menuRace.add(miRaceList);

    miAddNewRace.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    miAddNewRace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/race_user_registration.png"))); // NOI18N
    miAddNewRace.setText("Add new Race");
    miAddNewRace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        miAddNewRaceActionPerformed(evt);
      }
    });
    menuRace.add(miAddNewRace);

    jmAddStageToRace.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        .addGap(4, 4, 4)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jPanel1.getAccessibleContext().setAccessibleName("topPanel");
    jPanel1.getAccessibleContext().setAccessibleDescription("");
    tabbedPanel.getAccessibleContext().setAccessibleName("");

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

  public void connect() {
    connectButtonActionPerformed(null);
  }

  private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
    // TODO add your handling code here:
    if (vsTimeConnector != null) {
      vsTimeConnector.disconnect();
      vsTimeConnector = null;
    }
    String port = ports.getSelectedItem().toString();
    if (!port.equalsIgnoreCase("")) {
      jLabel3.setText("connecting to port " + port);

      String staticIP = null;
      if (VS_SETTING.getParam(con, "USE_STATIC_IP", "no").equalsIgnoreCase("yes")) {
        staticIP = VS_SETTING.getParam(con, "STATIC_IP", "192.168.1.255");
      };

      vsTimeConnector = new VSTimeConnector(this, port,
              VS_SETTING.getParam(con, "WAN_CONNECTION", ""), staticIP,
              WLANSetting.init(this).PORT_LISTING_INT, WLANSetting.init(this).PORT_SENDING_INT, con);
      vsTimeConnector.setSendListener(this);
      try {
        vsTimeConnector.connect();
        setStateMenu(true);
      } catch (Exception e) {
        jLabel3.setText(e.toString());
        System.out.println(e);
        vsTimeConnector = null;
      }
      //speaker.speak(speaker.getSpeachMessages().connected());
    }
  }//GEN-LAST:event_connectButtonActionPerformed

  public void disconnect() {
    menuDisconnectActionPerformed(null);
  }

  private void menuDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDisconnectActionPerformed
    if (vsTimeConnector != null) {
      vsTimeConnector.disconnect();
    }
    vsTimeConnector = null;
    setStateMenu(false);
    jLabel3.setText("disconnected");
    //speaker.speak(speaker.getSpeachMessages().disconnected());
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

  private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged
    // TODO add your handling code here:       
    //JOptionPane.showMessageDialog(this, ""+tabbedPanel.getSelectedComponent(), "test", JOptionPane.ERROR_MESSAGE);
    invateGroup = null;
    activeStage = null;
    Object tabObject = evt.getSource();
    if (tabObject instanceof JTabbedPane) {
      Object obj = tabbedPanel.getSelectedComponent();
      if (obj != null && obj instanceof StageTab) {
        StageTab tab = (StageTab) obj;
        if (tab.jchTV.isSelected()) {
          activeStage = tab.stage;
        }
      }
    }
  }//GEN-LAST:event_tabbedPanelStateChanged

  private void mSystemOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSystemOptionsActionPerformed
    // TODO add your handling code here:
    SystemOptions.init(this).setVisible(true);
  }//GEN-LAST:event_mSystemOptionsActionPerformed

  private void mSystemMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSystemMonitorActionPerformed
    if (httpServer == null) {
      SystemOptions.runWebServer(this, true);
    }

    String uri = "http://" + LOCAL_HOST + ":" + VS_SETTING.getParam(this.con, "WEB_PORT", 80);
    System.out.println("open url:" + uri);

    //WebPannel.getInstance("http://localhost:"+VS_SETTING.getParam(this.con, "WEB_PORT", 80)).setVisible(true);
    //Html.createHTMLPane("http://localhost:"+VS_SETTING.getParam(this.con, "WEB_PORT", 80));       
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI(uri));
      } catch (Exception e) {
        _toLog(e);
      }
    } else {
      Runtime runtime = Runtime.getRuntime();
      try {
        runtime.exec("xdg-open " + uri);
      } catch (IOException e) {
        _toLog(e);
      }

      //String os = System.getProperty("os.name").toLowerCase();
      // for Ubuntu
      // Runtime runtime = Runtime.getRuntime();
      // runtime.exec("/usr/bin/firefox -new-window " + url);
      // mac
      //Runtime rt = Runtime.getRuntime();
      //rt.exec("open " + url);
      // windows
      // rt.exec("rundll32 url.dll,FileProtocolHandler " + uri);
    }

    //Html.createHTMLPane("http://reports.root.panasonic.ru/PCISWebReportServer/webServer/index.jsp");
    //Html.createHTMLPane("http://reports.root.panasonic.ru/PCISWebReportServer/webServer/index.jsp");
  }//GEN-LAST:event_mSystemMonitorActionPerformed

  private void mConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mConsoleActionPerformed
    // TODO add your handling code here:
    VSTeamConsole.init(this).setVisible(true);
  }//GEN-LAST:event_mConsoleActionPerformed

  private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
    // TODO add your handling code here:    
    ports.removeAllItems();
    Pattern pattern = Pattern.compile("");
    if (SerialNativeInterface.getOsType() == 3) {
      pattern = Pattern.compile("tty.*USB*");
    }
    String portNames[] = SerialPortList.getPortNames(pattern);
    for (String portName : portNames) {
      ports.addItem(portName);
    }
    ports.addItem("WLAN");

    //ports.setModel(new javax.swing.DefaultComboBoxModel(jssc.SerialPortList.getPortNames()));
    //ports.addItem("WLAN");
  }//GEN-LAST:event_bRefreshActionPerformed

  private void jMenuItemTVMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTVMonitorActionPerformed
    // TODO add your handling code here:
    if (httpServer == null) {
      SystemOptions.runWebServer(this, true);
    }

    String uri = "http://" + LOCAL_HOST + ":" + VS_SETTING.getParam(this.con, "WEB_PORT", 80) + "/tv.htm";
    System.out.println("open url:" + uri);

    //WebPannel.getInstance("http://localhost:"+VS_SETTING.getParam(this.con, "WEB_PORT", 80)).setVisible(true);
    //Html.createHTMLPane("http://localhost:"+VS_SETTING.getParam(this.con, "WEB_PORT", 80));       
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI(uri));
      } catch (Exception e) {
        _toLog(e);
      }
    } else {
      Runtime runtime = Runtime.getRuntime();
      try {
        runtime.exec("xdg-open " + uri);
      } catch (IOException e) {
        _toLog(e);
      }
    }
  }//GEN-LAST:event_jMenuItemTVMonitorActionPerformed

  public static ImageIcon windowsIcon = null;

  public static ImageIcon getWindowsIcon() {
    return windowsIcon;
  }

  public static void setWindowsIcon(URL url) {
    windowsIcon = new ImageIcon(url);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        
        /*try{
          
          for (javax.swing.UIManager.LookAndFeelInfo info :  javax.swing.UIManager.getInstalledLookAndFeels()) {
            System.out.println(info.getClassName());
            // javax.swing.UIManager.setLookAndFeel(info.getClassName());
          }
          
          // javax.swing.plaf.metal.MetalLookAndFeel
          // javax.swing.plaf.nimbus.NimbusLookAndFeel 
          // com.sun.java.swing.plaf.motif.MotifLookAndFeel 
          // com.sun.java.swing.plaf.windows.WindowsLookAndFeel 
          // com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
         
          //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
          //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
          //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFee");
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception ein){}*/
        
        MainForm mainForm = new MainForm("VS Time Connector");
        setWindowsIcon(getClass().getResource("/images/vs-logo.png"));
        mainForm.setIconImage(getWindowsIcon().getImage());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainForm.setSize( (int) (dim.width *0.9), (int) (dim.height * 0.8));
        //mainForm.setSize( dim.width, dim.height);
        mainForm.setLocation(dim.width / 2 - mainForm.getSize().width / 2, dim.height / 2 - mainForm.getSize().height / 2);
        mainForm.setVisible(true);
      }
    });

    /*TreeSet<String> massiv = new TreeSet<>();
    String word = "�����";

    // 60
    int all_count = 0;
    int skip_count = 0;
    //int count_laterr = 4;
    for (int k1 = 0; k1 < word.length(); k1++) {
      for (int k2 = 0; k2 < word.length(); k2++) {
        for (int k3 = 0; k3 < word.length(); k3++) {
         // for (int k4 = 0; k4 < word.length(); k4++) {
            //for (int k5 = 0; k5 < word.length(); k5++) {
              String word2 = "" + word.charAt(k1) + word.charAt(k2) + word.charAt(k3);// + word.charAt(k4);// + word.charAt(k5);
              boolean skip2 = false;
              // skips 
              String last_word = word;
              for (int i = 0; i < word2.length(); i++) {
                int pos = last_word.indexOf(word2.charAt(i));
                if (pos >= 0) {
                  if (pos == 0) {
                    last_word = last_word.substring(pos + 1);
                  } else {
                    last_word = last_word.substring(0, pos) + last_word.substring(pos + 1);
                  }
                } else {
                  skip2 = true;
                  break;
                }
              }

              if (!skip2) {
                all_count++;
                if (massiv.contains(word2)) {
                  skip_count++;
                  System.out.println("skip:" + word2);
                } else {
                  massiv.add(word2);
                }
              }
            //}
          //}
        }
      }

    }
    System.out.println("All words : " + all_count);
    System.out.println("skips : " + skip_count);
    System.out.println("Right answer : " + massiv.size());*/
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bRefresh;
  private javax.swing.JButton connectButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenu jMenu3;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JMenuItem jMenuItemTVMonitor;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JMenuItem jmAddStageToRace;
  private javax.swing.JMenuItem mConsole;
  private javax.swing.JMenuItem mSystemMonitor;
  private javax.swing.JMenuItem mSystemOptions;
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

  public JTabbedPane getTabbedPanels() {
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

    if (VSTeamConsole.isOpened) {
      if (isPingCommand && !VSTeamConsole.showPing) {
        // ignore ping
      } else {
        VSTeamConsole.addText(data);
      }
    }

    jLabel3.setText(data + "   " + (vsTimeConnector != null ? vsTimeConnector.last_error : ""));
    if (!isPingCommand) {
      System.out.println(data);
      log.writeFile(data, true);
    }
    if (lap != null) {

      if (lap.transponderID == TRANS_FOR_GATE) {
        if (regForm != null && regForm.unRaceLapSound.isSelected()) {
          speaker.speak(speaker.getSpeachMessages().gate());
        }
        return;
      }

      this.lastTranponderID = lap.transponderID;
      long timeS = Calendar.getInstance().getTimeInMillis();
      long time = lap.time;
      lap_log.writeFile("LAP;" + new JDEDate(time).getDateAsYYYYMMDD_andTime("-", ":") + ";" + lap.transponderID + ";" + lap.baseStationID + ";" + lap.numberOfPacket + ";" + lap.transpnderCounter);
      if (Math.abs(time - timeS) > 1000 * 60 * 60) {
        time = timeS;//lap.time;
        System.out.println("Big gap between Time");
      }
      this.lastTranponderID = lap.transponderID;
      VS_STAGE_GROUP activeGroup = this.activeGroup;
      if (VSTeamConsole.isOpened) {
        VSTeamConsole.setLastTransID("" + lastTranponderID);
      }

      VS_REGISTRATION usr_reg = null;
      if (activeRace != null) {
        try {
          usr_reg = VS_REGISTRATION.dbControl.getItem(con, "VS_RACE_ID=? and (VS_TRANSPONDER=? OR VS_TRANS2=? OR VS_TRANS3=?)",
                  activeRace.RACE_ID, lap.transponderID, lap.transponderID, lap.transponderID);
        } catch (Exception ein) {
        }
      }
      if (transponderListener != null) {
        transponderListener.newTransponder(lap.transponderID, usr_reg);
      }

      if (activeGroup == null) {
        if (usr_reg != null) {
          if (regForm != null && regForm.unRaceLapSound.isSelected()) {
            speaker.speak(speaker.getSpeachMessages().msg(usr_reg.VS_USER_NAME));
          }
        } else {
          if (regForm != null && regForm.unRaceLapSound.isSelected()) {
            speaker.speak(speaker.getSpeachMessages().pilot("" + lap.transponderID));
          }
        }
        if (USE_TRANS_FOR_GATE && TRANS_FOR_GATE != 0) {
          vsTimeConnector.addBlinkTransponder(TRANS_FOR_GATE, VSColor.AQUA.vscolor, TRANS_FOR_GATE_COLOR, TRANS_FOR_GATE_BLINK);
          //vsTimeConnector.blinkingTimer.restart();
        }

        try {
          if (lastCheckingGrpup != null && lastCheckingGrpup.users != null) {
            for (VS_STAGE_GROUPS user : lastCheckingGrpup.users) {
              if (user.isTransponderForUser(con, activeRace.RACE_ID, lap.transponderID)) {
                user.RECEIVED_LAPS = true;
                if (activeGroup.stageTab != null) {
                  activeGroup.stageTab.pleasuUpdateTable = true;
                }
                break;
              }
            }
          }
        } catch (Exception e) {
          toLog(e);
        }
      }

      if (activeRace != null && activeGroup != null) {
        VS_STAGE_GROUPS user = null;
        for (VS_STAGE_GROUPS usr : activeGroup.users) {
          if (usr.VS_PRIMARY_TRANS == lap.transponderID) {
            user = usr;
            break;
          }
        }
        if (user == null) { // find user by TransID
          for (VS_STAGE_GROUPS usr : activeGroup.users) {
            if (usr.isTransponderForUser(con, activeRace.RACE_ID, lap.transponderID)) {
              user = usr;
              break;
            }
          }
        }
        if (user == null) {
          try {
            if (usr_reg == null) {
              VS_USERS global_user = VS_USERS.dbControl.getItem(con, "VSID=? OR VSID2=? OR VSID3=?", lap.transponderID, lap.transponderID, lap.transponderID);
              if (global_user == null) {
                global_user = new VS_USERS();
                global_user.VS_SOUND_EFFECT = 1;
                global_user.VSID1 = lap.transponderID;
                String userName = "USER_" + lap.transponderID;
                global_user.setName(userName);
                global_user.FIRST_NAME = userName;
                global_user.SECOND_NAME = userName;
                global_user.WEB_SID = "";
                global_user.WEB_SYSTEM = "";
                VS_USERS.dbControl.insert(con, global_user);
              }
              try {
                usr_reg = VS_REGISTRATION.dbControl.getItem(con, "VS_RACE_ID=? AND VS_USER_NAME=?", activeRace.RACE_ID, global_user.VS_NAME);
              } catch (Exception ein) {
              }
              if (usr_reg == null) {
                usr_reg = new VS_REGISTRATION();
                usr_reg.VS_TRANS1 = lap.transponderID;
                usr_reg.VS_USER_NAME = global_user.VS_NAME;
                usr_reg.VS_SOUND_EFFECT = global_user.VS_SOUND_EFFECT;
                usr_reg.VS_RACE_ID = activeGroup.stage.RACE_ID;
                usr_reg.IS_ACTIVE = 0;
                usr_reg.PILOT_TYPE = 0;
                usr_reg.FIRST_NAME = global_user.FIRST_NAME;
                usr_reg.SECOND_NAME = global_user.SECOND_NAME;
                usr_reg.WEB_SYSTEM = global_user.WEB_SYSTEM;
                usr_reg.WEB_SID = global_user.WEB_SID;
                usr_reg.NUM = VS_REGISTRATION.maxNum(con, usr_reg.VS_RACE_ID) + 1;
                VS_REGISTRATION.dbControl.insert(con, usr_reg);
              }
            }

            try {
              for (VS_STAGE_GROUPS usr : activeGroup.users) {
                if (usr.PILOT.equals(usr_reg.VS_USER_NAME)) {
                  user = usr;
                  usr.registration = usr_reg;
                  break;
                }
              }
            } catch (Exception ein) {
            }
            if (user == null) {
              user = new VS_STAGE_GROUPS();
              user.GROUP_NUM = activeGroup.GROUP_NUM;
              user.STAGE_ID = activeGroup.stage.ID;
              user.PILOT = usr_reg.VS_USER_NAME;
              user.REG_ID = usr_reg.ID;
              user.parent = activeGroup;
              user.NUM_IN_GROUP = VS_STAGE_GROUPS.getMaxNumInGroup(con, user.STAGE_ID, user.GROUP_NUM) + 1;
              user.isError = 2;
              user.VS_PRIMARY_TRANS = lap.transponderID;
              user.CHANNEL = "A1";
              VS_STAGE_GROUPS.dbControl.insert(con, user);
              activeGroup.users.add(user);
              if (activeGroup.stageTab != null) {
                try {
                  activeGroup.stageTab.pleasuUpdateTree = true;
                } catch (Exception ein) {
                  ein.printStackTrace();
                }
              }
              if (activeGroup.stageTab != null) {
                try {
                  activeGroup.stageTab.stageTableAdapter.loadData();
                  activeGroup.stageTab.pleasuUpdateTable = true;
                } catch (Exception ein) {
                  ein.printStackTrace();
                }
              }
            }
          } catch (Exception e) {
            _toLog(e);
          }
        }
        if (user != null) {
          VS_RACE_LAP lapNew = activeGroup.stage.addLapFromKeyPress(_mainForm, user, time);
          try {
            if (activeGroup.stageTab != null) {
              activeGroup.stageTab.pleasuUpdateTable = true;
            }
          } catch (Exception ein) {
          }
          if (USE_TRANS_FOR_GATE && TRANS_FOR_GATE != 0) {
            if (user.color == null) {
              user.color = VSColor.getColorForChannel(user.CHANNEL, activeGroup.stage.CHANNELS, activeGroup.stage.COLORS);
            }
            vsTimeConnector.addBlinkTransponder(TRANS_FOR_GATE, user.color.vscolor, TRANS_FOR_GATE_COLOR, TRANS_FOR_GATE_BLINK);
          }
        }
      }
    }
  }

  @Override
  public void sendVSText(String text) {
    if (VSTeamConsole.isOpened) {
      VSTeamConsole.addText(text);
    }
  }

  public interface LastTransponderListener {

    public void newTransponder(long transponder, VS_REGISTRATION user);
  }

  LastTransponderListener transponderListener = null;

  public void setTransponderListener(LastTransponderListener transponderListener) {
    this.transponderListener = transponderListener;
  }

}
