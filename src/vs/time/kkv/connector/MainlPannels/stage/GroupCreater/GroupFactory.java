/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.GroupCreater;

import RaceCreators.CopyRace;
import RaceCreators.CreateDuelRace;
import RaceCreators.CreateFastWithFastSlowWithSlowRace;
import RaceCreators.CreateRandomRace;
import java.util.ArrayList;
import java.util.List;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class GroupFactory {

  public static final String DEFAULT_RACE_TYPE = "Default";

  static List<IGroupCreater> creaters = new ArrayList();

  static {
    // Please add you GrpupCreater
    creaters.add(new CopyRace());
    creaters.add(new CreateRandomRace());
    creaters.add(new CreateDuelRace());
    creaters.add(new CreateFastWithFastSlowWithSlowRace());
  }

  // Old, System Race Types
  public final static String[] RACE_TYPES = new String[]{"Single elemination", "Double elemenation", "Whoop Race", "Olimpic Single elemenation", "Everyone with each(only 16)" /*, "Olimpic Losers"*/};

  public static boolean isSupportRaceType(int stageType, int raceType) {
    for (IGroupCreater creater : creaters) {
      if (creater.getRaceType() == raceType && creater.isSupportedStageType(stageType)) {
        return true;
      }
    }
    return false;
  }

  public static String[] getAllRacesTypes(int stageType) {
    List<String> types = new ArrayList();
    // old race types
    if (stageType == MainForm.STAGE_RACE) {
      for (String r_type : RACE_TYPES) {
        types.add(r_type);
      }
    } else {
      types.add(DEFAULT_RACE_TYPE);
    }
    // new race types. Custom Race Types
    for (IGroupCreater creater : creaters) {
      try {
        if (creater.isSupportedStageType(stageType)){
          types.add(creater.getRaceTypeName());
        }  
      } catch (Exception e) {
      }
    }
    String[] res = new String[types.size()];
    int index = 0;
    for (String t : types) {
      res[index] = t;
      index++;
    }
    return res;
  }

  public static int getRaceCodeByName(int stageType, String raceTypeName) {
    int index = 0;

    if (stageType == MainForm.STAGE_RACE) {
      for (String r_type : RACE_TYPES) {
        if (raceTypeName.equalsIgnoreCase(r_type)) {
          return index;
        }
        index++;
      }
    } else {
      if (raceTypeName.equalsIgnoreCase(DEFAULT_RACE_TYPE)) {
        return index;
      }
      index++;
    }
    // new race types
    for (IGroupCreater creater : creaters) {
      if (raceTypeName.equalsIgnoreCase(creater.getRaceTypeName()) && creater.isSupportedStageType(stageType)) {
        return creater.getRaceType();
      }
    }
    return 0;
  }

  public static String getRaceNameByCode(int stageType, int raceTypeCode) {
    int index = 0;
    if (stageType == MainForm.STAGE_RACE) {
      for (String r_type : RACE_TYPES) {
        if (index == raceTypeCode) {
          return r_type;
        }
        index++;
      }
    } else {
      if (raceTypeCode == 0) {
        return DEFAULT_RACE_TYPE;
      }
    }
    // new race types
    for (IGroupCreater creater : creaters) {
      if (creater.getRaceType() == raceTypeCode && creater.isSupportedStageType(stageType)) {
        return creater.getRaceTypeName();
      }
    }
    return "not defined";
  }

  public static IGroupCreater getRaceCreatorByCode(int stageType, int raceTypeCode) {
    for (IGroupCreater creater : creaters) {
      if (creater.getRaceType() == raceTypeCode && creater.isSupportedStageType(stageType) ) {
        return creater;
      }
    }
    return null;
  }

}
