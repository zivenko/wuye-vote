package com.ruoyi.wuye.domain.vote;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 投票记录对象 wuye_vote_record
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public class WuyeVoteRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 投票记录ID */
    private Long voteId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long appletId;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long templateId;

    /** 用户选择的选项 */
    @Excel(name = "用户选择的选项")
    private String choices;

    /** 投票时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteTime;

    /** 投票状态(有效: 1/无效: 0) */
    @Excel(name = "投票状态(有效: 1/无效: 0)")
    private Long status;

    /** 通过管理员投票 */
    @Excel(name = "通过管理员投票")
    private Long byAdmin;

    public void setVoteId(Long voteId) 
    {
        this.voteId = voteId;
    }

    public Long getVoteId() 
    {
        return voteId;
    }

    public void setAppletId(Long appletId) 
    {
        this.appletId = appletId;
    }

    public Long getAppletId() 
    {
        return appletId;
    }

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    public void setChoices(String choices) 
    {
        this.choices = choices;
    }

    public String getChoices() 
    {
        return choices;
    }

    public void setVoteTime(Date voteTime) 
    {
        this.voteTime = voteTime;
    }

    public Date getVoteTime() 
    {
        return voteTime;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setByAdmin(Long byAdmin) 
    {
        this.byAdmin = byAdmin;
    }

    public Long getByAdmin() 
    {
        return byAdmin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("voteId", getVoteId())
            .append("appletId", getAppletId())
            .append("templateId", getTemplateId())
            .append("choices", getChoices())
            .append("voteTime", getVoteTime())
            .append("status", getStatus())
            .append("byAdmin", getByAdmin())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
