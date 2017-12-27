/*** KKV Class Generator V.0.1, This class is based on 'VS_PRACTICA_GROUPS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Time;

public class VS_PRACTICA_GROUPS {
  
  public long GID;   //  NOT_DETECTED
  public long PRACTICA_ID;   //  NOT_DETECTED
  public int GROUP_NUM;   //  NOT_DETECTED
  public int NUM_IN_GROUP;   //  NOT_DETECTED
  public String PILOT;   //  NOT_DETECTED
  public int TRANSPONDER;   //  NOT_DETECTED
  public String CHANNEL;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_PRACTICA_GROUPS() {
  };
  
  public static DBModelControl<VS_PRACTICA_GROUPS> dbControl = new DBModelControl<VS_PRACTICA_GROUPS>(VS_PRACTICA_GROUPS.class, "VS_PRACTICA_GROUPS", new DBModelField[]{
    new DBModelField("GID").setDbFieldName("\"GID\"").setAutoIncrement(),
    new DBModelField("PRACTICA_ID").setDbFieldName("\"PRACTICA_ID\""),
    new DBModelField("GROUP_NUM").setDbFieldName("\"GROUP_NUM\""),
    new DBModelField("NUM_IN_GROUP").setDbFieldName("\"NUM_IN_GROUP\""),
    new DBModelField("PILOT").setDbFieldName("\"PILOT\""),
    new DBModelField("TRANSPONDER").setDbFieldName("\"TRANSPONDER\""),
    new DBModelField("CHANNEL").setDbFieldName("\"CHANNEL\""),
  });
  
}
