/*** KKV Class Generator V.0.1, This class is based on 'VS_STAGE_GROUPS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Time;

public class VS_STAGE_GROUPS {
  
  public long GID;   //  NOT_DETECTED
  public long STAGE_ID;   //  NOT_DETECTED
  public int GROUP_NUM;   //  NOT_DETECTED
  public int NUM_IN_GROUP;   //  NOT_DETECTED
  public String PILOT;   //  NOT_DETECTED
  public int TRANSPONDER;   //  NOT_DETECTED
  public String CHANNEL;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_STAGE_GROUPS() {
  };
  
  public static DBModelControl<VS_STAGE_GROUPS> dbControl = new DBModelControl<VS_STAGE_GROUPS>(VS_STAGE_GROUPS.class, "VS_STAGE_GROUPS", new DBModelField[]{
    new DBModelField("GID").setDbFieldName("\"GID\"").setAutoIncrement(),
    new DBModelField("STAGE_ID").setDbFieldName("\"STAGE_ID\""),
    new DBModelField("GROUP_NUM").setDbFieldName("\"GROUP_NUM\""),
    new DBModelField("NUM_IN_GROUP").setDbFieldName("\"NUM_IN_GROUP\""),
    new DBModelField("PILOT").setDbFieldName("\"PILOT\""),
    new DBModelField("TRANSPONDER").setDbFieldName("\"TRANSPONDER\""),
    new DBModelField("CHANNEL").setDbFieldName("\"CHANNEL\""),
  });
  
}
