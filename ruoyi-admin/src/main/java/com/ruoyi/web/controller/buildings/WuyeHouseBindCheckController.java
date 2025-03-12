package com.ruoyi.web.controller.buildings;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;
import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.ImageUtils;
import com.ruoyi.wuye.domain.buildings.WuyeHouseBindCheck;
import com.ruoyi.wuye.service.buildings.IWuyeHouseBindCheckService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
import com.ruoyi.common.utils.DateUtils;

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

    @Autowired
    private IWuyeHousesService wuyeHousesService;

    @Autowired
    private IWuyeAppletUsersService wuyeAppletUsersService;

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
     * 新增房屋绑定审核（含文件上传）
     */
    @Log(title = "房屋绑定审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file") MultipartFile file, 
                         @RequestParam("districtName") String districtName,
                         @RequestParam("buildingName") String buildingName,
                         @RequestParam("unitName") String unitName,
                         @RequestParam("roomNumber") String roomNumber) throws Exception
    {
        // 上传文件
        String filePath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);

        // 根据小区、楼栋、单元、房号查找房屋ID
        WuyeHouses house = wuyeHousesService.getHouseByLocation(districtName, buildingName, unitName, roomNumber);
        if (house == null) {
            return error("未找到对应的房屋信息");
        }

        // 创建审核记录
        WuyeHouseBindCheck wuyeHouseBindCheck = new WuyeHouseBindCheck();
        wuyeHouseBindCheck.setHouseId(house.getHouseId());
        wuyeHouseBindCheck.setCertificate(filePath);
        wuyeHouseBindCheck.setCheckStatus("uncheck"); // 设置初始状态为未审核

        // 设置小程序用户ID
        WuyeAppletUsers appletUser = SecurityUtils.getLoginUser().getAppletUser();
        wuyeHouseBindCheck.setAppletId(appletUser.getAppletId());

        return toAjax(wuyeHouseBindCheckService.insertWuyeHouseBindCheck(wuyeHouseBindCheck));
    }

    /**
     * 修改房屋绑定审核
     */
    @PreAuthorize("@ss.hasPermi('system:check:edit')")
    @Log(title = "房屋绑定审核", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam("checkId") Long checkId,
                            @RequestParam("districtName") String districtName,
                            @RequestParam("buildingName") String buildingName,
                            @RequestParam("unitName") String unitName,
                            @RequestParam("roomNumber") String roomNumber) throws Exception
    {
        // 获取现有的审核记录
        WuyeHouseBindCheck existingCheck = wuyeHouseBindCheckService.selectWuyeHouseBindCheckByCheckId(checkId);
        if (existingCheck == null)
        {
            return error("未找到对应的审核记录");
        }

        // 根据小区、楼栋、单元、房号查找房屋ID
        WuyeHouses house = wuyeHousesService.getHouseByLocation(districtName, buildingName, unitName, roomNumber);
        if (house == null) {
            return error("未找到对应的房屋信息");
        }

        // 更新房屋ID
        existingCheck.setHouseId(house.getHouseId());

        // 如果上传了新文件，处理文件上传
        if (file != null && !file.isEmpty())
        {
            String filePath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
            existingCheck.setCertificate(filePath);
        }

        // 重置审核状态为未审核
        existingCheck.setCheckStatus("uncheck");
        existingCheck.setCheckErrorMsg(null);
        existingCheck.setCheckTime(null);

        return toAjax(wuyeHouseBindCheckService.updateWuyeHouseBindCheck(existingCheck));
    }

    /**
     * 审核房屋绑定申请
     */
    @PreAuthorize("@ss.hasPermi('system:check:edit')")
    @Log(title = "房屋绑定审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult audit(@RequestBody WuyeHouseBindCheck wuyeHouseBindCheck)
    {
        // 如果是审核通过
        if ("success".equals(wuyeHouseBindCheck.getCheckStatus())) {
            // 获取完整的审核记录信息
            WuyeHouseBindCheck existingCheck = wuyeHouseBindCheckService.selectWuyeHouseBindCheckByCheckId(wuyeHouseBindCheck.getCheckId());
            if (existingCheck == null) {
                return error("未找到对应的审核记录");
            }

            // 1. 先检查房屋是否已被绑定
            WuyeHouses house = wuyeHousesService.selectWuyeHousesByHouseId(existingCheck.getHouseId());
            if (house == null) {
                return error("未找到对应的房屋信息");
            }
            
            // 检查房屋绑定状态
            if (house.getIsBind() != null && house.getIsBind() == 1L) {
                return error("此房屋已被绑定，审核无效！");
            }

            // 2. 更新房屋的绑定状态
            house.setIsBind(1L);
            wuyeHousesService.updateWuyeHouses(house);

            // 3. 更新用户的房屋绑定信息
            WuyeAppletUsers appletUser = wuyeAppletUsersService.selectWuyeAppletUsersByAppletId(existingCheck.getAppletId());
            if (appletUser != null) {
                String currentHouseIds = appletUser.getHouseIds();
                String newHouseId = String.valueOf(existingCheck.getHouseId());
                
                // 如果当前没有绑定房屋
                if (currentHouseIds == null || currentHouseIds.isEmpty()) {
                    appletUser.setHouseIds(newHouseId);
                } 
                // 如果已有绑定房屋，则追加
                else {
                    // 检查是否已经包含该房屋ID
                    String[] houseIds = currentHouseIds.split(",");
                    boolean contains = false;
                    for (String id : houseIds) {
                        if (id.equals(newHouseId)) {
                            contains = true;
                            break;
                        }
                    }
                    // 如果不包含，则追加
                    if (!contains) {
                        appletUser.setHouseIds(currentHouseIds + "," + newHouseId);
                    }
                }
                wuyeAppletUsersService.updateWuyeAppletUsers(appletUser);
            }
        }

        // 设置审核时间和审核人
        wuyeHouseBindCheck.setCheckTime(DateUtils.getNowDate());
        wuyeHouseBindCheck.setUserId(SecurityUtils.getUserId());

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

    /**
     * 获取房产证明图片
     */
    @GetMapping(value = "/certificate",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getAvatar(@RequestParam String imagePath) {

        return ImageUtils.getImage(imagePath);
    }
}
