/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Users;

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
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class UserListModelTable extends AbstractTableModel {

  private List<VS_USERS> rows;
  private MainForm mainForm = null;
  public String findString = "";
  public boolean isEdit = true;
  
  public UserListModelTable(MainForm mainForm, boolean isEdit) {
    this.isEdit = isEdit;
    this.mainForm = mainForm;
    loadData();
  }

  public void setFindString(String findString) {
    this.findString = findString;
    loadData();
  }

  public void loadData() {
    try {
      if (findString.trim().equalsIgnoreCase("")) {
        rows = VS_USERS.dbControl.getList(mainForm.con, "1=1 ORDER by VS_NAME");
      } else {
        int findid = -1;
        try{
          findid = Integer.parseInt(findString);
        }catch(Exception e){}
        
        String addon = "";
        if (findid!=-1){
          addon= " OR VSID=" + findid;
        }        
        rows = VS_USERS.dbControl.getList(mainForm.con, "VS_NAME_UPPER LIKE '%" + findString.toUpperCase()+ "%' "+addon+" ORDER by VS_NAME");
      }
    } catch (UserException e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Loading user is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public String getColumnName(int column) {
    if (column==0) return "Transponder";
    if (column==1) return "Pilot";
    return "";
  }
  
  

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (rows.size() > rowIndex) {
      VS_USERS usr = rows.get(rowIndex);
      if (columnIndex == 0) {
        return usr.VSID;
      }
      if (columnIndex == 1) {
        return usr.VS_NAME;
      }
    }
    return "";
  }
   
  public boolean isCellEditable(int row, int col){
    if (col==1 && isEdit) {
      return true;
    }
    if (col==0){
      //VS_USERS usr = rows.get(row);      
      //UserControlForm.init(mainForm, false, usr.VSID).setVisible(true);
    }
    return false;
  }
  
  public void showEditDialog(int row){
    if (row<rows.size()){
      VS_USERS usr = rows.get(row);      
      UserControlForm.init(mainForm, false, usr.VSID).setVisible(true);
    }  
  }
  
  public VS_USERS getUser(int row){
    VS_USERS usr = null;
    if (row<rows.size()){
      usr = rows.get(row);            
    }  
    return usr;
  }
  
  public void setValueAt(Object value, int row, int col) {
    if (rows.size() > row) {
      VS_USERS usr = rows.get(row);      
      if (col == 1) {
        usr.VS_NAME = value.toString();
        usr.VS_NAME_UPPER = value.toString().toUpperCase();
        try {
          usr.dbControl.update(mainForm.con, usr);
          //UserList.init(mainForm).refresh();
        } catch (UserException e) {
          JOptionPane.showMessageDialog(mainForm, "Edition is error. " + e.error + " " + e.details, "Error", JOptionPane.ERROR_MESSAGE);  
        }        
      }
    }
  }
  
}
