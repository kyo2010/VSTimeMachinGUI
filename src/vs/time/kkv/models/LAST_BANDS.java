/*** KKV Class Generator V.0.1, This class is based on 'LAST_BANDS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Time;

public class LAST_BANDS {
  
  public int NUM;   //  NOT_DETECTED
  public String NAME_BAND;   //  NOT_DETECTED
  
  /** Constructor */ 
  public LAST_BANDS() {
  };
  
  public static DBModelControl<LAST_BANDS> dbControl = new DBModelControl<LAST_BANDS>(LAST_BANDS.class, "LAST_BANDS", new DBModelField[]{
    new DBModelField("NUM").setDbFieldName("\"NUM\""),
    new DBModelField("NAME_BAND").setDbFieldName("\"NAME_BAND\""),
  });
  
}
