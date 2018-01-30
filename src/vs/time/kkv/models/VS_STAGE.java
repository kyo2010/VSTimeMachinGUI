/*** KKV Class Generator V.0.1, This class is based on 'VS_PRACTICA' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import vs.time.kkv.connector.MainForm;
import static vs.time.kkv.models.VS_REGISTRATION.dbControl;

public class VS_STAGE {
  
  public long ID = -1;   //  NOT_DETECTED
  public long RACE_ID;   //  NOT_DETECTED
  public int FLAG_BY_PYLOT_TYPE;
  public int STAGE_TYPE;
  public int STAGE_NUM;   //  NOT_DETECTED
  public String CAPTION;   //  NOT_DETECTED
  public int COUNT_PILOTS_IN_GROUP;   //  NOT_DETECTED
  public String CHANNELS;   //  NOT_DETECTED
  public int MIN_LAP_TIME;
  public int LAPS;
  public int IS_GROUP_CREATED;
  public int IS_SELECTED;  
    
  public Map<Integer,VS_STAGE_GROUP> groups = new HashMap<Integer,VS_STAGE_GROUP>();
  
  /** Constructor */ 
  public VS_STAGE() {
  };
  
  public static DBModelControl<VS_STAGE> dbControl = new DBModelControl<VS_STAGE>(VS_STAGE.class, "VS_STAGE", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("STAGE_TYPE").setDbFieldName("\"STAGE_TYPE\""),
    new DBModelField("FLAG_BY_PYLOT_TYPE").setDbFieldName("\"FLAG_BY_PYLOT_TYPE\""),
    new DBModelField("STAGE_NUM").setDbFieldName("\"STAGE_NUM\""),
    new DBModelField("CAPTION").setDbFieldName("\"CAPTION\""),
    new DBModelField("COUNT_PILOTS_IN_GROUP").setDbFieldName("\"COUNT_PILOTS_IN_GROUP\""),
    new DBModelField("CHANNELS").setDbFieldName("\"CHANNELS\""),
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("MIN_LAP_TIME").setDbFieldName("\"MIN_LAP_TIME\""),
    new DBModelField("IS_GROUP_CREATED").setDbFieldName("\"IS_GROUP_CREATED\""),
    new DBModelField("IS_SELECTED").setDbFieldName("\"IS_SELECTED\""),
  });
  
  public static void resetSelectedTab(Connection conn, long raceID){
    try{      
      dbControl.execSql(conn, "UPDATE "+dbControl.getTableAlias()+" SET IS_SELECTED=0 WHERE RACE_ID=?",raceID);
    }catch(Exception e){
      MainForm._toLog(e);              
    }    
  }
  
  public void loadGroups(Connection conn){
    try{
      groups.clear();
      int GROUP_INDEX=0;
      Map<Long,Integer> indexes = new HashMap<>();
      List<VS_STAGE_GROUPS> users = VS_STAGE_GROUPS.dbControl.getList(conn, "STAGE_ID=? ORDER BY GROUP_NUM, NUM_IN_GROUP", ID);
      for (VS_STAGE_GROUPS usr : users){
        long db_group_index = usr.GROUP_NUM;
        Integer phisical_group_index = indexes.get(db_group_index);
        if (phisical_group_index==null){
          phisical_group_index = GROUP_INDEX;
          indexes.put(db_group_index,phisical_group_index);
          GROUP_INDEX++;
        }                
        VS_STAGE_GROUP group = groups.get(phisical_group_index);
        if (group==null){
          group = new VS_STAGE_GROUP(this);   
          groups.put(phisical_group_index, group);
          group.GROUP_INDEX = GROUP_INDEX;
          group.GROUP_NUM = usr.GROUP_NUM;         
          group.GROUP_INDEX = phisical_group_index;                    
        }        
        group.users.add(usr);        
        usr.parent = group;        
      }
      checkConstarin();
    }catch(Exception e){
      MainForm._toLog(e);              
    }
  }
  
  public void checkConstarin(){
    TreeSet<String> userNames = new TreeSet<String>();
    for (Integer groupID : groups.keySet()){
      VS_STAGE_GROUP group = groups.get(groupID);
      group.useChannels = "";
      for (VS_STAGE_GROUPS usr : group.users){
        usr.isError = 0;
        if (group.useChannels.indexOf(usr.CHANNEL)>=0){
          usr.isError = 1;
        }
        if (userNames.contains(usr.PILOT)){
          usr.isError = 2;
        }        
        userNames.add(usr.PILOT);        
        group.useChannels += usr.CHANNEL+";";        
      }
    } 
  }
  
  public String toString(){
    return CAPTION;
  }
  
}
