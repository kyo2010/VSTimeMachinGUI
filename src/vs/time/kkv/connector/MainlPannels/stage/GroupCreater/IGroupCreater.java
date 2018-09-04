/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.GroupCreater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 * Create Group Class
 * @author kyo
 */
public abstract class IGroupCreater { 
  /** Check what kind stage and race type supported this alg */
  public abstract boolean isSupport(int stageType, int racyType); ;
  /** The genaral function for group creation */
  public abstract void createGroup(StageTab tab);
  
  // add user to group, if group is full, create new group
  public void addUserToGroup(VS_STAGE_GROUPS add_usr, List<VS_STAGE_GROUPS> users, int max_pilots_in_groups) {
    Map<Long, Integer> count_pilots_in_group = new HashMap();
    long max_group_index = 0;
    for (VS_STAGE_GROUPS usr : users) {
      Integer count_pilots = count_pilots_in_group.get(usr.GROUP_NUM);
      if (count_pilots == null) {
        count_pilots = 0;
      }
      count_pilots++;
      count_pilots_in_group.put(usr.GROUP_NUM, count_pilots);
      if (max_group_index < usr.GROUP_NUM) {
        max_group_index = usr.GROUP_NUM;
      }
    }
    // find empty group  
    boolean is_find = false;
    for (Long num : count_pilots_in_group.keySet()) {
      Integer count_pilots = count_pilots_in_group.get(num);
      if (count_pilots == null) {
        count_pilots = 0;
      }
      if (count_pilots < max_pilots_in_groups) {
        is_find = true;
        add_usr.GROUP_NUM = num;
        add_usr.NUM_IN_GROUP = count_pilots + 1;
        users.add(add_usr);
      }
    }
    // create new groups
    if (!is_find) {
      add_usr.GROUP_NUM = max_group_index + 1;
      add_usr.NUM_IN_GROUP = 1;
      users.add(add_usr);
    }
  }
  
  // Recalculate Channels, The algoritm try to keep previous pilot channel
  public void recalulateChannels(List<VS_STAGE_GROUPS> users, String STAGE_CHANNELS) {
    String[] channels = STAGE_CHANNELS.split(";");
    HashMap<Long, HashMap<String, Integer>> usingChannels = new HashMap();

    for (VS_STAGE_GROUPS usr : users) {
      HashMap<String, Integer> groupChannels = usingChannels.get(usr.GROUP_NUM);
      if (groupChannels == null) {
        groupChannels = new HashMap<String, Integer>();
        usingChannels.put(usr.GROUP_NUM, groupChannels);
      }
      Integer countUse = groupChannels.get(usr.CHANNEL);
      if (countUse == null) {
        countUse = 0;
      }
      countUse++;
      groupChannels.put(usr.CHANNEL, countUse);
    }
    HashMap<Long, HashMap<String, Integer>> checkChannelsAll = new HashMap<Long, HashMap<String, Integer>>();
    for (VS_STAGE_GROUPS usr : users) {
      HashMap<String, Integer> checkChannels = checkChannelsAll.get(usr.GROUP_NUM);
      if (checkChannels == null) {
        checkChannels = new HashMap<String, Integer>();
        checkChannelsAll.put(usr.GROUP_NUM, checkChannels);
      }
      HashMap<String, Integer> groupChannels = usingChannels.get(usr.GROUP_NUM);
      Integer countUse = groupChannels.get(usr.CHANNEL);
      if (countUse > 1) {
        if (checkChannels.get(usr.CHANNEL) == null) {
          checkChannels.put(usr.CHANNEL, 1);
        } else {
          for (String channel : channels) {
            if (groupChannels.get(channel) == null) {
              usr.CHANNEL = channel;
              groupChannels.put(usr.CHANNEL, 1);
              checkChannels.put(usr.CHANNEL, 1);
              break;
            }
          }
        }
      }
    }
  }
}
