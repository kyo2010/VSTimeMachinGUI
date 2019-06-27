/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import KKV.Export2excel.OutReport;
import KKV.Export2excel.XLSMaker;
import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import groovyjarjarantlr.actions.cpp.ActionLexer;
import vs.time.kkv.connector.MainlPannels.stage.StageNewForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.nkv.var.StringVar;
import ru.nkv.var.VarPool;
import ru.nkv.var.pub.IVar;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.MainForm.LastTransponderListener;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationImportForm;
import vs.time.kkv.connector.MainlPannels.RegistrationListImport.RegistrationSites.IRegSite;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.connector.MainlPannels.stage.StageTab;
import vs.time.kkv.connector.MainlPannels.stage.StageTableData;
import vs.time.kkv.connector.Race.RaceControlForm;
import vs.time.kkv.connector.Race.RaceList;
import vs.time.kkv.connector.Utils.KKVTreeTable.ListEditTools;
import vs.time.kkv.connector.Utils.OSDetector;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_REGISTRATION;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class RegistrationTab extends javax.swing.JPanel implements LastTransponderListener {

  public MainForm mainForm;
  public RegistrationModelTable regModelTable = null;
  JPopupMenu popup = null;
  public static int ROW_HEIGHT = 32;

  /**
   * Creates new form RegistrationForm
   */
  public RegistrationTab(MainForm _mainForm) {
    initComponents();
    this.mainForm = _mainForm;
    regModelTable = new RegistrationModelTable(this);
    jtPilotRegistration.setModel(regModelTable);
    jtPilotRegistration.setDefaultRenderer(Object.class, regModelTable);

    //jtPilotRegistration.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    //jtPilotRegistration.getColumnModel().getColumn(4).setPreferredWidth(800);    
    jtPilotRegistration.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    for (int i = 0; i < regModelTable.getColumnCount(); i++) {
      jtPilotRegistration.getColumnModel().getColumn(i).setPreferredWidth(regModelTable.getColumnWidth(i));
      STAGE_COLUMN colInfo = regModelTable.getColumn(i);
      if (colInfo.ID == regModelTable.RWSQ_PILOT_TYPE) {
        jtPilotRegistration.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(ListEditTools.generateBox(mainForm.PILOT_TYPES)));
      }
    }
    jtPilotRegistration.setRowHeight(ROW_HEIGHT);

    popup = new JPopupMenu();
    JMenuItem miAddUserToLastStage = new JMenuItem("Add Pilot to the last stage!");
    popup.add(miAddUserToLastStage);
    JMenuItem miGetTrans = new JMenuItem("Get Tranponder ID from previous Races");
    popup.add(miGetTrans);
    JMenuItem miEdit = new JMenuItem("Edit");
    popup.add(miEdit);
    JMenuItem miAdd = new JMenuItem("Add");
    popup.add(miAdd);
    JMenuItem miDeSelectAll = new JMenuItem("Remove Secection from pilots");
    popup.add(miDeSelectAll);
    JMenuItem miDelete = new JMenuItem("Delete");
    popup.add(miDelete);
    JMenuItem miDeleteAll = new JMenuItem("Delete All");
    popup.add(miDeleteAll);

    miGetTrans.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          List<String[]> res = VS_REGISTRATION.dbControl.getListFromSQL(mainForm.con,
                  "select VS_USER_NAME, VS_TRANSPONDER, VS_TRANS2, VS_TRANS3\n"
                  + "from VS_REGISTRATION as reg\n"
                  + "where reg.VS_RACE_ID = (SELECT MAX(reg1.VS_RACE_ID) from VS_REGISTRATION as reg1\n"
                  + "                         where\n"
                  + "                            reg1.VS_USER_NAME=reg.VS_USER_NAME and\n"
                  + "                            (reg1.VS_TRANSPONDER<>0 or  reg1.VS_TRANS2<>0 or reg1.VS_TRANS3<>0)\n"
                  + "                       )\n"
                  + "order by VS_USER_NAME");
          for (VS_REGISTRATION regInfo : regModelTable.rowsAll) {
            if (regInfo.transIsEmpty()) {
              for (String[] userInfo : res) {
                if (userInfo != null && userInfo.length >= 3) {
                  if (userInfo[0].equalsIgnoreCase(regInfo.VS_USER_NAME)) {
                    try {
                      regInfo.VS_TRANS1 = Integer.parseInt(userInfo[1]);
                    } catch (Exception ein) {
                    }
                    try {
                      regInfo.VS_TRANS2 = Integer.parseInt(userInfo[2]);
                    } catch (Exception ein) {
                    }
                    try {
                      regInfo.VS_TRANS3 = Integer.parseInt(userInfo[3]);
                    } catch (Exception ein) {
                    }
                    try {
                      VS_REGISTRATION.dbControl.update(mainForm.con, regInfo);
                    } catch (Exception ein) {
                    }
                  }
                }
              }
            }
          }
          RegistrationTab.this.refreshData();
        } catch (Exception ex) {
          mainForm.error_log.writeFile(ex);
        }
      }
    });

    miAddUserToLastStage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = jtPilotRegistration.getSelectedRow();
        VS_REGISTRATION regInfo = regModelTable.getRegInfo(row);
        if (regInfo != null) {
          //RegisterPilotlForm.init(mainForm, regInfo.ID).setVisible(true);
          StageTab tab = null;
          for (int i = mainForm.stageTabs.size() - 1; i >= 0; i--) {
            if (!mainForm.stageTabs.get(i).isOneTable) {
              tab = mainForm.stageTabs.get(i);
              break;
            }
          }
          if (tab != null) {
            VS_STAGE_GROUP group = null;
            boolean pleaseAddNewGroup = false;
            for (StageTableData td : tab.stageTableAdapter.rows) {
              if (td.isGrpup) {
                group = td.group;
              }
            }
            if (tab.stage.COUNT_PILOTS_IN_GROUP != 0 && group.users.size() >= tab.stage.COUNT_PILOTS_IN_GROUP) {
              pleaseAddNewGroup = true;
            }
            if (group == null) {
              return;
            }
            if (!pleaseAddNewGroup) {
              for (VS_STAGE_GROUPS user : group.users) {
                if (user.REG_ID == regInfo.ID) {
                  JOptionPane.showMessageDialog(mainForm, "Pilot '" + regInfo.getFullUserName() + "' was included into Group №" + group.GROUP_NUM, "Error!", JOptionPane.INFORMATION_MESSAGE);
                  return;
                }
              }
            }

            int res = JOptionPane.showConfirmDialog(RegistrationTab.this, "Do you want to add '" + regInfo.getFullUserName() + "' to '" + tab.stage.CAPTION + "' stage Group №" + (pleaseAddNewGroup ? group.GROUP_NUM + 1 : group.GROUP_NUM) + " ?", "Please confirm", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
              VS_STAGE_GROUPS pilot = new VS_STAGE_GROUPS();
              pilot.GROUP_NUM = (pleaseAddNewGroup ? group.GROUP_NUM + 1 : group.GROUP_NUM);
              pilot.NUM_IN_GROUP = 1;
              String[] channels = tab.stage.CHANNELS.split(";");
              pilot.CHANNEL = (channels != null && channels.length > 0) ? channels[0] : "";
              if (!pleaseAddNewGroup) {
                pilot.NUM_IN_GROUP = group.users.size();
                if (channels != null) {
                  for (String ch : channels) {
                    boolean isUsed = false;
                    for (VS_STAGE_GROUPS p : group.users) {
                      if (p.CHANNEL.equalsIgnoreCase(ch)) {
                        isUsed = true;
                        break;
                      }
                    }
                    if (!isUsed) {
                      pilot.CHANNEL = ch;
                      break;
                    }
                  }
                }
              }
              pilot.REG_ID = regInfo.ID;
              pilot.PILOT = regInfo.getFullUserName();
              pilot.VS_PRIMARY_TRANS = regInfo.VS_TRANS1;
              pilot.STAGE_ID = tab.stage.ID;
              try {
                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, pilot);
                tab.refreshButton();
              } catch (Exception ex) {
                mainForm.toLog(ex);
              }
            }
          }
        }
      }
    });

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

    miDeSelectAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int res = JOptionPane.showConfirmDialog(RegistrationTab.this, "Do you want to remove selection from all pilots?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
          try {
            VS_REGISTRATION.dbControl.execSql(mainForm.con, "update " + VS_REGISTRATION.dbControl.getTableAlias() + " SET IS_ACTIVE=0 " + " where VS_RACE_ID=?", mainForm.activeRace.RACE_ID);
            RegistrationTab.this.refreshData();
          } catch (Exception ex) {
            mainForm.error_log.writeFile(ex);
          }
        }
      }
    });

    miDeleteAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int res = JOptionPane.showConfirmDialog(RegistrationTab.this, "Do you want to delete all registrations?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
          try {
            VS_REGISTRATION.dbControl.execSql(mainForm.con, "DELETE from " + VS_REGISTRATION.dbControl.getTableAlias() + " where VS_RACE_ID=?", mainForm.activeRace.RACE_ID);
            RegistrationTab.this.refreshData();
          } catch (Exception ex) {
            mainForm.error_log.writeFile(ex);
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

        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
          //mouseReleased(e);
          JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());
          if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
          }
          popup.show(e.getComponent(), e.getX(), e.getY());
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          /*JTable source = (JTable) e.getSource();
          int row = source.rowAtPoint(e.getPoint());
          int column = source.columnAtPoint(e.getPoint());
          if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
          }
          popup.show(e.getComponent(), e.getX(), e.getY());*/
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
    String info = (user != null ? (user.VS_USER_NAME + " - ") : "") + mainForm.lastTranponderID;
    activeTransponder.setText(info);
    last_user = user;
    clearTransonderTimer.restart();
    activeTransponder.setToolTipText("Last Transponder ID : " + transponder);
  }

  Timer clearTransonderTimer = new Timer(5000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      activeTransponder.setText("");
      // activeTransponder.setVisible(false);
    }
  });

  public void refreshData() {
    activeTransponder.setVisible(false);
    regModelTable.loadData();
    //jtPilotRegistration.addNotify();
    //jtPilotRegistration.updateUI();
    jtPilotRegistration.setRowHeight(21);
    mainForm.setTransponderListener(this);

    lPilotsCount.setText("" + regModelTable.rowsAll.size());
    lActivePilotsCount.setText("" + regModelTable.activePilots);
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
        butImport = new javax.swing.JButton();
        butUploadToSite = new javax.swing.JButton();
        bRaceSetting = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edFind = new javax.swing.JTextField();
        bClearFind = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lPilotsCount = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lActivePilotsCount = new javax.swing.JLabel();
        unRaceLapSound = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtPilotRegistration = new javax.swing.JTable();

        butRegistPilot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/user_add.png"))); // NOI18N
        butRegistPilot.setText("Register");
        butRegistPilot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butRegistPilotActionPerformed(evt);
            }
        });

        butAddNewStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
        butAddNewStage.setText("New Stage");
        butAddNewStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butAddNewStageActionPerformed(evt);
            }
        });

        activeTransponder.setBackground(new java.awt.Color(0, 153, 51));
        activeTransponder.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        activeTransponder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activeTransponder.setText("Test");
        activeTransponder.setToolTipText("");
        activeTransponder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));
        activeTransponder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                activeTransponderMouseClicked(evt);
            }
        });

        butExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/xls.png"))); // NOI18N
        butExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butExportActionPerformed(evt);
            }
        });

        butReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/refresh-icon.png"))); // NOI18N
        butReload.setToolTipText("Refresh");
        butReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReloadActionPerformed(evt);
            }
        });

        butImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/import.png"))); // NOI18N
        butImport.setToolTipText("Pilots Import from Web");
        butImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butImportActionPerformed(evt);
            }
        });

        butUploadToSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/export.png"))); // NOI18N
        butUploadToSite.setToolTipText("Save All Result to Web");
        butUploadToSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butUploadToSiteActionPerformed(evt);
            }
        });

        bRaceSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/options.png"))); // NOI18N
        bRaceSetting.setToolTipText("Race Setting");
        bRaceSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRaceSettingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(bRaceSetting, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butRegistPilot, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butAddNewStage, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butImport, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butUploadToSite, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butExport, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butReload, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addComponent(activeTransponder, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bRaceSetting, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(butExport, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(butImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(activeTransponder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(butReload, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(butRegistPilot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(butAddNewStage, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(butUploadToSite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel1.setText("Find");

        edFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seachKey(evt);
            }
        });

        bClearFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/remove2.png"))); // NOI18N
        bClearFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearFindActionPerformed(evt);
            }
        });

        jLabel2.setText("Pilots:");

        lPilotsCount.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lPilotsCount.setText("0");

        jLabel3.setText("Active Pilots:");

        lActivePilotsCount.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lActivePilotsCount.setText("0");

        unRaceLapSound.setText("Lap Info Voice");
        unRaceLapSound.setToolTipText("Enable Sound Lap Info for UNRace mode");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edFind, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bClearFind, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lPilotsCount)
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lActivePilotsCount)
                .addGap(41, 41, 41)
                .addComponent(unRaceLapSound)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(bClearFind, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(edFind)
                            .addComponent(jLabel2)
                            .addComponent(lPilotsCount)
                            .addComponent(jLabel3)
                            .addComponent(lActivePilotsCount)
                            .addComponent(unRaceLapSound))
                        .addGap(3, 3, 3))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
    }// </editor-fold>//GEN-END:initComponents

  private void butRegistPilotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRegistPilotActionPerformed
    // TODO add your handling code here:
    RegisterPilotlForm.init(mainForm, -1).setVisible(true);
  }//GEN-LAST:event_butRegistPilotActionPerformed

  private void butAddNewStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddNewStageActionPerformed
    // TODO add your handling code here:
    StageNewForm.init(mainForm, null, null).setVisible(true);
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

  private void butImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butImportActionPerformed
    // TODO add your handling code here:
    RegistrationImportForm.init(this).setVisible(true);
  }//GEN-LAST:event_butImportActionPerformed

  private void butUploadToSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butUploadToSiteActionPerformed
    // TODO add your handling code here:

    IRegSite site = null;
    if (mainForm.activeRace.WEB_RACE_ID != null && !mainForm.activeRace.WEB_RACE_ID.equals("")) {
      site = RegistrationImportForm.getSite(mainForm.activeRace.WEB_SYSTEM_SID);
      if (site == null) {
        JOptionPane.showMessageDialog(null, "Please make a link with web system");
        return;
      }
      if (site.isSuportedToWebUpload()) {
      } else {
        JOptionPane.showMessageDialog(null, "Update race data is not supported for site :" + site.REG_SITE_NAME);
      }
    } else {
      JOptionPane.showMessageDialog(null, "Please make a link with web system");
      return;
    }

    try {
      site.uploadToWebSystem(this, null, true, true);
      for (StageTab stageTab : mainForm.stageTabs) {
        site.uploadToWebSystem(null, stageTab, false, true);
      }
    } catch (Exception e) {
    }
  }//GEN-LAST:event_butUploadToSiteActionPerformed

  private void bRaceSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRaceSettingActionPerformed
    // TODO add your handling code here:
    RaceControlForm.init(mainForm, mainForm.activeRace.RACE_ID, false).setVisible(true);
  }//GEN-LAST:event_bRaceSettingActionPerformed

  private void seachKey(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seachKey
    // TODO add your handling code here:
    regModelTable.applayFilter();
    jtPilotRegistration.setRowHeight(25);
  }//GEN-LAST:event_seachKey

  private void bClearFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearFindActionPerformed
    edFind.setText("");
    regModelTable.applayFilter();
    jtPilotRegistration.setRowHeight(25);
  }//GEN-LAST:event_bClearFindActionPerformed

  public void tableToXLS() {
    try {
      XSSFWorkbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();

      vs.time.kkv.connector.Utils.TableToXLS.tableToXLS2(wb, "Registration", mainForm.activeRace.RACE_NAME, "Registration", jtPilotRegistration, regModelTable.getColumns(), false);
      for (StageTab stageTab : mainForm.stageTabs) {
        try {
          stageTab.tableToXLS(wb);
        } catch (Exception e) {
        }
      }

      new File("XLS").mkdirs();
      String xlsFile = "XLS/" + Tools.createName(new JDEDate().getDateAsYYYYMMDD("-") + "_", 5) + ".xlsx";
      FileOutputStream fileOut = new FileOutputStream(xlsFile);
      wb.write(fileOut);
      fileOut.close();

      OSDetector.open(new File(xlsFile));
    } catch (Exception eout) {
      mainForm.toLog(eout);
      eout.printStackTrace();
      JOptionPane.showMessageDialog(this, "Creating xls-file is error.");

    }

  }

  public void tableToXLS_old() {
    try {
      JDEDate jd = new JDEDate();
      OutReport out = new OutReport(jd.getDDMMYYYY("-"));
      //out.setShowExcel(true);

      String sheetName = "Registration";
      out.setReportName(jd.getDDMMYYYY("-") + "_reg");
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
        stageTab.tableToXLS_old(out);
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
    private javax.swing.JButton bClearFind;
    private javax.swing.JButton bRaceSetting;
    private javax.swing.JButton butAddNewStage;
    private javax.swing.JButton butExport;
    private javax.swing.JButton butImport;
    private javax.swing.JButton butRegistPilot;
    private javax.swing.JButton butReload;
    private javax.swing.JButton butUploadToSite;
    public javax.swing.JTextField edFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtPilotRegistration;
    public javax.swing.JLabel lActivePilotsCount;
    private javax.swing.JLabel lPilotsCount;
    public javax.swing.JCheckBox unRaceLapSound;
    // End of variables declaration//GEN-END:variables
}
