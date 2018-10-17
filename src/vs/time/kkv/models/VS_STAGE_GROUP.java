/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.collections.transformation.SortedList;
import javax.swing.TransferHandler;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.stage.SCORE.ScoreCalulationFactory;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import static vs.time.kkv.models.VS_STAGE_GROUPS.VS_STAGE_GROUPS_FLOWER;

/**
 *
 * @author kyo
 */
public class VS_STAGE_GROUP implements Transferable {

  public long GROUP_NUM = 0;   // 1..N     Index in DataBase
  public int GROUP_INDEX = 0; // 0..N-1   phisical index
  public List<VS_STAGE_GROUPS> users = new ArrayList<VS_STAGE_GROUPS>();
  public String useChannels = "";
  public boolean isActive = false;
  public VS_STAGE stage = null;
  public StageTab stageTab = null; 

  public VS_STAGE_GROUP(VS_STAGE stage) {
    this.stage = stage;
  }

  public String toString() {
    return "Group " + GROUP_NUM;
  }
  
  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{VS_STAGE_GROUPS_FLOWER};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)) {
      return true;
    }
    return false;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)) {
      return this;
    }
    return null;
  }

  
  
  public static Comparator GROUP_QUAL_TIME_COMPARATOR = new Comparator<VS_STAGE_GROUPS>() {
    @Override
    public int compare(VS_STAGE_GROUPS o1, VS_STAGE_GROUPS o2) {     
      if (o1.RACE_TIME > o2.RACE_TIME) {
        return 1;
      }
      if (o1.RACE_TIME < o2.RACE_TIME) {
        return -1;
      }
      if (o1.QUAL_TIME > o2.QUAL_TIME) {
        return 1;
      }
      if (o1.QUAL_TIME < o2.QUAL_TIME) {
        return -1;
      }    
      if (o1.LAPS < o2.LAPS) {
        return 1;
      }
      if (o1.LAPS > o2.LAPS) {
        return -1;
      }      
      if (o1.BEST_LAP > o2.BEST_LAP) {
        return 1;
      }
      if (o1.BEST_LAP < o2.BEST_LAP) {
        return -1;
      }
      return 0;
    }
  };   
    
  public void recalculateScores(MainForm mainForm) {
    if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
      List<VS_STAGE_GROUPS> sorted_users = new ArrayList<VS_STAGE_GROUPS>();
      for (VS_STAGE_GROUPS user : users) {
        sorted_users.add(user);
      }
      //Collections.sort(sorted_users, GROUP_TIME_COMPARATOR);
      Collections.sort(sorted_users, GROUP_QUAL_TIME_COMPARATOR);

      int size = users.size();
      int WIN_USERS = Math.round(size / 2);
      int count = 1;
      int SCORE = stage.COUNT_PILOTS_IN_GROUP;
      
      List<Integer> scores = null;
      try{
        scores = ScoreCalulationFactory.getScoreCalulation(stage.SCORE_CALCULATION).getScores(stage, sorted_users);
      }catch(Exception e){}
     
      int i = 0;
      int pos = 1;
      for (VS_STAGE_GROUPS user : sorted_users) {        
        if (scores!=null) {
          user.SCORE = 0;
          try{
            user.SCORE = scores.get(i);
          }catch(Exception e){}
        }else{
          user.SCORE = SCORE;
        }
        
        //user.NUM_IN_GROUP = pos;
        pos++;
        
        if (count <= WIN_USERS) {
          user.WIN = 1;
          user.LOSE = 0;          
        } else {
          user.LOSE = 1;
          user.WIN = 0;
        }
        count++;
        SCORE--;
        try {
          //System.out.println("VS_STAGE_GROUPS - set win/lose2");
          VS_STAGE_GROUPS.dbControl.update(mainForm.con, user);
        } catch (Exception e) {
          MainForm._toLog(e);
        }
        i++;
      }
    }
  }
;
}
