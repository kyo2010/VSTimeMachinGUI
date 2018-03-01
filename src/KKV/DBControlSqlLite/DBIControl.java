/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.DBFieldAdapters.*;
import KKV.Utils.Tools;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nkv.var.pub.IVar;

/**
 *
 * @author kyo
 */
public class DBIControl<Model> {

  public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm:ss"); // for insert Timestamp into DB
  public static DBFieldAdapter[] dbFieldAdapters = new DBFieldAdapter[]{
    new DBIntegerAdapter(),
    new DBDoubleAdapter(),
    new DBLongAdapter(),
    new DBStringAdapter(),
    new DBJDEAdapterDDMMYYYY(),
    new DBJDEAdapterDDMMYYYYwithNULL(),
    new DBJDEAdaperWithTimeDDMMYYYY(),
    new DBTimestampAdapter(),
    new DBJDEAdapter(),
    new DBJDEAdapterWithTime(),
    new DBBigDecimalAdapter(),
    new DBArrayAdapter(),
    new DBTimeAdapter(), 
    new DBTimeStringAdapter(),
    new DBIntegerWithExceptionAdapter()
  };
  /**
   * General Fields !!!
   */
  public DBModelField[] fields;
  public Class clazz = null;
  public String callSql = null;
  public String tableName = "";
  public String addonJoins = ""; // additional joins for constarins and orders
  
  public String initSQl = null;
   public DBIControl setInitSQl(String initSQl) {
    this.initSQl = initSQl;
    return this;
  }

  public DBIControl setCallProcedure(String callSql) {
    this.callSql = callSql;
    return this;
  }

  public DBIControl setAddonJoins(String addonJoins) {
    this.addonJoins = addonJoins;
    return this;
  }

  /** You can hide line, if object = null */
  public Object prepareObjectBeforePrinting(Object obj) {
    return obj;
  }

  public void runCallableProcedureEx(Connection con, String procedure, Object... args) throws UserException {
    if (procedure == null || procedure.trim().equals("")) {
      return;
    }
    try {
      CallableStatement stat = con.prepareCall(procedure);
      fillStat(procedure, stat, args);
      stat.execute();
    } catch (Exception e) {
      throw new UserException("'" + procedure + "' Calling of DB2 procedure is error...", Tools.traceErrorWithCaption(e));
    }
  }
  
  public void runCallableProcedure(Connection con, Object... args) throws UserException {
    if (callSql == null || callSql.trim().equals("")) {
      return;
    }
    try {
      CallableStatement stat = con.prepareCall(callSql);
      fillStat(callSql, stat, args);
      stat.execute();
    } catch (Exception e) {
      throw new UserException("'" + callSql + "' Calling of DB2 procedure is error...", Tools.traceErrorWithCaption(e));
    }
  }

  public static void runCallableProcedure(Connection con, String callSql, Object... args) throws UserException {
    if (callSql == null || callSql.trim().equals("")) {
      return;
    }
    try {
      CallableStatement stat = con.prepareCall(callSql);
      fillStat(callSql, stat, args);
      stat.execute();
    } catch (Exception e) {
      throw new UserException("'" + callSql + "' Calling of DB2 procedure is error...", Tools.traceErrorWithCaption(e));
    }
  }

  // filName = ReportJavaBeans/psi/BW
  public static void runSQLScriptFromFile(Connection con, String fileName, IVar var) throws UserException {
    PreparedStatement stat = null;
    String sql = "";
    try {
      String[] sqls = Tools.getTextFromFile(fileName, var).split(";");
      for (String s : sqls) {
        sql = s;
        if (!s.trim().equalsIgnoreCase("")) {
          stat = con.prepareCall(sql);
          stat.execute();
        }
      }
    } catch (Exception e) {
      throw new UserException("'" + sql + "' Calling of DB2 procedure is error...", Tools.traceErrorWithCaption(e));
    }
  }
  
  public void execBatchSql(Connection conn, String sqls, Object... args) throws UserException {
    PreparedStatement stat = null;
    ResultSet rs = null;
    String sql = "";
    
    try {
      String[] sqlm = sqls.split(";");
      for (String sqli : sqlm){
        sql = sqli;
        sql = sql.trim();
        if (!sql.equalsIgnoreCase("")){
          stat = conn.prepareStatement(sql);      
          fillStat(sql, stat, args);
        }   
      }  
      //rs = stat.executeQuery();
      stat.execute();
    } catch (SQLException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      System.out.println("Sql is error. SQL : " + sql + "\nError message: \n" + se.toString());
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      System.out.println(Tools.traceErrorWithCaption(e));
      throw new UserException("Error", "GetList error. Model: " + clazz.getName() + " sql:" + sql);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stat != null) {
          stat.close();
        }
      } catch (Exception e) {
      }
    }
  }

  public static PreparedStatement fillStat(String sql, PreparedStatement stat, Object... args) throws UserException {
    try {
      if (args != null) {
        int argIndex = 1;
        for (Object arg : args) {
          if (arg != null) {
            Class cl = arg.getClass();
            if (cl.equals(String.class)) {
              stat.setString(argIndex, ((String) arg).trim());
            }else if (cl.equals(Integer.class) || cl.equals(int.class)) {
              stat.setInt(argIndex, (Integer) arg);
            }else if (cl.equals(Long.class) || cl.equals(long.class)) {
              stat.setLong(argIndex, (Long) arg);
            }else if (cl.equals(Double.class) || cl.equals(double.class)) {
              stat.setDouble(argIndex, (Double) arg);
            }else if (cl.equals(Timestamp.class)) {
              stat.setTimestamp(argIndex, (Timestamp) arg);
            }else{
              throw new UserException("Error","Object type '"+cl.getSimpleName()+"' can't set to statement! \nsql:"+sql);
            }
            argIndex++;
          }
        }
      } else {
        //do nothing
      }
    } catch (UserException ue) {
      throw ue;     
    } catch (Exception e) {
      throw new UserException("Error", "Error filling statement \n" + Tools.traceError(e) + "\nsql:"+sql);
    }
    return stat;
  }

  public DBFieldAdapter getSuitableAdapter(Field field) throws UserException {
    DBFieldAdapter suitableFieldAdapter = null;
    for (DBFieldAdapter fieldAdapter : dbFieldAdapters) {
      if (fieldAdapter.isValidClass(field)) {
        suitableFieldAdapter = fieldAdapter;
        break;
      }
    }
    if (suitableFieldAdapter == null) {
      throw new UserException("Error", "Adapter is not found for type:" + field.getType());
    }
    return suitableFieldAdapter;
  }

  public String getDBFiledNameByName(String name) throws UserException {
    for (DBModelField field : fields) {
      if (field.name.equals(name)) {
        return field.dbFieldName;
      }
      if (field.dbFieldName.equals(name)) {
        return field.dbFieldName;
      }
    }
    throw new UserException("Error", "Field name:" + name + " is not found into " + this.tableName + " " + this.getClass().getSimpleName());
  }

  public DBModelField getIDField() {
    for (DBModelField field : fields) {
      if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT) {
        return field;
      }
    }
    return null;
  }

  public DBModelField getModelFieldByName(String name) {
    for (DBModelField f : fields) {
      if (f.name.equalsIgnoreCase(name) || f.dbFieldName.equalsIgnoreCase(name)) {
        return f;
      }
    }
    return null;
  }

  public <Model> String getProperty(Model item, String propertyName) throws UserException {
    return getProperty(item, propertyName, 0);
  }

  public <Model> String[][] getListToStringArray(List<Model> list) throws UserException {
    String[][] result = new String[list.size()][list.size() == 0 ? 0 : fields.length];
    int m_index = 0;
    for (Model m : list) {
      int f_index = 0;
      for (DBModelField field : fields) {
        result[m_index][f_index] = getProperty(m, field.name);
        f_index++;
      }
      m_index++;
    }
    return result;
  }
  
  public <Model> List<String[]> getListFromSQL(Connection conn,String sql, Object... args) throws UserException {
    List<String[]> result = new ArrayList<String[]>();
    PreparedStatement stat = null;
    ResultSet rs = null;
    try {      
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
      rs = stat.executeQuery();
      int colCount = stat.getMetaData().getColumnCount();
      while (rs.next()){
        String[] lines = new String[colCount];
        result.add(lines);
        for (int i = 0; i<colCount; i++){
          lines[i] = rs.getString(i+1).trim();
        }
      }
    } catch (SQLException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      System.out.println("Sql is error. SQL : " + sql + "\nError message: \n" + se.toString());
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      System.out.println(Tools.traceErrorWithCaption(e));
      throw new UserException("Error", "GetList error. Model: " + clazz.getName() + " sql:" + sql);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stat != null) {
          stat.close();
        }
      } catch (Exception e) {
      }
    }
    return result;
  }

  public <Model> String[][] getListToStringArray(List<Model> list, String... output_fields) throws UserException {
    String[][] result = new String[list.size()][output_fields.length];
    int m_index = 0;
    for (Model m : list) {
        for (int i=0; i<output_fields.length; i++){
          String field1 = output_fields[i];
          String[] pars_fields = field1.split("\\+");
          String val = "";
          for (DBModelField field : fields) {      
            for (String pField : pars_fields){
              if (pField.equalsIgnoreCase(field.name) || pField.equalsIgnoreCase(field.dbFieldName) || pField.equalsIgnoreCase(field.caption)) {
                if (!val.equalsIgnoreCase("")) val+=" ";
                val += getProperty(m, field.name);              
              }
            }
          }        
          result[m_index][i] = val;
        }
      m_index++;
    }
    return result;
  }

  public <Model> String getProperty(Model item, String propertyName, int array_index) throws UserException {
    String value = "";
    try {
      Field fo = clazz.getField(propertyName);
      DBFieldAdapter adpter = getSuitableAdapter(fo);      
      DBModelField mf = getModelFieldByName(propertyName);
      value = adpter.getField(fo, item, propertyName, array_index, mf);
      if (adpter != null && mf != null) {
        mf.fieldType = adpter.getAdapterClass();
        mf.fieldAdapter = adpter;
      }
    } catch (IllegalAccessException se) {
      throw new UserException("Filed Access is not found", "Model : " + clazz.getName() + " field:" + propertyName);
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName() + " field:" + propertyName);
    }
    return value;
  }

  public <Model> void setProperty(Model item, String propertyName, String value) throws UserException {
    try {
      Field fo = clazz.getField(propertyName);
      DBModelField mf = getModelFieldByName(propertyName);
      getSuitableAdapter(fo).setField(fo, item, propertyName, value, 0,mf);
    } catch (IllegalAccessException se) {
      throw new UserException("Filed Access is not found", "Model : " + clazz.getName() + " field:" + propertyName);
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName() + " field:" + propertyName);
    }
  }

  public Class getPropertyClass(String propertyName) throws UserException {
    try {
      Field fo = clazz.getField(propertyName);
      return fo.getType();
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName() + " field:" + propertyName);
    }
  }

  public <Model> String itemToStr(Model item) {
    String fieldStr = "Table:"+tableName+"\n";
    fieldStr += "Class:"+clazz.getName()+"\n";
    DBModelField currField = null;
    try {
      for (DBModelField field : fields) {
        currField = field;
        String value = getProperty(item, field.name);
        if (field.fieldType.equals(String.class)) {
          String dopInfo = "";
          if (field.metaData != null /*
                   * && field.metaData.LENGTH<value.length()
                   */) {
            String red_beg = "";
            String red_end = "";
            if (field.metaData.LENGTH / 2 < value.length()) {
              red_beg = "<font color='green'>";
              red_end = "</font>";
            }
            if (field.metaData.LENGTH < value.length()) {
              red_beg = "<font color='red'>";
              red_end = "</font>";
            }
            dopInfo = red_beg + " (dbType: " + field.metaData.COLTYPE + " max_length:" + field.metaData.LENGTH + ", your length is:" + value.length() + ")" + red_end;
          }
          fieldStr += field.dbFieldName + "='" + value + "'" + dopInfo + "\n";
        } else {
          String dopInfo = "";
          if (field.metaData != null) {
            dopInfo = " (dbType: " + field.metaData.COLTYPE + ")";
          }
          fieldStr += field.dbFieldName + "=" + value + dopInfo + "\n";
        }
      }
    } catch (Exception e) {
      fieldStr += "Getting field is error - " + currField.name;
    }

    return fieldStr;
  }

  /**
   * Replace all '<' => &lt; '>' => '&gt'
   */
  public void replaseAllHTMLTag(Object obj) {
    try {
      for (DBModelField modelField : fields) {
        Field field = clazz.getField(modelField.name);
        if (field.getGenericType().equals(String.class) /*
                 * modelField.fieldType.equals(String.class)
                 */) {
          String val = (String) field.get(obj);
          val = val.replaceAll("<", "&lt;");
          val = val.replaceAll(">", "&gt;");
          field.set(obj, val);
        }
      }
    } catch (Exception e) {
      System.out.println("System error... please check..." + e.getMessage() + " " + Tools.traceError(e));
    }
  }

  ;
    
    public <Model> String getHTMLTable(String caption, List<Model> list) throws UserException {
    StringBuffer html = new StringBuffer();

    if (caption != null) {
      html.append("<b>" + caption + "</b><br/><br/>");
    };

    html.append("<table border='1' style='border-collapse: collapse; border-color:#000000;'>");
    html.append("<tr style='background:#4791C5; color:white; '>");
    for (DBModelField field : fields) {
      if (!field.hide) {
        html.append("<td lign='center'><b>" + (field.caption == null ? field.dbFieldName : field.caption) + "</b></td>");
      }
    }
    html.append("</tr>");

    for (Model obj : list) {
      html.append("<tr style='background:#ffffff;'>");
      for (DBModelField field : fields) {
        if (!field.hide) {
          html.append("<td>" + getProperty(obj, field.name) + "</td>");
        }
      }
      html.append("</tr>");
    }

    return html.toString();
  }

  public <Model> Model copyObject(Model item2) throws UserException {
    Model item = null;
    try {
      item = (Model) clazz.newInstance();
    } catch (Exception e) {
      throw new UserException("Default constructor is not found", "Model : " + clazz.getName());
    }

    if (item2 != null) {
      for (DBModelField modelField : fields) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          String value = fieldAdapter.getField(field, item2, modelField.name, 0,modelField);
          fieldAdapter.setField(field, item, modelField.name, value, 0,modelField);
        } catch (Exception e) {          
          throw new UserException("Error", "Object can not be copied. Field :" + modelField.name+"\n"+Tools.traceErrorWithCaption(e));
        }
      }
    }
    return item;
  }
  
  public Object copyObject(Object from_item, Object to_item) throws UserException {
    
      for (DBModelField modelField : fields) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          String value = fieldAdapter.getField(field, from_item, modelField.name, 0,modelField);
          fieldAdapter.setField(field, to_item, modelField.name, value, 0,modelField);
        } catch (Exception e) {
          throw new UserException("Error", "Object can not be copied. Field :" + modelField.name);
        }
      }
    
    return to_item;
  }
   
}
