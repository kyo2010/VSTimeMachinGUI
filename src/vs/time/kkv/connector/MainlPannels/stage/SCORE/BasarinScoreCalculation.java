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
public class BasarinScoreCalculation implements IScoreCalculation {

  public BasarinScoreCalculation(){}
  
  @Override
  public String getScoresName() {
    return "BasarinScoreCalculation";
  }
  
  //PLEASE DON't CHAGE
  public String getScoresCode(){
    return "BAS";
  };

  // 1 place - 500
  // 2 place - 475
  // 3 place - 350
  // 4 place - 325
  // ...
  @Override
  public List<Integer> getScores(VS_STAGE stage, List<VS_STAGE_GROUPS> sorted_users) {
     int SCORE = 500;
     List<Integer> result = new ArrayList();
     int i = 0;
     
     int[] SCORES = new int[]{ 500,475,350,325,300,275,250,225 };
     
     for (VS_STAGE_GROUPS user : sorted_users) {       
       SCORE = 0;
       if (SCORES.length>i) SCORE = SCORES[i];
       result.add(SCORE+user.wins*100);              
       i++;
     }
     return result;
  }    
}
