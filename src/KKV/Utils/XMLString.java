package KKV.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: asea1
 * Date: 19.07.2006
 * Time: 18:57:59
 * To change this template use File | Settings | File Templates.
 */
public class XMLString {

  public static final String UTF8_BOM = "\uFE00\uFF00";

  static synchronized public String replaceSpCharForSave(String info) {    
    info = info.replaceAll("&", "&amp;");
    info = info.replaceAll("<", "&lt;");
    info = info.replaceAll(">", "&gt;");
    info = info.replaceAll("\"", "&quot;");
    info = info.replaceAll("'", "&apos;");
    info = info.replaceAll("\n", "<br>");
    return info;
  }
   static synchronized public String[][] replaceSpCharForSave(String info[][]) {    
    if (info!=null){ 
      int col = 0;
      for (String[] lines : info){
        int row = 0;
        for (String line : lines){
          info[col][row]=replaceSpCharForSave(line);
          row++;
        }
        col++;
      }
    }  
    return info;
  }
}
