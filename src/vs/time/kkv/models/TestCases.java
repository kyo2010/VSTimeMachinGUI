/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import KKV.DBControlSqlLite.DBModelTest;
import KKV.Utils.UserException;
import java.sql.Connection;
import javax.swing.JOptionPane;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class TestCases {

  public static final String DATABASE_TEST_POOL = "races_pool.s3db";
  public static final String DATABASE_TEST = "races_temp.s3db";

  public Connection conPoolTests = null; // Database for Test
  public Connection conTest = null;  // Temp Database for Test
  MainForm mainForm = null;

  public TestCases(MainForm mainForm) {
    this.mainForm = mainForm;
  }

  public void addRaceToTest() {
    prepareConnections();
    
    closeConnections();
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
      VS_SETTING.setParam(conPoolTests, "DataBaseVersion", "" + db_version_new);
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
      if (conPoolTests!=null){
        conPoolTests.close();
        conPoolTests = null;      
      }  
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST_POOL, "Error", JOptionPane.ERROR_MESSAGE);
    }

    try {
      if (conTest!=null){
        conTest.close();
        conTest = null;      
      }  
    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(mainForm, "Database file is not found. " + DATABASE_TEST, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
