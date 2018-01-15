/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class StageTreeModel implements TreeModel {
  
  StageTab tab;
  
  public StageTreeModel(StageTab tab){
    this.tab = tab;
  }

  @Override
  public Object getRoot() {
    return tab.stage; 
  }

  @Override
  public Object getChild(Object parent, int index) {
    if (parent!=null && parent instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) parent;
      VS_STAGE_GROUP group = stage.groups.get(index);
      return group;
    }   
    if (parent instanceof VS_STAGE_GROUP){
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) parent;
      VS_STAGE_GROUPS usr = group.users.get(index);
      return usr;
    }
    //if (parent instanceof )
    return "";
  }

  @Override
  public int getChildCount(Object parent) {
    if (parent instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) parent;
      return stage.groups.size();
    }
    if (parent instanceof VS_STAGE_GROUP){
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) parent;
      return group.users.size();
    }
    //if (parent instanceof )
    return 0;
  }

  @Override
  public boolean isLeaf(Object node) {
   if (node instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) node;
      if (stage.groups.size()>0) return false;
    }
    if (node instanceof VS_STAGE_GROUP){
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) node;
      if (group.users.size()>0) return false; 
    }
    return true;
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) parent;
      for (int key: stage.groups.keySet()){
        if (stage.groups.get(key).equals(child)){
          return key;
        }
      }
    }
    if (parent instanceof VS_STAGE_GROUP){
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) parent;
      int index = 0;
      for (VS_STAGE_GROUPS usr : group.users){
        if (usr.equals(child)) return index;
        index++;
      }
    }
    return 0;
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    
  }

  @Override
  public void addTreeModelListener(TreeModelListener l) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
