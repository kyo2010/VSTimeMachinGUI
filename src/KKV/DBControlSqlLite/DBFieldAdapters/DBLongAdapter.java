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
public class DBLongAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == Long.class || field.getType() == long.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter

    @Override
    public Class getAdapterClass(){
      return Long.class;
    };
    
    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        Long value = resultSet.getLong(rsPosition);
        if (dbmf.dbValueHandle!=null) value = (Long)dbmf.dbValueHandle.prepareValue(value);
        try{
          field.set(obj, value);
        }catch(Exception e){
          field.set(obj, 0);
        }  
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        Long value = null;
        try{
          value = (Long)field.get(obj);
        }catch(Exception e){
          value = field.getLong(obj);
        }
        prepStat.setLong(psPosition, value);
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        field.setLong(obj, Long.parseLong(value));
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
      try{
        return ((Long)field.get(obj)).toString();
      }catch(Exception e){
        return field.getLong(obj) + "";
      }  
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
