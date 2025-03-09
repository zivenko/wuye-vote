package com.ruoyi.wuye.service.vote;

import com.ruoyi.wuye.domain.vote.WuyeVoteRecord;

import java.util.List;

/**
 * 投票记录Service接口
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public interface IWuyeVoteRecordService 
{
    /**
     * 查询投票记录
     * 
     * @param voteId 投票记录主键
     * @return 投票记录
     */
    public WuyeVoteRecord selectWuyeVoteRecordByVoteId(Long voteId);

    /**
     * 查询投票记录列表
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 投票记录集合
     */
    public List<WuyeVoteRecord> selectWuyeVoteRecordList(WuyeVoteRecord wuyeVoteRecord);

    /**
     * 新增投票记录
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 结果
     */
    public int insertWuyeVoteRecord(WuyeVoteRecord wuyeVoteRecord);

    /**
     * 修改投票记录
     * 
     * @param wuyeVoteRecord 投票记录
     * @return 结果
     */
    public int updateWuyeVoteRecord(WuyeVoteRecord wuyeVoteRecord);

    /**
     * 批量删除投票记录
     * 
     * @param voteIds 需要删除的投票记录主键集合
     * @return 结果
     */
    public int deleteWuyeVoteRecordByVoteIds(Long[] voteIds);

    /**
     * 删除投票记录信息
     * 
     * @param voteId 投票记录主键
     * @return 结果
     */
    public int deleteWuyeVoteRecordByVoteId(Long voteId);
}
