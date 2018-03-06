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
