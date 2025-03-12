package com.ruoyi.web.controller.vote;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.WuyeVoteRecordService;
import com.ruoyi.system.service.WuyeVoteTemplateService;
import com.ruoyi.system.domain.WuyeVoteRecord;
import com.ruoyi.system.domain.WuyeVoteTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投票记录Controller
 *
 * @author ruoyi
 * @date 2023-07-24
 */
@RestController
@RequestMapping("/system/record")
public class WuyeVoteRecordController extends BaseController
{
    @Autowired
    private WuyeVoteRecordService wuyeVoteRecordService;

    @Autowired
    private WuyeVoteTemplateService wuyeVoteTemplateService;

    /**
     * 新增投票记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "投票记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeVoteRecord wuyeVoteRecord)
    {
        // 检查是否在投票时间范围内
        WuyeVoteTemplate template = wuyeVoteTemplateService.selectWuyeVoteTemplateByTemplateId(wuyeVoteRecord.getTemplateId());
        if (template == null) {
            return error("投票模板不存在");
        }
        if (!template.isInVoteTimeRange()) {
            return error("不在投票时间范围内");
        }

        // 检查选项数量是否符合规则
        String[] choices = wuyeVoteRecord.getChoices().split(",");
        int maxChoices = Integer.parseInt(template.getRule());
        if (choices.length > maxChoices) {
            return error("选择的选项数量超过限制");
        }

        // 检查是否已经投过票
        WuyeVoteRecord query = new WuyeVoteRecord();
        query.setTemplateId(wuyeVoteRecord.getTemplateId());
        query.setAppletId(wuyeVoteRecord.getAppletId());
        query.setStatus(1L); // 有效票
        List<WuyeVoteRecord> existingVotes = wuyeVoteRecordService.selectWuyeVoteRecordList(query);
        if (!existingVotes.isEmpty()) {
            return error("该用户已经参与过此投票");
        }

        // 设置投票时间
        wuyeVoteRecord.setVoteTime(DateUtils.getNowDate());
        
        return toAjax(wuyeVoteRecordService.insertWuyeVoteRecord(wuyeVoteRecord));
    }

    /**
     * 小程序用户提交投票
     */
    @PostMapping("/submit")
    public AjaxResult submitVote(@RequestBody WuyeVoteRecord voteRecord)
    {
        // 检查是否在投票时间范围内
        WuyeVoteTemplate template = wuyeVoteTemplateService.selectWuyeVoteTemplateByTemplateId(voteRecord.getTemplateId());
        if (template == null) {
            return error("投票模板不存在");
        }
        if (!template.isInVoteTimeRange()) {
            return error("不在投票时间范围内");
        }

        // 检查选项数量是否符合规则
        String[] choices = voteRecord.getChoices().split(",");
        int maxChoices = Integer.parseInt(template.getRule());
        if (choices.length > maxChoices) {
            return error("选择的选项数量超过限制");
        }

        // 检查是否已经投过票
        WuyeVoteRecord query = new WuyeVoteRecord();
        query.setTemplateId(voteRecord.getTemplateId());
        query.setAppletId(voteRecord.getAppletId());
        query.setStatus(1L); // 有效票
        List<WuyeVoteRecord> existingVotes = wuyeVoteRecordService.selectWuyeVoteRecordList(query);
        if (!existingVotes.isEmpty()) {
            return error("您已经参与过此投票");
        }

        // 设置投票时间和状态
        voteRecord.setVoteTime(DateUtils.getNowDate());
        voteRecord.setStatus(1L); // 有效票
        voteRecord.setByAdmin(-1L); // 用户投票

        return toAjax(wuyeVoteRecordService.insertWuyeVoteRecord(voteRecord));
    }
} 