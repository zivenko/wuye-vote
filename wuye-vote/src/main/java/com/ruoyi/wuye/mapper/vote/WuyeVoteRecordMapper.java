package com.ruoyi.wuye.mapper.vote;

import com.ruoyi.wuye.domain.vote.WuyeVoteRecord;

import java.util.List;

/**
 * 投票记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public interface WuyeVoteRecordMapper 
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
     * 删除投票记录
     * 
     * @param voteId 投票记录主键
     * @return 结果
     */
    public int deleteWuyeVoteRecordByVoteId(Long voteId);

    /**
     * 批量删除投票记录
     * 
     * @param voteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWuyeVoteRecordByVoteIds(Long[] voteIds);
}
