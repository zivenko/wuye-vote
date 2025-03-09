package com.ruoyi.wuye.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class BuildingImportDTO {
    @ExcelProperty("小区")
    private String community;

    @ExcelProperty("楼栋")
    private String building;

    @ExcelProperty("单元")
    private String unit;

    @ExcelProperty("地址")
    private String address;
}