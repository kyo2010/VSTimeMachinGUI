/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author kyo
 */
public class MultiLineHeaderRenderer /*extends JList implements TableCellRenderer*/ 
        extends DefaultTableCellRenderer {
  public MultiLineHeaderRenderer() {
    setOpaque(true);
    setForeground(UIManager.getColor("TableHeader.foreground"));
    setBackground(UIManager.getColor("TableHeader.background"));
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    //ListCellRenderer renderer = getCellRenderer();
    //((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
    //setCellRenderer(renderer);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    
    String value_st = ""+value;
    //int pos = value_st.indexOf("\n");
    //if (pos>0){
    value_st = "<html><center><b>"+value_st.replaceAll("\n", "<br>")+"</b></center></html>";
    //}
    JLabel label = (JLabel)super.
            getTableCellRendererComponent(table,value_st, isSelected, hasFocus,row, column);
    /*setFont(table.getFont().deriveFont(Font.BOLD));
    String str = (value == null) ? "" : value.toString();
    BufferedReader br = new BufferedReader(new StringReader(str));
    String line;
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    //setListData(v);
    //setAlignmentX(10);*/
    
   // this.setVerticalTextPosition(SwingConstants.CENTER);
   // setHorizontalTextPosition(SwingConstants.RIGHT);
    return label;
  }
}
