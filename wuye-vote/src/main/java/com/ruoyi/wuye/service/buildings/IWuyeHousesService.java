package com.ruoyi.wuye.service.buildings;

import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;

import java.util.List;


/**
 * 物业房屋Service接口
 * 
 * @author ruoyi
 * @date 2025-03-07
 */
public interface IWuyeHousesService 
{
    /**
     * 查询物业房屋
     * 
     * @param houseId 物业房屋主键
     * @return 物业房屋
     */
    public WuyeHouses selectWuyeHousesByHouseId(Long houseId);


    /**
     * 查询物业房屋列表
     * 
     * @param wuyeHouses 物业房屋
     * @return 物业房屋集合
     */
    public List<WuyeHouses> selectWuyeHousesList(WuyeHouses wuyeHouses);

    /**
     * 新增物业房屋
     * 
     * @param wuyeHouses 物业房屋
     * @return 结果
     */
    public int insertWuyeHouses(WuyeHouses wuyeHouses);

    /**
     * 修改物业房屋
     * 
     * @param wuyeHouses 物业房屋
     * @return 结果
     */
    public int updateWuyeHouses(WuyeHouses wuyeHouses);

    /**
     * 批量删除物业房屋
     * 
     * @param houseIds 需要删除的物业房屋主键集合
     * @return 结果
     */
    public int deleteWuyeHousesByHouseIds(Long[] houseIds);

    /**
     * 删除物业房屋信息
     * 
     * @param houseId 物业房屋主键
     * @return 结果
     */
    public int deleteWuyeHousesByHouseId(Long houseId);

    List<WuyeHouses> selectHousesByOwnerInfo(String ownerName, String ownerIdNumber);
}
