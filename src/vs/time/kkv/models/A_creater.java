/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelTest;
import static KKV.DBControlSqlLite.DBModelTest.generateClazz;
import KKV.Utils.UserException;
import java.sql.Connection;

/**
 *
 * @author kyo
 */
public class A_creater {
  public static void main(String[] args){
    Connection con = null;
    try {
      con = DBModelTest.getConnectionForTest();
            
      //generateClazz(con,"VS_USERS","VS_USERS","vs.time.kkv.models");
      //generateClazz(con,"VS_RACE","VS_RACE","vs.time.kkv.models");
      //generateClazz(con,"VS_RACE_LAP","VS_RACE_LAP","vs.time.kkv.models");
      //generateClazz(con,"VS_SETTING","VS_SETTING","vs.time.kkv.models");
      //generateClazz(con,"VS_RACE_CYCLE","VS_RACE_CYCLE","vs.time.kkv.models");
      //generateClazz(con,"VS_REGISTRATION","VS_REGISTRATION","vs.time.kkv.models");
      //generateClazz(con,"VS_BANDS","VS_BANDS","vs.time.kkv.models");
      //generateClazz(con,"LAST_BANDS","LAST_BANDS","vs.time.kkv.models");
      //generateClazz(con,"VS_PRACTICA","VS_PRACTICA","vs.time.kkv.models");
      //generateClazz(con,"VS_STAGE_GROUPS","VS_STAGE_GROUPS","vs.time.kkv.models");
      
    } catch (UserException ue) {
      System.out.println("Error : " + ue.error + " details : " + ue.details);
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      try{
        if (con!=null) con.close();
      }catch(Exception e){}  
    }
  }
}
