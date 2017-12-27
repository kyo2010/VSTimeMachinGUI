/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.Utils.JDEDate;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kyo
 */
public class DBJDEAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == JDEDate.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return JDEDate.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        //Integer value = resultSet.getInt(rsPosition);
        //field.set(obj, value);
        Date date = resultSet.getDate(rsPosition);
        JDEDate jdeDate = new JDEDate();
        if ( date!=null)
          jdeDate.setDate(date.getTime());
        else jdeDate=null;
        if (dbmf.dbValueHandle!=null) jdeDate = (JDEDate)dbmf.dbValueHandle.prepareValue(jdeDate);
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
        JDEDate jd = (JDEDate)field.get(obj);
        if (jd==null) jd = new JDEDate();
        prepStat.setDate(psPosition, jd.getRealSQLDate());
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        //field.setInt(obj, Integer.parseInt(value));
        // xls_Date
        JDEDate jd = new JDEDate();
        try{    
          int excelDate = Integer.parseInt(value);
          jd.setExcelDate(excelDate);
        }catch(Exception e){          
          if (value.indexOf(".")==2){
            jd.setJDEDateAsDDMMYYYY(value, ".");
          }else{
            jd.setJDEDateAsYYYYMMDD(value, "-");
          }  
        }  
        field.set(obj, jd);
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      if (field.get(obj)==null) return "";
      return ((JDEDate) field.get(obj)).getDateAsYYYYMMDD("-");
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
