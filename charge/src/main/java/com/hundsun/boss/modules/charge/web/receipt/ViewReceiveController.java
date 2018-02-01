package com.hundsun.boss.modules.charge.web.receipt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.receipt.ChargeReceipt;
import com.hundsun.boss.modules.charge.form.receipt.ChargeReceiptForm;
import com.hundsun.boss.modules.charge.service.receipt.ChargeReceiptService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同到款Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/account/receive")
//@SuppressWarnings({"unchecked","rawtypes"})
public class ViewReceiveController extends BaseController {
    @Autowired
    private ChargeReceiptService chargeReceiptService;
    @Autowired
    private OfficeService officeService;
/**
 * 获取到款
 * @param receiveid
 * @return
 */
    @ModelAttribute
    public ChargeReceipt get(@RequestParam(required = false) String receiveid) {
        if (StringUtils.isNotBlank(receiveid)) {
            return chargeReceiptService.get(receiveid);
        } else {
            return new ChargeReceipt();
        }
    }
/**
 * 列表分页显示到款
 * @param chargeReceiptForm
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:account:receive:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeReceiptForm chargeReceiptForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }
        //绑定部门
        model.addAttribute("office", officeService.findByCode(chargeReceiptForm.getDepartment()));
        Page<ChargeReceiptForm> defaultpage = new Page<ChargeReceiptForm>(request, response);
        chargeReceiptForm.setPageNo(defaultpage.getPageNo());
        chargeReceiptForm.setPageSize(defaultpage.getPageSize());
        Page<ChargeReceiptForm> page = chargeReceiptService.find(defaultpage, chargeReceiptForm);    
        
        model.addAttribute("page", page);
//        model.addAttribute("chargeReceiptForm",chargeReceiptForm);
        return "modules/" + "charge/account/chargeReceiveList";
    }
/**
 * 表单显示到款
 * @param chargeReceipt
 * @param model
 * @return
 */
    @RequiresPermissions("charge:account:receive:view")
    @RequestMapping(value = "form")
    public String form(ChargeReceiptForm chargeReceipt, Model model) {
        model.addAttribute("syncReceive", chargeReceipt);
        return "modules/" + "charge/account/chargeReceiveForm";
    }


   
}
