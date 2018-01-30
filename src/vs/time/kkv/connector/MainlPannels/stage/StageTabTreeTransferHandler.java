/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import KKV.DBControlSqlLite.UserException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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
    if (selRows.length==0) return false;
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
        nn.GID = -1;
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

      try {
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);
        node.GROUP_NUM = parent.GROUP_NUM;
        long max = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, node.STAGE_ID, parent.GROUP_NUM);
        node.NUM_IN_GROUP = (int) (max + 1);
        node.parent = parent;
        VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, node);
        nodesToRemove.parent.users.remove(nodesToRemove);
        parent.users.add(node);
        stageTab.stage.checkConstarin();
        stageTab.refreshTableData();
        tree.updateUI();
      } catch (Exception e) {
        stageTab.mainForm.toLog(e);
      }
      return  true;
    }

    if (parant_obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS beforeNode = (VS_STAGE_GROUPS) dest.getLastPathComponent();
      VS_STAGE_GROUP parent = beforeNode.parent;
      try {                        
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);
        node.GROUP_NUM = parent.GROUP_NUM;                        
        nodesToRemove.parent.users.remove(nodesToRemove);
        parent.users.add(parent.users.indexOf(beforeNode), node);
        node.GID = -1;
        node.STAGE_ID = nodesToRemove.STAGE_ID;
        int index = 1;
        for (VS_STAGE_GROUPS usr : parent.users){
          usr.NUM_IN_GROUP = index;
          usr.GROUP_NUM = parent.GROUP_NUM;          
          index++;
          if (usr==node){
            VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, usr);           
          }else{          
            VS_STAGE_GROUPS.dbControl.update(stageTab.mainForm.con, usr);
          }  
          usr.parent = parent;          
        }                
        stageTab.stage.checkConstarin(); 
        stageTab.refreshTableData();
        tree.updateUI();
      } catch (Exception e) {
        stageTab.mainForm.toLog(e);
      }
      return true;
    }
    
    if (parant_obj instanceof VS_STAGE) {
      VS_STAGE stage = (VS_STAGE)parant_obj;
      VS_STAGE_GROUP parent = new VS_STAGE_GROUP(stage);
      VS_STAGE_GROUP last_group = null;
      for (int group_ndex : stage.groups.keySet()){
        last_group = stage.groups.get(group_ndex);
      }
      parent.GROUP_INDEX = last_group.GROUP_INDEX+1;
      parent.GROUP_NUM = last_group.GROUP_NUM +1;
      stage.groups.put(parent.GROUP_INDEX, parent);

      try {
        VS_STAGE_GROUPS.dbControl.delete(stageTab.mainForm.con, nodesToRemove);
        node.GROUP_NUM = parent.GROUP_NUM;
        long max = VS_STAGE_GROUPS.getMaxNumInGroup(stageTab.mainForm.con, node.STAGE_ID, parent.GROUP_NUM);
        node.NUM_IN_GROUP = (int) (max + 1);
        node.parent = parent;
        VS_STAGE_GROUPS.dbControl.insert(stageTab.mainForm.con, node);
        nodesToRemove.parent.users.remove(nodesToRemove);
        parent.users.add(node);
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
