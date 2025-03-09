package com.ruoyi.web.controller.vote;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.wuye.domain.vote.WuyeVoteTemplate;
import com.ruoyi.wuye.service.vote.IWuyeVoteTemplateService;
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

/**
 * 投票模板Controller
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/system/template")
public class WuyeVoteTemplateController extends BaseController
{
    @Autowired
    private IWuyeVoteTemplateService wuyeVoteTemplateService;

    /**
     * 查询投票模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(WuyeVoteTemplate wuyeVoteTemplate)
    {
//        startPage();
        List<WuyeVoteTemplate> list = wuyeVoteTemplateService.selectWuyeVoteTemplateList(wuyeVoteTemplate);
        return getDataTable(list);
    }

    /**
     * 导出投票模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:template:export')")
    @Log(title = "投票模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WuyeVoteTemplate wuyeVoteTemplate)
    {
        List<WuyeVoteTemplate> list = wuyeVoteTemplateService.selectWuyeVoteTemplateList(wuyeVoteTemplate);
        ExcelUtil<WuyeVoteTemplate> util = new ExcelUtil<WuyeVoteTemplate>(WuyeVoteTemplate.class);
        util.exportExcel(response, list, "投票模板数据");
    }

    /**
     * 获取投票模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:template:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return success(wuyeVoteTemplateService.selectWuyeVoteTemplateByTemplateId(templateId));
    }

    /**
     * 获取小程序用户投票模板详情
     */
    @GetMapping("/detail/{templateId}")
    public AjaxResult getTemplateDetail(@PathVariable("templateId") Long templateId)
    {
        return success(wuyeVoteTemplateService.selectWuyeVoteTemplateByTemplateId(templateId));
    }

    /**
     * 新增投票模板
     */
    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "投票模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WuyeVoteTemplate wuyeVoteTemplate)
    {
        return toAjax(wuyeVoteTemplateService.insertWuyeVoteTemplate(wuyeVoteTemplate));
    }

    /**
     * 修改投票模板
     */
    @PreAuthorize("@ss.hasPermi('system:template:edit')")
    @Log(title = "投票模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WuyeVoteTemplate wuyeVoteTemplate)
    {
        return toAjax(wuyeVoteTemplateService.updateWuyeVoteTemplate(wuyeVoteTemplate));
    }

    /**
     * 删除投票模板
     */
    @PreAuthorize("@ss.hasPermi('system:template:remove')")
    @Log(title = "投票模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(wuyeVoteTemplateService.deleteWuyeVoteTemplateByTemplateIds(templateIds));
    }

    /**
     * 查询用户可参与的投票模板列表
     */
    @GetMapping("/user/{appletId}")
    public AjaxResult userList(
            @PathVariable("appletId") Long appletId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status)
    {
        List<WuyeVoteTemplate> list = wuyeVoteTemplateService.selectUserVoteTemplateList(appletId, keyword, status);
        return success(list);
    }
}
