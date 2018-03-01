package KKV.Export2excel;

import KKV.Utils.UserException;
import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import groovyjarjarantlr.Utils;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.ws.BindingProvider;


public class XLSMaker {

    OutReport outReport = null;
    public static boolean formulaRemoverStarted = false;
    public static String EXPORT_PROG_NAME = "tools/toExcel2.exe";
    public static boolean showProcessLog = false;
    public static String EXPORT_EXT = "xlsx";
    public static String EXPORT_TMP_PATH = "reports/XLS/";
    
    public static Object xslObject = new Integer(1);

    public OutReport getOutReport() {
        return outReport;
    }

    /*
     * public XLSMaker(OutReport outReport) { this.outReport = outReport; }
     */
    private XLSMaker() {
    }

    private String makeXLS() throws UserException {
        return makeXLS(outReport);
    }

    public static String getXLSExt(boolean usemacros) {
        String ext = "xls";
        try {
            String ext1 = EXPORT_EXT;
            if (ext1 != null && !ext1.equals("")) {
                ext = ext1;
            }
        } catch (Exception e) {
        }
        if (!usemacros && ext.equalsIgnoreCase("xlsm")) {
            ext = "xlsx";
        }
        return ext;
    }

    public static String makeXLS(OutReport outReport) throws UserException {
      return __makeXLS(outReport);        
    }

    private static synchronized String __makeXLS_sync(OutReport outReport) throws UserException {
      return __makeXLS(outReport);
    }
    
    public static long reportCounter = 0;

    private static String __makeXLS(OutReport outReport) throws UserException {
        // synchronized (xslObject) {
        File dir = new File(EXPORT_TMP_PATH);
        dir.mkdirs();
        String repName = "";
                
        repName = outReport.getReportName();
        if (repName == null) {
            repName = "noname";
        } else {
            repName = repName.replace('\\', '_');
            repName = repName.replace('/', '_');
            repName = repName.replace("\"", "");
            repName = repName.replace("\'", "");
            repName = repName.replace(":", "");
            //repName = repName.replace(',','_');
        }
        JDEDate jd = new JDEDate();
        jd.setNowDate();

        boolean useMacros = false;
        if ((outReport.macrosFile != null && !outReport.macrosFile.equals(""))
                || (outReport.getMacrosFileForSheet1() != null && !outReport.getMacrosFileForSheet1().equals(""))
                || (outReport.getMacrosFileForSheet2() != null && !outReport.getMacrosFileForSheet2().equals(""))
                || (outReport.getMacrosFileForSheet3() != null && !outReport.getMacrosFileForSheet3().equals(""))) {
            useMacros = true;
        }
        String ext = getXLSExt(useMacros);

        String curDir = null;
        try {
            curDir = new File(".").getCanonicalPath() + "/";
        } catch (IOException e) {
            throw new UserException("Exception " + e.getMessage(), Tools.traceError(e));
        }
        
        String userName = "none";
        reportCounter++;
        try{
          userName = "report";
        }catch(Exception e){
        }

        repName = repName.replaceAll("\\[", "(");
        repName = repName.replaceAll("\\]", ")");
        String xlsName = curDir + EXPORT_TMP_PATH + 
                repName + " " + jd.getDateAsYYYYMMDD("-") + " " + userName+" rs" +reportCounter+ "." + ext;
                //Utils.Tools.createName(repName + "_" + jd.getDateAsYYYYMMDD("-") + "_", 5) + "." + ext;
        String mkName = curDir + EXPORT_TMP_PATH + Tools.createName("mk_") + ".2xls";
        //String varName = curDir + EXPORT_TMP_PATH + Tools.createName("mk_") + ".var";
        String macrosFile = null;
        if (outReport.macrosFile != null && !outReport.macrosFile.equals("")) {
            macrosFile = curDir + outReport.macrosFile;
        }
        String outputText = "Console output:\n";
        try {
            FileOutputStream fs = new FileOutputStream(mkName);
            Writer writer = new OutputStreamWriter(fs, "UTF-8");
            ArrayList streams = outReport.getStreamsInArray();
            /*if (streams.size() == 0) {
                throw new UserException("MakeXLS error", "Data for report is not found");

            }*/
            int i = 1;
            for (Iterator it = streams.iterator(); it.hasNext();) {
                String[] lines = (String[]) it.next();
                writer.write(("dataFile:" + curDir + lines[1] + "\n"));
                writer.write(("viewFile:" + curDir + lines[2] + "\n"));
                String nameSheet = lines[0];
                if (nameSheet == null) {
                    nameSheet = "";
                }
                if (nameSheet.equals("")) {
                    nameSheet = "noname_" + i;
                } else {
                    nameSheet = nameSheet.replace("\"", "");
                    //nameSheet = nameSheet.replace("&", "_");
                    if (nameSheet.length() > 30) {
                        nameSheet = nameSheet.substring(0, 30);
                    }
                }
                nameSheet = nameSheet.replaceAll("\\[", "(");
                nameSheet = nameSheet.replaceAll("\\]", ")");                
                writer.write(("nameSheet:" + nameSheet + "\n"));
                i++;
            }
            //writer.write(("varsFile:" + varName + "\n"));
            if (macrosFile != null) {
                writer.write(("macrosFile:" + macrosFile + "\n"));
            }

            if (outReport.getMacrosFileForSheet1() != null && !outReport.getMacrosFileForSheet1().equals("")) {
                writer.write(("macrosFile1:" + curDir + outReport.getMacrosFileForSheet1() + "\n"));
            }
            if (outReport.getMacrosFileForSheet2() != null && !outReport.getMacrosFileForSheet2().equals("")) {
                writer.write(("macrosFile2:" + curDir + outReport.getMacrosFileForSheet2() + "\n"));
            }
            if (outReport.getMacrosFileForSheet3() != null && !outReport.getMacrosFileForSheet3().equals("")) {
                writer.write(("macrosFile3:" + curDir + outReport.getMacrosFileForSheet3() + "\n"));
            }
            if (outReport.macrosFileForSheet4 != null && !outReport.macrosFileForSheet4.equals("")) {
                writer.write(("macrosFile4:" + curDir + outReport.macrosFileForSheet4 + "\n"));
            }
              if (outReport.macrosFileForSheet5 != null && !outReport.macrosFileForSheet5.equals("")) {
                writer.write(("macrosFile5:" + curDir + outReport.macrosFileForSheet5 + "\n"));
            }
            if (outReport.getMacrocProc() != null && !outReport.getMacrocProc().trim().equals("")) {
                writer.write(("macrosProc:" + outReport.getMacrocProc() + "\n"));
            }
            String autoFit = "yes";
            if (!outReport.autoFit) {
                autoFit = "no";
            }
            writer.write("autoFit:" + autoFit + "\n");
            writer.write(("saveToFile:" + xlsName + "\n"));
            writer.write(("showExcel:"+(outReport.isShowExcel()?"yes":"no")+"\n"));
            writer.write(("deleteTMPFile:yes\n"));
            writer.write(("showDiagnostic:no\n"));
            writer.close();
            fs.close();

            /*fs = new FileOutputStream(varName);
            writer = new OutputStreamWriter(fs, "UTF-8");
            writer.write(("[Var]\n"));
            for (RRVar var : outReport.report.vars) {
                writer.write((var.name + "=" + var.value + "\n"));
            }
            writer.close();
            fs.close();*/
            long startTime = new Date().getTime();
            
            if (showProcessLog) {
                ProcessBuilder pb = new ProcessBuilder(EXPORT_PROG_NAME, mkName);
                Process process = pb.start();

                //    long startTime = new Date().getTime();
                System.out.println("\nStart exporting to xls.");

                InputStreamReader isr = new InputStreamReader(process.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                String line = "";
                while ((line = br.readLine()) != null) {
                    outputText += line + "\n";
                    System.out.println(line);
                }

                isr.close();
                br.close();

                long elapsedTime = (new Date().getTime()) - startTime;
                System.out.println("\nFinish exporting to xls. Elapsed time: " + elapsedTime + " ms");
            } else {
                Process pr = Runtime.getRuntime().exec(EXPORT_PROG_NAME + " \"" + mkName + "\"");
                //System.out.println(pr.exitValue());
                pr.waitFor();
            }
            
            if (!new File(xlsName).exists()) {
                // additional checking...
                try{
                  //Thread.currentThread().wait(1000);
                  Thread.sleep(1000);
                }catch(Exception ew){}
                
                if (!new File(xlsName).exists()) {
                  throw new UserException("Error..", "XLS File creating error.");
                }
            } else {                
            }
            
            (new File(curDir + mkName)).delete();
            //(new File(curDir + varName)).delete();
            
           return xlsName;
        } catch (UserException ue) {
          ue.printStackTrace();
            throw ue;
        } catch (Exception e) {
          e.printStackTrace();
            throw new UserException("Create XLS-file - error !", outputText + Tools.traceError(e));
        }
        // }
    }

    private static synchronized void xlsFormulaRemover2(String fileNameIn) throws UserException {
        synchronized (xslObject) {
            formulaRemoverStarted = true;
            try {
                
                File dir = new File(EXPORT_TMP_PATH);
                dir.mkdirs();
                String newFileName = Tools.createName("convert_", 10);

                newFileName = new File(dir.getAbsolutePath() + "\\" + newFileName + "." + getXLSExt(false)).getAbsolutePath();
                String curDir = null;
                try {
                    curDir = new File(".").getCanonicalPath() + "/";
                } catch (IOException e) {
                    throw new UserException("Exception " + e.getMessage(), Tools.traceError(e));
                }
                //String fileNameInFull = new File(curDir + fileNameIn).getAbsolutePath();
                String fileNameInFull = fileNameIn.indexOf(":") > 0 ? fileNameIn : (curDir + fileNameIn);
                int pos = fileNameIn.lastIndexOf("\\");
                String outputText = "Console output:\n";
                try {
                    long startTime = new Date().getTime();
                    System.out.println("\nStart formula remover.");               
                    if (showProcessLog) {
                        ProcessBuilder pb = new ProcessBuilder(new String[]{EXPORT_PROG_NAME, "-fr", fileNameInFull, newFileName});
                        Process process = pb.start();

                        InputStreamReader isr = new InputStreamReader(process.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            outputText += line + "\n";
                            System.out.println(line);
                        }

                        long elapsedTime = (new Date().getTime()) - startTime;
                        System.out.println("\nFinish formula remover. Elapsed time: " + elapsedTime + " ms");
                    } else {
                        Process pr = Runtime.getRuntime().exec(EXPORT_PROG_NAME + " -fr " + "\"" + fileNameInFull + "\" \"" + newFileName + "\"");
                        pr.waitFor();
                    }

                    if (!new File(newFileName).exists()) {
                        throw new UserException("Error..", "Formula remover is error.");
                    } else {
                        new File(fileNameInFull).delete();
                        new File(newFileName).renameTo(new File(fileNameInFull));
                    }
                } catch (Exception e) {
                    throw new UserException("Create XLS-file - error !", Tools.traceError(e));
                }
            } finally {
                formulaRemoverStarted = false;
            }
        }
    }

    public static String xlsFormulaRemoverInNewFile(String fileNameIn) throws UserException {
      if (fileNameIn.indexOf(".csv")>0) return fileNameIn;
      return xlsFormulaRemoverInNewFile(fileNameIn, true);
    }

    public static synchronized String xlsFormulaRemoverInNewFile(String fileNameIn, boolean deleteOriginal) throws UserException {
        synchronized (xslObject) {
            formulaRemoverStarted = true;
            try {    
                File dir = new File(EXPORT_TMP_PATH);
                dir.mkdirs();
                String outputText = "Console output:\n";
                String newFileName = Tools.createName("convert_", 10);
                newFileName = dir.getAbsolutePath() + "\\" + newFileName + "." + getXLSExt(false);
                String curDir = null;
                try {
                    curDir = new File(".").getCanonicalPath() + "/";
                } catch (IOException e) {
                    throw new UserException("Exception " + e.getMessage(), Tools.traceError(e));
                }
                String fileNameInFull = fileNameIn.indexOf(":") > 0 ? fileNameIn : (curDir + fileNameIn);
                int pos = fileNameIn.lastIndexOf("\\");

                try {

                    // !!! important !!!
                    //  Process pr = Runtime.getRuntime().exec(EXPORT_PROG_NAME + " -fr " + "\"" + fileNameInFull + "\" \"" + newFileName + "\"");
                    //  pr.waitFor();

                    if (showProcessLog) {
                        long startTime = new Date().getTime();
                        System.out.println("\nStart formula remover.");

                        ProcessBuilder pb = new ProcessBuilder(new String[]{EXPORT_PROG_NAME, "-fr", fileNameInFull, newFileName});
                        Process process = pb.start();

                        InputStreamReader isr = new InputStreamReader(process.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            outputText += line + "\n";
                            System.out.println(line);
                        }
                        long elapsedTime = (new Date().getTime()) - startTime;
                        System.out.println("\nFinish formula remover. Elapsed time: " + elapsedTime + " ms");
                    } else {
                        Process pr = Runtime.getRuntime().exec(EXPORT_PROG_NAME + " -fr " + "\"" + fileNameInFull + "\" \"" + newFileName + "\"");
                        pr.waitFor();
                    }


                    if (!new File(newFileName).exists()) {
                        throw new UserException("Error..", "Formula remover is error.");
                    } else {
                        if (deleteOriginal) {
                            new File(fileNameInFull).delete();
                        }
                        //      new File(newFileName).renameTo(new File(fileNameIn));
                    }
                } catch (Exception e) {
                    throw new UserException("Create XLS-file - error !", Tools.traceError(e));
                }
                return newFileName;
            } finally {
                formulaRemoverStarted = false;
            }
        }
    }        
}
