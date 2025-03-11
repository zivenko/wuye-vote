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
     * 修改房屋绑定审核（含文件上传）
     */
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
}
