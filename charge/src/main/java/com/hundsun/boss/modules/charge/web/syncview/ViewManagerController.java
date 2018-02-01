package com.hundsun.boss.modules.charge.web.syncview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.sync.SyncManager;
import com.hundsun.boss.modules.charge.form.common.SyncManagerForm;
import com.hundsun.boss.modules.charge.service.sync.SyncManagerService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;
/**
 * 客户经理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/sync/manager")
public class ViewManagerController extends BaseController{
    @Autowired
    private SyncManagerService syncManagerService;
/**
 * 获取客户经理
 * @param customermanagerno
 * @return
 */
    @ModelAttribute
    public SyncManager get(@RequestParam(required = false) String customermanagerno) {
        if (StringUtils.isNotBlank(customermanagerno)) {
            return syncManagerService.get(customermanagerno);
        } else {
            return new SyncManager();
        }
    }
/**
 * 列表分页展示客户经理信息
 * @param syncManager
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:manager:view")
    @RequestMapping(value = { "list", "" })
    public String list(SyncManagerForm syncManager, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }
        Page<SyncManager> page = syncManagerService.find(new Page<SyncManager>(request, response), syncManager);
        model.addAttribute("page", page);
        return "modules/" + "charge/sync/syncManagerList";
    }
/**
 * 表单显示客户经理信息
 * @param syncManager
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:manager:view")
    @RequestMapping(value = "form")
    public String form(SyncManagerForm syncManager, Model model) {
        model.addAttribute("syncManager", syncManager);
        return "modules/" + "charge/sync/syncManagerForm";
    }

/**
 * 删除客户经理信息
 * @param customermanagerno
 * @param redirectAttributes
 * @return
 */
    @RequiresPermissions("charge:sync:manager:edit")
    @RequestMapping(value = "delete")
    public String delete(String customermanagerno, RedirectAttributes redirectAttributes) {
        syncManagerService.delete(customermanagerno);
        addMessage(redirectAttributes, "删除客户成功");
        return "redirect:" + Global.getAdminPath() + "/charge/sync/manager/?repage";
    }

 
}
