package com.example.demo.service;

import com.example.demo.model.WorkOrderWorkProcedure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/3 20:26
 */
public interface ExcelService {
    void SaveWorkOrderWorkProcedure(@Param("item") List<WorkOrderWorkProcedure> workOrderWorkProcedures);
    List<WorkOrderWorkProcedure> getAllWorkOrderWorkProcedure();
}
