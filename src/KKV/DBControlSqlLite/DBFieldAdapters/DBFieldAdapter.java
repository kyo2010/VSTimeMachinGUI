package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.UserException;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBFieldAdapter { 
    public Boolean isValidClass(Field field);//if not valid try next adapter
    
    public Class getAdapterClass();

    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws UserException, IllegalAccessException, SQLException;

    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws UserException, SQLException, IllegalAccessException;

    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws UserException, IllegalAccessException;

    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws UserException, IllegalAccessException;
    
    public String getDeafaultCellID(DBModelField mf);
}
