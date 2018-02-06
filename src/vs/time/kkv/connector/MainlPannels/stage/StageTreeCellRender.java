/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.color.ColorSpace;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class StageTreeCellRender extends DefaultTreeCellRenderer{
  public StageTreeCellRender() {
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component cmp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); //To change body of generated methods, choose Tools | Templates.
     if (value instanceof VS_STAGE_GROUPS){
      VS_STAGE_GROUPS usr = (VS_STAGE_GROUPS) value;
      if (usr.isError>0){
        if (!sel){
          if (usr.isError==1)
            setForeground(Color.RED);
          if (usr.isError>=2)
            setForeground(Color.BLUE);
        }else{
          setForeground(Color.YELLOW);          
        }          
      }
    }
    return cmp;
  }       
}
