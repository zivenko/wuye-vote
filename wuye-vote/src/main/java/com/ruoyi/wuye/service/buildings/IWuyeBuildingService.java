package com.ruoyi.wuye.service.buildings;

import com.ruoyi.wuye.domain.buildings.WuyeBuilding;
import com.ruoyi.wuye.domain.dto.BuildingExportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 物业小区层级Service接口
 * 
 * @author ruoyi
 * @date 2025-03-06
 */
public interface IWuyeBuildingService 
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
     * 批量删除物业小区层级
     * 
     * @param ids 需要删除的物业小区层级主键集合
     * @return 结果
     */
    public int deleteWuyeBuildingByIds(Long[] ids);

    /**
     * 删除物业小区层级信息
     * 
     * @param id 物业小区层级主键
     * @return 结果
     */
    public int deleteWuyeBuildingById(Long id);

    void importBuilding(MultipartFile file);

    /**
     * 导出物业小区数据
     * 
     * @param wuyeBuilding 查询条件
     * @return 导出数据列表
     */
    List<BuildingExportDTO> selectBuildingExportList(WuyeBuilding wuyeBuilding);
}
