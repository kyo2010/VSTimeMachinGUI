/*** KKV Class Generator V.0.1, This class is based on 'VS_SETTING' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import java.sql.Connection;
import java.sql.Time;

public class VS_SETTING {
  
  public String PARAM_NAME;   //  NOT_DETECTED
  public String PARAM_VALUE;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_SETTING() {
  };
  
  public static DBModelControl<VS_SETTING> dbControl = new DBModelControl<VS_SETTING>(VS_SETTING.class, "VS_SETTING", new DBModelField[]{
    new DBModelField("PARAM_NAME").setDbFieldName("\"PARAM_NAME\""),
    new DBModelField("PARAM_VALUE").setDbFieldName("\"PARAM_VALUE\""),
  });
  
  
  public static String getParam(Connection conn, String paramName, String paramDefault) {
    try{
      VS_SETTING v1 = VS_SETTING.dbControl.getItem(conn, "PARAM_NAME=?", paramName);
      if (v1!=null){
        return v1.PARAM_VALUE;
      }  
    }catch(Exception e){
      e.printStackTrace();
    } 
    return paramDefault;
  }
  
  public static void setParam(Connection conn, String paramName, String paramValue) {
    try{
      VS_SETTING.dbControl.delete(conn, "PARAM_NAME=?", paramName);
      VS_SETTING v1 = new VS_SETTING();
      v1.PARAM_NAME = paramName;
      v1.PARAM_VALUE = paramValue;
      VS_SETTING.dbControl.save(conn, v1);
    }catch(UserException e){
      System.out.println("Error:"+e.error+", datails:"+e.details);              
    }catch(Exception e){
      e.printStackTrace();
    }     
  }
  
  public static int getParam(Connection conn, String paramName, int paramDefault) {
    try{
      VS_SETTING v1 = VS_SETTING.dbControl.getItem(conn, "PARAM_NAME=?", paramName);
      if (v1!=null){
        return Integer.parseInt(v1.PARAM_VALUE);
      }  
    }catch(Exception e){
      e.printStackTrace();
    } 
    return paramDefault;
  }
  
  public static double getParam(Connection conn, String paramName, double paramDefault) {
    try{
      VS_SETTING v1 = VS_SETTING.dbControl.getItem(conn, "PARAM_NAME=?", paramName);
      if (v1!=null){
        return Double.parseDouble(v1.PARAM_VALUE);
      }  
    }catch(Exception e){
      e.printStackTrace();
    } 
    return paramDefault;
  }
  
}
