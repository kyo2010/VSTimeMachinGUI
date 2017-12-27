/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Race;

import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.UserException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RaceListModelTable extends AbstractTableModel {

  private List<VS_RACE> rows;
  private MainForm mainForm = null;
  public String findString = "";
  
  public RaceListModelTable(MainForm mainForm) {
    this.mainForm = mainForm;
    loadData();
  } 

  public void loadData() {
    try {             
      rows = VS_RACE.dbControl.getList(mainForm.con, "1=1 ORDER by RACE_DATE");
    } catch (UserException e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Loading races is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public int getColumnCount() {
    return 5;
  }

  @Override
  public String getColumnName(int columnIndex) {
    if (columnIndex == 0) {
        return "ID";
      }
      if (columnIndex == 1) {
        return "Date";
      }
      if (columnIndex == 2) {
        return "Name";
      }
      if (columnIndex == 3) {
        return "Count of laps";
      }
      if (columnIndex == 4) {
        return "Min lap time";
      } 
      return "";
  }
  
  

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (rows.size() > rowIndex) {
      VS_RACE race = rows.get(rowIndex);
      if (columnIndex == 0) {
        return race.RACE_ID;
      }
      if (columnIndex == 1) {
        return race.RACE_DATE.getDateAsYYYYMMDD("-");
      }
      if (columnIndex == 2) {
        return race.RACE_NAME;
      }
      if (columnIndex == 3) {
        return race.COUNT_OF_LAPS;
      }
      if (columnIndex == 4) {
        return race.MIN_LAP_TIME;
      }      
    }
    return "";
  }
   
  public boolean isCellEditable(int row, int col){
    if (col==2) return true;
    return false;
  }
  
  public void showEditDialog(int row){
    if (row<rows.size() && row>=0){
      VS_RACE race = rows.get(row);      
      RaceControlForm.init(mainForm, race.RACE_ID).setVisible(true);
    }  
  }
  
  public VS_RACE getRace(int row){
    VS_RACE race = null;
    if (row<rows.size()){
      race = rows.get(row);      
    }  
    return race;
  }
  
  public void setValueAt(Object value, int row, int col) {
    if (rows.size() > row) {
      VS_RACE race = rows.get(row);      
      if (col == 1) {
        race.RACE_NAME = value.toString();
        try {
          race.dbControl.update(mainForm.con, race);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(mainForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
    }
  }    
}
