package com.ruoyi.web.controller.vote;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.wuye.domain.vote.WuyeVoteRecord;
import com.ruoyi.wuye.service.vote.IWuyeVoteRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wuye.domain.vote.WuyeVoteTemplate;
import com.ruoyi.wuye.service.vote.IWuyeVoteTemplateService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.wuye.service.user.IWuyeAppletUsersService;
import com.ruoyi.common.core.domain.entity.wuye.user.WuyeAppletUsers;

/**
 * 投票记录Controller
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/system/record")
public class WuyeVoteRecordController extends BaseController
{
    @Autowired
    private IWuyeVoteRecordService wuyeVoteRecordService;

    @Autowired
    private IWuyeVoteTemplateService wuyeVoteTemplateService;

    @Autowired
    private IWuyeAppletUsersService wuyeAppletUsersService;

    /**
     * 查询投票记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WuyeVoteRecord wuyeVoteRecord)
    {
        startPage();
        List<WuyeVoteRecord> list = wuyeVoteRecordService.selectWuyeVoteRecordList(wuyeVoteRecord);
        return getDataTable(list);
    }

    /**
     * 导出投票记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "投票记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeVoteRecord wuyeVoteRecord)
    {
        List<WuyeVoteRecord> list = wuyeVoteRecordService.selectWuyeVoteRecordList(wuyeVoteRecord);
        ExcelUtil<WuyeVoteRecord> util = new ExcelUtil<WuyeVoteRecord>(WuyeVoteRecord.class);
        util.exportExcel(response, list, "投票记录数据");
    }

    /**
     * 获取投票记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{voteId}")
    public AjaxResult getInfo(@PathVariable("voteId") Long voteId)
    {
        return success(wuyeVoteRecordService.selectWuyeVoteRecordByVoteId(voteId));
    }

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
     * 修改投票记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "投票记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeVoteRecord wuyeVoteRecord)
    {
        return toAjax(wuyeVoteRecordService.updateWuyeVoteRecord(wuyeVoteRecord));
    }

    /**
     * 删除投票记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "投票记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{voteIds}")
    public AjaxResult remove(@PathVariable Long[] voteIds)
    {
        return toAjax(wuyeVoteRecordService.deleteWuyeVoteRecordByVoteIds(voteIds));
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

    /**
     * 获取投票详情（包含用户投票记录）
     */
    @GetMapping("/detail/{templateId}")
    public AjaxResult getVoteDetail(@PathVariable("templateId") Long templateId)
    {
        // 获取当前登录用户的appletId
        Long appletId = SecurityUtils.getLoginUser().getAppletUser().getAppletId();
        
        // 查询用户的投票记录
        WuyeVoteRecord query = new WuyeVoteRecord();
        query.setTemplateId(templateId);
        query.setAppletId(appletId);
        query.setStatus(1L); // 有效票
        List<WuyeVoteRecord> records = wuyeVoteRecordService.selectWuyeVoteRecordList(query);
        
        // 查询投票模板信息
        WuyeVoteTemplate template = wuyeVoteTemplateService.selectWuyeVoteTemplateByTemplateId(templateId);
        if (template == null) {
            return error("投票模板不存在");
        }

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("template", template);
        data.put("userChoice", records.isEmpty() ? null : records.get(0).getChoices());
        
        return success(data);
    }

    /**
     * 根据姓名和手机号查找用户
     */
    @GetMapping("/searchUser")
    public AjaxResult searchUser(@RequestParam String name, @RequestParam String mobile)
    {
        WuyeAppletUsers user = wuyeAppletUsersService.selectUserByNameAndMobile(name, mobile);
        return success(user);
    }

    /**
     * 根据房屋IDs获取可投票的模板列表
     */
    @GetMapping("/templates/{houseIds}")
    public AjaxResult getTemplatesByHouseIds(@PathVariable("houseIds") String houseIds)
    {
        List<WuyeVoteTemplate> templates = wuyeVoteTemplateService.selectTemplatesByHouseIds(houseIds);
        return success(templates);
    }
}
