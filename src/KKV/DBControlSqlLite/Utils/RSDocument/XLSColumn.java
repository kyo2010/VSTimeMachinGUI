package KKV.DBControlSqlLite.Utils.RSDocument;

/**
 * User: Voloshin Date: 31.07.12 Time: 17:17
 */
import KKV.DBControlSqlLite.DBFieldAdapters.DBFieldAdapter;
import KKV.DBControlSqlLite.DBModelControl;
import KKV.DBControlSqlLite.DBModelField;
import KKV.DBControlSqlLite.UserException;
import KKV.DBControlSqlLite.Utils.XMLString;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XLSColumn {

  public String colName;
  public int col = -1;
  public String findStrings[];
  public int shiftX;
  public int shiftY;
  public boolean isUsed;
  public boolean visibled = true;
  public Class type;
  public String dbColumn;
  public String headerCellId = "";
  public Object column_object = null; // for setup specific XLC Column Object
  public String formula = null; // Special formula if you want
  public List<Integer> formulaTypes = null; // Special formula if you want
  public HashMap<Integer,String> formulas = new HashMap<Integer,String>(); // Special formula if you want
  public Map<String,String> propertyString = new HashMap<String,String>();
  public Integer rwsq = -1;
  public int[] rwsqs = null;
  public String width = "";
  public String cell_id = null;
  public boolean replaseHTMLTags = false;
  public boolean skipSearch = false;
  public int groupLevel = 0;
  public int startSeacrhCol = -1;
  public int startSeacrhRow = -1;
  public boolean isNessesary = true;
  public boolean prolongateIfEmpty = false;
  public int statementIndex = -1;
  public String ArrayPropertyName = "";
  public int ArrayIndex = -1;
  public String propertyName = null;  
  public boolean beforeCellMustBeEmpty = false;
  public boolean LF_FindAfterLinkageCol = true;
  public XLSColumn linckageCol = null;
  public boolean useMarginRows = false;
  public int aprd_shift = 0;
  public int[] sumShiftPrds = null;
  public String upperCaption = null;
  public String upperCaption2 = null;
  public DBModelField mf = null;
  public Map<String, Object> column_objects = new HashMap<String, Object>();
  public int scale = 1;
  public int numberOfRepeat = 1;
  public int currentNumberOfRepeat = 1;  
  
  public XLSColumn(XLSColumn parent){
    this.colName =  parent.colName;
    this.col = parent.col;
    this.findStrings = new String[parent.findStrings.length];
    for (int i =0;i<parent.findStrings.length;i++)
      this.findStrings[i] = parent.findStrings[i];
    this.shiftX = parent.shiftX;
    this.shiftY = parent.shiftY;
    this.isUsed = parent.isUsed;
    this.visibled = parent.visibled;
    this.type =  parent.type;
    this.dbColumn =  parent.dbColumn;
    this.headerCellId = parent.headerCellId;
    this.column_object = parent.column_object;
    this.formula =  parent.formula; // Special formula if you want
    this.formulaTypes   = parent.formulaTypes; // Special formula if you want
    this.formulas =   parent.formulas; // Special formula if you want
    this.rwsq =  parent.rwsq;
    this.rwsqs = parent.rwsqs;
    this.width = parent.width;
    this.cell_id = parent.cell_id;
    this.replaseHTMLTags = parent.replaseHTMLTags;
    this.skipSearch = parent.skipSearch;
    this.groupLevel = parent.groupLevel;
    this.startSeacrhCol = parent.startSeacrhCol;
    this.startSeacrhRow = parent.startSeacrhRow;
    this.isNessesary = parent.isNessesary;
    this.prolongateIfEmpty = parent.prolongateIfEmpty;
    this.statementIndex = parent.statementIndex;
    this.ArrayPropertyName = parent.ArrayPropertyName;
    this.ArrayIndex = parent.ArrayIndex;
    this.propertyName = parent.propertyName;  
    this.beforeCellMustBeEmpty = parent.beforeCellMustBeEmpty;
    this.LF_FindAfterLinkageCol = parent.LF_FindAfterLinkageCol;
    this.linckageCol = parent.linckageCol;
    this.useMarginRows = parent.useMarginRows;
    this.aprd_shift = parent.aprd_shift;
    this.sumShiftPrds = parent.sumShiftPrds;
    this.upperCaption = parent.upperCaption;
    this.upperCaption2 = parent.upperCaption2;
    this.mf = parent.mf;
    this.column_objects = parent.column_objects;
    this.scale  = parent.scale;
    this.numberOfRepeat = parent.numberOfRepeat;
    this.currentNumberOfRepeat = parent.currentNumberOfRepeat;
  }        
          
  public static List<XLSColumn> copy(List<XLSColumn> src){
    List<XLSColumn> cols = new ArrayList<XLSColumn>();
    for (XLSColumn col : src){
      cols.add(new XLSColumn(col));
    }
    return cols;
  }    
  
  public static XLSColumn[] copy(XLSColumn[] src){
    XLSColumn[] cols = new XLSColumn[src.length];
    for (int i =0; i<src.length; i++){
      cols[i] = new XLSColumn(src[i]);
    }
    return cols;
  }    
  
  public XLSColumn setScale(int scale){
    this.scale = scale;
    return this;
  }
  
  public XLSColumn setDBModelField(DBModelField mf){
    this.mf = mf;
    return this;
  }    
  
  public XLSColumn setAPrdShift(int aprd_shift){
    this.aprd_shift = aprd_shift;
    return this;
  }
  
  public XLSColumn setSumShiftPrds(int[] sumShiftPrds){
    this.sumShiftPrds = sumShiftPrds;
    return this;
  }
  
  public XLSColumn setUpperCaption(String upperCaption){
    this.upperCaption = upperCaption;
    return this;
  }
  
  public XLSColumn setUpperCaption2(String upperCaption2){
    this.upperCaption2 = upperCaption2;
    return this;
  }
  
  public XLSColumn setNumberOfRepeat(int numberOfRepeat){
    this.numberOfRepeat = numberOfRepeat;
    return this;
  }

  public XLSColumn setArrayIndex(String ArrayPropertyName, int ArrayIndex) {
    this.ArrayIndex = ArrayIndex;
    this.ArrayPropertyName = ArrayPropertyName;
    return this;
  }    

  public XLSColumn(String colName, String[] findStr) {
    this(colName, findStr, 0, 0, "", String.class);
  }
  
  public XLSColumn(String colName, int rwsq) {
    this(colName, new String[] { colName } );
    this.rwsq = rwsq;
  }

  public XLSColumn setLinkageCol(XLSColumn linckageCol) {
    this.linckageCol = linckageCol;
    LF_FindAfterLinkageCol = true;
    return this;
  }
    
  public XLSColumn setFindString(String[] findStr) {
    this.findStrings = findStr;
    return this;
  }
  
   public XLSColumn setPropertyName(String propertyName) {
    this.propertyName = propertyName;
    return this;
  }
   
  public XLSColumn setPropertyString(String propertyName, String propertyValue) {
    propertyString.put(propertyName, propertyValue);
    return this;
  }
  
  public String getPropertyString(String propertyName) {
    String propertyValue = propertyString.get(propertyName);
    if (propertyValue==null) propertyValue = "";
    return propertyValue;
  }
  
  public XLSColumn setFindString(String findStr) {
    this.findStrings = new String[] {findStr};
    return this;
  }
  
  public XLSColumn setSkipSearch(boolean skipSearch) {
    this.skipSearch = skipSearch;
    return this;
  }
  
  public XLSColumn setPleaseUseMarginRows(boolean useMarginRows) {
    this.useMarginRows = useMarginRows;
    return this;
  }
  
  public XLSColumn setBeforeCellMustBeEmpty(boolean beforeCellMustBeEmpty) {
    this.beforeCellMustBeEmpty = beforeCellMustBeEmpty;
    return this;
  }


  public XLSColumn setProlongateIfEmpty(boolean prolongateIfEmpty) {
    this.prolongateIfEmpty = prolongateIfEmpty;
    return this;
  }

  public XLSColumn setGroupLevel(int groupLevel) {
    this.groupLevel = groupLevel;
    return this;
  }

  public XLSColumn setStartSeacrhCol(int startSeacrhCol) {
    this.startSeacrhCol = startSeacrhCol;
    return this;
  }
  
  public XLSColumn setStartSeacrhRow(int startSeacrhRow) {
    this.startSeacrhRow = startSeacrhRow;
    return this;
  }

  public XLSColumn setIsNessesary(boolean isNessesary) {
    this.isNessesary = isNessesary;
    return this;
  }

  /**
   * If use=true then <br> tags will be keeps
   */
  public XLSColumn setReplaseHTMLTags(boolean useHtmTag) {
    this.replaseHTMLTags = useHtmTag;
    return this;
  }

  public String getColCaption() {
    String result = "";
    if (findStrings != null && findStrings.length > 0) {
      result = findStrings[0];
      if (replaseHTMLTags) {
        result = XMLString.replaceSpCharForSave(result);
      }
    }
    return result;
  }

  public XLSColumn(String colName,
          String findStrings[],
          String dbColumn,
          Class type) {
    this.colName = colName;
    this.findStrings = findStrings;
    shiftX = 0;
    shiftY = 0;
    isUsed = false;
    this.type = type;
    this.dbColumn = dbColumn;
  }

  public XLSColumn setFormula(String formula) {
    this.formula = formula;
    return this;
  }
  
  public XLSColumn setFormulaType(int ...formulaTypes) {
    this.formulaTypes = new ArrayList<Integer>();
    for (int type : formulaTypes)
      this.formulaTypes.add(type);
    return this;
  }
  
   public boolean isExistFormulaType(int formulaType) {
    if (formulaTypes==null) return false;
    for (int type : formulaTypes)
      if (type==formulaType) return true;
    return false;
  }
  
  public XLSColumn setFormula(int type, String formula) {
    this.formulas.put(type,formula);
    return this;
  }
  
  public XLSColumn setFormula(String formula, int... types) {
    for (int type:types){
      this.formulas.put(type,formula);
    }
    return this;
  }
  
  public String getFormula(int type) {
    return formulas.get(type);
  }

  public XLSColumn setWidth(String width) {
    this.width = width;
    return this;
  }
  
  public XLSColumn setWidth(int width) {
    this.width = ""+width;
    return this;
  }

  public XLSColumn setRwsq(int rwsq) {
    this.rwsq = rwsq;
    return this;
  }
    
  public XLSColumn setRwsqs(int ... rwsqs) {
    this.rwsqs = rwsqs;
    return this;
  }

  public XLSColumn setShiftX(int shiftX) {
    this.shiftX = shiftX;
    return this;
  }

  public XLSColumn setShiftY(int shiftY) {
    this.shiftY = shiftY;
    return this;
  }

  public XLSColumn(String colName,
          String findStrings[],
          int shiftX, int shiftY,
          String dbColumn, Class type) {
    this.colName = colName;
    this.findStrings = findStrings;
    this.shiftX = shiftX;
    this.shiftY = shiftY;
    isUsed = false;
    this.type = type;
  }

  static public void clearColumns(XLSColumn[] columns) {
    for (XLSColumn c : columns) {
      c.isUsed = false;
      c.currentNumberOfRepeat = 1;
      c.col = -1;
    }
  }

  static public void clearColumns(List<XLSColumn> columns) {
    for (XLSColumn c : columns) {
      c.isUsed = false;
      c.col = -1;
    }
  }

  static public int getColumnIndex(XLSColumn[] columns, String colName) {
    for (XLSColumn c : columns) {
      if (c.colName.equalsIgnoreCase(colName)) {
        return c.col;
      }
    }
    throw new RuntimeException("Column '" + colName + "' wasn't found... '");
  }

  static public int getColumnIndex(List<XLSColumn> columnList, String colName) {
    for (XLSColumn c : columnList) {
      if (c.colName.equalsIgnoreCase(colName)) {
        return c.col;
      }
    }
    throw new RuntimeException("Column '" + colName + "' wasn't found... '");
  }
  
  public static XLSColumn getColumn(List<XLSColumn> columnList, String colName) {
    for (XLSColumn c : columnList) {
      if (c.colName.equalsIgnoreCase(colName)) {
        return c;
      }
    }
    throw new RuntimeException("Column '" + colName + "' wasn't found... '");
  }
  
  public static XLSColumn getColumn(XLSColumn[] columnList, String colName) {
    for (XLSColumn c : columnList) {
      if (c.colName.equalsIgnoreCase(colName)) {
        return c;
      }
    }
    throw new RuntimeException("Column '" + colName + "' wasn't found... '");
  }

  public XLSColumn setVisibled(boolean visibled) {
    this.visibled = visibled;
    return this;
  }

 /* public XLSColumn setCell_ID(String cell_id) {
    this.cell_id = cell_id;
    return this;
  }*/
  
  public XLSColumn setCellId(String cell_id) {
    this.cell_id = cell_id;
    return this;
  }

  public XLSColumn setStatementIndex(int statementIndex) {
    this.statementIndex = statementIndex;
    return this;
  }

  public XLSColumn setHeaderCellIdIndex(String headerCellId) {
    this.headerCellId = headerCellId;
    return this;
  }

  public XLSColumn setColumnObject(Object obj) {
    column_object = obj;
    return this;
  }
  
  public XLSColumn setColumnObject(String propName, Object obj) {
    column_objects.put(propName,obj);
    return this;
  }

  public static boolean areAllFound(XLSColumn[] columns) {
    for (XLSColumn col : columns) {
      if (!col.skipSearch && !col.isUsed && col.isNessesary) {
        return false;
      }
    }
    return true;
  }

  public static boolean areAllFoundAndThrows(XLSColumn[] columns) throws UserException {
    for (XLSColumn col : columns) {
      if (!col.skipSearch && col.isNessesary) {
        if (!col.isUsed && col.findStrings.length > 0) {
          throw new UserException("Parsing file is error", "Column name='" + col.colName + "' caption='" + col.findStrings[0] + "' is not found in file.");
        }
      }
    }
    return true;
  }

  public static boolean areAllFound(List<XLSColumn> columns) throws UserException {
    for (XLSColumn col : columns) {
      if (!col.skipSearch && !col.isUsed && col.isNessesary) {
        return false;
      }
    }
    return true;
  }
  
  public static XLSColumn getEmptyColumn(List<XLSColumn> columns) throws UserException {
    int index= 0;
    for (XLSColumn col : columns) {
      if (!col.skipSearch && !col.isUsed && col.isNessesary) {
        return col;
      }
      index++;
    }
    return null;
  }
  
  public static boolean areAllFoundAndThrows(String sheetName, List<XLSColumn> columns) throws UserException {
    int i = 0;
    for (XLSColumn col : columns) {
      if (!col.skipSearch && col.isNessesary) {
        if (!col.isUsed && col.findStrings.length > 0) {
          throw new UserException("Parsing file is error", "Sheet name:'"+sheetName+"'. Column '" + col.findStrings[0] + "' is not found in file to detect '" + col.colName + "' [" + i + "]");
        }
      }
      i++;
    }
    return true;
  }
  
  public static boolean areAllFoundAndThrows(String sheetName, XLSColumn[] columns) throws UserException {
    int i = 0;
    for (XLSColumn col : columns) {
      if (!col.skipSearch && col.isNessesary) {
        if (!col.isUsed && col.findStrings.length > 0) {
          UserException e = new UserException("Parsing file is error", "Sheet name:'"+sheetName+"'. Column '" + col.findStrings[0] + "' is not found in file to detect '" + col.colName + "' [" + i + "] number of repeat:"+col.numberOfRepeat);
          throw e;
        }
      }
      i++;
    }
    return true;
  }
  
  String oldValue = "";
  public void setValueToObj(Object obj, String fieldName, String value) throws UserException{
    try {
      Field field = obj.getClass().getField(fieldName);
      
      for (DBFieldAdapter fieldAdapter : DBModelControl.dbFieldAdapters) {
        if (fieldAdapter.isValidClass(field)) {
          value = value.replaceAll("''", "'");
          if (prolongateIfEmpty){
            if(value.equalsIgnoreCase("")) {
              value = oldValue;
            }
            oldValue = value;
          }                            
          fieldAdapter.setField(field, obj, fieldName, value, ArrayIndex,mf);
          break;
        }
      }
    } catch (UserException ue){
      throw ue;
    } catch (Exception e) {
    }
  }

 
}