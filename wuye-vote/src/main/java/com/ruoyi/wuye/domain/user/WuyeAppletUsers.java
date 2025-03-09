package com.ruoyi.wuye.domain.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小程序用户对象 wuye_applet_users
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
public class WuyeAppletUsers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小程序用户ID */
    private Long appletId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String nikeName;

    /** 小程序用户OPENID */
    @Excel(name = "小程序用户OPENID")
    private String openid;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 业主姓名 */
    @Excel(name = "业主姓名")
    private String name;

    /** 业主电话 */
    @Excel(name = "业主电话")
    private String mobile;

    /** 业主身份证号 */
    @Excel(name = "业主身份证号")
    private String idNumber;

    /** 房屋IDs */
    @Excel(name = "房屋IDs")
    private String houseIds;

    public void setAppletId(Long appletId) 
    {
        this.appletId = appletId;
    }

    public Long getAppletId() 
    {
        return appletId;
    }

    public void setNikeName(String nikeName) 
    {
        this.nikeName = nikeName;
    }

    public String getNikeName() 
    {
        return nikeName;
    }

    public void setOpenid(String openid) 
    {
        this.openid = openid;
    }

    public String getOpenid() 
    {
        return openid;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }

    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }

    public void setHouseIds(String houseIds) 
    {
        this.houseIds = houseIds;
    }

    public String getHouseIds() 
    {
        return houseIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appletId", getAppletId())
            .append("nikeName", getNikeName())
            .append("openid", getOpenid())
            .append("avatar", getAvatar())
            .append("name", getName())
            .append("mobile", getMobile())
            .append("idNumber", getIdNumber())
            .append("houseIds", getHouseIds())
            .toString();
    }
}
