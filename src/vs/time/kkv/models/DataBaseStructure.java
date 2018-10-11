/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import static KKV.DBControlSqlLite.DBIControl.fillStat;
import KKV.Utils.UserException;
import KKV.Utils.Tools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;

/**
 *
 * @author kyo
 */
public class DataBaseStructure {

  public static DBAddonStructure[] sql_addons = new DBAddonStructure[]{
    new DBAddonStructure(1.1, "ALTER TABLE VS_RACE_LAP ADD TIME_START INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.1, "ALTER TABLE VS_RACE_LAP ADD TIME_FROM_START INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE ADD SORT_TYPE INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE ADD RACE_TYPE INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE_GROUPS ADD SCORE INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.3, "ALTER TABLE VS_STAGE ADD PARENT_STAGE_ID INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.4, "ALTER TABLE VS_STAGE ADD PILOT_TYPE INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.5, "ALTER TABLE VS_STAGE ADD IS_LOCK INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.6, "ALTER TABLE VS_STAGE_GROUPS ADD WIN INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.6, "ALTER TABLE VS_STAGE_GROUPS ADD LOSE INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(1.7, "ALTER TABLE VS_STAGE ADD PILOTS_FOR_NEXT_ROUND INTEGER NOT NULL DEFAULT 3;"),  
    new DBAddonStructure(1.8, "ALTER TABLE VS_STAGE_GROUPS ADD ACTIVE_FOR_NEXT_STAGE INTEGER NOT NULL DEFAULT 1;"),      
    new DBAddonStructure(1.9, "ALTER TABLE VS_STAGE_GROUPS ADD CHECK_FOR_RACE INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(2.0, "ALTER TABLE VS_RACE ADD PLEASE_IGNORE_FIRST_LAP INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(2.1, "ALTER TABLE VS_STAGE_GROUPS ADD FIRST_LAP INTEGER NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(2.1, "ALTER TABLE VS_RACE ADD MAX_RACE_TIME INTEGER NOT NULL DEFAULT 0;"), 
    
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD VS_TRANS2 INTEGER NOT NULL DEFAULT 0;"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD VS_TRANS3 INTEGER NOT NULL DEFAULT 0;"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD VSID2 INTEGER NOT NULL DEFAULT 0;"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD VSID3 INTEGER NOT NULL DEFAULT 0;"), 
    
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD FIRST_NAME  TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD SECOND_NAME TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD WEB_SYSTEM  TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_REGISTRATION ADD WEB_SID     TEXT NOT NULL DEFAULT '';"), 
    
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD FIRST_NAME  TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD SECOND_NAME TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD WEB_SYSTEM  TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.2, "ALTER TABLE VS_USERS ADD WEB_SID     TEXT NOT NULL DEFAULT '';"), 
    
    new DBAddonStructure(2.2, "ALTER TABLE VS_STAGE_GROUPS ADD REG_ID INTEGER NOT NULL DEFAULT -1;"), 
        
    new DBAddonStructure(2.2, "CREATE TABLE VS_REGISTRATION_TRANS ( VS_RACE_ID INTEGER NOT NULL DEFAULT 0, VS_REG_ID INTEGER  NOT NULL DEFAULT 0, VS_TRANS INTEGER  NOT NULL DEFAULT 0)"), 
    new DBAddonStructure(2.2, "CREATE UNIQUE INDEX IX1_VS_REGISTRATION_TRANS ON VS_REGISTRATION_TRANS  (VS_RACE_ID, VS_REG_ID, VS_TRANS )"), 
    
    new DBAddonStructure(2.3, "ALTER TABLE VS_STAGE ADD COLORS TEXT NOT NULL DEFAULT 'RED;BLUE;GREEN;WHITE';"), 
    new DBAddonStructure(2.4, "ALTER TABLE VS_REGISTRATION ADD PICTURE_FILENAME TEXT NOT NULL DEFAULT '';"), 
    
    new DBAddonStructure(2.5, "ALTER TABLE VS_USERS ADD PHOTO TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.5, "ALTER TABLE VS_REGISTRATION ADD PHOTO TEXT NOT NULL DEFAULT '';"),     
    
    
    new DBAddonStructure(2.8, "PRAGMA foreign_keys=off;"),
    new DBAddonStructure(2.8, "BEGIN TRANSACTION;"),    
    new DBAddonStructure(2.8, "ALTER TABLE VS_USERS RENAME TO VS_USERS_OLD;"),    
    new DBAddonStructure(2.8, "CREATE TABLE VS_USERS ( "+
       "ID INTEGER  PRIMARY KEY AUTOINCREMENT, VSID INTEGER, VS_NAME VARCHAR(200), VS_SOUND_EFFECT INTEGER DEFAULT 1,"+
       "VS_NAME_UPPER TEXT  , VSID2 INTEGER DEFAULT 0, VSID3 INTEGER DEFAULT 0,  FIRST_NAME TEXT DEFAULT ''  NOT NULL,"+
       "SECOND_NAME TEXT DEFAULT ''  NOT NULL, WEB_SYSTEM TEXT DEFAULT '' NOT NULL, WEB_SID TEXT DEFAULT '' NOT NULL,"+
       "PHOTO TEXT DEFAULT '' NOT NULL );"),
    new DBAddonStructure(2.8,"insert into VS_USERS select * from VS_USERS_OLD;"),
    new DBAddonStructure(2.8, "COMMIT;"),
    new DBAddonStructure(2.8, "PRAGMA foreign_keys=on;"),
    new DBAddonStructure(2.8, "DROP TABLE VS_USERS_OLD;"),                 
    
    new DBAddonStructure(2.9, "ALTER TABLE VS_RACE ADD WEB_SYSTEM_SID TRXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(2.9, "ALTER TABLE VS_RACE ADD WEB_SYSTEM_CAPTION TRXT NOT NULL DEFAULT '';"), 
        
    new DBAddonStructure(3.0, "ALTER TABLE VS_REGISTRATION ADD WEB_PHOTO_URL  TEXT NOT NULL DEFAULT '';"),         
    
    new DBAddonStructure(3.1, "ALTER TABLE VS_USERS ADD REGION TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(3.1, "ALTER TABLE VS_REGISTRATION ADD REGION TEXT NOT NULL DEFAULT '';"),  
    
    new DBAddonStructure(3.2, "ALTER TABLE VS_RACE ADD JUDGE TEXT NOT NULL DEFAULT '';"),  
    new DBAddonStructure(3.2, "ALTER TABLE VS_RACE ADD SECRETARY TEXT NOT NULL DEFAULT '';"),  
    
    new DBAddonStructure(3.3, "ALTER TABLE VS_STAGE_GROUPS ADD QUAL_TIME INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.3, "ALTER TABLE VS_STAGE_GROUPS ADD QUAL_POS INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.3, "ALTER TABLE VS_STAGE_GROUPS ADD RACE_TIME_FINAL INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.3, "ALTER TABLE VS_STAGE_GROUPS ADD RACE_TIME_HALF_FINAL INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.3, "ALTER TABLE VS_STAGE_GROUPS ADD RACE_TIME_QUART_FINAL INTEGER NOT NULL DEFAULT 0;"),
    
    new DBAddonStructure(3.4, "ALTER TABLE VS_STAGE_GROUPS ADD GROUP_FINAL INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.4, "ALTER TABLE VS_STAGE_GROUPS ADD GROUP_HALF_FINAL INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(3.4, "ALTER TABLE VS_STAGE_GROUPS ADD GROUP_QUART_FINAL INTEGER NOT NULL DEFAULT 0;"),
    
    new DBAddonStructure(3.4, "ALTER TABLE VS_USERS ADD FAI TEXT NOT NULL DEFAULT '';"), 
    new DBAddonStructure(3.4, "ALTER TABLE VS_REGISTRATION ADD FAI TEXT NOT NULL DEFAULT '';"), 
    
    new DBAddonStructure(3.5, "ALTER TABLE VS_RACE ADD WEB_RACE_ID TEXT NOT NULL DEFAULT '';"),   
    
    new DBAddonStructure(3.6, "ALTER TABLE VS_STAGE ADD REP_COLS TEXT NOT NULL DEFAULT '';"),
    
    new DBAddonStructure(3.7, "ALTER TABLE VS_RACE_LAP ADD REG_ID INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(3.7, "ALTER TABLE VS_STAGE ADD USE_REG_ID_FOR_LAP INTEGR NOT NULL DEFAULT 0;"),
    
    new DBAddonStructure(3.8, "ALTER TABLE VS_STAGE_GROUPS ADD GROUP_TYPE INTEGER NOT NULL DEFAULT 0;"),     
    
    new DBAddonStructure(3.9, "ALTER TABLE VS_RACE ADD AUTO_WEB_UPDATE INTEGER NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(4.0, "ALTER TABLE VS_RACE ADD POST_START INTEGER NOT NULL DEFAULT 0;"),      
    new DBAddonStructure(4.1, "ALTER TABLE VS_RACE ADD LAP_DISTANCE INTEGER NOT NULL DEFAULT 0;"),  
    
    new DBAddonStructure(4.2, "ALTER TABLE VS_STAGE_GROUPS ADD IS_PANDING INTEGER NOT NULL DEFAULT 0;"), 
    new DBAddonStructure(4.3, "ALTER TABLE VS_STAGE_GROUPS ADD LAST_LAP INTEGER NOT NULL DEFAULT 0;"), 
    new DBAddonStructure(4.4, "ALTER TABLE VS_RACE ADD HYBRID_MODE INTEGER NOT NULL DEFAULT 1;"),
    new DBAddonStructure(4.5, "ALTER TABLE VS_RACE ADD RANDOM_BEEP INTEGER NOT NULL DEFAULT 1;"),
    
    new DBAddonStructure(4.6, "ALTER TABLE VS_STAGE ADD TRANSS TEXT NOT NULL DEFAULT '';"),    
    new DBAddonStructure(4.7, "ALTER TABLE VS_STAGE ADD SCORE_CALCULATION TEXT NOT NULL DEFAULT '';"),        
    
    new DBAddonStructure(4.8, "ALTER TABLE VS_STAGE_GROUPS ADD WINS INTEGER NOT NULL DEFAULT 0;"),
    new DBAddonStructure(4.8, "ALTER TABLE VS_STAGE_GROUPS ADD LOSES INTEGER NOT NULL DEFAULT 0;"),
    
    new DBAddonStructure(4.9, "ALTER TABLE VS_RACE ADD ALLOW_TO_FINISH_LAP INTEGER NOT NULL DEFAULT 0;"),               
  };

  public static class DBAddonStructure {

    public double version;
    public String sql;

    public DBAddonStructure(double version, String sql) {
      this.version = version;
      this.sql = sql;
    }
  }

  public static double executeAddons(double currentVersion, Connection conn) throws UserException {
    double max_version = 1.0;
    try {      
      for (DBAddonStructure addon : sql_addons) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try{
          if (addon.version>currentVersion){
            stat = conn.prepareStatement(addon.sql);
            stat.execute();
          }  
        }catch(Exception e){
            int res = JOptionPane.showConfirmDialog(null, "Do you want to continue? Addon sql version " + addon.sql + ", sql:"+addon.sql, "Please check database and restart programm", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
            
            }else{
              throw e;
            }          
        }finally{
          if (rs!=null) rs.close();
          if (stat!=null) stat.close();
        }  
        if (max_version<addon.version) max_version = addon.version;
      }
    } catch (Exception e) {
      throw new UserException("System update is error", Tools.traceError(e));
    }
    return max_version;
  }

}
