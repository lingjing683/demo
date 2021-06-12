package com.example.demo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/9 16:14
 */
public class ExcelReaderUtils {
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExcelReaderUtils(String filePath) {
        if (filePath == null) {
            return;
        }
        String type = filePath.substring(filePath.lastIndexOf("."));
        try {
            FileInputStream fis = new FileInputStream(filePath);
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(fis);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(fis);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            logger.error("文件未找到", e);
        } catch (IOException e) {
            logger.error("IO异常", e);
        }
    }

    public List<List<String>> readExcel() {
        List<List<String>> list = new ArrayList<>();
        int sheetNum = wb.getActiveSheetIndex();
        for (int x = 0; x <= sheetNum; x++) {
            sheet = wb.getSheetAt(x);
            int rowNum = sheet.getLastRowNum();
            List<String> context = new ArrayList<>();
            for (int y = 0; y < rowNum; y++) {
                row = sheet.getRow(y);
                //判断是否为空行
                if (row != null) {
                    int cellNum = row.getLastCellNum();
                    String cells = "";
                    String cell = "";
                    for (int z = 0; z < cellNum; z++) {

                        //获取每一个单元格中的内容，如果为空将单元格内容替换为null。
                        if (row.getCell(z) == null || row.getCell(z).toString().equals("")) {
                            cell = "null";
                        } else {
                            cell = row.getCell(z).toString();
                        }

                        //将读取到的所有单元格的内容都拼接在一起，形成字符串，并清理格式去掉回车符。
                        if (z == cellNum - 1) {
                            //cells = cells + cell.replaceAll("\n", "。") + "\n";
                            cells = cells;
                        } else {
                            //cells = cells + cell.replaceAll("\n", "。") + "\001";
                            cells = cells + cell.replaceAll("\n", "。") + "  ";
                        }
                    }
                    context.add(cells);
                }
            }
            list.add(context);
        }
        return list;
    }

    public void writeFile(List<List<String>> contextList, String filePath){
        writeFile(contextList,filePath,null);
    }
    public void writeFile(List<List<String>> contextList, String filePath, String fileType) {
        //判断文件路径是否存在，如果不存在则创建
        File path = new File(filePath);
        if (!path.exists()) {
            //System.out.println("路径不存在，正在创建...");
            path.mkdirs();
            //System.out.println("创建完成!");
        }

        //判断输入的文件生成类型
        String type = "";
        if (fileType != null && fileType != "") {
            type = "." + fileType;
        }

        String sheetName = null;
        File file = null;
        FileWriter fw = null;
        for (int i=0; i<contextList.size();i++){
            List<String> context = contextList.get(i);
            sheetName = wb.getSheetAt(i).getSheetName();
            file = new File(path.getAbsolutePath()+"/"+sheetName+type);
            try {
                file.createNewFile();
                fw = new FileWriter(file);
                for (int j=0; j<context.size();j++){
                    fw.write(context.get(j));
                    fw.flush();
                }
            } catch (IOException e) {
                System.out.println("生成输出文件出错！");
                e.printStackTrace();
            }
            int j=i+1;
            System.out.println("第"+j+"个文件："+sheetName+type+"，写入完成");
        }
    }
}
