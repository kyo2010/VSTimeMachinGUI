/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTableData;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import static vs.time.kkv.models.VS_STAGE_GROUPS.MAX_TIME;

/**
 *
 * @author kyo
 */
public class WebControl {
  public MainForm mainForm = MainForm._mainForm;  
  public static int MAX_DISPLAYS_COLUMNS = 17;
  public int countRefresh = 0;
  public StageTab tab = null;
  
  /** Return active group */
  public VS_STAGE_GROUP getActiveGroup(){
    if (mainForm.activeGroup != null) {
      return mainForm.activeGroup;
    } else if (mainForm.invateGroup != null) {
      return mainForm.invateGroup;
    } else if (mainForm.lastRaceGroup != null) {
      return mainForm.lastRaceGroup;
    } else if (mainForm.lastInvateGroup != null) {
      return mainForm.lastInvateGroup;
    }  
    return null;
  }
  
  /** Return active group */
  public List<VS_STAGE_GROUP> getGroups(){    
    
    for (int i = mainForm.stageTabs.size() - 1; i >= 0; i--) {
      if (!mainForm.stageTabs.get(i).isOneTable) {
        tab = mainForm.stageTabs.get(i);
        List<VS_STAGE_GROUP> groups = new ArrayList();
        for (Integer groupId : tab.stage.groups.keySet()) {
          VS_STAGE_GROUP group = tab.stage.groups.get(groupId);
          groups.add(group);
        }
        return groups;
      }
    }
    return null;
  }
  
  /** Return active group */
  public List<VS_STAGE_GROUP> getLastStageGroups(){
    StageTab tab = null;
    for (int i = mainForm.stageTabs.size() - 1; i >= 0; i--) {
        tab = mainForm.stageTabs.get(i);
        if (tab.stage.SHOW_FOR_TV==0) continue;
        List<VS_STAGE_GROUP> groups = new ArrayList();        
        for (Integer groupId : tab.stage.groups.keySet()) {
          VS_STAGE_GROUP group = tab.stage.groups.get(groupId);
          groups.add(group);
          for (VS_STAGE_GROUPS usr : group.users){
            try{
              if (usr.registration==null) usr.loadRegistration(mainForm.con,tab.stage.RACE_ID);
            }catch(Exception e){}
          }
        }
        return groups;      
    }
    return null;
  }
  
  /** Return active group */
  public List<VS_STAGE_GROUP> getLastStageGroups(int timer,
          double displayHeight, double pilotHeight,
          double groupHeight, double groupSpacerHeight
          ){
    List<VS_STAGE_GROUP> groups = getLastStageGroups();
    int page = timer/5;
    List<VirtualDisplay> displays = VirtualDisplay.getVirualDisplays(groups, displayHeight, pilotHeight, groupHeight, groupSpacerHeight);
    if (displays.size()>0){
      int displayIndex = page%displays.size();
      return displays.get(displayIndex).groups;
    }
    return groups;
  }
  
  /** Return active group */
  public VS_STAGE getLastStage(){
    StageTab tab = null;
    for (int i = mainForm.stageTabs.size() - 1; i >= 0; i--) {
        tab = mainForm.stageTabs.get(i);
        if (tab.stage.SHOW_FOR_TV==0) continue;        
        return tab.stage;      
    }
    return null;
  }
  
  /*** Return User by channel Pos */
  public VS_STAGE_GROUPS getPilot(VS_STAGE_GROUP group, int index){
    String channels_st = "";
    if (group != null) {
      channels_st = group.stage.CHANNELS;
    }
    String[] channels = channels_st.split(";");
    if (channels!=null && channels.length>index){
      for (VS_STAGE_GROUPS usr : group.users){
        if (usr.CHANNEL.equalsIgnoreCase(channels[index])) return usr;
      }
    };
    return null;
  }
  
  /*** Return User by channel Pos */
  public List<VS_STAGE_GROUPS> getPilotsOrderByLaps(VS_STAGE stage){
    List<VS_STAGE_GROUPS> pilots = new ArrayList<VS_STAGE_GROUPS>();
    if (stage!=null){
      for (Integer groupId : stage.groups.keySet()) {
        VS_STAGE_GROUP group = stage.groups.get(groupId);          
        for (VS_STAGE_GROUPS usr : group.users){
          try{
            if (usr.registration==null) usr.loadRegistration(mainForm.con,stage.RACE_ID);
          }catch(Exception e){}
          pilots.add(usr);
        }
      }
      Collections.sort(pilots, new Comparator<VS_STAGE_GROUPS>(){
        @Override
        public int compare(VS_STAGE_GROUPS o1, VS_STAGE_GROUPS o2) {
          if (o1.LAPS>o2.LAPS) return -1; 
          if (o1.LAPS<o2.LAPS) return 1;
          return 0;
        }      
      });
    }    
    return pilots;
  }
  
  /** get Caption for Locale*/
  public String L(String caption){
    return mainForm.getLocaleString(caption);    
  }
  
  public String getTime(long time){
    if (time==0) return "";
    if (time==MAX_TIME) return "";
    return StageTab.getTimeIntervel(time,false);
  }
  
  public String getRaceName(){
    if (mainForm.activeRace!=null){
      return mainForm.activeRace.RACE_NAME;
    }
    return "";
  }
  
  
  /** Return active group */
  public /*List<StageTableData>*/VirtualDisplay getLastStageVirtualDisplay(int timer,
          double displayHeight, double pilotHeight,
          double groupHeight, double groupSpacerHeight,
          int secundPerPage
          ){
    
    List<StageTableData> list = null;
    
    StageTab tab = null;
    for (int i = mainForm.stageTabs.size() - 1; i >= 0; i--) {
        StageTab tab1 = mainForm.stageTabs.get(i);
        if (tab1.stage.SHOW_FOR_TV==0) continue;
        tab = tab1;
        break;         
    }
    if (tab!=null){    
      int page = timer/secundPerPage;
      List<VirtualDisplay> displays = VirtualDisplay.getVirualDisplaysForTableData(tab.stageTableAdapter.rows, displayHeight, pilotHeight, groupHeight, groupSpacerHeight,tab.isOneTable);
      if (displays.size()>0){
        int displayIndex = page%displays.size();
        VirtualDisplay display = displays.get(displayIndex);
        display.DISPLAYS_COUNT = displays.size();
        display.tab = tab;
        display.COLUMNS_COUNT = display.tab.stageTableAdapter.getColumnCount();
        display.WIDTH = 0;
        display.WIDTHS.clear();                
        display.columns = display.tab.stageTableAdapter.getColumns();
        
        for (int i=0; i<display.COLUMNS_COUNT; i++){
          int width = tab.stageTableAdapter.getMinWidth(i);
          display.WIDTHS.add(width);         
          if (display.columns.size()>i && !display.columns.get(i).showOnWeb) continue;
          if (i<MAX_DISPLAYS_COLUMNS) {
            display.WIDTH += width;  
          }  
          //System.out.println("Width "+i+":"+display.WIDTHS.get(i));
        }
        //System.out.println("Width:"+display.WIDTH);
        
        for (StageTableData row: display.rows){
          if (row.pilot!=null){
            row.pilot.loadRegistration(mainForm.con, tab.stage.RACE_ID);
            if (row.pilot.color==null){
              //String m = row.pilot.color.w3css;
              row.pilot.color = VSColor.getColorForChannel(row.pilot.CHANNEL, tab.stage.CHANNELS, tab.stage.COLORS);
            }            
          }
        }
        
        return display;
      }    
    }    
    return null;
  }   
  
  public String getW3Color(VirtualDisplay display, VS_STAGE_GROUPS pilot){
    try{
      return pilot.color.w3css;
    }catch(Exception e){}
    return "";
  }
  
  public double getColumnWidth(VirtualDisplay display, int col){
    try{
      double w = Math.round(((double)(display.WIDTHS.get(col))/display.WIDTH)*10000);
      return w/100;
    }catch(Exception e){}
    return 0;
  }
  
  public boolean isPilotCol(VirtualDisplay display, int col){
    try{
      return (display.columns.get(col).ID==STAGE_COLUMN.CID_PILOT);
    }catch(Exception e){}
    return false;
  }
  
  public boolean isChannelCol(VirtualDisplay display, int col){
    try{
      return (display.columns.get(col).ID==STAGE_COLUMN.CID_CHANNEL);
    }catch(Exception e){}
    return false;
  }
  
  public int getColumnCount(VirtualDisplay display){
    
    if (display!=null) return display.COLUMNS_COUNT>=MAX_DISPLAYS_COLUMNS?MAX_DISPLAYS_COLUMNS:display.COLUMNS_COUNT;
    return 0;
  }
  
  public String getColumnName(VirtualDisplay display, int col){
    try{
      if (display!=null && display.columns!=null && 
          display.columns.size()>col && !display.columns.get(col).showOnWeb) return null;
    }catch(Exception e){}
    if (display!=null) return display.tab.stageTableAdapter.getColumnName(col);
    return "";
  }
  
  public String getValueAt(VirtualDisplay display, int row, int col){
    try{
      if (display!=null && display.columns!=null && 
          display.columns.size()>col && !display.columns.get(col).showOnWeb) return null;
    }catch(Exception e){}
    if (display!=null) return ""+display.tab.stageTableAdapter.getValueAt(row, col);
    return "";
  }
}
