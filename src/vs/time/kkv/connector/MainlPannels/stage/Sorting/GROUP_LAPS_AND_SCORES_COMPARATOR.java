/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.Sorting;

import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class GROUP_LAPS_AND_SCORES_COMPARATOR extends ISort{
  
   public String getName(){
     return "Laps & Scores";
   }
      
  @Override
    public int compare(VS_STAGE_GROUPS o1, VS_STAGE_GROUPS o2) {
      if (o1.LAPS > o2.LAPS) {
        return -1;
      }
      if (o1.LAPS < o2.LAPS) {
        return 1;
      }
    if (o1.SCORE>o2.SCORE){
        return -1;
      }
      if (o1.SCORE<o2.SCORE){
        return 1;
      }
      if (o1.RACE_TIME > o2.RACE_TIME) {
        return 1;
      }
      if (o1.RACE_TIME < o2.RACE_TIME) {
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
}
