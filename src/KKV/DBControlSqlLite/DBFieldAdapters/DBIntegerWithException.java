/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.DBControlSqlLite.DBFieldAdapters;

/**
 *
 * @author kyo
 */
public class DBIntegerWithException {
  public Integer data = null;
  public boolean isError = false;
  
  public DBIntegerWithException(Integer data){
    this.data = data;
  }

  @Override
  public boolean equals(Object obj) {
    DBIntegerWithException o = (DBIntegerWithException) obj;
    return data.equals(o.data);
  }

  @Override
  public String toString() {
    return ""+data;
  }
  
  
  
}
