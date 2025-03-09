package com.ruoyi.wuye.service.impl;

import java.util.List;

import com.ruoyi.wuye.domain.user.WuyeAppletUsers;
import com.ruoyi.wuye.mapper.user.WuyeAppletUsersMapper;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小程序用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@Service
public class WuyeAppletUsersServiceImpl implements IWuyeAppletUsersService
{
    @Autowired
    private WuyeAppletUsersMapper wuyeAppletUsersMapper;

    /**
     * 查询小程序用户
     * 
     * @param appletId 小程序用户主键
     * @return 小程序用户
     */
    @Override
    public WuyeAppletUsers selectWuyeAppletUsersByAppletId(Long appletId)
    {
        return wuyeAppletUsersMapper.selectWuyeAppletUsersByAppletId(appletId);
    }

    /**
     * 查询小程序用户列表
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 小程序用户
     */
    @Override
    public List<WuyeAppletUsers> selectWuyeAppletUsersList(WuyeAppletUsers wuyeAppletUsers)
    {
        return wuyeAppletUsersMapper.selectWuyeAppletUsersList(wuyeAppletUsers);
    }

    /**
     * 新增小程序用户
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 结果
     */
    @Override
    public int insertWuyeAppletUsers(WuyeAppletUsers wuyeAppletUsers)
    {
        return wuyeAppletUsersMapper.insertWuyeAppletUsers(wuyeAppletUsers);
    }

    /**
     * 修改小程序用户
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 结果
     */
    @Override
    public int updateWuyeAppletUsers(WuyeAppletUsers wuyeAppletUsers)
    {
        return wuyeAppletUsersMapper.updateWuyeAppletUsers(wuyeAppletUsers);
    }

    /**
     * 批量删除小程序用户
     * 
     * @param appletIds 需要删除的小程序用户主键
     * @return 结果
     */
    @Override
    public int deleteWuyeAppletUsersByAppletIds(Long[] appletIds)
    {
        return wuyeAppletUsersMapper.deleteWuyeAppletUsersByAppletIds(appletIds);
    }

    /**
     * 删除小程序用户信息
     * 
     * @param appletId 小程序用户主键
     * @return 结果
     */
    @Override
    public int deleteWuyeAppletUsersByAppletId(Long appletId)
    {
        return wuyeAppletUsersMapper.deleteWuyeAppletUsersByAppletId(appletId);
    }
}
