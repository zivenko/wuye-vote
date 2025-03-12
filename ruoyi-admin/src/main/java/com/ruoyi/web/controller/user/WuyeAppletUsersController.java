package com.ruoyi.web.controller.user;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.wuye.buildings.WuyeHouses;
import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.ImageUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.config.RuoYiConfig;



/**
 * 小程序用户Controller
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/users")
public class WuyeAppletUsersController extends BaseController
{
    final IWuyeAppletUsersService wuyeAppletUsersService;
    final IWuyeHousesService wuyeHousesService;

    final RedisCache redisCache;

    final TokenService tokenService;

    final AuthenticationManager authenticationManager;

    final WxMaService wxMaService;


    @PostMapping("/applet/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) throws WxErrorException {
        WxMaJscode2SessionResult sessionInfo =
                wxMaService.getUserService().getSessionInfo(loginBody.getCode());

        log.info("小程序用户认证结果：{}", sessionInfo);

        String openId = sessionInfo.getOpenid();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        UserConstants.APPLET_USER + openId, openId);
        AuthenticationContextHolder.setContext(authenticationToken);
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(openId, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        String token = tokenService.createToken(loginUser);

        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);

        return ajax;
    }

    @GetMapping("/applet/info")
    public AjaxResult info() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if(Objects.isNull(loginUser) || Objects.isNull(loginUser.getAppletUser())) {
            return AjaxResult.error("获取在线用户失败，请登录");
        }

        return AjaxResult.success(loginUser.getAppletUser());
    }

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

    /**
     * 上传头像
     */
    @PostMapping("/applet/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            WuyeAppletUsers loginUser = SecurityUtils.getLoginUser().getAppletUser();
            if (loginUser == null) {
                return AjaxResult.error("获取在线用户失败，请登录");
            }

            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (wuyeAppletUsersService.updateWuyeAppletUsersAvatar(loginUser.getAppletId(), avatar))
            {
                // 更新缓存用户头像
                loginUser.setAvatar(avatar);
                tokenService.setLoginUser(SecurityUtils.getLoginUser());
                
                // 返回成功结果
                return AjaxResult.success("上传成功", avatar);
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }

    /**
     * 获取用户头像
     */
    @GetMapping(value = "/avatar",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getAvatar(@RequestParam String imagePath) {

        return ImageUtils.getImage(imagePath);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/info")
    public AjaxResult updateUserInfo(@RequestBody WuyeAppletUsers user) {
        WuyeAppletUsers appletUser = SecurityUtils.getLoginUser().getAppletUser();
        if (appletUser == null) {
            return AjaxResult.error("获取在线用户失败，请登录");
        }

        // 设置用户ID
        user.setAppletId(appletUser.getAppletId());

        // 更新用户信息
        int rows = wuyeAppletUsersService.updateWuyeAppletUsers(user);
        if (rows > 0) {
            // 更新缓存中的用户信息
            appletUser.setName(user.getName());
            appletUser.setMobile(user.getMobile());
            appletUser.setIdNumber(user.getIdNumber());
            tokenService.setLoginUser(SecurityUtils.getLoginUser());

            // 尝试绑定房屋
            try {
                // 根据姓名和身份证号查找匹配的房屋
                List<WuyeHouses> matchedHouses = wuyeHousesService.selectHousesByOwnerInfo(user.getName(), user.getIdNumber());
                if (!matchedHouses.isEmpty()) {
                    // 提取房屋ID并拼接
                    String houseIds = matchedHouses.stream()
                            .map(house -> house.getHouseId().toString())
                            .collect(Collectors.joining(","));
                    
                    // 更新用户的房屋ID
                    appletUser.setHouseIds(houseIds);
                    wuyeAppletUsersService.updateWuyeAppletUsers(appletUser);
                    
                    // 更新房屋绑定状态
                    for (WuyeHouses house : matchedHouses) {
                        house.setIsBind(1L);
                        wuyeHousesService.updateWuyeHouses(house);
                    }
                }
            } catch (Exception e) {
                log.error("绑定房屋失败", e);
            }
            
            return AjaxResult.success();
        } else {
            return AjaxResult.error("更新失败");
        }
    }
}
