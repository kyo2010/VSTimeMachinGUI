/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RaceCreators;

import KKV.Utils.UserException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.GroupCreater.IGroupCreater;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class CopyRace extends IGroupCreater {
  
  public boolean isSupportedStageType(int stageType){
     //if (stageType==MainForm.STAGE_RACE) return true;
     //return false;
     if ( stageType==MainForm.STAGE_RACE || 
          stageType==MainForm.STAGE_PRACTICA || 
          stageType==MainForm.STAGE_QUALIFICATION ) 
     {
       return true;
     }
     return false;
   }

  @Override
  public int getRaceType() {
    // KKV ID start from 1000, You can use 2000, 3000, 5000...
    return 1000;
  }

  @Override
  public String getRaceTypeName() {
    return "Copy Race";
  }

  @Override
  public void createGroup(VS_STAGE stage, VS_STAGE parent_stage, Connection con) throws UserException{
    try {
            
      
      List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
      /*
      Map<String, VS_STAGE_GROUPS> qualification = null;
      if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
        List<VS_STAGE> stages = VS_STAGE.dbControl.getList(con, "RACE_ID=? and STAGE_TYPE=? order by ID desc", stage.RACE_ID, MainForm.STAGE_QUALIFICATION_RESULT);
        if (stages != null && stages.size() > 0) {
          qualification = VS_STAGE_GROUPS.dbControl.getMap(con, "PILOT", "STAGE_ID=?", stages.get(0).ID);
        }
      } */
      for (VS_STAGE_GROUPS user : groups) {
        VS_STAGE_GROUPS usr = new VS_STAGE_GROUPS(user); 
        usr.STAGE_ID = stage.ID;
        usr.GROUP_NUM = user.GROUP_NUM;
        usr.NUM_IN_GROUP = user.NUM_IN_GROUP;
        VS_STAGE_GROUPS.dbControl.insert(con, usr);
      }
    } catch (UserException ue){
      throw ue;
    } catch (Exception e) {
      throw new UserException("Error", "Creating group is error.\n" + e.toString());
    }
  }

}
