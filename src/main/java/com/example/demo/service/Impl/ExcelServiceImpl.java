package com.example.demo.service.Impl;

import com.example.demo.mapper.ExcelMapper;
import com.example.demo.model.WorkOrderWorkProcedure;
import com.example.demo.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingjing
 * @version 1.0
 * @date 2021/6/3 20:27
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    ExcelMapper excelMapper;

    @Override
    public void SaveWorkOrderWorkProcedure(List<WorkOrderWorkProcedure> workOrderWorkProcedures) {
        excelMapper.SaveWorkOrderWorkProcedure(workOrderWorkProcedures);
    }

    @Override
    public List<WorkOrderWorkProcedure> getAllWorkOrderWorkProcedure() {
        return excelMapper.getAllWorkOrderWorkProcedure();
    }
}
