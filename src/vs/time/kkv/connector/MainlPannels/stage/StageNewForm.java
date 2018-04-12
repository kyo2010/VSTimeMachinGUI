/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels.stage;

import vs.time.kkv.connector.Race.*;
import vs.time.kkv.connector.Users.*;
import KKV.DBControlSqlLite.DBModelTest;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_RACE;
import vs.time.kkv.models.VS_SETTING;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class StageNewForm extends javax.swing.JFrame {

  MainForm mainForm = null;
  int tabID = -1;
  VS_STAGE stage = null;
  //VS_RACE race = null;

  /**
   * Creates new form UserControlForm
   */
  private StageNewForm(MainForm mainForm) {
    this.mainForm = mainForm;
    initComponents();
    channelControls.add(new ChannelControl(1, jlChannel1, jcbChannel1, jcbColor1));
    channelControls.add(new ChannelControl(2, jlChannel2, jcbChannel2, jcbColor2));
    channelControls.add(new ChannelControl(3, jlChannel3, jcbChannel3, jcbColor3));
    channelControls.add(new ChannelControl(4, jlChannel4, jcbChannel4, jcbColor4));
    channelControls.add(new ChannelControl(5, jlChannel5, jcbChannel5, jcbColor5));
    channelControls.add(new ChannelControl(6, jlChannel6, jcbChannel6, jcbColor6));
    channelControls.add(new ChannelControl(7, jlChannel7, jcbChannel7, jcbColor7));
    channelControls.add(new ChannelControl(8, jlChannel8, jcbChannel8, jcbColor8));       
  }

  private static StageNewForm form = null;

  public static StageNewForm init(MainForm mainForm, VS_STAGE stage) {
    if (form == null) {
      form = new StageNewForm(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }
    form.setVisible(false);
    form.stage = stage;
    form.prepareForm();
    form.setTitle("New Stage");
    if (stage!=null){
      form.setTitle("Edit Stage "+stage.CAPTION+" ["+stage.ID+"] "+stage.RACE_ID);
    }            
   
    return form;
  }

  class ChannelControl {

    int index;
    JLabel label;
    JComboBox box;
    JComboBox color;

    public ChannelControl(int index, JLabel label, JComboBox box, JComboBox color) {
      this.index = index;
      this.label = label;
      this.box = box;
      this.color = color;
    }
  }
  List<ChannelControl> channelControls = new ArrayList<ChannelControl>();

  public void prepareForm() {

    jPilotType.setModel(new javax.swing.DefaultComboBoxModel(MainForm.PILOT_TYPES_NONE));
    List<VS_STAGE> stages = null;
    try {
      stages = VS_STAGE.dbControl.getList(mainForm.con, "RACE_ID=? order by ID", mainForm.activeRace.RACE_ID);
    } catch (Exception e) {
    }
    if (stages == null) {
      stages = new ArrayList<VS_STAGE>();
    }
    String[] stages_st = new String[stages.size() + 1];
    String last_stage = "";
    for (int i = 0; i < stages_st.length; i++) {
      if (i == 0) {
        stages_st[i] = "auto";
      } else {
        stages_st[i] = stages.get(i - 1).CAPTION;
        last_stage = stages_st[i];
      }
    }
    parentStage.setModel(new javax.swing.DefaultComboBoxModel(stages_st));

    if (stage != null) {
      butRecrateGropus.setVisible(true);
      jtCaption.setText(stage.CAPTION);
      jchGroupByPilotType.setSelected(stage.FLAG_BY_PYLOT_TYPE == 1);
      jtLapsCount.setText("" + stage.LAPS);
      jtMinLapTime.setText("" + stage.MIN_LAP_TIME);
      jtCountOfPilots.setSelectedIndex(stage.COUNT_PILOTS_IN_GROUP - 1);      
      String[] channels = stage.CHANNELS.split(";");
      String[] colors = stage.COLORS.split(";");
      int index = 0;
      for (String channel : channels) {
        channelControls.get(index).box.setSelectedItem(channel);
        String color = "WHITE";
        try{
          color = colors[index];
        }catch(Exception e){}
        channelControls.get(index).color.setSelectedItem(color);
        index++;
      }
      jcbStageType.setSelectedIndex(stage.STAGE_TYPE);
      try {
        parentStage.setSelectedItem(stage.PARENT_STAGE);
      } catch (Exception e) {
      }
      jPilotType.setSelectedIndex(stage.PILOT_TYPE);      
      jOrderBy.setSelectedIndex(stage.SORT_TYPE);
      PilotsForNextRound.setText(""+stage.PILOTS_FOR_NEXT_ROUND);
      jRaceType.setSelectedIndex(stage.RACE_TYPE);
    } else {
      VS_RACE race = mainForm.activeRace;
      //jtLapsCount.setText("" + 3);
      jtLapsCount.setText(""+race.COUNT_OF_LAPS);      
      jtMinLapTime.setText(""+race.MIN_LAP_TIME);
      parentStage.setSelectedItem(last_stage);
      jPilotType.setSelectedIndex(MainForm.PILOT_TYPE_NONE_INDEX);
      butRecrateGropus.setVisible(false);
      String st_channels = VS_SETTING.getParam(mainForm.con, "CHANNELS", "R2;R5;R7");
      String st_colors = VS_SETTING.getParam(mainForm.con, "COLORS", "RED;BLUE;GREEN");      
      String[] channels = st_channels.split(";");
      String[] colors = st_colors.split(";");
      int index = 0;
      for (String channel : channels) {
        channelControls.get(index).box.setSelectedItem(channel);
        String color = "WHITE";
        try{
          color = colors[index];
        }catch(Exception e){}
        channelControls.get(index).color.setSelectedItem(color);
        index++;
      }
      //jtCountOfPilots.setSelectedIndex(channels.length - 1);      
    }
    
    jtCountOfPilotsPropertyChange(null);
    jcbStageTypeActionPerformed(null);        
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
    Caption = new javax.swing.JLabel();
    jtCaption = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jcbStageType = new javax.swing.JComboBox();
    racePanel = new javax.swing.JPanel();
    jRaceTypeLabel = new javax.swing.JLabel();
    jRaceType = new javax.swing.JComboBox<>();
    jLabel5 = new javax.swing.JLabel();
    parentStage = new javax.swing.JComboBox<>();
    tabPane = new javax.swing.JTabbedPane();
    tabRace = new javax.swing.JPanel();
    LapsCaption = new javax.swing.JLabel();
    jtLapsCount = new javax.swing.JTextField();
    jchGroupByPilotType = new javax.swing.JCheckBox();
    jLabel3 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jtCountOfPilots = new javax.swing.JComboBox();
    jtMinLapTime = new javax.swing.JTextField();
    stagePanel = new javax.swing.JPanel();
    jlChannel1 = new javax.swing.JLabel();
    jcbChannel1 = new javax.swing.JComboBox();
    jlChannel5 = new javax.swing.JLabel();
    jcbChannel5 = new javax.swing.JComboBox();
    jcbChannel6 = new javax.swing.JComboBox();
    jlChannel6 = new javax.swing.JLabel();
    jcbChannel2 = new javax.swing.JComboBox();
    jlChannel2 = new javax.swing.JLabel();
    jlChannel3 = new javax.swing.JLabel();
    jcbChannel3 = new javax.swing.JComboBox();
    jlChannel7 = new javax.swing.JLabel();
    jcbChannel7 = new javax.swing.JComboBox();
    jcbChannel8 = new javax.swing.JComboBox();
    jlChannel8 = new javax.swing.JLabel();
    jcbChannel4 = new javax.swing.JComboBox();
    jlChannel4 = new javax.swing.JLabel();
    jcbColor1 = new javax.swing.JComboBox<>();
    jcbColor2 = new javax.swing.JComboBox<>();
    jcbColor3 = new javax.swing.JComboBox<>();
    jcbColor4 = new javax.swing.JComboBox<>();
    jcbColor5 = new javax.swing.JComboBox<>();
    jcbColor6 = new javax.swing.JComboBox<>();
    jcbColor7 = new javax.swing.JComboBox<>();
    jcbColor8 = new javax.swing.JComboBox<>();
    panelQualificationResult = new javax.swing.JPanel();
    panelQualificationResult2 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jPilotType = new javax.swing.JComboBox<>();
    jLabel6 = new javax.swing.JLabel();
    jOrderBy = new javax.swing.JComboBox<>();
    jLabel7 = new javax.swing.JLabel();
    PilotsForNextRound = new javax.swing.JTextField();
    tabRaceReport = new javax.swing.JPanel();
    pColumns = new javax.swing.JPanel();
    jLabel8 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    bSave = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();
    butRecrateGropus = new javax.swing.JButton();

    setTitle("Add Stage");
    setResizable(false);

    Caption.setText("Stage name:");

    jLabel4.setText("Stage type:");

    jcbStageType.setModel(new javax.swing.DefaultComboBoxModel(MainForm.STAGE_TYPES));
    jcbStageType.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jcbStageTypeActionPerformed(evt);
      }
    });
    jcbStageType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        jcbStageTypePropertyChange(evt);
      }
    });

    jRaceTypeLabel.setText("Race Type");

    jRaceType.setModel(new javax.swing.DefaultComboBoxModel(MainForm.RACE_TYPES));

    javax.swing.GroupLayout racePanelLayout = new javax.swing.GroupLayout(racePanel);
    racePanel.setLayout(racePanelLayout);
    racePanelLayout.setHorizontalGroup(
      racePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(racePanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jRaceTypeLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jRaceType, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    racePanelLayout.setVerticalGroup(
      racePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(racePanelLayout.createSequentialGroup()
        .addGroup(racePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jRaceTypeLabel)
          .addComponent(jRaceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(0, 7, Short.MAX_VALUE))
    );

    jLabel5.setText("Parent Stage");

    parentStage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    LapsCaption.setText("Laps:");

    jchGroupByPilotType.setText("Create groups by pilot type");
    jchGroupByPilotType.setAlignmentY(0.0F);
    jchGroupByPilotType.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    jchGroupByPilotType.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jchGroupByPilotType.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jchGroupByPilotTypeActionPerformed(evt);
      }
    });

    jLabel3.setText("Min lap time (sec):");

    jLabel1.setText("Count of pilots in group:");

    jtCountOfPilots.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
    jtCountOfPilots.setSelectedIndex(2);
    jtCountOfPilots.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        jtCountOfPilotsPropertyChange(evt);
      }
    });

    jtMinLapTime.setText("18");

    jlChannel1.setText("Channel 1:");

    jcbChannel1.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel5.setText("Channel 5:");

    jcbChannel5.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jcbChannel6.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel6.setText("Channel 6:");

    jcbChannel2.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel2.setText("Channel 2:");

    jlChannel3.setText("Channel 3:");

    jcbChannel3.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel7.setText("Channel 7:");

    jcbChannel7.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jcbChannel8.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel8.setText("Channel 8:");

    jcbChannel4.setModel(new javax.swing.DefaultComboBoxModel(mainForm.getBands()));

    jlChannel4.setText("Channel 4:");

    jcbColor1.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor2.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor3.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor4.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor5.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor6.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor7.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    jcbColor8.setModel(new javax.swing.DefaultComboBoxModel(VSColor.getColors()));

    javax.swing.GroupLayout stagePanelLayout = new javax.swing.GroupLayout(stagePanel);
    stagePanel.setLayout(stagePanelLayout);
    stagePanelLayout.setHorizontalGroup(
      stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(stagePanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jlChannel1)
          .addComponent(jlChannel2)
          .addComponent(jlChannel3)
          .addComponent(jlChannel4))
        .addGap(20, 20, 20)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jcbChannel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jcbChannel3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jcbChannel4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcbColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jcbColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jcbColor3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jcbColor4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(stagePanelLayout.createSequentialGroup()
            .addComponent(jlChannel5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jcbChannel5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(stagePanelLayout.createSequentialGroup()
            .addComponent(jlChannel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jcbChannel6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(stagePanelLayout.createSequentialGroup()
            .addComponent(jlChannel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jcbChannel7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(stagePanelLayout.createSequentialGroup()
            .addComponent(jlChannel8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jcbChannel8, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jcbColor5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    stagePanelLayout.setVerticalGroup(
      stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, stagePanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel1)
          .addComponent(jcbChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jlChannel5)
          .addComponent(jcbChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel2)
          .addComponent(jcbChannel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jlChannel6)
          .addComponent(jcbChannel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel3)
          .addComponent(jcbChannel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jlChannel7)
          .addComponent(jcbChannel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(stagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jlChannel4)
          .addComponent(jcbChannel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jlChannel8)
          .addComponent(jcbChannel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jcbColor8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout tabRaceLayout = new javax.swing.GroupLayout(tabRace);
    tabRace.setLayout(tabRaceLayout);
    tabRaceLayout.setHorizontalGroup(
      tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabRaceLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jchGroupByPilotType, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
          .addGroup(tabRaceLayout.createSequentialGroup()
            .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(tabRaceLayout.createSequentialGroup()
                .addComponent(LapsCaption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtLapsCount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(tabRaceLayout.createSequentialGroup()
                .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel3)
                  .addComponent(jLabel1))
                .addGap(20, 20, 20)
                .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jtCountOfPilots, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jtMinLapTime, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
      .addComponent(stagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    tabRaceLayout.setVerticalGroup(
      tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabRaceLayout.createSequentialGroup()
        .addGap(5, 5, 5)
        .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(LapsCaption)
          .addComponent(jtLapsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jchGroupByPilotType)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jtMinLapTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(tabRaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jtCountOfPilots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(stagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabPane.addTab("Race and Groups", tabRace);

    jLabel2.setText("Pilot Type");

    jPilotType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jLabel6.setText("Order by");

    jOrderBy.setModel(new javax.swing.DefaultComboBoxModel(MainForm.STAGE_SORTS));

    jLabel7.setText("Pilots for next round");

    PilotsForNextRound.setText("4");

    javax.swing.GroupLayout panelQualificationResult2Layout = new javax.swing.GroupLayout(panelQualificationResult2);
    panelQualificationResult2.setLayout(panelQualificationResult2Layout);
    panelQualificationResult2Layout.setHorizontalGroup(
      panelQualificationResult2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelQualificationResult2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelQualificationResult2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(panelQualificationResult2Layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addComponent(jPilotType, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(panelQualificationResult2Layout.createSequentialGroup()
            .addComponent(jLabel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PilotsForNextRound, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(53, 53, 53)
        .addComponent(jLabel6)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panelQualificationResult2Layout.setVerticalGroup(
      panelQualificationResult2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelQualificationResult2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelQualificationResult2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(jPilotType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6)
          .addComponent(jOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelQualificationResult2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(PilotsForNextRound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout panelQualificationResultLayout = new javax.swing.GroupLayout(panelQualificationResult);
    panelQualificationResult.setLayout(panelQualificationResultLayout);
    panelQualificationResultLayout.setHorizontalGroup(
      panelQualificationResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelQualificationResultLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panelQualificationResult2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    panelQualificationResultLayout.setVerticalGroup(
      panelQualificationResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelQualificationResultLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(panelQualificationResult2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(315, Short.MAX_VALUE))
    );

    tabPane.addTab("Result Parameters", panelQualificationResult);

    javax.swing.GroupLayout pColumnsLayout = new javax.swing.GroupLayout(pColumns);
    pColumns.setLayout(pColumnsLayout);
    pColumnsLayout.setHorizontalGroup(
      pColumnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    pColumnsLayout.setVerticalGroup(
      pColumnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 355, Short.MAX_VALUE)
    );

    jLabel8.setText("Show the following columns:");

    javax.swing.GroupLayout tabRaceReportLayout = new javax.swing.GroupLayout(tabRaceReport);
    tabRaceReport.setLayout(tabRaceReportLayout);
    tabRaceReportLayout.setHorizontalGroup(
      tabRaceReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabRaceReportLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabRaceReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(pColumns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(tabRaceReportLayout.createSequentialGroup()
            .addComponent(jLabel8)
            .addGap(0, 396, Short.MAX_VALUE)))
        .addContainerGap())
    );
    tabRaceReportLayout.setVerticalGroup(
      tabRaceReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabRaceReportLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel8)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pColumns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    jLabel8.getAccessibleContext().setAccessibleName("");

    tabPane.addTab("Columns", tabRaceReport);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(racePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jcbStageType, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(Caption)
              .addComponent(jLabel5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jtCaption)
              .addComponent(parentStage, 0, 517, Short.MAX_VALUE)))
          .addComponent(tabPane))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jcbStageType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(racePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(Caption)
          .addComponent(jtCaption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(parentStage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tabPane))
    );

    bSave.setText("Save");
    bSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bSaveActionPerformed(evt);
      }
    });

    bCancel.setText("Cancel");
    bCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bCancelActionPerformed(evt);
      }
    });

    butRecrateGropus.setText("Save & Recreate Groups");
    butRecrateGropus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butRecrateGropusActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(bSave)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butRecrateGropus)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(bCancel)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bSave)
          .addComponent(bCancel)
          .addComponent(butRecrateGropus))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_bCancelActionPerformed

  private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
    try {
      boolean isNewSatge = false;
      VS_STAGE parent_stage = null;
      if (stage == null) {
        stage = new VS_STAGE();
        stage.IS_GROUP_CREATED = 0;
        stage.RACE_ID = mainForm.activeRace.RACE_ID;
        stage.STAGE_NUM = 1;
        isNewSatge = true;
        try {
          int max = (int) VS_STAGE.dbControl.getMax(mainForm.con, "STAGE_NUM", "RACE_ID=?", stage.RACE_ID);
          stage.STAGE_NUM = max + 1;
        } catch (Exception e) {
        }
      }
      stage.PARENT_STAGE = parentStage.getSelectedItem().toString();      
      try {
        parent_stage = VS_STAGE.dbControl.getItem(mainForm.con, "CAPTION=? and RACE_ID=?", stage.PARENT_STAGE, stage.RACE_ID);
      } catch (Exception e) {
      }
      if (parent_stage!=null){
        stage.PARENT_STAGE_ID = parent_stage.ID;
      }
      
      stage.SORT_TYPE = jOrderBy.getSelectedIndex();
      stage.CAPTION = jtCaption.getText();
      stage.FLAG_BY_PYLOT_TYPE = jchGroupByPilotType.isSelected() ? 1 : 0;

      try {
        stage.LAPS = Integer.parseInt(jtLapsCount.getText());
      } catch (Exception e) {
      }
      try {
        stage.MIN_LAP_TIME = Integer.parseInt(jtMinLapTime.getText());
      } catch (Exception e) {
      }
      int count_of_pilots = jtCountOfPilots.getSelectedIndex() + 1;
      stage.COUNT_PILOTS_IN_GROUP = count_of_pilots;
      stage.STAGE_TYPE = jcbStageType.getSelectedIndex();
      stage.RACE_TYPE = jRaceType.getSelectedIndex();
      
      try {
        stage.PILOTS_FOR_NEXT_ROUND = Integer.parseInt(PilotsForNextRound.getText());
      } catch (Exception e) {
      }      
      
      stage.PILOT_TYPE = jPilotType.getSelectedIndex();

      String channels = "";
      String colors = "";
      for (int index = 0; index < count_of_pilots; index++) {
        channels += channelControls.get(index).box.getSelectedItem().toString() + ";";
        colors += channelControls.get(index).color.getSelectedItem().toString() + ";";
      }
      stage.CHANNELS = channels;
      stage.COLORS = colors;
      stage.resetSelectedTab(mainForm.con, stage.RACE_ID);
      stage.IS_SELECTED = 1;
      
      String colsInfo = "";
      if (linkingColumnAndChechkBox!=null){
        for (LinkingColumnAndChechkBox colLink : linkingColumnAndChechkBox){
          if (colLink.check.isSelected()){
            colsInfo += "1;";
          }else{
            colsInfo += "0;";
          } 
        }
      }
      stage.REP_COLS = colsInfo;
      
      if (stage.ID == -1) {

        if (stage.STAGE_TYPE == MainForm.STAGE_RACE){
         if (parent_stage!=null) {
            stage.PILOT_TYPE = parent_stage.PILOT_TYPE;
            stage.SORT_TYPE = MainForm.STAGE_SORT_BY_RACE_TIME;
          }
        } 
        
        if (isNewSatge && stage.STAGE_TYPE == MainForm.STAGE_RACE && 
                ( stage.RACE_TYPE == MainForm.RACE_TYPE_WHOOP ||
                  stage.RACE_TYPE == MainForm.RACE_TYPE_DOUBLE
                )) {         
          stage.SORT_TYPE = MainForm.STAGE_SORT_BY_SCORE_DESC;
          stage.ID = -1;
          String caption = stage.CAPTION;
          stage.CAPTION = caption+"_1";
          stage.dbControl.insert(mainForm.con, stage);
          stage.ID = -1;
          stage.CAPTION = caption+"_2";
          stage.dbControl.insert(mainForm.con, stage);
          stage.ID = -1;
          stage.CAPTION = caption+"_3";
          stage.dbControl.insert(mainForm.con, stage);          
          stage.CAPTION = caption+" result";
          stage.ID = -1;
          stage.STAGE_TYPE = MainForm.STAGE_RACE_RESULT;
          stage.SORT_TYPE = MainForm.STAGE_SORT_BY_LOSS_DESC;
          stage.dbControl.insert(mainForm.con, stage);
        } else {
          stage.dbControl.insert(mainForm.con, stage);
        }
      } else {
        stage.dbControl.update(mainForm.con, stage);
      }
      setVisible(false);

      mainForm.setActiveRace(mainForm.activeRace);
      VS_SETTING.setParam(mainForm.con, "CHANNELS", channels);
      VS_SETTING.setParam(mainForm.con, "COLORS", colors);

    } catch (Exception e) {
      mainForm.error_log.writeFile(e);
      JOptionPane.showMessageDialog(this, "Saving race is error. " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
        
  }//GEN-LAST:event_bSaveActionPerformed

  private void jchGroupByPilotTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchGroupByPilotTypeActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_jchGroupByPilotTypeActionPerformed

  private void jcbStageTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jcbStageTypePropertyChange
    // TODO add your handling code here:
    if (stage == null) {
      String text = jcbStageType.getSelectedItem().toString();
      int max = 1;
      try {
        max = (int) VS_STAGE.dbControl.getMax(mainForm.con, "STAGE_NUM", "RACE_ID=?", mainForm.activeRace.RACE_ID) + 1;
      } catch (Exception e) {
      }
      jtCaption.setText(text + max);
    }
  }//GEN-LAST:event_jcbStageTypePropertyChange

  private void jtCountOfPilotsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtCountOfPilotsPropertyChange
    // TODO add your handling code here:    
    int count_of_pilots = jtCountOfPilots.getSelectedIndex() + 1;
    for (ChannelControl chc : channelControls) {
      if (chc.index <= count_of_pilots) {
        chc.label.setVisible(true);
        chc.box.setVisible(true);
        chc.color.setVisible(true);
      } else {
        chc.label.setVisible(false);
        chc.box.setVisible(false);
        chc.color.setVisible(false);
      }
    }
  }//GEN-LAST:event_jtCountOfPilotsPropertyChange
  
  List<LinkingColumnAndChechkBox> linkingColumnAndChechkBox = new ArrayList<>();
  class LinkingColumnAndChechkBox{
    int index;
    STAGE_COLUMN col;
    JCheckBox check;    

    public LinkingColumnAndChechkBox(int index, STAGE_COLUMN col, JCheckBox check) {
      this.index = index;
      this.col = col;
      this.check = check;
    }
    
  }
  
  private void jcbStageTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbStageTypeActionPerformed
    // TODO add your handling code here:
    //tabPane.setSelectedIndex(0);    
    //tabRaceReport.setVisible(false);
    //tabPane.setEnabledAt(2, false);
    
        // Create Report Check boxes  
    pColumns.removeAll();   
    pColumns.setLayout(/*new FlowLayout()*/new BoxLayout(pColumns, BoxLayout.PAGE_AXIS) );   
    STAGE_COLUMN[] columns = StageTableAdapter.getDeafultColumns(jcbStageType.getSelectedIndex());
    linkingColumnAndChechkBox.clear();
    String[] colsInfo = stage==null?null:stage.REP_COLS.split(";");
    int index = 0;
    for ( STAGE_COLUMN col : columns) {      
      String cpation = col.caption.replaceAll("\\n", " ");
      JCheckBox check = new JCheckBox(cpation);      
      check.setSelected(true);
      if (colsInfo!=null && index<colsInfo.length){
        if ("0".equals(colsInfo[index])){
          check.setSelected(false);
        }
      }
      pColumns.add(check);    
      linkingColumnAndChechkBox.add(new LinkingColumnAndChechkBox(index, col, check));
      index++;
    }
    
    
    panelQualificationResult.setVisible(false);
    racePanel.setVisible(false);
    stagePanel.setVisible(false);    
            
   /* if (jcbStageType.getSelectedIndex() == MainForm.STAGE_RACE) {
      racePanel.setVisible(true);
    } else {
      racePanel.setVisible(false);
    }*/   

    if (jcbStageType.getSelectedIndex() == MainForm.STAGE_QUALIFICATION_RESULT || jcbStageType.getSelectedIndex() == MainForm.STAGE_RACE_RESULT) {
      //LapsCaption.setText("Count pilots for next round: ");
      tabPane.setSelectedIndex(1);
      panelQualificationResult.setVisible(true);
    } else if (jcbStageType.getSelectedIndex() == MainForm.STAGE_RACE_REPORT){
      //tabRaceReport.setVisible(true);
      tabPane.setEnabledAt(2, true);
      panelQualificationResult.setVisible(true);
      //tabPane.setSelectedIndex(2);
    } else {     
      racePanel.setVisible(true);
      stagePanel.setVisible(true);
      panelQualificationResult.setVisible(false);
      tabPane.setSelectedIndex(0);
    }    
    //updateUI();
    jPanel1.updateUI();
  }//GEN-LAST:event_jcbStageTypeActionPerformed

  private void butRecrateGropusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRecrateGropusActionPerformed
    // TODO add your handling code here:    
    try {
      if (stage != null && stage.ID != -1) {
        stage.IS_GROUP_CREATED = 0;
        VS_STAGE.dbControl.update(mainForm.con, stage);
        mainForm.setActiveRace(mainForm.activeRace);
        bSaveActionPerformed(evt);
        setVisible(false);
      }
    } catch (Exception e) {
      mainForm.toLog(e);
    }
  }//GEN-LAST:event_butRecrateGropusActionPerformed

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
      java.util.logging.Logger.getLogger(StageNewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(StageNewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(StageNewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(StageNewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new StageNewForm(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Caption;
  private javax.swing.JLabel LapsCaption;
  private javax.swing.JTextField PilotsForNextRound;
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSave;
  private javax.swing.JButton butRecrateGropus;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JComboBox<String> jOrderBy;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JComboBox<String> jPilotType;
  private javax.swing.JComboBox<String> jRaceType;
  private javax.swing.JLabel jRaceTypeLabel;
  private javax.swing.JComboBox jcbChannel1;
  private javax.swing.JComboBox jcbChannel2;
  private javax.swing.JComboBox jcbChannel3;
  private javax.swing.JComboBox jcbChannel4;
  private javax.swing.JComboBox jcbChannel5;
  private javax.swing.JComboBox jcbChannel6;
  private javax.swing.JComboBox jcbChannel7;
  private javax.swing.JComboBox jcbChannel8;
  private javax.swing.JComboBox<String> jcbColor1;
  private javax.swing.JComboBox<String> jcbColor2;
  private javax.swing.JComboBox<String> jcbColor3;
  private javax.swing.JComboBox<String> jcbColor4;
  private javax.swing.JComboBox<String> jcbColor5;
  private javax.swing.JComboBox<String> jcbColor6;
  private javax.swing.JComboBox<String> jcbColor7;
  private javax.swing.JComboBox<String> jcbColor8;
  private javax.swing.JComboBox jcbStageType;
  private javax.swing.JCheckBox jchGroupByPilotType;
  private javax.swing.JLabel jlChannel1;
  private javax.swing.JLabel jlChannel2;
  private javax.swing.JLabel jlChannel3;
  private javax.swing.JLabel jlChannel4;
  private javax.swing.JLabel jlChannel5;
  private javax.swing.JLabel jlChannel6;
  private javax.swing.JLabel jlChannel7;
  private javax.swing.JLabel jlChannel8;
  private javax.swing.JTextField jtCaption;
  private javax.swing.JComboBox jtCountOfPilots;
  private javax.swing.JTextField jtLapsCount;
  private javax.swing.JTextField jtMinLapTime;
  private javax.swing.JPanel pColumns;
  private javax.swing.JPanel panelQualificationResult;
  private javax.swing.JPanel panelQualificationResult2;
  private javax.swing.JComboBox<String> parentStage;
  private javax.swing.JPanel racePanel;
  private javax.swing.JPanel stagePanel;
  private javax.swing.JTabbedPane tabPane;
  private javax.swing.JPanel tabRace;
  private javax.swing.JPanel tabRaceReport;
  // End of variables declaration//GEN-END:variables
}
