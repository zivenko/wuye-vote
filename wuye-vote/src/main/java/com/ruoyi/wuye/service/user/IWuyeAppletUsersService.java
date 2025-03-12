package com.ruoyi.wuye.service.user;


import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import me.chanjar.weixin.common.error.WxErrorException;

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
     * 查询小程序用户
     *
     * @param openId 小程序用户openId
     * @return 小程序用户
     */
    WuyeAppletUsers selectWuyeAppletUsersByOpenId(String openId);

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
     * 修改小程序用户信息
     * 
     * @param wuyeAppletUsers 小程序用户信息，包含以下字段：
     *                        - appletId: 用户ID
     *                        - name: 姓名
     *                        - mobile: 手机号
     *                        - idNumber: 身份证号
     *                        - avatar: 头像
     *                        - nikeName: 昵称
     *                        - openid: 微信openid
     *                        - houseIds: 房屋ID列表
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

    /**
     * 解绑房屋
     * 
     * @param appletId 用户ID
     * @param houseId 房屋ID
     * @return 结果
     */
    public boolean unbindHouse(Long appletId, Long houseId);

    /**
     * 更新小程序用户头像
     * 
     * @param appletId 小程序用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateWuyeAppletUsersAvatar(Long appletId, String avatar);

    /**
     * 根据姓名和手机号查找用户
     * 
     * @param name 用户姓名
     * @param mobile 手机号
     * @return 小程序用户
     */
    public WuyeAppletUsers selectUserByNameAndMobile(String name, String mobile);
}
