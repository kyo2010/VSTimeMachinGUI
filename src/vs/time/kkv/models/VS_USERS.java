/*** KKV Class Generator V.0.1, This class is based on 'VS_USERS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import java.sql.Time;

public class VS_USERS {
  
  public int ID = -1;
  //public int VSID;   //  NOT_DETECTED
  public int VSID1;   //  NOT_DETECTED
  public int VSID2;   //  NOT_DETECTED
  public int VSID3;   //  NOT_DETECTED
  public String VS_NAME;   //  NOT_DETECTED
  public String VS_NAME_UPPER;   //  NOT_DETECTED
  public int VS_SOUND_EFFECT = 1;   //  NOT_DETECTED
  public String FIRST_NAME;   //  NOT_DETECTED
  public String SECOND_NAME;   //  NOT_DETECTED
  public String WEB_SYSTEM;
  public String WEB_SID;
  public String PHOTO;
  public String REGION = "";
  public String FAI = "";
  
  
  /** Constructor */ 
  public VS_USERS() {
  };
  
  public static DBModelControl<VS_USERS> dbControl = new DBModelControl<VS_USERS>(VS_USERS.class, "VS_USERS", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("VSID1").setDbFieldName("\"VSID\""),
    new DBModelField("VSID2").setDbFieldName("\"VSID2\""),
    new DBModelField("VSID3").setDbFieldName("\"VSID3\""),
    new DBModelField("VS_NAME").setDbFieldName("VS_NAME"),
    new DBModelField("VS_SOUND_EFFECT").setDbFieldName("\"VS_SOUND_EFFECT\""),
    new DBModelField("VS_NAME_UPPER").setDbFieldName("VS_NAME_UPPER"),
    
    new DBModelField("FIRST_NAME").setDbFieldName("\"FIRST_NAME\""),
    new DBModelField("SECOND_NAME").setDbFieldName("\"SECOND_NAME\""),
    new DBModelField("WEB_SYSTEM").setDbFieldName("\"WEB_SYSTEM\""),
    new DBModelField("WEB_SID").setDbFieldName("WEB_SID"),
    
    new DBModelField("PHOTO").setDbFieldName("PHOTO"),
    new DBModelField("REGION").setDbFieldName("REGION"),
    new DBModelField("FAI").setDbFieldName("FAI"),    
    
  });
  
  public void setName(String name){
    VS_NAME = name;
    VS_NAME_UPPER = VS_NAME.toUpperCase();
  }
  
}
