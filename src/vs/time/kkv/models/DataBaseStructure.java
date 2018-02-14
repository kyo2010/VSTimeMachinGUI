/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import static KKV.DBControlSqlLite.DBIControl.fillStat;
import KKV.DBControlSqlLite.UserException;
import KKV.DBControlSqlLite.Utils.Tools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author kyo
 */
public class DataBaseStructure {

  public static DBAddonStructure[] sql_addons = new DBAddonStructure[]{
    new DBAddonStructure(1.1, "ALTER TABLE VS_RACE_LAP ADD TIME_START INTEGER  NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.1, "ALTER TABLE VS_RACE_LAP ADD TIME_FROM_START INTEGER  NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE ADD SORT_TYPE INTEGER  NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE ADD RACE_TYPE INTEGER  NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.2, "ALTER TABLE VS_STAGE_GROUPS ADD SCORE INTEGER  NOT NULL DEFAULT 0;"),
    new DBAddonStructure(1.3, "ALTER TABLE VS_STAGE ADD PARENT_STAGE_ID INTEGER  NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.4, "ALTER TABLE VS_STAGE ADD PILOT_TYPE INTEGER  NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.5, "ALTER TABLE VS_STAGE ADD IS_LOCK INTEGER  NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.6, "ALTER TABLE VS_STAGE_GROUPS ADD WIN INTEGER  NOT NULL DEFAULT 0;"),    
    new DBAddonStructure(1.6, "ALTER TABLE VS_STAGE_GROUPS ADD LOSE INTEGER  NOT NULL DEFAULT 0;"),  
    new DBAddonStructure(1.7, "ALTER TABLE VS_STAGE ADD PILOTS_FOR_NEXT_ROUND INTEGER  NOT NULL DEFAULT 3;"),  
    new DBAddonStructure(1.8, "ALTER TABLE VS_STAGE_GROUPS ADD ACTIVE_FOR_NEXT_STAGE INTEGER NOT NULL DEFAULT 1;"),      
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
