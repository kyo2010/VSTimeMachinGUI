/*** KKV Class Generator V.0.1, This class is based on 'VS_USERS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import java.sql.Time;

public class VS_USERS {
  
  public int ID = -1;
  public int VSID;   //  NOT_DETECTED
  public String VS_NAME;   //  NOT_DETECTED
  public String VS_NAME_UPPER;   //  NOT_DETECTED
  public int VS_SOUND_EFFECT = 1;   //  NOT_DETECTED
  
  /** Constructor */ 
  public VS_USERS() {
  };
  
  public static DBModelControl<VS_USERS> dbControl = new DBModelControl<VS_USERS>(VS_USERS.class, "VS_USERS", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("VSID").setDbFieldName("\"VSID\""),
    new DBModelField("VS_NAME").setDbFieldName("VS_NAME"),
    new DBModelField("VS_SOUND_EFFECT").setDbFieldName("\"VS_SOUND_EFFECT\""),
    new DBModelField("VS_NAME_UPPER").setDbFieldName("VS_NAME_UPPER"),
  });
  
  public void setName(String name){
    VS_NAME = name;
    VS_NAME_UPPER = VS_NAME.toUpperCase();
  }
  
}
