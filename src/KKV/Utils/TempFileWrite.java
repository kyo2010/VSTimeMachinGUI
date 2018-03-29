package KKV.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

public class TempFileWrite {
  private Writer fileOut;
  public String fileName;
  public String br = "\n";
  
  public static final String UTF8_BOM = "\uFE00\uFF00";

  public TempFileWrite() {
    this("xxx.txt","\n");
  }

  public synchronized void delete() {
    try {
      fileOut.close();
      File f = new File(fileName);
      f.delete();
      fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "CP1251"));
      //fileOut = new FileOutputStream(fileName, true);
    } catch (Exception e) {
      System.out.println("" + e);
    }
  }
  
   public TempFileWrite(String fileName){
     this(fileName,"\n","CP1251");
   } 

  private TempFileWrite(String fileName, String br) {
    this.br = br;
    new File(new File(fileName).getParent()).mkdirs();
    try {
      this.fileName = fileName;
      fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
    } catch (Exception e) {
      System.out.println("" + e);
    }
  }
  
  public TempFileWrite(String fileName, String br, String codePage) {
    this.br = br;
    try{
    new File(new File(fileName).getParent()).mkdirs();
    }catch(Exception e){}
    try {
      this.fileName = fileName;
      if (codePage!=null)
        fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), codePage));
      else 
        fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
    } catch (Exception e) {
      System.out.println("" + e);
    }
  }

  public void writeFile(String writeStr) {
    System.out.println(writeStr);
    writeFile(writeStr, true);   
  }
  
  public void writeFile(Exception e) {
    String writeStr = ""; 
    if (e.getClass().equals(UserException.class)){
      UserException ue = (UserException)e;
      writeStr = ue.error+" "+ue.details;
    }else{
      writeStr = e.getMessage()+" "+Tools.traceError(e);
    }
    //System.out.println(writeStr);
    writeFile(writeStr, true);
  }
  
  public String getTime(){
    Date dt = Calendar.getInstance().getTime();
    String res = (dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate()+" "+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
    return res;
  };

  public void writeFile(String writeStr, boolean flagTime) {
    try {
      if (flagTime)
        writeStr = "[" + getTime() + "] " + writeStr;
        fileOut.write(writeStr);
        fileOut.flush();
    } catch (Exception e) {
      System.out.println("" + e);
    }
  }
  
  public void printXMLHeader(){
    try{ 
      //fileOut.write(UTF8_BOM.getBytes("UTF-8"));
      //fileOut.write(UTF8_BOM);
      fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-16"));
     } catch (Exception e) {
      System.out.println("" + e);
    }
  }
  
  public void println(String line){
    try {
      line = line + br;
     // line = Decoder.fromUTFtoWindows(line); 
      line = new String (line);
     // fileOut.write(line.getBytes("UTF-16"));
     // fileOut.write(line.getBytes());
    //  fileOut.write(br.getBytes("UTF-16"), 0, br.length());
    //  fileOut.write(line.getBytes());
    //  fileOut.write(br.getBytes(), 0, br.length());
      fileOut.write(line);
      fileOut.flush();
    } catch (Exception e) {
      System.out.println("" + e);
    }
  };

  public void closeFile() {
    try {
      fileOut.close();
    } catch (Exception e) {
      System.out.println("" + e);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    closeFile();
    super.finalize();
  }    

}
