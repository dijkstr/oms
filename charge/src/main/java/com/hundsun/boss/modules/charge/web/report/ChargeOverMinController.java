package com.hundsun.boss.modules.charge.web.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.entity.report.ChargeOverMin;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.charge.service.report.ChargeOverMinService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 第一次超保底Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/report/chargeOverMin")
public class ChargeOverMinController extends BaseController {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private ChargeOverMinService chargeOverMinService;

    @RequiresPermissions("charge:report:chargeOverMin:view")
    @RequestMapping(value = { "list", "" })
    public String list(SearchForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ChargeOverMin> defaultpage = new Page<ChargeOverMin>(request, response);
        form.setPageNo(defaultpage.getPageNo());
        form.setPageSize(defaultpage.getPageSize());
        Page<ChargeOverMin> page = chargeOverMinService.list(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("searchForm", form);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/report/chargeOverMinList";
    }
}
