/*** KKV Class Generator V.0.1, This class is based on 'VS_RACE_LAP' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import java.sql.Time;

public class VS_RACE_LAP {
  
  public long ID;   //  NOT_DETECTED
  public int RACE_ID;   //  NOT_DETECTED
  public int CYCLE_ID;
  public int BASE_ID;   //  NOT_DETECTED
  public int TRANSPONDER_ID;   //  NOT_DETECTED
  public int NUMBER_PACKET;   //  NOT_DETECTED
  public long TRANSPONDER_TIME;   //  NOT_DETECTED
  public int LAP;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_RACE_LAP() {
  };
  
  public static DBModelControl<VS_RACE_LAP> dbControl = new DBModelControl<VS_RACE_LAP>(VS_RACE_LAP.class, "VS_RACE_LAP", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("CYCLE_ID").setDbFieldName("\"CYCLE_ID\""),
    new DBModelField("BASE_ID").setDbFieldName("\"BASE_ID\""),
    new DBModelField("TRANSPONDER_ID").setDbFieldName("\"TRANSPONDER_ID\""),
    new DBModelField("NUMBER_PACKET").setDbFieldName("\"NUMBER_PACKET\""),
    new DBModelField("TRANSPONDER_TIME").setDbFieldName("\"TRANSPONDER_TIME\""),
    new DBModelField("LAP").setDbFieldName("\"LAP\""),
  });
  
}
