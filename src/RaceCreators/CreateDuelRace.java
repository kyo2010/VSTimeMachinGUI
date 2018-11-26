/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RaceCreators;

import KKV.Utils.UserException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.GroupCreater.IGroupCreater;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class CreateDuelRace extends IGroupCreater {

   public boolean isAllowDuplicatedPilotInStage(){ return true; };
  
  @Override
  public int getRaceType() {
    // KKV ID start from 1000, You can use 2000, 3000, 5000...
    return 1020;
  }
  
  public boolean isSupportedStageType(int stageType){
     if ( stageType==MainForm.STAGE_RACE || 
          stageType==MainForm.STAGE_PRACTICA || 
          stageType==MainForm.STAGE_QUALIFICATION ) 
     {
       return true;
     }
     return false;
   };

  @Override
  public String getRaceTypeName() {
    return "Duel Race (each with each other)";
  }

  public static int[][] generateFullDuelArray(int countOfUsers) {
    List<int[]> all_users = new ArrayList();
    for (int i = 0; i < countOfUsers; i++) {
      for (int j = i+1; j < countOfUsers; j++) {
        if (i == j) {
          continue;
        }
        all_users.add(new int[]{i, j});
      }
    }
    int[][] usersPos = new int[all_users.size()][];
    int indexUp = 0;
    int indexDown = all_users.size()-1;
    boolean up = true;
    for (int index = 0; index<all_users.size(); index++) {
      if (up) {
        usersPos[index] = all_users.get(indexUp);
        indexUp++;
      } else {
        usersPos[index] = all_users.get(indexDown);
        indexDown--;
      }
      up = !up;
      
    }
    return usersPos;
  }

  @Override
  public void createGroup(VS_STAGE stage, VS_STAGE parent_stage, Connection con) throws UserException {
    try {

      int countOfUsers = 0;
      List<VS_REGISTRATION> users = null;
      List<VS_STAGE_GROUPS> groups = null;
      
      String PILOT_TYPE_WHERE_FOR_USERS = "";
      if (stage.PILOT_TYPE!=MainForm.PILOT_TYPE_NONE_INDEX){
        PILOT_TYPE_WHERE_FOR_USERS =" AND PILOT_TYPE="+stage.PILOT_TYPE+" ";
      }

      if (parent_stage == null) {
        users = VS_REGISTRATION.dbControl.getList(con, "VS_RACE_ID=? and IS_ACTIVE=1 "+PILOT_TYPE_WHERE_FOR_USERS+" ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
        countOfUsers = users.size();
      } else {
        groups = VS_STAGE_GROUPS.dbControl.getList(con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
        countOfUsers = groups.size();
      }

      int[][] usersPos = generateFullDuelArray(countOfUsers);
      List<VS_STAGE_GROUPS> result = new ArrayList();
      for (int i = 0; i < usersPos.length; i++) {
        for (int j = 0; j < usersPos[i].length; j++) {
          VS_STAGE_GROUPS usr = null;
          if (groups != null) {
            usr = new VS_STAGE_GROUPS(groups.get(usersPos[i][j]));            
          } else {
            if (users != null) {
              usr = new VS_STAGE_GROUPS(users.get(usersPos[i][j]));              
            }
          }
          usr.STAGE_ID = stage.ID;
          usr.GROUP_NUM = i+1;
          usr.NUM_IN_GROUP = j+1;
          result.add(usr);
        }
      }
      // set Channels and keep to save previous channels
      recalulateChannels(result, stage.CHANNELS);
      // save to database
      for (VS_STAGE_GROUPS usr : result) {
        VS_STAGE_GROUPS.dbControl.insert(con, usr);
        System.out.println(usr.GROUP_NUM+"."+usr.NUM_IN_GROUP+" "+usr.PILOT+" "+usr.CHANNEL);
      }

    } catch (UserException ue) {
      throw ue;
    } catch (Exception e) {
      throw new UserException("Error", "Creating group is error.\n" + e.toString());
    }
  }

  public static void main(String[] args) {
    int[][] y = generateFullDuelArray(4);
    for (int i = 0; i < y.length; i++) {
      System.out.println(i + " = " + y[i][0] + " - " + y[i][1]);
    }
  }
}
