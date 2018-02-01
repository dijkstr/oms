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
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.charge.form.common.SyncProductForm;
import com.hundsun.boss.modules.charge.service.sync.SyncProductService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;
/**
 * 协同产品Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/sync/product")

public class ViewProductController extends BaseController{
    @Autowired
    private SyncProductService syncProductService;
/**
 * 获取产品
 * @param prdid
 * @return
 */
    @ModelAttribute
    public SyncProduct get(@RequestParam(required = false) String prdid) {
        if (StringUtils.isNotBlank(prdid)) {
            return syncProductService.get(prdid);
        } else {
            return new SyncProduct();
        }
    }
/**
 * 列表分页显示产品
 * @param syncProduct
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:product:view")
    @RequestMapping(value = { "list", "" })
    public String list(SyncProductForm syncProductForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }
        Page<SyncProduct> page = syncProductService.find(new Page<SyncProduct>(request, response), syncProductForm);
        model.addAttribute("page", page);
        return "modules/" + "charge/sync/syncProductList";
    }
/**
 * 表单显示产品
 * @param syncProduct
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:product:view")
    @RequestMapping(value = "form")
    public String form(SyncProductForm syncProduct, Model model) {
        model.addAttribute("syncProduct", syncProduct);
        return "modules/" + "charge/sync/syncProductForm";
    }
/**
 * 删除产品
 * @param prdid
 * @param redirectAttributes
 * @return
 */
    @RequiresPermissions("charge:sync:product:edit")
    @RequestMapping(value = "delete")
    public String delete(String prdid, RedirectAttributes redirectAttributes) {
        syncProductService.delete(prdid);
        addMessage(redirectAttributes, "删除产品成功");
        return "redirect:" + Global.getAdminPath() + "/charge/sync/product/?repage";
    }

  
}
