package com.example.demo.util;


import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/2 14:36
 */
@Slf4j
public class FileUtils {
    /**
     * 下载文件
     */
    public static String downloadFiles(HttpServletRequest request, HttpServletResponse response, String fileName) {
        if (fileName.isEmpty()) {
            return "文件名不能为空";
        }

        //设置文件路径
        try {
            //设置要下载的文件的名称
            response.setHeader("Content-Disposition",
                    "attchement;filename=" + new String(fileName.getBytes("utf-8"),
                            "ISO8859-1"));
            //通知客服文件的MIME类型
            response.setContentType("application/msexcel;charset=UTF-8");
            //获取文件的路径
            String filePath = "D:/file/"+fileName;
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return "文件下载成功";
    }

    /**
     * 判断文件大小
     *  @param file  文件
     *  @param size  限制大小
     *  @param unit  限制单位（B,K,M,G）
     */
    public static boolean checkFileSize(MultipartFile file, int size, String unit){
        if (file.isEmpty() || StringUtils.isEmpty(size) || StringUtils.isEmpty(unit)){
            return false;
        }
        long lent = file.getSize();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) lent;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) lent / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) lent / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) lent / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    /**
     * 封装单个上传方法
     */
    public static String upload(MultipartFile file){
        if (file.isEmpty()){
            return "文件夹为空";
        }

        //获取文件大小
        long fileSize = file.getSize();
        log.info("文件大小："+fileSize);
        //判断文件的大小
        if (!FileUtils.checkFileSize(file,50,"M")){
            log.error("上传文件规定小于50MB");
            return "上传文件规定小于50MB";
        }
        //获取文件名
        String filename = file.getOriginalFilename();
        log.info("上传的文件名："+filename);

        //获取文件后缀名
        String lastName = filename.substring(filename.lastIndexOf("."));
        log.info("文件后缀名："+lastName);

        ///设置文件存储路径
        String filePath = "D:/file/";
        String path = filePath + filename;
        File dest = new File(path);
        //检测文件是否存在
        if (dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();//创建文件夹
        }
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "上传失败";
    }
}
