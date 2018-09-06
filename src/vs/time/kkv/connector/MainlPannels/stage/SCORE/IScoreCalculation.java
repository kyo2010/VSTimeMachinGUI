/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.SCORE;

import java.util.List;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public interface IScoreCalculation {
  public String getScoresName();
  public String getScoresCode();
  public List<Integer> getScores(VS_STAGE stage, List<VS_STAGE_GROUPS> sorted_users);
}
