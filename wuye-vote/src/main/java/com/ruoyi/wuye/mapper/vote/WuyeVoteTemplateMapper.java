package com.ruoyi.wuye.mapper.vote;

import com.ruoyi.wuye.domain.vote.WuyeVoteTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 投票模板Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public interface WuyeVoteTemplateMapper 
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
     * 删除投票模板
     * 
     * @param templateId 投票模板主键
     * @return 结果
     */
    public int deleteWuyeVoteTemplateByTemplateId(Long templateId);

    /**
     * 批量删除投票模板
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWuyeVoteTemplateByTemplateIds(Long[] templateIds);

    /**
     * 查询用户可参与的投票模板列表
     * 
     * @param appletId 小程序用户ID
     * @param keyword 搜索关键词
     * @param status 状态过滤
     * @return 投票模板集合
     */
    public List<WuyeVoteTemplate> selectUserVoteTemplateList(@Param("appletId") Long appletId, 
                                                            @Param("keyword") String keyword,
                                                            @Param("status") Integer status);
}
