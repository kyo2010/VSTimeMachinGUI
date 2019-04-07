/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.web;

import java.util.ArrayList;
import java.util.List;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTableData;
import static vs.time.kkv.connector.web.TVTranslationServlet.MAX_TABLE_LINES;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 * Класс для отображения части групп, так как все группы сразу могут влезть на
 * экран телевизора бьем на группы на дисплеи и меняем дисплеи
 */
public class VirtualDisplay {

  boolean byGroup = false;
  public VS_STAGE_GROUP virtual_group = null;
  public List<Integer> numGroups = new ArrayList();
  public List<VS_STAGE_GROUPS> users = new ArrayList();
  public List<VS_STAGE_GROUP> groups = new ArrayList<VS_STAGE_GROUP>();
  public List<StageTableData> rows = new ArrayList<StageTableData>();
  public int CURRENT_DISPLAY = 1;
  public int DISPLAYS_COUNT = 0;
  public boolean SHOW_HEADER = false;
  public StageTab tab = null;
  public int start_row_index = 0;
  public int COLUMNS_COUNT = 0;
  public int WIDTH = 0;
  public List<Integer> WIDTHS = new ArrayList();
  public List<STAGE_COLUMN> columns = null;

  public VirtualDisplay(boolean byGroup) {
    this.byGroup = byGroup;
  }

  public static List<VirtualDisplay> getVirualDisplays(List<VS_STAGE_GROUP> groups,
          double displayHeight, double pilotHeight,
          double groupHeight, double groupSpacerHeight) {
    List<VirtualDisplay> displays = new ArrayList<VirtualDisplay>();
    boolean show_by_group = false;
    double height = 0;
    int CURRENT_DISPLAY = 1;
    if (groups.size() == 1) {
      show_by_group = false;
      VS_STAGE_GROUP group = groups.get(0);      
      VirtualDisplay currentDisplay = new VirtualDisplay(show_by_group);      
      currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY;        
      displays.add(currentDisplay);
      VS_STAGE_GROUP current_group = new VS_STAGE_GROUP(group.stage);
      current_group.showGropuNumber = false;
      currentDisplay.groups.add(current_group);
      for (VS_STAGE_GROUPS user : group.users) {
        if (height <= displayHeight) {
        } else {
          height = groupHeight;
          currentDisplay = new VirtualDisplay(show_by_group);
          CURRENT_DISPLAY++;
          currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY;           
          displays.add(currentDisplay);
          current_group = new VS_STAGE_GROUP(group.stage);
          current_group.showGropuNumber = false;
          currentDisplay.groups.add(current_group);
        }
        height += pilotHeight;
        currentDisplay.users.add(user);
        current_group.users.add(user);
        /*if (user.BEST_LAP < best_lap && user.BEST_LAP != 0) {
          best_lap = user.BEST_LAP;
        }
        if (user.RACE_TIME < best_time && user.RACE_TIME != 0) {
          best_time = user.RACE_TIME;
        }*/
      }
    } else {
      show_by_group = true;
      int index = 0;
      VirtualDisplay currentDisplay = new VirtualDisplay(show_by_group);
      currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY; 
      displays.add(currentDisplay);
      boolean showGropuNumber = true;
      if (groups.size()==1) showGropuNumber = false;
      for (VS_STAGE_GROUP group : groups) {
        group.showGropuNumber = showGropuNumber;
        if ( height + (showGropuNumber?(groupHeight+ (index==0?0:groupSpacerHeight) ):0) + 
             group.users.size()*pilotHeight <= displayHeight ) {
        } else {
          height = 0;
          currentDisplay = new VirtualDisplay(show_by_group);
          CURRENT_DISPLAY++;
          currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY; 
          displays.add(currentDisplay);
        }
        currentDisplay.numGroups.add((int)group.GROUP_NUM);
        height += (showGropuNumber?(groupHeight+ groupSpacerHeight):0) + group.users.size()*pilotHeight;
        currentDisplay.groups.add(group);
        for (VS_STAGE_GROUPS user : group.users) {
          /*if (user.BEST_LAP < best_lap && user.BEST_LAP != 0) {
            best_lap = user.BEST_LAP;
          }
          if (user.RACE_TIME < best_time && user.RACE_TIME != 0) {
            best_time = user.RACE_TIME;
          }*/
        }
        index++;
      }
    }
    return displays;
  }
  
  // for Table Rows
  // List<StageTableData>
  public static List<VirtualDisplay> getVirualDisplaysForTableData(List<StageTableData> rows,
          double displayHeight, double pilotHeight,
          double groupHeight, double groupSpacerHeight, boolean isOneGroup) {
    List<VirtualDisplay> displays = new ArrayList<VirtualDisplay>();
    boolean show_by_group = false;
    double height = 0;
    int CURRENT_DISPLAY = 1;
    if (isOneGroup) {
      VirtualDisplay currentDisplay = new VirtualDisplay(show_by_group);      
      currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY;   
      currentDisplay.SHOW_HEADER = true;
      displays.add(currentDisplay);    
      height = groupHeight;
      int index = 0;
      for (StageTableData row : rows) {
        if (height <= displayHeight) {
        } else {
          height = groupHeight;
          currentDisplay = new VirtualDisplay(show_by_group);
          currentDisplay.start_row_index = index;
          CURRENT_DISPLAY++;
          currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY;  
          currentDisplay.SHOW_HEADER = true;
          displays.add(currentDisplay);          
        }
        height += pilotHeight;
        currentDisplay.rows.add(row);   
        index++;
      }
    } else {
      show_by_group = true;
      VirtualDisplay currentDisplay = new VirtualDisplay(show_by_group);
      currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY; 
      displays.add(currentDisplay);
      int index = 0;
      for (StageTableData row : rows) {      
        if (row.isGrpup && index!=0){
          if ( (height + groupHeight+groupSpacerHeight + 
                row.group.users.size()*pilotHeight) > displayHeight ) 
          {
            height = 0;
            currentDisplay = new VirtualDisplay(show_by_group);
            CURRENT_DISPLAY++;
            currentDisplay.start_row_index = index;
            currentDisplay.CURRENT_DISPLAY = CURRENT_DISPLAY; 
            displays.add(currentDisplay);
          }
        }    
        if (row.isGrpup)
          height += groupHeight+ (index==0?0:groupSpacerHeight);
        else 
          height += pilotHeight;
        currentDisplay.rows.add(row);
        //System.out.println("index : "+index+" height:"+height+" max height : "+displayHeight);
        index++;        
      }      
    }
    return displays;
  }
}
