package com.ruoyi.wuye.domain.buildings;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业房屋对象 wuye_houses
 *
 * @author ruoyi
 * @date 2025-03-08
 */
public class WuyeHouses extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房屋ID */
    private Long houseId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long districtId;

    /** 楼栋ID */
    @Excel(name = "楼栋ID")
    private Long buildingId;

    /** 单元ID */
    @Excel(name = "单元ID")
    private Long unitId;

    /** 房号 */
    @Excel(name = "房号")
    private String roomNumber;

    /** 建筑面积 */
    @Excel(name = "建筑面积")
    private BigDecimal area;

    /** 物业类型(高层、洋房、别墅、联排、商铺) */
    @Excel(name = "物业类型(高层、洋房、别墅、联排、商铺)")
    private String type;

    /** 房屋编号 */
    @Excel(name = "房屋编号")
    private String houseNumber;

    /** 业主姓名 */
    @Excel(name = "业主姓名")
    private String ownerNames;

    /** 业主电话 */
    @Excel(name = "业主电话")
    private String ownerMobiles;

    /** 业主身份证号 */
    @Excel(name = "业主身份证号")
    private String ownerIdNumbers;

    /** 小程序绑定人(0：未绑定，1：已绑定) */
    @Excel(name = "小程序绑定人(0：未绑定，1：已绑定)")
    private Long isBind;

    public void setHouseId(Long houseId)
    {
        this.houseId = houseId;
    }

    public Long getHouseId()
    {
        return houseId;
    }

    public void setDistrictId(Long districtId)
    {
        this.districtId = districtId;
    }

    public Long getDistrictId()
    {
        return districtId;
    }

    public void setBuildingId(Long buildingId)
    {
        this.buildingId = buildingId;
    }

    public Long getBuildingId()
    {
        return buildingId;
    }

    public void setUnitId(Long unitId)
    {
        this.unitId = unitId;
    }

    public Long getUnitId()
    {
        return unitId;
    }

    public void setRoomNumber(String roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setArea(BigDecimal area)
    {
        this.area = area;
    }

    public BigDecimal getArea()
    {
        return area;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumber()
    {
        return houseNumber;
    }

    public void setOwnerNames(String ownerNames)
    {
        this.ownerNames = ownerNames;
    }

    public String getOwnerNames()
    {
        return ownerNames;
    }

    public void setOwnerMobiles(String ownerMobiles)
    {
        this.ownerMobiles = ownerMobiles;
    }

    public String getOwnerMobiles()
    {
        return ownerMobiles;
    }

    public void setOwnerIdNumbers(String ownerIdNumbers)
    {
        this.ownerIdNumbers = ownerIdNumbers;
    }

    public String getOwnerIdNumbers()
    {
        return ownerIdNumbers;
    }

    public void setIsBind(Long isBind)
    {
        this.isBind = isBind;
    }

    public Long getIsBind()
    {
        return isBind;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("houseId", getHouseId())
                .append("districtId", getDistrictId())
                .append("buildingId", getBuildingId())
                .append("unitId", getUnitId())
                .append("roomNumber", getRoomNumber())
                .append("area", getArea())
                .append("type", getType())
                .append("houseNumber", getHouseNumber())
                .append("ownerNames", getOwnerNames())
                .append("ownerMobiles", getOwnerMobiles())
                .append("ownerIdNumbers", getOwnerIdNumbers())
                .append("isBind", getIsBind())
                .toString();
    }
}
