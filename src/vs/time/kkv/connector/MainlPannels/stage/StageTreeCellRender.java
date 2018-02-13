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
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class StageTreeCellRender extends DefaultTreeCellRenderer {

  StageTab tab = null;

  public StageTreeCellRender(StageTab tab) {
    this.tab = tab;
  }

  public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
  public static final Color DEFAULT_BACKGROUD_COLOR = Color.WHITE;

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component cmp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); //To change body of generated methods, choose Tools | Templates.
    //setBackground(DEFAULT_BACKGROUD_COLOR);
    //setForeground(DEFAULT_FOREGROUND_COLOR);

    cmp.setBackground(DEFAULT_BACKGROUD_COLOR);
    cmp.setForeground(DEFAULT_FOREGROUND_COLOR);
    cmp.setFont(getFont().deriveFont(Font.PLAIN));

    if (tab.isOneTable) {
      cmp.setBackground(DEFAULT_BACKGROUD_COLOR);
      cmp.setForeground(DEFAULT_FOREGROUND_COLOR);
      cmp.setFont(getFont().deriveFont(Font.PLAIN));
    } else {
      if (value instanceof VS_STAGE_GROUPS) {
        VS_STAGE_GROUPS usr = (VS_STAGE_GROUPS) value;
        if (usr.isError > 0) {
          if (!sel) {
            if (usr.isError == 1) {
              cmp.setForeground(Color.RED);
            }
            if (usr.isError >= 2) {
              cmp.setForeground(Color.BLUE);
            }
          } else {
            cmp.setForeground(Color.YELLOW);
          }
        }

        if (usr.parent == tab.mainForm.activeGroup) {
          //cmp.setBackground(Color.GREEN);
          //cmp.setForeground(Color.BLACK);
          cmp.setFont(getFont().deriveFont(Font.BOLD)); //  Font.PLAIN     
        } else {
        }
      }
    }
    return cmp;
  }
}
