/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.SCORE;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kyo
 */
public class ScoreCalulationFactory {
  static public List<IScoreCalculation> SCORES_CALCULATION = new ArrayList<IScoreCalculation>();
  static{
    SCORES_CALCULATION.add(new ClassicScoreCalculation()); // default Score Calculation for new stage
    SCORES_CALCULATION.add(new ArmyScoreCalculation());
  }
  
  public static IScoreCalculation getScoreCalulation(String SCORE_CALCULATION_CODE){
    for (IScoreCalculation cal  :SCORES_CALCULATION){
      if (cal.getScoresCode().equalsIgnoreCase(SCORE_CALCULATION_CODE)) return cal;
    }
    return SCORES_CALCULATION.get(0);
  }
  
  public static int getScoreCalulationIndex(String SCORE_CALCULATION_CODE){
    int i = 0;
    for (IScoreCalculation cal  :SCORES_CALCULATION){
      if (cal.getScoresCode().equalsIgnoreCase(SCORE_CALCULATION_CODE)) return i;
      i++;
    }
    return 0;
  }
  
  public static IScoreCalculation getScoreCalulationIndex(int index){
    if (index<SCORES_CALCULATION.size()){
      return SCORES_CALCULATION.get(index);
    }
    return SCORES_CALCULATION.get(0);
  }
  
  public static String[] getScoreCalulationNames(){
    int i = 0;
    String[] res = new String[SCORES_CALCULATION.size()];
    for (IScoreCalculation cal  :SCORES_CALCULATION){
      res[i] = cal.getScoresName();
      i++;
    }
    return res;
  }
}
