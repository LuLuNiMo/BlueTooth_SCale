package com.example.scalebluetooth.File;

import android.content.Context;
import android.icu.util.LocaleData;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.record.formula.functions.T;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//* EXCEL的格式設定 字型大小 顏色 對齊方式、背景顏色等...

public class ExcelFormat {
    private WritableFont font14, font10, font12;
    private WritableCellFormat format14, format10, format12;


    public void Format() {
        try {
            font14 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            font14.setColour(jxl.format.Colour.LIGHT_BLUE);

            format14 = new WritableCellFormat(font14);
            format14.setAlignment(jxl.format.Alignment.CENTRE);
            format14.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            format14.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            font12 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
            format12 = new WritableCellFormat(font12);
            format12.setAlignment(jxl.format.Alignment.CENTRE);
            format12.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            format12.setBackground(Colour.WHITE);


            font10 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            format10 = new WritableCellFormat(font10);
            format10.setAlignment(jxl.format.Alignment.CENTRE);
            format10.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            format10.setBackground(Colour.GRAY_25);


        } catch (WriteException e) {
            Log.e("Format-WriteException--", e.getMessage());
            e.printStackTrace();
        }


    }


    /* 初始化 EXCEL*/
    public void initExcel(String fileName, String[] colName) {
        Format();
        WritableWorkbook book = null; //book   活頁簿
        WritableSheet sheet;       //Sheet   工作表

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            book = Workbook.createWorkbook(file);
            sheet = book.createSheet("條碼秤重資料", 0);
            sheet.addCell((WritableCell) new Label(0, 0, fileName, format14)); //建立標題欄

            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], format12));
            }

            sheet.setRowView(0, 340); //設定行高
            book.write();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("initExcel First Error--", ex.getMessage());

        } finally {
            if (book != null) {
                try {
                    book.close();
                } catch (IOException ex) {
                    Log.e("initExcel IOException--", ex.getMessage());
                    ex.printStackTrace();
                } catch (WriteException ex) {
                    Log.e("initExcel Write--", ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

    }

    /* 寫入 EXCEL*/

    @SuppressWarnings("unchecked")
    public void WriteEcel(ArrayList<ArrayList<String>> objList, String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook;
            InputStream in;
            Workbook book;
            WritableSheet sheet;

            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding("UTF-8"); //編碼TYPE
                in = new FileInputStream(new File(fileName));
                book = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), book);
                sheet = writebook.getSheet(0);

                for (int i = 0; i < objList.size(); i++) {
                    ArrayList<String> list = objList.get(i);

                    for (int j = 0; j < list.size(); j++) {
                        sheet.addCell(new Label(j, i + 1, list.get(j), format12));

                        if (list.get(j).length() <= 5) { //設定列寬
                            sheet.setColumnView(j, list.get(j).length() + 5);
                        } else {
                            sheet.setColumnView(j, list.get(j).length() + 8);
                        }
                    }

                    sheet.setRowView(i + 1, 350); //設定行高
                }

                writebook.write();

                in.close();
                writebook.close();
                Log.i("Excel Output ","----SUCCESS!!");
                Toast.makeText(c, "匯出EXCEL成功", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException--", e.getMessage());
                Toast.makeText(c, "匯出EXCEL失敗", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException--", e.getMessage());
                e.printStackTrace();
                Toast.makeText(c, "匯出EXCEL失敗", Toast.LENGTH_SHORT).show();
            } catch (BiffException e) {
                Log.e("BiffException--", e.getMessage());
                e.printStackTrace();
                Toast.makeText(c, "匯出EXCEL失敗", Toast.LENGTH_SHORT).show();
            } catch (RowsExceededException e) {
                Log.e("RowsExceededException--", e.getMessage());
                Toast.makeText(c, "匯出EXCEL失敗", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (WriteException e) {
                Log.e("WriteException--", e.getMessage());
                Toast.makeText(c, "匯出EXCEL失敗", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}














