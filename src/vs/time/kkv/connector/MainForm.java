/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector;

import vs.time.kkv.connector.web.RaceHttpServer;
import vs.time.kkv.connector.connection.DroneConnector;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageNewForm;
import vs.time.kkv.connector.MainlPannels.*;
import KKV.DBControlSqlLite.DBModelTest;
import KKV.OBS.OBSConfig;
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
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortList;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationImportForm;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.IRegSite;
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
import vs.time.kkv.connector.Utils.OSDetector;
import vs.time.kkv.connector.Utils.TTS.SpeekUtil;
import static vs.time.kkv.connector.WLANSetting.singelton;
import vs.time.kkv.connector.connection.VSTimeConnection.VSTimeConnector;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.connector.connection.buttonsConnector.ButtonComPortConnector;
import vs.time.kkv.connector.connection.com.ConnectionCOMPort;
import vs.time.kkv.connector.connection.wan.ConnectionSocket;
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
public class MainForm extends javax.swing.JFrame implements VSTimeMachineReciver, DroneConnector.VSSendListener {
  
  public interface IMainFormListener{
    public void backgroundIsCnanged();
  }
  public IMainFormListener mainFormListener = null;

  public void setMainFormListener(IMainFormListener mainFormListener) {
    this.mainFormListener = mainFormListener;
  }
  
  

  public boolean SAY_SECONDS_FOR_LAP = false;

  public static final int STAGE_PRACTICA = 0;
  public static final int STAGE_QUALIFICATION = 1;
  public static final int STAGE_QUALIFICATION_RESULT = 2;
  public static final int STAGE_RACE = 3;
  public static final int STAGE_RACE_RESULT = 4;
  public static final int STAGE_RACE_REPORT = 5;

  public final static String DEVICE_VS_TIME_MACHINE = "VS Time Machine";
  public final static String DEVICE_ARDUINO_CONTROL = "Manual checker";

  public final static String[] DEVICE_LIST = new String[]{DEVICE_VS_TIME_MACHINE, DEVICE_ARDUINO_CONTROL};
  public final static String[] PILOT_TYPES = new String[]{"None-PRO", "PRO", "Freestyle"};
  public final static String[] PILOT_TYPES_NONE = new String[]{"None-PRO", "PRO", "Freestyle", "None"};
  public final static int PILOT_TYPE_NONE_INDEX = 3;
  public final static String[] STAGE_TYPES = new String[]{"Practica", "Qualification",
    "Qualification Result", "Race",
    "Race Result", "Race Report"};

  public static final int RACE_TYPE_EVERYONE_WITH_EACH_16 = 4;
  public static final int RACE_TYPE_OLYMPIC = 3;
  //public static final int RACE_TYPE_OLYMPIC_LOSES = 4;
  public static final int RACE_TYPE_WHOOP = 2;
  public static final int RACE_TYPE_DOUBLE = 1;
  public static final int RACE_TYPE_SINGLE = 0;

  public static final int SCORE_WHOOP_NONE = 0;
  public static final int SCORE_WHOOP_WON = 1;
  public static final int SCORE_WHOOP_LOST = 2;
  public final static String[] SCORES_WHOOP = new String[]{"", "Won", "Lost"};

  public boolean USE_TRANS_FOR_GATE = false;
  public int TRANS_FOR_GATE = 0;
  public int USE_START_WAVE = 0;
  public VSColor TRANS_FOR_GATE_COLOR = null;
  public boolean TRANS_FOR_GATE_BLINK = false;
  public OBSConfig obsConfig = null;

  public boolean USE_TRAFIC_LIGHT = false;
  public int TRANS_TRAFIC_LIGHT = 0;

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
  
   public void toLog(String msg, Exception e) {
    log.writeFile(msg, e);
  }

  public void toLog(String st) {
    log.writeFile(st);
  }

  //public List<DroneConnector> vsTimeConnector = null;
  public List<DroneConnector> droneConnectors = new CopyOnWriteArrayList<DroneConnector>();

  public static void toRealLog(String st) {
    real_log.writeFile(st, true);
  }

  public TempFileWrite log = new TempFileWrite("VSTimeMachine.log");
  public TempFileWrite error_log = new TempFileWrite("error.log");
  public TempFileWrite lap_log = new TempFileWrite("lap.log");
  public static TempFileWrite real_log = new TempFileWrite("real.log");
  public TempFileWrite race_log = new TempFileWrite("race.csv");
  public Connection con = null;
  public SpeekUtil speaker = null;
  public Beep beep = null;

  public VS_RACE activeRace = null;
  public VS_STAGE_GROUP activeGroup = null;
  public VS_STAGE_GROUP invateGroup = null;
  public VS_STAGE_GROUP lastInvateGroup = null;
  public VS_STAGE_GROUP lastCheckingGrpup = null;
  public VS_STAGE_GROUP lastRaceGroup = null;
  public VS_STAGE activeStage = null;
  public StageTab activeStageTab = null;
  public VS_STAGE_GROUP checkingGrpup = null;

  public RegistrationTab regForm = null;
  public long raceTime = 0;
  public long unRaceTime = Calendar.getInstance().getTimeInMillis();
  public int lastTranponderID = -1;
  public RaceHttpServer httpServer = null;
  public String LOCAL_HOST = "localhost";
  public int AUTO_UPDATER_TIME = 30; // 30 sekund

  public List<StageTab> stageTabs = new CopyOnWriteArrayList<StageTab>();

  public void setActiveRace(VS_RACE race, boolean pleaseRebuildTabs) {

    if (activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop race. Group" + activeGroup.GROUP_NUM, "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    setTitle("Race - " + race.RACE_NAME);
    // Open Tabs

    try {
      VS_RACE.dbControl.execSql(con, "UPDATE VS_RACE SET IS_ACTIVE=0 WHERE IS_ACTIVE<>0");
      race.IS_ACTIVE = 1;
      VS_RACE.dbControl.update(con, race);
    } catch (Exception e) {
    }

    if (activeRace != null && activeRace.RACE_ID == race.RACE_ID && pleaseRebuildTabs == false) {
      refreshTabbedCaptions();
      return;
    }
    activeRace = race;

    tabListenerEnabled = false;
    tabbedPanel.removeAll();
    try {
      tabbedPanel.repaint();
    } catch (Exception e) {
    }

    if (race != null) {
      try {
        try {
          this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          //new SplashForm(null);
        } catch (Exception e) {
        }
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
            StageTab p = new StageTab(this, stage);
            stageTabs.add(p);
            stage.tab = p;
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
      } finally {
        try {
          this.setCursor(Cursor.getDefaultCursor());
        } catch (Exception e) {
        }
      }
    } else {
      jmAddStageToRace.setVisible(false);
    }
    if (activeRace != null) {
      lap_log.writeFile("---==  Set Active Race  ==---  ;" + activeRace.RACE_NAME + " [" + activeRace.RACE_ID + "]");
    }
    SplashForm.closeLastInfoFrom();

  }

  boolean tabListenerEnabled = false;

  /**
   * Creates new form MainForm
   */
  public MainForm(String caption) {
    _mainForm = this;

    try {
      if (OSDetector.isWindows()) {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
      }
      if (OSDetector.isMac()) {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
      }
      if (OSDetector.isLinux()) {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        UIManager.put("TableUI", "javax.swing.plaf.basic.BasicTableUI");
      }
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
          /*if (vsTimeConnector != null) {
            vsTimeConnector.disconnect();
          }
          vsTimeConnector = null;*/

          for (DroneConnector dc : droneConnectors) {
            dc.disconnect();
          }
          droneConnectors.clear();

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
    obsConfig = new OBSConfig(con);

    jmAddStageToRace.setVisible(false);
    try {
      VS_RACE race = VS_RACE.dbControl.getItem(con, "IS_ACTIVE=1");
      if (race != null) {
        setActiveRace(race, true);
      } else {
        SplashForm.closeLastInfoFrom();
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
            if (p.stage != null && p.stage.IS_SELECTED != 1) {
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
    autoUploadToWebTimer.start();
    lap_log.writeFile("---==  Progam is loaded  ==--- ");
    if (activeRace != null) {
      lap_log.writeFile("---==  Active Race  ==---  ;" + activeRace.RACE_NAME + " [" + activeRace.RACE_ID + "]");
    }
    TimerForm.init(this).setVisible(true);

    String dev = VS_SETTING.getParam(con, "DEVICE", "");
    String port = VS_SETTING.getParam(con, "PORT", "");

    if (!dev.equals("")) {
      FORM_DEVICE.setSelectedItem(dev);
    }
    if (!port.equals("")) {
      ports.setSelectedItem(port);
    }
  }

  public AtomicBoolean treadIsRunning = new AtomicBoolean(false);

  public class uploadThrad extends Thread {

    @Override
    public void run() {
      treadIsRunning.set(true);
      try {
        if (activeRace != null) {
          if (activeRace.AUTO_WEB_UPDATE == 1) {
            IRegSite site = null;
            if (activeRace.WEB_RACE_ID != null && !activeRace.WEB_RACE_ID.equals("")) {
              site = RegistrationImportForm.getSite(activeRace.WEB_SYSTEM_SID);
              if (site == null) {
                return;
              }
              if (site.isSuportedToWebUpload()) {
              } else {
                return;
              }
            } else {
              return;
            }
            if (regForm != null && activeStageTab == null) {
              site.uploadToWebSystem(regForm, null, false, false);
              //System.out.println("Registration has been updated");
            }
            if (activeStageTab != null) {
              site.uploadToWebSystem(null, activeStageTab, false, false);
              //System.out.println("Stage '"+activeStageTab.stage.CAPTION+"' has been updated");
            }
          }
        }
      } catch (Exception ex) {
      }
      treadIsRunning.set(false);
    }
  }

  // Autouploader Timer to web-site - One time per minute
  Timer autoUploadToWebTimer = new Timer(AUTO_UPDATER_TIME * 2000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (treadIsRunning.get() == false) {
        new uploadThrad().start();
      }
    }
  });

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

    try {
      USE_TRANS_FOR_GATE = VS_SETTING.getParam(con, "USE_TRANS_FOR_GATE", 0) == 1 ? true : false;
      TRANS_FOR_GATE = VS_SETTING.getParam(con, "TRANS_FOR_GATE", 0);
      TRANS_FOR_GATE_BLINK = VS_SETTING.getParam(con, "TRANS_FOR_GATE_BLINK", 0) == 1 ? true : false;
      TRANS_FOR_GATE_COLOR = VSColor.getColor(VS_SETTING.getParam(con, "TRANS_FOR_GATE_COLOR", "RED"));
    } catch (Exception e) {
    }

    try {
      USE_TRAFIC_LIGHT = VS_SETTING.getParam(con, "USE_TRAFIC_LIGHT", 0) == 1 ? true : false;
      TRANS_TRAFIC_LIGHT = VS_SETTING.getParam(con, "TRANS_TRAFIC_LIGHT", 0);
    } catch (Exception e) {
    }

    USE_START_WAVE = VS_SETTING.getParam(con, "USE_START_WAVE", 0);
    BACKGROUND_FOR_TV = VS_SETTING.getParam(con, "TV_BACKGROUND", "chromokey.png") + "?t=" + Calendar.getInstance().getTimeInMillis();

    if (mainFormListener!=null) mainFormListener.backgroundIsCnanged();
    
    for (DroneConnector vsTimeConnector : droneConnectors) {
      if (vsTimeConnector != null && vsTimeConnector.connected) {
        try {
          if (USE_TRANS_FOR_GATE) {
            vsTimeConnector.setColor(TRANS_FOR_GATE, TRANS_FOR_GATE_COLOR.vscolor);
          } else {
            // public void addBlinkTransponder(int transID, int color, VSColor gateColor, boolean isBlink) 
            vsTimeConnector.addBlinkTransponder(-1, 0, null, false);
          }
        } catch (Exception e) {
        }
      }
    }
  }

  public void setColorForGate() {
    for (DroneConnector vsTimeConnector : droneConnectors) {
      if (USE_TRANS_FOR_GATE && vsTimeConnector != null && vsTimeConnector.connected) {
        try {
          vsTimeConnector.setColor(TRANS_FOR_GATE, TRANS_FOR_GATE_COLOR.vscolor);
        } catch (Exception e) {
        }
      }
    }
  }

  public String getLocaleString(String caption) {
    if (langFile != null) {
      try {
        String val = langFile.getParam(caption);
        if (val != null && !val.equalsIgnoreCase("")) {
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
    menuParameters.setEnabled(false);
    if (isConnected) {

      for (DroneConnector vsTimeConnector : droneConnectors) {
        if (vsTimeConnector.transport != null && vsTimeConnector.transport.supportVSTimeMachineExtendMenu()) {
          menuParameters.setEnabled(true);
        }
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
    jLabel4 = new javax.swing.JLabel();
    FORM_DEVICE = new javax.swing.JComboBox<>();
    jPanel2 = new javax.swing.JPanel();
    ping = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    tabbedPanel = new javax.swing.JTabbedPane();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu3 = new javax.swing.JMenu();
    mSystemOptions = new javax.swing.JMenuItem();
    jMenuIWebAdmin = new javax.swing.JMenuItem();
    mSystemMonitor = new javax.swing.JMenuItem();
    jMenuItem3 = new javax.swing.JMenuItem();
    jMenuItem4 = new javax.swing.JMenuItem();
    jMenuItem10 = new javax.swing.JMenuItem();
    jMenuItemTVMonitor = new javax.swing.JMenuItem();
    jMenuItem1 = new javax.swing.JMenuItem();
    jMenuItem7 = new javax.swing.JMenuItem();
    jMenuItem8 = new javax.swing.JMenuItem();
    jMenuItem5 = new javax.swing.JMenuItem();
    jMenuItem6 = new javax.swing.JMenuItem();
    jMenuItem9 = new javax.swing.JMenuItem();
    updater = new javax.swing.JMenuItem();
    mConsole = new javax.swing.JMenuItem(){
      @Override
      public KeyStroke getAccelerator()
      {
        return KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
      }
    };
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

    jLabel4.setText("Device");

    FORM_DEVICE.setModel(new javax.swing.DefaultComboBoxModel(DEVICE_LIST));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(5, 5, 5)
        .addComponent(jLabel4)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(FORM_DEVICE, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
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
      .addComponent(connectButton)
      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
        .addComponent(bRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1)
          .addComponent(jLabel4)
          .addComponent(FORM_DEVICE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    jMenuIWebAdmin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuIWebAdmin.setText("Web Admin");
    jMenuIWebAdmin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuIWebAdminActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuIWebAdmin);

    mSystemMonitor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    mSystemMonitor.setText("Web Monitor");
    mSystemMonitor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mSystemMonitorActionPerformed(evt);
      }
    });
    jMenu3.add(mSystemMonitor);

    jMenuItem3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem3.setText("TV OSD");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem3ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem3);

    jMenuItem4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem4.setText("TV Stage result");
    jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem4ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem4);

    jMenuItem10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem10.setText("TV Stage result (old)");
    jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem10ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem10);

    jMenuItemTVMonitor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItemTVMonitor.setText("TV Monitor");
    jMenuItemTVMonitor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItemTVMonitorActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItemTVMonitor);

    jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem1.setText("TV Last Group Result");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem1ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem1);

    jMenuItem7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem7.setText("TV Last Group Result Lite");
    jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem7ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem7);

    jMenuItem8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem8.setText("TV Start Area");
    jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem8ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem8);

    jMenuItem5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem5.setText("TV Liderboard 16 ");
    jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem5ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem5);

    jMenuItem6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem6.setText("TV Liderboard 16 lite");
    jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem6ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem6);

    jMenuItem9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jMenuItem9.setText("TV Pilot1");
    jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem9ActionPerformed(evt);
      }
    });
    jMenu3.add(jMenuItem9);

    updater.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    updater.setText("Check update");
    updater.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        updaterActionPerformed(evt);
      }
    });
    jMenu3.add(updater);

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
        .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
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
    for (DroneConnector vsTimeConnector : droneConnectors) {
      if (vsTimeConnector != null && vsTimeConnector.connected) {
        try {
          VSTMParams form = VSTMParams.getInstanse(this);
        } catch (Exception e) {
        }
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

    String port = ports.getSelectedItem().toString();

    VS_SETTING.setParam(con, "DEVICE", FORM_DEVICE.getSelectedItem().toString());
    VS_SETTING.setParam(con, "PORT", port);

    for (DroneConnector vsTimeConnector : droneConnectors) {
      if (vsTimeConnector != null) {
        vsTimeConnector.disconnect();
        vsTimeConnector = null;
      }
    }
    droneConnectors.clear();

    DroneConnector vsTimeConnector = null;
    if (!port.equalsIgnoreCase("")) {
      jLabel3.setText("connecting to port " + port);

      if (FORM_DEVICE.getSelectedItem().equals(DEVICE_ARDUINO_CONTROL)) {

        String channels = VS_SETTING.getParam(con, "CHANNELS", "R1;R2;R5;R7");
        /*String[] ch_mas = new String[]{activeRace.CHANNEL1,activeRace.CHANNEL2,activeRace.CHANNEL3,activeRace.CHANNEL4};
        for (String ch : ch_mas){
          channels+=ch+";";
        }*/
        vsTimeConnector = new VSTimeConnector(new ButtonComPortConnector(this, port, SerialPort.BAUDRATE_9600, channels));
      } else {
        String staticIP = null;
        if (VS_SETTING.getParam(con, "USE_STATIC_IP", "no").equalsIgnoreCase("yes")) {
          staticIP = VS_SETTING.getParam(con, "STATIC_IP", "192.168.1.255");
        };
        if (port.equalsIgnoreCase("WLAN")) {
          vsTimeConnector = new VSTimeConnector(
                  new ConnectionSocket(this,
                          VS_SETTING.getParam(con, "WAN_CONNECTION", ""),
                          staticIP,
                          WLANSetting.init(this).PORT_LISTING_INT,
                          WLANSetting.init(this).PORT_SENDING_INT));
        } else {
          vsTimeConnector = new VSTimeConnector(new ConnectionCOMPort(this, port));
        }
      }

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
      droneConnectors.add(vsTimeConnector);
    }
    
    // repaint table for buttons find...
    if (activeStageTab!=null){
      activeStageTab.refreshButton();
    }
  }//GEN-LAST:event_connectButtonActionPerformed

  public void disconnect() {
    menuDisconnectActionPerformed(null);
  }

  private void menuDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDisconnectActionPerformed
    for (DroneConnector vsTimeConnector : droneConnectors) {
      if (vsTimeConnector != null) {
        vsTimeConnector.disconnect();
      }
      vsTimeConnector = null;
    }
    droneConnectors.clear();
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
    RaceControlForm.init(this, -1, false).setVisible(true);
  }//GEN-LAST:event_miAddNewRaceActionPerformed

  private void jmAddStageToRaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAddStageToRaceActionPerformed
    // TODO add your handling code here:
    if (activeRace != null) {
      StageNewForm.init(this, null, null).setVisible(true);
    }
  }//GEN-LAST:event_jmAddStageToRaceActionPerformed

  private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged
    // TODO add your handling code here:       
    //JOptionPane.showMessageDialog(this, ""+tabbedPanel.getSelectedComponent(), "test", JOptionPane.ERROR_MESSAGE);
    invateGroup = null;
    activeStage = null;
    activeStageTab = null;
    Object tabObject = evt.getSource();
    if (tabObject instanceof JTabbedPane) {
      Object obj = tabbedPanel.getSelectedComponent();
      if (obj != null && obj instanceof StageTab) {
        StageTab tab = (StageTab) obj;
        if (tab.jchTV.isSelected()) {
          activeStage = tab.stage;
          activeStageTab = tab;
        }
      }
    }
  }//GEN-LAST:event_tabbedPanelStateChanged

  public void refreshTabbedCaptions() {
    for (int tabI = 1; tabI < tabbedPanel.getTabCount(); tabI++) {
      StageTab tab = stageTabs.get(tabI - 1);
      tabbedPanel.setTitleAt(tabI, tab.stage.CAPTION);
      /* Object tabObject = tabbedPanel.getTabComponentAt(tabI);           
      if (tabObject != null && tabObject instanceof StageTab) {
        StageTab tab = (StageTab) tabObject;
        tabbedPanel.setTitleAt(tabI, tab.stage.CAPTION);
      }*/
    }
  }

  private void mSystemOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSystemOptionsActionPerformed
    // TODO add your handling code here:
    SystemOptions.init(this).setVisible(true);
  }//GEN-LAST:event_mSystemOptionsActionPerformed

  private void mSystemMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSystemMonitorActionPerformed
    openUrl("index.htm");
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

  public void openUrl(String url) {
    // TODO add your handling code here:
    if (httpServer == null) {
      SystemOptions.runWebServer(this, true);
    }

    String uri = "http://" + LOCAL_HOST + ":" + VS_SETTING.getParam(this.con, "WEB_PORT", 80) + "/" + url;
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
  }

  private void jMenuItemTVMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTVMonitorActionPerformed
    openUrl("tv.htm");
  }//GEN-LAST:event_jMenuItemTVMonitorActionPerformed

  private void updaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updaterActionPerformed
    try {
      Process proc = Runtime.getRuntime().exec("java -jar Updater.jar");
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this, "Java is not found", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
  }//GEN-LAST:event_updaterActionPerformed

  private void jMenuIWebAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIWebAdminActionPerformed
    // TODO add your handling code here:
    openUrl("index.htm?mode=admin");
  }//GEN-LAST:event_jMenuIWebAdminActionPerformed

  private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    // TODO add your handling code here:
    openUrl("group-result.htm");
  }//GEN-LAST:event_jMenuItem1ActionPerformed

  private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    // TODO add your handling code here:
    //openUrl("osd.htm");
    openUrl("osd.jsp");
  }//GEN-LAST:event_jMenuItem3ActionPerformed

  private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
    // TODO add your handling code here:
    //openUrl("stage.htm");
    openUrl("stage.jsp");
  }//GEN-LAST:event_jMenuItem4ActionPerformed

  private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    // TODO add your handling code here:
    openUrl("lb16.htm");
  }//GEN-LAST:event_jMenuItem5ActionPerformed

  private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
    // TODO add your handling code here:
    openUrl("lb16l.htm");
  }//GEN-LAST:event_jMenuItem6ActionPerformed

  private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
    // TODO add your handling code here:
    openUrl("group-result-lite.htm");
  }//GEN-LAST:event_jMenuItem7ActionPerformed

  private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    // TODO add your handling code here:
    openUrl("start.htm");
  }//GEN-LAST:event_jMenuItem8ActionPerformed

  private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
    // TODO add your handling code here:
    openUrl("tv.pilot1.htm");
  }//GEN-LAST:event_jMenuItem9ActionPerformed

  private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
    // TODO add your handling code here:
     openUrl("stage.htm");
  }//GEN-LAST:event_jMenuItem10ActionPerformed

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
    new SplashForm(null);
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
        //new SplashForm(null);
        MainForm mainForm = new MainForm("VS Time Connector");
        setWindowsIcon(getClass().getResource("/images/vs-logo.png"));
        mainForm.setIconImage(getWindowsIcon().getImage());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainForm.setSize((int) (dim.width * 0.9), (int) (dim.height * 0.8));
        //mainForm.setSize( dim.width, dim.height);
        mainForm.setLocation(dim.width / 2 - mainForm.getSize().width / 2, dim.height / 2 - mainForm.getSize().height / 2);
        mainForm.setVisible(true);
        //SplashForm.closeLastInfoFrom();
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
  private javax.swing.JComboBox<String> FORM_DEVICE;
  private javax.swing.JButton bRefresh;
  private javax.swing.JButton connectButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenu jMenu3;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuIWebAdmin;
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuItem jMenuItem10;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JMenuItem jMenuItem3;
  private javax.swing.JMenuItem jMenuItem4;
  private javax.swing.JMenuItem jMenuItem5;
  private javax.swing.JMenuItem jMenuItem6;
  private javax.swing.JMenuItem jMenuItem7;
  private javax.swing.JMenuItem jMenuItem8;
  private javax.swing.JMenuItem jMenuItem9;
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
  private javax.swing.JMenuItem updater;
  // End of variables declaration//GEN-END:variables

  public JTabbedPane getTabbedPanels() {
    return tabbedPanel;
  }

  public void setFormOnCenter(Window form) {
    try {
      Point p = this.getLocationOnScreen();
      form.setLocation(p.x + this.getWidth() / 2 - form.getSize().width / 2, p.y + this.getHeight() / 2 - form.getSize().height / 2);

      form.setIconImage(getWindowsIcon().getImage());
    } catch (Exception e) {
    }
  }

  @Override
  public void receiveDataForLog(String data) {
    boolean isPingCommand = false;
    if (data.indexOf("ping") == 0) {
      isPingCommand = true;
    }
    if (VSTeamConsole.isOpened) {
      if (isPingCommand && !VSTeamConsole.showPing) {
        // ignore ping
      } else {
        VSTeamConsole.addText(data, true, false);
      }
    }
  }

  @Override
  public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap) {
    boolean isPingCommand = false;

    if (commands[0].equalsIgnoreCase("ping")) {
      isPingCommand = true;
    }

    // gate:<GATE ID>, <COLOR>, <TRANSPONDER ID>, <TIME>,<CRC>\r\n
    if (commands[0].equalsIgnoreCase("gate")) {
      String gateID = params[0];
      VS_REGISTRATION usr_reg = null;
      if (activeRace != null && !lap.isPilotNumber) {
        try {
          int transponderID = Integer.parseInt(params[1]);
          usr_reg = VS_REGISTRATION.dbControl.getItem(con, "VS_RACE_ID=? and (VS_TRANSPONDER=? OR VS_TRANS2=? OR VS_TRANS3=?)",
                  activeRace.RACE_ID, transponderID, transponderID, transponderID);          
          String info = "";
          if (activeStage!=null){
            info = getLocaleString("Stage") + " : " + activeStage;
          }
          if (activeGroup!=null){
            info += " - " + getLocaleString("Group") + activeGroup.GROUP_NUM;
          
          }      
          obsConfig.changeSceneForGate(info, gateID, usr_reg);
        } catch (Exception ein) {
        }
      }
    }

    for (DroneConnector vsTimeConnector : droneConnectors) {
      jLabel3.setText(data + "   " + (vsTimeConnector != null ? vsTimeConnector.last_error : ""));
      break;
    }
    if (!isPingCommand) {
      System.out.println(data);
      log.writeFile(data, true);
      if (lap == null) {
        lap_log.writeFile("RCV;" + data);
      }
    }
    if (lap != null) {

      if (lap.isPilotNumber) {
        lap.transponderID = lap.pilotNumber;
        lap.baseStationID = 0;
        lap.time = Calendar.getInstance().getTimeInMillis();
        if (activeGroup != null) {
          lap.transponderID = 0;

          if (lap.isPilotChannel) {
            for (VS_STAGE_GROUPS usr : activeGroup.users) {
              if (usr.CHANNEL.equalsIgnoreCase(lap.pilotChannel)) {
                lap.transponderID = usr.VS_PRIMARY_TRANS;
                break;
              }
            }
          } else {
            if (activeGroup.users.size() >= lap.pilotNumber && lap.pilotNumber > 0) {
              lap.transponderID = activeGroup.users.get(lap.pilotNumber - 1).VS_PRIMARY_TRANS;
            }
          }
        }
      }

      if (lap.transponderID == TRANS_FOR_GATE) {
        if (regForm != null && regForm.unRaceLapSound.isSelected()) {
          speaker.speak(speaker.getSpeachMessages().gate());
        }
        return;
      }

      this.lastTranponderID = lap.transponderID;
      long timeS = Calendar.getInstance().getTimeInMillis();
      long time = lap.time;
      if (lap.isPilotNumber) {
        lap_log.writeFile("LAP;" + new JDEDate(time).getDateAsYYYYMMDD_andTime("-", ":") + ";pilot" + lap.pilotNumber);
      } else {
        lap_log.writeFile("LAP;" + new JDEDate(time).getDateAsYYYYMMDD_andTime("-", ":") + ";" + lap.transponderID + ";" + lap.baseStationID + ";" + lap.numberOfPacket + ";" + lap.transpnderCounter);
      }
      if (Math.abs(time - timeS) > 1000 * 60 * 60) {
        System.out.println("Big gap between Time : " + Math.abs(time - timeS) + " trans time:" + time + " system time:" + timeS);
        time = timeS;//lap.time;
      }
      this.lastTranponderID = lap.transponderID;
      VS_STAGE_GROUP activeGroup = this.activeGroup;
      if (VSTeamConsole.isOpened) {
        VSTeamConsole.setLastTransID("" + lastTranponderID);
      }

      VS_REGISTRATION usr_reg = null;
      if (activeRace != null && !lap.isPilotNumber) {
        try {
          usr_reg = VS_REGISTRATION.dbControl.getItem(con, "VS_RACE_ID=? and (VS_TRANSPONDER=? OR VS_TRANS2=? OR VS_TRANS3=?)",
                  activeRace.RACE_ID, lap.transponderID, lap.transponderID, lap.transponderID);
        } catch (Exception ein) {
        }
      }
      if (transponderListener != null) {
        transponderListener.newTransponder(lap.transponderID, usr_reg);
      }

      if (activeGroup == null && !lap.isPilotNumber) {
        if (usr_reg != null) {
          if (regForm != null && regForm.unRaceLapSound.isSelected()) {
            speaker.speak(speaker.getSpeachMessages().msg(usr_reg.VS_USER_NAME));
          }
        } else {
          if (regForm != null && regForm.unRaceLapSound.isSelected() && lap.transponderID != 0) {
            speaker.speak(speaker.getSpeachMessages().pilot("" + lap.transponderID));
          }
        }
        if (USE_TRANS_FOR_GATE && TRANS_FOR_GATE != 0) {

          for (DroneConnector vsTimeConnector : droneConnectors) {
            vsTimeConnector.addBlinkTransponder(TRANS_FOR_GATE, VSColor.AQUA.vscolor, TRANS_FOR_GATE_COLOR, TRANS_FOR_GATE_BLINK);
          }
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

        if (lap.isPilotNumber) {
          if (lap.isPilotChannel) {
            for (VS_STAGE_GROUPS usr : activeGroup.users) {
              if (usr.CHANNEL.equalsIgnoreCase(lap.pilotChannel)) {
                user = usr;
                break;
              }
            }
          } else {
            if (activeGroup.users.size() >= lap.pilotNumber && lap.pilotNumber > 0) {
              user = activeGroup.users.get(lap.pilotNumber - 1);
            }
          }
        } else {
        }

        for (VS_STAGE_GROUPS usr : activeGroup.users) {
          if (usr.VS_PRIMARY_TRANS == lap.transponderID) {
            user = usr;
            break;
          }
        }

        // Find User by Channel Trans     
        if (!activeGroup.stage.TRANSS.equals("") && user == null) {
          try {
            Map<String, List<String>> trans_by_channels = activeGroup.stage.getTanspondersForChannels();
            for (String channel : trans_by_channels.keySet()) {
              for (String trans_st : trans_by_channels.get(channel)) {
                if (("" + lap.transponderID).equalsIgnoreCase(trans_st)) {
                  for (VS_STAGE_GROUPS all_user : activeGroup.users) {
                    if (all_user.CHANNEL.equalsIgnoreCase(channel)) {
                      user = all_user;
                      user.VS_PRIMARY_TRANS = lap.transponderID;
                      break;
                    }
                  }
                }
                if (user != null) {
                  break;
                }
              }
              if (user != null) {
                break;
              }
            }
          } catch (Exception e) {
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
            for (DroneConnector vsTimeConnector : droneConnectors) {
              vsTimeConnector.addBlinkTransponder(TRANS_FOR_GATE, user.color.vscolor, TRANS_FOR_GATE_COLOR, TRANS_FOR_GATE_BLINK);
            }
          }
        }
      }
    }
  }

  public DroneConnector getMainDroneConnector() {
    for (DroneConnector vsTimeConnector : droneConnectors) {
      return vsTimeConnector;
    }
    return null;
  }

  @Override
  public void sendVSText(String text) {
    if (VSTeamConsole.isOpened) {
      VSTeamConsole.addText(text, false, false);
    }
    lap_log.writeFile("SND;" + text);
  }

  public interface LastTransponderListener {

    public void newTransponder(long transponder, VS_REGISTRATION user);
  }

  LastTransponderListener transponderListener = null;

  public void setTransponderListener(LastTransponderListener transponderListener) {
    this.transponderListener = transponderListener;
  }

}
