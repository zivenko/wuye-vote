package com.ruoyi.wuye.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;
import com.ruoyi.common.core.domain.vo.HouseVoteStatusVO;
import com.ruoyi.wuye.mapper.buildings.WuyeHousesMapper;
import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 物业房屋Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-07
 */
@Service
public class WuyeHousesServiceImpl implements IWuyeHousesService
{
    @Autowired
    private WuyeHousesMapper wuyeHousesMapper;

    /**
     * 查询物业房屋
     * 
     * @param houseId 物业房屋主键
     * @return 物业房屋
     */
    @Override
    public WuyeHouses selectWuyeHousesByHouseId(Long houseId)
    {
        return wuyeHousesMapper.selectWuyeHousesByHouseId(houseId);
    }

    /**
     * 查询物业房屋列表
     * 
     * @param wuyeHouses 物业房屋
     * @return 物业房屋
     */
    @Override
    public List<WuyeHouses> selectWuyeHousesList(WuyeHouses wuyeHouses)
    {
        return wuyeHousesMapper.selectWuyeHousesList(wuyeHouses);
    }

    /**
     * 新增物业房屋
     * 
     * @param wuyeHouses 物业房屋
     * @return 结果
     */
    @Override
    public int insertWuyeHouses(WuyeHouses wuyeHouses)
    {
        return wuyeHousesMapper.insertWuyeHouses(wuyeHouses);
    }

    /**
     * 修改物业房屋
     * 
     * @param wuyeHouses 物业房屋
     * @return 结果
     */
    @Override
    public int updateWuyeHouses(WuyeHouses wuyeHouses)
    {
        return wuyeHousesMapper.updateWuyeHouses(wuyeHouses);
    }

    /**
     * 批量删除物业房屋
     * 
     * @param houseIds 需要删除的物业房屋主键
     * @return 结果
     */
    @Override
    public int deleteWuyeHousesByHouseIds(Long[] houseIds)
    {
        return wuyeHousesMapper.deleteWuyeHousesByHouseIds(houseIds);
    }

    /**
     * 删除物业房屋信息
     * 
     * @param houseId 物业房屋主键
     * @return 结果
     */
    @Override
    public int deleteWuyeHousesByHouseId(Long houseId)
    {
        return wuyeHousesMapper.deleteWuyeHousesByHouseId(houseId);
    }

    /**
     * 根据业主信息查询房屋
     * 
     * @param ownerName 业主姓名
     * @param ownerIdNumber 业主身份证号
     * @return 房屋列表
     */
    @Override
    public List<WuyeHouses> selectHousesByOwnerInfo(String ownerName, String ownerIdNumber) {
        return wuyeHousesMapper.selectHousesByOwnerInfo(ownerName, ownerIdNumber);
    }

    @Override
    public WuyeHouses getHouseByLocation(String districtName, String buildingName, String unitName, String roomNumber) {
        return wuyeHousesMapper.selectHouseByLocation(districtName, buildingName, unitName, roomNumber);
    }

    /**
     * 查询房屋投票状态列表
     *
     * @param templateId 投票模板ID
     * @return 房屋投票状态列表
     */
    @Override
    public List<HouseVoteStatusVO> getHouseVoteStatusList(Long templateId) {
        return wuyeHousesMapper.selectHouseVoteStatusList(templateId);
    }
}
