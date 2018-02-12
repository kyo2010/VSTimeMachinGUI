/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.UserException;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_REGISTRATION;

/**
 *
 * @author kyo
 */
public class StageTableAdapter extends AbstractTableModel implements TableCellRenderer {

  private List<StageTableData> rows;
  private StageTab tab = null;
  private DefaultTableCellRenderer defaultTableCellRendererCellRenderer = new DefaultTableCellRenderer();

  public StageTableData getTableData(int row) {
    if (row < rows.size()) {
      return rows.get(row);
    }
    return null;
  }

  public static class STAGE_COLUMN {

    String caption;
    int width;
    int ID;

    public static final int CID_PILOT = 10;
    public static final int CID_PILOT_NUM = 15;
    public static final int CID_PILOT_TYPE = 17;
    public static final int CID_CHANNEL = 20;
    public static final int CID_TIME = 30;
    public static final int CID_BEST_LAP = 40;
    public static final int CID_LAPS = 50;
    public static final int CID_LAP = 100;
    public static final int CID_QUAL_STATUS = 110;

    public STAGE_COLUMN(int ID, String caption, int width) {
      this.ID = ID;
      this.caption = caption;
      this.width = width;
    }
  }

  public boolean show_laps = true;
  public boolean show_grups = true;

  final static int LAP_WEIGHT = 90;
  static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_CHANNEL, "Channel", 80),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Time", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_LAPS, "Laps", 50),};

  static STAGE_COLUMN[] STAGE_COLUMNS_RESULT = new STAGE_COLUMN[]{
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_NUM, "Num", 50),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT, "Pilot", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_PILOT_TYPE, "Type", 150),
    new STAGE_COLUMN(STAGE_COLUMN.CID_TIME, "Time", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_BEST_LAP, "Best Lap", 90),
    new STAGE_COLUMN(STAGE_COLUMN.CID_QUAL_STATUS, "Status", 150),};

  public STAGE_COLUMN[] getColumns() {
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      return STAGE_COLUMNS_RESULT;
    }
    return STAGE_COLUMNS_STAGE;
  }

  public StageTableAdapter(StageTab tab) {
    this.tab = tab;
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      show_grups = false;
      show_laps = false;
    }
    loadData();
    defaultTableCellRendererCellRenderer.setOpaque(true);
  }

  public boolean isQualificated(VS_STAGE_GROUPS pilot) {
    if (pilot.NUM_IN_GROUP <= tab.stage.LAPS || tab.stage.LAPS == -1) {
      return true;
    }
    return false;
  }

  public void loadData() {
    if (tab.stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION_RESULT) {
      //List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GID", parent_stage.ID);          
      try {
        List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(tab.mainForm.con, "STAGE_ID IN (SELECT stage.id from VS_STAGE as stage where stage.RACE_ID=? and stage.STAGE_TYPE=?) order by RACE_TIME, BEST_LAP", tab.stage.RACE_ID, MainForm.STAGE_QUALIFICATION);
        HashMap<String,VS_STAGE_GROUPS> pilots = new HashMap<String,VS_STAGE_GROUPS>();
        Map<String, VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getMap(tab.mainForm.con, "VS_TRANSPONDER", "VS_RACE_ID=?", tab.stage.RACE_ID);

        rows = new ArrayList<StageTableData>();
        int index = 1;
        VS_STAGE_GROUPS.dbControl.delete(tab.mainForm.con, "STAGE_ID=?", tab.stage.ID);

        for (VS_STAGE_GROUPS pilot : groups) {
          VS_REGISTRATION reg = regs.get("" + pilot.TRANSPONDER);
          if (reg != null) {
            pilot.PILOT = reg.VS_USER_NAME;
            pilot.PILOT_TYPE = reg.PILOT_TYPE;
          }

          if (tab.stage.FLAG_BY_PYLOT_TYPE == MainForm.PILOT_TYPE_NONE_INDEX || tab.stage.FLAG_BY_PYLOT_TYPE == reg.PILOT_TYPE) {
            if (pilots.get(pilot.PILOT)==null) {
              pilot.NUM_IN_GROUP = index;
              pilot.IS_FINISHED = 1;
              pilot.IS_RECALULATED = 1;

              rows.add(new StageTableData(pilot));
              index++;

              pilot.GID = -1;
              pilot.STAGE_ID = tab.stage.ID;              
              pilots.put(pilot.PILOT,pilot);
            }else{
              VS_STAGE_GROUPS pilot1 = pilots.get(pilot.PILOT);
              if (pilot1.BEST_LAP!=0 && pilot1.BEST_LAP<pilot.BEST_LAP){
                pilot.BEST_LAP = pilot1.BEST_LAP;
              }
            }            
          }          
        }
        for (VS_STAGE_GROUPS pilot : pilots.values()){
          if (isQualificated(pilot)) {
            VS_STAGE_GROUPS.dbControl.insert(tab.mainForm.con, pilot);
          }
        }  
        
        

      } catch (Exception ein) {
        MainForm._toLog(ein);
      }
    } else {
      rows = new ArrayList<StageTableData>();
      for (Integer groupId : tab.stage.groups.keySet()) {
        VS_STAGE_GROUP group = tab.stage.groups.get(groupId);        
        rows.add(new StageTableData(group));
        for (VS_STAGE_GROUPS pilot : group.users) {
          pilot.IS_RECALULATED = 0;
          rows.add(new StageTableData(pilot));
        }
      }
      try {
        tab.stage.laps = VS_RACE_LAP.dbControl.getMap3(tab.mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=?", tab.stage.RACE_ID, tab.stage.ID);
      } catch (Exception e) {
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
            lap = tab.stage.laps.get("" + td.pilot.GROUP_NUM).get("" + td.pilot.TRANSPONDER).get("" + getLapNumberFromCol(columnIndex));
          } catch (Exception e) {
          }
          if (lap == null) {
            return "";
          }
          return tab.getTimeIntervel(lap.TRANSPONDER_TIME);
        }
        if (sc != null && sc.ID == STAGE_COLUMN.CID_PILOT) {
          return td.pilot.PILOT;
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
      }
    }
    return "";
  }

  public boolean isCellEditable(int row, int col) {
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
    return false;
  }

  public void showEditDialog(int row) {
    if (row < rows.size() && row >= 0) {
      //  VS_RACE race = rows.get(row);      
      //  RaceControlForm.init(mainForm, race.RACE_ID).setVisible(true);
    }
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    StageTableData td = rows.get(row);
    if (col >= getColumns().length && td != null && !td.isGrpup) {
      try {
        // (?<!\\w)\\d+(?!\\w)
        //Pattern p = Pattern.compile("\\d+:[0-5][0-9]:[0-9][0-9]");
        String val = value == null ? "" : value.toString();
        //Matcher m = p.matcher(val);
        long time = 0;
        boolean isError = false;
        if (!val.equals("")) {
          int pos1 = val.lastIndexOf(":");
          if (pos1 > 0) {
            String ms_st = val.substring(pos1 + 1);
            long ms = Long.parseLong(ms_st);
            val = val.substring(0, pos1);
            pos1 = val.lastIndexOf(":");
            if (pos1 > 0) {
              String sec_st = val.substring(pos1 + 1);
              long sec = Long.parseLong(sec_st);
              val = val.substring(0, pos1);
              long min = Long.parseLong(val);
              time = (min * 60 + sec) * 1000 + ms * 10;
              VS_RACE_LAP lap = VS_RACE_LAP.saveTime(tab.mainForm.con, td.pilot.parent, time, td.pilot.TRANSPONDER, getLapNumberFromCol(col));
              if (lap == null) {
                isError = true;
              }
              VS_RACE_LAP.dbControl.putObjToMap(tab.stage.laps, "" + td.pilot.GROUP_NUM, "" + td.pilot.TRANSPONDER, "" + getLapNumberFromCol(col), lap);
            } else {
              isError = true;
            }
          } else {
            isError = true;
          }
          if (isError) {
            JOptionPane.showMessageDialog(tab, "Please input correct time.\nFormat: 00:00:00", "Input error", JOptionPane.INFORMATION_MESSAGE);
          }
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
        }
        td.raceButton = but;
        if (isSelected) {
          but.setForeground(table.getSelectionForeground());
          but.setBackground(table.getSelectionBackground());
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
      } else {
        if (td.pilot.parent == tab.mainForm.activeGroup) {
          if (td.pilot.IS_FINISHED == 1) {
            label.setBackground(Color.GREEN);
          } else {
            label.setBackground(Color.ORANGE);
          }
          label.setForeground(Color.BLACK);
          label.setFont(label.getFont().deriveFont(Font.BOLD)); //  Font.PLAIN     
        }
      }
    }
    return label;
  }

}
