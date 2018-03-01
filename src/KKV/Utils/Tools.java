package KKV.Utils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ru.nkv.var.pub.IVar;

public class Tools {

  private static StringBuffer resultErrTrace;
  public static final String THOUSEND_SEPARATOR = " ";

  /**
   * Метод для тетисрования
   * @param args аргументы
   */
  public static void main(String[] args) {
    //SystemOut.println("" + roundDo(233.9, 0.12));
    //Map <String, String> params = parseParameters ("-mail_group=\"Hello\" -msg=\"Hi there-is no one \"");

    //System.out.println(createName("test_"));
    //System.out.println(createName("test_",10));
    //System.out.println(createName("test_",5));
    
    System.out.println(lTrimZero("test"));
    System.out.println(lTrimZero("0test"));
    System.out.println(lTrimZero("0123"));
    System.out.println(lTrimZero("0000123"));
    System.out.println(lTrimZero(""));
    System.out.println(lTrimZero("00"));
    System.out.println(lTrimZero(null));
  }

  /** Удаляет слева нули */
  public static String lTrimZero(String value) {
    if (value==null) return "";
    int pos = -1;
    for (int i=0; i<value.length(); i++){
      if (value.charAt(i)!='0') break;
      pos = i;
    }
    if (pos!=-1) value = value.substring(pos+1);
    return value;
  }
  
  /** Удаляет слева пробелы */
  public static String lTrim(String value) {
    if (value==null) return "";
    int pos = -1;
    for (int i=0; i<value.length(); i++){
      if (value.charAt(i)!=' ') break;
      pos = i;
    }
    if (pos!=-1) value = value.substring(pos+1);
    return value;
  }

  /** Конструктор */
  public Tools() {
  }

  /**
   * Округление до заданной величины
   * @param value число для округления
   * @param valRound величина, до которой нужно округлить
   * @return округленное значение
   */
  synchronized public static double roundDo(double value, double valRound) {
    if (valRound == 0.00) {
      valRound = 0.01;
    }

    BigDecimal bd = new BigDecimal("" + Math.round(value / valRound));
    BigDecimal bd1 = bd.multiply(new BigDecimal("" + valRound));
    return bd1.doubleValue();
  }

  /**
   * Округление до заданной величины
   * @param sum число, которое нужно округлить
   * @param dec количество знаков после запятой
   * @return строку с округленным числом
   */
  synchronized public static String roundString(double sum, int dec) {
    long power = (long) Math.pow(10, dec);
    long first = Math.round(sum * power) / power;
    long second = Math.abs(Math.round(sum * power) % power);
    String secString = "" + second;
    for (int i = secString.length(); i < dec; i++) {
      secString = "0" + secString;
    }
    String res = "" + first;
    if (first == 0 && sum < 0) {
      res = "-" + res;
    }
    if (dec != 0) {
      res += "." + secString;
    }
    return res;
  }

  /** Возвращает строку из n пробелов
   * @param len количество пробелов
   * @return строку пробелов
   */
  synchronized public static String space(int len) {
    return repl(" ", len);
  }

  /**
   * Возвращает строку, реплицированную символом до заданной длины
   * @param symb символ для репликации
   * @param len количество символов
   * @return строку из символов
   */
  synchronized public static String repl(char symb, int len) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < len; i++) {
      sb.append(symb);
    }
    return "" + sb;
  }

  /**
   * Возвращает строку, реплицированную символом до заданной длины
   * @param symb символ для репликации
   * @param len количество символов
   * @return строку из символов
   */
  synchronized public static String repl(String symb, int len) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < len; i++) {
      sb.append(symb);
    }
    return "" + sb;
  }

  /**
   * Проверка на пустую строку
   * @param ln строка для проверки
   * @return true, если строка состоит только из пробелов
   */
  synchronized public static boolean empty(String ln) {
    return ln == null || ln.trim().equals("");
  }

  /**
   * Проверка на пустую строку
   * @param ln строка для проверки
   * @return true, если строка состоит только из пробелов
   */
  synchronized public static boolean empty(StringBuffer ln) {
    return ln == null || ln.toString().trim().equals("");
  }

  /**
   * Проверка на пустое значение
   * @param val число для проверки
   * @return true, если число = 0
   */
  synchronized public static boolean empty(double val) {
    return (val == 0.0);
  }

  /**
   * Возвращает строки, где возникло исключение
   * @param e исключение
   * @return строка с описанием исключения
   */
  synchronized public static String traceError(Throwable e) {
    OutputStream myOut = new ByteArrayOutputStream();
    e.printStackTrace(new PrintStream(myOut));
    return myOut.toString();
  }
  
  synchronized public static String traceErrorWithCaption(Throwable e) {
    try{
      UserException ue = (UserException) e;
      return ue.error+"<br>"+ue.details;
    }catch(Exception ein){}
    
    OutputStream myOut = new ByteArrayOutputStream();
    e.printStackTrace(new PrintStream(myOut));
    return e.getMessage()+" "+myOut.toString();
  }

  synchronized public static String traceErrorForWeb(Throwable e) {
    /*OutputStream myOut = new ByteArrayOutputStream();
    e.printStackTrace(new PrintStream(myOut));
    return myOut.toString().replaceAll("\n", "<br>");*/
    
    StringBuffer sb = new StringBuffer();
    
    sb.append(
            "&nbsp;<font color='blue'><a href='#' onclick='$(info"+e.hashCode()+").style.display=\"block\"'>details</a>"+
            "<div id='info"+e.hashCode()+"' style='display:none'>"+e.toString()+" "+Tools.traceError(e)+"</div></font>"
    );
     return sb.toString(); 
  }

  /**
   * Левая часть строки
   * @param str строка
   * @param len количество символов для формирования левой части
   * @return Левая часть строки
   */
  synchronized public static String left(String str, int len) {
    String res;
    try {
      res = str.substring(0, len);
    } catch (Exception e) {
      res = str;
    }
    return res;
  }

  /**
   * Добивка пробелами справа
   * @param str строка для добивки пробелами
   * @param len итоговый размер строки
   * @return строку добитую пробелами справа
   */
  synchronized public static String padr(String str, int len) {
    for (int i = str.length(); i < len; i++) {
      str += " ";
    }
    return str;
  }

  /**
   * Добивка символами слева заданным символом
   * @param str строка для добивки пробелами
   * @param len итоговый размер строки
   * @param symb символ добивки
   * @return строку добитую символами слева
   */
  synchronized public static String padl(String str, int len, char symb) {
    StringBuffer sb = new StringBuffer(repl(symb, len - str.length()));
    sb.append(str);
    return sb.toString();
  }

  /**
   * Добивка символами слева
   * @param str строка для добивки пробелами
   * @param len итоговый размер строки
   * @param symb символ добивки
   * @return строку добитую символами слева
   */
  synchronized public static String padl(String str, int len, String symb) {
    StringBuffer sb = new StringBuffer(repl(symb, len - str.length()));
    sb.append(str);
    return sb.toString();
  }

  /**
   * Добивка пробелами слева
   * @param str строка для добивки пробелами
   * @param len итоговый размер строки
   * @return строку добитую пробелами слева
   */
  synchronized public static String padl(String str, int len) {
    return padl(str, len, ' ');
  }

  /**
   * Добивка пробелами для центровки
   * @param str строка для добивки пробелами
   * @param len итоговый размер строки
   * @return строку добитую пробелами слева и справа
   */
  synchronized public static String padc(String str, int len) {
    if (str.equals("")) {
      return "";
    }

    if (str.length() > len) {
      return str.substring(0, len);
    }

    int pos = (len / 2 - str.length() / 2);
    if (pos < 0) {
      pos = 0;
    }
    String s = padl(str, pos + str.length());
    return s + space(len - s.length());
  }

  /**
   * Усекает концевые пробелы в строке
   * @param str Усекаемая строка
   * @return Строка без концевых пробелов
   */
  public synchronized static String rTrim(String str) {
    int pos = str.length();
    while (str.charAt(--pos) == ' ');
    return str.substring(0, pos + 1);
  }
  
  public synchronized static String removeSpaces(String str) {
    String res = "";
    for (int i = 0; i<str.length(); i++){
      if (str.charAt(i)!=' ') res+=str.charAt(i);
    }
    return res;
  }

  /**
   * Сжать строку. Удалить ВСЕ внутренние и внешние пробелы
   * @param str исходная строка
   * @return результирующая строка
   */
  synchronized public static String gripe(String str) {
    StringTokenizer st = new StringTokenizer(str);
    StringBuffer sb = new StringBuffer();
    while (st.hasMoreTokens()) {
      sb.append(st.nextToken().trim());
    }

    return sb.toString();
  }

  /**
   * Возвращает строку цедых чисел через запятую от массива целых чисел
   * @param intArr массив целых чисел
   * @return строку цедых чисел
   */
  synchronized public static String intArrToStr(int[] intArr) {
    if (intArr.length == 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < intArr.length; i++) {
      sb.append("" + intArr[i] + (i < intArr.length - 1 ? "," : ""));
    }
    return "" + sb;
  }

  /**
   * Возвращает строку, в которой строки перечислены через запятую от массива строк
   * @param strArr массив строк
   * @return результирующая строка
   */
  synchronized public static String stringArrToStr(String[] strArr) {
    if (strArr.length == 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      sb.append(strArr[i] + (i < strArr.length - 1 ? "," : ""));
    }
    return "" + sb;
  }

  /**
   * Возвращает строку, в которой строки перечислены через delim от массива строк
   * @param strArr
   * @param delim разделитель
   * @return результирующая строка
   */
  synchronized public static String stringArrToStr(String[] strArr, String delim) {
    if (strArr.length == 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      sb.append(strArr[i] + (i < strArr.length - 1 ? delim : ""));
    }
    return "" + sb;
  }

  /**
   * Возвращает строку, в которой строки перечислены через delim от массива строк
   * @param strArr
   * @param needQuote нужны ли апострофы
   * @return результирующая строка
   */
  synchronized public static String stringArrToStr(String[] strArr, boolean needQuote) {
    if (strArr.length == 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      sb.append("'" + strArr[i] + (i < strArr.length - 1 ? "'," : "'"));
    }
    return "" + sb;
  }

  /**
   * Возвращает массив строк от строки в которой строки перечислены через sym, и строка начинается beg и заканчивается end
   * @param str исходная строка
   * @param end символ, которым заканчивается строка
   * @param sym разделитель строк
   * @return результирующая строка
   */
  synchronized public static String[] getStringArrayFromString(String str, char beg, char end, String sym) {
    try {
      String[] res;
      int pos = 0;
      int start = str.indexOf(beg);

      ArrayList list = new ArrayList();
      pos = str.indexOf(sym);//находим позицию символа разделителя
      if (pos != -1)//если разделитель есть, то помещаем в список элемент строки от символа end до pos
      {
        list.add((str.substring(start + 1, pos)));
      }

      while (pos != -1) {

        if (str.indexOf(sym, pos + 1) != -1) //если есть разделитель, помещаем строчку в список
        {
          list.add((str.substring(pos + 1, pos = str.indexOf(sym, pos + 1))));
        }

        //если разделитель не найден, но найден символ end (последний элемент строки), то помещаем элемент строки в список
        if (str.indexOf(sym, pos + 1) == -1 && str.indexOf(end, pos + 1) != -1) {
          list.add((str.substring(pos + 1, str.replace(end, ' ').length() - 1)));
          pos = -1;//завершение цикла
        }

        //если разделитель не найден и не найден сивол end, добавляем всю оставшуюся часть строки
        if (str.indexOf(sym, pos + 1) == -1 && str.indexOf(end, pos + 1) == -1) {
          list.add((str.substring(pos + 1, str.trim().length())));
          pos = -1;//завершение цикла
        }
      }

      res = new String[list.size()];
      for (int i = 0; i < list.size(); res[i] = (String) list.get(i++));

      return res;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  /**
   * Возвращает массив целых от строки цифр, перечисленных через sym, заканчивающихся end
   * @param str исходная строка
   * @param end символ, которым заканчивается строка
   * @param sym разделитель строк
   * @return результирующая строка
   */
  synchronized public static int[] getIntArrayFromString(String str, char end, String sym) {
    try {
      int[] res;
      int pos = 0;

      ArrayList list = new ArrayList();
      pos = str.indexOf(sym);
      if (pos != -1) {
        list.add(new Integer(str.substring(1, pos)));
      }

      while (pos != -1) {

        if (str.indexOf(sym, pos + 1) != -1) {
          list.add(new Integer(str.substring(pos + 1, pos = str.indexOf(sym, pos + 1))));
        }
        if (str.indexOf(sym, pos + 1) == -1 && str.indexOf(end, pos + 1) != -1) {
          list.add(new Integer(str.substring(pos + 1, str.replace(end, ' ').length() - 1)));
          pos = -1;
        }

        if (str.indexOf(sym, pos + 1) == -1 && str.indexOf(end, pos + 1) == -1) {
          list.add(new Integer(str.substring(pos + 1, str.trim().length())));
          pos = -1;
        }
      }

      res = new int[list.size()];
      for (int i = 0; i < list.size(); res[i] = ((Integer) list.get(i++)).intValue());

      return res;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  /** substr - как в Clipper'e
   * @param line строка
   * @param from начало выделения строки
   * @param kol количество символов
   * @return новая строка
   */
  private static String substr(String line, int from, int kol) {
    if (kol < 0) {
      return "";
    }
    if (from < 0) {
      return "";
    }
    if (from >= line.length()) {
      return "";
    }
    if (from + kol > line.length()) {
      return line.substring(from, line.length());
    }
    return line.substring(from, from + kol);
  }

  /**
   * Сжать строку. Удалить повторяющиеся пробелы
   * @param str исходная строка
   * @return результирующая строка
   */
  synchronized public static String charone(String str) {
    StringTokenizer st = new StringTokenizer(str);
    StringBuffer sb = new StringBuffer();
    while (st.hasMoreTokens()) {
      sb.append(st.nextToken().trim() + " ");
    }

    return sb.toString();
  }

  /**
   * Разбивает строку на составляющие
   * @param line входная строка
   * @param bound границы разбиения
   * @param num номер подстроки
   * @return подстроку
   */
  synchronized public static String partLine(String line, int bound, int num) {
    if (line.equals("")) {
      return " ";
    }

    if (bound == 1) {
      return line.substring(num - 1, num);
    }

    int last_ch = 0;
    String out = "";

    for (int i = 0; i < num; i++) {

      if (last_ch > line.length()) {
        out = space(bound - 1);
      } else {
        out = substr(line, last_ch, bound - 1);
      }

      if (out.length() < bound - 1) {
        out += space(bound - out.length() - 1);
      }

      last_ch += (bound - 1);

      if (empty(substr(line, last_ch - 1, 1))) {
        out += " ";
      } else if (empty(substr(line, last_ch, 1))) {
        out += " ";
        last_ch++;
      } else if (empty(substr(line, last_ch - 2, 1))) {
        out = substr(out, 0, out.length() - 1) + "  ";
        last_ch--;
      } else if (empty(substr(line, last_ch + 1, 1))) {
        out += substr(line, last_ch, 1);
        last_ch += 2;
      } else {
        out += "-";
      }
    }

    return out;

  }

  synchronized public static double getDouble(String number) throws NumberFormatException {
    char[] chArr = number.toCharArray();
    char symb = '.';

    for (int i = 0; i < chArr.length; i++) {
      if (!Character.isDigit(chArr[i]) && chArr[i] != '-') {
        symb = chArr[i];
        break;
      }
    }
    return Double.parseDouble(number.replace(symb, '.'));
  }

  /**
   * Возвращает массив строк из {tttt, ppppp, vvvvv }
   */
  synchronized public static String[] getArray(String array) throws UserException {
    ArrayList al = new ArrayList();
    final String razd = ";";

    if (array == null) {
      throw new UserException("Error","Array is empty");
    }

    if ((array.substring(array.indexOf("{") + 1, array.indexOf("}"))).toLowerCase().trim().equals("all")) {
      return new String[]{"all"};
    }

    int posOfBeg = 1;
    int posOfRazd = array.indexOf(razd);

    while (posOfRazd != -1) {
      al.add(array.substring(posOfBeg, posOfRazd));
      posOfBeg = posOfRazd + 1;
      posOfRazd = array.indexOf(razd, posOfBeg);
    }

    String[] res = new String[al.size()];
    for (int i = 0; i < al.size(); i++) {
      res[i] = (String) al.get(i);
    }

    return res;
  }

  
  /** Запись в файл data.out
   * @param str строка для записи
   * @throws UserException
   */
  synchronized public static void toFile(String str) throws UserException {
    toFile(new String[]{str});
  }

  /** Запись в файл data.out
   * @param arr массив строк для записи
   * @throws UserException
   */
  synchronized public static void toFile(String[] arr) throws UserException {
    toFile(new String[][]{arr});
  }

  /** Запись в файл data.out
   * @param result массив строк для записи
   * @throws UserException
   */
  synchronized public static void toFile(String[][] result) throws UserException {
    try {
      File file = new File("data.out");
      FileWriter fw = new FileWriter(file);
      PrintWriter pw = new PrintWriter(fw);

      for (int i = 0; i < result.length; i++) {
        for (int j = 0; j < result[0].length; j++) {
          pw.write("" + (result[i][j]));
        }
        pw.write("\n");
      }

      fw.close();
      pw.close();
    } catch (Exception e) {
      throw new UserException("" + traceError(e), "");
    }
  }

  /**
   * Возвращает строку разделенную разделителем
   * @param arr - массив объектов для toString()
   * @param delim - разделитель
   * @return строка с разделителями
   */
  synchronized public static String arrToStr(Object[] arr, String delim) {
    if (arr.length == 0) {
      return "";
    }
    StringBuffer str = new StringBuffer();
    for (int i = 0; i < arr.length; i++) {
      str.append(arr[i] + (i < arr.length - 1 ? delim : ""));
    }
    return "" + str;
  }

  /**
   * Преобразует полное имя пользователя в фамилию с инициалами.
   * Если преобразовать невозможно, возвращает исходный вариант.
   * @param user 'Фамилия �?мя Отчество'
   * @return 'Фамилия �?.О.'
   */
  public synchronized static String FIO(String user) {
    try {
      String[] buffer = user.split(" ", 3);
      String f = buffer[0] + " ";
      String i = buffer[1].substring(0, 1) + ".";
      String o = buffer[2].substring(0, 1) + ".";
      return f + i + o;
    } catch (Exception e) {
      return user;
    }
  }

  /**
   * Создать новое уникальное имя файла.
   * @param prefix Строка, с которой будет начинаться имя.
   * @return Уникальное имя.
   */
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
    
   public static String getStringFromObject(Object[] objs, int index, String defaultValue) {
     String value = defaultValue;
     if (objs!=null && objs.length>index && objs[index]!=null){
       return objs[index].toString();
     }
     return value;
   }


   public static Map <String, String> parseParameters (String params)
     {
     Map <String, String> result = new HashMap <String, String> ();
     if (params.indexOf ("-") < 0)
       {
       return result;
       }


     params = " " + params;
     String tokens [] = params.split (" -");
     for (String t : tokens)
       {
       int q1 = t.indexOf ("=\"");
       int q2 = t.indexOf ("\"", q1 + 2);
       if (q1 < 0 || q2 < 0)
         {
         continue;
         }
       String key   = t.substring (0, q1).trim ();
       String value = t.substring (q1 + 2, q2).trim ();
       if (key == null || key.length () < 1)
         {
         continue;
         }
       if (value == null)
         {
         value = "";
         }
       result.put (key, value);
       }

     return result;
     }

   /** заменяет CellID в строке вида {test##=RC()}**/
   public static String addCellID(String cell_id, String cell_text) {
    if (cell_text.indexOf("{#") == 0) {
      cell_text = cell_text.replaceAll("\\{#", "{" + cell_id + "#");
    } else {
      cell_text = "{" + cell_id + "}" + cell_text;
    }
    return cell_text;
  }
   
   public static String getTextFromFile(String fileName){
     return getTextFromFile(fileName,null);
   }

   public static String getTextFromFile(String fileName, IVar var){
     StringBuffer sb = new StringBuffer();
     try{     
        FileInputStream fileIn = new FileInputStream( fileName );
        BufferedReader dataIn =
                new BufferedReader( new InputStreamReader( fileIn ) );

        String readLine = dataIn.readLine();
        String nameParam, valueParam;

        while( readLine != null )
        {
           readLine = readLine;
           sb.append(readLine+"\n");           
           readLine = dataIn.readLine();
        }   
        fileIn.close();
        dataIn.close();
      }catch(Exception e) {
        System.out.println("File '"+fileName+"' read is error. " + Tools.traceError(e));
      }       
     
     if (var!=null){
       return var.applyValues(sb.toString());
     }
     
     return sb.toString();
   }
     
   public static void deleteDir(File dir) {
    try {
      File[] files = dir.listFiles();
      if (files==null) {
        dir.delete();
        return;
      }//return;
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) deleteDir(files[i]);
        else files[i].delete();
      }
      System.out.println("Dir '"+dir.getAbsolutePath()+"' was deleted");
      dir.delete();
    } catch (Exception e) {
      System.out.println(e+" "+Tools.traceError(e));
    }
  }
}
