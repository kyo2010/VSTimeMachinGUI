/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite;

import KKV.Utils.UserException;
import KKV.DBControlSqlLite.DBFieldAdapters.DBFieldAdapter;
import KKV.DBControlSqlLite.DBFieldAdapters.IDBValueHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kimlaev
 */
public class DBModelField {
  public String name;
  public String dbFieldName = null;
  public Class  fieldType = null;
  public String dbFieldType = "NOT_DETECTED";
  public boolean autoTrim = true;
  public String[] joinFrom = new String[0];
  public String[] joinTo = new String[0];
  public DBModelControl joinDBControl = null;
  public String dateFormat = "";
  public boolean isArray = false;
  public int maxArraySize = 1;
  public int joinType = 0; // Join = 0, Left Join = 1
  public boolean hide = false; // use for Automation master, Don't show field in xls
  public DBModelFieldMetaData metaData = null;
  /** Using in Quick reports */
  public String caption = null;    // Use for sample cation
  public String subCaption =null;  // use subCaption with shift_xls_col from caption
  public int shift_xls_col = 0; 
  public boolean showZero = true;
  public boolean ProlongatePreviousValue = false;
  public boolean useMarginRowsWhenUploading = false;
  public int width = -1;
  public String captionID = null;
  public boolean isMainColumnForParsing = false;
  public Object columnObject = null;
  public String SQLCalculatedFormula = null;
  public ArrayList<String> alias = new ArrayList<String>();
  public int NUMBER_OF_DECIMAL = -1;
  public String CellID = null;
  public IDBValueHandler dbValueHandle = null;
  public boolean supportSpaceValue = false;
  public String formula = null;
  
  // Field ID
  public int rwsq = -1;

  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField setCaptionID(String captionID) {
    this.captionID = captionID;
    return this;
  }
  
  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField setFormula(String formula) {
    this.formula = formula;
    return this;
  }

  public DBModelField setSupportSpaceValue(boolean supportSpaceValue) {
    this.supportSpaceValue = supportSpaceValue;
    return this;
  }    
  
  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField setCellID(String CellID) {
    this.CellID = CellID;
    return this;
  }
  
  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField setDBValueHandle(IDBValueHandler dbValueHandle) {
    this.dbValueHandle = dbValueHandle;
    return this;
  }
  
  public DBModelField setRwsq(int rwsq) {
    this.rwsq = rwsq;
    return this;
  }
  
  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField setSQLCalculatedFormula(String SQLCalculatedFormula) {
    this.SQLCalculatedFormula = SQLCalculatedFormula;
    fieldTarget = FT_CALULATED_FIELD;
    return this;
  }
  
  public DBModelField setNumberOfDecimal(int NUMBER_OF_DECIMAL) {
    this.NUMBER_OF_DECIMAL = NUMBER_OF_DECIMAL;
    return this;
  }
  
  
  /** Set Caption ID, Example = {CAP_ID} */
  public DBModelField addAlias(String alias) {
    this.alias.add(alias);
    return this;
  }
  
  /** Set Flag isMainColumnForParsing  */
  public DBModelField isMainColumnForParsing(boolean isMainColumnForParsing) {
    this.isMainColumnForParsing = isMainColumnForParsing;
    return this;
  }  
       
  public DBModelField setPleaseUseMarginRowsWhenUploading(boolean useMarginRowsWhenUploading) {
    this.useMarginRowsWhenUploading = useMarginRowsWhenUploading;
    return this;
  }

  public DBModelField setWidth(int width) {
    this.width = width;
    return this;
  }
  
  public DBModelField setColumnObject(Object columnObject) {
    this.columnObject = columnObject;
    return this;
  }
  
  public DBModelField setCaption(String caption) {
    this.caption = caption;
    return this;
  }
  
  public int numberOfRepeat = 1;
  public DBModelField setNumberOfRepeat(int numberOfRepeat) {
    this.numberOfRepeat = numberOfRepeat;
    return this;
  }
  

  public DBModelField setShowZero(boolean showZero) {
    this.showZero = showZero;
    return this;
  }
  
  public DBModelField setProlongatePreviousValue(boolean ProlongatePreviousValue) {
    this.ProlongatePreviousValue = ProlongatePreviousValue;
    return this;
  }

  public DBModelField setShift_xls_col(int shift_xls_col) {
    this.shift_xls_col = shift_xls_col;
    return this;
  }

  public DBModelField setSubCaption(String subCaption) {
    this.subCaption = subCaption;
    return this;
  }
      
  /*public DBModelField setArrayType(int maxArraySize) {
    this.maxArraySize = maxArraySize;
    this.isArray = true;   
    return this;
  }*/
  
  public final static int FT_COMMON = 0;
  public final static int FT_AUTOINCREMENT = 1;   // it's ID field !!!
  public final static int FT_SKIP_INSERT = 2;  
  public final static int FT_SKIP_UPDATE = 3;  
  public final static int FT_SKIP = 5;
  public final static int FT_JOIN_FIELD = 4;  
  public final static int FT_SKIP_INSERT_UPDATE = 6;
  public final static int FT_CALULATED_FIELD = 7;
  public final static int FT_ARRAY = 8;
  
  public int FT_ARRAY_SIZE = -1;
  public boolean deleteFirstZero = false;
  
  public DBFieldAdapter fieldAdapter = null;
  
  public int fieldTarget = FT_COMMON;

  public DBModelField(String name) {
    this.name = name;
    this.dbFieldName = name;
  } 
  
  public DBModelField(String name, Class fieldType) {
    this.name = name;
    this.dbFieldName = name;
    this.fieldType = fieldType;
  }

  public DBModelField setFieldTarget(int fieldTarget) {
    this.fieldTarget = fieldTarget;
    return this;
  } 
  
  public DBModelField setFieldGenerateID() {
    this.fieldTarget = DBModelField.FT_AUTOINCREMENT;
    return this;
  }
  
  public DBModelField deleteFirstZero() {
    deleteFirstZero = true;
    return this;
  }
  
  public DBModelField setFieldASArray(int arraySize) {
    this.fieldTarget = FT_ARRAY;
    this.FT_ARRAY_SIZE = arraySize;
    this.maxArraySize = arraySize;
    this.isArray = true;
    return this;
  }
  
  public DBModelField setAutoIncrement() {
    this.fieldTarget = FT_AUTOINCREMENT;
    return this;
  }
  
  public String getJoinType(){
    if (joinType==1) return "LEFT JOIN";
    return "JOIN";
  }
  
  public DBModelField setJoin(DBModelControl joinDBControl ,String[] joinFrom, String[] joinTo) {
    this.joinFrom = joinFrom;
    this.joinTo = joinTo;
    this.joinDBControl = joinDBControl;
    this.fieldTarget = FT_JOIN_FIELD;
    joinType = 0;
    return this;
  } 
  
  public DBModelField setLeftJoin(DBModelControl joinDBControl ,String[] joinFrom, String[] joinTo) {
    this.joinFrom = joinFrom;
    this.joinTo = joinTo;
    this.joinDBControl = joinDBControl;
    this.fieldTarget = FT_JOIN_FIELD;
    joinType = 1;
    return this;
  } 

  public DBModelField setDbFieldName(String dbFieldName) {
    this.dbFieldName = dbFieldName;
    return this;
  }
  
  public DBModelField isHide(boolean hide) {
    this.hide= hide;
    return this;
  }

  public DBModelField setAutoTrim(boolean autoTrim) {
    this.autoTrim = autoTrim;
    return this;
  }
  
  public DBModelField setAutoTrim() {
    this.autoTrim = true;
    return this;
  }

  public DBModelField setDateFormat(String yyyymmdD) {
    dateFormat = yyyymmdD;
    return this;
  }
  
  public String getValue(Object model) throws NoSuchFieldException, UserException, IllegalAccessException{    
    return fieldAdapter.getField(model.getClass().getField(name), model, name, maxArraySize, this);
  }
    
}
