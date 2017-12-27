package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.Utils.JDEDate;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Dmitriy Voloshin
 * Date: 07.11.12
 * Time: 12:33
 */
public class DBStringAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == String.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return String.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        String value = resultSet.getString(rsPosition);
        if (value!=null && dbmf.autoTrim) value=value.trim();        
        if (value!=null && dbmf.deleteFirstZero) {
          value=value.trim();        
          while(value.length()>0 && value.charAt(0)=='0'){
            value = value.substring(1);
          }
        }        
        if (dbmf.dbValueHandle!=null) value = (String)dbmf.dbValueHandle.prepareValue(value);
        field.set(obj, value);
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        String value = (String) field.get(obj);
        prepStat.setString(psPosition, value);
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        String val = ""+field.get(obj);
        //val = val.replaceAll("<", "&lt;");
        //val = val.replaceAll(">", "&gt;");
        return val;
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
