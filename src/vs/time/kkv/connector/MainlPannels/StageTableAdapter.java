/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeTableModel;
import vs.time.kkv.models.VS_STAGE;

/**
 *
 * @author kyo
 */
public class StageTableAdapter implements TreeTableModel { 
  
  
  StageTab tab;
  
  public StageTableAdapter(StageTab tab){
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
    return "Group";
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
      return stage.groups.get(index-1);
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
    //if (parent instanceof )
    int y = 0;
    return 0;
  }

  @Override
  public boolean isLeaf(Object node) {
    if (node instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) node;
      return false;
    }
    return true;
  }
  
  @Override
  public int getIndexOfChild(Object parent, Object child) {
    /*if (parent instanceof VS_STAGE){
      VS_STAGE stage = (VS_STAGE) parent;
      for (Integer groupNum : stage.groups.keySet()){
        if (stage.groups.get(groupNum).equals(child)) return groupNum-1;
      }
    }    
    int y = 0;*/
    return 0;
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
