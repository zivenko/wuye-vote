package com.ruoyi.wuye.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wuye.domain.vote.WuyeVoteTemplate;
import com.ruoyi.wuye.mapper.vote.WuyeVoteTemplateMapper;
import com.ruoyi.wuye.service.vote.IWuyeVoteTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

/**
 * 投票模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@Service
public class WuyeVoteTemplateServiceImpl implements IWuyeVoteTemplateService
{
    @Autowired
    private WuyeVoteTemplateMapper wuyeVoteTemplateMapper;

    /**
     * 查询投票模板
     * 
     * @param templateId 投票模板主键
     * @return 投票模板
     */
    @Override
    public WuyeVoteTemplate selectWuyeVoteTemplateByTemplateId(Long templateId)
    {
        WuyeVoteTemplate template = wuyeVoteTemplateMapper.selectWuyeVoteTemplateByTemplateId(templateId);
        if (template != null) {
            template.setStatus(template.calculateStatus());
        }
        return template;
    }

    /**
     * 查询投票模板列表
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 投票模板
     */
    @Override
    public List<WuyeVoteTemplate> selectWuyeVoteTemplateList(WuyeVoteTemplate wuyeVoteTemplate)
    {
        // 获取所有数据
        List<WuyeVoteTemplate> list = wuyeVoteTemplateMapper.selectWuyeVoteTemplateList(wuyeVoteTemplate);
        
        // 更新每个模板的状态
        for (WuyeVoteTemplate template : list) {
            template.setStatus(template.calculateStatus());
        }
        
        return list;
    }

    /**
     * 新增投票模板
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 结果
     */
    @Override
    public int insertWuyeVoteTemplate(WuyeVoteTemplate wuyeVoteTemplate)
    {
        wuyeVoteTemplate.setCreateTime(DateUtils.getNowDate());
        return wuyeVoteTemplateMapper.insertWuyeVoteTemplate(wuyeVoteTemplate);
    }

    /**
     * 修改投票模板
     * 
     * @param wuyeVoteTemplate 投票模板
     * @return 结果
     */
    @Override
    public int updateWuyeVoteTemplate(WuyeVoteTemplate wuyeVoteTemplate)
    {
        wuyeVoteTemplate.setUpdateTime(DateUtils.getNowDate());
        return wuyeVoteTemplateMapper.updateWuyeVoteTemplate(wuyeVoteTemplate);
    }

    /**
     * 批量删除投票模板
     * 
     * @param templateIds 需要删除的投票模板主键
     * @return 结果
     */
    @Override
    public int deleteWuyeVoteTemplateByTemplateIds(Long[] templateIds)
    {
        return wuyeVoteTemplateMapper.deleteWuyeVoteTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除投票模板信息
     * 
     * @param templateId 投票模板主键
     * @return 结果
     */
    @Override
    public int deleteWuyeVoteTemplateByTemplateId(Long templateId)
    {
        return wuyeVoteTemplateMapper.deleteWuyeVoteTemplateByTemplateId(templateId);
    }

    @Override
    public List<WuyeVoteTemplate> selectUserVoteTemplateList(Long appletId, String keyword, Integer status)
    {
        // 获取用户可参与的投票模板列表
        List<WuyeVoteTemplate> list = wuyeVoteTemplateMapper.selectUserVoteTemplateList(appletId, keyword, status);
        
        // 更新每个模板的状态
        for (WuyeVoteTemplate template : list) {
            template.setStatus(template.calculateStatus());
        }
        
        return list;
    }
}
