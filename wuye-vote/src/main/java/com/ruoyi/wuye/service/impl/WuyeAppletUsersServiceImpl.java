package com.ruoyi.wuye.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;
import com.ruoyi.wuye.mapper.user.WuyeAppletUsersMapper;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 小程序用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WuyeAppletUsersServiceImpl implements IWuyeAppletUsersService
{
    final WuyeAppletUsersMapper wuyeAppletUsersMapper;


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
     * 查询小程序用户
     *
     * @param openId 小程序用户openId
     * @return 小程序用户
     */
    @Override
    public WuyeAppletUsers selectWuyeAppletUsersByOpenId(String openId)
    {
        return wuyeAppletUsersMapper.selectWuyeAppletUsersByOpenId(openId);
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
     * 修改小程序用户信息
     * 
     * @param wuyeAppletUsers 小程序用户信息
     * @return 结果
     */
    @Override
    public int updateWuyeAppletUsers(WuyeAppletUsers wuyeAppletUsers)
    {
        // 参数校验
        if (wuyeAppletUsers.getAppletId() == null) {
            log.error("更新用户信息失败：用户ID不能为空");
            return 0;
        }
        
        // 更新用户信息
        int rows = wuyeAppletUsersMapper.updateWuyeAppletUsers(wuyeAppletUsers);
        if (rows > 0) {
            log.info("更新用户信息成功，用户ID：{}", wuyeAppletUsers.getAppletId());
        } else {
            log.error("更新用户信息失败，用户ID：{}", wuyeAppletUsers.getAppletId());
        }
        return rows;
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

    /**
     * 更新小程序用户头像
     * 
     * @param appletId 小程序用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateWuyeAppletUsersAvatar(Long appletId, String avatar)
    {
        return wuyeAppletUsersMapper.updateWuyeAppletUsersAvatar(appletId, avatar);
    }

    /**
     * 解绑房屋
     *
     * @param appletId 用户ID
     * @param houseId 房屋ID
     * @return 结果
     */
    @Override
    public boolean unbindHouse(Long appletId, Long houseId) {
        // 1. 获取用户当前的房屋ID列表
        WuyeAppletUsers user = wuyeAppletUsersMapper.selectWuyeAppletUsersByAppletId(appletId);
        if (user == null || user.getHouseIds() == null || user.getHouseIds().isEmpty()) {
            return false;
        }

        // 2. 从列表中移除要解绑的房屋ID
        String[] houseIdArray = user.getHouseIds().split(",");
        List<String> houseIdList = new ArrayList<>(Arrays.asList(houseIdArray));
        houseIdList.remove(houseId.toString());

        // 3. 更新用户的房屋ID列表
        String newHouseIds = String.join(",", houseIdList);
        int rows = wuyeAppletUsersMapper.updateHouseIds(appletId, newHouseIds);

        return rows > 0;
    }
}
