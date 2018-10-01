/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage.Sorting;

import java.util.Comparator;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public abstract class ISort implements Comparator<VS_STAGE_GROUPS> {
  int code = 0;
  
  public ISort(){
    code = SortFactory.DEF_SORT_INDEX;
    SortFactory.DEF_SORT_INDEX++;
  }
  
  public int getCode(){
    return code;
  };
  public abstract String getName();
}
