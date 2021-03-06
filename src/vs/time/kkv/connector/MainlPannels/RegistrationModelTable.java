/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.Utils.UserException;
import java.awt.Color;
import java.awt.Component;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTableAdapter;
import vs.time.kkv.connector.MainlPannels.stage.StageTableData;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RegistrationModelTable extends AbstractTableModel  implements TableCellRenderer{

  public List<VS_REGISTRATION> rows = new ArrayList<VS_REGISTRATION>();
  public List<VS_REGISTRATION> rowsAll = new ArrayList<VS_REGISTRATION>();
  public int activePilots = 0;
  private RegistrationTab regForm = null;  
  public String findString = "";
  
  public RegistrationModelTable(RegistrationTab regForm) {
    this.regForm = regForm;    
    loadData();
  } 

  public void loadData() {
    try {             
      rowsAll = VS_REGISTRATION.dbControl.getList(regForm.mainForm.con, "VS_RACE_ID=? ORDER by NUM",regForm.mainForm.activeRace.RACE_ID);
      //rows = rowsAll;
      activePilots = 0;
      for (VS_REGISTRATION reg : rowsAll){
        if (reg.IS_ACTIVE==1) activePilots++;
      }
      applayFilter();
    } catch (UserException e) {
      regForm.mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(regForm.mainForm, "Loading pilots is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void applayFilter(){
    String find = regForm.edFind.getText();  
    find = find.trim();
    String findLo = find.trim();
    if (find.equalsIgnoreCase("")){
      rows = rowsAll;
    }else{
      rows = new ArrayList();
      for (VS_REGISTRATION reg : rowsAll){
        if (reg.FIRST_NAME.toLowerCase().indexOf(findLo)>=0 /*|| reg.FIRST_NAME.indexOf(find)>=0*/){
          rows.add(reg);
        }else if (reg.SECOND_NAME.toLowerCase().indexOf(findLo)>=0 /*|| reg.SECOND_NAME.indexOf(find)>=0*/){
          rows.add(reg);
        }else if (reg.VS_USER_NAME.toLowerCase().indexOf(findLo)>=0 /* || reg.VS_USER_NAME.indexOf(find)>=0*/){
          rows.add(reg);
        };
      }      
    }
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public int getColumnCount() {
    return STAGE_COLUMNS_STAGE.length;
  }   
  public static final int RWSQ_ACT    = 10;
  public static final int RWSQ_REG_ID = 20;
  public static final int RWSQ_NUM    = 30;
  public static final int RWSQ_PILOT_TYPE = 40;
  public static final int RWSQ_TRANS  = 50;
  public static final int RWSQ_OSD_NAME = 60;
  public static final int RWSQ_FIRST_NAME = 70;
  public static final int RWSQ_SECOND_NAME = 80;
  public static final int RWSQ_WEB_SYSTEM = 90;  
    
  public static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(RWSQ_ACT, "Act", 50),
    new STAGE_COLUMN(RWSQ_REG_ID, "RegID", 50),
    new STAGE_COLUMN(RWSQ_NUM, "Num", 50),
    new STAGE_COLUMN(RWSQ_PILOT_TYPE, "Pilot Type", 90),
    new STAGE_COLUMN(RWSQ_TRANS, "Trans", 90),
    new STAGE_COLUMN(RWSQ_OSD_NAME, "OSD Name", 200),
    new STAGE_COLUMN(RWSQ_FIRST_NAME, "First Name", 200),
    new STAGE_COLUMN(RWSQ_SECOND_NAME, "Second Name", 200),
    new STAGE_COLUMN(RWSQ_WEB_SYSTEM, "Reg.System", 200),
  };
  
  public List<STAGE_COLUMN> getColumns(){
    List<STAGE_COLUMN> cols = new ArrayList();
    for (STAGE_COLUMN col : STAGE_COLUMNS_STAGE){
      cols.add(col);
    }
    return cols;
  }

  @Override
  public String getColumnName(int columnIndex) {
    if (STAGE_COLUMNS_STAGE.length>columnIndex){
      return STAGE_COLUMNS_STAGE[columnIndex].caption;
    }
    return "";
  }  

  public STAGE_COLUMN getColumn(int columnIndex) {
    if (STAGE_COLUMNS_STAGE.length>columnIndex){
      return STAGE_COLUMNS_STAGE[columnIndex];
    }
    return null;
  }    
  
  public int getColumnWidth(int columnIndex) {
    if (STAGE_COLUMNS_STAGE.length>columnIndex){
      return STAGE_COLUMNS_STAGE[columnIndex].width;
    }
    return 0;
  }    

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex==0) return Boolean.class;
    //if (columnIndex==2) return String.class;
    return String.class;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    STAGE_COLUMN col = getColumn(columnIndex);
    if (rows.size() > rowIndex && col!=null) {
      VS_REGISTRATION reg = rows.get(rowIndex);
      if (col.ID == RWSQ_ACT) {
        return reg.IS_ACTIVE==1?true:false;
      }
      if (col.ID == RWSQ_REG_ID) {
        return "[ "+reg.ID+" ]";
      }
      if (col.ID == RWSQ_NUM) {
        return reg.NUM;
      }
      if (col.ID == RWSQ_PILOT_TYPE) {
        try{
          return MainForm.PILOT_TYPES[reg.PILOT_TYPE];
        }catch(Exception e){
          return MainForm.PILOT_TYPES[0];
        }
      }
      if (col.ID == RWSQ_TRANS) {
        return reg.VS_TRANS1;
      }
      if (col.ID == RWSQ_OSD_NAME) {
        return reg.VS_USER_NAME;
      }
      if (col.ID == RWSQ_FIRST_NAME) {
        return reg.FIRST_NAME;
      }
      if (col.ID == RWSQ_SECOND_NAME) {
        return reg.SECOND_NAME;
      }
      if (col.ID==RWSQ_WEB_SYSTEM){
        return reg.WEB_SYSTEM;
      }
    }
    return "";
  }
   
  public boolean isCellEditable(int row, int col){
    STAGE_COLUMN colInfo = getColumn(col);
    if (colInfo!=null && colInfo.ID==RWSQ_ACT) return true;
    if (colInfo!=null && colInfo.ID==RWSQ_PILOT_TYPE) return true;
    if (colInfo!=null && colInfo.ID==RWSQ_NUM) return true;
    return false;
  }
    
  public VS_REGISTRATION getRegInfo(int row){
    VS_REGISTRATION reg = null;
    if (row<rows.size()){
      reg = rows.get(row);      
    }  
    return reg;
  }
  
  public void setValueAt(Object value, int row, int col) {
    STAGE_COLUMN colInfo = getColumn(col);    
    if (rows.size() > row && colInfo!=null) {
      VS_REGISTRATION reg = rows.get(row);      
      /*if (col == 4) {
        reg.VS_USER_NAME = value.toString();
        try {
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }*/
      if (colInfo.ID == RWSQ_ACT) {
        if (reg.IS_ACTIVE==1){
          int res = JOptionPane.showConfirmDialog(regForm, "Do you like to delete pilot '"+reg.getFullUserName()+"' ?", "Information", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.NO_OPTION) {
            return;
          }        
        }
        reg.IS_ACTIVE = (Boolean)value?1:0;        
        if (reg.IS_ACTIVE==1) activePilots++;
        if (reg.IS_ACTIVE==0) activePilots--;
        if (activePilots<0) activePilots = 0;
        if (activePilots>rowsAll.size()) activePilots = rowsAll.size();
        regForm.lActivePilotsCount.setText(""+activePilots);
        try {
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
      if (colInfo.ID == RWSQ_PILOT_TYPE) {
        try {
          reg.PILOT_TYPE = ListEditTools.returnIndex(MainForm.PILOT_TYPES, value);
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
      if (colInfo.ID == RWSQ_NUM) {                
        try {
          reg.NUM = Integer.parseInt(value.toString());
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
        }        
      }
    }
  }    
  
  private DefaultTableCellRenderer defaultTableCellRendererCellRenderer = new DefaultTableCellRenderer();

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    VS_REGISTRATION regInfo = rows.get(row);
    JLabel label = (JLabel) defaultTableCellRendererCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (regInfo!=null){
      if ( regInfo.transIsEmpty() )
      {
        label.setBackground(Color.PINK);
      }else{
        if (isSelected){
          label.setBackground(Color.BLUE);          
        }else{
          label.setBackground(Color.WHITE);
        }  
      }
    }    
    return label;
  }
}
