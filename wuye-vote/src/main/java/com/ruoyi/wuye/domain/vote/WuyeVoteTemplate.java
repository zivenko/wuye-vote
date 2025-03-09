package com.ruoyi.wuye.domain.vote;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.DateUtils;

/**
 * 投票模板对象 wuye_vote_template
 *
 * @author ruoyi
 * @date 2025-03-09
 */
public class WuyeVoteTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long templateId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 选项(JSON形式) */
    @Excel(name = "选项(JSON形式)")
    private String choices;

    /** 投票次数 */
    @Excel(name = "投票次数")
    private Long voteTimes;

    /** 规则 */
    @Excel(name = "规则")
    private String rule;

    /** 小区IDs */
    @Excel(name = "小区IDs")
    private String communityIds;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态（0-未开始 1-进行中 2-已结束） */
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Excel(name = "状态", readConverterExp = "0=未开始,1=进行中,2=已结束")
    private transient Integer status;

    /** 参与人数 */
    private transient Long voteCount;

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setChoices(String choices)
    {
        this.choices = choices;
    }

    public String getChoices()
    {
        return choices;
    }

    public void setVoteTimes(Long voteTimes)
    {
        this.voteTimes = voteTimes;
    }

    public Long getVoteTimes()
    {
        return voteTimes;
    }

    public void setRule(String rule)
    {
        this.rule = rule;
    }

    public String getRule()
    {
        return rule;
    }

    public void setCommunityIds(String communityIds)
    {
        this.communityIds = communityIds;
    }

    public String getCommunityIds()
    {
        return communityIds;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * 计算当前投票状态
     * @return 0-未开始 1-进行中 2-已结束
     */
    public Integer calculateStatus() {
        Date now = DateUtils.getNowDate();
        if (startTime == null || endTime == null) {
            return 0;
        }
        
        // 将日期转换为相同格式进行比较
        String nowStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String startStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, startTime);
        String endStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, endTime);
        
        try {
            Date normalizedNow = DateUtils.parseDate(nowStr);
            Date normalizedStart = DateUtils.parseDate(startStr);
            Date normalizedEnd = DateUtils.parseDate(endStr);
            
            if (normalizedNow.before(normalizedStart)) {
                return 0; // 未开始
            } else if (normalizedNow.after(normalizedEnd)) {
                return 2; // 已结束
            } else {
                return 1; // 进行中
            }
        } catch (Exception e) {
            return 0; // 出现异常时默认为未开始
        }
    }

    /**
     * 判断当前是否在投票时间范围内
     * @return true-在范围内 false-不在范围内
     */
    public boolean isInVoteTimeRange() {
        return calculateStatus() == 1;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        if (status == null) {
            status = calculateStatus();
        }
        return status;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("templateId", getTemplateId())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("choices", getChoices())
                .append("voteTimes", getVoteTimes())
                .append("rule", getRule())
                .append("communityIds", getCommunityIds())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .toString();
    }
}
