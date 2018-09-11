/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import KKV.Utils.JDEDate;
import KKV.Utils.Tools;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.swing.JTable;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static vs.time.kkv.connector.MainForm._toLog;
import vs.time.kkv.connector.MainlPannels.stage.STAGE_COLUMN;
import vs.time.kkv.models.VS_STAGE;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public class TableToXLS {

  public static Workbook tableToXLS2(Workbook wb, String sheetName, String raceCaption, String stageCaption, JTable jTable, List<STAGE_COLUMN> columns, boolean showExcel) {
    try {
      boolean createNew = (wb == null);
      if (createNew) {
        wb = new XSSFWorkbook(); //or new HSSFWorkbook();
      }
      Sheet sheet = wb.createSheet(sheetName);

      Font font2 = wb.createFont();
      font2.setFontHeightInPoints((short) 14);
      font2.setFontName("Calibri");
      //font2.setColor(IndexedColors.GREEN.getIndex());
      CellStyle style2 = wb.createCellStyle();
      style2.setFont(font2);

      CellStyle styleHeader = wb.createCellStyle();
      styleHeader.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
      styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
      styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
      styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
      styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderRight(CellStyle.BORDER_THIN);
      styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderTop(CellStyle.BORDER_THIN);
      styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setFont(font2);

      CellStyle styleTable = wb.createCellStyle();
      styleTable.setBorderBottom(CellStyle.BORDER_THIN);
      styleTable.setBottomBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderLeft(CellStyle.BORDER_THIN);
      styleTable.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderRight(CellStyle.BORDER_THIN);
      styleTable.setRightBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderTop(CellStyle.BORDER_THIN);
      styleTable.setTopBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setFont(font2);

      CellStyle styleTableRight = wb.createCellStyle();
      styleTableRight.cloneStyleFrom(styleTable);
      styleTableRight.setAlignment(XSSFCellStyle.ALIGN_RIGHT);

      sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 7));
      sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));
      sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));

      Cell cell0 = sheet.createRow(0).createCell(1);
      cell0.setCellValue(raceCaption);
      cell0.setCellStyle(style2);

      Cell cell1 = sheet.createRow(1).createCell(1);
      cell1.setCellValue(stageCaption);
      cell1.setCellStyle(style2);

      Cell cell2 = sheet.createRow(2).createCell(1);
      cell2.setCellValue("");
      cell2.setCellStyle(style2);

      int rowCount = jTable.getRowCount();
      int colCount = jTable.getColumnCount();
      Row header = sheet.createRow(3);
      for (int i = 0; i < colCount; i++) {
        Cell cell = header.createCell(i + 1);
        cell.setCellValue(new XSSFRichTextString(jTable.getColumnName(i)));
        cell.setCellStyle(styleHeader);
      }

      for (int row = 0; row < rowCount; row++) {
        Row tableRow = sheet.createRow(4 + row);
        for (int col = 0; col < colCount; col++) {
          boolean itLapTime = false;
          if (jTable.getColumnName(col).toLowerCase().indexOf("lap") >= 0) {
            itLapTime = true;
          }
          Object obj = jTable.getModel().getValueAt(row, col);
          Cell cell = tableRow.createCell(col + 1);
          cell.setCellValue(new XSSFRichTextString(obj.toString()));
          cell.setCellStyle(styleTable);
          try {
            String cellID = columns.get(col).cellID;
            if (cellID.equalsIgnoreCase("num") || cellID.equalsIgnoreCase("TXT_RIGHT")
                    || cellID.equalsIgnoreCase("int")) {
              cell.setCellStyle(styleTableRight);
            }
          } catch (Exception e) {
          }
        }
      }

      sheet.setColumnWidth(0, 5 * 100);
      for (int col = 0; col < colCount; col++) {
        try {
          sheet.setColumnWidth(col + 1, (short) jTable.getColumnModel().getColumn(col).getWidth() * 50);
          //columns.get(col).width*100);         
        } catch (Exception e) {
        }
      }

      if (createNew) {
        if (showExcel) {
          new File("XLS").mkdirs();
          String xlsFile = "XLS/" + Tools.createName(new JDEDate().getDateAsYYYYMMDD("-") + "_", 5) + ".xlsx";
          FileOutputStream fileOut = new FileOutputStream(xlsFile);
          wb.write(fileOut);
          fileOut.close();

          OSDetector.open(new File(xlsFile));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return wb;
  }

  public static void groups2xls(VS_STAGE stage) {
    try {
      Workbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();     
      Sheet sheet = wb.createSheet(stage.CAPTION);

      Font font2 = wb.createFont();
      font2.setFontHeightInPoints((short) 14);
      font2.setFontName("Calibri");
      //font2.setColor(IndexedColors.GREEN.getIndex());
      CellStyle style2 = wb.createCellStyle();
      style2.setFont(font2);

      CellStyle styleHeader = wb.createCellStyle();
      styleHeader.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
      styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
      styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
      styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
      styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderRight(CellStyle.BORDER_THIN);
      styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setBorderTop(CellStyle.BORDER_THIN);
      styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
      styleHeader.setFont(font2);

      CellStyle styleTable = wb.createCellStyle();
      styleTable.setBorderBottom(CellStyle.BORDER_THIN);
      styleTable.setBottomBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderLeft(CellStyle.BORDER_THIN);
      styleTable.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderRight(CellStyle.BORDER_THIN);
      styleTable.setRightBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setBorderTop(CellStyle.BORDER_THIN);
      styleTable.setTopBorderColor(IndexedColors.BLACK.getIndex());
      styleTable.setFont(font2);

      CellStyle styleTableRight = wb.createCellStyle();
      styleTableRight.cloneStyleFrom(styleTable);
      styleTableRight.setAlignment(XSSFCellStyle.ALIGN_RIGHT);

      sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 7));
      sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));

      Cell cell0 = sheet.createRow(0).createCell(1);
      cell0.setCellValue(stage.CAPTION);
      cell0.setCellStyle(style2);

      Cell cell1 = sheet.createRow(1).createCell(1);
      cell1.setCellValue("");
      cell1.setCellStyle(style2);

      String[] captions = new String[]{"Group", "Pilot", "Channel"};
      int colCount = captions.length;
      Row header = sheet.createRow(2);
      for (int i = 0; i < colCount; i++) {
        Cell cell = header.createCell(i + 1);
        cell.setCellValue(captions[i]);
        cell.setCellStyle(styleHeader);
      }

      int row = 0;
      for (Integer groupNum : stage.groups.keySet()) {
        VS_STAGE_GROUP group = stage.groups.get(groupNum);
        for (VS_STAGE_GROUPS usr : group.users) {
          Row tableRow = sheet.createRow(3 + row);
          String line = "data:";
          line += "Group " + group.GROUP_NUM + ":$$" + usr.PILOT + "$$:" + usr.CHANNEL + ":";

          for (int col = 0; col < captions.length; col++) {
            Object obj = "";
            if (col == 0) {
              obj = group.GROUP_NUM;
            }
            if (col == 1) {
              obj = usr.PILOT;
            }
            if (col == 2) {
              obj = usr.CHANNEL;
            }
            Cell cell = tableRow.createCell(col + 1);
            cell.setCellValue(new XSSFRichTextString(obj.toString()));
            cell.setCellStyle(styleTable);
          }
          row++;
        }
      }
      new File("XLS").mkdirs();
      String xlsFile = "XLS/" + Tools.createName(new JDEDate().getDateAsYYYYMMDD("-") + "_", 5) + ".xlsx";
      FileOutputStream fileOut = new FileOutputStream(xlsFile);
      wb.write(fileOut);
      fileOut.close();

      OSDetector.open(new File(xlsFile));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
