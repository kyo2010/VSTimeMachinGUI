/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import KKV.DBControlSqlLite.UserException;
import KKV.DBControlSqlLite.Utils.Tools;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainlPannels.InfoForm;
import vs.time.kkv.connector.Utils.KKVTreeTable.JTreeTable;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeCellRender;
import vs.time.kkv.connector.Utils.SpeekUtil;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.FileOutputStream;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 *
 * @author kyo
 */
public class StageTab extends javax.swing.JPanel {

  MainForm mainForm;
  JTreeTable treeTable = null;
  public VS_STAGE stage = null;
  StageTreeModel treeModel = null;
  public JPopupMenu popupMenuJTree = null;
  public StageTableAdapter stageTableAdapter = null;
  Timer raceTimer = new Timer(10, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      long t = Calendar.getInstance().getTimeInMillis();
      long d = t - mainForm.raceTime;
      timerCaption.setText(getTimeIntervel(d));
    }
  });

  public String getTimeIntervel(long time) {
    long min = time / 1000 / 60;
    long sec = time / 1000 - min * 60;
    long milisec = time - (sec + min * 60) * 1000;
    milisec = Math.round(milisec / 10);
    return Tools.padl("" + min, 2, "0") + ":" + Tools.padl("" + sec, 2, "0") + ":" + Tools.padl("" + milisec, 2, "0");
  }

  ;

  /**
   * Creates new form PracticaTableTab
   */
  public StageTab(MainForm main, VS_STAGE _stage) {
    this.stage = _stage;
    initComponents();
    this.mainForm = main;
    //topPanel.setVisible(false);      

    timerCaption.setVisible(false);

    refreshData(false);
    refreshDataActionPerformed(null);
    

    //treeTable = new JTreeTable(new StageTableAdapter2(this));
    //jScrollPane1.add(treeTable);
    //jScrollPane1.setViewportView(treeTable);
    //treeTable.setDragEnabled(true);    
    jTree.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
          int row = jTree.getClosestRowForLocation(e.getX(), e.getY());
          jTree.setSelectionRow(row);
          Object obj = null;
          if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
            obj = jTree.getSelectionPath().getLastPathComponent();
          }
          if (obj != null && (obj instanceof VS_STAGE_GROUPS || obj instanceof VS_STAGE_GROUP)) {
            popupMenuJTree.show(e.getComponent(), e.getX(), e.getY());
          }
        }
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2) {
          editUserTree();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });
    popupMenuJTree = new JPopupMenu();
    JMenuItem miAdd = new JMenuItem("Add");
    popupMenuJTree.add(miAdd);
    JMenuItem miEdit = new JMenuItem("Edit");
    popupMenuJTree.add(miEdit);
    JMenuItem miDelete = new JMenuItem("Delete");
    popupMenuJTree.add(miDelete);
    miEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editUserTree();
      }
    });
    miAdd.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addUserToTreeTree();
      }
    });
    miDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Object obj = null;
        if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
          obj = jTree.getSelectionPath().getLastPathComponent();
        }
        if (obj != null && obj instanceof VS_STAGE_GROUPS) {
          VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
          int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete '" + user.PILOT + "' pilot?", "Delete Pilot from group", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              VS_STAGE_GROUPS.dbControl.delete(mainForm.con, user);
              refreshData(true);
            } catch (UserException ex) {
              mainForm.toLog(ex);
            }
          }
        }
        if (obj != null && obj instanceof VS_STAGE_GROUP) {
          VS_STAGE_GROUP group = (VS_STAGE_GROUP) obj;
          int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete Group" + group.GROUP_NUM + " ?", "Delete group", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              for (VS_STAGE_GROUPS usr : group.users) {
                VS_STAGE_GROUPS.dbControl.delete(mainForm.con, usr);
              }
              refreshData(true);
            } catch (UserException ex) {
              mainForm.toLog(ex);
            }
          }
        }
      }
    });

    jTable.addMouseListener(new MouseAdapter() {
      boolean infoWindowRunning = false;

      @Override
      public synchronized void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
          JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());
          if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
          }
          if (column == 1 && !infoWindowRunning) {
            StageTableData td = StageTab.this.stageTableAdapter.getTableData(row);
            if (mainForm.activeGroup != null && mainForm.activeGroup != td.group) {
              JOptionPane.showMessageDialog(mainForm, "Please stop race. Group" + mainForm.activeGroup.GROUP_NUM, "Information", JOptionPane.INFORMATION_MESSAGE);
              return;
            }

            if (mainForm.activeGroup != null && mainForm.activeGroup == td.group) {
              mainForm.activeGroup = null;
              raceTimer.stop();
              //timerCaption.setVisible(false);              
            } else {
              if (td != null && td.isGrpup == true) {
                mainForm.activeGroup = td.group;
                timerCaption.setText(getTimeIntervel(0));
                timerCaption.setVisible(true);
                infoWindowRunning = true;
                mainForm.speaker.speak("One!");
                InfoForm.init(mainForm, "1").setVisible(true);
                Timer t1 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                  public void actionPerformed(ActionEvent e) {
                    mainForm.speaker.speak("Two!");
                    InfoForm.init(mainForm, "2").setVisible(true);
                    Timer t2 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                      public void actionPerformed(ActionEvent e) {
                        mainForm.speaker.speak("Three!");
                        InfoForm.init(mainForm, "3").setVisible(true);
                        Timer t3 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                          public void actionPerformed(ActionEvent e) {
                            mainForm.speaker.speak("Go!");
                            InfoForm.init(mainForm, "Go!").setVisible(true);
                            mainForm.raceTime = Calendar.getInstance().getTimeInMillis();
                            raceTimer.start();
                            Timer t4 = new Timer(1000, new ActionListener() {      // Timer 4 seconds
                              public void actionPerformed(ActionEvent e) {
                                InfoForm.init(mainForm, "").setVisible(false);
                                infoWindowRunning = false;
                              }
                            });
                            t4.setRepeats(false);
                            t4.start();
                          }
                        });
                        t3.setRepeats(false);
                        t3.start();
                      }
                    });
                    t2.setRepeats(false);
                    t2.start();
                  }
                });
                t1.setRepeats(false);
                t1.start();
              }
            }
            source.updateUI();
          }
        }
      }

      public void mouseReleased(MouseEvent e) {

      }

    });
  }

  public class MinThread extends Thread {

    public int seconds;

    public MinThread(int seconds) {
      this.seconds = seconds;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(seconds * 1000);
      } catch (InterruptedException ex) {
      }
    }
  }

  public void checkGroupConstrain() {

  }

  // expandAllNodes(tree, 0, tree.getRowCount());
  public void expandAllJTree() {
    expandAllNodes(jTree, 0, jTree.getRowCount());
  }

  public void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
    for (int i = startingIndex; i < rowCount; ++i) {
      tree.expandRow(i);
    }
    if (tree.getRowCount() != rowCount) {
      expandAllNodes(tree, rowCount, tree.getRowCount());
    }
  }

  public void editUserTree() {
    Object obj = null;
    if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
      obj = jTree.getSelectionPath().getLastPathComponent();
    }
    if (obj != null && obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) jTree.getSelectionPath().getParentPath().getLastPathComponent();
      StageTabTreeEditForm.init(mainForm, this, group, user).setVisible(true);
    }
  }

  public void addUserToTreeTree() {
    Object obj = null;
    if (jTree.getSelectionPath() != null && jTree.getSelectionPath().getLastPathComponent() != null) {
      obj = jTree.getSelectionPath().getLastPathComponent();
    }
    if (obj != null && obj instanceof VS_STAGE_GROUPS) {
      VS_STAGE_GROUPS user = (VS_STAGE_GROUPS) obj;
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) jTree.getSelectionPath().getParentPath().getLastPathComponent();
      StageTabTreeEditForm.init(mainForm, this, group, null).setVisible(true);
    }
    if (obj != null && obj instanceof VS_STAGE_GROUP) {
      VS_STAGE_GROUP group = (VS_STAGE_GROUP) obj;
      StageTabTreeEditForm.init(mainForm, this, group, null).setVisible(true);
    }
  }

  public void refreshTableData() {
    if (stageTableAdapter != null) {
      stageTableAdapter.loadData();
      jTable.updateUI();
    }
  }

  public void refreshData(boolean refreshInterface) {
    if (stage != null && stage.IS_GROUP_CREATED == 0) {
      createGroups();
    }
    stage.loadGroups(mainForm.con, stage.RACE_ID, stage.ID);
    refreshTableData();
    //jTable.addNotify();
    //jTree.addNotify();
    //jTree.updateUI();
    if (refreshInterface) {
      jTree.updateUI();
      //treeModel = new StageTreeModel(this);
      //jTree.setModel(treeModel);
    }
    expandAllJTree();
  }

  private void print() {
    Document document = new Document(PageSize.A4.rotate());
    try {
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("jTable.pdf"));

      document.open();
      PdfContentByte cb = writer.getDirectContent();

      cb.saveState();
      Graphics2D g2 = cb.createGraphicsShapes(500, 500);

      Shape oldClip = g2.getClip();
      g2.clipRect(0, 0, 500, 500);

      jTable.print(g2);
      g2.setClip(oldClip);

      g2.dispose();
      cb.restoreState();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    document.close();
  }

  public void print2() {
    try {
      //BaseFont bf = BaseFont.createFont("C:\\WINXP\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
      //SystemFont font=new SystemFont(new Font("Arial",Font.BOLD,12),new Color(255,255,255));

      Font font = new Font(bf);

      try {
        int rowCount = jTable.getRowCount();
        //Document document = new Document();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream("C:/data.pdf"));
        document.open();
        int colCount = jTable.getColumnCount();
        PdfPTable tab = new PdfPTable(jTable.getColumnCount());

        float[] widths = new float[colCount];
        for (int i = 0; i < jTable.getColumnCount(); i++) {
          tab.addCell(jTable.getColumnName(i));
          widths[i] = stageTableAdapter.getMinWidth(i);
        }
        tab.setWidths(widths);
        for (int row = 0; row < rowCount; row++) {
          for (int col = 0; col < colCount; col++) {
            Object obj = jTable.getModel().getValueAt(row, col);
            tab.addCell(obj.toString());
          }
        }
        Font font1 = new Font(bf, 12);
        String text1 = "Тест";
        document.add(new Paragraph(text1, font));
        
        text1 = "Test";
        document.add(new Paragraph(text1, font));

        document.add(tab);
        document.close();
      } catch (Exception e) {
      }
    } catch (Exception e) {
    }
  }

  public void createGroups() {
    try {
      if (stage != null && stage.ID != -1) {
        VS_STAGE_GROUPS.dbControl.delete(mainForm.con, "STAGE_ID=?", stage.ID);
        List<VS_REGISTRATION> users = VS_REGISTRATION.dbControl.getList(mainForm.con, "VS_RACE_ID=? ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
        int count_man_in_group = 0;
        int GRUP_NUM = 1;
        String[] channels = stage.CHANNELS.split(";");
        int prev_type_pilot = -1;
        for (VS_REGISTRATION user : users) {
          // Create new Group, if FLAG_BY_PYLOT_TYPE=1 and New Pilot Type 
          if (prev_type_pilot != -1 && prev_type_pilot != user.PILOT_TYPE && stage.FLAG_BY_PYLOT_TYPE == 1) {
            GRUP_NUM++;
            count_man_in_group = 0;
          }
          // Create new group, if full
          count_man_in_group = count_man_in_group + 1;
          if (count_man_in_group > stage.COUNT_PILOTS_IN_GROUP) {
            GRUP_NUM++;
            count_man_in_group = 1;
          }
          VS_STAGE_GROUPS gr = new VS_STAGE_GROUPS();
          gr.STAGE_ID = stage.ID;
          gr.GROUP_NUM = GRUP_NUM;
          gr.PILOT = user.VS_USER_NAME;
          gr.NUM_IN_GROUP = count_man_in_group;
          gr.CHANNEL = channels[count_man_in_group - 1];
          gr.TRANSPONDER = user.VS_TRANSPONDER;
          prev_type_pilot = user.PILOT_TYPE;
          VS_STAGE_GROUPS.dbControl.insert(mainForm.con, gr);
        }
        stage.IS_GROUP_CREATED = 1;
        VS_STAGE.dbControl.update(mainForm.con, stage);
      }
    } catch (Exception e) {
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    topPanel = new javax.swing.JPanel();
    butRemoveSatge = new javax.swing.JButton();
    butConfig = new javax.swing.JButton();
    timerCaption = new javax.swing.JLabel();
    pdfButton = new javax.swing.JButton();
    refreshData = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jSplitPane2 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTree = new ExpandedJTree();

    butRemoveSatge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/remove.png"))); // NOI18N
    butRemoveSatge.setText("Delete Stage");
    butRemoveSatge.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butRemoveSatgeActionPerformed(evt);
      }
    });

    butConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/options.png"))); // NOI18N
    butConfig.setText("Config");
    butConfig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butConfigActionPerformed(evt);
      }
    });

    timerCaption.setBackground(new java.awt.Color(0, 153, 51));
    timerCaption.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    timerCaption.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    timerCaption.setText("00:00:00");
    timerCaption.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 5));

    pdfButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/report-icon.png"))); // NOI18N
    pdfButton.setText("Report");
    pdfButton.setToolTipText("Create PDF Report");
    pdfButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pdfButtonActionPerformed(evt);
      }
    });

    refreshData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/refresh-icon.png"))); // NOI18N
    refreshData.setToolTipText("Refresh data");
    refreshData.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        refreshDataActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
    topPanel.setLayout(topPanelLayout);
    topPanelLayout.setHorizontalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(timerCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(refreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pdfButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butConfig)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butRemoveSatge)
        .addContainerGap())
    );
    topPanelLayout.setVerticalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topPanelLayout.createSequentialGroup()
        .addGap(8, 8, 8)
        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(butConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pdfButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addComponent(butRemoveSatge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(timerCaption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(refreshData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    jSplitPane2.setDividerLocation(200);

    jTable.setModel(new javax.swing.table.DefaultTableModel(
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
    jScrollPane1.setViewportView(jTable);

    jSplitPane2.setRightComponent(jScrollPane1);

    jTree.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
    jTree.setDragEnabled(true);
    jScrollPane2.setViewportView(jTree);

    jSplitPane2.setLeftComponent(jScrollPane2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE))
        .addGap(0, 0, 0)
        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSplitPane1)
          .addComponent(jSplitPane2)))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void butRemoveSatgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRemoveSatgeActionPerformed
    // TODO add your handling code here:
    int res = JOptionPane.showConfirmDialog(this, "Do you want to delete '" + stage.CAPTION + "' Stage?", "Delete Stage", JOptionPane.YES_NO_OPTION);
    if (res == JOptionPane.YES_OPTION) {
      try {
        VS_STAGE.dbControl.delete(mainForm.con, stage);
        mainForm.setActiveRace(mainForm.activeRace);
      } catch (Exception e) {
        mainForm.toLog(e);
      }
    }
  }//GEN-LAST:event_butRemoveSatgeActionPerformed

  private void butConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butConfigActionPerformed
    // TODO add your handling code here:
    StageNewForm.init(mainForm, stage).setVisible(true);
  }//GEN-LAST:event_butConfigActionPerformed

  private void pdfButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfButtonActionPerformed
    // TODO add your handling code here:
    print2();
  }//GEN-LAST:event_pdfButtonActionPerformed

  private void refreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshDataActionPerformed
    // TODO add your handling code here:
    treeModel = new StageTreeModel(this);
    jTree.setModel(treeModel);
    jTree.setTransferHandler(new StageTabTreeTransferHandler(this));
    jTree.setDragEnabled(true);
    jTree.setDropMode(DropMode.USE_SELECTION);
    StageTreeCellRender render = new StageTreeCellRender();    
    jTree.setCellRenderer(render);
    expandAllJTree();

    stageTableAdapter = new StageTableAdapter(this);
    jTable.setModel(stageTableAdapter);
    jTable.setDefaultRenderer(Object.class, stageTableAdapter);

    for (int i = 0; i < stageTableAdapter.getColumnCount(); i++) {
      jTable.getColumnModel().getColumn(i).setMinWidth(stageTableAdapter.getMinWidth(i));
    }
    jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    refreshData(true);     
  }//GEN-LAST:event_refreshDataActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton butConfig;
  private javax.swing.JButton butRemoveSatge;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  public javax.swing.JTable jTable;
  public javax.swing.JTree jTree;
  private javax.swing.JButton pdfButton;
  private javax.swing.JButton refreshData;
  private javax.swing.JLabel timerCaption;
  private javax.swing.JPanel topPanel;
  // End of variables declaration//GEN-END:variables
}
