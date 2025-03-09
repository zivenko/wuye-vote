package com.ruoyi.wuye.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wuye.domain.buildings.WuyeHouseBindCheck;
import com.ruoyi.wuye.mapper.buildings.WuyeHouseBindCheckMapper;
import com.ruoyi.wuye.service.buildings.IWuyeHouseBindCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 房屋绑定审核Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@Service
public class WuyeHouseBindCheckServiceImpl implements IWuyeHouseBindCheckService
{
    @Autowired
    private WuyeHouseBindCheckMapper wuyeHouseBindCheckMapper;

    /**
     * 查询房屋绑定审核
     * 
     * @param checkId 房屋绑定审核主键
     * @return 房屋绑定审核
     */
    @Override
    public WuyeHouseBindCheck selectWuyeHouseBindCheckByCheckId(Long checkId)
    {
        return wuyeHouseBindCheckMapper.selectWuyeHouseBindCheckByCheckId(checkId);
    }

    /**
     * 查询房屋绑定审核列表
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 房屋绑定审核
     */
    @Override
    public List<WuyeHouseBindCheck> selectWuyeHouseBindCheckList(WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        return wuyeHouseBindCheckMapper.selectWuyeHouseBindCheckList(wuyeHouseBindCheck);
    }

    /**
     * 新增房屋绑定审核
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 结果
     */
    @Override
    public int insertWuyeHouseBindCheck(WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        wuyeHouseBindCheck.setCreateTime(DateUtils.getNowDate());
        return wuyeHouseBindCheckMapper.insertWuyeHouseBindCheck(wuyeHouseBindCheck);
    }

    /**
     * 修改房屋绑定审核
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 结果
     */
    @Override
    public int updateWuyeHouseBindCheck(WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        return wuyeHouseBindCheckMapper.updateWuyeHouseBindCheck(wuyeHouseBindCheck);
    }

    /**
     * 批量删除房屋绑定审核
     * 
     * @param checkIds 需要删除的房屋绑定审核主键
     * @return 结果
     */
    @Override
    public int deleteWuyeHouseBindCheckByCheckIds(Long[] checkIds)
    {
        return wuyeHouseBindCheckMapper.deleteWuyeHouseBindCheckByCheckIds(checkIds);
    }

    /**
     * 删除房屋绑定审核信息
     * 
     * @param checkId 房屋绑定审核主键
     * @return 结果
     */
    @Override
    public int deleteWuyeHouseBindCheckByCheckId(Long checkId)
    {
        return wuyeHouseBindCheckMapper.deleteWuyeHouseBindCheckByCheckId(checkId);
    }
}
