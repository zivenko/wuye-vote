package com.ruoyi.wuye.domain.dto;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业小区导出对象
 */
public class BuildingExportDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 小区名称 */
    @Excel(name = "小区")
    private String communityName;

    /** 楼栋名称 */
    @Excel(name = "楼栋")
    private String buildingName;

    /** 单元名称 */
    @Excel(name = "单元")
    private String unitName;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
} 