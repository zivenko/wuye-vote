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

    /** 房屋ID */
    @Excel(name = "房屋ID")
    private Long houseId;

    /** 房屋证明 */
    @Excel(name = "房屋证明")
    private String certificate;

    /** 审核状态（success, fail, uncheck) */
    @Excel(name = "审核状态", readConverterExp = "审核状态（success, fail, uncheck)")
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

    public void setHouseId(Long houseId) 
    {
        this.houseId = houseId;
    }

    public Long getHouseId() 
    {
        return houseId;
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
            .append("houseId", getHouseId())
            .append("certificate", getCertificate())
            .append("checkStatus", getCheckStatus())
            .append("checkErrorMsg", getCheckErrorMsg())
            .append("createTime", getCreateTime())
            .append("checkTime", getCheckTime())
            .append("userId", getUserId())
            .toString();
    }
}
