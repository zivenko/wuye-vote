package com.ruoyi.web.controller.buildings;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.wuye.domain.buildings.WuyeBuilding;
import com.ruoyi.wuye.domain.dto.BuildingExportDTO;
import com.ruoyi.wuye.service.buildings.IWuyeBuildingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 物业小区层级Controller
 * 
 * @author ruoyi
 * @date 2025-03-06
 */
@RestController
@RequestMapping("/system/building")
public class WuyeBuildingController extends BaseController
{
    @Autowired
    private IWuyeBuildingService wuyeBuildingService;

    /**
     * 查询物业小区层级列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WuyeBuilding wuyeBuilding)
    {
//        startPage();
        List<WuyeBuilding> list = wuyeBuildingService.selectWuyeBuildingList(wuyeBuilding);
        return getDataTable(list);
    }

    /**
     * 导出物业小区层级列表
     */
    @PreAuthorize("@ss.hasPermi('system:building:export')")
    @Log(title = "物业小区层级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeBuilding wuyeBuilding)
    {
        List<BuildingExportDTO> list = wuyeBuildingService.selectBuildingExportList(wuyeBuilding);
        ExcelUtil<BuildingExportDTO> util = new ExcelUtil<BuildingExportDTO>(BuildingExportDTO.class);
        util.exportExcel(response, list, "物业小区数据");
    }

    /**
     * 获取物业小区层级详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:building:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wuyeBuildingService.selectWuyeBuildingById(id));
    }

    /**
     * 新增物业小区层级
     */
    @PreAuthorize("@ss.hasPermi('system:building:add')")
    @Log(title = "物业小区层级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeBuilding wuyeBuilding)
    {
        return toAjax(wuyeBuildingService.insertWuyeBuilding(wuyeBuilding));
    }

    /**
     * 修改物业小区层级
     */
    @PreAuthorize("@ss.hasPermi('system:building:edit')")
    @Log(title = "物业小区层级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeBuilding wuyeBuilding)
    {
        return toAjax(wuyeBuildingService.updateWuyeBuilding(wuyeBuilding));
    }

    /**
     * 删除物业小区层级
     */
    @PreAuthorize("@ss.hasPermi('system:building:remove')")
    @Log(title = "物业小区层级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wuyeBuildingService.deleteWuyeBuildingByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:building:import')")
    @PostMapping("/import")
    public AjaxResult importBuilding(MultipartFile file) {
        try {
            wuyeBuildingService.importBuilding(file);
            return AjaxResult.success("导入成功");
        } catch (Exception e) {
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }
}
