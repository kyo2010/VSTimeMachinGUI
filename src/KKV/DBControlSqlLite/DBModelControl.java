/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite;

import KKV.DBControlSqlLite.DBFieldAdapters.DBFieldAdapter;
import KKV.DBControlSqlLite.Utils.Tools;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

import java.lang.reflect.Field;
//import java.lang.reflect.Type;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kimlaev
 */
public class DBModelControl<Model> extends DBIControl {

  public DBModelControl setCallProcedure(String callSql) {
    this.callSql = callSql;
    return this;
  }

  public DBModelControl setInitSQl(String initSQl) {
    this.initSQl = initSQl;
    return this;
  }

  public String getTableAlias() {
    return clazz.getSimpleName();
  }

  public <Model> DBModelControl(Class clazz, String tableName, DBModelField[] fields) {
    this.clazz = clazz;
    this.tableName = tableName;
    this.fields = fields;
  }

  public String NEXT_FIELD_NAME = null;
  public String PRED_FIELD_NAME = null;

  public DBModelControl setIterratorFields(String PRED_FIELD_NAME, String NEXT_FIELD_NAME) {
    this.NEXT_FIELD_NAME = NEXT_FIELD_NAME;
    this.PRED_FIELD_NAME = PRED_FIELD_NAME;
    return this;
  }

  public int count(Connection conn, String where, Object... args) throws UserException {
    String sql = "SELECT count(*) FROM " + tableName + ((where == null || where.equals("")) ? "" : " WHERE " + where);
    PreparedStatement stat = null;
    ResultSet rs = null;
    int result = -1;
    try {
      stat = conn.prepareStatement(sql);
      stat = fillStat(sql, stat, args);
      rs = stat.executeQuery();
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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

  public int count(Connection conn, String where) throws UserException {
    return count(conn, where, new Object[]{});
  }

  public String getSelectSql(String where) throws UserException {
    String sql_fields = "";
    String joins = "";
    for (DBModelField field : fields) {
      if (field.fieldTarget == DBModelField.FT_JOIN_FIELD) {
        //continue;
        for (DBModelField jField : field.joinDBControl.fields) {
          if (!sql_fields.equals("")) {
            sql_fields += ",";
          }
          sql_fields += field.joinDBControl.getTableAlias() + "." + jField.dbFieldName;
        }
        String joinCondition = "";
        int index = 0;
        for (String f1 : field.joinFrom) {
          if (index >= field.joinTo.length) {
            throw new UserException("Error", "Join connditionals length doesn't match " + field.joinFrom.length + " " + field.joinTo.length);
          }
          String f2 = field.joinTo[index];
          if (!joinCondition.equals("")) {
            joinCondition += " AND ";
          }
          joinCondition += getTableAlias() + "." + getDBFiledNameByName(f1) + "="
                  + field.joinDBControl.getTableAlias() + "." + field.joinDBControl.getDBFiledNameByName(f2);
          index++;
        }
        joins += " " + field.getJoinType() + " " + field.joinDBControl.tableName + " as " + field.joinDBControl.getTableAlias() + " ON " + joinCondition + "\n";
      } else {
        if (field.fieldTarget == DBModelField.FT_SKIP) {
        } else if (field.fieldTarget == DBModelField.FT_CALULATED_FIELD) {
          if (!sql_fields.equals("")) {
            sql_fields += ",";
          }
          sql_fields += field.SQLCalculatedFormula;
        } else if (field.dbFieldName != null) {
          if (field.fieldTarget == DBModelField.FT_ARRAY) {
            for (int i = 0; i < field.FT_ARRAY_SIZE; i++) {
              if (!sql_fields.equals("")) {
                sql_fields += ",";
              }
              sql_fields += getTableAlias() + "." + "\"" + field.dbFieldName + i + "\"";
            }
          } else {
            if (!sql_fields.equals("")) {
              sql_fields += ",";
            }
            if (field.dbFieldName.indexOf("(") >= 0) {
              sql_fields += field.dbFieldName;
            } else {
              sql_fields += getTableAlias() + "." + field.dbFieldName;
            }
          }
        }
      }
    }
    String sql = "SELECT " + sql_fields + " FROM " + tableName + " as " + getTableAlias() + addonJoins + joins + ((where == null || "".equals(where)) ? "" : " WHERE " + where);
    sql = sql.replaceAll("\\<T1\\>", getTableAlias() + ".");
    return sql;
  }

  public <Model> List<Model> __getListFromRS(ResultSet rs, String sql) throws UserException {
    Model item = null;
    List<Model> result = new ArrayList<Model>();
    Model prev_item = null;
    try {
      while (rs.next()) {
        try {
          item = (Model) clazz.newInstance();
        } catch (Exception e) {
          throw new UserException("Default constructor is not found", "Model : " + clazz.getName());
        }

        int rsIndex = 1;
        boolean isNewItem = false;
        boolean joinExist = false;
        Map<String, Object> join_objs = new HashMap<String, Object>();
        for (DBModelField modelField : fields) {
          if (modelField.fieldTarget == DBModelField.FT_JOIN_FIELD) {
            joinExist = true;
            List jList = null;
            boolean itIsItem = false;

            try {
              jList = (List) clazz.getField(modelField.name).get(item);
            } catch (Exception e) {
              itIsItem = true;
              //throw new UserException("Error", "We can't get List object from field " + modelField.name + " " + e.getMessage());
            }
            if (jList != null) {
              jList.clear();
            }
            Object join_item = modelField.joinDBControl.clazz.newInstance();
            join_objs.put(modelField.name, join_item);
            for (DBModelField joinField : modelField.joinDBControl.fields) {
              if (rs.getString(rsIndex) != null) {
                Field field = modelField.joinDBControl.clazz.getField(joinField.name);
                DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
                modelField.fieldAdapter = fieldAdapter;
                fieldAdapter.loadFromRS(field, join_item, rs, rsIndex, joinField, 0);
              } else {
                isNewItem = true;
                join_objs.put(modelField.name, null);
              }
              rsIndex++;
            }
            try {
              clazz.getField(modelField.name).set(item, join_item);
            } catch (Exception e) {
            }
          } else {
            if (modelField.fieldTarget == DBModelField.FT_SKIP) {
            } else if (modelField.dbFieldName != null) {
              Field field = clazz.getField(modelField.name);
              DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
              modelField.fieldAdapter = fieldAdapter;

              if (modelField.fieldTarget == DBModelField.FT_ARRAY) {
                for (int i = 0; i < modelField.FT_ARRAY_SIZE; i++) {
                  fieldAdapter.loadFromRS(field, item, rs, rsIndex, modelField, i);
                  rsIndex++;
                }
              } else {
                fieldAdapter.loadFromRS(field, item, rs, rsIndex, modelField, 0);
                rsIndex++;
              }

              if (prev_item == null) {
                isNewItem = true;
              } else {
                if (field.get(item) != null && field.get(prev_item) != null) {
                  if (!field.get(item).equals(field.get(prev_item))) {
                    isNewItem = true;
                  }
                }
                if (field.get(item) == null && field.get(prev_item) != null) {
                  isNewItem = true;
                }
                if (field.get(item) != null && field.get(prev_item) == null) {
                  isNewItem = true;
                }
              }
            }
          }
        }
        if (isNewItem || joinExist == false) {
          result.add(item);

          /*try{
            if(clazz.getField("ACCOUNT").get(item).toString().equalsIgnoreCase("70010549")){
              int y = 0;
            } 
          }catch(Exception e){}  */
          if (PRED_FIELD_NAME != null && prev_item != null) {
            try {
              clazz.getField(PRED_FIELD_NAME).set(item, prev_item);
            } catch (Exception e) {
            }
          }
          if (NEXT_FIELD_NAME != null && prev_item != null) {
            try {
              clazz.getField(NEXT_FIELD_NAME).set(prev_item, item);
            } catch (Exception e) {
            }
          }
          prev_item = item;
        }
        // add List to prev_item
        if (joinExist) {
          for (String name : join_objs.keySet()) {
            List jList = null;
            try {
              jList = (List) clazz.getField(name).get(prev_item);
            } catch (Exception e) {
              // throw new UserException("Error", "We can't get List object from field " + name + " " + e.getMessage());
            }
            if (jList == null) {
              try {
                jList = new ArrayList<Model>();
                clazz.getField(name).set(prev_item, jList);
              } catch (Exception e) {
              }
            }
            try {
              if (join_objs.get(name) != null && jList != null) {
                jList.add(join_objs.get(name));
              }
            } catch (Exception e) {
            }

          }
        }
      }
    } catch (SQLException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      System.out.println("Sql is error. SQL : " + sql + "\nError message: \n" + se.toString());
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (IllegalAccessException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      throw new UserException("Field Access is not found", "Model : " + clazz.getName());
    } catch (NoSuchFieldException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      throw new UserException("Field is not found", "Model : " + clazz.getName());
    } catch (Exception e) {
      System.out.println(Tools.traceErrorWithCaption(e));
      throw new UserException("Error", "GetList error. Model: " + clazz.getName() + " sql:" + sql);
    } finally {
    }
    return result;
  }

  public <Model> List<Model> getList(Connection conn, String where, Object... args) throws UserException {
    if (conn == null) {
      throw new UserException("Connection is null", "Development error. " + getTableAlias());
    }
    List<Model> result = new ArrayList<Model>();
    PreparedStatement stat = null;
    ResultSet rs = null;
    String sql = "";
    try {
      sql = getSelectSql(where);
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
      rs = stat.executeQuery();
      result = __getListFromRS(rs, sql);
    } catch (SQLException se) {
      System.out.println(Tools.traceErrorWithCaption(se));
      System.out.println("Sql is error. SQL : " + sql + "\nError message: \n" + se.toString());
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (UserException ue) {
      System.out.println(Tools.traceErrorWithCaption(ue));
      throw ue;
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

  public <Model> Map<String, Model> getMap(Connection conn, String key1, String where) throws UserException {
    return getMap(conn, key1, where, new Object[]{});
  }

  public <Model> Map<String, Model> getMap(Connection conn, String key1, String where, Object... args) throws UserException {
    Map<String, Model> result = new HashMap<String, Model>();
    DBModelField f_key1 = null;
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_key1 = modelField;
      }
    }
    if (f_key1 == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String keyValue1 = "";
      try {
        Field field = clazz.getField(f_key1.name);
        DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
        keyValue1 = fieldAdapter.getField(field, obj, f_key1.name, 0, f_key1);
      } catch (Exception e) {
        throw new UserException("Error", "I can't read a field name '" + f_key1.name + "' in obj:" + clazz.getSimpleName());
      }
      result.put(keyValue1.trim(), obj);
    }
    return result;
  }

  public <Model> Map<String, List<Model>> getMapInList(Connection conn, String key1, String where, Object... args) throws UserException {
    Map<String, List<Model>> result = new HashMap<String, List<Model>>();
    DBModelField f_key1 = null;
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_key1 = modelField;
      }
    }
    if (f_key1 == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String keyValue1 = "";
      try {
        Field field = clazz.getField(f_key1.name);
        DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
        keyValue1 = fieldAdapter.getField(field, obj, f_key1.name, 0, f_key1);
      } catch (Exception e) {
        throw new UserException("Error", "I can't read a field name '" + f_key1.name + "' in obj:" + clazz.getSimpleName());
      }
      List<Model> list_res = result.get(keyValue1.trim());
      if (list_res == null) {
        list_res = new ArrayList<Model>();
        result.put(keyValue1.trim(), list_res);
      }
      list_res.add(obj);
    }
    return result;
  }

  // distinct by some field
  public ArrayList<String> getDistinct(Connection conn, String... fields) throws UserException {
    ArrayList<String> items = new ArrayList<String>();
    String fields_st = null;
    for (String field : fields) {
      if (fields_st != null) {
        fields_st += ",";
      } else {
        fields_st = "";
      }
      fields_st += field;
    }

    String sql = "SELECT DISTINCT \"" + fields_st + "\" FROM " + tableName;
    PreparedStatement stat = null;

    ResultSet rs = null;
    try {
      stat = conn.prepareStatement(sql);

      rs = stat.executeQuery();

      String tempItem = null;

      while (rs.next()) {
        tempItem = null;
        for (String field : fields) {
          if (tempItem != null) {
            tempItem += " - ";
          } else {
            tempItem = "";
          }
          tempItem += rs.getString(field).trim();
        }
        items.add(tempItem);
      }
      rs.close();
      stat.close();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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
    return items;
  }

  public List<String> getDistinctByCondition(Connection conn, String field, String condition, Object... args) throws UserException {

    List<String> items = new ArrayList<String>();
    String sql = "SELECT DISTINCT " + field + " FROM " + tableName + " where " + condition;
    PreparedStatement stat = null;

    ResultSet rs = null;
    try {
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
      rs = stat.executeQuery();

      String tempItem = null;

      while (rs.next()) {

        tempItem = rs.getString(1).trim();

        items.add(tempItem);
      }
      rs.close();
      stat.close();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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
    return items;
  }

  public String[][] getDistinctByCondition(Connection conn, String fieldsName[], String condition, Object... args) throws UserException {
    return getDistinctByCondition(conn, fieldsName, null, false, condition, args);
  }

  public String[][] getDistinctByCondition(Connection conn, String fieldsName[], String addonRecords[][], String condition, Object... args) throws UserException {
    return getDistinctByCondition(conn, fieldsName, addonRecords, false, condition, args);
  }

  public String[][] getDistinctByCondition(Connection conn, String fieldsName[], String addonRecords[][], boolean inEnd, String condition, Object... args) throws UserException {

    ArrayList<String[]> items = new ArrayList<String[]>();
    if (addonRecords == null) {
      addonRecords = new String[0][0];
    }

    String field_st = "";
    for (String field : fieldsName) {
      if (!field_st.equals("")) {
        field_st += ",";
      }
      field_st += field;
    }

    String sql = "SELECT DISTINCT " + field_st + " FROM " + tableName + " where " + condition;
    PreparedStatement stat = null;

    ResultSet rs = null;
    try {
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
      rs = stat.executeQuery();
      while (rs.next()) {
        String[] info = new String[fieldsName.length];
        for (int i = 0; i < fieldsName.length; i++) {
          info[i] = rs.getString(i + 1);
        }
        items.add(info);
      }
      rs.close();
      stat.close();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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

    String[][] res = new String[items.size() + addonRecords.length][];
    if (inEnd) {
      for (int i = 0; i < items.size(); i++) {
        res[i] = items.get(i);
      }
      for (int i = 0; i < addonRecords.length; i++) {
        res[i + items.size()] = addonRecords[i];
      }
    } else {
      for (int i = 0; i < addonRecords.length; i++) {
        res[i] = addonRecords[i];
      }
      for (int i = 0; i < items.size(); i++) {
        res[i + addonRecords.length] = items.get(i);
      }
    }

    if (res.length == 0) {
      res = new String[0][0];
    }

    return res;
  }

  public ArrayList<String[]> getDistinctsByCondition(Connection conn, String condition, String... fields) throws UserException {

    ArrayList<String[]> items = new ArrayList<String[]>();
    String fields_st = null;
    for (String field : fields) {
      if (fields_st != null) {
        fields_st += ",";
      } else {
        fields_st = "";
      }
      fields_st += field;
    }
    String sql = "SELECT DISTINCT " + fields_st + " FROM " + tableName + " where " + condition;
    PreparedStatement stat = null;

    ResultSet rs = null;
    try {
      stat = conn.prepareStatement(sql);
      rs = stat.executeQuery();

      String tempItem = null;

      while (rs.next()) {
        String[] rows = new String[fields.length];
        int index = 0;
        for (String field : fields) {
          rows[index] = rs.getString(field).trim();
          index++;
        }
        items.add(rows);
      }
      rs.close();
      stat.close();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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
    return items;
  }

  public ArrayList<String[]> getGroupBy(Connection conn, String condition, String orderby, String... fields) throws UserException {

    ArrayList<String[]> items = new ArrayList<String[]>();
    String fields_st = null;
    String group_by_st = "";
    for (String field : fields) {
      if (fields_st != null) {
        fields_st += ",";
      } else {
        fields_st = "";
      }
      fields_st += field;
      if (field.indexOf("MAX") != 0 && field.indexOf("MIN") != 0 && field.indexOf("SUM") != 0) {
        if (!group_by_st.equals("")) {
          group_by_st += ",";
        }
        group_by_st += field;
      }
    }

    String sql = "SELECT " + fields_st + " FROM " + tableName + " where " + condition + " Group by " + group_by_st + " " + orderby;
    PreparedStatement stat = null;

    ResultSet rs = null;
    try {
      stat = conn.prepareStatement(sql);
      rs = stat.executeQuery();

      String tempItem = null;

      while (rs.next()) {
        String[] rows = new String[fields.length];
        int index = 0;
        for (String field : fields) {
          rows[index] = rs.getString(index + 1).trim();
          index++;
        }
        items.add(rows);
      }
      rs.close();
      stat.close();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName());
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
    return items;
  }

  public Model getItem(Connection conn, String where) throws UserException {
    List<Model> itemList = getList(conn, (!"".equals(where) ? where : "1=1") /*+ " fetch first 1 rows only"*/);
    Model item = null;
    if (itemList.size() > 0) {
      item = itemList.get(0);
    }
    return item;
  }

  public Model getItem(Connection conn, String where, Object... args) throws UserException {
    List<Model> itemList = getList(conn, (!"".equals(where) ? where : "1=1") /*+" fetch first 1 rows only"*/, args);
    Model item = null;
    if (itemList.size() > 0) {
      item = itemList.get(0);
    }
    return item;
  }

  public <Model> List<Model> getList(Connection conn) throws UserException {
    return getList(conn, "");
  }

  public <Model> Model getByID(Connection conn, Object id) throws UserException {
    DBModelField fieldIDName = getIDField();
    if (fieldIDName == null) {
      throw new UserException("ID field not found", "Model : " + clazz.getName());
    }
    List<Model> items = getList(conn, fieldIDName.dbFieldName + "=" + id);
    if (items.size() == 0) {
      return null;
    }
    return items.get(0);
  }

  public <Model> List<Model> getList(Connection conn, String where) throws UserException {
    return getList(conn, where, new Object[]{});
  }

  public long getMax(Connection conn, String field, String where, Object... args) throws UserException {
    long id = -1;
    try {
      if (where != null && !where.equalsIgnoreCase("")) {
        where = " where " + where;
      }
      String sql = "select MAX(" + field + ") from " + this.tableName + where;
      PreparedStatement stat = null;
      ResultSet rs = null;
      try {
        stat = conn.prepareStatement(sql);
        fillStat(sql, stat, args);
        rs = stat.executeQuery();
        if (rs.next()) {
          id = rs.getLong(1);
        }
      } catch (Exception e) {
        if (rs != null) {
          rs.close();
        }
        if (stat == null) {
          stat.close();
        }
      }
    } catch (Exception e) {
      throw new UserException(e.toString(), Tools.traceErrorWithCaption(e));
    }
    return id;
  }

  public boolean save(Connection conn, Model item) throws UserException {
    DBModelField fieldIDName = getIDField();

    try {
      Object id = fieldIDName == null ? null : (Object) clazz.getField(fieldIDName.name).get(item);
      if (id == null) {
        insert(conn, item);
      } else {
        if (fieldIDName == null) {
          throw new UserException("ID field not found", "Model : " + clazz.getName());
        }
        update(conn, item);
      }
    } catch (IllegalAccessException se) {
      throw new UserException("Filed Access is not found", "Model : " + clazz.getName());
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName());
    }
    return true;
  }

  public <Model> boolean insert(Connection conn, Model item) throws UserException {
    List<Model> items = new ArrayList<Model>();
    items.add(item);
    return insert(conn, items);
  }
  private PreparedStatement stat_insert = null;
  private boolean useSavedPreparedStatment = false;

  public void setUseSavedPreparedStatment(boolean useSavedPreparedStatment) {
    this.useSavedPreparedStatment = useSavedPreparedStatment;
    if (!useSavedPreparedStatment) {
      // statment is opened, try to close the insert statment
      if (stat_insert != null) {
        try {
          stat_insert.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public <Model> boolean insert(Connection conn, List<Model> items) throws UserException {
    String sql_fields = "";
    DBModelField fieldIDName = null;
    PreparedStatement stat = null;
    Model currentItem = null;
    String values_debug = "";

    String sql = "";
    String addon_sql = "";
    DBModelField field_def = null;

    ResultSet rs = null;

    try {
      if (!useSavedPreparedStatment || stat_insert == null) {
        for (DBModelField field : fields) {
          if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT) {
            fieldIDName = field;
          }
          if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT
                  || field.fieldTarget == DBModelField.FT_JOIN_FIELD
                  || field.fieldTarget == DBModelField.FT_SKIP
                  || field.fieldTarget == DBModelField.FT_SKIP_INSERT_UPDATE
                  || field.fieldTarget == DBModelField.FT_CALULATED_FIELD
                  || field.fieldTarget == DBModelField.FT_SKIP_INSERT) {
            continue;
          }
          if (field.dbFieldName != null) {
            if (field.fieldTarget == DBModelField.FT_ARRAY) {
              for (int i = 0; i < field.FT_ARRAY_SIZE; i++) {
                if (!sql_fields.equals("")) {
                  sql_fields += ",";
                }
                sql_fields += "\"" + field.dbFieldName + i + "\"";
              }
            } else {
              if (!sql_fields.equals("")) {
                sql_fields += ",";
              }
              sql_fields += field.dbFieldName;
            }
          }
        }
        String values = "";
        for (DBModelField field : fields) {
          if (field.fieldTarget == DBModelField.FT_AUTOINCREMENT
                  || field.fieldTarget == DBModelField.FT_JOIN_FIELD
                  || field.fieldTarget == DBModelField.FT_SKIP
                  || field.fieldTarget == DBModelField.FT_SKIP_INSERT_UPDATE
                  || field.fieldTarget == DBModelField.FT_CALULATED_FIELD
                  || field.fieldTarget == DBModelField.FT_SKIP_INSERT || field.dbFieldName == null) {
            continue;
          }
          if (field.fieldTarget == DBModelField.FT_ARRAY) {
            for (int i = 0; i < field.FT_ARRAY_SIZE; i++) {
              if (!values.equals("")) {
                values += ",";
              }
              values += "?";
            }
          } else {
            if (!values.equals("")) {
              values += ",";
            }
            values += "?";
          }
        }

        sql = "INSERT INTO " + tableName + " ( " + sql_fields + " ) VALUES (" + values + ")";
        if (fieldIDName != null) {
          //sql = "SELECT MAX(" + fieldIDName.dbFieldName + ") AS ID FROM NEW TABLE ( " + sql + " )";
          addon_sql = "select last_insert_rowid()";
        }
        stat = conn.prepareStatement(sql);
        if (useSavedPreparedStatment) {
          stat_insert = stat;
        }
      } else {
        stat = stat_insert;
      }
      for (Model item : items) {
        // TODO
        currentItem = item;
        values_debug = "";
        int paramIndex = 1;
        for (DBModelField modelField : fields) {
          if (modelField.fieldTarget == DBModelField.FT_AUTOINCREMENT
                  || modelField.fieldTarget == DBModelField.FT_JOIN_FIELD
                  || modelField.fieldTarget == DBModelField.FT_SKIP
                  || modelField.fieldTarget == DBModelField.FT_SKIP_INSERT_UPDATE
                  || modelField.fieldTarget == DBModelField.FT_CALULATED_FIELD
                  || modelField.fieldTarget == DBModelField.FT_SKIP_INSERT || modelField.dbFieldName == null) {
            continue;
          }
          field_def = modelField;
          Field field = clazz.getField(modelField.name);

          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          if (modelField.fieldTarget == DBModelField.FT_ARRAY) {
            for (int i = 0; i < modelField.FT_ARRAY_SIZE; i++) {
              fieldAdapter.setPSField(field, item, stat, paramIndex, i, modelField);
              paramIndex++;
            }
          } else {
            fieldAdapter.setPSField(field, item, stat, paramIndex, 0, modelField);
            paramIndex++;
          }
        }
        if (fieldIDName == null) {
          stat.execute();
        } else {
          stat.execute();
          stat.close();
          stat = conn.prepareStatement(addon_sql);
          rs = stat.executeQuery();
          while (rs.next()) {
            if (java.lang.Integer.class.equals(clazz.getField(fieldIDName.name).getGenericType())
                    || int.class.equals(clazz.getField(fieldIDName.name).getGenericType())) {
              clazz.getField(fieldIDName.name).set(item, rs.getInt(1));
            } else if (java.lang.Long.class.equals(clazz.getField(fieldIDName.name).getGenericType())
                    || long.class.equals(clazz.getField(fieldIDName.name).getGenericType()) //  java.lang.long.class.equals(clazz.getField(fieldIDName.name).getGenericType())
                    ) {
              clazz.getField(fieldIDName.name).set(item, rs.getLong(1));
            } else {
              clazz.getField(fieldIDName.name).set(item, rs.getInt(1));
            }
          }
        }
      }
    } catch (SQLException se) {
      fillMetaData(conn);
      throw new UserException("Sql is error", se + "\n" + itemToStr(currentItem));
    } catch (IllegalAccessException se) {
      throw new UserException("Filed Access is not found", "Model : " + clazz.getName() + " field:" + field_def.name);
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName() + " field:" + field_def.name);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (!useSavedPreparedStatment) {
          // statment is opened
          if (stat != null) {
            stat.close();
          }
        }
      } catch (Exception e) {
      }
    }
    return true;
  }

  public <Model> boolean update(Connection conn, Model item) throws UserException {
    String sql_fields = "";
    DBModelField fieldIDName = null;
    PreparedStatement stat = null;
    String sql = "";
    ResultSet rs = null;
    try {
      List<DBModelField> fieldForStatement = new ArrayList<DBModelField>();
      List<Field> fieldForStatement2 = new ArrayList<Field>();
      String values = "";
      for (DBModelField modelField : fields) {
        if (modelField.fieldTarget == DBModelField.FT_AUTOINCREMENT) {
          fieldIDName = modelField;
        }
        if (modelField.fieldTarget == DBModelField.FT_AUTOINCREMENT
                || modelField.fieldTarget == DBModelField.FT_JOIN_FIELD
                || modelField.fieldTarget == DBModelField.FT_SKIP
                || modelField.fieldTarget == DBModelField.FT_SKIP_INSERT_UPDATE
                || modelField.fieldTarget == DBModelField.FT_CALULATED_FIELD
                || modelField.fieldTarget == DBModelField.FT_SKIP_UPDATE || modelField.dbFieldName == null) {
          continue;
        }
        if (modelField.fieldTarget == DBModelField.FT_ARRAY) {
          for (int i = 0; i < modelField.FT_ARRAY_SIZE; i++) {
            if (!values.equals("")) {
              values += ", ";
            }
            values += "\"" + modelField.dbFieldName + i + "\"" + "=?";
          }
        } else {
          if (!values.equals("")) {
            values += ", ";
          }
          values += modelField.dbFieldName + "=?";
        }
        Field field = clazz.getField(modelField.name);
        fieldForStatement.add(modelField);
        fieldForStatement2.add(field);
      }

      if (fieldIDName == null) {
        throw new UserException("ID field not found", "Model : " + clazz.getName());
      }

      sql = "UPDATE " + tableName + " SET " + values + "  WHERE " + fieldIDName.dbFieldName + "=" + clazz.getField(fieldIDName.name).get(item);
      stat = conn.prepareStatement(sql);

      int paramIndex = 1;
      int filedIndex = 0;
      for (Field field : fieldForStatement2) {
        //fill statement parameters
        DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
        DBModelField modelField = fieldForStatement.get(filedIndex);
        if (modelField.fieldTarget == DBModelField.FT_ARRAY) {
          for (int i = 0; i < modelField.FT_ARRAY_SIZE; i++) {
            fieldAdapter.setPSField(field, item, stat, paramIndex, i, modelField);
            paramIndex++;
          }
        } else {
          fieldAdapter.setPSField(field, item, stat, paramIndex, 0, modelField);
          paramIndex++;
        }
        filedIndex++;
      }
      stat.execute();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql);
    } catch (IllegalAccessException se) {
      throw new UserException("Filed Access is not found", "Model : " + clazz.getName());
    } catch (NoSuchFieldException se) {
      throw new UserException("Filed is not found", "Model : " + clazz.getName());
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
    return true;
  }

  public <Model> boolean delete(Connection conn, Model item) throws UserException {
    try {
      DBModelField fieldIDName = getIDField();
      if (fieldIDName == null) {
        throw new UserException("<ID> field not found in class definition", "Model : " + clazz.getName());
      }
      return delete(conn, fieldIDName.dbFieldName + "=" + clazz.getField(fieldIDName.name).get(item));
    } catch (IllegalAccessException se) {
      throw new UserException("Field Access is not found", "Model : " + clazz.getName());
    } catch (NoSuchFieldException se) {
      throw new UserException("Field is not found", "Model : " + clazz.getName());
    }
  }

  public <Model> boolean delete(Connection conn, String where) throws UserException {
    return delete(conn, where, new Object[]{});
  }

  public <Model> boolean delete(Connection conn, String where, Object... args) throws UserException {
    PreparedStatement stat = null;
    String sql = "";
    ResultSet rs = null;
    try {
      sql = "DELETE FROM " + tableName + " " + (where == null || where.equalsIgnoreCase("") ? "" : " WHERE " + where);
      stat = conn.prepareStatement(sql);
      stat = fillStat(sql, stat, args);
      stat.execute();
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "Error:\n" + se);
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
    return true;
  }

  public <Model> Map<String, Map<String, Model>> getMap2(Connection conn, String key1, String key2, String where) throws UserException {
    return getMap2(conn, key1, key2, where, new Object[]{});
  }

  public <Model> Map<String, Map<String, Model>> getMap2(Connection conn, String key1, String key2, String where, Object... args) throws UserException {
    Map<String, Map<String, Model>> result = new TreeMap<String, Map<String, Model>>();
    DBModelField[] f_keys = new DBModelField[]{null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || (modelField.dbFieldName != null && modelField.dbFieldName.equalsIgnoreCase(key1))) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || (modelField.dbFieldName != null && modelField.dbFieldName.equalsIgnoreCase(key2))) {
        f_keys[1] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }

      Map<String, Model> m1 = result.get(key_values[0]);
      if (m1 == null) {
        m1 = new HashMap<String, Model>();
        result.put(key_values[0], m1);
      }
      m1.put(key_values[1], obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, List<Model>>> getMap2InList(Connection conn, String key1, String key2, String where, Object... args) throws UserException {
    Map<String, Map<String, List<Model>>> result = new TreeMap<String, Map<String, List<Model>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || (modelField.dbFieldName != null && modelField.dbFieldName.equalsIgnoreCase(key1))) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || key2.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[1] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }

      Map<String, List<Model>> m1 = result.get(key_values[0]);
      if (m1 == null) {
        m1 = new HashMap<String, List<Model>>();
        result.put(key_values[0], m1);
      }
      List<Model> list_obj = m1.get(key_values[1]);
      if (list_obj == null) {
        list_obj = new ArrayList<Model>();
        m1.put(key_values[1], list_obj);
      }
      list_obj.add(obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, Map<String, List<Model>>>> getMap3InList(Connection conn, String key1, String key2, String key3, String where, Object... args) throws UserException {
    Map<String, Map<String, Map<String, List<Model>>>> result = new TreeMap<String, Map<String, Map<String, List<Model>>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || (modelField.dbFieldName != null && modelField.dbFieldName.equalsIgnoreCase(key1))) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || key2.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[1] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key3) || key3.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[2] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[2] == null) {
      throw new UserException("Error", "Key field '" + key3 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", "", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      Map<String, Map<String, List<Model>>> m0 = result.get(key_values[0]);
      if (m0 == null) {
        m0 = new HashMap<String, Map<String, List<Model>>>();
        result.put(key_values[0], m0);
      }
      Map<String, List<Model>> m1 = m0.get(key_values[1]);
      if (m1 == null) {
        m1 = new HashMap<String, List<Model>>();
        m0.put(key_values[1], m1);
      }
      List<Model> list_obj = m1.get(key_values[2]);
      if (list_obj == null) {
        list_obj = new ArrayList<Model>();
        m1.put(key_values[2], list_obj);
      }
      list_obj.add(obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, Map<String, Model>>> getMap3(Connection conn, String key1, String key2, String key3, String where) throws UserException {
    return getMap3(conn, key1, key2, key3, where, new Object[]{});
  }

  public <Model> Map<String, Map<String, Map<String, Model>>> getMap3(Connection conn, String key1, String key2, String key3, String where, Object... args) throws UserException {
    Map<String, Map<String, Map<String, Model>>> result = new HashMap<String, Map<String, Map<String, Model>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || key1.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || key2.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[1] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key3) || key3.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[2] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[2] == null) {
      throw new UserException("Error", "Key field '" + key3 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", "", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      Map<String, Map<String, Model>> m1 = result.get(key_values[0]);
      if (m1 == null) {
        m1 = new HashMap<String, Map<String, Model>>();
        result.put(key_values[0], m1);
      }
      Map<String, Model> m2 = m1.get(key_values[1]);
      if (m2 == null) {
        m2 = new HashMap<String, Model>();
        m1.put(key_values[1], m2);
      }
      m2.put(key_values[2], obj);
    }
    return result;
  }

  public <Model> void putObjToMap(Map<String, Map<String, Map<String, Model>>> result, String key1, String key2, String key3, Model obj) {
    Map<String, Map<String, Model>> m1 = result.get(key1);
    if (m1 == null) {
      m1 = new HashMap<String, Map<String, Model>>();
      result.put(key1, m1);
    }
    Map<String, Model> m2 = m1.get(key2);
    if (m2 == null) {
      m2 = new HashMap<String, Model>();
      m1.put(key2, m2);
    }
    m2.put(key3, obj);
  }

  public <Model> Map<String, Map<String, Map<String, Map<String, Model>>>> getMap4(Connection conn, String key0, String key1, String key2, String key3, String where) throws UserException {
    return getMap4(conn, key0, key1, key2, key3, where, new Object[]{});
  }

  public <Model> Map<String, Map<String, Map<String, Map<String, Model>>>> getMap4(Connection conn, String key0, String key1, String key2, String key3, String where, Object... args) throws UserException {
    Map<String, Map<String, Map<String, Map<String, Model>>>> result = new HashMap<String, Map<String, Map<String, Map<String, Model>>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null, null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key0) || key0.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key1) || key1.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[1] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || key2.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[2] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key3) || key3.equalsIgnoreCase(modelField.dbFieldName)) {
        f_keys[3] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key0 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[2] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[3] == null) {
      throw new UserException("Error", "Key field '" + key3 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getList(conn, where, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", "", "", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          modelField.fieldAdapter = fieldAdapter;
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      Map<String, Map<String, Map<String, Model>>> m0 = result.get(key_values[0]);
      if (m0 == null) {
        m0 = new HashMap<String, Map<String, Map<String, Model>>>();
        result.put(key_values[0], m0);
      }
      Map<String, Map<String, Model>> m1 = m0.get(key_values[1]);
      if (m1 == null) {
        m1 = new HashMap<String, Map<String, Model>>();
        m0.put(key_values[1], m1);
      }
      Map<String, Model> m2 = m1.get(key_values[2]);
      if (m2 == null) {
        m2 = new HashMap<String, Model>();
        m1.put(key_values[2], m2);
      }
      m2.put(key_values[3], obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>> getMap5(Connection conn, String where, String... keys) throws UserException {
    Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>> result = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Model>>>>>();
    List<Model> list = getList(conn, where);
    for (Model obj : list) {
      String[] key_values = new String[keys.length];
      for (DBModelField modelField : fields) {

        for (int index = 0; index < keys.length; index++) {
          String key = keys[index];
          if (modelField.name.equalsIgnoreCase(key) || key.equalsIgnoreCase(modelField.dbFieldName)) {
            try {
              Field field = clazz.getField(modelField.name);
              DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
              modelField.fieldAdapter = fieldAdapter;
              key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
            } catch (Exception e) {
              throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
            }
          }
        }
      }

      Map<String, Map<String, Map<String, Map<String, Model>>>> m0 = result.get(key_values[0]);
      if (m0 == null) {
        m0 = new HashMap<String, Map<String, Map<String, Map<String, Model>>>>();
        result.put(key_values[0], m0);
      }
      Map<String, Map<String, Map<String, Model>>> m1 = m0.get(key_values[1]);
      if (m1 == null) {
        m1 = new HashMap<String, Map<String, Map<String, Model>>>();
        m0.put(key_values[1], m1);
      }
      Map<String, Map<String, Model>> m2 = m1.get(key_values[2]);
      if (m2 == null) {
        m2 = new HashMap<String, Map<String, Model>>();
        m1.put(key_values[2], m2);
      }
      Map<String, Model> m3 = m2.get(key_values[3]);
      if (m3 == null) {
        m3 = new HashMap<String, Model>();
        m2.put(key_values[3], m3);
      }
      m3.put(key_values[4], obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>>> getMap6(Connection conn, String where, String... keys) throws UserException {
    Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>>> result = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>>>();
    List<Model> list = getList(conn, where);
    for (Model obj : list) {
      String[] key_values = new String[keys.length];
      for (DBModelField modelField : fields) {

        for (int index = 0; index < keys.length; index++) {
          String key = keys[index];
          if (modelField.name.equalsIgnoreCase(key) || key.equalsIgnoreCase(modelField.dbFieldName)) {
            try {
              Field field = clazz.getField(modelField.name);
              DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
              modelField.fieldAdapter = fieldAdapter;
              key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
            } catch (Exception e) {
              throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
            }
          }
        }
      }

      int key = 0;
      Map<String, Map<String, Map<String, Map<String, Map<String, Model>>>>> m00 = result.get(key_values[key]);
      if (m00 == null) {
        m00 = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Model>>>>>();
        result.put(key_values[key], m00);
      }

      key++;
      Map<String, Map<String, Map<String, Map<String, Model>>>> m0 = m00.get(key_values[key]);
      if (m0 == null) {
        m0 = new HashMap<String, Map<String, Map<String, Map<String, Model>>>>();
        m00.put(key_values[key], m0);
      }
      key++;
      Map<String, Map<String, Map<String, Model>>> m1 = m0.get(key_values[key]);
      if (m1 == null) {
        m1 = new HashMap<String, Map<String, Map<String, Model>>>();
        m0.put(key_values[key], m1);
      }
      key++;
      Map<String, Map<String, Model>> m2 = m1.get(key_values[key]);
      if (m2 == null) {
        m2 = new HashMap<String, Map<String, Model>>();
        m1.put(key_values[key], m2);
      }
      key++;
      Map<String, Model> m3 = m2.get(key_values[key]);
      if (m3 == null) {
        m3 = new HashMap<String, Model>();
        m2.put(key_values[key], m3);
      }
      key++;
      m3.put(key_values[key], obj);
    }
    return result;
  }

  public void fillMetaData(Connection conn) {
    /*try {
      if (fields.length > 0 && fields[0].metaData == null) {
        List<DBModelFieldMetaData> meta = DBModelFieldMetaData.dbControl.getList(conn, "RTRIM(TBCREATOR)||'.'||RTRIM(TBNAME)=? order by COLNO", tableName);
        for (DBModelField field : fields) {
          String dbFieldName = field.dbFieldName;
          dbFieldName = dbFieldName.replaceAll("\"", "");
          for (DBModelFieldMetaData fieldMeta : meta) {
            if (fieldMeta.NAME.equalsIgnoreCase(dbFieldName)) {
              field.metaData = fieldMeta;
              break;
            }
          }
        }
      }
    } catch (UserException ue) {
      System.out.println("fillMetaData is Error. " + ue.error + " " + ue.details);
    } catch (Exception e) {
      System.out.println("fillMetaData is Error. " + Tools.traceErrorWithCaption(e));
    }*/
  }
  int obj_count = 0;

  public void execSql(Connection conn, String sql, Object... args) throws UserException {
    PreparedStatement stat = null;
    ResultSet rs = null;

    try {
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
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

  public <Model> Map<String, List<Model>> getMapListWithParams(Connection conn, String key1, Map<String, String> params, String where) throws UserException {
    Map<String, List<Model>> result = new HashMap<String, List<Model>>();
    DBModelField[] f_keys = new DBModelField[]{null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_keys[0] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getListFromMap(conn, where, params);
    for (Model obj : list) {
      String[] key_values = new String[]{""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0, modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      List<Model> rList = result.get(key_values[0]);
      if (rList == null) {
        rList = new ArrayList<Model>();
        result.put(key_values[0], rList);
      }
      rList.add(obj);
    }
    return result;
  }

  public List getListFromMap(Connection conn, String addonWhere, Map<String, String> params) throws UserException {
    return getListWithParam(conn, addonWhere, params);
  }

  public <Model> List<Model> getListWithParam(Connection conn, String addonWhere, Map<String, String> params, Object... args) throws UserException {
    List<Model> result = new ArrayList<Model>();
    PreparedStatement stat = null;
    ResultSet rs = null;
    String sql = getSelectSql(addonWhere);
    sql = sql.replaceAll("\\{ADDON_WHERE\\}", addonWhere);
    if (params != null) {
      for (String name : params.keySet()) {
        String value = params.get(name);
        sql = sql.replaceAll("\\{" + name + "\\}", value);
      }
    }
    try {
      stat = conn.prepareStatement(sql);
      fillStat(sql, stat, args);
      if (fields.length == 0) {
        int count = stat.executeUpdate();
        return result;
      } else {
        rs = stat.executeQuery();
      }
      result = __getListFromRS(rs, sql);
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (Exception e) {
      throw new UserException("Error", "GetList error. Model: " + clazz.getName() + ". " + Tools.traceErrorWithCaption(e));
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
}
