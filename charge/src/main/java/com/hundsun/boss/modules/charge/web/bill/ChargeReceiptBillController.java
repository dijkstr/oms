package com.hundsun.boss.modules.charge.web.bill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.form.bill.ChargeReceiptForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeReceiptBillService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 详单管理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/bill/chargeReceipt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeReceiptBillController extends BaseController {

    @Autowired
    private ChargeReceiptBillService chargeReceiptBillService;

    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:bill:chargeReceipt:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeReceiptForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<ChargeReceiptForm>(request, response);
        form.setPageNo(defaultpage.getPageNo());
        form.setPageSize(defaultpage.getPageSize());
        Page<ChargeReceiptForm> page = chargeReceiptBillService.queryChargeReceiptList(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("chargeReceiptForm", form);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/bill/chargeReceiptList";
    }
    
    // 收入详单
    @RequestMapping(value = "/viewChargeReceipt")
    public String viewIncomeDetail(ChargeReceiptForm form, Model model) {
        try {
            // 生成下载用文件预览
            model.addAttribute("detail", chargeReceiptBillService.getFinSummaryDetailList(form));
        } catch (Exception e) {
            logger.error("error:", e);
        } finally {
        }
        return "modules/" + "charge/bill/chargeReceiptForm";
    }
    
    @RequiresPermissions("charge:bill:chargeReceipt:view")
    @RequestMapping(value = "chargeDetailPDFExport")
    public String chargeDetailPDFExport() {
        return "modules/" + "charge/bill/chargeDetailPDFExport";
    }
}
