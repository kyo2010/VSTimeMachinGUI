/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 *
 * @author kyo
 */
public class DBTimeAdapter implements DBFieldAdapter {

  @Override
  public Boolean isValidClass(Field field) {
    if (field.getType() == Time.class) {
      return true;
    }
    return false;
  }//if not valid try next adapter

  @Override
  public Class getAdapterClass() {
    return Time.class;
  }

  ;

    @Override
  public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
    Time value = resultSet.getTime(rsPosition);
    if (dbmf.dbValueHandle!=null) value = (Time)dbmf.dbValueHandle.prepareValue(value);
    try {
      field.set(obj, value);
    } catch (Exception e) {
      field.set(obj, 0);
    }
  }

  @Override
  public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
    Time value = null;
    try {
      value = (Time) field.get(obj);
      prepStat.setTime(psPosition, value);
    } catch (Exception e) {
    }

  }

  @Override
  public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
    field.set(obj, null);

    try {
      long xls_time = (long) (Double.parseDouble(value) * 24 * 60 * 60);
      /*int hh = (int) xls_time/(60*60);
       double mm1 = xls_time - hh*60*60;
       int mm = (int) mm1/(60);
       int ss = (int) mm1-mm*60;
       */
      
      int hh = (int) xls_time/(60*60);
      double mm1 = xls_time - hh*60*60;
      int mm = (int) mm1/(60);
      int ss = (int) mm1-mm*60;
      
      Time val = new Time(hh, mm, ss);
      field.set(obj, val);
      //value = Tools.padl(""+hh, 2,"0")+":" + Tools.padl(""+mm, 2,"0")+":" + Tools.padl(""+ss, 2,"0");

    } catch (Exception eout) {
      try {
        Time val = Time.valueOf(value);
        field.set(obj, val);
      } catch (Exception e) {
        int pos = value.indexOf("-");
        if (pos > 0) {
          int hh = Integer.parseInt(value.substring(0, pos));
          int mm = Integer.parseInt(value.substring(pos + 1));
          Time val = Time.valueOf(hh + ":" + mm + ":00");
          field.set(obj, val);
        }
      }
    }

    /*if (1 == 1) {
      Time t = (Time) field.get(obj);
      JDEDate jd = new JDEDate();
      jd.setDate(t.getTime());
      String val = jd.getTimeString();
      System.out.println("Time in: " + value + " -> " + val);
    }*/
  }

  @Override
  public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
    String val = "";
    Time t = (Time) field.get(obj);
    if (t != null) {
      JDEDate jd = new JDEDate();
      jd.setDate(t.getTime());
      val = jd.getTimeString();
    }
    return val;
  }

  public String getDeafaultCellID(DBModelField mf) {
    return null;
  }
}
