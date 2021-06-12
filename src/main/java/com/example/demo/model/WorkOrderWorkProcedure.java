package com.example.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/3 20:12
 */

@Data
public class WorkOrderWorkProcedure implements Serializable {
    @Excel(name="工单编号",orderNum = "0")
    private String workOrderNo;
    @Excel(name="工序名称",orderNum = "1")
    private String workProcedureName;
    @Excel(name="工序类型",orderNum = "2")
    private String workProcedureType;
    @Excel(name="备注",orderNum = "3")
    private String comment;
}
