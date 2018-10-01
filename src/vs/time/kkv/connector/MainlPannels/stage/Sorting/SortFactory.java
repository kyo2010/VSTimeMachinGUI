/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.Sorting;

import java.util.ArrayList;
import java.util.List;

/**
 *  vs.time.kkv.connector.MainlPannels.stage.Sorting.SortFactory.getSortOrders()
 * @author kyo
 */
public class SortFactory {
 
  // @TODO : remove Sort Types from MainForm, and uses only this Factory
  public static final int STAGE_SORT_BY_RACE_TIME = 0;
  public static final int STAGE_SORT_BY_LAP_TIME = 1;
  public static final int STAGE_SORT_BY_SCORE_DESC = 2;
  public static final int STAGE_SORT_BY_LOSS_DESC = 3;
  public static final int STAGE_SORT_BY_LAPS = 4;
  public static final int STAGE_SORT_BY_LAPS_AND_SCORES = 5;
  public final static String[] STAGE_SORTS = new String[]{"Race time", 
                                                          "Best lap time", 
                                                         // "Score", 
                                                          //"Loss",
                                                          //"Laps & LAP Time", "Laps & Scores"
                                                         };
  
  static int DEF_SORT_INDEX = 0;

  static List<ISort> SORTS = new ArrayList();
  static{    
    SORTS.add(new GROUP_RACE_TIME());
    SORTS.add(new GROUP_BEST_LAP());    
    SORTS.add(new GROUP_SCORE_COMPARATOR());
    SORTS.add(new GROUP_LOST_COMPARATOR());                
    SORTS.add(new GROUP_LAPS_COMPARATOR());
    SORTS.add(new GROUP_LAPS_AND_SCORES_COMPARATOR());
  }
  
  public static ISort getSortComparatorByID(int sortID){
    for (ISort sort : SORTS){
      if (sort.getCode()==sortID) return sort;
    }
    return null;
  }
  
  public static String[] getSortOrders(){
    String[] res = new String[SORTS.size()];
    int index = 0;
    for (ISort sort : SORTS){
      res[index] = sort.getName();
      index++;        
    }
    return res;
  }
  
}
