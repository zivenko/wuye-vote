package com.ruoyi.wuye.service.user;

import com.ruoyi.wuye.domain.user.WuyeAppletUsers;

import java.util.List;


/**
 * 小程序用户Service接口
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
public interface IWuyeAppletUsersService 
{
    /**
     * 查询小程序用户
     * 
     * @param appletId 小程序用户主键
     * @return 小程序用户
     */
    public WuyeAppletUsers selectWuyeAppletUsersByAppletId(Long appletId);

    /**
     * 查询小程序用户列表
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 小程序用户集合
     */
    public List<WuyeAppletUsers> selectWuyeAppletUsersList(WuyeAppletUsers wuyeAppletUsers);

    /**
     * 新增小程序用户
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 结果
     */
    public int insertWuyeAppletUsers(WuyeAppletUsers wuyeAppletUsers);

    /**
     * 修改小程序用户
     * 
     * @param wuyeAppletUsers 小程序用户
     * @return 结果
     */
    public int updateWuyeAppletUsers(WuyeAppletUsers wuyeAppletUsers);

    /**
     * 批量删除小程序用户
     * 
     * @param appletIds 需要删除的小程序用户主键集合
     * @return 结果
     */
    public int deleteWuyeAppletUsersByAppletIds(Long[] appletIds);

    /**
     * 删除小程序用户信息
     * 
     * @param appletId 小程序用户主键
     * @return 结果
     */
    public int deleteWuyeAppletUsersByAppletId(Long appletId);
}
