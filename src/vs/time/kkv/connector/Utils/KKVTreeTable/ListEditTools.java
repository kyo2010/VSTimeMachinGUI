/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.KKVTreeTable;

import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author kyo
 */
public class ListEditTools {
  public static int returnIndex(String[] items, Object value){
    int index = 0;
    String st_value = (String) value;
    for (String item : items){
      if (item.equalsIgnoreCase(st_value)){
        return index;
      }
      index++;
    }      
    return 0;
  }
  
  public static int returnIndex(List<String> items, Object value){
    int index = 0;
    String st_value = (String) value;
    for (String item : items){
      if (item.equalsIgnoreCase(st_value)){
        return index;
      }
      index++;
    }      
    return 0;
  }
  
  public static JComboBox generateBox(String[] items){
     JComboBox bx=null;
     bx=new JComboBox();
     for (String item : items){
       bx.addItem(item);
     }
     return bx;
  }
  
  public static JComboBox generateBox(List<String> items){
     JComboBox bx=null;
     bx=new JComboBox();
     for (String item : items){
       bx.addItem(item);
     }
     return bx;
  }
}
