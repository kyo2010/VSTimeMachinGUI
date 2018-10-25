/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author kyo
 */
class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    public int activeCol = -1;
    public int activeRow = -1;
    ActionListener usersActionListener = null;


    public ButtonEditor(ActionListener usersActionListener) {
        super(new JComboBox());
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
         
        });
        this.usersActionListener = usersActionListener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
          //button.setForeground(table.getSelectionForeground());
          //button.setBackground(table.getSelectionBackground());
        } else {
          //button.setForeground(table.getForeground());
          //button.setBackground(table.getBackground());
        }        
//      StageTableData td = rows.get(row);        
        label = table.getModel().getValueAt(row, column).toString();
        //label = (value == null) ? "" : value.toString();
        button.setText(label);
        activeCol = column;
        activeRow = row;
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
          //JOptionPane.showMessageDialog(button, "Ouch!" + " col:"+activeCol+" row:"+activeRow);       
          if (usersActionListener!=null){
            usersActionListener.actionPerformed(new ActionEvent(this,activeRow,"col:"+activeCol+" row:"+activeRow));
          }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}