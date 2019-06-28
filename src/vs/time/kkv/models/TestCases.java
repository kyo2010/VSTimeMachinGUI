/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelTest;
import KKV.Utils.UserException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class TestCases {

  public static final String DATABASE_TEST_POOL = "races_pool.s3db";
  public static final String DATABASE_TEST = "races_temp.s3db";
  
  public static final int TEST_RACE_REGISTRATION = 100;
  public static final int TEST_RACE_STAGE_CRATING = 200;
  public static final int TEST_RACE_STAGE_CREATED = 300;
  public static final int TEST_RACE_FINISHED = 400;
  
  public Connection conPoolTests = null; // Database for Test
  public Connection conTest = null;  // Temp Database for Test
  MainForm mainForm = null;
  
  public boolean TEST_IS_RUN = false;
  public int     TEST_CURRENT_RACE_ITEM = 0;
  public int     TEST_STATE = 0;
  public int     TEST_STAGE_ITEM = -1;
  public List<VS_RACE> TEST_RACES = null;
  public VS_RACE TEST_RACE = null; 
  
  public TestCases(MainForm mainForm) {
    this.mainForm = mainForm;
  }
  
  public void continueTest() throws UserException{
    if (TEST_RACE==null){
      // Start new Race
      TEST_CURRENT_RACE_ITEM++;
      if (TEST_RACES!=null && TEST_RACES.size()>TEST_CURRENT_RACE_ITEM){
        TEST_RACE = TEST_RACES.get(TEST_CURRENT_RACE_ITEM);      
        runTest(TEST_RACE);
      }  
    }else{
      
    }                     
  };

  public void addRaceToTest() {
    if (mainForm.activeRace != null) {
      prepareConnections();

      try {
        VS_RACE new_race = new VS_RACE();
        int old_race_id = mainForm.activeRace.RACE_ID;
        VS_RACE.dbControl.copyObject(mainForm.activeRace, new_race);
        new_race.RACE_ID = -1;
        VS_RACE.dbControl.insert(conPoolTests, new_race);
        List<VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? order by NUM", old_race_id);
        Map<Long, Long> new_reg_ids = new HashMap<Long, Long>();
        for (VS_REGISTRATION reg : regs) {
          long old_reg_id = reg.ID;
          reg.VS_RACE_ID = new_race.RACE_ID;
          reg.ID = -1;
          VS_REGISTRATION.dbControl.insert(conPoolTests, reg);
          long new_reg_id = reg.ID;
          new_reg_ids.put(old_reg_id, new_reg_id);
        }
        int new_race_id = new_race.RACE_ID;
        List<VS_STAGE> stages = VS_STAGE.dbControl.getList(mainForm.con, "RACE_ID=? order by ID", old_race_id);
        Map<Long, Long> new_stage_ids = new HashMap<Long, Long>();
        for (VS_STAGE stage : stages) {
          long old_stage_id = stage.ID;
          List<VS_STAGE_GROUPS> groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GROUP_NUM, NUM_IN_GROUP, GID", old_stage_id);
          stage.ID = -1;
          stage.RACE_ID = new_race_id;
          VS_STAGE.dbControl.insert(conPoolTests, stage);
          long new_stage_id = stage.ID;
          new_stage_ids.put(old_stage_id, new_stage_id);
          for (VS_STAGE_GROUPS group : groups) {
            group.STAGE_ID = new_stage_id;
            group.GID = -1;
            try {
              group.REG_ID = new_reg_ids.get(group.REG_ID);
            } catch (Exception e) {
              mainForm.toLog("Error to transform REG_ID:" + group.REG_ID);
            }
            VS_STAGE_GROUPS.dbControl.insert(conPoolTests, group);
          }
        }

        List<VS_RACE_LAP> laps = VS_RACE_LAP.dbControl.getList(mainForm.con, "1=1 order by STAGE_ID, GROUP_NUM, LAP");
        for (VS_RACE_LAP lap : laps) {
          lap.ID = -1;
          try {
            lap.STAGE_ID = new_stage_ids.get(lap.STAGE_ID);
            lap.REG_ID = new_reg_ids.get(lap.REG_ID);
            lap.RACE_ID = new_race_id;
            VS_RACE_LAP.dbControl.insert(conPoolTests, lap);
          } catch (Exception e) {
            mainForm.toLog("Error to transform REG_ID:" + lap.REG_ID + " or STAGE_ID:" + lap.STAGE_ID);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        closeConnections();
      }
    }
  }

  public void runTest(VS_RACE race) throws UserException {
    VS_RACE new_race = new VS_RACE();
    TEST_STAGE_ITEM = -1;
    int old_race_id = race.RACE_ID;
    VS_RACE.dbControl.copyObject(race, new_race);
    new_race.RACE_ID = -1;
    VS_RACE.dbControl.insert(conTest, new_race);
    new_race.setActive(conTest);
    int new_race_id = new_race.RACE_ID;
    List<VS_REGISTRATION> regs = VS_REGISTRATION.dbControl.getList(conPoolTests, "VS_RACE_ID=? order by NUM", old_race_id);
    Map<Long, Long> new_reg_ids = new HashMap<Long, Long>();
    for (VS_REGISTRATION reg : regs) {
      long old_reg_id = reg.ID;
      reg.VS_RACE_ID = new_race_id;
      reg.ID = -1;
      VS_REGISTRATION.dbControl.insert(conTest, reg);
      long new_reg_id = reg.ID;
      new_reg_ids.put(old_reg_id, new_reg_id);
    }
    TEST_STATE = TEST_RACE_REGISTRATION;
    mainForm.switchDataBase(conTest);
    mainForm.setActiveRace(new_race, true);
  }

  public void runTest() throws UserException {
    TEST_IS_RUN = true;
    TEST_CURRENT_RACE_ITEM = 0;
    prepareConnections();    
    clearDataBase(conTest);
    
    try {
      TEST_RACES = VS_RACE.dbControl.getList(conPoolTests, "1=1 order by RACE_ID");
      if (TEST_RACES.size()>0){
        TEST_RACE = TEST_RACES.get(0);      
        runTest(TEST_RACE);
      }  
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.mainForm, "Test is failed. Race ID: "+TEST_RACE.RACE_NAME+" ["+TEST_RACE.RACE_ID+"]", "Information", JOptionPane.INFORMATION_MESSAGE);      
    } finally {
      closeConnections();
    }
  }

  public void clearDataBase(Connection con) {
    try {
      VS_USERS.dbControl.delete(con, "1=1");
      VS_REGISTRATION.dbControl.delete(con, "1=1");
      VS_RACE.dbControl.delete(con, "1=1");
      VS_RACE_LAP.dbControl.delete(con, "1=1");
      VS_STAGE.dbControl.delete(con, "1=1");
      VS_STAGE_GROUPS.dbControl.delete(con, "1=1");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void prepareConnections() {
    try {
      conPoolTests = DBModelTest.getConnection(DATABASE_TEST_POOL);
      // check for update
      double db_version = VS_SETTING.getParam(conPoolTests, "DataBaseVersion", 1.0);
      double db_version_new = DataBaseStructure.executeAddons(db_version, conPoolTests);
      VS_SETTING.setParam(conPoolTests, "DataBaseVersion", "" + db_version_new);
    } catch (UserException ue) {
      mainForm.error_log.writeFile(ue);
      JOptionPane.showMessageDialog(mainForm, ue.details, ue.error, JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST_POOL, "Error", JOptionPane.ERROR_MESSAGE);
    }

    try {
      conTest = DBModelTest.getConnection(DATABASE_TEST);
      // check for update
      double db_version = VS_SETTING.getParam(conTest, "DataBaseVersion", 1.0);
      double db_version_new = DataBaseStructure.executeAddons(db_version, conTest);
      VS_SETTING.setParam(conTest, "DataBaseVersion", "" + db_version_new);
    } catch (UserException ue) {
      mainForm.error_log.writeFile(ue);
      JOptionPane.showMessageDialog(mainForm, ue.details, ue.error, JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void closeConnections() {
    try {
      if (conPoolTests != null) {
        conPoolTests.close();
        conPoolTests = null;
      }
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST_POOL, "Error", JOptionPane.ERROR_MESSAGE);
    }

    try {
      if (conTest != null) {
        conTest.close();
        conTest = null;
      }
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
