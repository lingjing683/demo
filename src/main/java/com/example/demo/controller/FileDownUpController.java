package com.example.demo.controller;

import com.example.demo.service.ExcelService;
import com.example.demo.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/2 14:40
 */
@RestController
@RequestMapping("/file")
public class FileDownUpController {

    @Autowired
    ExcelService excelService;

    /**
     * 单文件上传
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public String upload(String userId,@RequestParam("file") MultipartFile file){
        String result= FileUtils.upload(file);
        if (result == null){
            return null;
        }
        return result;
    }


    /**
     * 文件下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "1.txt";// 文件名
        String result = FileUtils.downloadFiles(request, response, fileName);
        if (request == null) {
            return null;
        }
        return result;
    }
}
