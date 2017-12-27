/*** KKV Class Generator V.0.1, This class is based on 'VS_PRACTICA' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Time;

public class VS_PRACTICA {
  
  public long ID;   //  NOT_DETECTED
  public long RACE_ID;   //  NOT_DETECTED
  public int PRACTICA_NUM;   //  NOT_DETECTED
  public String CAPTION;   //  NOT_DETECTED
  public int PRACTICA_TYPE;   //  NOT_DETECTED
  public int COUNT_PILOTS_IN_GROUP;   //  NOT_DETECTED
  public String CHANNELS;   //  NOT_DETECTED
  public int MIN_LAP_TIME;
  public int LAPS;
  
  /** Constructor */ 
  public VS_PRACTICA() {
  };
  
  public static DBModelControl<VS_PRACTICA> dbControl = new DBModelControl<VS_PRACTICA>(VS_PRACTICA.class, "VS_PRACTICA", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("PRACTICA_NUM").setDbFieldName("\"PRACTICA_NUM\""),
    new DBModelField("CAPTION").setDbFieldName("\"CAPTION\""),
    new DBModelField("PRACTICA_TYPE").setDbFieldName("\"PRACTICA_TYPE\""),
    new DBModelField("COUNT_PILOTS_IN_GROUP").setDbFieldName("\"COUNT_PILOTS_IN_GROUP\""),
    new DBModelField("CHANNELS").setDbFieldName("\"CHANNELS\""),
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("MIN_LAP_TIME").setDbFieldName("\"MIN_LAP_TIME\""),
  });
  
}
