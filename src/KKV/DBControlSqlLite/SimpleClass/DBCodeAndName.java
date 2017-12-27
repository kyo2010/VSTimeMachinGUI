/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.SimpleClass;

/**
 *
 * @author kyo
 */
public class DBCodeAndName {
  public String code;
  public String name;
  public String dop_info = "";
  
  public DBCodeAndName(){}

  public DBCodeAndName(String code, String name) {
    this.code = code;
    this.name = name;
  }
  
  public int getCodeAsInt(){
    try{
      return Integer.parseInt(code);
    }catch(Exception e){}
    return 0;
  }
    
}
