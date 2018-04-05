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

  private List<VS_REGISTRATION> rows;
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
  
  public static STAGE_COLUMN[] STAGE_COLUMNS_STAGE = new STAGE_COLUMN[]{
    new STAGE_COLUMN(10, "Act", 50),
    new STAGE_COLUMN(20, "Num", 50),
    new STAGE_COLUMN(30, "Pilot Type", 90),
    new STAGE_COLUMN(40, "Trans", 90),
    new STAGE_COLUMN(50, "OSD Name", 200),
    new STAGE_COLUMN(60, "First Name", 200),
    new STAGE_COLUMN(70, "Second Name", 200),
  };
  

  @Override
  public String getColumnName(int columnIndex) {
    if (STAGE_COLUMNS_STAGE.length>columnIndex){
      return STAGE_COLUMNS_STAGE[columnIndex].caption;
    }
    return "";
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
    if (rows.size() > rowIndex) {
      VS_REGISTRATION race = rows.get(rowIndex);
      if (columnIndex == 0) {
        return race.IS_ACTIVE==1?true:false;
      }
      if (columnIndex == 1) {
        return race.NUM;
      }
      if (columnIndex == 2) {
        try{
          return MainForm.PILOT_TYPES[race.PILOT_TYPE];
        }catch(Exception e){
          return MainForm.PILOT_TYPES[0];
        }
      }
      if (columnIndex == 3) {
        return race.VS_TRANS1;
      }
      if (columnIndex == 4) {
        return race.VS_USER_NAME;
      }
      if (columnIndex == 5) {
        return race.FIRST_NAME;
      }
      if (columnIndex == 6) {
        return race.SECOND_NAME;
      }
    }
    return "";
  }
   
  public boolean isCellEditable(int row, int col){
    if (col==0) return true;
    if (col==2) return true;
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
    if (rows.size() > row) {
      VS_REGISTRATION reg = rows.get(row);      
      if (col == 4) {
        reg.VS_USER_NAME = value.toString();
        try {
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
      if (col == 0) {
        reg.IS_ACTIVE = (Boolean)value?1:0;
        try {
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
      if (col == 2) {
        try {
          reg.PILOT_TYPE = ListEditTools.returnIndex(MainForm.PILOT_TYPES, value);
          reg.dbControl.update(regForm.mainForm.con, reg);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(regForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
      if (col == 1) {                
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
