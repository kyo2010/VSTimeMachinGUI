/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class VSTM_LapInfo {
  //lap:<Number	packet>,<ID	Base	station>,<ID	transponder>,<Time	in	milliseconds>,<Transponder	start	number>,<CRC	8>\r\n
  public long numberOfPacket;
  public int baseStationID;
  public int transponderID;
  public long time;
  public int transpnderCounter;  
  
  // lap for Pilot number.. start from 1 to N
  public boolean isPilotNumber = false;
  public boolean isPilotChannel = false;
  public int pilotNumber = 0;
  public String pilotChannel = "";
  
  public VS_STAGE_GROUPS pilotObj = null; // Usinf fo PilotNumber 
}
