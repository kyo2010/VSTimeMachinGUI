/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import KKV.DBControlSqlLite.UserException;
import KKV.DBControlSqlLite.Utils.JDEDate;
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
import java.io.File;
import java.io.FileOutputStream;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static vs.time.kkv.connector.MainlPannels.stage.StageTableAdapter.getLapNumberFromCol;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_SETTING;

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
    JMenuItem miExport = new JMenuItem("Export");
    popupMenuJTree.add(miExport);
    
    JMenuItem miAdd = new JMenuItem("Add");
    popupMenuJTree.add(miAdd);
    JMenuItem miEdit = new JMenuItem("Edit");
    popupMenuJTree.add(miEdit);
    JMenuItem miDelete = new JMenuItem("Delete");
    popupMenuJTree.add(miDelete);
    miExport.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        treeToPDF();
      }
    });
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
              for (VS_STAGE_GROUPS user : mainForm.activeGroup.users) {
                user.IS_FINISHED = 1;
                user.recalculateLapTimes(mainForm.con, stage, true);
              }
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
  
  public BaseFont getRussianFont() throws DocumentException, IOException{
    //return BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);
    String path = (new File("")).getAbsolutePath();
    String encode = VS_SETTING.getParam(mainForm.con, "PDF-encode", "CP1251");
    BaseFont bfComic = BaseFont.createFont( path +"\\font.ttf", encode, BaseFont.EMBEDDED);
    return bfComic;
  }

  public void treeToPDF(){
    try {
      File dir = new File("reports");
      dir.mkdirs();
      JDEDate jd = new JDEDate();
      String fileName = dir.getAbsolutePath()+"\\"+jd.getDateAsYYYYMMDD_andTime("-", "_")+".pdf";
      
      BaseFont bf = getRussianFont();
      Font font = new Font(bf);

      try {
        int rowCount = jTable.getRowCount();
        //Document document = new Document();
        Document document = new Document(/*PageSize.A4.rotate()*/);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        
        Font font1 = new Font(bf, 16);
        Paragraph caption = new Paragraph(stage.CAPTION, font1);
        caption.getFont().setStyle(Font.BOLD);
        document.add(caption);

        //text1 = "Test";
        document.add(new Paragraph(" ", font));
        
        for (Integer groupNum : stage.groups.keySet()){
          VS_STAGE_GROUP group = stage.groups.get(groupNum);
          Paragraph grpup_pr = new Paragraph("Group"+group.GROUP_NUM, font);
          grpup_pr.getFont().setStyle(Font.BOLD);
          document.add(grpup_pr);
          for (VS_STAGE_GROUPS usr : group.users){
            Paragraph user_pr = new Paragraph(usr.PILOT + "\tcahnnel: "+usr.CHANNEL, font);
            user_pr.getFont().setStyle(Font.NORMAL);
            document.add(user_pr);
          }
          document.add(new Paragraph(" ", font));
        }

        document.close();

        try //try statement 
        {
          Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + fileName +"\"");   //open the file chart.pdf 

        } catch (Exception e) //catch any exceptions here 
        {
          System.out.println("Error" + e);  //print the error 
        }

      } catch (Exception e) {
      }
    } catch (Exception e) {
    }
  }

  public void tableToPDF() {
    try {
      File dir = new File("reports");
      dir.mkdirs();
      JDEDate jd = new JDEDate();
      String fileName = dir.getAbsolutePath()+"\\"+jd.getDateAsYYYYMMDD_andTime("-", "_")+".pdf";
      
      BaseFont bf = getRussianFont();
      Font font = new Font(bf); 
      Font fontBold = new Font(bf); 
      fontBold.setStyle(Font.BOLD);
      Font fontCption = new Font(bf,16);      
      fontCption.setStyle(Font.BOLD);
 
      try {
        int rowCount = jTable.getRowCount();
        //Document document = new Document();
        Document document = new Document(/*PageSize.A4.rotate()*/);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        int colCount = jTable.getColumnCount();
        PdfPTable tab = new PdfPTable(jTable.getColumnCount());        

        int[] widths = new int[colCount];
        int max_width = 0;
        for (int i = 0; i < jTable.getColumnCount(); i++) {
          Paragraph paragraph=new Paragraph(jTable.getColumnName(i),fontBold);                     
          tab.addCell(paragraph);         
          widths[i] = stageTableAdapter.getMinWidth(i);
          max_width += widths[i];
        }
        tab.setWidths(widths);
        tab.setWidthPercentage(100);
       
        for (int row = 0; row < rowCount; row++) {
          for (int col = 0; col < colCount; col++) {
            Object obj = jTable.getModel().getValueAt(row, col);
            Paragraph paragraph=new Paragraph(obj.toString(),font); 
            paragraph.getFont().setStyle(Font.NORMAL);
            tab.addCell(paragraph);
          }
        }
        
        Paragraph caption = new Paragraph("���� : " + stage.CAPTION, fontCption);        
        document.add(caption);

        //text1 = "Test";
        document.add(new Paragraph(" ", font));

        tab.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        document.add(tab);
        document.close();

        try //try statement 
        {
          Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + fileName +"\"");   //open the file chart.pdf 

        } catch (Exception e) //catch any exceptions here 
        {
          System.out.println("Error" + e);  //print the error 
        }

      } catch (Exception e) {
      }
    } catch (Exception e) {
    }
  }

  public void createGroups() {
    try {
      if (stage != null && stage.ID != -1) {
        VS_STAGE parent_stage = null;
        try {
          parent_stage = VS_STAGE.dbControl.getItem(mainForm.con, "CAPTION=?", stage.PARENT_STAGE);
        } catch (Exception e) {
        }

        VS_STAGE_GROUPS.dbControl.delete(mainForm.con, "STAGE_ID=?", stage.ID);

        if (parent_stage != null) {
          List<VS_STAGE_GROUPS> groups = null;//VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GID", parent_stage.ID);
          // Copy grups to new Stage
          Map<String, Map<String, Map<String, VS_RACE_LAP>>> laps = VS_RACE_LAP.dbControl.getMap3(mainForm.con, "GROUP_NUM", "TRANSPONDER_ID", "LAP", "RACE_ID=? and STAGE_ID=?", stage.RACE_ID, parent_stage.ID);
          for (VS_STAGE_GROUPS usr : groups) {
            usr.IS_FINISHED = 1;
            usr.recalculateLapTimes(mainForm.con, stage, true);
          }
          if (stage.STAGE_TYPE == MainForm.STAGE_RACE) {
            //if (parent_stage.STAGE_TYPE == MainForm.STAGE_QUALIFICATION) {
            if (1 == 1) {
              // based on best time
              groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by RACE_TIME, BEST_LAP", parent_stage.ID);
              checkGroupConstrain();
              Map<String, VS_REGISTRATION> users = VS_REGISTRATION.dbControl.getMap(mainForm.con, "VS_TRANSPONDER", "VS_RACE_ID=? ORDER BY PILOT_TYPE,NUM", stage.RACE_ID);
              TreeSet<String> user_names = new TreeSet();
              for (VS_STAGE_GROUPS usr : groups) {
                if (user_names.contains(usr.PILOT)) {
                  usr.isError = 2;
                }
                user_names.add(usr.PILOT);
              }
              List<VS_STAGE_GROUPS> inactives = new ArrayList<VS_STAGE_GROUPS>();
              for (VS_STAGE_GROUPS usr : groups) {
                VS_REGISTRATION reg = users.get("" + usr.TRANSPONDER);
                if (reg != null) {
                  usr.PILOT = reg.VS_USER_NAME;
                  if (reg.IS_ACTIVE == 0 || usr.isError == 2) {
                    inactives.add(usr);
                  }
                }
              }
              for (VS_STAGE_GROUPS del : inactives) {
                groups.remove(del);
              }
              int count_man = groups.size();
              int count_max_in_groups = stage.COUNT_PILOTS_IN_GROUP;
              int count_man_in_group = 1;
              int count_groups = count_man / count_max_in_groups;
              if (count_groups * count_max_in_groups < count_man) {
                count_groups++;
              }
              int current_group = 1;
              String[] channels = stage.CHANNELS.split(";");
              HashMap<Integer, HashMap<String, Integer>> usingChannels = new HashMap<Integer, HashMap<String, Integer>>();
              for (VS_STAGE_GROUPS usr : groups) {
                HashMap<String, Integer> groupChannels = usingChannels.get(current_group);
                if (groupChannels == null) {
                  groupChannels = new HashMap<String, Integer>();
                  usingChannels.put(current_group, groupChannels);
                }
                usr.GID = -1;
                //usr.CHANNEL = channels[count_man_in_group - 1];
                usr.STAGE_ID = stage.ID;
                usr.IS_FINISHED = 0;
                usr.IS_RECALULATED = 0;
                usr.NUM_IN_GROUP = count_man_in_group;
                usr.GROUP_NUM = current_group;
                usr.BEST_LAP = 0;
                usr.LAPS = 0;
                usr.RACE_TIME = 0;
                Integer countUse = groupChannels.get(usr.CHANNEL);
                if (countUse == null) {
                  countUse = 0;
                }
                countUse++;
                groupChannels.put(usr.CHANNEL, countUse);
                current_group++;
                if (current_group > count_groups) {
                  current_group = 1;
                  count_man_in_group++;
                }
              }
              HashMap<Integer, HashMap<String, Integer>> checkChannelsAll = new HashMap<Integer, HashMap<String, Integer>>();
              for (VS_STAGE_GROUPS usr : groups) {
                HashMap<String, Integer> checkChannels = checkChannelsAll.get((int) usr.GROUP_NUM);
                if (checkChannels == null) {
                  checkChannels = new HashMap<String, Integer>();
                  checkChannelsAll.put((int) usr.GROUP_NUM, checkChannels);
                }
                HashMap<String, Integer> groupChannels = usingChannels.get((int) usr.GROUP_NUM);
                Integer countUse = groupChannels.get(usr.CHANNEL);
                if (countUse > 1) {
                  if (checkChannels.get(usr.CHANNEL) == null) {
                    checkChannels.put(usr.CHANNEL, 1);
                  } else {
                    for (String channel : channels) {
                      if (groupChannels.get(channel) == null) {
                        usr.CHANNEL = channel;
                        groupChannels.put(usr.CHANNEL, 1);
                      }
                    }
                  }
                }
                VS_STAGE_GROUPS.dbControl.insert(mainForm.con, usr);
              }
            } else {
              // Double         

            }
          } else {
            groups = VS_STAGE_GROUPS.dbControl.getList(mainForm.con, "STAGE_ID=? order by GID", parent_stage.ID);
            // usual copy
            for (VS_STAGE_GROUPS grp : groups) {
              grp.GID = -1;
              grp.STAGE_ID = stage.ID;
              grp.isError = 0;
              grp.IS_FINISHED = 0;
              grp.IS_RECALULATED = 0;
              VS_STAGE_GROUPS.dbControl.insert(mainForm.con, grp);
            }
          }
        } else {
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
    bNewStage = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jSplitPane2 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTree = new ExpandedJTree();

    addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        formKeyPressed(evt);
      }
    });

    topPanel.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        topPanelKeyPressed(evt);
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

    bNewStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
    bNewStage.setText("New Stage");
    bNewStage.setToolTipText("Add a new stage tab");
    bNewStage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bNewStageActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
    topPanel.setLayout(topPanelLayout);
    topPanelLayout.setHorizontalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(timerCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
        .addComponent(refreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pdfButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butConfig)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(bNewStage)
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
            .addComponent(butRemoveSatge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bNewStage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(butConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pdfButton))
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
    jTable.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTableKeyPressed(evt);
      }
    });
    jScrollPane1.setViewportView(jTable);

    jSplitPane2.setRightComponent(jScrollPane1);

    jTree.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
    jTree.setDragEnabled(true);
    jTree.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTreeKeyPressed(evt);
      }
    });
    jScrollPane2.setViewportView(jTree);

    jSplitPane2.setLeftComponent(jScrollPane2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jSplitPane2))
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
    tableToPDF();
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

  private void keyPressed(java.awt.event.KeyEvent evt) {
    if (raceTimer.isRunning()) {
      //JOptionPane.showMessageDialog(mainForm, "key:"+evt.getKeyChar(), "Information", JOptionPane.INFORMATION_MESSAGE);              
      if (evt.getKeyChar() >= '1' && evt.getKeyChar() <= '8') {
        try {
          int user_index = evt.getKeyChar() - '0' - 1;
          long time = Calendar.getInstance().getTimeInMillis();
          VS_STAGE_GROUPS usr = mainForm.activeGroup.users.get(user_index);
          stage.addLapFromKeyPress(mainForm, usr, time);
          jTable.updateUI();
        } catch (Exception e) {
        }
      }
    }
  }

  private void bNewStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewStageActionPerformed
    // TODO add your handling code here:
    StageNewForm.init(mainForm, null).setVisible(true);
  }//GEN-LAST:event_bNewStageActionPerformed

  private void jTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);

  }//GEN-LAST:event_jTableKeyPressed

  private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    keyPressed(evt);
    // TODO add your handling code here:
  }//GEN-LAST:event_formKeyPressed

  private void jTreeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTreeKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);
  }//GEN-LAST:event_jTreeKeyPressed

  private void topPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topPanelKeyPressed
    // TODO add your handling code here:
    keyPressed(evt);
  }//GEN-LAST:event_topPanelKeyPressed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bNewStage;
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
