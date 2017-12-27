/*** KKV Class Generator V.0.1, This class is based on 'VS_REGISTRATION' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Connection;
import java.sql.Time;

public class VS_REGISTRATION {
  
  public long ID = -1;   //  NOT_DETECTED  
  public long VS_RACE_ID;   //  NOT_DETECTED
  public long NUM;
  public int VS_TRANSPONDER;   //  NOT_DETECTED
  public String VS_USER_NAME;   //  NOT_DETECTED
  public int IS_ACTIVE;   //  NOT_DETECTED
  public int VS_SOUND_EFFECT;
  public int PILOT_TYPE;
  
  /** Constructor */ 
  public VS_REGISTRATION() {
  };
  
  public static DBModelControl<VS_REGISTRATION> dbControl = new DBModelControl<VS_REGISTRATION>(VS_REGISTRATION.class, "VS_REGISTRATION", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("NUM").setDbFieldName("\"NUM\""),
    new DBModelField("VS_RACE_ID").setDbFieldName("\"VS_RACE_ID\""),
    new DBModelField("VS_TRANSPONDER").setDbFieldName("\"VS_TRANSPONDER\""),
    new DBModelField("VS_USER_NAME").setDbFieldName("\"VS_USER_NAME\""),
    new DBModelField("IS_ACTIVE").setDbFieldName("\"IS_ACTIVE\""),
    new DBModelField("VS_SOUND_EFFECT").setDbFieldName("\"VS_SOUND_EFFECT\""),
    new DBModelField("PILOT_TYPE").setDbFieldName("\"PILOT_TYPE\""),
  });
  
  public static long maxNum(Connection conn, long raceID){
    long num = 0;
    try{
      num = dbControl.getMax(conn, "NUM", "VS_RACE_ID=?", raceID);
    }catch(Exception e){}
    return num;
  }
  
}
