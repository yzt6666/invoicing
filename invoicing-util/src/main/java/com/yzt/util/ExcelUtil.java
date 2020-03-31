package com.yzt.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
    public XSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [][]values, XSSFWorkbook wb, String mergeCell, Integer cellNum ){

        // 第一步，创建一个XSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new XSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        XSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 创建一个居中格式

        //声明列对象
        XSSFCell cell = null;

        if (StringUtils.isNotEmpty(mergeCell)) {
            //合并单元格
//            CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,cellNum);//起始行,结束行,起始列,结束列
//            sheet.addMergedRegion(callRangeAddress);
            cell = row.createCell(0);
            cell.setCellValue(mergeCell);
            cell.setCellStyle(style);
            row = sheet.createRow(1);
            // HSSFCell cell = null;
            //创建标题
            for(int i=0;i<title.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
//                cell.setCellStyle(style);
            }

            //创建内容
            for(int i=0;i<values.length;i++){
                row = sheet.createRow(i + 2);
                for(int j=0;j<values[i].length;j++){
                    //将内容按顺序赋给对应的列对象
                    row.createCell(j).setCellValue(values[i][j]);
                }
            }
        }else{

            // HSSFCell cell = null;
            //创建标题
            for(int i=0;i<title.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
//                cell.setCellStyle(style);
            }

            //创建内容
            for(int i=0;i<values.length;i++){
                row = sheet.createRow(i + 1);
                for(int j=0;j<values[i].length;j++){
                    //将内容按顺序赋给对应的列对象
                    row.createCell(j).setCellValue(values[i][j]);
                }
            }

        }
        return wb;
    }

}
