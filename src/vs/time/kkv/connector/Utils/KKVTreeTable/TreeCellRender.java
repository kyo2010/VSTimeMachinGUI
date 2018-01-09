/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.KKVTreeTable;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author kyo
 */
public class TreeCellRender extends JPanel implements TreeCellRenderer {

  private static final long serialVersionUID = 1L;
  private JTable table;

  public TreeCellRender() {
    super(new BorderLayout());
    table = new JTable();
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
          boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    final String v = (String) ((DefaultMutableTreeNode) value).getUserObject();
    table.setModel(new DefaultTableModel() {
      private static final long serialVersionUID = 1L;

      @Override
      public int getRowCount() {
        return 2;
      }

      @Override
      public int getColumnCount() {
        return 2;
      }

      @Override
      public Object getValueAt(int row, int column) {
        return v + ":" + row + ":" + column;
      }
    });
    table.setPreferredScrollableViewportSize(table.getPreferredSize());
    return this;
  }
}
