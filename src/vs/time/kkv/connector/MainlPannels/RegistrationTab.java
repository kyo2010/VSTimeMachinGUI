/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import KKV.Export2excel.OutReport;
import KKV.Export2excel.XLSMaker;
import KKV.Utils.JDEDate;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import vs.time.kkv.connector.MainlPannels.stage.StageNewForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import ru.nkv.var.StringVar;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainForm.LastTransponderListener;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.Race.RaceList;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;

/**
 *
 * @author kyo
 */
public class RegistrationTab extends javax.swing.JPanel implements LastTransponderListener {

  MainForm mainForm;
  RegistrationModelTable regModelTable = null;
  JPopupMenu popup = null;

  /**
   * Creates new form RegistrationForm
   */
  public RegistrationTab(MainForm _mainForm) {
    initComponents();
    this.mainForm = _mainForm;
    regModelTable = new RegistrationModelTable(this);
    jtPilotRegistration.setModel(regModelTable);
    jtPilotRegistration.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    jtPilotRegistration.getColumnModel().getColumn(4).setPreferredWidth(800);
    jtPilotRegistration.setRowHeight(28);
    jtPilotRegistration.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(ListEditTools.generateBox(mainForm.PILOT_TYPES)));

    popup = new JPopupMenu();
    JMenuItem miEdit = new JMenuItem("Edit");
    popup.add(miEdit);
    JMenuItem miAdd = new JMenuItem("Add");
    popup.add(miAdd);
    JMenuItem miDelete = new JMenuItem("Delete");
    popup.add(miDelete);
    miEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = jtPilotRegistration.getSelectedRow();
        VS_REGISTRATION regInfo = regModelTable.getRegInfo(row);
        if (regInfo != null) {
          RegisterPilotlForm.init(mainForm, regInfo.ID).setVisible(true);
        }
      }
    });
    miAdd.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        butRegistPilotActionPerformed(e);
      }
    });
    miDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = jtPilotRegistration.getSelectedRow();
        VS_REGISTRATION regInfo = regModelTable.getRegInfo(row);
        if (regInfo != null) {
          int res = JOptionPane.showConfirmDialog(RegistrationTab.this, "Do you want to delete '" + regInfo.VS_USER_NAME + "' Pilot?", "Delete Pilot", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              regInfo.dbControl.delete(mainForm.con, regInfo);
              RegistrationTab.this.refreshData();
            } catch (Exception ex) {
              mainForm.error_log.writeFile(ex);
            }
          }
        }
      }
    });
    jtPilotRegistration.add(popup);

    jtPilotRegistration.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
          JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());
          
            source.changeSelection(row, column, false, false);
            VS_REGISTRATION regInfo = regModelTable.getRegInfo(row);
            if (regInfo != null) {
               RegisterPilotlForm.init(mainForm, regInfo.ID).setVisible(true);
            }          
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

    refreshData();
  }

  long transponder = -1;
   VS_REGISTRATION last_user = null;

  @Override
  public void newTransponder(long transponder, VS_REGISTRATION user) {
    activeTransponder.setVisible(true);
    this.transponder = transponder;
    String info = (user!=null?(user.VS_USER_NAME + " - "):"") + mainForm.lastTranponderID;
    activeTransponder.setText( info );    
    last_user = user;
  }

  public void refreshData() {
    activeTransponder.setVisible(false);
    regModelTable.loadData();
    jtPilotRegistration.updateUI();
    mainForm.setTransponderListener(this);
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
    jPanel2 = new javax.swing.JPanel();
    butRegistPilot = new javax.swing.JButton();
    butAddNewStage = new javax.swing.JButton();
    activeTransponder = new javax.swing.JLabel();
    butExport = new javax.swing.JButton();
    butReload = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jtPilotRegistration = new javax.swing.JTable();

    butRegistPilot.setText("Register Pilot");
    butRegistPilot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butRegistPilotActionPerformed(evt);
      }
    });

    butAddNewStage.setText("Add a New Race Stage");
    butAddNewStage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butAddNewStageActionPerformed(evt);
      }
    });

    activeTransponder.setBackground(new java.awt.Color(0, 153, 51));
    activeTransponder.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    activeTransponder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    activeTransponder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));
    activeTransponder.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        activeTransponderMouseClicked(evt);
      }
    });

    butExport.setText("Report");
    butExport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butExportActionPerformed(evt);
      }
    });

    butReload.setText("Reload");
    butReload.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butReloadActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(butRegistPilot)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butAddNewStage, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butExport)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butReload)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
        .addComponent(activeTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(5, 5, 5)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(butRegistPilot)
          .addComponent(butAddNewStage)
          .addComponent(activeTransponder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butExport)
          .addComponent(butReload)))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(5, 5, 5))
    );

    jtPilotRegistration.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    jScrollPane1.setViewportView(jtPilotRegistration);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jScrollPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void butRegistPilotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRegistPilotActionPerformed
    // TODO add your handling code here:
    RegisterPilotlForm.init(mainForm, -1).setVisible(true);
  }//GEN-LAST:event_butRegistPilotActionPerformed

  private void butAddNewStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddNewStageActionPerformed
    // TODO add your handling code here:
    StageNewForm.init(mainForm, null).setVisible(true);
  }//GEN-LAST:event_butAddNewStageActionPerformed

  private void activeTransponderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activeTransponderMouseClicked
    // TODO add your handling code here:
    if (SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() >= 2) {
      RegisterPilotlForm reg = RegisterPilotlForm.init(mainForm, last_user == null ? -1 : last_user.ID);
      //reg.
      if (last_user == null && transponder != -1) {
        reg.edTransponder.setText("" + transponder);
      }
      reg.setVisible(true);
    }
  }//GEN-LAST:event_activeTransponderMouseClicked

  private void butExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butExportActionPerformed
    // TODO add your handling code here:
    tableToXLS();
  }//GEN-LAST:event_butExportActionPerformed

  private void butReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReloadActionPerformed
    // TODO add your handling code here:
    regModelTable.loadData();
    jtPilotRegistration.updateUI();
  }//GEN-LAST:event_butReloadActionPerformed

  public void tableToXLS() {
    try {
      JDEDate jd = new JDEDate();
      OutReport out = new OutReport(jd.getDDMMYYYY("-"));
      //out.setShowExcel(true);

      String sheetName = "Registration";
      out.setReportName(mainForm.activeRace.RACE_NAME);
      int sheet = out.addStream();

      out.setReportName(sheet, sheetName);
      out.setViewFileName(sheet, "view.xml");
      IVar pool = new VarPool();
      pool.addChild(new StringVar("VisibleSheet", ""));
      pool.addChild(new StringVar("ConditionalFormatting", ""));
      pool.addChild(new StringVar("ExcelCellNames", ""));
      pool.addChild(new StringVar("ColumsInfo", ""));
      pool.addChild(new StringVar("FIX_ROWS", "4"));

      out.applayPoolToViewFile(sheet, pool);
      out.addToDataFile(sheet, "info:$$Race : " + mainForm.activeRace.RACE_NAME + " as of " + jd.getDDMMYYYY("-") + "$$");
      out.addToDataFile(sheet, "info:$$Stage : " + sheetName + "$$");
      out.addToDataFile(sheet, "info:");

      int rowCount = jtPilotRegistration.getRowCount();
      int colCount = jtPilotRegistration.getColumnCount();
      String head = "head:";
      for (int i = 1; i < colCount; i++) {
        head += "$$" + jtPilotRegistration.getColumnName(i) + "$$:";
      }
      out.addToDataFile(sheet, head);

      for (int row = 0; row < rowCount; row++) {
        String line = "data:";
        for (int col = 1; col < colCount; col++) {
          Object obj = jtPilotRegistration.getModel().getValueAt(row, col);
          line += "{TXT}$$" + obj + "$$:";
        }
        out.addToDataFile(sheet, line);
      }

      for (StageTab stageTab : mainForm.stageTabs) {
        stageTab.tableToXLS(out);
      }

      out.closeDataStreams();
      String xlsFile = XLSMaker.makeXLS(out);
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + xlsFile + "\"");   //open the file chart.pdf 
    } catch (Exception e) {
      e.printStackTrace();
      mainForm._toLog(e);
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel activeTransponder;
  private javax.swing.JButton butAddNewStage;
  private javax.swing.JButton butExport;
  private javax.swing.JButton butRegistPilot;
  private javax.swing.JButton butReload;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTable jtPilotRegistration;
  // End of variables declaration//GEN-END:variables
}
