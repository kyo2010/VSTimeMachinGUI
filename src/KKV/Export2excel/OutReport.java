package KKV.Export2excel;

import KKV.Utils.JDEDate;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import ru.nkv.var.pub.IVar;

/**
 * Created by IntelliJ IDEA. User: Asea1 Date: 05.01.2004 Time: 10:28:06
 * <p/>
 * Class for output result Report
 */
public class OutReport {

  public static String USER_PATH = "reports/XLS/";
  public static String VIEWS_PATH = "tools/";
  public static String MACROS_PATH = "tools/";

  String user;
  File dir = null;
  ArrayList<ReportStream> streams = new ArrayList<ReportStream>();
  private String reportName = "";
  private String reportDetails = "";
  public String macrosFile = "";
  private String macrocProc = "";
  public boolean autoFit = true;
  private boolean storeOriginalXLSFile = false;
  private String originalXLSFile = "";
  // Добавление к письму
  String mailContent = null;
  String mailSubject = null;
  public String macrosFileForSheet1 = "";
  public String macrosFileForSheet2 = "";
  public String macrosFileForSheet3 = "";
  public String macrosFileForSheet4 = "";
  public String macrosFileForSheet5 = "";
  public String DocumentType = "1";
  public String lastZipFileName = null;
  boolean sendFinishMessage = true;
  public String lastDocID = null;
  public boolean showExcel = false;

  public boolean isShowExcel() {
    return showExcel;
  }

  public void setShowExcel(boolean showExcel) {
    this.showExcel = showExcel;
  }
 
     

  public String getLastZipFileName() {
    return lastZipFileName;
  }

  public String getMacrosFileForSheet1() {
    return macrosFileForSheet1;
  }

  public void setMacrosFileForSheet1(String macrosFileForSheet1) {
    this.macrosFileForSheet1 = macrosFileForSheet1;
  }

  public String getMacrosFileForSheet2() {
    return macrosFileForSheet2;
  }

  public void setMacrosFileForSheet2(String macrosFileForSheet2) {
    this.macrosFileForSheet2 = macrosFileForSheet2;
  }

  public String getMacrosFileForSheet3() {
    return macrosFileForSheet3;
  }

  public void setMacrosFileForSheet3(String macrosFileForSheet3) {
    this.macrosFileForSheet3 = macrosFileForSheet3;
  }

  public void setMacrosFileForSheet4(String macrosFileForSheet4) {
    this.macrosFileForSheet4 = macrosFileForSheet4;
  }

  public void setMacrosFileForSheet5(String macrosFileForSheet5) {
    this.macrosFileForSheet5 = macrosFileForSheet5;
  }

  public String getMailContent() {
    return mailContent;
  }

  public void setMailContent(String mailContent) {
    this.mailContent = mailContent;
  }

  public String getMailSubject() {
    return mailSubject;
  }

  public void setMailSubject(String mailSubject) {
    this.mailSubject = mailSubject;
  }

  public String getMacrocProc() {
    return macrocProc;
  }

  public void setMacrocProc(String macrocProc) {
    this.macrocProc = macrocProc;
  }

  public boolean isAutoFit() {
    return autoFit;
  }

  public void setAutoFit(boolean autoFit) {
    this.autoFit = autoFit;
  }

  //  constructor. Create User Directory
  public OutReport(String user) {
    this.user = user;
    try {
      dir = new File(USER_PATH + user);
      dir.mkdirs();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getStreamCount() {
    return streams.size();
  }

  public ArrayList getStreamsInArray() {
    ArrayList str = new ArrayList();
    for (Iterator it = streams.iterator(); it.hasNext();) {
      ReportStream rs = (ReportStream) it.next();
      String[] lines = new String[3];
      lines[0] = rs.nameReport;
      lines[1] = rs.nameDateFile;
      lines[2] = rs.nameViewFile;
      str.add(lines);
    }
    return str;
  }

  public void createStream(int countStream) {
    for (int i = 0; i < countStream; i++) {
      streams.add(new ReportStream(dir, i, user));
    }
  }

  public int addStream() {
    int num = streams.size();
    streams.add(new ReportStream(dir, num, user));
    return num;
  }

  public void clearAllStreams() {
    streams.clear();
    macrosFileForSheet1 = "";
    macrosFileForSheet2 = "";
    macrosFileForSheet3 = "";
    macrosFile = "";
    macrocProc = "";
    closeMacrosStream();
    try {
      if (macrosStreamWriter != null) {
        macrosStreamWriter.close();
      }
    } catch (Exception e) {
    }
    macrosStreamWriter = null;
  }

  public int addStreamInFirst() {
    int num = streams.size();
    streams.add(0, new ReportStream(dir, num, user));
    return 0;
  }

  public int addStreamInPos(int pos) {
    streams.add(pos, new ReportStream(dir, pos, user));
    return pos;
  }

  public void addToDataFile(int numStream, String line) {
    if (line == null) {
      return;
    }
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).addToDataFile(line);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addToDataFile(int numStream, List<String> lines) {
    if (lines == null) {
      return;
    }
    if (numStream < 0) {
      return;
    }
    try {
      for (String line : lines) {
        if (line != null) {
          ((ReportStream) streams.get(numStream)).addToDataFile(line);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void closeDataStreams(){
    for (ReportStream stream : streams){
      stream.closeDataFile();
      stream.closeViewStream();
    }
  }

  public void closeDataFile(int numStream) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).closeDataFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void applayPoolToViewFile(int numStream, IVar pool) throws IOException {
    if (numStream < 0) {
      return;
    }
    ((ReportStream) streams.get(numStream)).applayPoolToViewFile(pool);
  }

  public void deleteDataFile(int numStream) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).deleteDataFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  add Line to file with idRow
  public void addToDataFile(int numStream, String idRow, String line) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).addToDataFile(idRow, line);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  set View File Name for current Stream
  public void setViewFileName(int numStream, String viewFileName) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).nameViewFile = VIEWS_PATH + viewFileName;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  set View File Name for current Stream
  public void setMacrosFileName(String macrosFile) {
    this.macrosFile = MACROS_PATH + macrosFile;
  }

  //  set View File Name for current Stream
  public void setMacrosFileNameA(String macrosFile) {
    this.macrosFile = macrosFile;
  }
  FileOutputStream macrosFileStream = null;
  OutputStreamWriter macrosStreamWriter = null;

  // close data file
  public void closeMacrosStream() {
    if (macrosFileStream != null) {
      try {
        macrosStreamWriter.close();
        macrosFileStream.close();
      } catch (IOException e) {
        e.printStackTrace();  //To change body of catch statement use Options | File Templates.
      }
    }
    macrosFileStream = null;
  }

  public void createMacrosStream() {
    if (macrosFileStream == null) {
      try {
        JDEDate jd = new JDEDate();
        String nameFile = jd.getTimeString("");
        File vbsFile = new File(dir, nameFile + ".vbs");
        macrosFile = dir.getPath() + "/" + nameFile + ".vbs";
        macrosFileStream = new FileOutputStream(vbsFile);
        macrosStreamWriter = new OutputStreamWriter(macrosFileStream, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void applayPoolToMacrosFile(IVar pool) throws IOException {
    String orgFile = macrosFile;
    FileInputStream fileIn = new FileInputStream(orgFile);
    BufferedReader dataIn
            = new BufferedReader(new InputStreamReader(fileIn));

    StringBuffer fS = new StringBuffer();
    String readLine = dataIn.readLine();
    while (readLine != null) {
      fS.append(readLine + "\n");
      readLine = dataIn.readLine();
    }
    String macrosTxt = pool.applyValues(fS.toString());
    fileIn.close();
    dataIn.close();

    createMacrosStream();
    macrosStreamWriter.write(macrosTxt);
    closeMacrosStream();
  }

  public void applayPoolToMacrosFile2(IVar pool) throws IOException {
    String orgFile = macrosFile;
    String macrosTxt = pool.applyValuesToFile(orgFile);
    createMacrosStream();
    macrosStreamWriter.write(macrosTxt);
    closeMacrosStream();
  }

  //  set View File Name for current Stream
  public void setDataFileName(int numStream, String dataFileName) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).nameDateFile = "!Resources/" + dataFileName;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  set View File Name for current Stream
  public void setReportName(int numStream, String reportName) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).setNameReport(reportName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  set View File Name for current Stream
  public void setDataFileNameA(int numStream, String dataFileName) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).nameDateFile = dataFileName;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //  set View File Name for current Stream
  public void setViewFileNameA(int numStream, String viewFileName) {
    if (numStream < 0) {
      return;
    }
    try {
      ((ReportStream) streams.get(numStream)).nameViewFile = viewFileName;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  public String getReportDetails() {
    return reportDetails;
  }

  public void setReportDetails(String reportDetails) {
    this.reportDetails = reportDetails;
  }
  String dopInfo = "";

  public void setStoreOriginalXLSFile(boolean storeOriginalXLSFile) {
    this.storeOriginalXLSFile = storeOriginalXLSFile;
  }

  public String getOriginalXLSFile() {
    return originalXLSFile;
  }

  public void setOriginalXLSFile(String originalXLSFile) {
    this.originalXLSFile = originalXLSFile;
  }

  public static class ReportStream {

    public String nameReport;
    String nameFile;
    File dataFile = null;
    File viewFile = null;
    public String nameViewFile = "";
    public String nameDateFile = "";
    public String macrosFile = "";
    FileOutputStream dataFileStream = null;
    FileOutputStream viewFileStream = null;
    Writer writer = null;
    Writer viewWriter = null;
    static int reportID = 0;
    String user = "";
    File directory = null;
    int index = 0;

    public ReportStream(File dir, int index, String user) {
      JDEDate rd = new JDEDate();
      this.user = user;
      nameFile = rd.getDateAsYYYYMMDD_andTime("-", "");
      this.index = index;
      directory = dir;
      reportID++;
      try {
        dataFile = new File(dir, nameFile + "_" + index + ".dat");
        nameDateFile = dir.getPath() + "/" + nameFile + "_" + index + ".dat";
        dataFile.createNewFile();
        dataFileStream = new FileOutputStream(dataFile);
        writer = new OutputStreamWriter(dataFileStream, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // delete file
    public void deleteDataFile() {
      try {
        FileInputStream fis = null;
        dataFile.deleteOnExit();
        dataFile.deleteOnExit();
      } catch (Exception e) {
      }
    }

    //  add line to dtat file
    public void addToDataFile(String line) {
      if (dataFileStream != null && line != null) {
        try {
          //dataFileStream.write((line + "\n").getBytes());
          writer.write(line + "\n");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    // close data file
    public void closeDataFile() {
      if (dataFileStream != null) {
        try {
          writer.close();
          dataFileStream.close();
        } catch (IOException e) {
          e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
      }
    }

    public void createViewStream() {
      if (viewFileStream == null) {
        try {
          JDEDate rd = new JDEDate();
          nameFile = rd.getDateAsYYYYMMDD_andTime("-", "");
          viewFile = new File(directory, nameFile + "_" + index + ".xml");
          nameViewFile = directory.getPath() + "/" + nameFile + "_" + index + ".xml";
          viewFile.createNewFile();
          viewFileStream = new FileOutputStream(viewFile);
          viewWriter = new OutputStreamWriter(viewFileStream, "UTF-8");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    public void closeViewStream() {
      if (viewFileStream != null) {
        try {
          viewWriter.close();
          viewFileStream.close();
          viewFile.deleteOnExit();
          viewFileStream = null;
        } catch (IOException e) {
        }
      }
    }

    public void applayPoolToViewFile(IVar pool) throws IOException {
      String orgFile = nameViewFile;
      createViewStream();
      String view = pool.applyValuesToFile(orgFile);
      addToViewFile(view);
      closeViewStream();
    }

    //  add Line to file with idRow
    public void addToViewFile(String line) {
      if (viewFileStream != null) {
        try {
          //dataFileStream.write((idRow + ":" + line + "\n").getBytes());
          viewWriter.write(line + "\n");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    //  add Line to file with idRow
    public void addToDataFile(String idRow, String line) {
      if (dataFileStream != null) {
        try {
          //dataFileStream.write((idRow + ":" + line + "\n").getBytes());
          writer.write(idRow + ":" + line + "\n");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    public String getNameReport() {
      return nameReport;
    }

    public void setNameReport(String nameReport) {
      this.nameReport = nameReport;
    }
  }
}
