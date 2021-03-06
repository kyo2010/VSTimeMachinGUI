/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Users;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.models.VS_USERS;

/**
 *
 * @author kyo
 */
public class UserList extends javax.swing.JFrame {

  public MainForm mainForm = null;
  public UserListModelTable userListModelTable = null;
  
  public interface UserListListener{
    // return true if close windows
    boolean selectUserAndCloseWindow(VS_USERS user); 
  }
  
  /**
   * Creates new form UserList
   */
  public UserList(MainForm mainForm) {
    initComponents();
    this.mainForm = mainForm;    
        
    jtUsers.addMouseListener(new MouseAdapter() {            

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==2){
          int row = jtUsers.getSelectedRow();
          if (user_listener!=null){
            boolean please_close = user_listener.selectUserAndCloseWindow(userListModelTable.getUser(row));
            if (please_close) setVisible(false);
          }else{  
            userListModelTable.showEditDialog(row);
          }
          //JOptionPane.showMessageDialog(UserList.this, "Rows:"+jtUsers.getSelectedRow(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
      
    });
    
    jtFind.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
         update();     
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
         update();     
      }
      @Override
      public void changedUpdate(DocumentEvent e) {
        update();     
      }      
      public void update(){
        userListModelTable.findString = jtFind.getText();
        refreshData();   
      }
    });
    
    
    setVisible(false);
  }
  
  public void refreshData(){
    userListModelTable.loadData();
    jtUsers.addNotify();     
  }
      
  private static UserList form = null;
  UserListListener user_listener = null;

  public static UserList init(MainForm mainForm, UserListListener user_listener) {
    if (form == null) {
      form = new UserList(mainForm);
      if (mainForm != null) {
        mainForm.setFormOnCenter(form);
      }
    }      
    form.user_listener = user_listener;
    form.userListModelTable = new UserListModelTable(mainForm,user_listener==null?true:false);
    form.jtUsers.setModel( form.userListModelTable );   
    
    form.jtUsers.getColumnModel().getColumn(0).setHeaderValue("ID");
    form.jtUsers.getColumnModel().getColumn(1).setHeaderValue("Trans");
    form.jtUsers.getColumnModel().getColumn(2).setHeaderValue("Racer");  
    form.jtUsers.getColumnModel().getColumn(2).setPreferredWidth(600);
    form.jtUsers.setRowHeight(28);
    
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
    jtFind = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    butFind = new javax.swing.JButton();
    butAddNewUser = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    butClose = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jtUsers = new javax.swing.JTable();

    setTitle("Pilot List");
    setIconImage(MainForm.getWindowsIcon().getImage());

    jLabel1.setText("Find Pilot:");

    butFind.setText("Find");
    butFind.setPreferredSize(new java.awt.Dimension(63, 23));
    butFind.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butFindActionPerformed(evt);
      }
    });

    butAddNewUser.setText("Add User");
    butAddNewUser.setPreferredSize(new java.awt.Dimension(80, 23));
    butAddNewUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        butAddNewUserActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(butFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(40, 40, 40)
        .addComponent(butAddNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(butFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(butAddNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void butCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCloseActionPerformed
    setVisible(false);
  }//GEN-LAST:event_butCloseActionPerformed

  private void butAddNewUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddNewUserActionPerformed
    UserControlForm.init(mainForm, true, -1).setVisible(true);
  }//GEN-LAST:event_butAddNewUserActionPerformed

  private void butFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butFindActionPerformed
    userListModelTable.findString = jtFind.getText();
    refreshData();
  }//GEN-LAST:event_butFindActionPerformed

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
      java.util.logging.Logger.getLogger(UserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(UserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(UserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(UserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new UserList(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton butAddNewUser;
  private javax.swing.JButton butClose;
  private javax.swing.JButton butFind;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextField jtFind;
  private javax.swing.JTable jtUsers;
  // End of variables declaration//GEN-END:variables
}
