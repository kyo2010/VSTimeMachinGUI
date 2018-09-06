/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.SCORE;

import java.util.ArrayList;
import java.util.List;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class ArmyScoreCalculation implements IScoreCalculation {

  public ArmyScoreCalculation(){}
  
  @Override
  public String getScoresName() {
    return "Army";
  }
  
  //PLEASE DON't CHAGE
  public String getScoresCode(){
    return "ARM";
  };

  // 1 place - 4
  // 2 place - 3
  // 3 place - 2
  // 4 place - 1
  // ...
  @Override
  public List<Integer> getScores(VS_STAGE stage, List<VS_STAGE_GROUPS> sorted_users) {
     int SCORE = stage.COUNT_PILOTS_IN_GROUP;
     List<Integer> result = new ArrayList();
     int i = 0;
     for (VS_STAGE_GROUPS user : sorted_users) {       
       if (user.LAPS<stage.LAPS)  result.add(0);
       else result.add(SCORE);       
       if (i==0) SCORE--;
       SCORE--;        
       i++;
     }
     return result;
  }    
}
