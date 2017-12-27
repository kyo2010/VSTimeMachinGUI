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
public class DBDoubleAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == Double.class || field.getType() == double.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return Double.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        Double value = resultSet.getDouble(rsPosition);
        if (dbmf.dbValueHandle!=null) value = (Double)dbmf.dbValueHandle.prepareValue(value);
        field.set(obj, value);
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        Double value = null;
        try {
            value = field.getDouble(obj);
        } catch (Exception e) {
            value = (Double) field.get(obj);
        }
        prepStat.setDouble(psPosition, value);
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {     
      if (value=="") value = "0";
      value = value.replaceAll(",", ".");
      field.setDouble(obj, Double.parseDouble(value));       
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        return field.getDouble(obj) + "";
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
