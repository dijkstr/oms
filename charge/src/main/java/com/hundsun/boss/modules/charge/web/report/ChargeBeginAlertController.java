package com.hundsun.boss.modules.charge.web.report;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.report.ChargeBeginAlert;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.charge.service.report.ChargeBeginAlertService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 第一次收入Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/report/chargeBeginAlert")
public class ChargeBeginAlertController extends BaseController {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private ChargeBeginAlertService chargeBeginAlertService;

    @RequiresPermissions("charge:report:chargeBeginAlert:view")
    @RequestMapping(value = { "list", "" })
    public String list(SearchForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ChargeBeginAlert> defaultpage = new Page<ChargeBeginAlert>(request, response);
        form.setPageNo(defaultpage.getPageNo());
        form.setPageSize(defaultpage.getPageSize());
        String month = Formatter.formatDate(Formatter.DATE_FORMAT4, new Date());
        if (CommonUtil.isNullorEmpty(form.getCharge_begin_month())) {
            form.setCharge_begin_month(month);
        }
        if (CommonUtil.isNullorEmpty(form.getCharge_end_month())) {
            form.setCharge_end_month(month);
        }
        Page<ChargeBeginAlert> page = chargeBeginAlertService.list(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("searchForm", form);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/report/chargeBeginAlertList";
    }
}
