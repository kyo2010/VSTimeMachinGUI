/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

import KKV.Utils.Tools;
import java.sql.Time;

/**
 *
 * @author kyo
 */
public class DBTimeString {
  public String time_st = "";
  public Time time = null;
  
  public String toString(){
    return time_st;
  }
  
  public DBTimeString(){
  }
  
  public DBTimeString(String value){
    time=null;
    try{
      long xls_time = (long)(Double.parseDouble(value)*24*60*60);
      time = new Time(xls_time*1000);
      int hh = (int) xls_time/(60*60);
      double mm1 = xls_time - hh*60*60;
      int mm = (int) mm1/(60);
      int ss = (int) mm1-mm*60;
      time_st = Tools.padl(""+hh, 2, '0')+":"+Tools.padl(""+mm, 2, '0');
    }catch(Exception e){
      time_st = value;
    }  
    
    try{
        int pos = value.indexOf("-");
        if (pos > 0 && time==null) {
          int hh = Integer.parseInt(value.substring(0, pos));
          int mm = Integer.parseInt(value.substring(pos + 1,pos +3 ));
          time = Time.valueOf(hh + ":" + mm + ":00");          
        };
    }catch(Exception e){} 
    
    try{
        int pos = value.indexOf(":");
        if (pos > 0 && time==null) {
          int hh = Integer.parseInt(value.substring(0, pos));
          int mm = Integer.parseInt(value.substring(pos + 1,pos +3 ));
          time = Time.valueOf(hh + ":" + mm + ":00");          
        };
    }catch(Exception e){} 
  }
  
  public static void main (String[] args){
    DBTimeString ts = new DBTimeString("15:00");
  }
          
}
