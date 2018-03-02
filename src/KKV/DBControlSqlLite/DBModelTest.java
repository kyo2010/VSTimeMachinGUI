/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.DBFieldAdapters.JDEDateAndTime;
import KKV.DBControlSqlLite.DBModelControl;
import KKV.DBControlSqlLite.DBModelField;
import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import KKV.Utils.TempFileWrite;
import org.sqlite.JDBC;

/**
 *
 * @author kimlaev
 */
public class DBModelTest {
  
  public static final String DATABASE = "races.s3db";

  public Integer id = null;
  public String name;
  public int account;
  public double value;

  public DBModelTest() {
  }

  public DBModelTest(String NAME, int INT_VALUE, double VALUE) {
    this.name = NAME;
    this.account = INT_VALUE;
    this.value = VALUE;
  }
  
  static DBModelControl<DBModelTest> dbControl = new DBModelControl<DBModelTest>(DBModelTest.class, "PSI_BW.TEST", new DBModelField[]{
            new DBModelField("id").setFieldTarget(DBModelField.FT_AUTOINCREMENT).setDbFieldName("ID"),
            new DBModelField("name").setDbFieldName("NAME1"),
            new DBModelField("account").setDbFieldName("INT_VALUE"),
            new DBModelField("value").setDbFieldName("VALUE"),}){                                        
            };

  public static void main(String[] args) {
    Connection con = null;
    try {
      con = DBModelTest.getConnectionForTest();
            
      generateClazz(con,"VS_USERS","VS_USERS","vs.time.kkv.models");
    } catch (UserException ue) {
      System.out.println("Error : " + ue.error + " details : " + ue.details);
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      try{
        if (con!=null) con.close();
      }catch(Exception e){}  
    }
  }
  
  public static Connection getConnectionForTest() throws UserException{
    try{
  //    COM.ibm.db2.jdbc.app.DB2Driver DB2Driver = new COM.ibm.db2.jdbc.app.DB2Driver();
  //    DriverManager.registerDriver(DB2Driver);
      
      DriverManager.registerDriver(new JDBC());
      File f = new File("");
      String jdbc_connect_st = "jdbc:sqlite:"+f.getAbsolutePath()+"/"+DATABASE;
      
      Connection con = DriverManager.getConnection(jdbc_connect_st);
      return con;
    }catch(Exception e){
      throw new UserException(e.getMessage(),Tools.traceError(e));
    }  
  }
  
  public static void generateClazz(Connection con, String tableName, String className, String path) throws UserException {
    PreparedStatement stat = null;
    ResultSet rs = null;    
    //rg.sqllite.RS rs = null;  
    //org.sqlite.
    if (path.indexOf("..")==0){
    }else{
      path = path.replace('.', '\\');
      path = "src\\"+path;
    }
    ArrayList<DBModelField> fields = new ArrayList<DBModelField>();
    try{
      /*String sql = "select NAME, COLTYPE, NULLS, LENGTH, SCALE, COLNO, DEFAULT, GENERATED "+
                   "from SYSIBM.SYSCOLUMNS "+
                   "where RTRIM(TBCREATOR)||'.'||RTRIM(TBNAME)='"+tableName+"' "+
                   "order by COLNO";
      */
      String sql = "select * from "+tableName+"";
      
      boolean showJDEDateAndTime = false;
      boolean showJDEDate = false;
      boolean showBigDecimal = false;
      stat = con.prepareStatement(sql);
      rs = stat.executeQuery(); 
      ResultSetMetaData meta = rs.getMetaData();
      for (int i=1; i<=meta.getColumnCount(); i++){
        Class fieldClazz = String.class;
        int colType = meta.getColumnType(i);
        String fieldName = meta.getColumnName(i);
        /*if (colType.equalsIgnoreCase("TIMESTMP")) {fieldClazz = JDEDateAndTime.class; showJDEDateAndTime = true;}
        if (colType.equalsIgnoreCase("DATE")) {    fieldClazz = JDEDate.class; showJDEDate = true; }
        if (colType.equalsIgnoreCase("TIME"))      fieldClazz = Time.class;
        if (colType.equalsIgnoreCase("BIGINT"))    fieldClazz = long.class;
        if (colType.equalsIgnoreCase("INTEGER"))   fieldClazz = int.class;
        if (colType.equalsIgnoreCase("DECIMAL")) { fieldClazz = BigDecimal.class; showBigDecimal = true; }*/
        DBModelField field = new DBModelField(fieldName,fieldClazz);
        //field.dbFieldType = colType;
        if (rs.getMetaData().isAutoIncrement(i)) field.setAutoIncrement();
        fields.add(field);
      }
      /*while (rs.next()){
        Class fieldClazz = String.class;
        String colType = rs.getString("COLTYPE").trim();
        if (colType.equalsIgnoreCase("TIMESTMP")) {fieldClazz = JDEDateAndTime.class; showJDEDateAndTime = true;}
        if (colType.equalsIgnoreCase("DATE")) {    fieldClazz = JDEDate.class; showJDEDate = true; }
        if (colType.equalsIgnoreCase("TIME"))      fieldClazz = Time.class;
        if (colType.equalsIgnoreCase("BIGINT"))    fieldClazz = long.class;
        if (colType.equalsIgnoreCase("INTEGER"))   fieldClazz = int.class;
        if (colType.equalsIgnoreCase("DECIMAL")) { fieldClazz = BigDecimal.class; showBigDecimal = true; }
        DBModelField field = new DBModelField(rs.getString("NAME"),fieldClazz);
        field.dbFieldType = colType;
        String gen = rs.getString("GENERATED").trim();
        if (gen.equalsIgnoreCase("A")) field.setAutoIncrement();
        fields.add(field);
      }*/
      rs.close();
      stat.close();
      
      File dir = new File(path); 
      dir.mkdirs();
      TempFileWrite clazz = new TempFileWrite(path+"\\"+className+".java","\n","CP1251");
      
      //String package_name = path.replaceAll("..\\\\src\\\\", ""); 
      String package_name = path.replaceAll("src\\\\", ""); 
      
      package_name = package_name.replaceAll("\\\\", ".");
      
      clazz.delete();
      clazz.println("/*** KKV Class Generator V.0.1, This class is based on '"+tableName+"' table.");
      clazz.println(" *** The class was generated automatically.  ***/");
      clazz.println("");
      clazz.println("package "+package_name+";");
      clazz.println("");
      clazz.println("import KKV.DBControlSqlLite.*;");
      if (showJDEDate) clazz.println("import Utils.JDEDate;");
      if (showJDEDateAndTime) clazz.println("import DBClasses.DBControl.DBFieldAdapters.JDEDateAndTime;");
      if (showBigDecimal) clazz.println("import java.math.BigDecimal;");
      clazz.println("import java.sql.Time;");      
      clazz.println("");
      clazz.println("public class "+className+" {");
      clazz.println("  ");
      for (DBModelField field : fields){
        clazz.println("  public "+field.fieldType.getSimpleName()+" "+field.name.replace(' ', '_')+";   //  "+field.dbFieldType);
      } 
      clazz.println("  ");
      clazz.println("  /** Constructor */ ");
      clazz.println("  public "+className+"() {");
      clazz.println("  };");
      clazz.println("  ");
      clazz.println("  public static DBModelControl<"+className+"> dbControl = new DBModelControl<"+className+">("+className+".class, \""+tableName+"\", new DBModelField[]{");
      for (DBModelField field : fields){
        String addon = "";
        if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT){
          addon = ".setAutoIncrement()";
        }
        clazz.println("    new DBModelField(\""+field.name.replace(' ', '_')+"\").setDbFieldName(\"\\\""+field.name+"\\\"\")"+addon+",");
      }  
      clazz.println("  });");
      clazz.println("  ");
      clazz.println("}");
      
      clazz.closeFile();
      
      System.out.println("Class '"+className+"' has been successufuly generated.");
    }catch(Exception e){
      throw new UserException("Error",e.getMessage()+" "+Tools.traceError(e)); 
    } finally{
      if (rs!=null) try { rs.close();} catch(Exception e){};
      if (stat!=null) try { stat.close();} catch(Exception e){};
    }
  }
  
  public static void generateClazzForSql(Connection con, String sql_file, String className, String path) throws UserException {
    PreparedStatement stat = null;
    ResultSet rs = null;    
    String pathSql = sql_file;
    if (path.indexOf("..")==0){
    }else{
      path = path.replace('.', '\\');
      pathSql = path+"\\"+sql_file;
      path = "..\\src\\"+path;
    }
    String sql = Tools.getTextFromFile(pathSql);
    ArrayList<DBModelField> fields = new ArrayList<DBModelField>();
    try{            
      boolean showJDEDateAndTime = false;
      boolean showJDEDate = false;
      boolean showBigDecimal = false;
      stat = con.prepareStatement(sql);
      rs = stat.executeQuery();    
      ResultSetMetaData md = null;
      try {
        md = rs.getMetaData();
      } catch (Exception e) {
        e.printStackTrace();
        throw new UserException("Information", "SQL Script has been executed very well.");
      }
       for (int i = 0; i < md.getColumnCount(); i++) {
        String fied_name = md.getColumnName(i + 1).trim();
        Class fieldClazz = String.class;
        String colType = ""+md.getColumnType(i + 1);
        if (colType.equalsIgnoreCase("TIMESTMP")) {fieldClazz = JDEDateAndTime.class; showJDEDateAndTime = true;}
        if (colType.equalsIgnoreCase("DATE")) {    fieldClazz = JDEDate.class; showJDEDate = true; }
        if (colType.equalsIgnoreCase("TIME"))      fieldClazz = Time.class;
        if (colType.equalsIgnoreCase("BIGINT"))    fieldClazz = long.class;
        if (colType.equalsIgnoreCase("INTEGER"))   fieldClazz = int.class;
        if (colType.equalsIgnoreCase("DECIMAL")) { fieldClazz = BigDecimal.class; showBigDecimal = true; }
        DBModelField field = new DBModelField(fied_name,fieldClazz);
        field.dbFieldType = colType;
        //String gen = rs.getString("GENERATED").trim();
        //if (gen.equalsIgnoreCase("A")) field.setAutoIncrement();
        fields.add(field);       
      }
      rs.close();
      stat.close();
      
      File dir = new File(path); 
      dir.mkdirs();
      TempFileWrite clazz = new TempFileWrite(path+"\\"+className+".java","\n","CP1251");
      
      String package_name = path.replaceAll("..\\\\src\\\\", ""); 
      package_name = package_name.replaceAll("\\\\", ".");
      
      clazz.delete();
      clazz.println("/*** KKV Class Generator V.0.1, This class is based on sql.");
      clazz.println(" *** The class was generated automatically.  ***/");
      clazz.println("");
      clazz.println("package "+package_name+";");
      clazz.println("");
      clazz.println("import DBClasses.DBControl.*;");
      if (showJDEDate) clazz.println("import Utils.JDEDate;");
      clazz.println("import Utils.Tools;");      
      if (showJDEDateAndTime) clazz.println("import DBClasses.DBControl.DBFieldAdapters.JDEDateAndTime;");
      if (showBigDecimal) clazz.println("import java.math.BigDecimal;");
      clazz.println("import java.sql.Time;");      
      clazz.println("");
      clazz.println("public class "+className+" {");
      clazz.println("  ");
      for (DBModelField field : fields){
        clazz.println("  public "+field.fieldType.getSimpleName()+" "+field.name.replace(' ', '_')+";   //  "+field.dbFieldType);
      } 
      clazz.println("  ");
      clazz.println("  /** Constructor */ ");
      clazz.println("  public "+className+"() {");
      clazz.println("  };");
      clazz.println("  ");      
      clazz.println("  public static DBSelectControl<"+className+"> dbSql = new DBSelectControl<"+className+">("+className+".class,\n"+
                    "    Tools.getTextFromFile(\""+pathSql.replaceAll("\\\\", "\\\\\\\\")+"\"),\n"+
                    "    new DBModelField[]{");
      for (DBModelField field : fields){
        String addon = "";
        if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT){
          addon = ".setAutoIncrement()";
        }
        clazz.println("      new DBModelField(\""+field.name.replace(' ', '_')+"\").setDbFieldName(\"\\\""+field.name+"\\\"\")"+addon+",");
      }  
      clazz.println("  });");
      clazz.println("  ");
      clazz.println("}");
      
      clazz.closeFile();
      
      System.out.println("Class '"+className+"' has been successufuly generated.");
    }catch(Exception e){
      throw new UserException("Error",e.getMessage()+" "+Tools.traceError(e)); 
    } finally{
      if (rs!=null) try { rs.close();} catch(Exception e){};
      if (stat!=null) try { stat.close();} catch(Exception e){};
    }
  }
  
  public String toString() {
    return "ID:" + this.id + " NAME:" + this.name + " IV:" + this.name + " VAL:" + this.account;
  }
}
