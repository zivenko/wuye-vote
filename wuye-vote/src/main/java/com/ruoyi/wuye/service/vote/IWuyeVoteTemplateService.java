package com.ruoyi.wuye.service.vote;

import com.ruoyi.wuye.domain.vote.WuyeVoteTemplate;

import java.util.List;


/**
 * 投票模板Service接口
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public interface IWuyeVoteTemplateService 
{
    /**
     * 查询投票模板
     * 
     * @param templateId 投票模板主键
     * @return 投票模板
     */
    public WuyeVoteTemplate selectWuyeVoteTemplateByTemplateId(Long templateId);

    /**
     * 查询投票模板列表
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 投票模板集合
     */
    public List<WuyeVoteTemplate> selectWuyeVoteTemplateList(WuyeVoteTemplate wuyeVoteTemplate);

    /**
     * 新增投票模板
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 结果
     */
    public int insertWuyeVoteTemplate(WuyeVoteTemplate wuyeVoteTemplate);

    /**
     * 修改投票模板
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 结果
     */
    public int updateWuyeVoteTemplate(WuyeVoteTemplate wuyeVoteTemplate);

    /**
     * 批量删除投票模板
     * 
     * @param templateIds 需要删除的投票模板主键集合
     * @return 结果
     */
    public int deleteWuyeVoteTemplateByTemplateIds(Long[] templateIds);

    /**
     * 删除投票模板信息
     * 
     * @param templateId 投票模板主键
     * @return 结果
     */
    public int deleteWuyeVoteTemplateByTemplateId(Long templateId);

    /**
     * 查询用户可参与的投票模板列表
     * 
     * @param appletId 小程序用户ID
     * @param keyword 搜索关键词
     * @param status 状态过滤
     * @return 投票模板集合
     */
    public List<WuyeVoteTemplate> selectUserVoteTemplateList(Long appletId, String keyword, Integer status);

    /**
     * 根据房屋IDs获取可投票的模板列表
     * 
     * @param houseIds 房屋ID列表，逗号分隔
     * @return 投票模板集合
     */
    public List<WuyeVoteTemplate> selectTemplatesByHouseIds(String houseIds);
}
