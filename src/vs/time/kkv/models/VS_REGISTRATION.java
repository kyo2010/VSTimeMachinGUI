/*** KKV Class Generator V.0.1, This class is based on 'VS_REGISTRATION' table.
 *** The class was generated automatically.  ***/

package vs.time.kkv.models;

import KKV.DBControlSqlLite.*;
import KKV.Utils.UserException;
import java.io.File;
import java.sql.Connection;
import java.sql.Time;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import vs.time.kkv.connector.MainlPannels.ImageImplement;

public class VS_REGISTRATION {
  
  public static final String PHOTO_PATH = "web/pilots_photo/";
  
  public long ID = -1;   //  NOT_DETECTED  
  public long VS_RACE_ID;   //  NOT_DETECTED
  //public long USER_REG_ID;   //  NOT_DETECTED
  public long NUM;
//public int VS_TRANSPONDER;   //  NOT_DETECTED
  public int VS_TRANS1 = 0;   //  NOT_DETECTED
  public int VS_TRANS2 = 0;   //  NOT_DETECTED
  public int VS_TRANS3 = 0;   //  NOT_DETECTED
  
  public String VS_USER_NAME = "";   //  NOT_DETECTED
  public int IS_ACTIVE = 1;   //  NOT_DETECTED
  public int VS_SOUND_EFFECT = 1;
  public int PILOT_TYPE = 0;  
  
  public String FIRST_NAME = "";   //  NOT_DETECTED
  public String SECOND_NAME= "";   //  NOT_DETECTED
  public String WEB_SYSTEM = "";
  public String WEB_SID = "";
  public String PICTURE_FILENAME = "";
  public String PHOTO = "";  
  public String WEB_PHOTO_URL = "";
  public String REGION = "";
  public String FAI = "";
  
  /** Constructor */ 
  public VS_REGISTRATION() {
  };
  
  public static DBModelControl<VS_REGISTRATION> dbControl = new DBModelControl<VS_REGISTRATION>(VS_REGISTRATION.class, "VS_REGISTRATION", new DBModelField[]{
    new DBModelField("ID").setDbFieldName("\"ID\"").setAutoIncrement(),
    new DBModelField("NUM").setDbFieldName("\"NUM\""),
    new DBModelField("VS_RACE_ID").setDbFieldName("\"VS_RACE_ID\""),
    //new DBModelField("USER_REG_ID").setDbFieldName("\"USER_REG_ID\""),
   // new DBModelField("VS_TRANSPONDER").setDbFieldName("\"VS_TRANSPONDER\""),
    new DBModelField("VS_TRANS1").setDbFieldName("\"VS_TRANSPONDER\""),
    new DBModelField("VS_TRANS2").setDbFieldName("\"VS_TRANS2\""),
    new DBModelField("VS_TRANS3").setDbFieldName("\"VS_TRANS3\""),
    
    new DBModelField("VS_USER_NAME").setDbFieldName("\"VS_USER_NAME\""),
    new DBModelField("IS_ACTIVE").setDbFieldName("\"IS_ACTIVE\""),
    new DBModelField("VS_SOUND_EFFECT").setDbFieldName("\"VS_SOUND_EFFECT\""),
    new DBModelField("PILOT_TYPE").setDbFieldName("\"PILOT_TYPE\""),
    
    new DBModelField("FIRST_NAME").setDbFieldName("\"FIRST_NAME\""),
    new DBModelField("SECOND_NAME").setDbFieldName("\"SECOND_NAME\""),
    new DBModelField("WEB_SYSTEM").setDbFieldName("\"WEB_SYSTEM\""),
    new DBModelField("WEB_SID").setDbFieldName("WEB_SID"),
    new DBModelField("PICTURE_FILENAME").setDbFieldName("PICTURE_FILENAME"),
    
    new DBModelField("PHOTO").setDbFieldName("PHOTO"),
    new DBModelField("WEB_PHOTO_URL").setDbFieldName("WEB_PHOTO_URL"),    
    new DBModelField("REGION").setDbFieldName("REGION"),    
    new DBModelField("FAI").setDbFieldName("FAI"),    
  });
  
  public static long maxNum(Connection conn, long raceID){
    long num = 0;
    try{
      num = dbControl.getMax(conn, "NUM", "VS_RACE_ID=?", raceID);
    }catch(Exception e){}
    return num;
  }
  
  public String toString(){
    return getFullUserName();
  } 
  
  public String getTransponders(){
    String trans = ""+VS_TRANS1;
    if (VS_TRANS2!=0) trans+=";"+VS_TRANS2;
    if (VS_TRANS3!=0) trans+=";"+VS_TRANS3;
    return trans;
  }
  
  public String getFullUserName(){
    return VS_USER_NAME+" / "+FIRST_NAME+" "+SECOND_NAME;
  }
  
  public static VS_USERS updateGlobalUserPHOTO(Connection con, VS_REGISTRATION usr){
    // Creating global user  
    VS_USERS global_user = null;
    try{
      global_user = VS_USERS.dbControl.getItem(con, "VS_NAME=?", usr.VS_USER_NAME);
    }catch(Exception e){}  
    
      if (global_user==null){
        global_user = new VS_USERS();
        global_user.FIRST_NAME = usr.FIRST_NAME;
        global_user.SECOND_NAME = usr.SECOND_NAME;
        global_user.VS_NAME = usr.VS_USER_NAME;
        global_user.VS_NAME_UPPER = global_user.VS_NAME.toUpperCase();
        global_user.VSID1 =usr.VS_TRANS1;
        global_user.VSID2 =usr.VS_TRANS2;
        global_user.VSID3 =usr.VS_TRANS3;
        global_user.VS_SOUND_EFFECT = usr.VS_SOUND_EFFECT;
        global_user.WEB_SID = usr.WEB_SID;
        global_user.WEB_SYSTEM = usr.WEB_SYSTEM;
        global_user.PHOTO = usr.PHOTO;
        try{
          VS_USERS.dbControl.insert(con, global_user);
        }catch(UserException e){
          System.out.println(e.error+" "+e.details);      
        }catch(Exception e){
          e.printStackTrace();
        }  
      }
      
      if (global_user!=null && global_user.PHOTO!=null && !global_user.PHOTO.equals("")){
        //usr.PHOTO = global_user.PHOTO;
      }           
      
      // PHOTO           
      if (!usr.PHOTO.equalsIgnoreCase("")){                        
        if (!global_user.PHOTO.equalsIgnoreCase("")) {
          if (!usr.PHOTO.equalsIgnoreCase(global_user.PHOTO)) new File(global_user.PHOTO).delete();                    
        }   
        global_user.PHOTO = usr.PHOTO;
        String fileName = usr.PHOTO;
        fileName = VS_REGISTRATION.PHOTO_PATH+"pilot_"+global_user.ID+"."+ FilenameUtils.getExtension(usr.PHOTO);
        new File(VS_REGISTRATION.PHOTO_PATH).mkdirs();
        try{
         // FileUtils.copyFile(new File(usr.PHOTO), new File(fileName));
         ImageImplement.savePhotoAndResize(usr.PHOTO,fileName);
        }catch(Exception e){}
        usr.PHOTO = fileName;
        global_user.PHOTO = fileName;
        try{
          VS_REGISTRATION.dbControl.update(con,usr);    
          VS_USERS.dbControl.update(con,global_user); 
         }catch(UserException e){
          System.out.println(e.error+" "+e.details);        
        }catch(Exception e){}  
      } else{
        usr.PHOTO = global_user.PHOTO;
        try{
          VS_USERS.dbControl.update(con,global_user);   
         }catch(UserException e){
          System.out.println(e.error+" "+e.details);        
        }catch(Exception e){}  
      }
    return global_user;
  }
}
