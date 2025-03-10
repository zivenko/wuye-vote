package com.ruoyi.wuye.domain.user;

import lombok.Data;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@Data
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
}
