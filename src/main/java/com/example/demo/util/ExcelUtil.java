package com.example.demo.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.collections4.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/9 20:26
 */
@Log4j2
public class ExcelUtil {
    /**
     * Map集合导出
     *
     * @param list     需要导出的数据
     * @param fileName 导出的文件名
     * @param response HttpServletResponse对象
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception{
        defaultExport(list, fileName, response);
    }

    /**
     * 复杂导出Excel，包括文件名以及表名（不创建表头）
     *
     * @param list      需要导出的数据
     * @param title     表格首行标题（不需要就传null）
     * @param sheetName 工作表名称
     * @param pojoClass 映射的实体类
     * @param fileName  导出的文件名（如果为null，则默认文件名为当前时间戳）
     * @param response  HttpServletResponse对象
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) throws Exception{
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    /**
     * 复杂导出Excel，包括文件名以及表名（创建表头）
     *
     * @param list           需要导出的数据
     * @param title          表格首行标题（不需要就传null）
     * @param sheetName      工作表名称
     * @param pojoClass      映射的实体类
     * @param fileName       导出的文件名
     * @param isCreateHeader 是否创建表头
     * @param response       HttpServletResponse对象
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   boolean isCreateHeader, HttpServletResponse response) throws Exception{
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * 默认导出方法
     *
     * @param list         需要导出的数据
     * @param pojoClass    对应的实体类
     * @param fileName     导出的文件名
     * @param response     HttpServletResponse对象
     * @param exportParams 导出参数实体
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response,
                                      ExportParams exportParams) throws Exception{
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downloadExcel(fileName, workbook, response);
    }

    /**
     * 默认导出方法
     *
     * @param list     Map集合
     * @param fileName 导出的文件名
     * @param response HttpServletResponse对象
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response)throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (null != workbook) {
            downloadExcel(fileName, workbook, response);
        }
    }

    /**
     * Excel导出
     *
     * @param fileName Excel导出
     * @param workbook Excel对象
     * @param response HttpServletResponse对象
     */
    public static void downloadExcel(String fileName, Workbook workbook, HttpServletResponse response) throws Exception{
        try {
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("导出文件名不能为空");
            }
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel; charset=utf-8");
            response.setHeader ("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode (fileName,"UTF-8"));
            response.setHeader("FileName", encodeFileName);
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径来导入Excel
     *
     * @param filePath   文件路径
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  映射的实体类
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception{
        //判断文件是否存在
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            log.error("模板不能为空", e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file       上传的文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  映射的实体类
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception{
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            log.error("excel文件不能为空", e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Class<T> clz) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件转List
     *
     * @param file
     * @param pojoClass
     * @param <T>
     * @return
     */
    public static <T> List<T> fileToList(MultipartFile file, Class<T> pojoClass) throws Exception{
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        List<T> list = ExcelUtil.importExcel(file, 1, 1, pojoClass);
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("未解析到表格数据");
        }
        return list;
    }
}