/*** KKV Class Generator V.0.1, This class is based on 'VS_REGISTRATION' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Connection;
import java.sql.Time;

public class VS_REGISTRATION {
  
  public long ID = -1;   //  NOT_DETECTED  
  public long VS_RACE_ID;   //  NOT_DETECTED
  //public long USER_REG_ID;   //  NOT_DETECTED
  public long NUM;
//public int VS_TRANSPONDER;   //  NOT_DETECTED
  public int VS_TRANS1;   //  NOT_DETECTED
  public int VS_TRANS2;   //  NOT_DETECTED
  public int VS_TRANS3;   //  NOT_DETECTED
  
  public String VS_USER_NAME;   //  NOT_DETECTED
  public int IS_ACTIVE;   //  NOT_DETECTED
  public int VS_SOUND_EFFECT;
  public int PILOT_TYPE;  
  
  public String FIRST_NAME;   //  NOT_DETECTED
  public String SECOND_NAME;   //  NOT_DETECTED
  public String WEB_SYSTEM;
  public String WEB_SID;
  
  /** Constructor */ 
  public VS_REGISTRATION() {
  };
  
  public static DBModelControl<VS_REGISTRATION> dbControl = new DBModelControl<VS_REGISTRATION>(VS_REGISTRATION.class, "VS_REGISTRATION", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("NUM").setDbFieldName("\"NUM\""),
    new DBModelField("VS_RACE_ID").setDbFieldName("\"VS_RACE_ID\""),
    //new DBModelField("USER_REG_ID").setDbFieldName("\"USER_REG_ID\""),
   // new DBModelField("VS_TRANSPONDER").setDbFieldName("\"VS_TRANSPONDER\""),
    new DBModelField("VS_TRANS1").setDbFieldName("\"VS_TRANSPONDER\""),
    new DBModelField("VS_TRANS2").setDbFieldName("\"VS_TRANS2\""),
    new DBModelField("VS_TRANS3").setDbFieldName("\"VS_TRANS3\""),
    
    new DBModelField("VS_USER_NAME").setDbFieldName("\"VS_USER_NAME\""),
    new DBModelField("IS_ACTIVE").setDbFieldName("\"IS_ACTIVE\""),
    new DBModelField("VS_SOUND_EFFECT").setDbFieldName("\"VS_SOUND_EFFECT\""),
    new DBModelField("PILOT_TYPE").setDbFieldName("\"PILOT_TYPE\""),
    
    new DBModelField("FIRST_NAME").setDbFieldName("\"FIRST_NAME\""),
    new DBModelField("SECOND_NAME").setDbFieldName("\"SECOND_NAME\""),
    new DBModelField("WEB_SYSTEM").setDbFieldName("\"WEB_SYSTEM\""),
    new DBModelField("WEB_SID").setDbFieldName("WEB_SID"),
  });
  
  public static long maxNum(Connection conn, long raceID){
    long num = 0;
    try{
      num = dbControl.getMax(conn, "NUM", "VS_RACE_ID=?", raceID);
    }catch(Exception e){}
    return num;
  }
  
  public String toString(){
    return VS_USER_NAME;
  }  
}
