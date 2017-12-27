/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeTableModel;

/**
 *
 * @author kyo
 */
public class PracticaTableAdapter implements TreeTableModel { 
  
  
  PracticaTab tab;
  
  public PracticaTableAdapter(PracticaTab tab){
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
    return "Node:"+ ((String[]) node)[0]+" Value:"+column;
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
    return new String[]{"M1","M2"};
  }

  @Override
  public Object getChild(Object parent, int index) {
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    return 0;
  }

  @Override
  public boolean isLeaf(Object node) {
    return false;
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return 0;
  }

  @Override
  public void addTreeModelListener(TreeModelListener l) {    
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
  }
  
}
