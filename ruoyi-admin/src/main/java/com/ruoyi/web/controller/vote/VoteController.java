package com.ruoyi.web.controller.vote;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.wuye.service.buildings.IWuyeHousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/house")
public class VoteController extends BaseController {

    @Autowired
    private IWuyeHousesService wuyeHousesService;

    /**
     * 获取房屋投票状态列表
     */
    @GetMapping("/vote-status")
    public TableDataInfo getHouseVoteStatus(@RequestParam("templateId") Long templateId) {
        startPage();
        return getDataTable(wuyeHousesService.getHouseVoteStatusList(templateId));
    }
} 