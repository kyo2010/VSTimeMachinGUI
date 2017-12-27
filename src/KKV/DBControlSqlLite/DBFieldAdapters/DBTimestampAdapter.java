package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Author: Dmitriy Voloshin
 * Date: 07.11.12
 * Time: 12:33
 */
public class DBTimestampAdapter implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == Timestamp.class) {
            return true;
        }
        return false;
    }
    
    @Override
    public Class getAdapterClass(){
      return Timestamp.class;
    };

    //to model field
    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        Timestamp value = resultSet.getTimestamp(rsPosition);
        if (dbmf.dbValueHandle!=null) value = (Timestamp)dbmf.dbValueHandle.prepareValue(value);
        field.set(obj, value);
    }

    //to db field
    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        Timestamp value = (Timestamp) field.get(obj);
        if (value == null) {
            //current timestamp
            value = new Timestamp(Calendar.getInstance().getTime().getTime());
        }
        prepStat.setTimestamp(psPosition, value);
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        return field.get(obj) + "";
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
