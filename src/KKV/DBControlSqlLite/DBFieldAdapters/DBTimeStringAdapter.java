/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.Utils.JDEDate;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 *
 * @author kyo
 */
public class DBTimeStringAdapter implements DBFieldAdapter {

  @Override
  public Boolean isValidClass(Field field) {
    if (field.getType() == DBTimeString.class) {
      return true;
    }
    return false;
  }//if not valid try next adapter

  @Override
  public Class getAdapterClass() {
    return DBTimeString.class;
  }

  ;

    @Override
  public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
    //Time value = resultSet.getTime(rsPosition);
    String value = resultSet.getString(rsPosition);
    
    try {
      DBTimeString ts = new DBTimeString(value);
      if (dbmf.dbValueHandle!=null) ts = (DBTimeString)dbmf.dbValueHandle.prepareValue(ts);
      field.set(obj, ts);
    } catch (Exception e) {
      field.set(obj, 0);
    }
  }

  @Override
  public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
    DBTimeString value = null;
    try {
      value = (DBTimeString) field.get(obj);      
    } catch (Exception e) {
    }
    prepStat.setString(psPosition, value==null?"":value.time_st);
  }

  @Override
  public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
    //field.set(obj, null);
    field.set(obj, new DBTimeString(value));    
  }

  @Override
  public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {  
    DBTimeString t = (DBTimeString) field.get(obj);
    return t==null?"":t.time_st;
  }
  
  public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
