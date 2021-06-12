package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.model.WorkOrderWorkProcedure;
import com.example.demo.service.ExcelService;
import com.example.demo.util.ExcelUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/9 21:31
 */
@RestController
//@Api(value = "ExcelController", tags = { "Excel导入导出访问接口" })
@RequestMapping("/Excel")
public class ExcelController {
    @Autowired
    ExcelService excelService;

    @GetMapping("exportExcel")
    //@ApiOperation("导出Excel表格")
    public void exportExcel(HttpServletResponse response) throws Exception {
        List<WorkOrderWorkProcedure> list =new ArrayList<WorkOrderWorkProcedure>();
        list=excelService.getAllWorkOrderWorkProcedure();
        String fileName="工单工序表.xls";
        ExcelUtil.exportExcel(list, null, "sheet1", WorkOrderWorkProcedure.class, fileName, response);
    }

    @RequestMapping("importExcel")
    //@ApiOperation("导入Excel表格")
    @ResponseBody
    public Result importExcel(@RequestParam MultipartFile file){
        List<WorkOrderWorkProcedure> list =new ArrayList<WorkOrderWorkProcedure>();
        list= ExcelUtil.importExcel(file, WorkOrderWorkProcedure.class);
        //System.out.println("888888888"+list);
        excelService.SaveWorkOrderWorkProcedure(list);
        return Result.succ("导入成功");
    }

}
