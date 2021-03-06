/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import KKV.Utils.UserException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.*;

class StageTabTreeTransferHandler extends TransferHandler {

  DataFlavor nodesFlavor;
  DataFlavor[] flavors = new DataFlavor[1];
  VS_STAGE_GROUPS nodesToRemove;
  VS_STAGE_GROUPS nodeNew;
  TreePath pathFrom = null;

  boolean remove = false;
  StageTab stageTab = null;

  public StageTabTreeTransferHandler(StageTab stageTab) {
    flavors[0] = VS_STAGE_GROUPS.VS_STAGE_GROUPS_FLOWER;
    this.stageTab = stageTab;
  }

  @Override
  public boolean canImport(TransferHandler.TransferSupport support) {
    if (stageTab.mainForm.activeGroup!=null) return false;
    if (stageTab.stage.IS_LOCK==1) return false;
    if (!support.isDrop()) {
      return false;
    }
    support.setShowDropLocation(true);
    if (!support.isDataFlavorSupported(VS_STAGE_GROUPS.VS_STAGE_GROUPS_FLOWER)) {
      return false;
    }
    // Do not allow a drop on the drag source selections.
    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
    JTree tree = (JTree) support.getComponent();
    /*int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for (int i = 0; i < selRows.length; i++) {
            if (selRows[i] == dropRow) {
                return false;
            }
        }*/
    // Do not allow MOVE-action drops if a non-leaf node is
    // selected unless all of its children are also selected.
    int action = support.getDropAction();
    if (action == MOVE) {
      return haveCompleteNode(tree);
    }
    // Do not allow a non-leaf node to be copied to a level
    // which is less than its source level.
    /*TreePath dest = dl.getPath();
        
        if ( dest.getLastPathComponent() instanceof VS_STAGE_GROUP){
          return true;
        }*/
    return false;

    /*DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (firstNode.getChildCount() > 0 && target.getLevel() < firstNode.getLevel()) {
            return false;
        }
        return true;*/
  }

  private boolean haveCompleteNode(JTree tree) {
    int[] selRows = tree.getSelectionRows();
    if (selRows.length == 0) {
      return false;
    }
    TreePath path = tree.getPathForRow(selRows[0]);
    pathFrom = path;
    try {
      VS_STAGE_GROUPS first = (VS_STAGE_GROUPS) path.getLastPathComponent();
      return true;
    } catch (Exception e) {
    }
    try {
      VS_STAGE_GROUP second = (VS_STAGE_GROUP) path.getLastPathComponent();
      return true;
    } catch (Exception e) {
    }
    try {
      VS_STAGE root = (VS_STAGE) path.getLastPathComponent();
      return true;
    } catch (Exception e) {
    }
    return false;
  }

  @Override
  protected Transferable createTransferable(JComponent c) {
    JTree tree = (JTree) c;
    TreePath[] paths = tree.getSelectionPaths();
    if (paths != null) {
      VS_STAGE_GROUPS node = (VS_STAGE_GROUPS) paths[0].getLastPathComponent();
      nodesToRemove = node;
      try {
        //return new VS_STAGE_GROUPS(nodes);
        VS_STAGE_GROUPS nn = node.copy();
        //nn.GID = ;
        nodeNew = nn;
        return nn;
      } catch (UserException ex) {
        stageTab.mainForm.toLog(ex);
      }
    }
    return null;
  }

  @Override
  protected void exportDone(JComponent source, Transferable data, int action) {
    System.out.println("exportDone started");
    if ((action & MOVE) == MOVE) {
      remove = true;
    } else {
      remove = false;
    }
    System.out.println("exportDone finished");
  }

  @Override
  public int getSourceActions(JComponent c) {
    return COPY_OR_MOVE;
  }

  @Override
  public boolean importData(TransferHandler.TransferSupport support) {
    System.out.println("importData started");
    if (!canImport(support)) {
      return false;
    }
    // Extract transfer data.
    VS_STAGE_GROUPS node = null;
    try {
      Transferable t = support.getTransferable();
      node = (VS_STAGE_GROUPS) t.getTransferData(nodesFlavor);
    } catch (Exception e) {
      node = this.nodeNew;
    }
    // Get drop location info.
    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
    TreePath dest = dl.getPath();

    Object parant_obj = dest.getLastPathComponent();
    JTree tree = (JTree) support.getComponent();
    StageTreeModel model = (StageTreeModel) tree.getModel();

    if (parant_obj instanceof VS_STAGE_GROUP) {
      VS_STAGE_GROUP parent = (VS_STAGE_GROUP) dest.getLastPathComponent();
      node.GROUP_NUM = parent.GROUP_NUM;
      
       if (!canBeNewUserInserted(stageTab.mainForm.con, node)) {
        JOptionPane.showMessageDialog(stageTab.mainForm, "Group " + node.GROUP_NUM + " contains " + node.PILOT +"(" + node.VS_PRIMARY_TRANS+")", "I cann't do it...", JOptionPane.INFORMATION_MESSAGE);
        stageTab.refreshTableData();
        tree.updateUI();
        return false;
      }

      try {
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);        
        long max = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, node.STAGE_ID, parent.GROUP_NUM);
        node.NUM_IN_GROUP = (int) (max + 1);
        node.parent = parent;
        VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, node);
        nodesToRemove.parent.users.remove(nodesToRemove);
        parent.users.add(node);
        moveTimeResult(stageTab.mainForm.con,stageTab.stage.RACE_ID,stageTab.stage.ID,node.VS_PRIMARY_TRANS,nodesToRemove.GROUP_NUM,node.GROUP_NUM);
      
        stageTab.stage.checkConstarin();
        stageTab.refreshTableData();
        tree.updateUI();
      } catch (Exception e) {
        stageTab.mainForm.toLog(e);
      }            
      return true;
    }

    if (parant_obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS beforeNode = (VS_STAGE_GROUPS) dest.getLastPathComponent();
      VS_STAGE_GROUP parent = beforeNode.parent;

      try {       
        node.GROUP_NUM = parent.GROUP_NUM;
        if (nodesToRemove!=null && nodesToRemove.parent!=null && nodesToRemove.parent.users!=null){
          nodesToRemove.parent.users.remove(nodesToRemove);
        } 
        
        if (!canBeNewUserInserted(stageTab.mainForm.con, node)) {
          JOptionPane.showMessageDialog(stageTab.mainForm, "Group " + node.GROUP_NUM + " contains " + node.PILOT +"(" + node.VS_PRIMARY_TRANS+")", "I cann't do it...", JOptionPane.INFORMATION_MESSAGE);
          stageTab.refreshTableData();
          tree.updateUI();
          return false;
        }
        
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);
        
        parent.users.add(parent.users.indexOf(beforeNode), node);        
        node.STAGE_ID = nodesToRemove.STAGE_ID;
        int index = 1;        
        for (VS_STAGE_GROUPS usr : parent.users) {
          usr.NUM_IN_GROUP = index;
          usr.GROUP_NUM = parent.GROUP_NUM;                   
          index++;
          if (usr == node) {
            node.GID = -1;
            VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, usr);
          } else {        
            //System.out.println("VS_STAGE_GROUPS - tree / node");
            VS_STAGE_GROUPS.dbControl.update(stageTab.mainForm.con, usr);
          }
          usr.parent = parent;
        }
        
        moveTimeResult(stageTab.mainForm.con,stageTab.stage.RACE_ID,stageTab.stage.ID,node.VS_PRIMARY_TRANS,nodesToRemove.GROUP_NUM,node.GROUP_NUM);
      
        stageTab.stage.checkConstarin();
        stageTab.refreshTableData();
        tree.updateUI();
      } catch (Exception e) {
        stageTab.mainForm.toLog(e);
      }
      return true;
    }

    if (parant_obj instanceof VS_STAGE) {
      VS_STAGE stage = (VS_STAGE) parant_obj;
      VS_STAGE_GROUP parent = new VS_STAGE_GROUP(stage);
      VS_STAGE_GROUP last_group = null;
      for (int group_ndex : stage.groups.keySet()) {
        last_group = stage.groups.get(group_ndex);
      }
      parent.GROUP_INDEX = last_group.GROUP_INDEX + 1;
      parent.GROUP_NUM = last_group.GROUP_NUM + 1;
      node.GROUP_NUM = parent.GROUP_NUM;
      stage.groups.put(parent.GROUP_INDEX, parent);
      if (!canBeNewUserInserted(stageTab.mainForm.con, node)) {
        JOptionPane.showMessageDialog(stageTab.mainForm, "Group " + node.GROUP_NUM + " contains " + node.PILOT +"(" + node.VS_PRIMARY_TRANS+")", "I cann't do it...", JOptionPane.INFORMATION_MESSAGE);
        stageTab.refreshTableData();
        tree.updateUI();
        return false;
      }

      try {
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);
        long max = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, node.STAGE_ID, parent.GROUP_NUM);
        node.NUM_IN_GROUP = (int) (max + 1);
        node.parent = parent;
        VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, node);
        nodesToRemove.parent.users.remove(nodesToRemove);
        parent.users.add(node);
        
        moveTimeResult(stageTab.mainForm.con,stageTab.stage.RACE_ID,stageTab.stage.ID,node.VS_PRIMARY_TRANS,nodesToRemove.GROUP_NUM,node.GROUP_NUM);      
        
        stageTab.stage.checkConstarin();
        stageTab.refreshTableData();
        tree.updateUI();
      } catch (Exception e) {
        stageTab.mainForm.toLog(e);
      }
      return true;
    }

    System.out.println("importData finished");

    return false;
  }
  
  public boolean moveTimeResult(Connection con, long race_id, long stage_id, int transponderId, long old_group, long new_group){
    if (old_group==new_group) return true;
    try{
     List<VS_RACE_LAP> laps = VS_RACE_LAP.dbControl.getList(con, "RACE_ID=? and STAGE_ID=? and GROUP_NUM=? AND TRANSPONDER_ID=?", race_id,stage_id,old_group,transponderId);
     for (VS_RACE_LAP lap: laps){
        lap.GROUP_NUM = new_group;
        lap.TRANSPONDER_ID = transponderId;
        VS_RACE_LAP.dbControl.update(con, lap);
     }
     return true;
    }catch(Exception e){
      MainForm._toLog(e);
    }
    return false;
  }

  public static boolean canBeNewUserInserted(Connection con, VS_STAGE_GROUPS new_user) {
    return true;
    /*try {
      VS_STAGE_GROUPS t1 = VS_STAGE_GROUPS.dbControl.getItem(con, "STAGE_ID=? and GROUP_NUM=? and TRANSPONDER=?", new_user.STAGE_ID, new_user.GROUP_NUM, new_user.VS_PRIMARY_TRANS);
      if (t1 == null) {
        return true;
      }
      if (t1.GID == new_user.GID) {
        return true;
      }
    } catch (Exception e) {
      MainForm._toLog(e);
    }
    return false;*/
  }

  @Override
  public String toString() {
    return getClass().getName();
  }

  public class NodesTransferable implements Transferable {

    DefaultMutableTreeNode[] nodes;

    public NodesTransferable(DefaultMutableTreeNode[] nodes) {
      this.nodes = nodes;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (!isDataFlavorSupported(flavor)) {
        throw new UnsupportedFlavorException(flavor);
      }
      return nodes;
    }

    public DataFlavor[] getTransferDataFlavors() {
      return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return nodesFlavor.equals(flavor);
    }
  }
}
