package com.ruoyi.wuye.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wuye.domain.vote.WuyeVoteRecord;
import com.ruoyi.wuye.mapper.vote.WuyeVoteRecordMapper;
import com.ruoyi.wuye.service.vote.IWuyeVoteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 投票记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@Service
public class WuyeVoteRecordServiceImpl implements IWuyeVoteRecordService
{
    @Autowired
    private WuyeVoteRecordMapper wuyeVoteRecordMapper;

    /**
     * 查询投票记录
     * 
     * @param voteId 投票记录主键
     * @return 投票记录
     */
    @Override
    public WuyeVoteRecord selectWuyeVoteRecordByVoteId(Long voteId)
    {
        return wuyeVoteRecordMapper.selectWuyeVoteRecordByVoteId(voteId);
    }

    /**
     * 查询投票记录列表
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 投票记录
     */
    @Override
    public List<WuyeVoteRecord> selectWuyeVoteRecordList(WuyeVoteRecord wuyeVoteRecord)
    {
        return wuyeVoteRecordMapper.selectWuyeVoteRecordList(wuyeVoteRecord);
    }

    /**
     * 新增投票记录
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 结果
     */
    @Override
    public int insertWuyeVoteRecord(WuyeVoteRecord wuyeVoteRecord)
    {
        wuyeVoteRecord.setCreateTime(DateUtils.getNowDate());
        return wuyeVoteRecordMapper.insertWuyeVoteRecord(wuyeVoteRecord);
    }

    /**
     * 修改投票记录
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 结果
     */
    @Override
    public int updateWuyeVoteRecord(WuyeVoteRecord wuyeVoteRecord)
    {
        wuyeVoteRecord.setUpdateTime(DateUtils.getNowDate());
        return wuyeVoteRecordMapper.updateWuyeVoteRecord(wuyeVoteRecord);
    }

    /**
     * 批量删除投票记录
     * 
     * @param voteIds 需要删除的投票记录主键
     * @return 结果
     */
    @Override
    public int deleteWuyeVoteRecordByVoteIds(Long[] voteIds)
    {
        return wuyeVoteRecordMapper.deleteWuyeVoteRecordByVoteIds(voteIds);
    }

    /**
     * 删除投票记录信息
     * 
     * @param voteId 投票记录主键
     * @return 结果
     */
    @Override
    public int deleteWuyeVoteRecordByVoteId(Long voteId)
    {
        return wuyeVoteRecordMapper.deleteWuyeVoteRecordByVoteId(voteId);
    }
}
