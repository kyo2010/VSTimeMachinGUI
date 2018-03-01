/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.DBFieldAdapters.DBFieldAdapter;
import KKV.Utils.Tools;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kyo
 */
public class DBSelectControl<Model> extends DBIControl {

  public String sql; 

  public <Model> List<Model> getList(Connection conn, String addonWhere, Object... args) throws UserException {
    return getListWithParam(conn, addonWhere, null, args);
  }
    
  public <Model> DBSelectControl(Class clazz, String sql, DBModelField[] fields) {
    this.clazz = clazz;
    this.sql = sql;
    this.fields = fields;
  }
  
  public DBSelectControl setInitSQl(String initSQl) {
    this.initSQl = initSQl;
    return this;
  }

  public <Model> List<Model> getListWithParam(Connection conn, String addonWhere, Map<String, String> params, Object... args) throws UserException {
    List<Model> result = new ArrayList<Model>();
    PreparedStatement stat = null;
    ResultSet rs = null;
    Model item = null;
    String newSql = sql;
    newSql = newSql.replaceAll("\\{ADDON_WHERE\\}", addonWhere);
    if (params != null) {
      for (String name : params.keySet()) {
        String value = params.get(name);
        newSql = newSql.replaceAll("\\{" + name + "\\}", value);
      }
    }

    try {
      stat = conn.prepareStatement(newSql);
      fillStat(newSql, stat, args);
      if (fields.length == 0) {
        int count = stat.executeUpdate();
        return result;
      } else {
        rs = stat.executeQuery();
      }
      while (rs.next()) {
        try {
          item = (Model) clazz.newInstance();
        } catch (Exception e) {
          throw new UserException("Default constructor is not found", "Model : " + clazz.getName());
        }

        int rsIndex = 1;
        for (DBModelField modelField : fields) {
          if (modelField.fieldTarget==DBModelField.FT_SKIP) continue;
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          fieldAdapter.loadFromRS(field, item, rs, rsIndex, modelField, 0);
          rsIndex++;
        }
        result.add(item);


      }
    } catch (SQLException se) {
      throw new UserException("Sql is error", "SQL : " + sql + "\nError message: \n" + se.toString());
    } catch (IllegalAccessException se) {
      throw new UserException("Field Access is not found", "Model : " + clazz.getName());
    } catch (NoSuchFieldException se) {
      throw new UserException("Field is not found", "Model : " + clazz.getName());
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
  
    public <Model> Map<String, List<Model>> getMapListWithParams(Connection conn, String key1,  Map<String, String> params, String where) throws UserException {
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
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0,modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      List<Model> rList = result.get(key_values[0]);
      if (rList==null){
        rList = new ArrayList<Model>();
        result.put(key_values[0], rList);
      }
      rList.add(obj);
    }
    return result; 
  }
  
  public <Model> List<Model> getListFromMap(Connection conn, String addonWhere, Map<String, String> params) throws UserException {
    return getListWithParam(conn, addonWhere, params);
  }

  public <Model> Map<String, Map<String, Map<String, Model>>> getMap3(Connection conn, String key1, String key2, String key3, String where, Object... args) throws UserException {
    return getMap3(conn,key1,key2,key3,null,where,args);
  }
  
  public <Model> Map<String, Map<String, Map<String, Model>>> getMap3UseParamMap(Connection conn, String key1, String key2, String key3, Map<String, String> params, Object... args) throws UserException {
    return getMap3(conn,key1,key2,key3,params,"",args);
  }
  
  public <Model> Map<String, Map<String, Map<String, Model>>> getMap3(Connection conn, String key1, String key2, String key3, Map<String, String> params, String where, Object... args) throws UserException {
    Map<String, Map<String, Map<String, Model>>> result = new HashMap<String, Map<String, Map<String, Model>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || modelField.dbFieldName.equalsIgnoreCase(key2)) {
        f_keys[1] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key3) || modelField.dbFieldName.equalsIgnoreCase(key3)) {
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

    List<Model> list = getListWithParam(conn, where, params, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", "", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0,modelField).trim();
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

  public <Model> Map<String, Map<String, Model>> getMap2(Connection conn, String key1, String key2, String where, Object... args) throws UserException {
    return getMap2Ex(conn,null,key1,key2,where,args);
  }  
    
  public <Model> Map<String, Map<String, Model>> getMap2Ex(Connection conn, Map<String, String> params, String key1, String key2, String where, Object... args) throws UserException {
    Map<String, Map<String, Model>> result = new HashMap<String, Map<String, Model>>();
    DBModelField[] f_keys = new DBModelField[2];
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || modelField.dbFieldName.equalsIgnoreCase(key2)) {
        f_keys[1] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }
    if (f_keys[1] == null) {
      throw new UserException("Error", "Key field '" + key2 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getListWithParam(conn, where, params, args);
    for (Model obj : list) {
      String[] key_values = new String[]{"", ""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0,modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      Map<String, Model> m2 = result.get(key_values[0]);
      if (m2 == null) {
        m2 = new HashMap<String, Model>();
        result.put(key_values[0], m2);
      }
      m2.put(key_values[1], obj);
    }
    return result;
  }

  
  public <Model> Map<String, Model> getMapWithParams(Connection conn, String key1,  Map<String, String> params, String where) throws UserException {
    Map<String, Model> result = new HashMap<String, Model>();
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
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0,modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      result.put(key_values[0], obj);
    }
    return result;
  }
    
  public <Model> Map<String, Model> getMap(Connection conn, String key1, String where, Object... args) throws UserException {
    return getMapEx(conn, null, key1, where, args);
  }
  
  public <Model> Map<String, Model> getMapEx(Connection conn, Map<String, String> params, String key1, String where, Object... args) throws UserException {
    Map<String, Model> result = new HashMap<String, Model>();
    DBModelField[] f_keys = new DBModelField[]{null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_keys[0] = modelField;
      }
    }
    if (f_keys[0] == null) {
      throw new UserException("Error", "Key field '" + key1 + "' is not found in object " + clazz.getSimpleName());
    }

    List<Model> list = getListWithParam(conn, where, params, args);
    for (Model obj : list) {
      String[] key_values = new String[]{""};
      int index = 0;
      for (DBModelField modelField : f_keys) {
        try {
          Field field = clazz.getField(modelField.name);
          DBFieldAdapter fieldAdapter = getSuitableAdapter(field);
          key_values[index] = fieldAdapter.getField(field, obj, modelField.name, 0,modelField).trim();
          index++;
        } catch (Exception e) {
          throw new UserException("Error", "I can't read a field name '" + modelField.name + "' in obj:" + clazz.getSimpleName());
        }
      }
      result.put(key_values[0], obj);
    }
    return result;
  }

  public <Model> Map<String, Map<String, Map<String, Map<String, Model>>>> getMap4(Connection conn, String key0, String key1, String key2, String key3, String where, Object... args) throws UserException {
    Map<String, Map<String, Map<String, Map<String, Model>>>> result = new HashMap<String, Map<String, Map<String, Map<String, Model>>>>();
    DBModelField[] f_keys = new DBModelField[]{null, null, null, null};
    for (DBModelField modelField : fields) {
      if (modelField.name.equalsIgnoreCase(key0) || modelField.dbFieldName.equalsIgnoreCase(key0)) {
        f_keys[0] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key1) || modelField.dbFieldName.equalsIgnoreCase(key1)) {
        f_keys[1] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key2) || modelField.dbFieldName.equalsIgnoreCase(key2)) {
        f_keys[2] = modelField;
      }
      if (modelField.name.equalsIgnoreCase(key3) || modelField.dbFieldName.equalsIgnoreCase(key3)) {
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
}
