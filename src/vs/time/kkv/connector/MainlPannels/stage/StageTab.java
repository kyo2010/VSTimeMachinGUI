/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import KKV.DBControlSqlLite.UserException;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
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
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.Utils.KKVTreeTable.JTreeTable;
import vs.time.kkv.connector.Utils.KKVTreeTable.TreeCellRender;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

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

  /**
   * Creates new form PracticaTableTab
   */
  public StageTab(MainForm main, VS_STAGE _stage) {
    this.stage = _stage;
    initComponents();
    this.mainForm = main;
    //topPanel.setVisible(false);      
    
    butStartRace.setVisible(false);

    refreshData(false);
    treeModel = new StageTreeModel(this);
    jTree.setModel(treeModel);
    jTree.setTransferHandler(new StageTabTreeTransferHandler(this));
    jTree.setDragEnabled(true);
    jTree.setDropMode(DropMode.USE_SELECTION);
    StageTreeCellRender render = new StageTreeCellRender();
    //render.setClosedIcon(new ImageIcon(...));
    //render.setOpenIcon(new ImageIcon(...));
    //render.setLeafIcon(new ImageIcon(...)); 
    jTree.setCellRenderer(render);       
    expandAllJTree();

    stageTableAdapter = new StageTableAdapter(this);
    jTable.setModel(stageTableAdapter);
    jTable.setDefaultRenderer(Object.class, stageTableAdapter);
    
    for (int i=0; i<stageTableAdapter.getColumnCount(); i++){
      jTable.getColumnModel().getColumn(i).setMinWidth(stageTableAdapter.getMinWidth(i));
    }    
    jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
              refreshData(false);
            } catch (UserException ex) {
              mainForm.toLog(ex);
            }
          }
        }
        if (obj!=null && obj instanceof VS_STAGE_GROUP){
        VS_STAGE_GROUP group = (VS_STAGE_GROUP) obj;
          int res = JOptionPane.showConfirmDialog(StageTab.this, "Do you want to delete Group"+group.GROUP_NUM+" ?", "Delete group", JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            try {
              for (VS_STAGE_GROUPS usr :  group.users){
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
  }
  
  public void checkGroupConstrain (){
  
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
  
  public void refreshTableData(){
    if (stageTableAdapter!=null){
      stageTableAdapter.loadData();
      jTable.updateUI();
    }  
  }

  public void refreshData(boolean refreshInterface) {
    if (stage != null && stage.IS_GROUP_CREATED == 0) {
      createGroups();
    }
    stage.loadGroups(mainForm.con);
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
    butStartRace = new javax.swing.JButton();
    butRemoveSatge = new javax.swing.JButton();
    butConfig = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jSplitPane2 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTree = new ExpandedJTree();

    butStartRace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/race_add.png"))); // NOI18N
    butStartRace.setText("Start Race");
    butStartRace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butStartRaceActionPerformed(evt);
      }
    });

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

    javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
    topPanel.setLayout(topPanelLayout);
    topPanelLayout.setHorizontalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(butStartRace)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
          .addComponent(butConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butRemoveSatge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(butStartRace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
          .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE))
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

  private void butStartRaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butStartRaceActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_butStartRaceActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton butConfig;
  private javax.swing.JButton butRemoveSatge;
  private javax.swing.JButton butStartRace;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  private javax.swing.JTable jTable;
  public javax.swing.JTree jTree;
  private javax.swing.JPanel topPanel;
  // End of variables declaration//GEN-END:variables
}
