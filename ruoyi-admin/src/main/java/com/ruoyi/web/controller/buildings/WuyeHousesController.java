package com.ruoyi.web.controller.buildings;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.wuye.domain.buildings.WuyeHouses;
import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
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

/**
 * 物业房屋Controller
 * 
 * @author ruoyi
 * @date 2025-03-07
 */
@RestController
@RequestMapping("/system/houses")
public class WuyeHousesController extends BaseController
{
    @Autowired
    private IWuyeHousesService wuyeHousesService;

    /**
     * 查询物业房屋列表
     */
    @PreAuthorize("@ss.hasPermi('system:houses:list')")
    @GetMapping("/list")
    public TableDataInfo list(WuyeHouses wuyeHouses)
    {
        startPage();
        List<WuyeHouses> list = wuyeHousesService.selectWuyeHousesList(wuyeHouses);
        return getDataTable(list);
    }

    /**
     * 导出物业房屋列表
     */
    @PreAuthorize("@ss.hasPermi('system:houses:export')")
    @Log(title = "物业房屋", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeHouses wuyeHouses)
    {
        List<WuyeHouses> list = wuyeHousesService.selectWuyeHousesList(wuyeHouses);
        ExcelUtil<WuyeHouses> util = new ExcelUtil<WuyeHouses>(WuyeHouses.class);
        util.exportExcel(response, list, "物业房屋数据");
    }

    /**
     * 获取物业房屋详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:houses:query')")
    @GetMapping(value = "/{houseId}")
    public AjaxResult getInfo(@PathVariable("houseId") Long houseId)
    {
        return success(wuyeHousesService.selectWuyeHousesByHouseId(houseId));
    }

    /**
     * 新增物业房屋
     */
    @PreAuthorize("@ss.hasPermi('system:houses:add')")
    @Log(title = "物业房屋", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeHouses wuyeHouses)
    {
        return toAjax(wuyeHousesService.insertWuyeHouses(wuyeHouses));
    }

    /**
     * 修改物业房屋
     */
    @PreAuthorize("@ss.hasPermi('system:houses:edit')")
    @Log(title = "物业房屋", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeHouses wuyeHouses)
    {
        return toAjax(wuyeHousesService.updateWuyeHouses(wuyeHouses));
    }

    /**
     * 删除物业房屋
     */
    @PreAuthorize("@ss.hasPermi('system:houses:remove')")
    @Log(title = "物业房屋", businessType = BusinessType.DELETE)
	@DeleteMapping("/{houseIds}")
    public AjaxResult remove(@PathVariable Long[] houseIds)
    {
        return toAjax(wuyeHousesService.deleteWuyeHousesByHouseIds(houseIds));
    }
}
