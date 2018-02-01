package com.hundsun.boss.modules.charge.web.finance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.charge.service.finance.PayableService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 应收款管理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/finance/payable")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PayableController extends BaseController {

    @Autowired
    private PayableService payableService;
    
    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:finance:payable:view")
    @RequestMapping(value = { "list", "" })
    public String list(FinSummaryForm finSummaryForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<FinSummaryForm>(request, response);
        finSummaryForm.setPageNo(defaultpage.getPageNo());
        finSummaryForm.setPageSize(defaultpage.getPageSize());
        Page<FinSummaryForm> page = payableService.payableList(defaultpage, finSummaryForm);
        model.addAttribute("page", page);
        model.addAttribute("finSummaryForm", finSummaryForm);
        model.addAttribute("office", officeService.findByCode(finSummaryForm.getOffice_id()));
        return "modules/" + "charge/finance/payableList";
    }

}
