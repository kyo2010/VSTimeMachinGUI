/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.Utils.JDEDate;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;

/**
 *
 * @author kyo
 */
public class DBJDEAdaperWithTimeDDMMYYYY implements DBFieldAdapter {
    @Override
    public Boolean isValidClass(Field field) {
        if (field.getType() == JDEDateAndTimeDDMMYYYY.class) {
            return true;
        }
        return false;
    }//if not valid try next adapter
    
    @Override
    public Class getAdapterClass(){
      return JDEDateAndTimeDDMMYYYY.class;
    };

    @Override
    public void loadFromRS(Field field, Object obj, ResultSet resultSet, int rsPosition, DBModelField dbmf, int ArrayIndex) throws IllegalAccessException, SQLException {
        //Integer value = resultSet.getInt(rsPosition);
        //field.set(obj, value);
        //Date date = resultSet.getDate(rsPosition);
        Timestamp date = resultSet.getTimestamp(rsPosition);
        JDEDateAndTimeDDMMYYYY jdeDate = new JDEDateAndTimeDDMMYYYY();        
        if ( date!=null)
          jdeDate.setDate(date.getTime());
        else jdeDate=null;
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
                
          if (jd==null) 
            prepStat.setTimestamp(psPosition, null);
          else
            prepStat.setTimestamp(psPosition, jd.getTimestamp());   
     }

    @Override
    public void setField(Field field, Object obj, String propertyName, String value, int ArrayIndex, DBModelField mf) throws IllegalAccessException {
        //field.setInt(obj, Integer.parseInt(value));
        // xls_Date
        JDEDateAndTimeDDMMYYYY jd = new JDEDateAndTimeDDMMYYYY();
        String o_val = value;
        try{             
          double excelDate = Double.parseDouble(value); 
          jd.setExcelDateWithTime(excelDate);
        }catch(Exception e){          
          value = value.replaceAll("в", "");
          value = value.replaceAll("\\n", " ");
          /*if ("21.05.15 в 06-30".equalsIgnoreCase(o_val)){
            System.out.println(o_val);
          }*/
          if (value.indexOf(".")==2){
            value = value.replaceAll("-", ":");
            jd.setJDEDateAsDDMMYYYY_HH_MM_SS(value, ".",":");
          }else{
            jd.setJDEDateAsYYYYMMDD(value, "-");
          }  
        }  
        //System.out.println(o_val+" -> "+jd.getDateAsDDMMYYYY_andTime("."));
        if (value.equals("")) 
          field.set(obj,null);
        else
          field.set(obj, jd);
    }

    @Override
    public String getField(Field field, Object obj, String propertyName, int ArrayIndex, DBModelField mf) throws IllegalAccessException {      
      if (field.get(obj)==null) return "";
      JDEDate res = (JDEDate) field.get(obj);
      return res.getDateAsDDMMYYYY_andTime(".");
    }
    
    public String getDeafaultCellID(DBModelField mf){
      return "{dateMMDDYYYYandTime}";
    };
}
