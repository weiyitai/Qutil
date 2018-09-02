package com.qianbajin.qutil.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
/*
 * @des
 * @Created  wWX407408  at 2017/7/31  17:25 
 *
 */
public class ExcelUtil {

    /**
     * 读取excel表格,返回一张表格
     *
     * @param is        文件输入流
     * @param boolIndex excel表格的第几张表,下标从0开始
     * @return
     */
    public static Sheet getSheet(InputStream is, int boolIndex) {
        try {
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(boolIndex);
            workbook.close();
            return sheet;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取excel表格的某一纵列，返回一个cell数组,通过cell.getContent()获得表格里内容
     *
     * @param is        文件输入流
     * @param boolIndex excel表格的第几张表,下标从0开始
     * @param column    要读取的列
     */
    public static Cell[] getColumn(InputStream is, int boolIndex, int column) {
        try {
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(boolIndex);
            Cell[] column1 = sheet.getColumn(column);
            return column1;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取excel表格的某一行,返回一个cell数组,通过cell.getContent()获得表格里内容
     *
     * @param is        文件输入流
     * @param boolIndex excel表格的第几张表,下标从0开始
     * @param row       要读取的行
     */
    public static Cell[] getRow(InputStream is, int boolIndex, int row) {
        try {
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(boolIndex);
            Cell[] row1 = sheet.getRow(row);
            workbook.close();
            return row1;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将excel表格的某一列写到新的excel表格里
     *
     * @param is        文件输入流
     * @param boolIndex excel表格的第几张表,下标从0开始
     * @param column    要读取的列
     * @param file      新的excel表格文件,xls格式或 xlsx格式
     */
    public static boolean writeColumn2Excel(InputStream is, int boolIndex, int column, File file) {
        boolean success = false;
        try {
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(boolIndex);
            Cell[] cells = sheet.getColumn(column);
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet(file.getName(), 0);
            for (int i = 0; i < cells.length; i++) {
                Cell cell = cells[i];
                Label label = new Label(0, i, cell.getContents());
                sheet1.addCell(label);
            }
            workbook.close();
            workbook1.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 将excel表格的某一列写到文件里
     *
     * @param is        文件输入流
     * @param boolIndex excel表格的第几张表,下标从0开始
     * @param column    要读取的列
     * @param file      要写到的文件
     */
    public static boolean writeColumn2File(InputStream is, int boolIndex, int column, File file) {
        boolean success = false;
        try {
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(boolIndex);
            Cell[] cells = sheet.getColumn(column);
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            for (Cell cell : cells) {
                bw.write(cell.getContents());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            workbook.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return success;
    }
}
