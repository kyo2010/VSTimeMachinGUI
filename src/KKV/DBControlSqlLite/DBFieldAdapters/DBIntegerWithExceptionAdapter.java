/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.Utils.UserException;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kyo
 */
public class DBIntegerWithExceptionAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == DBIntegerWithException.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return Integer.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        Integer value = resultSet.getInt(rsPosition);
        try{
          DBIntegerWithException val = new DBIntegerWithException(value);
          field.set(obj, val);
        }catch(Exception e){
          field.set(obj, 0);
        }  
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        DBIntegerWithException value = null;
        try{
          value = (DBIntegerWithException)field.get(obj);
          prepStat.setInt(psPosition, value==null?0:value.data);
        }catch(Exception e){          
        }          
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException, UserException {
      try{
        field.set(obj, new DBIntegerWithException(Integer.parseInt(value)));
      }catch(Exception e){
        //field.setInt(obj, Integer.parseInt(value));    
        throw new UserException("Error","Cann't convert '"+value+"' to integer value");
      }
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      
        DBIntegerWithException val = (DBIntegerWithException)field.get(obj);
        try{
          return ""+val.data;
        }catch(Exception ei){
          return "0";
        }  
        
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };

}
