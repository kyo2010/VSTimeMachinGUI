/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;


import KKV.Export2excel.OutReport;
import KKV.Export2excel.XLSMaker;
import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import KKV.Utils.UserException;
import com.lowagie.text.pdf.BaseFont;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.InfoForm;
import vs.time.kkv.connector.Utils.KKVTreeTable.JTreeTable;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeCellRender;
import vs.time.kkv.connector.Utils.TTS.SpeekUtil;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import ru.nkv.var.StringVar;
import ru.nkv.var.Var;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import static vs.time.kkv.connector.MainForm._toLog;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationImportForm;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.IRegSite;
import vs.time.kkv.connector.MainlPannels.TimerForm;
import vs.time.kkv.connector.SystemOptions;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.connector.Utils.Beep;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.connector.Utils.MultiLineHeaderRenderer;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_SETTING;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vs.time.kkv.connector.MainlPannels.stage.GroupCreater.GroupFactory;
import vs.time.kkv.connector.Utils.TableToXLS;



/**
 *
 * @author kyo
 */
public class StageTab extends javax.swing.JPanel {

  public MainForm mainForm;
  public VS_STAGE stage = null;
  StageTreeModel treeModel = null;
  public JPopupMenu popupMenuJTree = null;
  public StageTableAdapter stageTableAdapter = null;
  public VS_STAGE_GROUP checkingGrpup = null;
  public boolean FIRST_RACER_IS_FINISHED = false;

  public boolean pleasuUpdateTree = false;
  public boolean pleasuUpdateTable = false;
  public boolean isOneTable = false;

  public void fireStructChange() {
    //System.out.println("fireStructChange");
    //jTable.setModel(stageTableAdapter);
    jTable.tableChanged(null);
    MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();
    for (int i = 0; i < stageTableAdapter.getColumnCount(); i++) {
      jTable.getColumnModel().getColumn(i).setMinWidth(stageTableAdapter.getMinWidth(i));
      jTable.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
    }
  }
  
  public void refreshTable(){
    try{
      jTable.setRowHeight(30);
      //jTable.notifyAll();
      //jTable.updateUI();
    }catch(Exception e){}      
  }

  boolean raceTimerIsOver = false;
  boolean iveSaid10seckudForRaceOver = false;
  Timer raceTimer = new Timer(500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (mainForm.vsTimeConnector != null) {
        mainForm.vsTimeConnector.checkConnection();
      }
      long current_time = Calendar.getInstance().getTimeInMillis();
      long raceTime = current_time - mainForm.raceTime;
      timerCaption.setText(getTimeIntervelForTimer(raceTime));
      try {
        if (pleasuUpdateTree) {
          pleasuUpdateTree = false;
          //System.out.println("repaint tree");
          if (jTree != null) {
            try{
              jTree.notifyAll();
              jTree.updateUI();
            }catch(Exception ein){}
          }
        }
        if (pleasuUpdateTable) {
          //System.out.println("repaint table");
          pleasuUpdateTable = false;
          if (jTable != null) {
            //stageTableAdapter.             
            //jTable.addNotify();
            refreshTable();
          }
        }
      } catch (Exception ex) {
      }
      long max_race_time = 0;
      if (mainForm.activeRace != null) {
        max_race_time = mainForm.activeRace.MAX_RACE_TIME;
      }
      if (max_race_time != 0 && max_race_time != -1) {
        max_race_time = max_race_time * 1000;
        long diff = (raceTime + 10000 - max_race_time) / 1000;
        long diff_ms = max_race_time - raceTime;
        if (diff == 0) {
          if (!iveSaid10seckudForRaceOver) {
            mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().raceIsOverIn10sec());
            iveSaid10seckudForRaceOver = true;
          }
        }
        if (diff_ms < 0 && !raceTimerIsOver) {
          stopRace(true);
        }
      }
    }
  });

  public static String getFlightSpeed(VS_RACE race, long bestLapTime) {
    String res = "";
    try {
      int m = race.LAP_DISTANCE;
      if (bestLapTime > 0 && m > 0 && bestLapTime != VS_STAGE_GROUPS.MAX_TIME) {
        double ss = ((double) m) / bestLapTime;
        double speed = ss * 3600;
        res = "" + Math.round(speed);
      }
    } catch (Exception e) {
    }
    return res;
  }

  public int checkerCycle = 0;
  boolean pleaseMakeYelowPilot = false;
  public Timer checkerTimer = new Timer(500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (mainForm.vsTimeConnector != null) {
        mainForm.vsTimeConnector.checkConnection();
      }
      Timer timer = (Timer) e.getSource();
      InfoForm.init(mainForm, "Check", 100).setVisible(true);

      try {
        mainForm.vsTimeConnector.rfidLock(mainForm.RFIDLockPassword);
        try {
          //Thread.sleep(500);
          Thread.currentThread().sleep(50);
        } catch (Exception ein) {
        }
      } catch (Exception ein) {
      }
      if (checkingGrpup != null && checkingGrpup.users != null) {
        for (VS_STAGE_GROUPS user : checkingGrpup.users) {
          user.RECEIVED_LAPS = false;
        }
      }

      bStopChecking.setVisible(true);

      if (checkerCycle * timer.getInitialDelay() < 1000) {
        /*try {
          mainForm.vsTimeConnector.setColor(0, 0);
        } catch (Exception ein) {
        }
        try {
          //Thread.sleep(150);
          Thread.currentThread().sleep(150);
        } catch (Exception ein) {
        }
*/        try {
          //mainForm.vsTimeConnector.setPowerMax();
          mainForm.setColorForGate();
        } catch (Exception ein) {
        }

        if (pleaseMakeYelowPilot) {
          for (VS_STAGE_GROUPS user : checkingGrpup.users) {
            user.RECEIVED_LAPS = false;
          }
          pleaseMakeYelowPilot = false;
          //pleasuUpdateTable = true;
          refreshTable();
        }

        checkerCycle++;
        return;
      }

      try {
        if (checkingGrpup != null) {
          int pilot_num = checkerCycle % checkingGrpup.users.size();
          if (checkingGrpup.users.get(pilot_num).CHECK_FOR_RACE == 2) {
            Collection<Integer> userTrans = checkingGrpup.users.get(pilot_num).getUserTransponders(mainForm.con, stage.RACE_ID, stage);
            VSColor vs_color = VSColor.getColorForChannel(checkingGrpup.users.get(pilot_num).CHANNEL, stage.CHANNELS, stage.COLORS);
            VS_RACE race = VS_RACE.dbControl.getItem(mainForm.con, "RACE_ID=?", stage.RACE_ID);
            for (Integer transID : userTrans) {
              int vs_color_to_send = vs_color.getVSColor();
              if (race.HYBRID_MODE != 1) {
                vs_color_to_send |= (1 << 7);
              }
              mainForm.vsTimeConnector.seachTransponder(transID, vs_color_to_send);
              //   mainForm.vsTimeConnector.setColor(transID, vs_color.getVSColor());
              try {
                //Thread.sleep(150);
                Thread.currentThread().sleep(150);
              } catch (Exception ein) {
              }
            }
            checkingGrpup.users.get(pilot_num).color = vs_color;
            try {
              //System.out.println("time1: " + Calendar.getInstance().getTimeInMillis());
              //Thread.currentThread().wait(400);              
              //System.out.println("time2: "+Calendar.getInstance().getTimeInMillis());    
            } catch (Exception ein) {
            }
            //mainForm.vsTimeConnector.seachTransponder(checkingGrpup.users.get(pilot_num).TRANSPONDER,vs_color.getVSColor());            

          }

          //VSColor vs_color = VSColor.getColorForChannel(checkingGrpup.users.get(pilot_num).CHANNEL);
          //if (vs_color!=null){
          //  mainForm.vsTimeConnector.setColor(checkingGrpup.users.get(pilot_num).TRANSPONDER, vs_color.getVSColor());
          //}                                     
        }
        for (VS_STAGE_GROUPS user : checkingGrpup.users) {
          Collection<Integer> userTrans = user.getUserTransponders(mainForm.con, stage.RACE_ID,stage);
          for (Integer transID : userTrans) {
            if (user.getRegistration(mainForm.con, stage.RACE_ID) != null) {
              if (mainForm.vsTimeConnector.isTransponderSeached(transID) && user.CHECK_FOR_RACE != 1) {
                user.CHECK_FOR_RACE = 1;
                try {
                  //System.out.println("VS_STAGE_GROUPS - check timer");
                  VS_STAGE_GROUPS.dbControl.update(mainForm.con, user);
                } catch (Exception ein) {
                }
                user.VS_PRIMARY_TRANS = transID;
                //pleasuUpdateTable = true;
                refreshTable();
                mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().pilotIsChecked(user.PILOT));
              }
            }
          }
        }
      } catch (Exception ex) {
      }
      checkerCycle++;
      boolean all_ok = true;
      for (VS_STAGE_GROUPS user : checkingGrpup.users) {
        if (user.CHECK_FOR_RACE != 1) {
          all_ok = false;
        }
      }
      // Ceck 2.5 secs minimum - fol lap 
      if (checkerCycle * timer.getInitialDelay() < 2500) {
        all_ok = false;
      }
      if (checkerCycle * timer.getInitialDelay() > 1000 * 300 || all_ok) {
        timer.stop();
        for (VS_STAGE_GROUPS user : checkingGrpup.users) {
          if (user.CHECK_FOR_RACE == 2) {
            user.CHECK_FOR_RACE = 0;
          }
        };
        //pleasuUpdateTable = true;
        refreshTable();
        checkingGrpup = null;
        InfoForm.init(mainForm, "").setVisible(false);
        stopSearch();
      }
    }
  });

  public static String getTimeIntervel(long time) {
    long min = time / 1000 / 60;
    long sec = time / 1000 - min * 60;
    long milisec = time - (sec + min * 60) * 1000;
    //milisec = Math.round(milisec / 10);
    return Tools.padl("" + min, 2, "0") + ":" + Tools.padl("" + sec, 2, "0") + ":" + Tools.padl("" + milisec, 3, "0");
  }

  public static String getTimeIntervelForTimer(long time) {
    long min = time / 1000 / 60;
    long sec = time / 1000 - min * 60;
    long milisec = time - (sec + min * 60) * 1000;
    milisec = Math.round(milisec / 10);
    return Tools.padl("" + min, 2, "0") + ":" + Tools.padl("" + sec, 2, "0");
  }

  Color BUTTON_BACKGROUND;
  Border BUTTON_DEFAULT_BORDER;
  Border BUTTON_RED_BORDER;

  /**
   * Creates new form PracticaTableTab
   */
  public StageTab(MainForm main, VS_STAGE _stage) {
    this.stage = _stage;
    initComponents();
    this.mainForm = main;
    BUTTON_BACKGROUND = autoStrartRaceButton.getBackground();
    BUTTON_DEFAULT_BORDER = autoStrartRaceButton.getBorder();
    BUTTON_RED_BORDER = new LineBorder(Color.RED,3);
    
    
    bStopChecking.setVisible(false);
    //topPanel.setVisible(false);              

    timerCaption.setVisible(false);

    if (stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT
            || stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT
            || stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
      isOneTable = true;
      butCopyGropusToClipboard.setVisible(false);
    }

    // isOneTable = true; for Debug to hide tree
    refreshData(false);
    refreshDataActionPerformedWitoutRecalulation();

    //treeTable = new JTreeTable(new StageTableAdapter2(this));
    //jScrollPane1.add(treeTable);
    //jScrollPane1.setViewportView(treeTable);
    //treeTable.setDragEnabled(true);    
    jTree.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (stage.IS_LOCK == 1) {
          return;
        }
        if (mainForm.activeGroup != null) {
          JOptionPane.showMessageDialog(StageTab.this, "Please stop the Active Race.");
          return;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
          int row = jTree.getClosestRowForLocation(e.getX(), e.getY());
          jTree.setSelectionRow(row);
          Object obj = null;
          if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
            obj = jTree.getSelectionPath().getLastPathComponent();
          }
          if (obj != null && (obj instanceof VS_STAGE_GROUPS || obj instanceof VS_STAGE_GROUP)) {
            popupMenuJTree.show(e.getComponent(), e.getX(), e.getY());
          }
        }
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2) {
          editUserTree();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });
    popupMenuJTree = new JPopupMenu();
    JMenuItem miExport = new JMenuItem("Export");
    popupMenuJTree.add(miExport);

    JMenuItem miAdd = new JMenuItem("Add");
    popupMenuJTree.add(miAdd);
    JMenuItem miEdit = new JMenuItem("Edit");
    popupMenuJTree.add(miEdit);
    JMenuItem miDelete = new JMenuItem("Delete");
    popupMenuJTree.add(miDelete);
    miExport.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        treeToXLS();
      }
    });
    miEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editUserTree();
      }
    });
    miAdd.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addUserToTreeTree();
      }
    });
    miDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Object obj = null;
        if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
          obj = jTree.getSelectionPath().getLastPathComponent();
        }
        if (obj != null && obj instanceof VS_STAGE_GROUPS) {
          VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
          int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete '" + user.PILOT + "' pilot?", "Delete Pilot from group", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              VS_STAGE_GROUPS.dbControl.delete(mainForm.con, user);
              refreshData(true);
            } catch (UserException ex) {
              mainForm.toLog(ex);
            }
          }
        }
        if (obj != null && obj instanceof VS_STAGE_GROUP) {
          VS_STAGE_GROUP group = (VS_STAGE_GROUP) obj;
          int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete Group" + group.GROUP_NUM + " ?", "Delete group", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              for (VS_STAGE_GROUPS usr : group.users) {
                VS_STAGE_GROUPS.dbControl.delete(mainForm.con, usr);
              }
              refreshData(true);
            } catch (UserException ex) {
              mainForm.toLog(ex);
            }
          }
        }
      }
    });

    jTable.addMouseListener(new MouseAdapter() {
      boolean infoWindowRunning = false;

      @Override
      public synchronized void mouseClicked(MouseEvent e) {
        if (isOneTable) {
          return;
        }
        if (stage.IS_LOCK == 1) {
          return;
        }
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
          JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());
          if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
          }
          StageTableData td = StageTab.this.stageTableAdapter.getTableData(row);
          if (td == null || !td.isGrpup) {
            return;
          }
          //INAVITATION          
          if (column == 3 && !infoWindowRunning && td != null && td.isGrpup) {  // Press invate
            invateAction(td.group.GROUP_NUM, true);
          }

          if (column == 2 && !infoWindowRunning && td != null && td.isGrpup) { // Seach and Check and Ligting
            if (checkerTimer.isRunning()) {
              //checkerTimer.stop();
              stopSearch();
              refreshTable();
              return;
            }
            startSearchAction(td.group.GROUP_NUM, true);
          }
          if (column == 1 && !infoWindowRunning && td != null && td.isGrpup) { // Start Race
            if (mainForm.activeGroup != null && mainForm.activeGroup == td.group) {
              stopRace(false);
              refreshTable();
              //timerCaption.setVisible(false);              
            } else {
              startRaceAction(td.group.GROUP_NUM, true);
            }
          }
        }
      }

      public void mouseReleased(MouseEvent e) {
      }

    });

    if (isOneTable) {
      jSplitPane1.setVisible(false);
      jTree.setVisible(false);
      jScrollPane2.setVisible(false);
      butGroupExport.setVisible(false);
    }

    if (mainForm.activeRace.WEB_RACE_ID != null && !mainForm.activeRace.WEB_RACE_ID.equals("")) {
      butCopyToWeb.setVisible(true);
    } else {
      butCopyToWeb.setVisible(false);
    }

  }

  /*public void checkGroupConstrain() {      
  }*/
  public void stopRace(boolean byTimer) {
    
    if (byTimer && mainForm.activeRace.ALLOW_TO_FINISH_LAP==1){
      raceTimerIsOver = true;
      if (mainForm.activeGroup!=null){
        for (VS_STAGE_GROUPS user : mainForm.activeGroup.users) {
          user.MAIN_TIME_IS_OVER = 1;
        }        
      }
      mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().raceTimeIsOver());
      return;
    }
    
    // To show Table result for TV
    mainForm.lap_log.writeFile("---==  Stop Race  ==---;" + stage.CAPTION + " [" + stage.ID + "];Group;" + mainForm.activeGroup.GROUP_NUM);
    mainForm.invateGroup = null;
    iveSaid10seckudForRaceOver = false;
    mainForm.unRaceTime = Calendar.getInstance().getTimeInMillis();            
    raceTimer.stop();        
    
    for (VS_STAGE_GROUPS user : mainForm.activeGroup.users) {
      user.IS_FINISHED = 1;
      user.recalculateLapTimes(mainForm.con, stage, true);
    }
    long group_num = mainForm.activeGroup.GROUP_NUM;
    mainForm.activeGroup.recalculateScores(mainForm);
    mainForm.activeGroup = null;
    jTree.updateUI();
    refreshTable();
    //mainForm.speaker.speak("The Stage finshed");
    mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().groupFinished(group_num));
  }

  // expandAllNodes(tree, 0, tree.getRowCount());
  public void expandAllJTree() {
    expandAllNodes(jTree, 0, jTree.getRowCount());
  }

  public void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
    for (int i = startingIndex; i < rowCount; ++i) {
      tree.expandRow(i);
    }
    if (tree.getRowCount() != rowCount) {
      expandAllNodes(tree, rowCount, tree.getRowCount());
    }
  }

  public void editUserTree() {
    Object obj = null;
    if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
      obj = jTree.getSelectionPath().getLastPathComponent();
    }
    if (obj != null && obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) jTree.getSelectionPath().getParentPath().getLastPathComponent();
      StageTabTreeEditForm.init(mainForm, this, group, user).setVisible(true);
    }
  }

  public void addUserToTreeTree() {
    Object obj = null;
    if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
      obj = jTree.getSelectionPath().getLastPathComponent();
    }
    if (obj != null && obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) jTree.getSelectionPath().getParentPath().getLastPathComponent();
      StageTabTreeEditForm.init(mainForm, this, group, null).setVisible(true);
    }
    if (obj != null && obj instanceof VS_STAGE_GROUP) {
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) obj;
      StageTabTreeEditForm.init(mainForm, this, group, null).setVisible(true);
    }
  }

  public void refreshTableData() {
    if (stageTableAdapter != null) {
      stageTableAdapter.loadData();
      jTable.updateUI();
    }
  }

  public void refreshData(boolean refreshInterface) {
    if (stage != null && stage.IS_GROUP_CREATED == 0) {
      createGroups();
    }
    stage.loadGroups(mainForm.con, stage.RACE_ID, stage.ID);
    refreshTableData();
    //jTable.addNotify();
    //jTree.addNotify();
    //jTree.updateUI();
    if (refreshInterface && isOneTable == false) {
      jTree.updateUI();
      //treeModel = new StageTreeModel(this);
      //jTree.setModel(treeModel);
    }
    if (isOneTable == false) {
      expandAllJTree();
    }
  }

  /*public BaseFont getRussianFont() throws DocumentException, IOException {
    //return BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);
    String path = (new File("")).getAbsolutePath();
    String encode = VS_SETTING.getParam(mainForm.con, "PDF-encode", "CP1251");
    BaseFont bfComic = BaseFont.createFont(path + File.separator+"font.ttf", encode, BaseFont.EMBEDDED);
    return bfComic;
  }*/
  
  public void treeToXLS() {
    TableToXLS.groups2xls(stage);
  }

  public void treeToXLS_old() {
    try {
      JDEDate jd = new JDEDate();
      OutReport out = new OutReport(jd.getDDMMYYYY("-"));
      //out.setShowExcel(true);
      out.setReportName(jd.getDDMMYYYY("-") + "_groups");
      int sheet = out.addStream();

      out.setReportName(sheet, stage.CAPTION);
      out.setViewFileName(sheet, "view.xml");
      IVar pool = new VarPool();
      pool.addChild(new StringVar("VisibleSheet", ""));
      pool.addChild(new StringVar("ConditionalFormatting", ""));
      pool.addChild(new StringVar("ExcelCellNames", ""));
      pool.addChild(new StringVar("ColumsInfo", ""));
      pool.addChild(new StringVar("FIX_ROWS", "4"));

      out.applayPoolToViewFile(sheet, pool);
      out.addToDataFile(sheet, "info:$$Race : " + mainForm.activeRace.RACE_NAME + " as of " + jd.getDDMMYYYY("-") + "$$");
      out.addToDataFile(sheet, "info:$$Stage : " + stage.CAPTION + "$$");
      out.addToDataFile(sheet, "info:");

      String head = "head:Group:Pilot:Channel:";
      out.addToDataFile(sheet, head);

      for (Integer groupNum : stage.groups.keySet()) {
        VS_STAGE_GROUP group = stage.groups.get(groupNum);
        for (VS_STAGE_GROUPS usr : group.users) {
          String line = "data:";
          line += "Group " + group.GROUP_NUM + ":$$" + usr.PILOT + "$$:" + usr.CHANNEL + ":";
          out.addToDataFile(sheet, line);
        }
      }

      out.closeDataStreams();
      String xlsFile = XLSMaker.makeXLS(out);
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + xlsFile + "\"");   //open the file chart.pdf 
    } catch (Exception e) {
      e.printStackTrace();
      mainForm._toLog(e);
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

    topPanel = new javax.swing.JPanel();
    butRemoveSatge = new javax.swing.JButton();
    butConfig = new javax.swing.JButton();
    timerCaption = new javax.swing.JLabel();
    pdfButton = new javax.swing.JButton();
    refreshData = new javax.swing.JButton();
    bNewStage = new javax.swing.JButton();
    butGroupExport = new javax.swing.JButton();
    jchTV = new javax.swing.JCheckBox();
    javax.swing.JButton bRestartWebServer = new javax.swing.JButton();
    butCopyToClipboard = new javax.swing.JButton();
    butCopyGropusToClipboard = new javax.swing.JButton();
    butCopyToWeb = new javax.swing.JButton();
    bStopChecking = new javax.swing.JButton();
    autoStrartRaceButton = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jSplitPane2 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTree = new ExpandedJTree();

    addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        formKeyPressed(evt);
      }
    });

    topPanel.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        topPanelKeyPressed(evt);
      }
    });

    butRemoveSatge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/remove.png"))); // NOI18N
    butRemoveSatge.setText("Delete Stage");
    butRemoveSatge.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butRemoveSatgeActionPerformed(evt);
      }
    });

    butConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/options.png"))); // NOI18N
    butConfig.setToolTipText("Config");
    butConfig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butConfigActionPerformed(evt);
      }
    });

    timerCaption.setBackground(new java.awt.Color(0, 153, 51));
    timerCaption.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    timerCaption.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    timerCaption.setText("00:00:00");
    timerCaption.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 5));

    pdfButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/xls.png"))); // NOI18N
    pdfButton.setToolTipText("Create Report");
    pdfButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pdfButtonActionPerformed(evt);
      }
    });

    refreshData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/refresh-icon.png"))); // NOI18N
    refreshData.setToolTipText("Refresh data");
    refreshData.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        refreshDataActionPerformed(evt);
      }
    });

    bNewStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
    bNewStage.setText("New Stage");
    bNewStage.setToolTipText("Add a new stage tab");
    bNewStage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bNewStageActionPerformed(evt);
      }
    });

    butGroupExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/xlsGroups2.png"))); // NOI18N
    butGroupExport.setToolTipText("Groups Export");
    butGroupExport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butGroupExportActionPerformed(evt);
      }
    });

    jchTV.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    jchTV.setSelected(true);
    jchTV.setToolTipText("Show Table on TV");
    jchTV.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jchTV.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    jchTV.setLabel("TV");
    jchTV.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jchTVStateChanged(evt);
      }
    });

    bRestartWebServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/web-restart.png"))); // NOI18N
    bRestartWebServer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bRestartWebServerActionPerformed(evt);
      }
    });

    butCopyToClipboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/clipboard_paste.png"))); // NOI18N
    butCopyToClipboard.setToolTipText("Copy to clipboard");
    butCopyToClipboard.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butCopyToClipboardActionPerformed(evt);
      }
    });

    butCopyGropusToClipboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/clipboard_paste_group.png"))); // NOI18N
    butCopyGropusToClipboard.setToolTipText("Groups - copy to clipboard");
    butCopyGropusToClipboard.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butCopyGropusToClipboardActionPerformed(evt);
      }
    });

    butCopyToWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/export.png"))); // NOI18N
    butCopyToWeb.setToolTipText("Copy to Web Site");
    butCopyToWeb.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butCopyToWebActionPerformed(evt);
      }
    });

    bStopChecking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/stopSeach.png"))); // NOI18N
    bStopChecking.setToolTipText("Stop Trans Seach");
    bStopChecking.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bStopCheckingActionPerformed(evt);
      }
    });

    autoStrartRaceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/start_button.png"))); // NOI18N
    autoStrartRaceButton.setToolTipText("Auto Start Race Invitation, Start Search, + 3minutes to Ready, Start Race... Next group If You stop AutoStart, Active Race is not be stopped.");
    autoStrartRaceButton.setBorder(null);
    autoStrartRaceButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        autoStrartRaceButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
    topPanel.setLayout(topPanelLayout);
    topPanelLayout.setHorizontalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(timerCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
        .addComponent(autoStrartRaceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bStopChecking, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butCopyToWeb, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butCopyGropusToClipboard, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butCopyToClipboard, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bRestartWebServer, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jchTV)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(refreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butGroupExport, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pdfButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bNewStage)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butRemoveSatge)
        .addContainerGap())
    );
    topPanelLayout.setVerticalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addGap(4, 4, 4)
        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(butCopyToWeb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butCopyGropusToClipboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butCopyToClipboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bRestartWebServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(pdfButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(butRemoveSatge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bNewStage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addComponent(timerCaption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(refreshData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butGroupExport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jchTV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bStopChecking, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(autoStrartRaceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    butCopyToWeb.setVisible(false);
    autoStrartRaceButton.getAccessibleContext().setAccessibleDescription("Auto Start Race: Invitation, Start Search, wait 3minutes and Start Race...");

    jSplitPane2.setDividerLocation(200);

    jTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    jTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jTable.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTableKeyPressed(evt);
      }
    });
    jScrollPane1.setViewportView(jTable);

    jSplitPane2.setRightComponent(jScrollPane1);

    jTree.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
    jTree.setDragEnabled(true);
    jTree.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTreeKeyPressed(evt);
      }
    });
    jScrollPane2.setViewportView(jTree);

    jSplitPane2.setLeftComponent(jScrollPane2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jSplitPane2))
        .addGap(0, 0, 0)
        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSplitPane1)
          .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void butRemoveSatgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRemoveSatgeActionPerformed
    // TODO add your handling code here:
    if (mainForm.activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop the Active Race.");
      return;
    }
    int res = JOptionPane.showConfirmDialog(this, "Do you want to delete '" + stage.CAPTION + "' Stage?", "Delete Stage", JOptionPane.YES_NO_OPTION);
    if (res == JOptionPane.YES_OPTION) {
      try {
        VS_STAGE.dbControl.delete(mainForm.con, stage);
        mainForm.setActiveRace(mainForm.activeRace,true);
      } catch (Exception e) {
        mainForm.toLog(e);
      }
    }
  }//GEN-LAST:event_butRemoveSatgeActionPerformed

  private void butConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butConfigActionPerformed
    // TODO add your handling code here:    

    if (mainForm.activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop the Active Race.");
      return;
    }

    // refresh data tab
    //refreshDataActionPerformed(evt);
    StageNewForm.init(mainForm, stage, this).setVisible(true);
  }//GEN-LAST:event_butConfigActionPerformed

  private void pdfButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfButtonActionPerformed
    // TODO add your handling code here:
    //tableToPDF();
    tableToXLS();
  }//GEN-LAST:event_pdfButtonActionPerformed

  public void tableToXLS_old() {
    try {
      JDEDate jd = new JDEDate();
      OutReport out = new OutReport(jd.getDDMMYYYY("-"));
      //out.setShowExcel(true);
      out.setReportName(jd.getDDMMYYYY("-") + "_stage");

      tableToXLS_old(out);

      out.closeDataStreams();
      String xlsFile = XLSMaker.makeXLS(out);
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + xlsFile + "\"");   //open the file chart.pdf 
    } catch (Exception e) {
      e.printStackTrace();
      mainForm._toLog(e);
    }
  }
  
   public void tableToXLS() {
     tableToXLS(null);
   }
  
  // POI
   public void tableToXLS(Workbook wb) {
    try {
      TableToXLS.tableToXLS2(wb, stage.CAPTION,mainForm.activeRace.RACE_NAME, stage.CAPTION, jTable, this.stageTableAdapter.getColumns(),true);
    } catch (Exception e) {
      e.printStackTrace();
      mainForm._toLog(e);
    }
  }

  public void tableToXLS_old(OutReport out) {
    try {
      JDEDate jd = new JDEDate();
      int sheet = out.addStream();

      out.setReportName(sheet, stage.CAPTION);
      out.setViewFileName(sheet, "view.xml");
      IVar pool = new VarPool();
      pool.addChild(new StringVar("VisibleSheet", ""));
      pool.addChild(new StringVar("ConditionalFormatting", ""));
      pool.addChild(new StringVar("ExcelCellNames", ""));
      pool.addChild(new StringVar("ColumsInfo", ""));

      if (stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
        pool.addChild(new StringVar("FIX_ROWS", "5"));
        out.addToDataFile(sheet, "info:$$" + mainForm.getLocaleString("Confirm by") + "$$::$$" + mainForm.getLocaleString("Race") + " : " + mainForm.activeRace.RACE_NAME + "$$:");
        out.addToDataFile(sheet, "info:$$" + mainForm.getLocaleString("General Judge") + "$$::$$" + mainForm.getLocaleString("Stage") + " : " + stage.CAPTION + "$$:");
        out.addToDataFile(sheet, "info2::$$" + mainForm.activeRace.JUDGE + "$$::$$" + mainForm.getLocaleString("Race date") + " : " + mainForm.activeRace.RACE_DATE.getDateAsDDMMYYYY("-") + "$$:");
      } else {
        pool.addChild(new StringVar("FIX_ROWS", "4"));
        out.addToDataFile(sheet, "info:$$" + mainForm.getLocaleString("Race") + " : " + mainForm.activeRace.RACE_NAME + "$$:");
        out.addToDataFile(sheet, "info:$$" + mainForm.getLocaleString("Stage") + " : " + stage.CAPTION + "$$:");
      }

      out.addToDataFile(sheet, "info:");
      out.applayPoolToViewFile(sheet, pool);

      int rowCount = jTable.getRowCount();
      int colCount = jTable.getColumnCount();
      String head = "head:";
      for (int i = 0; i < colCount; i++) {
        head += "$$" + jTable.getColumnName(i).replaceAll("\n", "<br>") + "$$:";
      }
      out.addToDataFile(sheet, head);

      for (int row = 0; row < rowCount; row++) {
        String line = "data:";
        for (int col = 0; col < colCount; col++) {
          boolean itLapTime = false;
          if (jTable.getColumnName(col).toLowerCase().indexOf("lap") >= 0) {
            itLapTime = true;
          }
          Object obj = jTable.getModel().getValueAt(row, col);
          /*if (itLapTime && obj!=null && !obj.toString().equals("")){
              try{
                obj = getTimeIntervel(Long.parseLong(obj.toString()));
              }catch(Exception e){}  
            }*/
          line += "{" + this.stageTableAdapter.getColumnCellID(col) + "}$$" + obj + "$$:";
        }
        out.addToDataFile(sheet, line);
      }

      if (stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
        out.addToDataFile(sheet, "info:");
        out.addToDataFile(sheet, "info:");
        out.addToDataFile(sheet, "info3:$$" + mainForm.getLocaleString("Secretary") + "$$::$$" + mainForm.activeRace.SECRETARY + "$$:");
      }

      out.closeDataFile(sheet);
    } catch (Exception e) {
      e.printStackTrace();
      mainForm._toLog(e);
    }
  }
  
  public void refreshButton(){
    refreshDataActionPerformed(null);
  }

  private void refreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshDataActionPerformed
    // TODO add your handling code here:
    if (mainForm.activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop the Active Race.");
      return;
    }

    for (Integer grup_index : stage.groups.keySet()) {
      VS_STAGE_GROUP st_gr = stage.groups.get(grup_index);
      for (VS_STAGE_GROUPS usr : st_gr.users) {
        usr.IS_RECALULATED = 0;
        try {
          VS_STAGE_GROUPS.dbControl.update(mainForm.con, usr);
        } catch (Exception e) {
        }
      }
    }

    refreshDataActionPerformedWitoutRecalulation();

    /*if (stage.STAGE_TYPE==MainForm.STAGE_RACE){
      StageTableAdapter.STAGE_COLUMN[] columns = stageTableAdapter.getColumns();
      for (int index=0; index<columns.length; index++){        
        if (columns[index].ID==StageTableAdapter.STAGE_COLUMN.CID_SCORE && stage.RACE_TYPE==MainForm.RACE_TYPE_WHOOP){
          jTable.getColumnModel().getColumn(index).setCellEditor(new DefaultCellEditor(ListEditTools.generateBox(mainForm.SCORES_WHOOP)));
        }
      }
    }*/
  }//GEN-LAST:event_refreshDataActionPerformed

  public void refreshDataActionPerformedWitoutRecalulation() {
    // TODO add your handling code here:
    if (mainForm.activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop the Active Race.");
      return;
    }

    if (isOneTable == false) {
      treeModel = new StageTreeModel(this);
      jTree.setModel(treeModel);
      jTree.setTransferHandler(new StageTabTreeTransferHandler(this));
      jTree.setDragEnabled(true);
      jTree.setDropMode(DropMode.USE_SELECTION);
      StageTreeCellRender render = new StageTreeCellRender(this);
      jTree.setCellRenderer(render);
      expandAllJTree();
    }

    jTable.setRowHeight(30);
    stageTableAdapter = new StageTableAdapter(this);
    jTable.setModel(stageTableAdapter);
    jTable.setDefaultRenderer(Object.class, stageTableAdapter);

    MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();
    for (int i = 0; i < stageTableAdapter.getColumnCount(); i++) {
      jTable.getColumnModel().getColumn(i).setMinWidth(stageTableAdapter.getMinWidth(i));
      jTable.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
    }
    jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    refreshData(true);

    /*if (stage.STAGE_TYPE==MainForm.STAGE_RACE){
      StageTableAdapter.STAGE_COLUMN[] columns = stageTableAdapter.getColumns();
      for (int index=0; index<columns.length; index++){        
        if (columns[index].ID==StageTableAdapter.STAGE_COLUMN.CID_SCORE && stage.RACE_TYPE==MainForm.RACE_TYPE_WHOOP){
          jTable.getColumnModel().getColumn(index).setCellEditor(new DefaultCellEditor(ListEditTools.generateBox(mainForm.SCORES_WHOOP)));
        }
      }
    }*/
  }

  private void keyPressed(java.awt.event.KeyEvent evt) {
    if (raceTimer.isRunning()) {
      //JOptionPane.showMessageDialog(mainForm, "key:"+evt.getKeyChar(), "Information", JOptionPane.INFORMATION_MESSAGE);              
      if (evt.getKeyChar() >= '1' && evt.getKeyChar() <= '8') {
        if (evt.isAltDown() || evt.isControlDown()) {
          try {
            int user_index = evt.getKeyChar() - '0' - 1;
            long time = Calendar.getInstance().getTimeInMillis();
            VS_STAGE_GROUPS usr = mainForm.activeGroup.users.get(user_index);
            VS_RACE_LAP lap = stage.getLastLap(mainForm, usr.GROUP_NUM, usr.VS_PRIMARY_TRANS, usr.REG_ID, mainForm.raceTime, usr);
            if (lap != null) {
              if (time - lap.TIME_FROM_START > 5000) {
                int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete last time" + usr.PILOT + " ?", "Delete lap time?", JOptionPane.YES_NO_OPTION);
                if (res != JOptionPane.YES_OPTION) {
                  return;
                }
              }
              try {
                stage.delLap(mainForm, usr.GROUP_NUM, usr.VS_PRIMARY_TRANS, usr.REG_ID, lap.LAP, lap);
                usr.IS_RECALULATED = 0;
                usr.BEST_LAP = 0;
                usr.IS_FINISHED = 0;
                pleasuUpdateTable = true;
              } catch (Exception ein) {
              }
            }
          } catch (Exception e) {
          }
        } else {
          try {
            int user_index = evt.getKeyChar() - '0' - 1;
            long time = Calendar.getInstance().getTimeInMillis();
            VS_STAGE_GROUPS usr = mainForm.activeGroup.users.get(user_index);
            stage.addLapFromKeyPress(mainForm, usr, time);
            pleasuUpdateTable = true;
          } catch (Exception e) {
          }
        }
      }
    }
  }

  private void bNewStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewStageActionPerformed
    // TODO add your handling code here:
    if (mainForm.activeGroup != null) {
      JOptionPane.showMessageDialog(this, "Please stop the Active Race.");
      return;
    }
    StageNewForm.init(mainForm, null, null).setVisible(true);
  }//GEN-LAST:event_bNewStageActionPerformed

  private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    keyPressed(evt);
    // TODO add your handling code here:
  }//GEN-LAST:event_formKeyPressed

  private void jTreeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTreeKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);
  }//GEN-LAST:event_jTreeKeyPressed

  private void topPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topPanelKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);
  }//GEN-LAST:event_topPanelKeyPressed

  private void butGroupExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butGroupExportActionPerformed
    // TODO add your handling code here:
    treeToXLS();
  }//GEN-LAST:event_butGroupExportActionPerformed

  private void jchTVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jchTVStateChanged
    // TODO add your handling code here:
    if (jchTV.isSelected()) {
      mainForm.activeStage = stage;
    } else {
      mainForm.activeStage = null;
    }
  }//GEN-LAST:event_jchTVStateChanged

  private void bRestartWebServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRestartWebServerActionPerformed
    // TODO add your handling code here
    try {
      SystemOptions.runWebServer(mainForm, false);
    } catch (Exception e) {
    }
    SystemOptions.runWebServer(mainForm, true);
  }//GEN-LAST:event_bRestartWebServerActionPerformed

  public int getMaxPilotName() {
    int result = 5;
    for (Integer grup_index : stage.groups.keySet()) {
      VS_STAGE_GROUP st_gr = stage.groups.get(grup_index);
      for (VS_STAGE_GROUPS usr : st_gr.users) {
        if (usr.PILOT.trim().length() > result) {
          result = usr.PILOT.trim().length();
        }
      }
    }
    return result;
  }

  private void butCopyToClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCopyToClipboardActionPerformed
    // TODO add your handling code here:

    /*int len = getMaxPilotName();
    
    String sep = " | ";
    StringBuffer text = new StringBuffer();
    text.append(mainForm.getLocaleString("Race")+" : "+mainForm.activeRace.RACE_NAME+"\n");
    text.append(mainForm.getLocaleString("Stage")+" : "+stage.CAPTION+"\n");
    text.append("\n");
    
    for (Integer grup_index : stage.groups.keySet()){
      VS_STAGE_GROUP st_gr = stage.groups.get(grup_index);
      for (VS_STAGE_GROUPS usr : st_gr.users){        
        text.append(mainForm.getLocaleString("Group")+usr.GROUP_NUM+sep+ Tools.padr(usr.PILOT.trim(), len)+sep+usr.CHANNEL +sep+getTimeIntervel(usr.RACE_TIME)+sep+getTimeIntervel(usr.BEST_LAP)+"\n");
      }
      text.append("\n");
    } */
    int rowCount = jTable.getRowCount();
    int colCount = jTable.getColumnCount();

    String sep = " \t ";
    StringBuffer text = new StringBuffer();
    text.append(mainForm.getLocaleString("Race") + " : " + mainForm.activeRace.RACE_NAME + "\n");
    text.append(mainForm.getLocaleString("Stage") + " : " + stage.CAPTION + "\n");
    text.append("\n");

    /*String head = "";
    for (int i = 0; i < colCount; i++) {
      head += jTable.getColumnName(i).replaceAll("\n", " ") + sep;
    }
    text.append(head+"\n");*/
    for (int row = 0; row < rowCount; row++) {
      String line = "";
      StageTableData std = this.stageTableAdapter.getTableData(row);
      if (std.isGrpup && isOneTable) {
        continue;
      }
      if (std.isGrpup) {
        line += "--==  " + mainForm.getLocaleString("Group") + " " + std.group.GROUP_NUM + "  ==--";
      } else {
        for (int col = 0; col < colCount; col++) {
          Object obj = jTable.getModel().getValueAt(row, col);
          if (obj.equals("")) {
            continue;
          }
          line += jTable.getColumnName(col).replaceAll("\n", " ") + sep + obj + sep + "\n";
        }
      }
      text.append(line + "\n");
    }

    StringSelection data = new StringSelection(text.toString());
    Clipboard cb = Toolkit.getDefaultToolkit()
            .getSystemClipboard();
    cb.setContents(data, data);

  }//GEN-LAST:event_butCopyToClipboardActionPerformed

  private void butCopyGropusToClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCopyGropusToClipboardActionPerformed
    // TODO add your handling code here:

    int len = getMaxPilotName();
    String sep = " | ";
    StringBuffer text = new StringBuffer();

    text.append(mainForm.getLocaleString("Race") + " : " + mainForm.activeRace.RACE_NAME + "\n");
    text.append(mainForm.getLocaleString("Stage") + " : " + stage.CAPTION + "\n");
    text.append("\n");

    for (Integer grup_index : stage.groups.keySet()) {
      VS_STAGE_GROUP st_gr = stage.groups.get(grup_index);
      for (VS_STAGE_GROUPS usr : st_gr.users) {
        text.append(mainForm.getLocaleString("Group") + usr.GROUP_NUM + sep + Tools.padr(usr.PILOT.trim(), len) + sep + usr.CHANNEL + "\n");
      }
      text.append("\n");
    }

    StringSelection data = new StringSelection(text.toString());
    Clipboard cb = Toolkit.getDefaultToolkit()
            .getSystemClipboard();
    cb.setContents(data, data);
  }//GEN-LAST:event_butCopyGropusToClipboardActionPerformed

  private void butCopyToWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCopyToWebActionPerformed
    // TODO add your handling code here:
    if (mainForm.activeRace.WEB_RACE_ID != null && !mainForm.activeRace.WEB_RACE_ID.equals("")) {
      IRegSite site = RegistrationImportForm.getSite(mainForm.activeRace.WEB_SYSTEM_SID);
      if (site == null) {
        return;
      }
      if (site.isSuportedToWebUpload()) {
        site.uploadToWebSystem(null, this, false, true);
      } else {
        JOptionPane.showMessageDialog(null, "Update race data is not supported for site :" + site.REG_SITE_NAME);
      }
    }
  }//GEN-LAST:event_butCopyToWebActionPerformed

  private void bStopCheckingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStopCheckingActionPerformed
    // TODO add your handling code here:
    stopSearch();
  }//GEN-LAST:event_bStopCheckingActionPerformed

  private void jTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);
  }//GEN-LAST:event_jTableKeyPressed

  private void autoStrartRaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoStrartRaceButtonActionPerformed
    if (autoStratRaceTimer.isRunning()){
      currentStateAutoStrat = AUTOSTART_STATE_STOP;     
      currentStateAutoStrat = 0;
      autoStratRaceTimer.stop();
      /*autoStrartRaceButton.setBackground(BUTTON_BACKGROUND);    
      Border b = 
      autoStrartRaceButton.getBorder();*/
      autoStrartRaceButton.setBorder(BUTTON_DEFAULT_BORDER);
    }else{
      int MINUTES = 3;
      try{
        MINUTES = Integer.parseInt(VS_SETTING.getParam(mainForm.con, "WAITING_TIME", "3"));
      }catch(Exception e){}        
      WATING_TIME = MINUTES*1000*60;               
              
      currentStateAutoStrat = 0;     
      currentStateAutoStrat = 0;
      autoStratRaceTimer.start();
      mainForm.unRaceTime = Calendar.getInstance().getTimeInMillis();
      //autoStrartRaceButton.setBackground(Color.RED);
      autoStrartRaceButton.setBorder(BUTTON_RED_BORDER);
    }
    
  }//GEN-LAST:event_autoStrartRaceButtonActionPerformed

  static final int AUTOSTART_STATE_INVATE = 100;
  static final int AUTOSTART_STATE_SEARCH_TRANS = 200;
  static final int AUTOSTART_STATE_WAITING = 300;
  static final int AUTOSTART_STATE_RACE = 400;
  static final int AUTOSTART_STATE_RACING = 450;  
  static final int AUTOSTART_STATE_STOP = 500;
  int currentStateAutoStrat = 0;
  int currentTimeoutAutoStrat = 0;
  VS_STAGE_GROUP GROUP_FOR_AUTO_START = null;
  long WATING_TIME = 1000*60*3; // 3 minutes

  /**
   * * Timer for Automatic Start Race
   */
  Timer autoStratRaceTimer = new Timer(1000, new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (mainForm.activeGroup == null) {
        if (currentStateAutoStrat == AUTOSTART_STATE_STOP) {
          autoStratRaceTimer.stop();
        }
        currentTimeoutAutoStrat += autoStratRaceTimer.getDelay();
        if (currentStateAutoStrat == 0 || currentStateAutoStrat == AUTOSTART_STATE_RACING) {
          // find new group
          GROUP_FOR_AUTO_START = null;          
          //List<Integer> keySet = new ArrayList(stage.groups.keySet());
          //Collections.sort(keySet);
          for (Integer grup_index : stage.groups.keySet()) {
            VS_STAGE_GROUP st_gr = stage.groups.get(grup_index);
            if (st_gr.users != null && st_gr.users.size() > 0) {
              if (st_gr.users.get(0).IS_FINISHED==1) continue;
              if (st_gr.users.get(0).IS_FINISHED==0){
                GROUP_FOR_AUTO_START = st_gr;
                break;
              }
            }
          }
          if (GROUP_FOR_AUTO_START!=null){
            currentStateAutoStrat = AUTOSTART_STATE_INVATE;
            currentTimeoutAutoStrat = 0;
            invateAction(GROUP_FOR_AUTO_START.GROUP_NUM, false);
          }else{
            autoStratRaceTimer.stop();
            //autoStrartRaceButton.setBackground(BUTTON_BACKGROUND);
            autoStrartRaceButton.setBorder(BUTTON_DEFAULT_BORDER);
            mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().stageFinished());
            JOptionPane.showMessageDialog(mainForm, "The Stage has been finished.", "Information", JOptionPane.INFORMATION_MESSAGE);
          }          
        }
        if (currentStateAutoStrat == AUTOSTART_STATE_INVATE && currentTimeoutAutoStrat>5000 && GROUP_FOR_AUTO_START!=null) {
          startSearchAction(GROUP_FOR_AUTO_START.GROUP_NUM, false);
          currentStateAutoStrat = AUTOSTART_STATE_WAITING;
          currentTimeoutAutoStrat = 0;
        }
        if (currentStateAutoStrat == AUTOSTART_STATE_WAITING && GROUP_FOR_AUTO_START!=null){
          //if (currentTimeoutAutoStrat==0) mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().raceWillBeStarted(WATING_TIME));
          long interval = WATING_TIME-currentTimeoutAutoStrat;
          int min = (int)interval/(1000*60);
          if (interval==1000*60*min && min!=0) {
            mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().raceWillBeStarted(interval));
          }          
          if (interval==1000*30) mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().raceWillBeStarted(interval));
          if (interval==0 || interval<0) {
            currentStateAutoStrat = AUTOSTART_STATE_RACE;
            currentTimeoutAutoStrat = 0;      
            stopSearch();
            String message = startRaceAction(GROUP_FOR_AUTO_START.GROUP_NUM, false);
            if (message.equals("")) currentStateAutoStrat = AUTOSTART_STATE_RACING;
            else {
              mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().waitingAdmin());
              JOptionPane.showMessageDialog(mainForm, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
          }
        }
      }
    }
  });

  public void stopSearch() {
    // TODO : 
    mainForm.lap_log.writeFile("---==  Stop Search  ==---");
    checkerTimer.stop();
    InfoForm.init(mainForm, "", 100).setVisible(false);
    if (mainForm.vsTimeConnector != null) {
      preapreTimeMachineToRace();
      try {
        Thread.currentThread().sleep(300);
      } catch (Exception ein) {
      }
      preapreTimeMachineToRace();
      InfoForm.init(mainForm, "", 100).setVisible(false);
    }
    this.bStopChecking.setVisible(false);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton autoStrartRaceButton;
  private javax.swing.JButton bNewStage;
  private javax.swing.JButton bStopChecking;
  private javax.swing.JButton butConfig;
  private javax.swing.JButton butCopyGropusToClipboard;
  private javax.swing.JButton butCopyToClipboard;
  private javax.swing.JButton butCopyToWeb;
  private javax.swing.JButton butGroupExport;
  private javax.swing.JButton butRemoveSatge;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  public javax.swing.JTable jTable;
  public javax.swing.JTree jTree;
  public javax.swing.JCheckBox jchTV;
  private javax.swing.JButton pdfButton;
  public javax.swing.JButton refreshData;
  private javax.swing.JLabel timerCaption;
  private javax.swing.JPanel topPanel;
  // End of variables declaration//GEN-END:variables

  public void createGroups() {
    try {
      if (stage != null && stage.ID != -1) {
        VS_STAGE parent_stage = null;
        if (parent_stage == null) {
          try {
            parent_stage = VS_STAGE.dbControl.getItem(mainForm.con, "ID=? and RACE_ID=?", stage.PARENT_STAGE_ID, stage.RACE_ID);
          } catch (Exception e) {
          }
        }
        if (parent_stage == null) {
          try {
            parent_stage = VS_STAGE.dbControl.getItem(mainForm.con, "CAPTION=? and RACE_ID=?", stage.PARENT_STAGE, stage.RACE_ID);
          } catch (Exception e) {
          }
        }

        if (stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
          return;
        }

        Map<String, VS_STAGE_GROUPS> qualification = null;
        if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
          List<VS_STAGE> stages = VS_STAGE.dbControl.getList(mainForm.con, "RACE_ID=? and STAGE_TYPE=? order by ID desc", stage.RACE_ID, MainForm.STAGE_QUALIFICATION_RESULT);
          if (stages != null && stages.size() > 0) {
            qualification = VS_STAGE_GROUPS.dbControl.getMap(mainForm.con, "PILOT", "STAGE_ID=?", stages.get(0).ID);
          }
        }

        VS_STAGE_GROUPS.dbControl.delete(mainForm.con, "STAGE_ID=?", stage.ID);

        if (parent_stage != null) {
          List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 order by GID", parent_stage.ID);
          // Copy grups to new Stage
          Map<String, Map<String, Map<String, VS_RACE_LAP>>> laps = VS_RACE_LAP.dbControl.getMap3(mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=? order by GROUP_NUM", stage.RACE_ID, parent_stage.ID);
          if (parent_stage.STAGE_TYPE != MainForm.STAGE_QUALIFICATION_RESULT && parent_stage.STAGE_TYPE != MainForm.STAGE_RACE_RESULT) {

          }
          if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
            if ((stage.RACE_TYPE == MainForm.RACE_TYPE_OLYMPIC || stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE) && parent_stage != null && parent_stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT && parent_stage.PARENT_STAGE_ID > 0) {
              try {
                parent_stage = VS_STAGE.dbControl.getItem(mainForm.con, "ID=? and RACE_ID=?", parent_stage.PARENT_STAGE_ID, stage.RACE_ID);
              } catch (Exception e) {
              }
            }
            /*if (stage.RACE_TYPE == MainForm.RACE_TYPE_OLYMPIC_LOSES && parent_stage != null && parent_stage.STAGE_TYPE == MainForm.STAGE_RACE) {
              groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
              HashMap<Long, List<VS_STAGE_GROUPS>> groupNums = new HashMap<>();
              List<Long> groupNumbers = new ArrayList<>();
              for (VS_STAGE_GROUPS usr : groups) {
                List<VS_STAGE_GROUPS> users = groupNums.get(usr.GROUP_NUM);
                if (users == null) {
                  users = new ArrayList<VS_STAGE_GROUPS>();
                  groupNums.put(usr.GROUP_NUM, users);
                  groupNumbers.add(usr.GROUP_NUM);
                }
                users.add(usr);
              }
              List<VS_STAGE_GROUPS> new_groups = new ArrayList<VS_STAGE_GROUPS>();
              int count_groups = (groupNums.size() + 1) / 2;
              for (int group = 0; group < count_groups; group++) {
                int NUM_IN_GROUP = 1;
                Long GROUP_NUM_FIRST = groupNumbers.get(group);
                Long GROUP_NUM_SECOND = groupNumbers.get(groupNumbers.size() - group - 1);
                if (GROUP_NUM_FIRST == GROUP_NUM_SECOND) {
                  GROUP_NUM_SECOND = -1L;
                }
                List<VS_STAGE_GROUPS> users = groupNums.get(GROUP_NUM_FIRST);
                for (VS_STAGE_GROUPS usr : users) {
                  if (usr.LOSE == 1) {
                    usr.STAGE_ID = stage.ID;
                    usr.GROUP_NUM = group + 1;
                    usr.NUM_IN_GROUP = NUM_IN_GROUP;
                    usr.WIN = 0;
                    usr.SCORE = 0;
                    usr.SCORE = 0;
                    usr.GID = -1;
                    usr.BEST_LAP = 0;
                    usr.RACE_TIME = 0;
                    NUM_IN_GROUP++;
                    new_groups.add(usr);
                  }
                }
                if (GROUP_NUM_SECOND != -1L) {
                  users = groupNums.get(GROUP_NUM_SECOND);
                  for (VS_STAGE_GROUPS usr : users) {
                    if (usr.LOSE == 1) {
                      usr.STAGE_ID = stage.ID;
                      usr.GROUP_NUM = group + 1;
                      usr.NUM_IN_GROUP = NUM_IN_GROUP;
                      usr.WIN = 0;
                      usr.SCORE = 0;
                      usr.SCORE = 0;
                      usr.GID = -1;
                      usr.BEST_LAP = 0;
                      usr.RACE_TIME = 0;
                      NUM_IN_GROUP++;
                      new_groups.add(usr);
                    }
                  }
                }
              }
              try {
                recalulateChannels(new_groups);
              } catch (Exception e) {
                e.printStackTrace();
              }
              for (VS_STAGE_GROUPS usr : new_groups) {
                usr.IS_FINISHED = 0;
                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
              }

            } else */
            if ((stage.RACE_TYPE == MainForm.RACE_TYPE_OLYMPIC || stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE) && parent_stage != null && parent_stage.STAGE_TYPE == MainForm.STAGE_RACE) {
              // Olimpic system implementation base on Qualification time, 1-4, 2-3
              // Check all groups
              groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
              List<VS_STAGE_GROUPS> groups_for_detect_losers = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by GROUP_NUM, NUM_IN_GROUP", parent_stage.ID);
              List<VS_STAGE_GROUPS> groups_losers_temp = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=1 order by GROUP_NUM", parent_stage.ID);
              List<VS_STAGE_GROUPS> groups_losers = new ArrayList<VS_STAGE_GROUPS>();
              List<VS_STAGE_GROUPS> groups_losers_new = new ArrayList<VS_STAGE_GROUPS>();
              int last_loser_goruop = 1;
              int NUM_IN_GROUP_LOSERS = 1;
              if (groups_losers_temp != null) {
                for (VS_STAGE_GROUPS usr : groups_losers_temp) {
                  if (NUM_IN_GROUP_LOSERS > stage.COUNT_PILOTS_IN_GROUP) {
                    last_loser_goruop++;
                    NUM_IN_GROUP_LOSERS = 1;
                  }
                  if (usr.WIN == 1) {
                    usr.IS_FINISHED = 0;
                    usr.STAGE_ID = stage.ID;
                    usr.GROUP_NUM = last_loser_goruop;
                    usr.NUM_IN_GROUP = NUM_IN_GROUP_LOSERS;
                    usr.WIN = 0;
                    usr.SCORE = 0;
                    usr.SCORE = 0;
                    usr.GID = -1;
                    usr.LAPS = 0;
                    usr.BEST_LAP = 0;
                    usr.RACE_TIME = 0;
                    usr.GROUP_TYPE = 1;
                    NUM_IN_GROUP_LOSERS++;
                    groups_losers.add(usr);
                  }
                }
              }

              HashMap<Long, List<VS_STAGE_GROUPS>> groupNums = new HashMap<>();
              List<Long> groupNumbers = new ArrayList<>();
              for (VS_STAGE_GROUPS usr : groups) {
                List<VS_STAGE_GROUPS> users = groupNums.get(usr.GROUP_NUM);
                if (users == null) {
                  users = new ArrayList<VS_STAGE_GROUPS>();
                  groupNums.put(usr.GROUP_NUM, users);
                  groupNumbers.add(usr.GROUP_NUM);
                }
                users.add(usr);
              }
              List<VS_STAGE_GROUPS> new_groups = new ArrayList<VS_STAGE_GROUPS>();
              NUM_IN_GROUP_LOSERS = 1;
              last_loser_goruop = 1;
              int count_loser_group = groups.size() / 2 / stage.COUNT_PILOTS_IN_GROUP;
              for (VS_STAGE_GROUPS usr : groups_for_detect_losers) {
                if (usr.LOSE == 1) {
                  if (last_loser_goruop > count_loser_group) {
                    last_loser_goruop = 1;
                    NUM_IN_GROUP_LOSERS++;
                  }
                  usr.STAGE_ID = stage.ID;
                  usr.GROUP_NUM = last_loser_goruop;
                  usr.NUM_IN_GROUP = NUM_IN_GROUP_LOSERS;
                  usr.WIN = 0;
                  usr.LOSE = 0;
                  usr.SCORE = 0;
                  usr.SCORE = 0;
                  usr.loses = 1;
                  usr.GID = -1;
                  usr.LAPS = 0;
                  usr.BEST_LAP = 0;
                  usr.GROUP_TYPE = 1;
                  usr.RACE_TIME = 0;
                  usr.IS_FINISHED = 0;
                  usr.GROUP_TYPE = 1;
                  last_loser_goruop++;
                  groups_losers_new.add(usr);
                }
              }
              int count_groups = (groupNums.size() + 1) / 2;
              for (int group = 0; group < count_groups; group++) {
                int NUM_IN_GROUP = 1;
                Long GROUP_NUM_FIRST = groupNumbers.get(group);
                Long GROUP_NUM_SECOND = groupNumbers.get(groupNumbers.size() - group - 1);
                if (GROUP_NUM_FIRST == GROUP_NUM_SECOND) {
                  GROUP_NUM_SECOND = -1L;
                }
                List<VS_STAGE_GROUPS> users = groupNums.get(GROUP_NUM_FIRST);
                for (VS_STAGE_GROUPS usr : users) {
                  if (usr.WIN == 1) {
                    usr.STAGE_ID = stage.ID;
                    usr.IS_FINISHED = 0;
                    usr.GROUP_NUM = group + 1;
                    usr.NUM_IN_GROUP = NUM_IN_GROUP;
                    usr.WIN = 0;
                    usr.SCORE = 0;
                    usr.SCORE = 0;
                    usr.GID = -1;
                    usr.LAPS = 0;
                    usr.BEST_LAP = 0;
                    usr.RACE_TIME = 0;
                    NUM_IN_GROUP++;
                    new_groups.add(usr);
                  }
                }
                if (GROUP_NUM_SECOND != -1L) {
                  users = groupNums.get(GROUP_NUM_SECOND);
                  NUM_IN_GROUP_LOSERS = 1;
                  last_loser_goruop = 0;
                  for (VS_STAGE_GROUPS usr : users) {
                    if (usr.WIN == 1) {
                      usr.STAGE_ID = stage.ID;
                      usr.GROUP_NUM = group + 1;
                      usr.NUM_IN_GROUP = NUM_IN_GROUP;
                      usr.WIN = 0;
                      usr.SCORE = 0;
                      usr.SCORE = 0;
                      usr.GID = -1;
                      usr.BEST_LAP = 0;
                      usr.LAPS = 0;
                      usr.IS_FINISHED = 0;
                      usr.RACE_TIME = 0;
                      NUM_IN_GROUP++;
                      new_groups.add(usr);
                    }
                  }
                }
              }
              try {
                recalulateChannels(new_groups);
              } catch (Exception e) {
                e.printStackTrace();
              }

              try {
                recalulateChannels(groups_losers);
              } catch (Exception e) {
                e.printStackTrace();
              }

              try {
                recalulateChannels(groups_losers_new);
              } catch (Exception e) {
                e.printStackTrace();
              }

              long max_real_groups = 0;
              for (VS_STAGE_GROUPS usr : new_groups) {
                //VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                if (max_real_groups < usr.GROUP_NUM) {
                  max_real_groups = usr.GROUP_NUM;
                }
              }
              if (stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE) {
                long max_real_groups2 = max_real_groups;
                if (groups_losers.size() > 0) {
                  for (VS_STAGE_GROUPS usr : groups_losers) {
                    usr.GROUP_NUM = max_real_groups + usr.GROUP_NUM;
                    usr.IS_FINISHED = 0;
                    //VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                    if (max_real_groups2 < usr.GROUP_NUM) {
                      max_real_groups2 = usr.GROUP_NUM;
                    }
                  }
                }
                for (VS_STAGE_GROUPS usr : groups_losers_new) {
                  usr.GROUP_NUM = max_real_groups2 + usr.GROUP_NUM;
                  usr.IS_FINISHED = 0;
                  //VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                }

                // Merge groups
                if (groups_losers_new.size() == 0 && groups_losers.size() == stage.COUNT_PILOTS_IN_GROUP / 2) {
                  for (VS_STAGE_GROUPS usr : groups_losers) {
                    //new_groups.add(usr);
                    addUserToGroup(usr, new_groups, stage.COUNT_PILOTS_IN_GROUP);
                  }
                  groups_losers.clear();
                  for (VS_STAGE_GROUPS usr : new_groups) {
                    usr.IS_PANDING = 0;
                  }
                  try {
                    recalulateChannels(new_groups);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                } else {
                  if (new_groups.size() == stage.COUNT_PILOTS_IN_GROUP / 2) {
                    for (VS_STAGE_GROUPS usr : new_groups) {
                      usr.IS_PANDING = 1;
                      usr.WIN = 1;
                    }
                  }
                  if (groups_losers_new.size() != 0 && groups_losers.size() != 0) {
                    if (groups_losers_new.size() < stage.COUNT_PILOTS_IN_GROUP && groups_losers.size() >= stage.COUNT_PILOTS_IN_GROUP) {
                      for (VS_STAGE_GROUPS usr : groups_losers_new) {
                        usr.IS_PANDING = 1;
                        usr.WIN = 1;
                      }
                    } else {
                      for (VS_STAGE_GROUPS usr : groups_losers_new) {
                        //groups_losers.add(usr);
                        addUserToGroup(usr, groups_losers, stage.COUNT_PILOTS_IN_GROUP);
                      }
                      groups_losers_new.clear();
                      try {
                        recalulateChannels(groups_losers);
                      } catch (Exception e) {
                        e.printStackTrace();
                      }
                    }
                  }

                }
              }

              // Saving to database
              for (VS_STAGE_GROUPS usr : new_groups) {
                usr.STAGE_ID = stage.ID;
                usr.CHECK_FOR_RACE = 1;
                if (qualification != null) {
                  VS_STAGE_GROUPS quala = qualification.get(usr.PILOT);
                  if (quala != null) {
                    usr.QUAL_TIME = quala.RACE_TIME;
                  }
                }
                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
              }
              if (stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE) {
                for (VS_STAGE_GROUPS usr : groups_losers) {
                  usr.STAGE_ID = stage.ID;
                  usr.CHECK_FOR_RACE = 1;
                  if (qualification != null) {
                    VS_STAGE_GROUPS quala = qualification.get(usr.PILOT);
                    if (quala != null) {
                      usr.QUAL_TIME = quala.RACE_TIME;
                    }
                  }
                  VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                }
                for (VS_STAGE_GROUPS usr : groups_losers_new) {
                  usr.STAGE_ID = stage.ID;
                  usr.CHECK_FOR_RACE = 1;
                  if (qualification != null) {
                    VS_STAGE_GROUPS quala = qualification.get(usr.PILOT);
                    if (quala != null) {
                      usr.QUAL_TIME = quala.RACE_TIME;
                    }
                  }
                  VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                }
              }

              /*if (stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE) {
                for (VS_STAGE_GROUPS usr : groups_losers) {
                  usr.GROUP_NUM = max_real_groups + usr.GROUP_NUM;
                  usr.IS_FINISHED = 0;
                  VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
                }
              }*/
            } else if (stage.STAGE_TYPE==MainForm.STAGE_RACE && GroupFactory.getRaceCreatorByCode(stage.RACE_TYPE)!=null){
               try{
                 GroupFactory.getRaceCreatorByCode(stage.RACE_TYPE).createGroup(stage, parent_stage, mainForm.con);
               }catch(UserException ue){
                 JOptionPane.showConfirmDialog(this, ue.details, ue.error, JOptionPane.YES_OPTION);            
               }
            } else {
              // based on best time
              groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
              //checkGroupConstrain();
              //Map<String, VS_REGISTRATION> users = VS_REGISTRATION.dbControl.getMap(mainForm.con, "VS_TRANSPONDER", "VS_RACE_ID=? ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
              TreeSet<String> user_names = new TreeSet();
              for (VS_STAGE_GROUPS usr : groups) {
                if (user_names.contains(usr.PILOT)) {
                  usr.isError = 2;
                }
                user_names.add(usr.PILOT);
              }
              List<VS_STAGE_GROUPS> inactives = new ArrayList<VS_STAGE_GROUPS>();
              for (VS_STAGE_GROUPS usr : groups) {
                VS_REGISTRATION reg = usr.getRegistration(mainForm.con, stage.RACE_ID);
                if (reg != null) {
                  usr.PILOT = reg.VS_USER_NAME;
                  usr.REG_ID = reg.ID;
                  if (reg.IS_ACTIVE == 0 || usr.isError == 2) {
                    inactives.add(usr);
                  }
                }
              }
              for (VS_STAGE_GROUPS del : inactives) {
                groups.remove(del);
              }
              int count_man = groups.size();
              int count_max_in_groups = stage.COUNT_PILOTS_IN_GROUP;
              int count_man_in_group = 1;
              int count_groups = count_man / count_max_in_groups;
              if (count_groups * count_max_in_groups < count_man) {
                count_groups++;
              }
              int current_group = 1;

              //int[] GROUP_ALG4 = new int[]{1, 4, 2, 3};
              //int[] GROUP_ALG8 = new int[]{1, 8, 3, 6, 4, 5, 2, 7};                      
              int[] GROUP_ALG = createGroupsAlg(count_groups);

              String[] channels = stage.CHANNELS.split(";");
              HashMap<Integer, HashMap<String, Integer>> usingChannels = new HashMap<Integer, HashMap<String, Integer>>();
              for (VS_STAGE_GROUPS usr : groups) {
                HashMap<String, Integer> groupChannels = usingChannels.get(current_group);
                if (groupChannels == null) {
                  groupChannels = new HashMap<String, Integer>();
                  usingChannels.put(current_group, groupChannels);
                }
                usr.GID = -1;
                //usr.CHANNEL = channels[count_man_in_group - 1];
                usr.STAGE_ID = stage.ID;
                usr.IS_FINISHED = 0;
                usr.IS_RECALULATED = 0;
                usr.NUM_IN_GROUP = count_man_in_group;
                usr.GROUP_NUM = current_group;
                try {
                  if (GROUP_ALG != null) //usr.GROUP_NUM = GROUP_ALG.get(current_group - 1);
                  {
                    usr.GROUP_NUM = GROUP_ALG[current_group - 1];
                  }
                  /*if (count_groups == 4) {
                    usr.GROUP_NUM = GROUP_ALG4[current_group - 1];
                  }
                  if (count_groups == 8) {
                    usr.GROUP_NUM = GROUP_ALG8[current_group - 1];
                  } */
                } catch (Exception ein) {
                }
                usr.BEST_LAP = 0;
                usr.LAPS = 0;
                usr.RACE_TIME = 0;
                Integer countUse = groupChannels.get(usr.CHANNEL);
                if (countUse == null) {
                  countUse = 0;
                }
                countUse++;
                groupChannels.put(usr.CHANNEL, countUse);
                current_group++;
                if (current_group > count_groups) {
                  current_group = 1;
                  count_man_in_group++;
                }
              }

              //HashMap<Integer, HashMap<String, Integer>> checkChannelsAll = new HashMap<Integer, HashMap<String, Integer>>();
              recalulateChannels(groups);
              for (VS_STAGE_GROUPS usr : groups) {
                /*HashMap<String, Integer> checkChannels = checkChannelsAll.get((int) usr.GROUP_NUM);
                if (checkChannels == null) {
                  checkChannels = new HashMap<String, Integer>();
                  checkChannelsAll.put((int) usr.GROUP_NUM, checkChannels);
                }
                HashMap<String, Integer> groupChannels = usingChannels.get((int) usr.GROUP_NUM);
                Integer countUse = groupChannels.get(usr.CHANNEL);
                if (countUse > 1) {
                  if (checkChannels.get(usr.CHANNEL) == null) {
                    checkChannels.put(usr.CHANNEL, 1);
                  } else {
                    for (String channel : channels) {
                      if (groupChannels.get(channel) == null) {
                        usr.CHANNEL = channel;
                        groupChannels.put(usr.CHANNEL, 1);
                      }
                    }
                  }
                }*/

                if (qualification != null) {
                  VS_STAGE_GROUPS quala = qualification.get(usr.PILOT);
                  if (quala != null) {
                    usr.QUAL_TIME = quala.RACE_TIME;
                  }
                }

                usr.IS_FINISHED = 0;
                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
              }
            }
          } else { // Create pilot list as parent_id - only copy
            groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 order by GID", parent_stage.ID);
            // usual copy
            for (VS_STAGE_GROUPS usr : groups) {
              usr.GID = -1;
              usr.STAGE_ID = stage.ID;
              usr.isError = 0;
              usr.IS_FINISHED = 0;
              usr.IS_RECALULATED = 0;
              usr.LAPS = 0;
              usr.RACE_TIME = 0;
              usr.BEST_LAP = 0;

              if (qualification != null) {
                VS_STAGE_GROUPS quala = qualification.get(usr.PILOT);
                if (quala != null) {
                  usr.QUAL_TIME = quala.RACE_TIME;
                }
              }

              VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
            }
          }
        } else { // Parent Stage = null
          List<VS_REGISTRATION> users = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? and IS_ACTIVE=1 ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
          int count_man_in_group = 0;
          int GRUP_NUM = 1;
          String[] channels = stage.CHANNELS.split(";");
          if (stage.RACE_TYPE == MainForm.RACE_TYPE_EVERYONE_WITH_EACH_16) {
            int[] indx_pilots = {1, 2, 3, 4,
              5, 7, 6, 8,
              10, 11, 9, 12,
              15, 14, 16, 13,
              13, 1, 5, 9,
              14, 10, 2, 6,
              11, 15, 7, 3,
              4, 8, 12, 16,
              6, 16, 1, 11,
              12, 5, 15, 2,
              8, 9, 3, 14,
              13, 4, 10, 7,
              7, 12, 14, 1,
              2, 13, 8, 11,
              16, 3, 10, 5,
              9, 6, 4, 15,
              1, 8, 15, 10,
              9, 2, 7, 16,
              3, 12, 13, 6,
              5, 14, 11, 4};
            if (users.size() == 16) {
              for (int i = 0; i < indx_pilots.length; i++) {
                if (i % 4 == 0 && i != 0) {
                  GRUP_NUM++;
                  count_man_in_group = 0;
                }
                count_man_in_group = count_man_in_group + 1;
                VS_REGISTRATION user = users.get(indx_pilots[i] - 1);
                VS_STAGE_GROUPS gr = new VS_STAGE_GROUPS();
                gr.STAGE_ID = stage.ID;
                gr.REG_ID = user.ID;
                gr.GROUP_NUM = GRUP_NUM;
                gr.PILOT = user.VS_USER_NAME;
                gr.NUM_IN_GROUP = count_man_in_group;
                gr.CHANNEL = channels[count_man_in_group - 1];
                gr.REG_ID = user.ID;
                gr.VS_PRIMARY_TRANS = user.VS_TRANS1;
                //prev_type_pilot = user.PILOT_TYPE;

                if (qualification != null) {
                  VS_STAGE_GROUPS quala = qualification.get(gr.PILOT);
                  if (quala != null) {
                    gr.QUAL_TIME = quala.RACE_TIME;
                  }
                }

                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, gr);
              }
            } else {
              //ERROR
              System.out.println("ERROR GREATE GROUPS 16!!! ::" + users.size());
              JOptionPane.showConfirmDialog(StageTab.this, "The number of pilots should be 16.", "Error", JOptionPane.YES_OPTION);
            }
          } else {
            int prev_type_pilot = -1;
            for (VS_REGISTRATION user : users) {
              /*if (user.VS_TRANS1 == 0) {
                JOptionPane.showMessageDialog(this, "Please set Transponder ID for " + user.VS_USER_NAME);
                return;
              }*/
              // Create new Group, if FLAG_BY_PYLOT_TYPE=1 and New Pilot Type 
              if (prev_type_pilot != -1 && prev_type_pilot != user.PILOT_TYPE && stage.FLAG_BY_PYLOT_TYPE == 1) {
                GRUP_NUM++;
                count_man_in_group = 0;
              }
              // Create new group, if full
              count_man_in_group = count_man_in_group + 1;
              if (count_man_in_group > stage.COUNT_PILOTS_IN_GROUP) {
                GRUP_NUM++;
                count_man_in_group = 1;
              }
              VS_STAGE_GROUPS gr = new VS_STAGE_GROUPS();
              gr.STAGE_ID = stage.ID;
              gr.REG_ID = user.ID;
              gr.GROUP_NUM = GRUP_NUM;
              gr.PILOT = user.VS_USER_NAME;
              gr.NUM_IN_GROUP = count_man_in_group;
              gr.CHANNEL = channels[count_man_in_group - 1];
              gr.REG_ID = user.ID;
              gr.VS_PRIMARY_TRANS = user.VS_TRANS1;
              prev_type_pilot = user.PILOT_TYPE;

              if (qualification != null) {
                VS_STAGE_GROUPS quala = qualification.get(gr.PILOT);
                if (quala != null) {
                  gr.QUAL_TIME = quala.RACE_TIME;
                }
              }

              VS_STAGE_GROUPS.dbControl.insert(mainForm.con, gr);
            }
          }
        }
        System.out.println("Satge has been created : " + stage.ID);
        stage.IS_GROUP_CREATED = 1;
        VS_STAGE.dbControl.update(mainForm.con, stage);
      }
    } catch (Exception e) {
    }
  }

  public void recalulateChannels(List<VS_STAGE_GROUPS> users) {
    String[] channels = stage.CHANNELS.split(";");
    HashMap<Long, HashMap<String, Integer>> usingChannels = new HashMap();

    for (VS_STAGE_GROUPS usr : users) {
      HashMap<String, Integer> groupChannels = usingChannels.get(usr.GROUP_NUM);
      if (groupChannels == null) {
        groupChannels = new HashMap<String, Integer>();
        usingChannels.put(usr.GROUP_NUM, groupChannels);
      }
      Integer countUse = groupChannels.get(usr.CHANNEL);
      if (countUse == null) {
        countUse = 0;
      }
      countUse++;
      groupChannels.put(usr.CHANNEL, countUse);
    }
    HashMap<Long, HashMap<String, Integer>> checkChannelsAll = new HashMap<Long, HashMap<String, Integer>>();
    for (VS_STAGE_GROUPS usr : users) {
      HashMap<String, Integer> checkChannels = checkChannelsAll.get(usr.GROUP_NUM);
      if (checkChannels == null) {
        checkChannels = new HashMap<String, Integer>();
        checkChannelsAll.put(usr.GROUP_NUM, checkChannels);
      }
      HashMap<String, Integer> groupChannels = usingChannels.get(usr.GROUP_NUM);
      Integer countUse = groupChannels.get(usr.CHANNEL);
      if (countUse > 1) {
        if (checkChannels.get(usr.CHANNEL) == null) {
          checkChannels.put(usr.CHANNEL, 1);
        } else {
          for (String channel : channels) {
            if (groupChannels.get(channel) == null) {
              usr.CHANNEL = channel;
              groupChannels.put(usr.CHANNEL, 1);
              checkChannels.put(usr.CHANNEL, 1);
              break;
            }
          }
        }
      }
    }
  }

  public void preapreTimeMachineToRace() {
    try {
      if (mainForm.vsTimeConnector != null) {
        mainForm.vsTimeConnector.rfidUnlock();
        Thread.currentThread().sleep(200);
        mainForm.vsTimeConnector.setTime();
        Thread.currentThread().sleep(200);
        //mainForm.vsTimeConnector.rfidUnlock();
        //Thread.currentThread().sleep(200);
      }
    } catch (Exception ein) {
    }
  }

  public void addUserToGroup(VS_STAGE_GROUPS add_usr, List<VS_STAGE_GROUPS> users, int max_pilots_in_groups) {
    Map<Long, Integer> count_pilots_in_group = new HashMap();
    long max_group_index = 0;
    for (VS_STAGE_GROUPS usr : users) {
      Integer count_pilots = count_pilots_in_group.get(usr.GROUP_NUM);
      if (count_pilots == null) {
        count_pilots = 0;
      }
      count_pilots++;
      count_pilots_in_group.put(usr.GROUP_NUM, count_pilots);
      if (max_group_index < usr.GROUP_NUM) {
        max_group_index = usr.GROUP_NUM;
      }
    }
    // find empty group  
    boolean is_find = false;
    for (Long num : count_pilots_in_group.keySet()) {
      Integer count_pilots = count_pilots_in_group.get(num);
      if (count_pilots == null) {
        count_pilots = 0;
      }
      if (count_pilots < max_pilots_in_groups) {
        is_find = true;
        add_usr.GROUP_NUM = num;
        add_usr.NUM_IN_GROUP = count_pilots + 1;
        users.add(add_usr);
      }
    }
    // create new groups
    if (!is_find) {
      add_usr.GROUP_NUM = max_group_index + 1;
      add_usr.NUM_IN_GROUP = 1;
      users.add(add_usr);
    }
  }

  public StageTableData getGroupByNum(long GROUP_NUM) {
    if (stageTableAdapter.rows != null) {
      for (StageTableData tr : stageTableAdapter.rows) {
        if (tr.isGrpup && tr.group.GROUP_NUM == GROUP_NUM) {
          return tr;
        }
      }
    }
    return null;
  }

  public void runFinishCommand() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process p = runtime.exec("finish.cmd");
      int exitCode = p.waitFor();
    } catch (Exception rt_e) {
      MainForm._toLog(rt_e);
    }
  }

  public String startRaceAction(long GROUP_NUM, boolean showDialog) {
    FIRST_RACER_IS_FINISHED = true;
    raceTimerIsOver = false;    
    String message = "";
    if (mainForm.vsTimeConnector != null && showDialog) {
      long sec = (Calendar.getInstance().getTimeInMillis() - mainForm.vsTimeConnector.lastPingTime) / 1000;
      if (sec >= 5) {
        message = "The VS Time Machine has not response for " + sec + " seconds.";
        if (showDialog) {
          int res = JOptionPane.showConfirmDialog(StageTab.this, message + "\nDo you want to start ?", "Please check ping command.", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
          } else {
            return message;
          }
        } else {
          return message;
        }
      }
    }

    StageTableData td = getGroupByNum(GROUP_NUM);
    if (td == null) {
      return "Group is not found";
    }

    if (mainForm.activeGroup != null && mainForm.activeGroup != td.group) {
      message = "Please stop race. Group" + mainForm.activeGroup.GROUP_NUM;
      if (showDialog) {
        JOptionPane.showMessageDialog(mainForm, message, "Information", JOptionPane.INFORMATION_MESSAGE);
      }
      return message;
    }
    try {
      if (VS_SETTING.getParam(mainForm.con, "CHECK_RACE_GROUP", 1) == 1) {
        if (mainForm.lastInvateGroup == null || mainForm.lastInvateGroup.stage.ID != td.group.stage.ID
                || mainForm.lastInvateGroup.GROUP_INDEX != td.group.GROUP_INDEX) {
          message = "Please Invate the Group " + td.group.GROUP_NUM;
          if (showDialog) {
            JOptionPane.showMessageDialog(mainForm, message, "Information", JOptionPane.INFORMATION_MESSAGE);
          }
          return message;
        }
      }
    } catch (Exception ein) {
    }
    if (td.group != null && td.group.users != null && td.group.users.size() > 0 && td.group.users.get(0).IS_FINISHED == 1) {
      if (showDialog) {
        int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to re-flight Group" + td.group.GROUP_NUM + " ?", "Re-flight", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
        } else {
          return "";
        }
      }
    }
    if (mainForm.vsTimeConnector == null || !mainForm.vsTimeConnector.connected) {
      message = "Transponder hub device is not connected.";
      if (showDialog) {
        int res = JOptionPane.showConfirmDialog(StageTab.this, message + "\nDo you like to start?", "Information", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
        } else {
          return message;
        }
      } else {
        return message;
      }
    }
    preapreTimeMachineToRace();
    mainForm.setColorForGate();
    if (td != null && td.isGrpup == true) {
      if (stage.IS_LOCK == 1) {
        return "";
      }
      mainForm.lap_log.writeFile("---==  Run Race  ==---;" + stage.CAPTION + " [" + stage.ID + "];Group;" + td.group.GROUP_NUM);
      // We need to check : Run Race Group = Invate RaceGroup
      //mainForm.invateGroup = null;
      td.group.stageTab = StageTab.this;
      for (VS_STAGE_GROUPS user : td.group.users) {
        user.FIRST_LAP = 0;
        user.MAIN_TIME_IS_OVER = 0;
        user.hasBeenFlightLastLap = false;
        //user.BEST_LAP = 0;
        //user.LAPS =0;
      }
      timerCaption.setText(getTimeIntervelForTimer(0));
      timerCaption.setVisible(true);
      VS_RACE race = null;

      try {
        race = VS_RACE.dbControl.getItem(mainForm.con, "RACE_ID=?", stage.RACE_ID);
      } catch (UserException ex) {
        Logger.getLogger(StageTab.class.getName()).log(Level.SEVERE, null, ex);
      }
      if (race != null && race.RANDOM_BEEP != 1) {
        InfoForm.init(mainForm, "3").setVisible(true);
        mainForm.activeGroup = td.group;
        refreshTable();
        //if (useSpeach) mainForm.speaker.speak("Three!");                
        mainForm.beep.paly("three");
        Timer t1 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
          public void actionPerformed(ActionEvent e) {
            InfoForm.init(mainForm, "2").setVisible(true);
            //if (useSpeach) mainForm.speaker.speak("Two!");
            mainForm.beep.paly("two");
            Timer t2 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
              public void actionPerformed(ActionEvent e) {
                InfoForm.init(mainForm, "1").setVisible(true);
                //if (useSpeach) mainForm.speaker.speak("One!");                        
                mainForm.beep.paly("one");
                //int rnd = (int) (Math.random() * 3000);
                Timer t3 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                  public void actionPerformed(ActionEvent e) {
                    Runtime runtime = Runtime.getRuntime();
                    try {
                      Process p = runtime.exec("run.cmd");
                      int exitCode = p.waitFor();
                    } catch (Exception rt_e) {
                      MainForm._toLog(rt_e);
                    }
                    InfoForm.init(mainForm, "Go!").setVisible(true);
                    mainForm.beep.paly("beep");
                    //if (useSpeach) mainForm.speaker.speak("Go!");
                    mainForm.raceTime = Calendar.getInstance().getTimeInMillis();
                    preapreTimeMachineToRace();
                    raceTimer.start();
                    FIRST_RACER_IS_FINISHED = false;
                    jTree.updateUI();
                    refreshTable();
                    Timer t4 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                      public void actionPerformed(ActionEvent e) {
                        InfoForm.init(mainForm, "").setVisible(false);
                      }
                    });
                    t4.setRepeats(false);
                    t4.start();
                  }
                });
                t3.setRepeats(false);
                t3.start();
              }
            });
            t2.setRepeats(false);
            t2.start();
          }
        });
        t1.setRepeats(false);
        t1.start();
      } else {
        InfoForm.init(mainForm, "!!!").setVisible(true);
        mainForm.beep.palyAndWait("attention");
        mainForm.activeGroup = td.group;
        int rnd = (int) (Math.random() * 3000);
        Timer t3 = new Timer(3000 + rnd, new ActionListener() {      // Timer 3-6 seconds
          public void actionPerformed(ActionEvent e) {
            Runtime runtime = Runtime.getRuntime();
            try {
              Process p = runtime.exec("run.cmd");
              int exitCode = p.waitFor();
            } catch (Exception rt_e) {
              MainForm._toLog(rt_e);
            }
            InfoForm.init(mainForm, "Go!").setVisible(true);
            mainForm.beep.paly("beep");
            //if (useSpeach) mainForm.speaker.speak("Go!");
            mainForm.raceTime = Calendar.getInstance().getTimeInMillis();
            preapreTimeMachineToRace();
            raceTimer.start();
            FIRST_RACER_IS_FINISHED = false;
            jTree.updateUI();
            Timer t4 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
              public void actionPerformed(ActionEvent e) {
                InfoForm.init(mainForm, "").setVisible(false);
              }
            });
            t4.setRepeats(false);
            t4.start();
          }
        });
        t3.setRepeats(false);
        t3.start();
      }
    }
    pleasuUpdateTable = true;
    return message;
  }

  public String startSearchAction(long GROUP_NUM, boolean showDialog) {
    String message = "";
    if (mainForm.vsTimeConnector == null || !mainForm.vsTimeConnector.connected) {
      message = "Transponder hub device is not connected.\nThis function is not been activated.";
      if (showDialog) {
        JOptionPane.showMessageDialog(StageTab.this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
      }
      return message;
    }

    StageTableData td = getGroupByNum(GROUP_NUM);
    if (td == null) {
      return "Group is not found";
    }

    if (mainForm.activeGroup != null && mainForm.activeGroup != td.group) {
      message = "Please stop race. Group" + mainForm.activeGroup.GROUP_NUM;
      if (showDialog) {
        JOptionPane.showMessageDialog(mainForm, message, "Information", JOptionPane.INFORMATION_MESSAGE);
      }
      return message;
    }
    mainForm.lap_log.writeFile("---==  Start Search  ==---;" + stage.CAPTION + " [" + stage.ID + "];Group;" + td.group.GROUP_NUM);
    mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().findTransponders(td.group.GROUP_NUM));
    mainForm.vsTimeConnector.clearTransponderSearchQueue();
    checkingGrpup = td.group;
    checkingGrpup.stageTab = this;
    mainForm.lastCheckingGrpup = checkingGrpup;
    for (VS_STAGE_GROUPS user : checkingGrpup.users) {
      user.CHECK_FOR_RACE = 2;
      user.FIRST_LAP = 0;
      user.RECEIVED_LAPS = false;
      user.color = VSColor.getColorForChannel(user.CHANNEL, stage.CHANNELS, stage.COLORS);
      try {
        user.registration = null;
        user.loadRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
        user.VS_PRIMARY_TRANS = user.registration.VS_TRANS1;
      } catch (Exception ein) {
      }
    }
    pleasuUpdateTable = true;
    pleaseMakeYelowPilot = true; // to reset Lap Checker for pilot, the second row will be yellow
    checkerCycle = 0;
    checkerTimer.start();
    return message;
  }

  public String invateAction(long GROUP_NUM, boolean showDialog) {
    String message = "";
    StageTableData td = getGroupByNum(GROUP_NUM);
    if (td == null) {
      return "Group is not found";
    }
    preapreTimeMachineToRace();
    if (mainForm.activeGroup != null && mainForm.activeGroup != td.group) {
      message = "Please stop race. Group" + mainForm.activeGroup.GROUP_NUM;
      if (showDialog) {
        JOptionPane.showMessageDialog(mainForm, message, "Information", JOptionPane.INFORMATION_MESSAGE);
      }
      return message;
    }
    List<String> pilots = new ArrayList<String>();
    if (td != null && td.group != null && td.group.users != null) {
      mainForm.lap_log.writeFile("---==  Invate  ==---;" + stage.CAPTION + " [" + stage.ID + "];Group;" + td.group.GROUP_NUM);
      mainForm.lastInvateGroup = td.group;
      for (VS_STAGE_GROUPS user : td.group.users) {
        VS_REGISTRATION reg = user.getRegistration(mainForm.con, mainForm.activeRace.RACE_ID);
        String pilot = user.PILOT;
        if (reg != null) {
          pilot = reg.FIRST_NAME + " " + reg.SECOND_NAME;
        }
        if (pilot.trim().equals("")) {
          pilot = user.PILOT;
        }
        pilots.add(pilot);
        user.color = VSColor.getColorForChannel(user.CHANNEL, stage.CHANNELS, stage.COLORS);
      }
      mainForm.invateGroup = td.group;
      mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().invatieGroup(td.group.GROUP_NUM, pilots));
    }
    try {
      if (mainForm.vsTimeConnector != null && mainForm.vsTimeConnector.connected) {
        mainForm.vsTimeConnector.setTime();
      }
    } catch (Exception ein) {
    }
    return message;
  }

  public static int[] createGroupsAlg(int count_groups) {
    int[] GROUP_ALG = null;
    if (count_groups % 2 == 0) {
      GROUP_ALG = new int[count_groups];
      boolean flag_to_end = false;
      for (int i = 0; i < count_groups / 2; i++) {
        if (!flag_to_end) {
          //0 => 0,1        2 => 2,3
          GROUP_ALG[i] = i + 1;
          GROUP_ALG[i + 1] = count_groups - i;
        } else {
          // 1 - last-1, last-2,  2=>    
          GROUP_ALG[count_groups - (i) - 1] = i + 1;
          GROUP_ALG[count_groups - (i)] = count_groups - i;
        }
        flag_to_end = !flag_to_end;
      }
    }
    return GROUP_ALG;
  }

  public static void printGroup(int[] groups) {
    System.out.print("Count group:" + groups.length + "   ");
    for (int i = 0; i < groups.length / 2; i++) {
      System.out.print(groups[i * 2] + "-" + groups[i * 2 + 1] + "   ");
    }
    System.out.println("");
  }

  public static void main(String[] args) {
    printGroup(createGroupsAlg(2));
    printGroup(createGroupsAlg(4));
    printGroup(createGroupsAlg(6));
    printGroup(createGroupsAlg(8));
    printGroup(createGroupsAlg(10));
    printGroup(createGroupsAlg(16));
    printGroup(createGroupsAlg(32));
    printGroup(createGroupsAlg(64));
  }
}
