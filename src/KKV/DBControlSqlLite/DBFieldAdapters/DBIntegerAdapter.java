package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Dmitriy Voloshin
 * Date: 07.11.12
 * Time: 12:33
 */
public class DBIntegerAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == Integer.class || field.getType() == int.class) {
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
        if (dbmf.dbValueHandle!=null) value = (Integer)dbmf.dbValueHandle.prepareValue(value);
        try{
          field.set(obj, value);
        }catch(Exception e){
          field.set(obj, 0);
        }  
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        Integer value = null;
        try{
          value = (Integer)field.get(obj);
          prepStat.setInt(psPosition, value==null?0:value);
        }catch(Exception e){
          value = field.getInt(obj);
          prepStat.setInt(psPosition, value==null?0:value);
        }  
        
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      try{
        field.set(obj, new Integer(Integer.parseInt(value)));
      }catch(Exception e){
        field.setInt(obj, Integer.parseInt(value));    
      }  
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      try{
        return field.getInt(obj) + "";
      }catch(Exception e){
        Integer val = (Integer)field.get(obj);
        try{
          return ""+val.intValue(); 
        }catch(Exception ei){
          if (mf.supportSpaceValue) return "";
          return "0";
        }  
      }  
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };

}
