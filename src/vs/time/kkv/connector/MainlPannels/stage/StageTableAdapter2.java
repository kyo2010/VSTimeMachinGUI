/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeTableModel;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class StageTableAdapter2 implements TreeTableModel { 
  
  
  StageTab tab;
  
  public StageTableAdapter2(StageTab tab){
    this.tab = tab;
  }

  @Override
  public int getColumnCount() {
    return 3;  
  }

  @Override
  public String getColumnName(int column) {
    return "Col"+column;
  }

  @Override
  public Class getColumnClass(int column) {
    return String.class;
  }

  @Override
  public Object getValueAt(Object node, int column) {
    if (node instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) node;
      if (column==0)
        return ((VS_STAGE) node).CAPTION;
      else 
        return "";
    }
    if (node instanceof VS_STAGE_GROUP){
      if (column==0)
        return ((VS_STAGE_GROUP) node).toString();
      else 
        return "";    
    }
    if (node instanceof VS_STAGE_GROUPS){
      VS_STAGE_GROUPS usr = (VS_STAGE_GROUPS)node;
      if (column==0)
        return usr.PILOT;
      else 
        return "";    
    }
    return "";
  }

  @Override
  public boolean isCellEditable(Object node, int column) {
    return false;
  }

  @Override
  public void setValueAt(Object aValue, Object node, int column) {
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
    if (parent instanceof VS_STAGE_GROUPS){
      return null;
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
    if (parent instanceof VS_STAGE_GROUPS){
      return 0;
    }
    /*if (parent instanceof VS_STAGE_GROUPS){
      VS_STAGE_GROUPS group = (VS_STAGE_GROUPS) parent;
      return group.
    }*/
    
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
    if (node instanceof VS_STAGE_GROUPS){
      return true; 
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
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
  }
  
}
