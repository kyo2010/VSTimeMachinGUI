/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

/**
 *
 * @author kyo
 */
 public class STAGE_COLUMN {

    public static final int CID_PILOT = 10;
    public static final int CID_PILOT_NUM = 15;
    public static final int CID_PILOT_TYPE = 17;
    public static final int CID_CHANNEL = 20;
    public static final int CID_SCORE = 25;
    public static final int CID_WIN = 26;
    public static final int CID_LOSS = 27;
    public static final int CID_TIME = 30;
    public static final int CID_BEST_LAP = 40;
    public static final int CID_LAPS = 50;
    public static final int CID_LAP = 100;
    public static final int CID_QUAL_STATUS = 110;
    public static final int CID_RACE_STATUS = 120;
    
    public String caption;
    public int width;
    public int ID;
    public boolean isEditing = false;
    public String cellID = "TXT";

    public STAGE_COLUMN(int ID, String caption, int width) {
      this.ID = ID;
      this.caption = caption;
      this.width = width;
    }

    public STAGE_COLUMN setIsEditing(boolean isEditing) {
      this.isEditing = isEditing;
      return this;
    }

    public STAGE_COLUMN setCellID(String cellID) {
      this.cellID = cellID;
      return this;
    }        
  }