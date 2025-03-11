package com.ruoyi.wuye.mapper.buildings;

import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 物业房屋Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-07
 */
public interface WuyeHousesMapper 
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
     * 根据业主信息查询房屋
     *
     * @param ownerName 业主姓名
     * @param ownerIdNumber 业主身份证号
     * @return 房屋列表
     */
    public List<WuyeHouses> selectHousesByOwnerInfo(@Param("ownerName") String ownerName, @Param("ownerIdNumber") String ownerIdNumber);

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
     * 删除物业房屋
     * 
     * @param houseId 物业房屋主键
     * @return 结果
     */
    public int deleteWuyeHousesByHouseId(Long houseId);

    /**
     * 批量删除物业房屋
     * 
     * @param houseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWuyeHousesByHouseIds(Long[] houseIds);


    public WuyeHouses selectHouseByLocation(String districtName, String buildingName, String unitName, String roomNumber);
}
