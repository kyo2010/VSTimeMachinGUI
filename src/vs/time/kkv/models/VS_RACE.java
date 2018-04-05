/*** KKV Class Generator V.0.1, This class is based on 'VS_RACE' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.DBModelControl;
import KKV.Utils.JDEDate;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class VS_RACE {
  
  public int RACE_ID = -1;   //  NOT_DETECTED
  public String RACE_NAME = "Race";   //  NOT_DETECTED
  public JDEDate RACE_DATE = new JDEDate();   //  NOT_DETECTED
  public int COUNT_OF_LAPS = 3;   //  NOT_DETECTED
  public int MIN_LAP_TIME = 18;
  public long MAX_RACE_TIME = 300;
  public int IS_ACTIVE = 0;
  public int PLEASE_IGNORE_FIRST_LAP = 0;
  public String CHANNEL1 = "";
  public String CHANNEL2 = "";
  public String CHANNEL3 = "";
  public String CHANNEL4 = "";
  public String WEB_SYSTEM_SID = "";
  public String WEB_SYSTEM_CAPTION = "";
  public String JUDGE = "";
  public String SECRETARY = "";
          
  
  public List<VS_REGISTRATION> users = new ArrayList<>();
  
  
  /** Constructor */ 
  public VS_RACE() {
  };
  
  public static DBModelControl<VS_RACE> dbControl = new DBModelControl<VS_RACE>(VS_RACE.class, "VS_RACE", new DBModelField[]{
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\"").setAutoIncrement(),
    new DBModelField("RACE_NAME").setDbFieldName("\"RACE_NAME\""),
    new DBModelField("RACE_DATE").setDbFieldName("\"RACE_DATE\""),
    new DBModelField("COUNT_OF_LAPS").setDbFieldName("\"COUNT_OF_LAPS\""),
    new DBModelField("MIN_LAP_TIME").setDbFieldName("\"MIN_LAP_TIME\""),
    new DBModelField("IS_ACTIVE").setDbFieldName("\"IS_ACTIVE\""),
    new DBModelField("CHANNEL1").setDbFieldName("\"CHANNEL1\""),
    new DBModelField("CHANNEL2").setDbFieldName("\"CHANNEL2\""),
    new DBModelField("CHANNEL3").setDbFieldName("\"CHANNEL3\""),
    new DBModelField("CHANNEL4").setDbFieldName("\"CHANNEL4\""),
    new DBModelField("PLEASE_IGNORE_FIRST_LAP").setDbFieldName("\"PLEASE_IGNORE_FIRST_LAP\""),
    new DBModelField("MAX_RACE_TIME").setDbFieldName("\"MAX_RACE_TIME\""),

    new DBModelField("WEB_SYSTEM_SID").setDbFieldName("\"WEB_SYSTEM_SID\""),
    new DBModelField("WEB_SYSTEM_CAPTION").setDbFieldName("\"WEB_SYSTEM_CAPTION\""),    
    
    new DBModelField("JUDGE").setDbFieldName("\"JUDGE\""),    
    new DBModelField("SECRETARY").setDbFieldName("\"SECRETARY\""),    
  });
  
}
