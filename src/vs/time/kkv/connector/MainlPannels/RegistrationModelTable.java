/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.Utils.UserException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTableAdapter;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RegistrationModelTable extends AbstractTableModel {

  public List<VS_REGISTRATION> rows = new ArrayList<>();
  private RegistrationTab regForm = null;
  public String findString = "";
  
  public RegistrationModelTable(RegistrationTab regForm) {
    this.regForm = regForm;    
    loadData();
  } 

  public void loadData() {
    try {             
      rows = VS_REGISTRATION.dbControl.getList(regForm.mainForm.con, "VS_RACE_ID=? ORDER by NUM",regForm.mainForm.activeRace.RACE_ID);
    } catch (UserException e) {
      regForm.mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(regForm.mainForm, "Loading pilots is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);
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
    
  public static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(RWSQ_ACT, "Act", 50),
    new STAGE_COLUMN(RWSQ_REG_ID, "RegID", 50),
    new STAGE_COLUMN(RWSQ_NUM, "Num", 50),
    new STAGE_COLUMN(RWSQ_PILOT_TYPE, "Pilot Type", 90),
    new STAGE_COLUMN(RWSQ_TRANS, "Trans", 90),
    new STAGE_COLUMN(RWSQ_OSD_NAME, "OSD Name", 200),
    new STAGE_COLUMN(RWSQ_FIRST_NAME, "First Name", 200),
    new STAGE_COLUMN(RWSQ_SECOND_NAME, "Second Name", 200),
  };
  

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
}
