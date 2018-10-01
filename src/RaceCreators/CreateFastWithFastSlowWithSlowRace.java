/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RaceCreators;

import KKV.Utils.UserException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.GroupCreater.IGroupCreater;
import vs.time.kkv.connector.MainlPannels.stage.Sorting.GROUP_SCORE_COMPARATOR;
import vs.time.kkv.connector.MainlPannels.stage.Sorting.ISort;
import vs.time.kkv.connector.MainlPannels.stage.Sorting.SortFactory;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class CreateFastWithFastSlowWithSlowRace extends IGroupCreater {

  @Override
  public int getRaceType() {
    // KKV ID start from 1000, You can use 2000, 3000, 5000...
    return 1050;
  }

  @Override
  public String getRaceTypeName() {
    return "Faster pilot with the faster pilot";
  }
  
  
  
  @Override
  public void createGroup(VS_STAGE stage, VS_STAGE parent_stage, Connection con) throws UserException {
    try {
      List<VS_REGISTRATION> users = null;
      List<VS_STAGE_GROUPS> groups = null;   
      int countOfUsers = 0;
      if (parent_stage==null) {
        users = VS_REGISTRATION.dbControl.getList(con, "VS_RACE_ID=? and IS_ACTIVE=1 ORDER BY PILOT_TYPE,NUM", stage.RACE_ID); 
        countOfUsers = users.size();
      }else{      
        if (parent_stage.STAGE_TYPE==MainForm.STAGE_RACE_RESULT || parent_stage.STAGE_TYPE==MainForm.STAGE_QUALIFICATION_RESULT){
          groups = VS_STAGE_GROUPS.dbControl.getList(con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by GROUP_NUM, NUM_IN_GROUP", parent_stage.ID);
        }else{
          groups = VS_STAGE_GROUPS.dbControl.getList(con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
          //Collections.sort(groups, VS_STAGE_GROUP.GROUP_SCORE_COMPARATOR);
          //Collections.sort(groups, SortFactory.getSortComparatorByID("Scores"));
          ISort sc = SortFactory.getSortComparatorByID(stage.SORT_TYPE);
          if (sc!=null){
            Collections.sort(groups, sc);
          }        
          
        }
        countOfUsers = groups.size();
      }            
      long GROUP_NUM = 1;
      int NUM_IN_GROUP = 1;
      List<VS_STAGE_GROUPS> result = new ArrayList();
      for (int i=0; i<countOfUsers; i++){
        VS_STAGE_GROUPS usr = null;
        int wins = 0;
        if (users!=null) {
          usr = new VS_STAGE_GROUPS(users.get(i));         
        }
        if (groups!=null) {
          VS_STAGE_GROUPS prev_group = groups.get(i);
          usr = new VS_STAGE_GROUPS(prev_group);
          wins = prev_group.wins+prev_group.WIN;
        }
        usr.STAGE_ID = stage.ID;
        usr.GROUP_NUM = GROUP_NUM;
        usr.NUM_IN_GROUP = NUM_IN_GROUP;
        result.add(usr);
        NUM_IN_GROUP++;
        if (NUM_IN_GROUP > stage.COUNT_PILOTS_IN_GROUP) {
          NUM_IN_GROUP = 1;
          GROUP_NUM++;
        }
      }
      // set Channels and keep to save previous channels
      recalulateChannels(result, stage.CHANNELS);
      // save to database
      for (VS_STAGE_GROUPS usr : result) {
        VS_STAGE_GROUPS.dbControl.insert(con, usr);
      }
    } catch (UserException ue) {
      throw ue;
    } catch (Exception e) {
      throw new UserException("Error", "Creating group is error.\n" + e.toString());
    }
  }
}
