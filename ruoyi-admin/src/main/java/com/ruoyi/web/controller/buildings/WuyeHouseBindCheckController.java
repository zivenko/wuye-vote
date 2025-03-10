package com.ruoyi.web.controller.buildings;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.wuye.domain.buildings.WuyeHouseBindCheck;
import com.ruoyi.wuye.service.buildings.IWuyeHouseBindCheckService;
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
import com.ruoyi.common.
core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 房屋绑定审核Controller
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@RestController
@RequestMapping("/system/check")
public class WuyeHouseBindCheckController extends BaseController
{
    @Autowired
    private IWuyeHouseBindCheckService wuyeHouseBindCheckService;

    /**
     * 查询房屋绑定审核列表
     */
    @PreAuthorize("@ss.hasPermi('system:check:list')")
    @GetMapping("/list")
    public TableDataInfo list(WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        startPage();
        List<WuyeHouseBindCheck> list = wuyeHouseBindCheckService.selectWuyeHouseBindCheckList(wuyeHouseBindCheck);
        return getDataTable(list);
    }

    /**
     * 导出房屋绑定审核列表
     */
    @PreAuthorize("@ss.hasPermi('system:check:export')")
    @Log(title = "房屋绑定审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        List<WuyeHouseBindCheck> list = wuyeHouseBindCheckService.selectWuyeHouseBindCheckList(wuyeHouseBindCheck);
        ExcelUtil<WuyeHouseBindCheck> util = new ExcelUtil<WuyeHouseBindCheck>(WuyeHouseBindCheck.class);
        util.exportExcel(response, list, "房屋绑定审核数据");
    }

    /**
     * 获取房屋绑定审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:check:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") Long checkId)
    {
        return success(wuyeHouseBindCheckService.selectWuyeHouseBindCheckByCheckId(checkId));
    }

    /**
     * 新增房屋绑定审核
     */
    @Log(title = "房屋绑定审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        WuyeAppletUsers appletUser = SecurityUtils.getLoginUser().getAppletUser();
        wuyeHouseBindCheck.setAppletId(appletUser.getAppletId());
        return toAjax(wuyeHouseBindCheckService.insertWuyeHouseBindCheck(wuyeHouseBindCheck));
    }

    /**
     * 修改房屋绑定审核
     */
    @PreAuthorize("@ss.hasPermi('system:check:edit')")
    @Log(title = "房屋绑定审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        return toAjax(wuyeHouseBindCheckService.updateWuyeHouseBindCheck(wuyeHouseBindCheck));
    }

    /**
     * 删除房屋绑定审核
     */
    @PreAuthorize("@ss.hasPermi('system:check:remove')")
    @Log(title = "房屋绑定审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds)
    {
        return toAjax(wuyeHouseBindCheckService.deleteWuyeHouseBindCheckByCheckIds(checkIds));
    }
}
