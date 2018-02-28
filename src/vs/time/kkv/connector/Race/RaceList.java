/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Race;

import vs.time.kkv.connector.Users.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_STAGE;

/**
 *
 * @author kyo
 */
public class RaceList extends javax.swing.JFrame {

  public MainForm mainForm = null;
  public RaceListModelTable raceModelTable = null;
  public JPopupMenu popup = null;

  /**
   * Creates new form UserList
   */
  public RaceList(MainForm _mainForm) {
    initComponents();
    this.mainForm = _mainForm;
    raceModelTable = new RaceListModelTable(mainForm);
    jtUsers.setModel(raceModelTable);

    jtUsers.getColumnModel().getColumn(2).setMinWidth(300);

    popup = new JPopupMenu();
    JMenuItem miSelectRace = new JMenuItem("Select Race");
    popup.add(miSelectRace);
    JMenuItem miEdit = new JMenuItem("Edit");
    popup.add(miEdit);
    JMenuItem miDelete = new JMenuItem("Delete");
    popup.add(miDelete);
    miSelectRace.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectRace();
      }
    });
    miEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = jtUsers.getSelectedRow();
        raceModelTable.showEditDialog(row);
      }
    });
    miDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = jtUsers.getSelectedRow();
        VS_RACE race = raceModelTable.getRace(row);
        if (race != null) {
          int res = JOptionPane.showConfirmDialog(RaceList.this, "Do you want to delete '" + race.RACE_NAME + "' Race?", "Delete Race", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              VS_RACE.dbControl.delete(mainForm.con, race);
              RaceList.this.refreshData();
            } catch (Exception ex) {
              mainForm.error_log.writeFile(ex);
            }
          }
        }
      }
    });
    jtUsers.add(popup);
    //popup.set

    jtUsers.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        try {
          if (e.getButton() == MouseEvent.BUTTON3) {
            //popup.setLocation(e.getLocationOnScreen());
            //popup.show(e.getComponent(), e.getX(), e.getY());
            //popup.setVisible(true);            
          } else {
            //popup.setVisible(false);   
          }
          if (e.getClickCount() == 2) {
            selectRace();
            //raceModelTable.showEditDialog(row);          
            //JOptionPane.showMessageDialog(UserList.this, "Rows:"+jtUsers.getSelectedRow(), "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (Exception ex) {
          mainForm.error_log.writeFile(ex);
          //JOptionPane.showMessageDialog(UserList.this, "Rows:"+jtUsers.getSelectedRow(), "Error", JOptionPane.ERROR_MESSAGE);        
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());

          if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
          }
          popup.show(e.getComponent(), e.getX(), e.getY());
        }
      }

    });

    setVisible(false);
  }

  public void selectRace() {
    int row = jtUsers.getSelectedRow();
    VS_RACE race = raceModelTable.getRace(row);
    try {
      if (race != null) {
        VS_RACE.dbControl.execSql(mainForm.con, "UPDATE " + VS_RACE.dbControl.getTableAlias() + " SET IS_ACTIVE=0");
      }
      race.IS_ACTIVE = 1;
      VS_RACE.dbControl.update(mainForm.con, race);
    } catch (Exception ex) {
      mainForm.toLog(ex);
    }
    setVisible(false);
    mainForm.setActiveRace(race);
  }

  public void refreshData() {
    raceModelTable.loadData();
    jtUsers.addNotify();
  }

  private static RaceList form = null;

  public static RaceList init(MainForm mainForm) {
    if (form == null) {
      form = new RaceList(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    return form;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    butAddNewRace = new javax.swing.JButton();
    butRaceEdit = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    butClose = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jtUsers = new javax.swing.JTable();

    setTitle("Race List");
    setIconImage(MainForm.getWindowsIcon().getImage());

    butAddNewRace.setText("Add New Race");
    butAddNewRace.setPreferredSize(new java.awt.Dimension(80, 23));
    butAddNewRace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butAddNewRaceActionPerformed(evt);
      }
    });

    butRaceEdit.setText("Race edit");
    butRaceEdit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butRaceEditActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(butAddNewRace, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(butRaceEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(butAddNewRace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(butRaceEdit))
        .addGap(125, 125, 125))
    );

    butClose.setText("Close");
    butClose.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butCloseActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(butClose)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(butClose)
        .addContainerGap())
    );

    jtUsers.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null}
      },
      new String [] {
        "Transponder", "Name"
      }
    ));
    jtUsers.setRowHeight(28);
    jtUsers.setRowMargin(2);
    jtUsers.setRowSelectionAllowed(false);
    jScrollPane1.setViewportView(jtUsers);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void butCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCloseActionPerformed
    setVisible(false);
  }//GEN-LAST:event_butCloseActionPerformed

  private void butAddNewRaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddNewRaceActionPerformed
    RaceControlForm.init(mainForm, -1).setVisible(true);
  }//GEN-LAST:event_butAddNewRaceActionPerformed

  private void butRaceEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRaceEditActionPerformed
    // TODO add your handling code here:
    int row = jtUsers.getSelectedRow();
    raceModelTable.showEditDialog(row);
  }//GEN-LAST:event_butRaceEditActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(RaceList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(RaceList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(RaceList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(RaceList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new RaceList(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton butAddNewRace;
  private javax.swing.JButton butClose;
  private javax.swing.JButton butRaceEdit;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable jtUsers;
  // End of variables declaration//GEN-END:variables
}
