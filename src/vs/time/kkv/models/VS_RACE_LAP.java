/*** KKV Class Generator V.0.1, This class is based on 'VS_RACE_LAP' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import java.sql.Connection;
import java.sql.Time;
import vs.time.kkv.connector.MainForm;

public class VS_RACE_LAP {
  
  public long ID;   //  NOT_DETECTED
  public long RACE_ID;   //  NOT_DETECTED
  public long STAGE_ID;
  public long GROUP_NUM;
  public int BASE_ID;   //  NOT_DETECTED
  public int TRANSPONDER_ID;   //  NOT_DETECTED
  public int NUMBER_PACKET;   //  NOT_DETECTED
  public long TRANSPONDER_TIME;   //  NOT_DETECTED
  public long TIME_START = 0;
  public long TIME_FROM_START = 0;
  public int LAP;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_RACE_LAP() {
  };
  
  public static DBModelControl<VS_RACE_LAP> dbControl = new DBModelControl<VS_RACE_LAP>(VS_RACE_LAP.class, "VS_RACE_LAP", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("STAGE_ID").setDbFieldName("\"STAGE_ID\""),
    new DBModelField("GROUP_NUM").setDbFieldName("\"GROUP_NUM\""),
    new DBModelField("BASE_ID").setDbFieldName("\"BASE_ID\""),
    new DBModelField("TRANSPONDER_ID").setDbFieldName("\"TRANSPONDER_ID\""),
    new DBModelField("NUMBER_PACKET").setDbFieldName("\"NUMBER_PACKET\""),
    new DBModelField("TRANSPONDER_TIME").setDbFieldName("\"TRANSPONDER_TIME\""),
    new DBModelField("TIME_START").setDbFieldName("\"TIME_START\""),
    new DBModelField("TIME_FROM_START").setDbFieldName("\"TIME_FROM_START\""),
    new DBModelField("LAP").setDbFieldName("\"LAP\""),
  });
 
  public static VS_RACE_LAP saveTime(Connection con, VS_STAGE_GROUP group, long time, int TRANSPONDER_ID, int LAP){
    try{
      VS_RACE_LAP.dbControl.delete(con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and LAP=?",group.stage.RACE_ID,group.stage.ID,group.GROUP_NUM, LAP);
    }catch(Exception e){}
    try{
      VS_RACE_LAP lap = new VS_RACE_LAP();
      lap.BASE_ID = -1;
      lap.RACE_ID = group.stage.RACE_ID;
      lap.STAGE_ID = group.stage.ID;
      lap.GROUP_NUM = group.GROUP_NUM;
      lap.NUMBER_PACKET = 0;
      lap.TRANSPONDER_ID = TRANSPONDER_ID;
      lap.TRANSPONDER_TIME = time;
      lap.LAP = LAP;                        
      VS_RACE_LAP.dbControl.insert(con,lap);
      for (VS_STAGE_GROUPS usr : group.users){
        if (usr.TRANSPONDER==TRANSPONDER_ID){
          usr.IS_RECALULATED = 0;   
        }
      }      
      return lap;
    }catch(Exception e){
      MainForm._toLog(e);         
    }
    return null;
  }
}
