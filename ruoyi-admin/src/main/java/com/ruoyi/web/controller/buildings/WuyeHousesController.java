package com.ruoyi.web.controller.buildings;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;
import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.wuye.domain.buildings.WuyeHouseBindCheck;
import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
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
import com.ruoyi.wuye.service.buildings.IWuyeHouseBindCheckService;

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

    @Autowired
    private IWuyeAppletUsersService wuyeAppletUsersService;

    @Autowired
    private IWuyeHouseBindCheckService wuyeHouseBindCheckService;

    /**
     * 查询物业房屋列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WuyeHouses wuyeHouses)
    {
        startPage();
        List<WuyeHouses> list = wuyeHousesService.selectWuyeHousesList(wuyeHouses);
        return getDataTable(list);
    }

    /**
     * 通过appletUser中的appletId查询房屋
     */
    @GetMapping("/")
    public AjaxResult list() {
        WuyeAppletUsers appletUser = SecurityUtils.getLoginUser().getAppletUser();
        if (appletUser == null) {
            return AjaxResult.error("获取用户信息失败");
        }

        // 如果用户没有绑定房屋，返回空列表
        appletUser = wuyeAppletUsersService.selectWuyeAppletUsersByAppletId(appletUser.getAppletId());

        SecurityUtils.getLoginUser().setAppletUser(appletUser);

        List<WuyeHouses> houses = new ArrayList<>();

        // 1. 获取已绑定的房屋
        if (appletUser.getHouseIds() != null && !appletUser.getHouseIds().isEmpty()) {
            String[] houseIdArray = appletUser.getHouseIds().split(",");
            Long[] houseIds = Arrays.stream(houseIdArray)
                    .map(Long::parseLong)
                    .toArray(Long[]::new);

            for (Long houseId : houseIds) {
                WuyeHouses house = wuyeHousesService.selectWuyeHousesByHouseId(houseId);
                if (house != null) {
                    houses.add(house);
                }
            }
        }

        // 2. 获取审核中和审核失败的房屋
        WuyeHouseBindCheck queryCheck = new WuyeHouseBindCheck();
        queryCheck.setAppletId(appletUser.getAppletId());
        List<WuyeHouseBindCheck> checks = wuyeHouseBindCheckService.selectWuyeHouseBindCheckList(queryCheck);
        
        for (WuyeHouseBindCheck check : checks) {
            // 只处理审核中(uncheck)和审核失败(fail)的记录
            if ("uncheck".equals(check.getCheckStatus()) || "fail".equals(check.getCheckStatus())) {
                WuyeHouses house = wuyeHousesService.selectWuyeHousesByHouseId(check.getHouseId());
                if (house != null) {
                    // 添加审核状态和失败原因到房屋对象
                    house.setCertificate(check.getCertificate());
                    house.setCheckStatus(check.getCheckStatus());
                    house.setCheckErrorMsg(check.getCheckErrorMsg());
                    houses.add(house);
                }
            }
        }

        return AjaxResult.success(houses);
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

    /**
     * 解绑房屋
     */
    @PostMapping("/unbind/{houseId}")
    public AjaxResult unbindHouse(@PathVariable Long houseId) {
        WuyeAppletUsers appletUser = SecurityUtils.getLoginUser().getAppletUser();

        // 1. 更新房屋绑定状态
        WuyeHouses house = new WuyeHouses();
        house.setHouseId(houseId);
        house.setIsBind(0L);
        wuyeHousesService.updateWuyeHouses(house);

        // 2. 更新用户房屋列表
        boolean result = wuyeAppletUsersService.unbindHouse(appletUser.getAppletId(), houseId);
        if (result) {
            return AjaxResult.success("解绑成功");
        } else {
            return AjaxResult.error("解绑失败");
        }
    }
}
