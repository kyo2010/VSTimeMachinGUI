/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kyo
 */
public class DBArrayAdapter implements DBFieldAdapter {
   
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType().isArray()) {
            return true;
        }
        return false;
    }//if not valid try next adapter

    @Override
    public Class getAdapterClass(){
      return String[].class.getClass();
    };
    
    
    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        String value = resultSet.getString(rsPosition);
        if (value!=null && dbmf.autoTrim) value=value.trim();
        String[] arr = (String[])field.get(obj);
        arr[ArrayIndex]=value;
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        String[] values = (String[]) field.get(obj);        
        try{
          prepStat.setString(psPosition, values[ArrayIndex]);
        }catch(Exception e){
          prepStat.setString(psPosition, "");
        }  
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {      
      Object[] objs = (Object[])field.get(obj);
      objs[ArrayIndex] = value;
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {        
        Object[] objs = (Object[])field.get(obj);
        String val = "";
        try{
          val = ""+objs[ArrayIndex];
        }catch(Exception e){}  
        //val = val.replaceAll("<", "&lt;");
        //val = val.replaceAll(">", "&gt;");
        return val;
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
