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
public class CreateRandomRace extends IGroupCreater {

  @Override
  public int getRaceType() {
    // KKV ID start from 1000, You can use 2000, 3000, 5000...
    return 1010;
  }

  public boolean isSupportedStageType(int stageType) {
    if (stageType == MainForm.STAGE_RACE
            || stageType == MainForm.STAGE_PRACTICA
            || stageType == MainForm.STAGE_QUALIFICATION) {
      return true;
    }
    return false;
  }

  @Override
  public String getRaceTypeName() {
    return "Random Race";
  }

  public static int[] generateRandomArray(int countOfUsers) {
    int[] usersPos = new int[countOfUsers];
    for (int i = 0; i < countOfUsers; i++) {
      usersPos[i] = -1;
    }
    Math.random();
    for (int i = 0; i < countOfUsers; i++) {
      boolean pleaseRegenerate = false;
      int pos = 0;
      pos += (int) (Math.random() * (countOfUsers));
      do {
        pleaseRegenerate = false;
        if (pos >= countOfUsers) {
          pos -= countOfUsers;
        }
        for (int j = 0; j < i; j++) {
          if (usersPos[j] == pos) {
            pleaseRegenerate = true;
            if (i > countOfUsers - 3) {
              pos++;
            } else {
              pos += (int) (Math.random() * (countOfUsers));
            }
            break;
          }
        }
      } while (pleaseRegenerate);
      usersPos[i] = pos;
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
      if (stage.PILOT_TYPE != MainForm.PILOT_TYPE_NONE_INDEX) {
        PILOT_TYPE_WHERE_FOR_USERS = " AND PILOT_TYPE=" + stage.PILOT_TYPE + " ";
      }

      if (parent_stage == null) {
        users = VS_REGISTRATION.dbControl.getList(con, "VS_RACE_ID=? and IS_ACTIVE=1 " + PILOT_TYPE_WHERE_FOR_USERS + " ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
        countOfUsers = users.size();
      } else {
        groups = VS_STAGE_GROUPS.dbControl.getList(con, "STAGE_ID=? AND ACTIVE_FOR_NEXT_STAGE=1 AND GROUP_TYPE=0 order by RACE_TIME, BEST_LAP, NUM_IN_GROUP", parent_stage.ID);
        countOfUsers = groups.size();
      }
      int[] usersPos = generateRandomArray(countOfUsers);
      long GROUP_NUM = 1;
      int NUM_IN_GROUP = 1;
      List<VS_STAGE_GROUPS> result = new ArrayList();
      for (int i = 0; i < countOfUsers; i++) {
        VS_STAGE_GROUPS usr = null;
        if (groups != null) {
          usr = new VS_STAGE_GROUPS(groups.get(usersPos[i]));
        } else {
          if (users != null) {
            usr = new VS_STAGE_GROUPS(users.get(usersPos[i]));
          }
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

  public static void main(String[] args) {
    int[] y = generateRandomArray(10);
    for (int i = 0; i < y.length; i++) {
      System.out.println(i + " = " + y[i]);
    }
  }
}
