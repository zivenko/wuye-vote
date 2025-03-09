package com.ruoyi.wuye.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wuye.domain.buildings.WuyeBuilding;
import com.ruoyi.wuye.domain.dto.BuildingImportDTO;
import com.ruoyi.wuye.domain.dto.BuildingExportDTO;
import com.ruoyi.wuye.listeners.BuildingImportListener;
import com.ruoyi.wuye.mapper.buildings.WuyeBuildingMapper;
import com.ruoyi.wuye.service.buildings.IWuyeBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 物业小区层级Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-06
 */
@Service
public class WuyeBuildingServiceImpl implements IWuyeBuildingService
{
    @Autowired
    private WuyeBuildingMapper wuyeBuildingMapper;

    /**
     * 查询物业小区层级
     * 
     * @param id 物业小区层级主键
     * @return 物业小区层级
     */
    @Override
    public WuyeBuilding selectWuyeBuildingById(Long id)
    {
        return wuyeBuildingMapper.selectWuyeBuildingById(id);
    }

    /**
     * 查询物业小区层级列表
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 物业小区层级
     */
    @Override
    public List<WuyeBuilding> selectWuyeBuildingList(WuyeBuilding wuyeBuilding)
    {
        return wuyeBuildingMapper.selectWuyeBuildingList(wuyeBuilding);
    }

    /**
     * 新增物业小区层级
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 结果
     */
    @Override
    public int insertWuyeBuilding(WuyeBuilding wuyeBuilding)
    {
        wuyeBuilding.setCreateTime(DateUtils.getNowDate());
        return wuyeBuildingMapper.insertWuyeBuilding(wuyeBuilding);
    }

    /**
     * 修改物业小区层级
     * 
     * @param wuyeBuilding 物业小区层级
     * @return 结果
     */
    @Override
    public int updateWuyeBuilding(WuyeBuilding wuyeBuilding)
    {
        wuyeBuilding.setUpdateTime(DateUtils.getNowDate());
        return wuyeBuildingMapper.updateWuyeBuilding(wuyeBuilding);
    }

    /**
     * 批量删除物业小区层级
     * 
     * @param ids 需要删除的物业小区层级主键
     * @return 结果
     */
    @Override
    public int deleteWuyeBuildingByIds(Long[] ids)
    {
        return wuyeBuildingMapper.deleteWuyeBuildingByIds(ids);
    }

    /**
     * 删除物业小区层级信息
     * 
     * @param id 物业小区层级主键
     * @return 结果
     */
    @Override
    public int deleteWuyeBuildingById(Long id)
    {
        return wuyeBuildingMapper.deleteWuyeBuildingById(id);
    }

    @Override
    public void importBuilding(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),
                            BuildingImportDTO.class,
                            new BuildingImportListener(this))
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new RuntimeException("导入Excel失败", e);
        }
    }

    @Override
    public List<BuildingExportDTO> selectBuildingExportList(WuyeBuilding wuyeBuilding) {
        List<BuildingExportDTO> exportList = new ArrayList<>();
        
        // 1. 获取所有数据
        List<WuyeBuilding> allBuildings = wuyeBuildingMapper.selectWuyeBuildingList(new WuyeBuilding());
        
        // 2. 构建ID到对象的映射，方便查找
        Map<Long, WuyeBuilding> buildingMap = allBuildings.stream()
                .collect(Collectors.toMap(WuyeBuilding::getId, b -> b));
        
        // 3. 处理每个单元
        allBuildings.stream()
                .filter(b -> b.getLevel() == 3) // 只处理单元级别
                .forEach(unit -> {
                    BuildingExportDTO dto = new BuildingExportDTO();
                    dto.setUnitName(unit.getName());
                    
                    // 查找楼栋
                    WuyeBuilding building = buildingMap.get(unit.getPid());
                    if (building != null) {
                        dto.setBuildingName(building.getName());
                        
                        // 查找小区
                        WuyeBuilding community = buildingMap.get(building.getPid());
                        if (community != null) {
                            dto.setCommunityName(community.getName());
                            dto.setAddress(community.getAddress());
                        }
                    }
                    
                    exportList.add(dto);
                });
        
        return exportList;
    }
}
