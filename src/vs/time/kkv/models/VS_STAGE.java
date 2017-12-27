/*** KKV Class Generator V.0.1, This class is based on 'VS_PRACTICA' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Connection;
import java.sql.Time;
import vs.time.kkv.connector.MainForm;
import static vs.time.kkv.models.VS_REGISTRATION.dbControl;

public class VS_STAGE {
  
  public long ID;   //  NOT_DETECTED
  public long RACE_ID;   //  NOT_DETECTED
  public int FLAG_BY_PYLOT_TYPE;
  public int STAGE_TYPE;
  public int STAGE_NUM;   //  NOT_DETECTED
  public String CAPTION;   //  NOT_DETECTED
  public int COUNT_PILOTS_IN_GROUP;   //  NOT_DETECTED
  public String CHANNELS;   //  NOT_DETECTED
  public int MIN_LAP_TIME;
  public int LAPS;
  public int IS_GROUP_CREATED;
  public int IS_SELECTED;
  
  /** Constructor */ 
  public VS_STAGE() {
  };
  
  public static DBModelControl<VS_STAGE> dbControl = new DBModelControl<VS_STAGE>(VS_STAGE.class, "VS_STAGE", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("STAGE_TYPE").setDbFieldName("\"STAGE_TYPE\""),
    new DBModelField("FLAG_BY_PYLOT_TYPE").setDbFieldName("\"FLAG_BY_PYLOT_TYPE\""),
    new DBModelField("STAGE_NUM").setDbFieldName("\"STAGE_NUM\""),
    new DBModelField("CAPTION").setDbFieldName("\"CAPTION\""),
    new DBModelField("COUNT_PILOTS_IN_GROUP").setDbFieldName("\"COUNT_PILOTS_IN_GROUP\""),
    new DBModelField("CHANNELS").setDbFieldName("\"CHANNELS\""),
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("MIN_LAP_TIME").setDbFieldName("\"MIN_LAP_TIME\""),
    new DBModelField("IS_GROUP_CREATED").setDbFieldName("\"IS_GROUP_CREATED\""),
    new DBModelField("IS_SELECTED").setDbFieldName("\"IS_SELECTED\""),
  });
  
  public static void resetSelectedTab(Connection conn, long raceID){
    try{
      dbControl.execSql(conn, "UPDATE "+dbControl.getTableAlias()+" SET IS_SELECTED=0 WHERE RACE_ID=?",raceID);
    }catch(Exception e){
      MainForm.toLog(e);              
    }    
  }
  
}
