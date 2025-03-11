package com.ruoyi.wuye.domain.buildings;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 房屋绑定审核对象 wuye_house_bind_check
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
public class WuyeHouseBindCheck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 检查表ID */
    private Long checkId;

    /** 小程序用户ID */
    @Excel(name = "小程序用户ID")
    private Long appletId;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String appletUserName;

    /** 用户身份证号 */
    @Excel(name = "身份证号")
    private String appletUserIdNumber;

    /** 房屋ID */
    @Excel(name = "房屋ID")
    private Long houseId;

    /** 小区名称 */
    @Excel(name = "小区")
    private String districtName;

    /** 楼栋名称 */
    @Excel(name = "楼栋")
    private String buildingName;

    /** 单元名称 */
    @Excel(name = "单元")
    private String unitName;

    /** 房号 */
    @Excel(name = "房号")
    private String roomNumber;

    /** 房屋证明 */
    @Excel(name = "房屋证明")
    private String certificate;

    /** 审核状态（success, fail, uncheck) */
    @Excel(name = "审核状态", readConverterExp = "success=已通过,fail=未通过,uncheck=待审核")
    private String checkStatus;

    /** 审核失败信息 */
    @Excel(name = "审核失败信息")
    private String checkErrorMsg;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /** 审核员ID */
    @Excel(name = "审核员ID")
    private Long userId;

    public void setCheckId(Long checkId) 
    {
        this.checkId = checkId;
    }

    public Long getCheckId() 
    {
        return checkId;
    }

    public void setAppletId(Long appletId) 
    {
        this.appletId = appletId;
    }

    public Long getAppletId() 
    {
        return appletId;
    }

    public String getAppletUserName() {
        return appletUserName;
    }

    public void setAppletUserName(String appletUserName) {
        this.appletUserName = appletUserName;
    }

    public String getAppletUserIdNumber() {
        return appletUserIdNumber;
    }

    public void setAppletUserIdNumber(String appletUserIdNumber) {
        this.appletUserIdNumber = appletUserIdNumber;
    }

    public void setHouseId(Long houseId) 
    {
        this.houseId = houseId;
    }

    public Long getHouseId() 
    {
        return houseId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCertificate(String certificate) 
    {
        this.certificate = certificate;
    }

    public String getCertificate() 
    {
        return certificate;
    }

    public void setCheckStatus(String checkStatus) 
    {
        this.checkStatus = checkStatus;
    }

    public String getCheckStatus() 
    {
        return checkStatus;
    }

    public void setCheckErrorMsg(String checkErrorMsg) 
    {
        this.checkErrorMsg = checkErrorMsg;
    }

    public String getCheckErrorMsg() 
    {
        return checkErrorMsg;
    }

    public void setCheckTime(Date checkTime) 
    {
        this.checkTime = checkTime;
    }

    public Date getCheckTime() 
    {
        return checkTime;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("checkId", getCheckId())
            .append("appletId", getAppletId())
            .append("appletUserName", getAppletUserName())
            .append("appletUserIdNumber", getAppletUserIdNumber())
            .append("houseId", getHouseId())
            .append("districtName", getDistrictName())
            .append("buildingName", getBuildingName())
            .append("unitName", getUnitName())
            .append("roomNumber", getRoomNumber())
            .append("certificate", getCertificate())
            .append("checkStatus", getCheckStatus())
            .append("checkErrorMsg", getCheckErrorMsg())
            .append("createTime", getCreateTime())
            .append("checkTime", getCheckTime())
            .append("userId", getUserId())
            .toString();
    }
}
