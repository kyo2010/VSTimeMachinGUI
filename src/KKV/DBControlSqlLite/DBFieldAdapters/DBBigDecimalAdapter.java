package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.Utils.UserException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBBigDecimalAdapter implements DBFieldAdapter {
   
    public final int SCALE = 7;
  
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == BigDecimal.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter

    @Override
    public Class getAdapterClass(){
      return BigDecimal.class;
    };
    
    
    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        BigDecimal value = resultSet.getBigDecimal(rsPosition);
        field.set(obj, value);
    }

    @Override
    public void setPSField(Field field, Object obj, PreparedStatement prepStat, int psPosition, int ArrayIndex, DBModelField mf) throws SQLException, IllegalAccessException {
        BigDecimal value = null;
        try {
            value = (BigDecimal) field.get(obj);
        } catch (Exception e) {
            // value = new BigDecimal(field.getDouble(obj));
            // throw new UserException("Error getting BigDecimal field",e);
        }
        if (value!=null) value = value.setScale(SCALE,RoundingMode.CEILING);
        /*if (mf!=null && mf.NUMBER_OF_DECIMAL!=-1)
            out_value = out_value.setScale(mf.NUMBER_OF_DECIMAL,RoundingMode.CEILING);*/
        prepStat.setBigDecimal(psPosition, value);
    }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException, UserException {      
      String old_value = value;
      BigDecimal out_value = BigDecimal.ZERO;
      try{
        if (value==null || value.trim().equalsIgnoreCase("")){
          field.set(obj, BigDecimal.ZERO);
        }else{
          if (value.length()>=2 && value.charAt(value.length()-1)=='-'){
            value = "-"+value.substring(0, value.length()-1);
          }
          value = value.replaceAll(" ", "");
          //value = value.replaceAll(".", "");
          int pos1 = value.lastIndexOf(".");
          int pos2 = value.lastIndexOf(",");
          if (pos1>pos2) value = value.replaceAll(",", "");
          if (pos2>pos1) {
            value = value.replaceAll("\\.", "");
            value = value.replaceAll(",", ".");                    
          }          
          if (value.charAt(value.length()-1)=='.') value = value.substring(0,value.length()-1);
          out_value = new BigDecimal(Double.parseDouble(value));
          if (mf!=null && mf.NUMBER_OF_DECIMAL!=-1)
            out_value = out_value.setScale(mf.NUMBER_OF_DECIMAL,RoundingMode.CEILING);
          field.set(obj, out_value);
        } 
      }catch(Exception e){
        throw new UserException("Error","Incorrect amount : "+old_value);
      }  
      //System.out.println("in:"+old_value+" out:"+out_value.toString());
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        BigDecimal val = (BigDecimal) field.get(obj);
        return val==null?"":(""+val.toString());
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return null;
    };
}
