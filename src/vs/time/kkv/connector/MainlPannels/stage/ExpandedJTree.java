/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class ExpandedJTree extends JTree {
  
  @Override
  public void collapsePath(TreePath path) {
    if (path.getLastPathComponent() instanceof VS_STAGE) return;
    super.collapsePath(path); //To change body of generated methods, choose Tools | Templates.       
  }

}
