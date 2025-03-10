package com.ruoyi.framework.web.service;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.framework.web.service.SysPermissionService;

import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户验证处理
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IWuyeAppletUsersService wuyeAppletUsersService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if(StringUtils.startsWith(username, UserConstants.APPLET_USER)) {
            String openid = StrUtil.subAfter(username, UserConstants.APPLET_USER, false);
            return loadAppletUser(openid);
        }

        return loadSysUser(username);
    }

    public UserDetails loadAppletUser(String openid) {


        WuyeAppletUsers user = wuyeAppletUsersService.selectWuyeAppletUsersByOpenId(openid);

        if (StringUtils.isNull(user))
        {
            wuyeAppletUsersService.insertWuyeAppletUsers(new WuyeAppletUsers()
                                                            .setOpenid(openid)
                                                            .setName("微信用户"));
        }


        return createLoginUser(user);
    }

    public UserDetails loadSysUser(String username) {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(WuyeAppletUsers user) {
        SysUser permissionUserTmp = new SysUser();

        SysRole applet = roleService.selectRoleByRoleKey("applet");

        permissionUserTmp.setRoles(Collections.singletonList(applet));
        return new LoginUser(user.getOpenid(), user,
                permissionService.getMenuPermission(permissionUserTmp));
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
