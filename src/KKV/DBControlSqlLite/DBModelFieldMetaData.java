/*** KKV Class Generator V.0.1, based on SYSIBM.SYSCOLUMNS 
 *** This class will be generated automatically.  ***/

package KKV.DBControlSqlLite;

import KKV.Utils.JDEDate;
import java.math.BigDecimal;

public class DBModelFieldMetaData {
  
  public String NAME;   //  VARCHAR
  public String TBNAME;   //  VARCHAR
  public String TBCREATOR;   //  VARCHAR
  public String REMARKS;   //  VARCHAR
  public String COLTYPE;   //  CHAR
  public String NULLS;   //  CHAR
  public String CODEPAGE;   //  SMALLINT
  public String DBCSCODEPG;   //  SMALLINT
  public int LENGTH;   //  SMALLINT
  public int SCALE;   //  SMALLINT
  public int COLNO;   //  SMALLINT
  public long COLCARD;   //  BIGINT
  public String HIGH2KEY;   //  VARCHAR
  public String LOW2KEY;   //  VARCHAR
  public int AVGCOLLEN;   //  INTEGER
  public String KEYSEQ;   //  SMALLINT
  public String TYPENAME;   //  VARCHAR
  public String TYPESCHEMA;   //  VARCHAR
  public String DEFAULT;   //  VARCHAR
  public int LONGLENGTH;   //  INTEGER
  public String LOGGED;   //  CHAR
  public String COMPACT;   //  CHAR
  public String NQUANTILES;   //  SMALLINT
  public String NMOSTFREQ;   //  SMALLINT
  public String COMPOSITE_CODEPAGE;   //  SMALLINT
  public String PARTKEYSEQ;   //  SMALLINT
  public String SOURCE_TABSCHEMA;   //  VARCHAR
  public String SOURCE_TABNAME;   //  VARCHAR
  public String HIDDEN;   //  CHAR
  public String GENERATED;   //  CHAR
  public int INLINE_LENGTH;   //  INTEGER
  public long NUMNULLS;   //  BIGINT
  public int DATAMODEL;   //  INTEGER
  public String SUB_COUNT;   //  SMALLINT
  public String SUB_DELIM_LENGTH;   //  SMALLINT
  public String IDENTITY;   //  CHAR
  public String COMPRESS;   //  CHAR
  public String AVGDISTINCTPERPAGE;   //  DOUBLE
  public String PAGEVARIANCERATIO;   //  DOUBLE
  public String IMPLICITVALUE;   //  VARCHAR
  public int SECLABELID;   //  INTEGER
  
  /** Constructor */ 
  public DBModelFieldMetaData() {
  };
  
  public static DBModelControl<DBModelFieldMetaData> dbControl = new DBModelControl<DBModelFieldMetaData>(DBModelFieldMetaData.class, "SYSIBM.SYSCOLUMNS", new DBModelField[]{
    new DBModelField("NAME").setDbFieldName("\"NAME\"").setAutoTrim(true),
    new DBModelField("TBNAME").setDbFieldName("\"TBNAME\"").setAutoTrim(true),
    new DBModelField("TBCREATOR").setDbFieldName("\"TBCREATOR\"").setAutoTrim(true),
    new DBModelField("REMARKS").setDbFieldName("\"REMARKS\""),
    new DBModelField("COLTYPE").setDbFieldName("\"COLTYPE\"").setAutoTrim(true),
    new DBModelField("NULLS").setDbFieldName("\"NULLS\""),
    new DBModelField("CODEPAGE").setDbFieldName("\"CODEPAGE\""),
    new DBModelField("DBCSCODEPG").setDbFieldName("\"DBCSCODEPG\""),
    new DBModelField("LENGTH").setDbFieldName("\"LENGTH\""),
    new DBModelField("SCALE").setDbFieldName("\"SCALE\""),
    new DBModelField("COLNO").setDbFieldName("\"COLNO\""),
    new DBModelField("COLCARD").setDbFieldName("\"COLCARD\""),
    new DBModelField("HIGH2KEY").setDbFieldName("\"HIGH2KEY\""),
    new DBModelField("LOW2KEY").setDbFieldName("\"LOW2KEY\""),
    new DBModelField("AVGCOLLEN").setDbFieldName("\"AVGCOLLEN\""),
    new DBModelField("KEYSEQ").setDbFieldName("\"KEYSEQ\""),
    new DBModelField("TYPENAME").setDbFieldName("\"TYPENAME\""),
    new DBModelField("TYPESCHEMA").setDbFieldName("\"TYPESCHEMA\""),
    new DBModelField("DEFAULT").setDbFieldName("\"DEFAULT\""),
    new DBModelField("LONGLENGTH").setDbFieldName("\"LONGLENGTH\""),
    new DBModelField("LOGGED").setDbFieldName("\"LOGGED\""),
    new DBModelField("COMPACT").setDbFieldName("\"COMPACT\""),
    new DBModelField("NQUANTILES").setDbFieldName("\"NQUANTILES\""),
    new DBModelField("NMOSTFREQ").setDbFieldName("\"NMOSTFREQ\""),
    new DBModelField("COMPOSITE_CODEPAGE").setDbFieldName("\"COMPOSITE_CODEPAGE\""),
    new DBModelField("PARTKEYSEQ").setDbFieldName("\"PARTKEYSEQ\""),
    new DBModelField("SOURCE_TABSCHEMA").setDbFieldName("\"SOURCE_TABSCHEMA\""),
    new DBModelField("SOURCE_TABNAME").setDbFieldName("\"SOURCE_TABNAME\""),
    new DBModelField("HIDDEN").setDbFieldName("\"HIDDEN\""),
    new DBModelField("GENERATED").setDbFieldName("\"GENERATED\""),
    new DBModelField("INLINE_LENGTH").setDbFieldName("\"INLINE_LENGTH\""),
    new DBModelField("NUMNULLS").setDbFieldName("\"NUMNULLS\""),
    new DBModelField("DATAMODEL").setDbFieldName("\"DATAMODEL\""),
    new DBModelField("SUB_COUNT").setDbFieldName("\"SUB_COUNT\""),
    new DBModelField("SUB_DELIM_LENGTH").setDbFieldName("\"SUB_DELIM_LENGTH\""),
    new DBModelField("IDENTITY").setDbFieldName("\"IDENTITY\""),
    new DBModelField("COMPRESS").setDbFieldName("\"COMPRESS\""),
    new DBModelField("AVGDISTINCTPERPAGE").setDbFieldName("\"AVGDISTINCTPERPAGE\""),
    new DBModelField("PAGEVARIANCERATIO").setDbFieldName("\"PAGEVARIANCERATIO\""),
    new DBModelField("IMPLICITVALUE").setDbFieldName("\"IMPLICITVALUE\""),
    new DBModelField("SECLABELID").setDbFieldName("\"SECLABELID\""),
  });
  
}
