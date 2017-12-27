/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author kyo
 */
public class DBJDEAdapterWithTime implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == JDEDateAndTime.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return JDEDateAndTime.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        //Integer value = resultSet.getInt(rsPosition);
        //field.set(obj, value);
        Timestamp date = resultSet.getTimestamp(rsPosition);
        JDEDateAndTime jdeDate = new JDEDateAndTime();
        if ( date!=null)
          jdeDate.setDate(date.getTime());
        else jdeDate=null;
        if (dbmf.dbValueHandle!=null) jdeDate = (JDEDateAndTime)dbmf.dbValueHandle.prepareValue(jdeDate);
        field.set(obj, jdeDate);
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        /*Integer value = null;
        try{
          value = (Integer)field.get(obj);
        }catch(Exception e){
          value = field.getInt(obj);
        }  
        prepStat.setInt(psPosition, value);*/
        JDEDateAndTime jd = (JDEDateAndTime)field.get(obj);
        if (jd==null) jd = new JDEDateAndTime();
        prepStat.setTimestamp(psPosition, jd.getTimestamp());
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        //field.setInt(obj, Integer.parseInt(value));
        // xls_Date
        JDEDateAndTime jd = new JDEDateAndTime();
        try{    
          int excelDate = Integer.parseInt(value);
          jd.setExcelDate(excelDate);
        }catch(Exception e){
          jd.setJDEDateAsYYYYMMDD(value, "-");
        }  
        field.set(obj, jd);
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      if (field.get(obj)==null) return "";
      return ((JDEDateAndTime) field.get(obj)).getDateAsYYYYMMDD_andTime("-",":");
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
