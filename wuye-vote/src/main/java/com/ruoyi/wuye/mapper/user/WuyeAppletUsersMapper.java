package com.ruoyi.wuye.mapper.user;


import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 小程序用户Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
public interface WuyeAppletUsersMapper 
{
    /**
     * 查询小程序用户
     *
     * @param openId 小程序openId
     * @return 小程序用户
     */
    public WuyeAppletUsers selectWuyeAppletUsersByOpenId(String openId);

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
     * 删除小程序用户
     * 
     * @param appletId 小程序用户主键
     * @return 结果
     */
    public int deleteWuyeAppletUsersByAppletId(Long appletId);

    /**
     * 批量删除小程序用户
     * 
     * @param appletIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWuyeAppletUsersByAppletIds(Long[] appletIds);

    /**
     * 更新小程序用户头像
     * 
     * @param appletId 小程序用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateWuyeAppletUsersAvatar(@Param("appletId") Long appletId, @Param("avatar") String avatar);

    /**
     * 更新用户房屋ID列表
     * 
     * @param appletId 用户ID
     * @param houseIds 新的房屋ID列表
     * @return 结果
     */
    public int updateHouseIds(@Param("appletId") Long appletId, @Param("houseIds") String houseIds);
}
