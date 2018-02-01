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
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;
import com.hundsun.boss.modules.charge.form.common.SyncCustomerForm;
import com.hundsun.boss.modules.charge.service.sync.SyncCustomerService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 客户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/sync/customer")
public class ViewCustomerController extends BaseController {

    @Autowired
    private SyncCustomerService syncCustomerService;
/**
 * 获取客户对象
 * @param customerId
 * @return
 */
    @ModelAttribute
    public SyncCustomer get(@RequestParam(required = false) String customerId) {
        if (StringUtils.isNotBlank(customerId)) {
            return syncCustomerService.get(customerId);
        } else {
            return new SyncCustomer();
        }
    }
/**
 * 列表分页展示协同客户信息
 * @param syncCustomer
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:customer:view")
    @RequestMapping(value = { "list", "" })
    public String list(SyncCustomerForm syncCustomer, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            //            syncCustomer.setCreateBy(user);
        }
        Page<SyncCustomer> page = syncCustomerService.find(new Page<SyncCustomer>(request, response), syncCustomer);
        model.addAttribute("page", page);
        return "modules/" + "charge/sync/syncCustomerList";
    }
/**
 * 表单显示客户信息
 * @param syncCustomer
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:customer:view")
    @RequestMapping(value = "form")
    public String form(SyncCustomerForm syncCustomer, Model model) {
        model.addAttribute("syncCustomer", syncCustomer);
        return "modules/" + "charge/sync/syncCustomerForm";
    }
    
/**
 * 删除客户
 * @param customerid
 * @param redirectAttributes
 * @return
 */
    @RequiresPermissions("charge:sync:customer:edit")
    @RequestMapping(value = "delete")
    public String delete(String customerid, RedirectAttributes redirectAttributes) {
        syncCustomerService.delete(customerid);
        addMessage(redirectAttributes, "删除客户成功");
        return "redirect:" + Global.getAdminPath() + "/charge/sync/customer/?repage";
    } 
  
}
