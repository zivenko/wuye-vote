package com.ruoyi.wuye.mapper.buildings;

import com.ruoyi.wuye.domain.buildings.WuyeBuilding;

import java.util.List;


/**
 * 物业小区层级Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-06
 */
public interface WuyeBuildingMapper 
{
    /**
     * 查询物业小区层级
     * 
     * @param id 物业小区层级主键
     * @return 物业小区层级
     */
    public WuyeBuilding selectWuyeBuildingById(Long id);

    /**
     * 查询物业小区层级列表
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 物业小区层级集合
     */
    public List<WuyeBuilding> selectWuyeBuildingList(WuyeBuilding wuyeBuilding);

    /**
     * 新增物业小区层级
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 结果
     */
    public int insertWuyeBuilding(WuyeBuilding wuyeBuilding);

    /**
     * 修改物业小区层级
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 结果
     */
    public int updateWuyeBuilding(WuyeBuilding wuyeBuilding);

    /**
     * 删除物业小区层级
     * 
     * @param id 物业小区层级主键
     * @return 结果
     */
    public int deleteWuyeBuildingById(Long id);

    /**
     * 批量删除物业小区层级
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWuyeBuildingByIds(Long[] ids);
}
