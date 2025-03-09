package com.ruoyi.wuye.service.buildings;

import com.ruoyi.wuye.domain.buildings.WuyeHouseBindCheck;

import java.util.List;
/**
 * 房屋绑定审核Service接口
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
public interface IWuyeHouseBindCheckService 
{
    /**
     * 查询房屋绑定审核
     * 
     * @param checkId 房屋绑定审核主键
     * @return 房屋绑定审核
     */
    public WuyeHouseBindCheck selectWuyeHouseBindCheckByCheckId(Long checkId);

    /**
     * 查询房屋绑定审核列表
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 房屋绑定审核集合
     */
    public List<WuyeHouseBindCheck> selectWuyeHouseBindCheckList(WuyeHouseBindCheck wuyeHouseBindCheck);

    /**
     * 新增房屋绑定审核
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 结果
     */
    public int insertWuyeHouseBindCheck(WuyeHouseBindCheck wuyeHouseBindCheck);

    /**
     * 修改房屋绑定审核
     * 
     * @param wuyeHouseBindCheck 房屋绑定审核
     * @return 结果
     */
    public int updateWuyeHouseBindCheck(WuyeHouseBindCheck wuyeHouseBindCheck);

    /**
     * 批量删除房屋绑定审核
     * 
     * @param checkIds 需要删除的房屋绑定审核主键集合
     * @return 结果
     */
    public int deleteWuyeHouseBindCheckByCheckIds(Long[] checkIds);

    /**
     * 删除房屋绑定审核信息
     * 
     * @param checkId 房屋绑定审核主键
     * @return 结果
     */
    public int deleteWuyeHouseBindCheckByCheckId(Long checkId);
}
