/*** KKV Class Generator V.0.1, This class is based on 'VS_BANDS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Time;

public class VS_BANDS {
  
  public String ID;   //  NOT_DETECTED
  public String POS;   //  NOT_DETECTED
  public String NAME;   //  NOT_DETECTED
  public String FREQUENCY;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_BANDS() {
  };
  
  public static DBModelControl<VS_BANDS> dbControl = new DBModelControl<VS_BANDS>(VS_BANDS.class, "VS_BANDS", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("POS").setDbFieldName("\"POS\""),
    new DBModelField("NAME").setDbFieldName("\"NAME\""),
    new DBModelField("FREQUENCY").setDbFieldName("\"FREQUENCY\""),
  });
  
}
