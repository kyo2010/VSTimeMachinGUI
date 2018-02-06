/*** KKV Class Generator V.0.1, This class is based on 'VS_STAGE_GROUPS' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Time;
import java.util.Map;

public class VS_STAGE_GROUPS implements Transferable{
  
  public static final long MAX_TIME = (99*60+59)*1000+999;

  public static DataFlavor VS_STAGE_GROUPS_FLOWER = null;
  static{
    try{
      String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" +VS_STAGE_GROUPS[].class.getName() + "\"";
      VS_STAGE_GROUPS_FLOWER = new DataFlavor(mimeType);
    }catch(Exception e){}
  };
  
  public long GID;   //  NOT_DETECTED
  public long STAGE_ID;   //  NOT_DETECTED
  public long GROUP_NUM;   //  NOT_DETECTED
  public long NUM_IN_GROUP;   //  NOT_DETECTED
  public String PILOT;   //  NOT_DETECTED
  public int TRANSPONDER;   //  NOT_DETECTED
  public String CHANNEL;   //  NOT_DETECTED
  public int isError = 0;
  public long LAPS;
  public long BEST_LAP;
  public long RACE_TIME;
  public int IS_RECALULATED = 0;
  public int IS_FINISHED = 0;
  
  public VS_STAGE_GROUP parent = null;
  
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
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("BEST_LAP").setDbFieldName("\"BEST_LAP\""),
    new DBModelField("RACE_TIME").setDbFieldName("\"RACE_TIME\""),
    new DBModelField("IS_RECALULATED").setDbFieldName("\"IS_RECALULATED\""),
    new DBModelField("IS_FINISHED").setDbFieldName("\"IS_FINISHED\""),
  });
  
  public String toString(){
    return  PILOT + " ("+CHANNEL+")";
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{VS_STAGE_GROUPS_FLOWER};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)){
      return true;
    }
     return false;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)){
      return this;
    }
    return null;
  }
  
  public VS_STAGE_GROUPS copy() throws UserException{
    VS_STAGE_GROUPS inst = (VS_STAGE_GROUPS) dbControl.copyObject((VS_STAGE_GROUPS)this);
    return inst;
  }
  
  public static long getMaxNumInGroup(Connection conn, long stageId, long groupNum) throws UserException{
    long max = VS_STAGE_GROUPS.dbControl.getMax(conn,
                "NUM_IN_GROUP", "STAGE_ID=? and GROUP_NUM=?", stageId, groupNum);
    return max;
  }
  
  public void recalculateLapTimes(Connection conn, VS_STAGE stage, Map<String, Map<String, Map<String, VS_RACE_LAP>>> laps, boolean please_recalculate){
    try{
      if (IS_RECALULATED==1 && please_recalculate!=true){
        
      }else{        
        long best_time_lap = MAX_TIME;
        long _RACE_TIME = 0;
        LAPS = 0;
        for (String lap_st : laps.get(""+GROUP_NUM).get(""+TRANSPONDER).keySet()){
          //int lap = Integer.parseInt(lap_st);
          VS_RACE_LAP lap = laps.get(""+GROUP_NUM).get(""+TRANSPONDER).get(lap_st);
          if (lap.TRANSPONDER_TIME<best_time_lap) best_time_lap = lap.TRANSPONDER_TIME;   
          LAPS++;
          _RACE_TIME += lap.TRANSPONDER_TIME;
        } 
        if (LAPS>0){
          BEST_LAP = best_time_lap;         
        }
        if ((stage.LAPS == (int)LAPS)){
          RACE_TIME = _RACE_TIME;
        } 
        if (IS_FINISHED==1){
          if (BEST_LAP==0) BEST_LAP = MAX_TIME;
          if ((stage.LAPS != (int)LAPS)){
            RACE_TIME = MAX_TIME;
          }
        }
        IS_RECALULATED = 1;
        VS_STAGE_GROUPS.dbControl.update(conn, this);
      }
    }catch(Exception e){
    }  
  }
}
