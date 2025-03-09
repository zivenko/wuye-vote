package com.ruoyi.web.controller.user;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.wuye.domain.user.WuyeAppletUsers;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
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
 * 小程序用户Controller
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@RestController
@RequestMapping("/system/users")
public class WuyeAppletUsersController extends BaseController
{
    @Autowired
    private IWuyeAppletUsersService wuyeAppletUsersService;

    /**
     * 查询小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:users:list')")
    @GetMapping("/list")
    public TableDataInfo list(WuyeAppletUsers wuyeAppletUsers)
    {
        startPage();
        List<WuyeAppletUsers> list = wuyeAppletUsersService.selectWuyeAppletUsersList(wuyeAppletUsers);
        return getDataTable(list);
    }

    /**
     * 导出小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:users:export')")
    @Log(title = "小程序用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeAppletUsers wuyeAppletUsers)
    {
        List<WuyeAppletUsers> list = wuyeAppletUsersService.selectWuyeAppletUsersList(wuyeAppletUsers);
        ExcelUtil<WuyeAppletUsers> util = new ExcelUtil<WuyeAppletUsers>(WuyeAppletUsers.class);
        util.exportExcel(response, list, "小程序用户数据");
    }

    /**
     * 获取小程序用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:users:query')")
    @GetMapping(value = "/{appletId}")
    public AjaxResult getInfo(@PathVariable("appletId") Long appletId)
    {
        return success(wuyeAppletUsersService.selectWuyeAppletUsersByAppletId(appletId));
    }

    /**
     * 新增小程序用户
     */
    @PreAuthorize("@ss.hasPermi('system:users:add')")
    @Log(title = "小程序用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeAppletUsers wuyeAppletUsers)
    {
        return toAjax(wuyeAppletUsersService.insertWuyeAppletUsers(wuyeAppletUsers));
    }

    /**
     * 修改小程序用户
     */
    @PreAuthorize("@ss.hasPermi('system:users:edit')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeAppletUsers wuyeAppletUsers)
    {
        return toAjax(wuyeAppletUsersService.updateWuyeAppletUsers(wuyeAppletUsers));
    }

    /**
     * 删除小程序用户
     */
    @PreAuthorize("@ss.hasPermi('system:users:remove')")
    @Log(title = "小程序用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{appletIds}")
    public AjaxResult remove(@PathVariable Long[] appletIds)
    {
        return toAjax(wuyeAppletUsersService.deleteWuyeAppletUsersByAppletIds(appletIds));
    }
}
