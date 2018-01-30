/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import javax.swing.JButton;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class StageTableData {
    public VS_STAGE_GROUP group = null;
    public VS_STAGE_GROUPS pilot = null;
    public JButton raceButton = null;
    public boolean isGrpup = false;
    public StageTableData(VS_STAGE_GROUP group) {
      this.group = group;
      isGrpup = true;
    }
    public StageTableData(VS_STAGE_GROUPS pilot) {
      this.pilot = pilot;
      isGrpup = false;
    }
  }