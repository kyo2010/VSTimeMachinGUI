/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KKV.Export2excel;

import KKV.Utils.Tools;
import java.util.Calendar;
import java.util.Random;

/**
 * Набор сервисных функций для работы с XLS
 * @author kimlaev
 */
public class XLSTools {
  /** �?мена столбцов в Excel */
  public static String[] XLSColName = {
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
    "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD",
    "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN",
    "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX",
    "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH",
    "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR",
    "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB",
    "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL",
    "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV",
    "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF",
    "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP",
    "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ",
    "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ",
    "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET",
    "EU", "EV", "EW", "EX", "EY", "EZ", "FA", "FB", "FC", "FD",
    "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN",
    "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX",
    "FY", "FZ", "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH",
    "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR",
    "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ", "HA", "HB",
    "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL",
    "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV",
    "HW", "HX", "HY", "HZ", "IA", "IB", "IC", "ID", "IE", "IF",
    "IG", "IH", "II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP",
    "IQ", "IR", "IS", "IT", "IU", "IV"
  };

  /** Возвращает имя столбца Excel по номеру */
  public static String getXLSColumnName(int num){
    if (num<0) return XLSColName[0];
    if (num>=XLSColName.length) return XLSColName[XLSColName.length-1];
    return XLSColName[num];
  }
  
  /** Возвращает имя столбца Excel по номеру */
  public static int getXLSColumnIndex(String colWord){
    for (int index=0; index<XLSColName.length; index++){
      if (XLSColName[index].equalsIgnoreCase(colWord)) return index;
    }
    return 0;
  }

  /** Добавляет новое ID для клетки с замещением старой */
  public static String addCellID(String cell_id, String cell_text) {
    if (cell_text.indexOf("{#") == 0) {
      cell_text = cell_text.replaceAll("\\{#", "{" + cell_id + "#");
    } else {
      cell_text = "{" + cell_id + "}" + cell_text;
    }
    return cell_text;
  }
  
    public synchronized static String createName(String prefix) {
    return createName(prefix,20).toUpperCase();
  }

  public synchronized static String createName(String prefix, int len)
  {
    if (len<=3) len=5;
    return prefix + 
            Long.toString(Calendar.getInstance().getTimeInMillis(), 20).toUpperCase()
            + Tools.padl(""+new Random().nextInt(999),3, "0");
  }

  public synchronized static String generateUID() {
    String res = "{"+Tools.createName("", 8).toUpperCase()+"-KKV"+((int) (Math.random()*10))+"-"+Tools.createName("", 4).toUpperCase()+ "-"+Tools.createName("", 4).toUpperCase()+"-"+Tools.createName("", 12).toUpperCase()+"}"; 
    return res;        
  }

  public static void main(String[] args){}

}
