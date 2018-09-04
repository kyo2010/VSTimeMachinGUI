/** * KKV Class Generator V.0.1, This class is based on 'VS_PRACTICA' table.
 *** The class was generated automatically.  ** */
package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import KKV.Utils.JDEDate;
import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
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
  public String COLORS = "";
  public int MIN_LAP_TIME;
  public int LAPS;
  public int IS_GROUP_CREATED;
  public int IS_SELECTED;
  public int SORT_TYPE;
  public int RACE_TYPE;
  public int PILOT_TYPE;
  public String PARENT_STAGE = "";
  public long PARENT_STAGE_ID = -1;
  public int IS_LOCK = 0;
  public int PILOTS_FOR_NEXT_ROUND = 3;
  public String REP_COLS = "";
  public int USE_REG_ID_FOR_LAP = 1;
  public StageTab tab = null;
  public String TRANSS = "";

  //public VS_RACE race = null;

  public Map<String, Map<String, Map<String, VS_RACE_LAP>>> laps_check_reg_id = null;

  public Map<Integer, VS_STAGE_GROUP> groups = new HashMap<Integer, VS_STAGE_GROUP>();

  /**
   * Constructor
   */
  public VS_STAGE() {
  }

  public static DBModelControl<VS_STAGE> dbControl = new DBModelControl<VS_STAGE>(VS_STAGE.class, "VS_STAGE", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("RACE_ID").setDbFieldName("\"RACE_ID\""),
    new DBModelField("STAGE_TYPE").setDbFieldName("\"STAGE_TYPE\""),
    new DBModelField("FLAG_BY_PYLOT_TYPE").setDbFieldName("\"FLAG_BY_PYLOT_TYPE\""),
    new DBModelField("STAGE_NUM").setDbFieldName("\"STAGE_NUM\""),
    new DBModelField("CAPTION").setDbFieldName("\"CAPTION\""),
    new DBModelField("COUNT_PILOTS_IN_GROUP").setDbFieldName("\"COUNT_PILOTS_IN_GROUP\""),
    new DBModelField("CHANNELS").setDbFieldName("\"CHANNELS\""),
    new DBModelField("COLORS").setDbFieldName("\"COLORS\""),
    new DBModelField("TRANSS").setDbFieldName("\"TRANSS\""),    
    new DBModelField("LAPS").setDbFieldName("\"LAPS\""),
    new DBModelField("MIN_LAP_TIME").setDbFieldName("\"MIN_LAP_TIME\""),
    new DBModelField("IS_GROUP_CREATED").setDbFieldName("\"IS_GROUP_CREATED\""),
    new DBModelField("IS_SELECTED").setDbFieldName("\"IS_SELECTED\""),
    new DBModelField("PARENT_STAGE").setDbFieldName("\"PARENT_STAGE\""),
    new DBModelField("SORT_TYPE").setDbFieldName("\"SORT_TYPE\""),
    new DBModelField("RACE_TYPE").setDbFieldName("\"RACE_TYPE\""),
    new DBModelField("PILOT_TYPE").setDbFieldName("\"PILOT_TYPE\""),
    new DBModelField("PARENT_STAGE_ID").setDbFieldName("\"PARENT_STAGE_ID\""),
    new DBModelField("IS_LOCK").setDbFieldName("\"IS_LOCK\""),
    new DBModelField("PILOTS_FOR_NEXT_ROUND").setDbFieldName("\"PILOTS_FOR_NEXT_ROUND\""),
    new DBModelField("REP_COLS").setDbFieldName("\"REP_COLS\""),
    new DBModelField("USE_REG_ID_FOR_LAP").setDbFieldName("\"USE_REG_ID_FOR_LAP\""),});

  public static void resetSelectedTab(Connection conn, long raceID) {
    try {
      dbControl.execSql(conn, "UPDATE " + dbControl.getTableAlias() + " SET IS_SELECTED=0 WHERE RACE_ID=?", raceID);
    } catch (Exception e) {
      MainForm._toLog(e);
    }
  }

  public void loadGroups(Connection conn, long race_id, long stage_id) {
    try {
      groups.clear();
      int GROUP_INDEX = 0;
      Map<Long, Integer> indexes = new HashMap<>();
      List<VS_STAGE_GROUPS> users = VS_STAGE_GROUPS.dbControl.getList(conn, "STAGE_ID=? ORDER BY GROUP_NUM, NUM_IN_GROUP", stage_id);

      Map<String, VS_REGISTRATION> regs_by_id = VS_REGISTRATION.dbControl.getMap(conn, "ID", "VS_RACE_ID=?", race_id);
      Map<String, VS_REGISTRATION> regs_by_name = VS_REGISTRATION.dbControl.getMap(conn, "VS_USER_NAME", "VS_RACE_ID=?", race_id);
      Map<String, VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getMap(conn, "VS_TRANS1", "VS_RACE_ID=?", race_id);
      Map<String, VS_REGISTRATION> regs2 = VS_REGISTRATION.dbControl.getMap(conn, "VS_TRANS2", "VS_RACE_ID=?", race_id);
      Map<String, VS_REGISTRATION> regs3 = VS_REGISTRATION.dbControl.getMap(conn, "VS_TRANS3", "VS_RACE_ID=?", race_id);

      for (VS_STAGE_GROUPS usr : users) {
        long db_group_index = usr.GROUP_NUM;
        VS_REGISTRATION reg_user = null;

        if (usr.REG_ID != 0) {
          reg_user = regs_by_id.get("" + usr.REG_ID);
        }

        if (reg_user == null && !usr.PILOT.equals("")) {
          // by name
          reg_user = regs_by_name.get("" + usr.PILOT);
        }

        if (reg_user == null) {
          if (usr.VS_PRIMARY_TRANS == 0) {
            reg_user = regs.get("" + usr.VS_PRIMARY_TRANS);
          }
          if (reg_user == null) {
            reg_user = regs2.get("" + usr.VS_PRIMARY_TRANS);
          }
          if (reg_user == null) {
            reg_user = regs3.get("" + usr.VS_PRIMARY_TRANS);
          }
        }

        if (reg_user == null) {
          reg_user = new VS_REGISTRATION();
          reg_user.PILOT_TYPE = 0;
          reg_user.NUM = VS_REGISTRATION.maxNum(conn, race_id) + 1;
          reg_user.VS_RACE_ID = RACE_ID;
          reg_user.VS_TRANS1 = usr.VS_PRIMARY_TRANS;
          reg_user.VS_TRANS2 = 0;
          reg_user.VS_TRANS3 = 0;
          reg_user.VS_SOUND_EFFECT = 1;
          reg_user.IS_ACTIVE = 0;
          reg_user.VS_USER_NAME = usr.PILOT;
          VS_REGISTRATION.dbControl.insert(conn, reg_user);
          regs.put("" + usr.VS_PRIMARY_TRANS, reg_user);
        }
        usr.PILOT = reg_user.VS_USER_NAME;
        usr.REG_ID = reg_user.ID;
        usr.PILOT_TYPE = reg_user.PILOT_TYPE;
        Integer phisical_group_index = indexes.get(db_group_index);
        if (phisical_group_index == null) {
          phisical_group_index = GROUP_INDEX;
          indexes.put(db_group_index, phisical_group_index);
          GROUP_INDEX++;
        }
        VS_STAGE_GROUP group = groups.get(phisical_group_index);
        if (group == null) {
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
    } catch (Exception e) {
      MainForm._toLog(e);
    }
  }

  public void checkConstarin() {
    TreeSet<String> userNames = new TreeSet<String>();
    for (Integer groupID : groups.keySet()) {
      VS_STAGE_GROUP group = groups.get(groupID);
      group.useChannels = "";
      for (VS_STAGE_GROUPS usr : group.users) {
        usr.isError = 0;
        if (group.useChannels.indexOf(usr.CHANNEL) >= 0) {
          usr.isError = 1;
        }
        if (userNames.contains(usr.PILOT)) {
          usr.isError = 2;
        }
        userNames.add(usr.PILOT);
        group.useChannels += usr.CHANNEL + ";";
      }
    }
  }

  public String toString() {
    return CAPTION;
  }

  public VS_RACE_LAP getLap(long GROUP_NUM, int TRANSPONDER, int lapNumber, long REG_ID) {
    VS_RACE_LAP lap = null;
    try {
      if (USE_REG_ID_FOR_LAP == 1) {
        lap = laps_check_reg_id.get("" + GROUP_NUM).get("" + REG_ID).get(lapNumber);
      } else {
        lap = laps_check_reg_id.get("" + GROUP_NUM).get("" + TRANSPONDER).get(lapNumber);
      }
    } catch (Exception e) {
    }
    return lap;
  }

  public void delLap(MainForm mainForm, long GROUP_NUM, int TRANSPONDER, long REG_ID, int lapNumber, VS_RACE_LAP delLap) {
    VS_RACE_LAP lap = null;
    try {
      if (USE_REG_ID_FOR_LAP == 1) {
        laps_check_reg_id.get("" + GROUP_NUM).get("" + REG_ID).remove("" + lapNumber);
      } else {
        laps_check_reg_id.get("" + GROUP_NUM).get("" + TRANSPONDER).remove("" + lapNumber);
      }
      VS_RACE_LAP.dbControl.delete(mainForm.con, delLap);
    } catch (Exception e) {
    }
  }

  public VS_RACE_LAP getLastLap(MainForm mainForm, long GROUP_NUM, int TRANSPONDER, long REG_ID, long START_TIME, VS_STAGE_GROUPS usr) {
    boolean find_error = false;
    try {
      Map<String, VS_RACE_LAP> user_laps = null;
      if (USE_REG_ID_FOR_LAP == 1) {
        user_laps = laps_check_reg_id.get("" + GROUP_NUM).get("" + REG_ID);
      } else {
        user_laps = laps_check_reg_id.get("" + GROUP_NUM).get("" + TRANSPONDER);
      }
      HashSet<Integer> sorted = new HashSet();
      try {
        if (user_laps != null) {
          for (String lapnum : user_laps.keySet()) {
            sorted.add(Integer.parseInt(lapnum));
          }
        }
      } catch (Exception ein) {
        find_error = true;
      }
      int lap_index = 1;
      if (!find_error) {
        for (int lap_number : sorted) {
          if (lap_number != lap_index) {
            find_error = true;
            break;
          }
          if (user_laps.get("" + lap_number).TIME_START != START_TIME) {
            find_error = true;
            break;
          }
          lap_index++;
        }
      }
      if (find_error) {
        // delete from DataBase
        try {
          if (user_laps != null) {
            user_laps.clear();
            VS_RACE_LAP.dbControl.delete(mainForm.con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? and TRANSPONDER_ID=?", RACE_ID, ID, GROUP_NUM, TRANSPONDER);
          }
        } catch (Exception ein) {
          mainForm._toLog(ein);
        }
      } else {
        if (user_laps != null) {
          return user_laps.get("" + (lap_index - 1));
        }
      }
    } catch (Exception e) {
      // mainForm._toLog(e);
    } finally {
    }
    usr.IS_FINISHED = 0;
    usr.IS_RECALULATED = 0;
    usr.BEST_LAP = 0;
    usr.RACE_TIME = 0;
    return null;
  }

  public VS_RACE_LAP addLapFromKeyPress(MainForm mainForm, VS_STAGE_GROUPS usr, long time) {
    VS_RACE_LAP lap_return = null;
    try {
      usr.IS_FINISHED = 0;
      usr.IS_RECALULATED = 0;
      VS_RACE_LAP lap = new VS_RACE_LAP();
      lap.BASE_ID = 0;
      lap.REG_ID = usr.REG_ID;
      lap.GROUP_NUM = usr.GROUP_NUM;
      lap.TRANSPONDER_ID = usr.VS_PRIMARY_TRANS;
      lap.RACE_ID = mainForm.activeGroup.stage.RACE_ID;
      lap.STAGE_ID = mainForm.activeGroup.stage.ID;
      lap.TIME_START = mainForm.raceTime;
      lap.TIME_FROM_START = time;
      VS_RACE_LAP last_lap = getLastLap(mainForm, lap.GROUP_NUM, lap.TRANSPONDER_ID, usr.REG_ID, mainForm.raceTime, usr);
      lap.LAP = last_lap == null ? 1 : (last_lap.LAP + 1);
      
      VS_RACE race = mainForm.activeRace;
      
      if (race != null && race.POST_START==1 && usr.START_TIME!=0 && lap.LAP==1){
        lap.TRANSPONDER_TIME = time - usr.START_TIME;        
      }else{
        lap.TRANSPONDER_TIME = last_lap == null ? (time - mainForm.raceTime) : (time - last_lap.TIME_FROM_START);
      }

      if (lap.LAP == 1) {
        usr.RACE_TIME = 0;
        usr.BEST_LAP = 0;
        usr.SCORE = 0;
      }

      long lap_time = lap.TRANSPONDER_TIME;
      
      if (race != null && race.PLEASE_IGNORE_FIRST_LAP == 1 && lap.LAP == 1 && race.POST_START==1 && usr.START_TIME<mainForm.raceTime) {
        usr.START_TIME = time;
      }

      if (race != null && race.PLEASE_IGNORE_FIRST_LAP == 1 && usr.FIRST_LAP != 0 && lap.LAP == 1) {
        lap_time = time - usr.FIRST_LAP;        
      }

      VS_RACE_LAP lap_for_sound = null;
      if (race != null && race.PLEASE_IGNORE_FIRST_LAP == 1 && usr.FIRST_LAP == 0) {
        usr.FIRST_LAP = time;
        lap_for_sound = lap;
        lap_for_sound.LAP = 0;
      } else {
        if (Calendar.getInstance().getTimeInMillis() - mainForm.raceTime < 2000) {
          // ignore lapinfo
        } else {
          if (lap_time >= MIN_LAP_TIME * 1000) {
            try {
              mainForm.race_log.writeFile("\"" + usr.parent.stage.CAPTION + "\";" + "Group" + usr.parent.GROUP_NUM + ";" + usr.VS_PRIMARY_TRANS + ";\"" + usr.PILOT + "\";" + time + ";" + new JDEDate(time).getTimeString(":") + ";" + lap.LAP + ";" + StageTab.getTimeIntervel(lap.TRANSPONDER_TIME) + ";");
            } catch (Exception ein) {
            }
            VS_RACE_LAP.dbControl.insert(mainForm.con, lap);
            if (USE_REG_ID_FOR_LAP == 1) {
              VS_RACE_LAP.dbControl.putObjToMap(laps_check_reg_id, "" + lap.GROUP_NUM, "" + usr.REG_ID, "" + lap.LAP, lap);
            } else {
              VS_RACE_LAP.dbControl.putObjToMap(laps_check_reg_id, "" + lap.GROUP_NUM, "" + lap.TRANSPONDER_ID, "" + lap.LAP, lap);
            }
            lap_for_sound = lap;
            lap_return = lap;
          }
        }
      }
      usr.IS_RECALULATED = 0;

      String krug = "";
      if (lap_for_sound != null) {
        String sec = "";
        long s = lap.TRANSPONDER_TIME/1000;
        if (mainForm.SAY_SECONDS_FOR_LAP) sec= " "+s;
        mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().lapTime(usr.PILOT+sec, lap_for_sound.LAP, usr.parent.stage.LAPS));
        if (lap_for_sound.LAP==usr.parent.stage.LAPS){
          if (tab!=null && !tab.FIRST_RACER_IS_FINISHED ) {
            tab.FIRST_RACER_IS_FINISHED = true;
            tab.runFinishCommand();
          }
        }
      }

    } catch (Exception e) {
      mainForm._toLog(e);
    }
    return lap_return;
  }
  
  String cashe_TRANSS = null;
  Map<String, List<String>> cashe_TRANSS_ARRAY = null;
  
  public Map<String, List<String>> getTanspondersForChannels(){
    if (cashe_TRANSS!=null && cashe_TRANSS.equals(TRANSS)){
      return cashe_TRANSS_ARRAY;
    }    
    Map<String, List<String>> result = new HashMap();
    if (!TRANSS.equalsIgnoreCase("")){
      String[] masTrans = TRANSS.split(":");
      String[] channels = CHANNELS.split(";");
      if (masTrans!=null && channels!=null){        
        for (int channel=0; channel<masTrans.length; channel++){
          List<String> trans_list = new ArrayList();
          if (channel>=channels.length) break;
          String channel_st = channels[channel];
          result.put( channel_st, trans_list);
          String trans[] = masTrans[channel].split(";");
          if (trans!=null){
            for (int i=0; i<trans.length; i++){
              if (!trans[i].trim().equals("")){
                trans_list.add(trans[i].trim());
              }
            }
          }        
        }
      }
    }
    cashe_TRANSS = TRANSS;
    cashe_TRANSS_ARRAY = result;
    return result;
  }
}
