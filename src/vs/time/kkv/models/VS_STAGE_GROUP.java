/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.TransferHandler;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import static vs.time.kkv.models.VS_STAGE_GROUPS.VS_STAGE_GROUPS_FLOWER;

/**
 *
 * @author kyo
 */
public class VS_STAGE_GROUP implements Transferable{
  public long GROUP_NUM = 0;   // 1..N     Index in DataBase
  public int GROUP_INDEX = 0; // 0..N-1   phisical index
  public List<VS_STAGE_GROUPS> users = new ArrayList<VS_STAGE_GROUPS>();
  public String useChannels = "";
  public boolean isActive = false;
  public VS_STAGE stage = null;
  public StageTab stageTab = null;
  
  public VS_STAGE_GROUP(VS_STAGE stage){
    this.stage = stage;
  }
  
  public String toString(){
    return "Group "+GROUP_NUM;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{VS_STAGE_GROUPS_FLOWER};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
     if (flavor.equals(VS_STAGE_GROUPS_FLOWER)){
      return true;
    }
     return false;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (flavor.equals(VS_STAGE_GROUPS_FLOWER)){
      return this;
    }
    return null;
  }
}
