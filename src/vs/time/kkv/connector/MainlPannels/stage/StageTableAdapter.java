/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.Utils.UserException;
import static com.lowagie.text.pdf.BidiOrder.R;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import vs.time.kkv.models.VS_USERS;

import java.awt.Color;
import java.awt.Component;
import static java.awt.SystemColor.text;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.connector.web.TVTranslationServlet;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import static vs.time.kkv.models.VS_STAGE_GROUP.GROUP_TIME_COMPARATOR;

/**
 *
 * @author kyo
 */
public class StageTableAdapter extends AbstractTableModel implements TableCellRenderer {

  public List<StageTableData> rows = null;
  private StageTab tab = null;
  private DefaultTableCellRenderer defaultTableCellRendererCellRenderer = new DefaultTableCellRenderer();

  public static boolean SHOW_CHECK_RACE_BUTTON = true;

  public StageTableData getTableData(int row) {
    if (row < rows.size()) {
      return rows.get(row);
    }
    return null;
  }

  public boolean show_laps = true;
  public boolean show_grups = true;

  final static int LAP_WEIGHT = 90;
  public static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_CHANNEL, "Channel", 80),
    //new STAGE_COLUMN(STAGE_COLUMN.CID_REG_ID, "Reg ID", 50), 
    new STAGE_COLUMN(STAGE_COLUMN.CID_TRANS, "Trans", 50),  
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 100).setIsEditing(true).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SPEED, "Speed", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),};

  public static STAGE_COLUMN[] STAGE_COLUMNS_STAGE_RACE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_CHANNEL, "Channel", 80),
    //new STAGE_COLUMN(STAGE_COLUMN.CID_REG_ID, "Reg ID", 50),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TRANS, "Trans", 50),    
    new STAGE_COLUMN(STAGE_COLUMN.CID_SCORE, "Score", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_WIN, "Win", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LOSS, "Loss", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 100).setIsEditing(true).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SPEED, "Speed", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),};

  public static STAGE_COLUMN[] STAGE_COLUMNS_RESULT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 70).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SPEED, "Speed", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_QUAL_STATUS, "Status", 150),};

  public static STAGE_COLUMN[] STAGE_COLUMNS_REPORT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Place", 70).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_REGION, "Region", 200),
    new STAGE_COLUMN(STAGE_COLUMN.CID_FAI, "FAI", 100),
    //new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Best\nRace Time", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_QUAL_TIME, "Qualification\ntime", 140).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_QUAL_POS, "Qualification\nposition", 140).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_RACE_TIME_QUART_FINAL, "Quarter-Finals\nrace time", 140).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_RACE_TIME_HALF_FINAL, "Semifinal\nrace time", 140).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_RACE_TIME_FINAL, "The final\nrace time", 140).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SCORE, "Score", 50).setCellID("INT"),};

  public static STAGE_COLUMN[] STAGE_COLUMNS_RACE_RESULT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SCORE, "Score", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_WIN, "Win", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LOSS, "Loss", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SPEED, "Speed", 100).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_RACE_STATUS, "Status", 150),};

  public String getColumnCellID(int column) {
    String cellID = "TXT_RIGHT";
    List<STAGE_COLUMN> columns = getColumns();
    if (column < columns.size()) {
      cellID = columns.get(column).cellID;
    }
    return cellID;
  }

  public static STAGE_COLUMN[] getDeafultColumns(int STAGE_TYPE) {
    STAGE_COLUMN[] pattern = null;
    if (STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      pattern = STAGE_COLUMNS_RESULT;
    } else if (STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
      pattern = STAGE_COLUMNS_REPORT;
    } else if (STAGE_TYPE == MainForm.STAGE_RACE_RESULT) {
      pattern = STAGE_COLUMNS_RACE_RESULT;
    } else if (STAGE_TYPE == MainForm.STAGE_RACE) {
      pattern = STAGE_COLUMNS_STAGE_RACE;
    } else {
      pattern = STAGE_COLUMNS_STAGE;
    }
    return pattern;
  }

  List<STAGE_COLUMN> tabColumns = null;

  public List<STAGE_COLUMN> getColumns() {
    if (tabColumns != null) {
      return tabColumns;
    }
    STAGE_COLUMN[] pattern = getDeafultColumns(tab.stage.STAGE_TYPE);
    String[] colsInfo = tab.stage.REP_COLS.split(";");
    tabColumns = new ArrayList<STAGE_COLUMN>();
    int col_index = 0;
    for (STAGE_COLUMN col : pattern) {
      boolean please_add = true;
      if (colsInfo != null && col_index < colsInfo.length) {
        if ("0".equals(colsInfo[col_index])) {
          please_add = false;
        }
      }
      if (please_add) {
        tabColumns.add(col);
      }
      col_index++;
    }

    return tabColumns;
  }

  public StageTableAdapter(StageTab tab) {
    this.tab = tab;
    if (tab.isOneTable) {
      show_grups = false;
      show_laps = false;
    }
    loadData();
    defaultTableCellRendererCellRenderer.setOpaque(true);
  }

  public boolean isQualificated(VS_STAGE_GROUPS pilot) {
    if (pilot.NUM_IN_GROUP <= tab.stage.PILOTS_FOR_NEXT_ROUND || tab.stage.PILOTS_FOR_NEXT_ROUND == -1) {
      return true;
    }
    return false;
  }

  public boolean isOut(VS_STAGE_GROUPS pilot) {
    if (!(pilot.NUM_IN_GROUP <= tab.stage.PILOTS_FOR_NEXT_ROUND || tab.stage.PILOTS_FOR_NEXT_ROUND == -1)) {
      return true;
    }
    if (pilot.LOSE >= 2) {
      return true;
    }
    return false;
  }

  public void loadData() {
    rows = new ArrayList<StageTableData>();
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      //List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GID", parent_stage.ID);          
      try {
        String order_by = "BEST_LAP";
        if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_RACE_TIME) {
          order_by = "RACE_TIME, BEST_LAP";
        }
        List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID IN (SELECT stage.id from VS_STAGE as stage where stage.RACE_ID=? and stage.STAGE_TYPE=?) order by " + order_by, tab.stage.RACE_ID, MainForm.STAGE_QUALIFICATION);
        HashMap<String, VS_STAGE_GROUPS> pilots = new HashMap<String, VS_STAGE_GROUPS>();
        Map<String, VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getMap(tab.mainForm.con, "VS_TRANS1", "VS_RACE_ID=?", tab.stage.RACE_ID);

        int index = 1;
        VS_STAGE_GROUPS.dbControl.delete(tab.mainForm.con, "STAGE_ID=?", tab.stage.ID);

        for (VS_STAGE_GROUPS pilot : groups) {
          //VS_REGISTRATION reg = regs.get("" + pilot.VS_PRIMARY_TRANS);
          VS_REGISTRATION reg = pilot.getRegistration(tab.mainForm.con, tab.stage.RACE_ID);
          if (reg != null) {
            pilot.PILOT = reg.VS_USER_NAME;
            pilot.PILOT_TYPE = reg.PILOT_TYPE;
          }

          if (tab.stage.PILOT_TYPE == MainForm.PILOT_TYPE_NONE_INDEX || tab.stage.PILOT_TYPE == pilot.PILOT_TYPE) {
            if (pilots.get(pilot.PILOT) == null) {
              pilot.NUM_IN_GROUP = index;
              pilot.GROUP_NUM = 1;
              //pilot.IS_FINISHED = 1;
              pilot.IS_RECALULATED = 1;

              rows.add(new StageTableData(pilot));
              index++;

              pilot.GID = -1;
              pilot.STAGE_ID = tab.stage.ID;
              pilots.put(pilot.PILOT, pilot);
            } else {
              VS_STAGE_GROUPS pilot1 = pilots.get(pilot.PILOT);
              if (pilot.BEST_LAP != 0 && pilot.BEST_LAP < pilot1.BEST_LAP) {
                pilot1.BEST_LAP = pilot.BEST_LAP;                
              }
              pilot1.LAPS += pilot.LAPS;
            }
          }
        }
        for (VS_STAGE_GROUPS pilot : pilots.values()) {
          if (isQualificated(pilot)) {
            pilot.ACTIVE_FOR_NEXT_STAGE = 1;
          } else {
            pilot.ACTIVE_FOR_NEXT_STAGE = 0;
          }
          VS_STAGE_GROUPS.dbControl.insert(tab.mainForm.con, pilot);
        }

      } catch (Exception ein) {
        MainForm._toLog(ein);
      }
    } else if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT) {
      //List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GID", parent_stage.ID);          
      try {
        VS_STAGE_GROUPS.dbControl.delete(tab.mainForm.con, "STAGE_ID=?", tab.stage.ID);

        long union_stage_id = tab.stage.PARENT_STAGE_ID;
        VS_STAGE parent_stage = null;
        try {
          parent_stage = VS_STAGE.dbControl.getItem(tab.mainForm.con, "ID=? and RACE_ID=?", union_stage_id, tab.stage.RACE_ID);
          if (parent_stage != null && parent_stage.STAGE_TYPE == MainForm.STAGE_RACE) {
            // Собирать все Raсe у которых PARENT_STAGE_ID 
            union_stage_id = parent_stage.PARENT_STAGE_ID;
          }
        } catch (Exception e) {
        }

        // get all results        
        List<VS_STAGE_GROUPS> all_groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID IN (SELECT stage.id from VS_STAGE as stage where stage.STAGE_TYPE=? and stage.PARENT_STAGE_ID=? and stage.RACE_ID=?) ", MainForm.STAGE_RACE, union_stage_id, tab.stage.RACE_ID);
        List<VS_STAGE_GROUPS> results = new ArrayList<VS_STAGE_GROUPS>();
        HashMap<String, VS_STAGE_GROUPS> pilots = new HashMap<String, VS_STAGE_GROUPS>();
        Map<String, VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getMap(tab.mainForm.con, "VS_TRANS1", "VS_RACE_ID=?", tab.stage.RACE_ID);

        rows = new ArrayList<StageTableData>();
        int index = 1;
        VS_STAGE_GROUPS.dbControl.delete(tab.mainForm.con, "STAGE_ID=?", tab.stage.ID);
        for (VS_STAGE_GROUPS pilot : all_groups) {
          //VS_REGISTRATION reg = regs.get("" + pilot.TRANSPONDER);
          VS_REGISTRATION reg = pilot.getRegistration(tab.mainForm.con, tab.stage.RACE_ID);
          if (reg != null) {
            pilot.PILOT = reg.VS_USER_NAME;
            pilot.REG_ID = reg.ID;
            pilot.PILOT_TYPE = reg.PILOT_TYPE;
          }
          VS_STAGE_GROUPS result = pilots.get(pilot.PILOT);
          if (result == null) {
            pilot.NUM_IN_GROUP = index;
            //pilot.GROUP_FINAL = pilot.GROUP_NUM;
            pilot.IS_FINISHED = 1;
            pilot.IS_RECALULATED = 1;
            pilot.GID = -1;
            pilot.STAGE_ID = tab.stage.ID;
            //pilot.recalculateLapTimes(tab.mainForm.con, tab.stage, true);
            result = pilot;
            pilots.put(pilot.PILOT, result);
          } else {
            result.SCORE += pilot.SCORE;
          }

          if (result.BEST_LAP == 0) {
            result.BEST_LAP = pilot.BEST_LAP;
          }
          if (pilot.BEST_LAP != 0 && pilot.BEST_LAP < result.BEST_LAP) {
            result.BEST_LAP = pilot.BEST_LAP;
          }
          if (result.RACE_TIME == 0) {
            result.RACE_TIME = pilot.RACE_TIME;
          }
          if (pilot.RACE_TIME != 0 && pilot.RACE_TIME < result.RACE_TIME) {
            result.RACE_TIME = pilot.RACE_TIME;
          }
          if (result!=pilot){
            result.wins += pilot.WIN;
            result.loses += pilot.LOSE;
            result.LAPS += pilot.LAPS;
          }  

        }

        results.clear();
        for (VS_STAGE_GROUPS pilot : pilots.values()) {
          results.add(pilot);
        }
        if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_LOSS_DESC) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_LOST_COMPARATOR);
        } else if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_SCORE_DESC) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_SCORE_COMPARATOR);
        } else if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_LAPS) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_LAPS_COMPARATOR);
        }else if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_LAPS_AND_SCORES) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_LAPS_AND_SCORES_COMPARATOR);
        } else {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_TIME_COMPARATOR);
        }
        index = 1;
        for (VS_STAGE_GROUPS pilot : results) {
          pilot.GROUP_FINAL = pilot.GROUP_NUM;
          pilot.GROUP_NUM = 1;
          pilot.GID = -1;
          pilot.NUM_IN_GROUP = index;
          pilot.WIN = pilot.wins;
          pilot.LOSE = pilot.loses;
          pilot.STAGE_ID = tab.stage.ID;
          pilot.IS_RECALULATED = 1;
          pilot.IS_FINISHED = 1;
          pilot.STAGE_ID = tab.stage.ID;
          index++;
          try {
            if (isQualificated(pilot)) {
              pilot.ACTIVE_FOR_NEXT_STAGE = 1;
            } else {
              pilot.ACTIVE_FOR_NEXT_STAGE = 0;
            }
            VS_STAGE_GROUPS.dbControl.insert(tab.mainForm.con, pilot);
          } catch (Exception e) {
            tab.mainForm._toLog(e);
          }
          rows.add(new StageTableData(pilot));
        }
      } catch (Exception ein) {
        MainForm._toLog(ein);
      }
    } else if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {
      try {
        int NUM_IN_GROUP = 1;
        VS_STAGE_GROUPS.dbControl.delete(tab.mainForm.con, "STAGE_ID=?", tab.stage.ID);
        List<VS_STAGE> stages = VS_STAGE.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_TYPE in (" + MainForm.STAGE_QUALIFICATION_RESULT + "," + MainForm.STAGE_RACE_RESULT + ") order by ID desc", tab.stage.RACE_ID);        
        long stage_id_final = 0;        
        if (stages.size()==2){
          // If Olimpic System, one Qulification and One Race Result, then read all groups
          List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID=? order by STAGE_ID, GROUP_NUM, NUM_IN_GROUP", stages.get(1).ID);
          if (groups.size()>=16){ // if count pilots>16
            List<VS_STAGE> races = VS_STAGE.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_TYPE in (" + MainForm.STAGE_RACE + ") order by ID desc", tab.stage.RACE_ID);
            int count = 0;
            // First Race = Race Result
            for (VS_STAGE race : races){             
              if (count!=0 && count<=3){
                stages.add(count, race);
              }  
              count++;
            }          
          }
        }
        
        if (stages.size()==1 && stages.get(0).STAGE_TYPE==MainForm.STAGE_QUALIFICATION_RESULT){
          // If Olimpic System, one Qulification and One Race Result, then read all groups
          List<VS_STAGE_GROUPS> groups = null;
          groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID=? order by STAGE_ID, GROUP_NUM, NUM_IN_GROUP", stages.get(0).ID);          
          if (groups.size()>=16){ // if count pilots>16
            List<VS_STAGE> races = VS_STAGE.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_TYPE in (" + MainForm.STAGE_RACE + ") order by ID desc", tab.stage.RACE_ID);
            int count = 0;
            // First Race = Race Result
            for (VS_STAGE race : races){             
              if (count<3){
                stages.add(count, race);
              }  
              count++;
            }          
          }else{
            List<VS_STAGE> races = VS_STAGE.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_TYPE in (" + MainForm.STAGE_RACE + ") order by ID desc", tab.stage.RACE_ID);
            int count = 0;
            // First Race = Race Result
            for (VS_STAGE race : races){             
              if (count<1){
                stages.add(count, race);
              }  
              count++;
            }          
          }          
        }        
        if (stages.size() > 0) {
          stage_id_final = stages.get(0).ID;
        }
        long stage_id_half_final = 0;
        if ((stages.size()-1) > 1) {
          stage_id_half_final = stages.get(1).ID;
        }
        long stage_id_qart_final = 0;
        if ((stages.size()-1) > 2) {
          stage_id_qart_final = stages.get(2).ID;
        }                
        List<VS_STAGE_GROUPS> results = new ArrayList<VS_STAGE_GROUPS>();
        for (VS_STAGE stage : stages) {
          List<VS_STAGE_GROUPS> groups = null;
          if (stage.STAGE_TYPE==MainForm.STAGE_QUALIFICATION_RESULT || stage.STAGE_TYPE==MainForm.STAGE_RACE_REPORT){
            groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID=? order by STAGE_ID, GROUP_NUM, NUM_IN_GROUP", stage.ID);
          }else{
            groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID=? order by STAGE_ID, LAPS desc, RACE_TIME", stage.ID);         
          }
          
          for (VS_STAGE_GROUPS usr : groups) {
            VS_STAGE_GROUPS res = null;
            for (VS_STAGE_GROUPS result : results) {
              if (result.PILOT.equalsIgnoreCase(usr.PILOT)) {
                res = result;
                break;
              }
            }
            if (res == null) {
              res = (VS_STAGE_GROUPS) VS_STAGE_GROUPS.dbControl.copyObject(usr);
              res.STAGE_ID = tab.stage.ID;
              res.GROUP_NUM = 1;
              res.NUM_IN_GROUP = NUM_IN_GROUP;
              res.IS_FINISHED = 1;
              res.IS_RECALULATED = 1;
              NUM_IN_GROUP++;
              results.add(res);
            }
            if (stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
              res.QUAL_POS = usr.NUM_IN_GROUP;
              res.QUAL_TIME = usr.RACE_TIME;
            }
            if (usr.RACE_TIME != 0 && res.RACE_TIME > usr.RACE_TIME) {
              res.RACE_TIME = usr.RACE_TIME;
            }
            if (usr.BEST_LAP != 0 && res.BEST_LAP > usr.BEST_LAP) {
              res.BEST_LAP = usr.BEST_LAP;
            }
            if (stage.ID == stage_id_final) {
              res.RACE_TIME_FINAL = usr.RACE_TIME;
              res.GROUP_FINAL = usr.GROUP_FINAL;
              if (usr.GROUP_NUM!=0) res.GROUP_FINAL = usr.GROUP_NUM;
            }
            if (stage.ID == stage_id_half_final) {
              res.RACE_TIME_HALF_FINAL = usr.RACE_TIME;
              res.GROUP_HALF_FINAL = usr.GROUP_FINAL;
              if (usr.GROUP_NUM!=0) res.GROUP_HALF_FINAL = usr.GROUP_NUM;
            }
            if (stage.ID == stage_id_qart_final) {
              res.RACE_TIME_QUART_FINAL = usr.RACE_TIME;
              res.GROUP_QUART_FINAL = usr.GROUP_FINAL;
              if (usr.GROUP_NUM!=0) res.GROUP_QUART_FINAL = usr.GROUP_NUM;
            }
          }
        }

        int SCORE = 40;
        if (results.size() < 40) {
          SCORE = results.size();
        }
        int MAX_SCORE = SCORE;

        for (VS_STAGE_GROUPS pilot : results) {
          if (SCORE > 0) {
            pilot.SCORE = SCORE;
          }
          if (pilot.NUM_IN_GROUP == 1) {
            pilot.SCORE += Math.round(MAX_SCORE/5);
          }
          if (pilot.NUM_IN_GROUP == 2) {
            pilot.SCORE += Math.round(MAX_SCORE/8);
          }
          if (pilot.NUM_IN_GROUP == 3) {
            pilot.SCORE += Math.round(MAX_SCORE/13);
          }
          SCORE--;
          VS_STAGE_GROUPS.dbControl.insert(tab.mainForm.con, pilot);
          rows.add(new StageTableData(pilot));
        }
      } catch (Exception ein) {

        MainForm._toLog(ein);
      }
    } else {
      if (tab.stage.ID == 114) {
        int y = 0;
      }
      for (Integer groupId : tab.stage.groups.keySet()) {
        VS_STAGE_GROUP group = tab.stage.groups.get(groupId);
        rows.add(new StageTableData(group));
        for (VS_STAGE_GROUPS pilot : group.users) {
          //if (pilot.IS_FINISHED==0) {
          pilot.IS_RECALULATED = 0;
          //}
          //   pilot.IS_RECALULATED = 0;
          // }  
          rows.add(new StageTableData(pilot));
        }
      }
      try {
        if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
          tab.stage.laps_check_reg_id = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "REG_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
        } else {
          tab.stage.laps_check_reg_id = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
        }
      } catch (Exception e) {
      }
      for (Integer groupId : tab.stage.groups.keySet()) {
        VS_STAGE_GROUP group = tab.stage.groups.get(groupId);
        for (VS_STAGE_GROUPS pilot : group.users) {
          pilot.recalculateLapTimes(tab.mainForm.con, tab.stage, false);
        }
      }
    }
    //List<VS_RACE_LAP> laps = VS_RACE_LAP.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_ID=?", STAGE_COLUMNS);
  }

  public int getMinWidth(int col) {
    if (col < getColumns().size()) {
      return getColumns().get(col).width;
    }
    return LAP_WEIGHT;
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }
  
  int laps_count = 0;
  
  @Override
  public int getColumnCount() {
    int laps = tab.stage.LAPS;
    if (show_laps) {
      for (StageTableData row : rows) {
        if (!row.isGrpup) {
          if (laps < row.pilot.LAPS_INTO_BD) {
            laps = (int) row.pilot.LAPS_INTO_BD;
          }
        }
      }
    }
    laps_count = laps;
    return getColumns().size() + (show_laps ? laps : 0);
  }

  @Override
  public String getColumnName(int columnIndex) {
    if (columnIndex < getColumns().size()) {
      return getColumns().get(columnIndex).caption;
    }
    return tab.mainForm.getLocaleString("Lap") + " " + (columnIndex - getColumns().size() + 1);
  }
  
  public String getColumnLocaleName(int columnIndex) {
    if (columnIndex < getColumns().size()) {
      return getColumns().get(columnIndex).captionOriginal;
    }
    return "Lap" + " " + (columnIndex - getColumns().size() + 1);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (rows.size() > rowIndex) {
      StageTableData td = rows.get(rowIndex);
      if (td.isGrpup) {
        if (columnIndex == 0) {
          String addon = "";      
          if (td.group.users!=null && td.group.users.size()>0){
            if ( td.group.users.get(0).IS_PANDING==1){
              addon = " waiting for losers";
            }
          }      
          return " -= " + tab.mainForm.getLocaleString("Group") + " " + td.group.GROUP_NUM + " =- "+addon;
        }
        return "";
      } else {
        STAGE_COLUMN sc = null;
        if (columnIndex < getColumns().size()) {
          sc = getColumns().get(columnIndex);
        } else {
          // lap time
          VS_RACE_LAP lap = null;
          try {
            if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
              lap = tab.stage.laps_check_reg_id.get("" + td.pilot.GROUP_NUM).get("" + td.pilot.REG_ID).get("" + getLapNumberFromCol(columnIndex));
            } else {
              lap = tab.stage.laps_check_reg_id.get("" + td.pilot.GROUP_NUM).get("" + td.pilot.VS_PRIMARY_TRANS).get("" + getLapNumberFromCol(columnIndex));
            }
          } catch (Exception e) {
          }
          if (lap == null) {
            return "";
          }
          return tab.getTimeIntervel(lap.TRANSPONDER_TIME);
        }
                
        if (sc != null && sc.ID == STAGE_COLUMN.CID_TRANS && td.pilot!=null) {
           //return "[ " + td.pilot.VS_PRIMARY_TRANS +" ]";
           return td.pilot.VS_PRIMARY_TRANS;
        }
        
        if (sc != null && sc.ID == STAGE_COLUMN.CID_PILOT) {
          String addon = "";
          try {
            VS_REGISTRATION reg = td.pilot.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
            if (reg != null) {
              addon = " / " + reg.FIRST_NAME + " " + reg.SECOND_NAME;
            }
          } catch (Exception e) {
          }
          String addon2 = "";
          if (tab.stage.STAGE_TYPE==MainForm.STAGE_RACE && tab.stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE){
            if (td.pilot.GROUP_TYPE==0) addon2 = "★ ";
            else addon2 = "☟ ";
          }
          return addon2 + td.pilot.PILOT + addon;
        }
        
        if (sc != null && sc.ID == STAGE_COLUMN.CID_REG_ID) {          
          return "[ " + td.pilot.REG_ID +" ]";
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_REGION) {
          try {
            VS_REGISTRATION reg = td.pilot.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
            if (reg != null) {
              if (reg.REGION.equalsIgnoreCase("none")) {
                return "";
              }
              return reg.REGION;
            }
          } catch (Exception e) {
          }
          return "";
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_FAI) {
          try {
            VS_REGISTRATION reg = td.pilot.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
            if (reg != null) {
              if (reg.FAI.equalsIgnoreCase("none")) {
                return "";
              }
              return reg.FAI;
            }
          } catch (Exception e) {
          }
          return "";
        }

        long cols = td.pilot.recalculateLapTimes(tab.mainForm.con, tab.stage, false);
        if (cols>laps_count) tab.fireStructChange();

        if (sc != null && sc.ID == STAGE_COLUMN.CID_BEST_LAP) {
          if (td.pilot.BEST_LAP == 0) {
            return "";
          }
          return tab.getTimeIntervel(td.pilot.BEST_LAP);
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_PILOT_NUM) {
          return td.pilot.NUM_IN_GROUP;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_PILOT_TYPE) {
          try {
            return MainForm.PILOT_TYPES[td.pilot.PILOT_TYPE];
          } catch (Exception e) {
            return tab.mainForm.getLocaleString("NONE");
          }
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_LAPS) {
          if (td.pilot.LAPS == 0 && td.pilot.IS_FINISHED == 0) {
            return "";
          }
          return td.pilot.LAPS;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_TIME) {
          if (td.pilot.RACE_TIME == 0) {
            return "";
          }
          return tab.getTimeIntervel(td.pilot.RACE_TIME);
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_CHANNEL) {
          return td.pilot.CHANNEL;
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_QUAL_STATUS) {
          if (isQualificated(td.pilot)) {
            return tab.mainForm.getLocaleString("qualified");
          } else {
            return tab.mainForm.getLocaleString("not qualified");
          }
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_RACE_STATUS) {
          if (isOut(td.pilot)) {
            return tab.mainForm.getLocaleString("out");
          } else {
            return "";
          }
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_SCORE) {
          return td.pilot.SCORE;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_WIN) {
          return td.pilot.WIN;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_LOSS) {
          return td.pilot.LOSE;
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_QUAL_POS) {
          if (td.pilot.QUAL_POS == 0) {
            return "";
          }
          return td.pilot.QUAL_POS;
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_QUAL_TIME) {
          if (td.pilot.QUAL_TIME == 0) {
            return "";
          }
          return tab.getTimeIntervel(td.pilot.QUAL_TIME);
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_RACE_TIME_QUART_FINAL) {
          if (td.pilot.RACE_TIME_QUART_FINAL == 0) {
            return "";
          }
          String addon = "";
          if (td.pilot.GROUP_QUART_FINAL != 0) {
            addon = " (" + (char) ('A' + (td.pilot.GROUP_QUART_FINAL - 1)) + ")";
          }
          return tab.getTimeIntervel(td.pilot.RACE_TIME_QUART_FINAL) + addon;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_RACE_TIME_HALF_FINAL) {
          if (td.pilot.RACE_TIME_HALF_FINAL == 0) {
            return "";
          }
          String addon = "";
          if (td.pilot.GROUP_HALF_FINAL != 0) {
            addon = " (" + (char) ('A' + (td.pilot.GROUP_HALF_FINAL - 1)) + ")";
          }
          return tab.getTimeIntervel(td.pilot.RACE_TIME_HALF_FINAL) + addon;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_RACE_TIME_FINAL) {
          if (td.pilot.RACE_TIME_FINAL == 0) {
            return "";
          }
          String addon = "";
          if (td.pilot.GROUP_FINAL != 0) {
            addon = " (" + (char) ('A' + (td.pilot.GROUP_FINAL - 1)) + ")";
          }
          return tab.getTimeIntervel(td.pilot.RACE_TIME_FINAL) + addon;
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_SPEED) {
          if (td.pilot.BEST_LAP == 0) {
            return "";
          }
          String val = StageTab.getFlightSpeed(tab.mainForm.activeRace ,  td.pilot.BEST_LAP);
          if (!val.equalsIgnoreCase("")) val += " "+tab.mainForm.getLocaleString("km/h");
          return val;
        }
      }
    }
    return "";
  }

  public boolean isCellEditable(int row, int col) {
    if (tab.stage.SORT_TYPE == MainForm.STAGE_RACE_RESULT) {
      return false;
    }
    if (tab.stage.IS_LOCK == 1) {
      return false;
    }
    if (tab.mainForm.activeGroup != null) {
      return false;
    }
    StageTableData td = rows.get(row);
    if (td.isGrpup) {
      //if (col==1) return true;
      return false;
    }
    STAGE_COLUMN sc = null;
    if (col < getColumns().size()) {
      sc = getColumns().get(col);
    }
    if (col >= getColumns().size()) {
      return true;
    }
    if (sc != null) {
      if (sc.isEditing) {
        return true;
      }
      if (sc.ID == STAGE_COLUMN.CID_SCORE || sc.ID == STAGE_COLUMN.CID_WIN || sc.ID == STAGE_COLUMN.CID_LOSS) {
        return true;
      }
    }
    return false;
  }

  public void showEditDialog(int row) {
    if (row < rows.size() && row >= 0) {
      //  VS_RACE race = rows.get(row);      
      //  RaceControlForm.init(mainForm, race.RACE_ID).setVisible(true);
    }
  }

  public long getTimerInterval(String value) {
    long result = -1;
    try {
      String val = value == null ? "" : value.toString();
      if (!val.equals("")) {
        int pos1 = val.lastIndexOf(":");
        if (pos1 > 0) {
          String ms_st = val.substring(pos1 + 1);
          long ms = Long.parseLong(ms_st);
          val = val.substring(0, pos1);
          pos1 = val.lastIndexOf(":");
          if (pos1 > 0) {
            // Forma DD:DD:DD
            String sec_st = val.substring(pos1 + 1);
            long sec = Long.parseLong(sec_st);
            val = val.substring(0, pos1);
            long min = Long.parseLong(val);
            result = (min * 60 + sec) * 1000 + ms;
          } else {
            // format DD:DD
            long min = Long.parseLong(val);
            long sec = ms;
            ms = 0;
            result = (min * 60 + sec) * 1000 + ms;
          }
        }
      } else {
        result = -2;
      }
    } catch (Exception e) {

    }
    return result;
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    StageTableData td = rows.get(row);
    STAGE_COLUMN sc = null;
    if (col < getColumns().size()) {
      sc = getColumns().get(col);
    }
    if (sc != null && sc.ID == STAGE_COLUMN.CID_SCORE && !td.isGrpup) {
      try {
        td.pilot.SCORE = Integer.parseInt("" + value);
        //System.out.println("VS_STAGE_GROUPS - sat value0");
        VS_STAGE_GROUPS.dbControl.update(tab.mainForm.con, td.pilot);
      } catch (Exception e) {
      }
    }
    if (sc != null && sc.ID == STAGE_COLUMN.CID_WIN && !td.isGrpup) {
      try {
        int res = 0;
        try {
          res = Integer.parseInt("" + value);
        } catch (Exception e) {
        }
        td.pilot.WIN = res >= 1 ? 1 : 0;
        if (td.pilot.WIN == 1) {
          td.pilot.LOSE = 0;
        } else {
          td.pilot.LOSE = 1;
        }
        //System.out.println("VS_STAGE_GROUPS - sat value2");
        VS_STAGE_GROUPS.dbControl.update(tab.mainForm.con, td.pilot);
      } catch (Exception e) {
      }
    }
    if (sc != null && sc.ID == STAGE_COLUMN.CID_LOSS && !td.isGrpup) {
      try {
        int res = 0;
        try {
          res = Integer.parseInt("" + value);
        } catch (Exception e) {
        }
        td.pilot.LOSE = res >= 1 ? 1 : 0;
        if (td.pilot.LOSE == 1) {
          td.pilot.WIN = 0;
        } else {
          td.pilot.WIN = 1;
        }
        //System.out.println("VS_STAGE_GROUPS - sat value3");
        VS_STAGE_GROUPS.dbControl.update(tab.mainForm.con, td.pilot);
      } catch (Exception e) {
      }
    }
    if (col >= getColumns().size() && td != null && !td.isGrpup) {
      try {
        // (?<!\\w)\\d+(?!\\w)
        //Pattern p = Pattern.compile("\\d+:[0-5][0-9]:[0-9][0-9]");
        String val = value == null ? "" : value.toString();
        long time = getTimerInterval(val);
        boolean isError = false;
        if (time != -1) {
          int lapNum =  getLapNumberFromCol(col);
          if (time == -2) { // it's sapce = only delete time
            if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
              VS_RACE_LAP.dbControl.delete(tab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and REG_ID=?",tab.stage.RACE_ID, tab.stage.ID,td.pilot.GROUP_NUM,  td.pilot.REG_ID);            
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.REG_ID, "" +lapNum, null);
            } else {
              VS_RACE_LAP.dbControl.delete(tab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and TRANSPONDER_ID=?",tab.stage.RACE_ID, tab.stage.ID,td.pilot.GROUP_NUM,  td.pilot.VS_PRIMARY_TRANS);                          
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.VS_PRIMARY_TRANS, "" + lapNum, null);
            } 
          } else {
            VS_RACE_LAP lap = VS_RACE_LAP.saveTime(tab.mainForm.con, tab.stage, td.pilot.parent, time, td.pilot.VS_PRIMARY_TRANS, td.pilot.REG_ID, lapNum);
            if (lap == null) {
              isError = true;
            }
            if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.REG_ID, "" + lapNum, lap);
            } else {
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.VS_PRIMARY_TRANS, "" + lapNum, lap);
            }
          }
        } else {
          isError = true;
        }
        if (isError) {
          JOptionPane.showMessageDialog(tab, "Please input correct time.\nFormat: 00:00:00", "Input error", JOptionPane.INFORMATION_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(tab, "Please input correct time.\nFormat: 00:00:00", "Input error", JOptionPane.INFORMATION_MESSAGE);
      }
      tab.jTable.updateUI();
    };
    if (sc != null && sc.ID == STAGE_COLUMN.CID_TIME && td != null && !td.isGrpup) {
      try {
        // (?<!\\w)\\d+(?!\\w)
        //Pattern p = Pattern.compile("\\d+:[0-5][0-9]:[0-9][0-9]");
        String val = value == null ? "" : value.toString();
        long time = getTimerInterval(val);
        boolean isError = false;
        if (time != -1) {
          if (tab.stage.USE_REG_ID_FOR_LAP==1){
            VS_RACE_LAP.dbControl.delete(tab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and REG_ID=?",
                  tab.stage.RACE_ID, tab.stage.ID, td.pilot.GROUP_NUM, td.pilot.REG_ID);
          }else{
            VS_RACE_LAP.dbControl.delete(tab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and TRANSPONDER_ID=?",
                  tab.stage.RACE_ID, tab.stage.ID, td.pilot.GROUP_NUM, td.pilot.VS_PRIMARY_TRANS);          
          }  
          long lapTime = time / tab.stage.LAPS;
          for (int i = 0; i < tab.stage.LAPS; i++) {
            if (i + 1 == tab.stage.LAPS) {
              lapTime = time - lapTime * (tab.stage.LAPS - 1);
            }
            VS_RACE_LAP lap = VS_RACE_LAP.saveTime(tab.mainForm.con, tab.stage, td.pilot.parent, lapTime, td.pilot.VS_PRIMARY_TRANS, td.pilot.REG_ID, i + 1);
            if (tab.stage.USE_REG_ID_FOR_LAP == 1) {
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.REG_ID, "" + (i + 1), lap);
            } else {
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps_check_reg_id, "" + td.pilot.GROUP_NUM, "" + td.pilot.VS_PRIMARY_TRANS, "" + (i + 1), lap);
            }
          }
          td.pilot.parent.recalculateScores(tab.mainForm);
        } else {
          isError = true;
        }
        if (isError) {
          JOptionPane.showMessageDialog(tab, "Please input correct time.\nFormat: 00:00:00", "Input error", JOptionPane.INFORMATION_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(tab, "Please input correct time.\nFormat: 00:00:00", "Input error", JOptionPane.INFORMATION_MESSAGE);
      }
      tab.jTable.updateUI();
    };
  }

  public int getLapNumberFromCol(int col) {
    return col - getColumns().size() + 1;
  }

  public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
  public static final Color DEFAULT_BACKGROUD_COLOR = Color.WHITE;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    StageTableData td = rows.get(row);
    JLabel label = (JLabel) defaultTableCellRendererCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (td.isGrpup) {
      label.setVerticalTextPosition(SwingConstants.CENTER);
      label.setHorizontalTextPosition(SwingConstants.RIGHT);
      label.setBackground(Color.LIGHT_GRAY);
      label.setForeground(Color.BLACK);
      label.setFont(label.getFont().deriveFont(Font.BOLD)); //  Font.PLAIN      
      table.setRowHeight(row, 30);

      if (column == 1) {
        JButton but = new JButton(tab.mainForm.getLocaleString("Start!"));
        if (tab.mainForm.activeGroup != null && tab.mainForm.activeGroup == td.group) {
          but.setText(tab.mainForm.getLocaleString("Stop"));
          but.setFont(but.getFont().deriveFont(Font.BOLD));
          but.setForeground(Color.RED);
        }
        td.raceButton = but;
        if (isSelected) {
          but.setForeground(table.getSelectionForeground());
          but.setBackground(table.getSelectionBackground());
        } else {
          but.setForeground(table.getForeground());
          but.setBackground(UIManager.getColor("Button.background"));
        }
        if (td.group != null && td.group.users != null && td.group.users.size() > 0 && td.group.users.get(0).IS_FINISHED == 1) {

        } else {
          //but.setBackground(Color.YELLOW);
          //label.setBackground(Color.YELLOW);
        }
        return but;
      }
      if (column == 3) {
        JButton but = new JButton(tab.mainForm.getLocaleString("Invate"));
        if (td.isGrpup && td.group == tab.checkingGrpup) {
          but.setForeground(table.getSelectionForeground());
          but.setBackground(new Color(193, 211, 245));
        } else {
          but.setForeground(table.getForeground());
          but.setBackground(UIManager.getColor("Button.background"));
        }
        return but;
      }
      if (column == 2 && SHOW_CHECK_RACE_BUTTON) {
        JButton but = new JButton(tab.mainForm.getLocaleString("Check"));
        if (td.isGrpup && td.group == tab.checkingGrpup) {
          but.setForeground(table.getSelectionForeground());
          but.setBackground(new Color(193, 211, 245));
        } else {
          but.setForeground(table.getForeground());
          but.setBackground(UIManager.getColor("Button.background"));
        }
        return but;
      }
    } else {
                 
      label.setBackground(DEFAULT_BACKGROUD_COLOR);
      label.setForeground(DEFAULT_FOREGROUND_COLOR);
      table.setRowHeight(row, 30);

      if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
        if (isQualificated(td.pilot)) {
        } else {
          label.setForeground(Color.RED);
        }
      } else if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT) {

      } else if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT) {
        /*if (isOut(td.pilot)) {
          label.setForeground(Color.RED);
        } else {
        }*/
        if (isQualificated(td.pilot)) {
        } else {
          label.setForeground(Color.RED);
        }
      } else {
        //if (tab.stage.CAPTION.equalsIgnoreCase("Qualification5"))
        if (show_laps && !td.isGrpup && column >= (getColumns().size() + tab.stage.LAPS)) {
          label.setBackground(Color.LIGHT_GRAY);
        } else {
          if  (this.tab.stage.ID  ==245){
            int y = 0;
          }
          
          if (column == 0 && SHOW_CHECK_RACE_BUTTON) {
            if (td.pilot.CHECK_FOR_RACE == 0) {
              label.setBackground(Color.WHITE);
            } else if (td.pilot.CHECK_FOR_RACE == 2) {
              label.setBackground(Color.LIGHT_GRAY);
            } else {
              if (td.pilot.color == null) {
                td.pilot.color = VSColor.getColorForChannel(td.pilot.CHANNEL, tab.stage.CHANNELS, tab.stage.COLORS);
              }
              if (td.pilot != null && td.pilot.color != null) {
                label.setBackground(td.pilot.color.getColor());
              } else {
                label.setBackground(Color.YELLOW);
              }
            }
          } else {
            if (td.pilot.parent == tab.mainForm.activeGroup) {
              if (td.pilot.IS_FINISHED == 1) {
                label.setBackground(Color.GREEN);
              } else {
                label.setBackground(Color.ORANGE);
              }
              label.setForeground(Color.BLACK);
              label.setFont(label.getFont().deriveFont(Font.BOLD)); //  Font.PLAIN     
            } else {
              if (td.pilot.IS_FINISHED == 1) {
                label.setBackground(Color.GREEN);
              }
            }
            if (column==2){
              /*
              STAGE_COLUMN sc = null;
        if (columnIndex < getColumns().size()) {
          sc = getColumns().get(columnIndex);
              
              */
              if (td.pilot.RECEIVED_LAPS) {
                 label.setBackground(Color.GREEN);
              }else{
                label.setBackground(Color.YELLOW);               
              }
            }            
          }
        }
      }
    }
    return label;
  }

}
