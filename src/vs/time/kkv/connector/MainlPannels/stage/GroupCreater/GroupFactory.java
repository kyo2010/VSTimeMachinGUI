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

/**
 *
 * @author kyo
 */
public class GroupFactory {
  static List<IGroupCreater> creaters = new ArrayList();
  static{
    // Please add you GrpupCreater
    creaters.add(new CopyRace());
    creaters.add(new CreateRandomRace());   
    creaters.add(new CreateDuelRace());   
    creaters.add(new CreateFastWithFastSlowWithSlowRace());       
  } 
  
  // Old, System Race Types
  public final static String[] RACE_TYPES = new String[]{"Single elemination", "Double elemenation", "Whoop Race", "Olimpic Single elemenation", "Everyone with each(only 16)" /*, "Olimpic Losers"*/};
  
  public static boolean isSupportRaceType(int raceType){
    for (IGroupCreater creater : creaters){
       if (creater.getRaceType()==raceType) return true;      
    }  
    return false;
  }

  public static String[] getAllRacesTypes(){
    List<String> types = new ArrayList();
    // old race types
    for (String r_type : RACE_TYPES){
      types.add(r_type);
    }    
    // new race types. Custom Race Types
    for (IGroupCreater creater : creaters){
      try{
        types.add(creater.getRaceTypeName());
      }catch(Exception e){}
    }    
    String[] res = new String[types.size()];
    int index = 0;
    for (String t : types){
      res[index] = t;
      index++;
    }
    return res;
  }
  
  public static int getRaceCodeByName(String raceTypeName){
    int index = 0;
    for (String r_type : RACE_TYPES){
      if (raceTypeName.equalsIgnoreCase(r_type)) return index;
      index++;
    }    
    // new race types
    for (IGroupCreater creater : creaters){
      if (raceTypeName.equalsIgnoreCase(creater.getRaceTypeName())) return creater.getRaceType();
    }    
    return 0;  
  }
  
  public static String getRaceNameByCode(int raceTypeCode){
    int index = 0;
    for (String r_type : RACE_TYPES){
      if (index==raceTypeCode) return r_type;
      index++;
    }    
    // new race types
    for (IGroupCreater creater : creaters){
      if (creater.getRaceType()==raceTypeCode) return creater.getRaceTypeName();
    }    
    return "not defined";  
  }
  
   public static IGroupCreater getRaceCreatorByCode(int raceTypeCode){
    for (IGroupCreater creater : creaters){
      if (creater.getRaceType()==raceTypeCode) return creater;
    }    
    return null;  
  }
  
  
}
