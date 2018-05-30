/** * KKV Class Generator V.0.1, This class is based on 'VS_STAGE_GROUPS' table.
 *** The class was generated automatically.  ** */
package vs.time.kkv.models;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import vs.time.kkv.connector.TimeMachine.VSColor;

public class VS_STAGE_GROUPS implements Transferable {

  public static final long MAX_TIME = (99 * 60 + 59) * 1000 + 999;

  public static DataFlavor VS_STAGE_GROUPS_FLOWER = null;

  static {
    try {
      String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + VS_STAGE_GROUPS[].class.getName() + "\"";
      VS_STAGE_GROUPS_FLOWER = new DataFlavor(mimeType);
    } catch (Exception e) {
    }
  }
  
  public long GID;   //  NOT_DETECTED
  public long STAGE_ID;   //  NOT_DETECTED
  public long GROUP_NUM;   //  NOT_DETECTED
  public long NUM_IN_GROUP;   //  NOT_DETECTED
  public long REG_ID;   //  NOT_DETECTED
  public int VS_PRIMARY_TRANS;   //  NOT_DETECTED
  public String CHANNEL;   //  NOT_DETECTED
  public String PILOT;   //  NOT_DETECTED
  public int isError = 0;
  public long LAPS;
  public long LAPS_INTO_BD = 0;
  public long BEST_LAP;
  public long FIRST_LAP;
  public long RACE_TIME;
  public long START_TIME = 0; // for postponing start
  public int IS_RECALULATED = 0;
  public int IS_FINISHED = 0;
  public int PILOT_TYPE = 0;
  public int SCORE = 0;
  public int WIN = 0;
  public int LOSE = 0;
  public int ACTIVE_FOR_NEXT_STAGE = 1;
  public int CHECK_FOR_RACE = 0;
  public VSColor color = null;
  public boolean RECEIVED_LAPS = true;
  
  public long QUAL_TIME = 0;
  public long QUAL_POS = 0;
  public long RACE_TIME_FINAL = 0;
  public long RACE_TIME_HALF_FINAL = 0;
  public long RACE_TIME_QUART_FINAL = 0;
  
  public long GROUP_FINAL = 0;
  public long GROUP_HALF_FINAL = 0;
  public long GROUP_QUART_FINAL = 0;
  public int GROUP_TYPE = 0; 

  public VS_REGISTRATION registration = null;   //  NOT_DETECTED

  public int wins = 0;
  public int loses = 0;

  public VS_STAGE_GROUP parent = null;

  /**
   * Constructor
   */
  public VS_STAGE_GROUPS() {
  }
  ; 
  
  public static DBModelControl<VS_STAGE_GROUPS> dbControl = new DBModelControl<VS_STAGE_GROUPS>(VS_STAGE_GROUPS.class, "VS_STAGE_GROUPS", new DBModelField[]{
    new DBModelField("GID").setDbFieldName("\"GID\"").setAutoIncrement(),
    new DBModelField("STAGE_ID").setDbFieldName("\"STAGE_ID\""),
    new DBModelField("GROUP_NUM").setDbFieldName("\"GROUP_NUM\""),
    new DBModelField("NUM_IN_GROUP").setDbFieldName("\"NUM_IN_GROUP\""),
    new DBModelField("REG_ID").setDbFieldName("\"REG_ID\""),
    new DBModelField("PILOT").setDbFieldName("\"PILOT\""),
    new DBModelField("VS_PRIMARY_TRANS").setDbFieldName("\"TRANSPONDER\""),
    new DBModelField("CHANNEL").setDbFieldName("\"CHANNEL\""),
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("BEST_LAP").setDbFieldName("\"BEST_LAP\""),
    new DBModelField("RACE_TIME").setDbFieldName("\"RACE_TIME\""),
    new DBModelField("IS_RECALULATED").setDbFieldName("\"IS_RECALULATED\""),
    new DBModelField("IS_FINISHED").setDbFieldName("\"IS_FINISHED\""),
    new DBModelField("SCORE").setDbFieldName("\"SCORE\""),
    new DBModelField("WIN").setDbFieldName("\"WIN\""),
    new DBModelField("LOSE").setDbFieldName("\"LOSE\""),
    new DBModelField("ACTIVE_FOR_NEXT_STAGE").setDbFieldName("\"ACTIVE_FOR_NEXT_STAGE\""),
    new DBModelField("CHECK_FOR_RACE").setDbFieldName("\"CHECK_FOR_RACE\""),
    new DBModelField("FIRST_LAP").setDbFieldName("\"FIRST_LAP\""),
  
    new DBModelField("QUAL_TIME").setDbFieldName("\"QUAL_TIME\""),
    new DBModelField("QUAL_POS").setDbFieldName("\"QUAL_POS\""),
    new DBModelField("RACE_TIME_FINAL").setDbFieldName("\"RACE_TIME_FINAL\""),
    new DBModelField("RACE_TIME_HALF_FINAL").setDbFieldName("\"RACE_TIME_HALF_FINAL\""),
    new DBModelField("RACE_TIME_QUART_FINAL").setDbFieldName("\"RACE_TIME_QUART_FINAL\""),
     
    new DBModelField("GROUP_FINAL").setDbFieldName("\"GROUP_FINAL\""),
    new DBModelField("GROUP_HALF_FINAL").setDbFieldName("\"GROUP_HALF_FINAL\""),
    new DBModelField("GROUP_QUART_FINAL").setDbFieldName("\"GROUP_QUART_FINAL\""),
    
    new DBModelField("GROUP_TYPE").setDbFieldName("\"GROUP_TYPE\""),    
  });

  public VS_REGISTRATION getRegistration(Connection conn, long raceID){
    loadRegistration(conn,raceID);
    return registration;
  }
  
  public void resetRegistration(){
    registration = null;
  };
  
  public void loadRegistration(Connection conn, long raceID) {
    try {
      if (registration == null && REG_ID != 0) {
        registration = VS_REGISTRATION.dbControl.getItem(conn, "VS_RACE_ID=? and ID=?", raceID, REG_ID);
      }
      if (registration == null && (REG_ID == -1 || REG_ID==0) ) {
        registration = VS_REGISTRATION.dbControl.getItem(conn, "VS_RACE_ID=? and VS_USER_NAME=?", raceID, PILOT.trim());
        if (registration!=null) {
          REG_ID = registration.ID;
          dbControl.update(conn, this);
        }  
      }
    } catch (Exception e) {
    }
  }
  
  public Collection<Integer> getUserTransponders(Connection conn, long raceID){
    loadRegistration(conn, raceID);
    Set<Integer> trans = new TreeSet<>();
    //List<Integer> trans = new ArrayList<>();
    trans.add(VS_PRIMARY_TRANS);
    if (registration!=null){
      if (registration.VS_TRANS1!=0 && registration.VS_TRANS1!=-1)
        trans.add(registration.VS_TRANS1);
      if (registration.VS_TRANS2!=0 && registration.VS_TRANS2!=-1)
        trans.add(registration.VS_TRANS2);
      if (registration.VS_TRANS3!=0 && registration.VS_TRANS3!=-1)      
        trans.add(registration.VS_TRANS3); 
    }
    return trans;        
  }
  
  
  public boolean isTransponderForUser(Connection conn, long raceID, int trans){
    if (this.VS_PRIMARY_TRANS==trans) return true;
    loadRegistration(conn, raceID);
    if (registration!=null){
      if (registration.VS_TRANS1==trans) return true;
      if (registration.VS_TRANS2==trans) return true;
      if (registration.VS_TRANS3==trans) return true;
    } 
    return false;
  }

  public String toString() {
    return PILOT + " (" + CHANNEL + ")";
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{VS_STAGE_GROUPS_FLOWER};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)) {
      return true;
    }
    return false;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)) {
      return this;
    }
    return null;
  }

  public VS_STAGE_GROUPS copy() throws UserException {
    VS_STAGE_GROUPS inst = (VS_STAGE_GROUPS) dbControl.copyObject((VS_STAGE_GROUPS) this);
    return inst;
  }

  public static long getMaxNumInGroup(Connection conn, long stageId, long groupNum) throws UserException {
    long max = VS_STAGE_GROUPS.dbControl.getMax(conn,
            "NUM_IN_GROUP", "STAGE_ID=? and GROUP_NUM=?", stageId, groupNum);
    return max;
  }

  /** return maximum laps */
  public long recalculateLapTimes(Connection conn, VS_STAGE stage, boolean please_recalculate) {
    long result = 0;
    try {      
      if (IS_RECALULATED == 1 && please_recalculate != true) {

      } else {
        long best_time_lap = MAX_TIME;
        long _RACE_TIME = 0;
        LAPS = 0;
        boolean all_laps_is_exist = false;
        try {
          if (stage.ID==114){
            int y = 0;
          }          
          boolean hasSkip = false;
          
          Map<String, VS_RACE_LAP> laps = null;
          if (stage.USE_REG_ID_FOR_LAP==1){
            laps = stage.laps_check_reg_id.get("" + GROUP_NUM).get("" + REG_ID);
          }else{
            laps = stage.laps_check_reg_id.get("" + GROUP_NUM).get("" + VS_PRIMARY_TRANS);
          }     
          LAPS_INTO_BD = stage.LAPS;
          for (VS_RACE_LAP lap : laps.values()){
            if (lap!=null && LAPS_INTO_BD<lap.LAP) LAPS_INTO_BD=lap.LAP;
          }     
          result = LAPS_INTO_BD;
          
          for (int lap_num = 1; lap_num <= stage.LAPS; lap_num++) {
            //int lap = Integer.parseInt(lap_st);
            VS_RACE_LAP lap = laps.get("" + lap_num);
            if (lap != null) {
              if (lap.TRANSPONDER_TIME < best_time_lap) {
                best_time_lap = lap.TRANSPONDER_TIME;
              }
              LAPS++;
              _RACE_TIME += lap.TRANSPONDER_TIME;
              if (lap_num == stage.LAPS && !hasSkip) {
                all_laps_is_exist = true;

              }
            } else {
              hasSkip = true;
            }
          }
        } catch (Exception ein) {
        }
        if (LAPS > 0) {
          BEST_LAP = best_time_lap;
        }
        if (all_laps_is_exist) {
          RACE_TIME = _RACE_TIME;
        }
        if (all_laps_is_exist) {
          IS_FINISHED = 1;
        }
        if (IS_FINISHED == 1) {
          if (BEST_LAP == 0) {
            BEST_LAP = MAX_TIME;
          }
          if (!all_laps_is_exist) {
            RACE_TIME = MAX_TIME;
          }
        }
        IS_RECALULATED = 1;
        //System.out.println("VS_STAGE_GROUPS - recal lap");
        //System.out.println(GID+" "+PILOT+" "+stage.CAPTION+" "+RACE_TIME);
        VS_STAGE_GROUPS.dbControl.update(conn, this);
      }
    } catch (Exception e) {
    }
    return result;
  }  
}
