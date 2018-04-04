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
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import static vs.time.kkv.models.VS_STAGE_GROUP.GROUP_TIME_COMPARATOR;

/**
 *
 * @author kyo
 */
public class StageTableAdapter extends AbstractTableModel implements TableCellRenderer {

  private List<StageTableData> rows;
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
  static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_CHANNEL, "Channel", 80),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setIsEditing(true).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),};

  static STAGE_COLUMN[] STAGE_COLUMNS_STAGE_RACE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_CHANNEL, "Channel", 80),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SCORE, "Score", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_WIN, "Win", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LOSS, "Loss", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setIsEditing(true).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50).setCellID("INT"),};

  static STAGE_COLUMN[] STAGE_COLUMNS_RESULT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_QUAL_STATUS, "Status", 150),};
  
  static STAGE_COLUMN[] STAGE_COLUMNS_REPORT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_REGION, "Region", 200),    
    //new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    
  };


  static STAGE_COLUMN[] STAGE_COLUMNS_RACE_RESULT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 220),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_SCORE, "Score", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_WIN, "Win", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LOSS, "Loss", 50).setCellID("INT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Race Time", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90).setCellID("TXT_RIGHT"),
    new STAGE_COLUMN(STAGE_COLUMN.CID_RACE_STATUS, "Status", 150),};

  public String getColumnCellID(int column) {
    String cellID = "TXT_RIGHT";
    STAGE_COLUMN[] columns = getColumns();
    if (column < columns.length) {
      cellID = columns[column].cellID;
    }
    return cellID;
  }

  public STAGE_COLUMN[] getColumns() {
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      return STAGE_COLUMNS_RESULT;
    }
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_REPORT){
      return STAGE_COLUMNS_REPORT;
    }
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE_RESULT) {
      return STAGE_COLUMNS_RACE_RESULT;
    }
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_RACE) {
      return STAGE_COLUMNS_STAGE_RACE;
    }
    return STAGE_COLUMNS_STAGE;
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
              //pilot.IS_FINISHED = 1;
              pilot.IS_RECALULATED = 1;

              rows.add(new StageTableData(pilot));
              index++;

              pilot.GID = -1;
              pilot.STAGE_ID = tab.stage.ID;
              pilots.put(pilot.PILOT, pilot);
            } else {
              VS_STAGE_GROUPS pilot1 = pilots.get(pilot.PILOT);
              if (pilot1.BEST_LAP != 0 && pilot1.BEST_LAP < pilot.BEST_LAP) {
                pilot.BEST_LAP = pilot1.BEST_LAP;
              }
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

        if (tab.stage.CAPTION.equalsIgnoreCase("Race Result13")) {
          int y = 0;
        }

        long union_stage_id = tab.stage.PARENT_STAGE_ID;
        VS_STAGE parent_stage = null;
        try {
          parent_stage = VS_STAGE.dbControl.getItem(tab.mainForm.con, "ID=? and RACE_ID=?", union_stage_id, tab.stage.RACE_ID);
          if (parent_stage != null && parent_stage.STAGE_TYPE == MainForm.STAGE_RACE) {
            // Собирать все Raсe у которых PARENT_STAGE_ID = ntreotq ujyrb
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
            pilot.PILOT_TYPE = reg.PILOT_TYPE;
          }
          VS_STAGE_GROUPS result = pilots.get(pilot.PILOT);
          if (result == null) {
            pilot.NUM_IN_GROUP = index;
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
          result.wins += pilot.WIN;
          result.loses += pilot.LOSE;

        }

        results.clear();
        for (VS_STAGE_GROUPS pilot : pilots.values()) {
          results.add(pilot);
        }
        if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_LOSS_DESC) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_LOST_COMPARATOR);
        } else if (tab.stage.SORT_TYPE == MainForm.STAGE_SORT_BY_SCORE_DESC) {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_SCORE_COMPARATOR);
        } else {
          Collections.sort(results, VS_STAGE_GROUP.GROUP_TIME_COMPARATOR);
        }
        index = 1;
        for (VS_STAGE_GROUPS pilot : results) {
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
        List<VS_STAGE> qualification = VS_STAGE.dbControl.getList(tab.mainForm.con, "STAGE_TYPE in (" + MainForm.STAGE_QUALIFICATION_RESULT + ") order by ID desc");
        List<VS_STAGE_GROUPS> results = new ArrayList<VS_STAGE_GROUPS>();
        for (VS_STAGE stage : stages) {
          List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID=? order by GROUP_NUM, NUM_IN_GROUP", stage.ID);
          for (VS_STAGE_GROUPS usr : groups) {  
            VS_STAGE_GROUPS res = null;
            for (VS_STAGE_GROUPS result : results) {
              if (result.PILOT.equalsIgnoreCase(usr.PILOT)) {
                res = result;
                break;
              }
            }
            if (res==null){
              res = (VS_STAGE_GROUPS) VS_STAGE_GROUPS.dbControl.copyObject(usr);
              res.STAGE_ID = tab.stage.ID;
              res.GROUP_NUM = 1;
              res.NUM_IN_GROUP = NUM_IN_GROUP;
              res.IS_FINISHED = 1;
              res.IS_RECALULATED = 1;
              NUM_IN_GROUP++;
              results.add(res);
            }
          }
        }
        for (VS_STAGE_GROUPS pilot : results){
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
        tab.stage.laps = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
      } catch (Exception e) {
      }
      for (Integer groupId : tab.stage.groups.keySet()) {
        VS_STAGE_GROUP group = tab.stage.groups.get(groupId);
        for (VS_STAGE_GROUPS pilot : group.users) {
          pilot.recalculateLapTimes(tab.mainForm.con, tab.stage, false);
          if (tab.stage.ID == 114) {
            System.out.println(pilot.GID + " " + pilot.PILOT + " " + pilot.RACE_TIME);
          }
        }
      }
    }
    //List<VS_RACE_LAP> laps = VS_RACE_LAP.dbControl.getList(tab.mainForm.con, "RACE_ID=? and STAGE_ID=?", STAGE_COLUMNS);
  }

  public int getMinWidth(int col) {
    if (col < getColumns().length) {
      return getColumns()[col].width;
    }
    return LAP_WEIGHT;
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public int getColumnCount() {
    return getColumns().length + (show_laps ? tab.stage.LAPS : 0);
  }

  @Override
  public String getColumnName(int columnIndex) {
    if (columnIndex < getColumns().length) {
      return getColumns()[columnIndex].caption;
    }
    return "Lap " + (columnIndex - getColumns().length + 1);
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
          return " -= Group " + td.group.GROUP_NUM + " =- ";
        }
        return "";
      } else {
        STAGE_COLUMN sc = null;
        if (columnIndex < getColumns().length) {
          sc = getColumns()[columnIndex];
        } else {
          // lap time
          VS_RACE_LAP lap = null;
          try {
            lap = tab.stage.laps.get("" + td.pilot.GROUP_NUM).get("" + td.pilot.VS_PRIMARY_TRANS).get("" + getLapNumberFromCol(columnIndex));
          } catch (Exception e) {
          }
          if (lap == null) {
            return "";
          }
          return tab.getTimeIntervel(lap.TRANSPONDER_TIME);
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
          return td.pilot.PILOT + addon;
        }
        
        if (sc != null && sc.ID == STAGE_COLUMN.CID_REGION) {
          try {
            VS_REGISTRATION reg = td.pilot.getRegistration(tab.mainForm.con, tab.mainForm.activeRace.RACE_ID);
            if (reg != null) {
              if (reg.REGION.equalsIgnoreCase("none")) return "";
              return reg.REGION;
            }
          } catch (Exception e) {
          }
          return "";
        }

        td.pilot.recalculateLapTimes(tab.mainForm.con, tab.stage, false);

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
            return "NONE";
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
            return "qualified";
          } else {
            return "not qualified";
          }
        }

        if (sc != null && sc.ID == STAGE_COLUMN.CID_RACE_STATUS) {
          if (isOut(td.pilot)) {
            return "out";
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
    if (col < getColumns().length) {
      sc = getColumns()[col];
    }
    if (col >= getColumns().length) {
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
      }
    } catch (Exception e) {

    }
    return result;
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    StageTableData td = rows.get(row);
    STAGE_COLUMN sc = null;
    if (col < getColumns().length) {
      sc = getColumns()[col];
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
    if (col >= getColumns().length && td != null && !td.isGrpup) {
      try {
        // (?<!\\w)\\d+(?!\\w)
        //Pattern p = Pattern.compile("\\d+:[0-5][0-9]:[0-9][0-9]");
        String val = value == null ? "" : value.toString();
        long time = getTimerInterval(val);
        boolean isError = false;
        if (time != -1) {
          VS_RACE_LAP lap = VS_RACE_LAP.saveTime(tab.mainForm.con, td.pilot.parent, time, td.pilot.VS_PRIMARY_TRANS, getLapNumberFromCol(col));
          if (lap == null) {
            isError = true;
          }
          VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps, "" + td.pilot.GROUP_NUM, "" + td.pilot.VS_PRIMARY_TRANS, "" + getLapNumberFromCol(col), lap);
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
          VS_RACE_LAP.dbControl.delete(tab.mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and TRANSPONDER_ID=?",
                  tab.stage.RACE_ID, tab.stage.ID, td.pilot.GROUP_NUM, td.pilot.VS_PRIMARY_TRANS);
          long lapTime = time / tab.stage.LAPS;
          for (int i = 0; i < tab.stage.LAPS; i++) {
            if (i + 1 == tab.stage.LAPS) {
              lapTime = time - lapTime * (tab.stage.LAPS - 1);
            }
            VS_RACE_LAP lap = VS_RACE_LAP.saveTime(tab.mainForm.con, td.pilot.parent, lapTime, td.pilot.VS_PRIMARY_TRANS, i + 1);
            VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps, "" + td.pilot.GROUP_NUM, "" + td.pilot.VS_PRIMARY_TRANS, "" + (i + 1), lap);
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
    return col - getColumns().length + 1;
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
        JButton but = new JButton("Start!");
        if (tab.mainForm.activeGroup != null && tab.mainForm.activeGroup == td.group) {
          but.setText("Stop");
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
        JButton but = new JButton("Invate");
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
        JButton but = new JButton("Check");
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
        }
      }
    }
    return label;
  }

}
