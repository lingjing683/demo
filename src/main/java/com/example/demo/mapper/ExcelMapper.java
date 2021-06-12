package com.example.demo.mapper;

import com.example.demo.model.WorkOrderWorkProcedure;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/3 20:19
 */
@Repository
public interface ExcelMapper {
    void SaveWorkOrderWorkProcedure(@Param("item") List<WorkOrderWorkProcedure> workOrderWorkProcedures);
    List<WorkOrderWorkProcedure> getAllWorkOrderWorkProcedure();
}
