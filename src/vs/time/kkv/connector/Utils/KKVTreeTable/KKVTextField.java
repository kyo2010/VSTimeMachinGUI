/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.KKVTreeTable;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 *
 * @author kyo
 */
public class KKVTextField extends JTextField {

  JPopupMenu popup = new JPopupMenu();

  public KKVTextField() {
    super();

    add(popup);
    setComponentPopupMenu(popup);

    popup.add(new AbstractAction("Copy") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        KKVTextField.this.copy();
      }
    });
    
    popup.add(new AbstractAction("Cut") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        KKVTextField.this.cut();
      }
    });
    
    popup.add(new AbstractAction("Paste") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        KKVTextField.this.paste();
      }
    });
    
    popup.addSeparator();

    popup.add(new AbstractAction("Select All") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        KKVTextField.this.selectAll();
      }
    });
  }
}
