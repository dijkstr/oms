package com.hundsun.boss.modules.charge.web.finance;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.charge.service.finance.FinSummaryService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 财务汇总表Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/finance/finSummary")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FinSummaryController extends BaseController {

    @Autowired
    private FinSummaryService finSummaryService;

    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:finance:finSummary:view")
    @RequestMapping(value = { "list", "" })
    public String list(FinSummaryForm finSummaryForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<FinSummaryForm>(request, response);
        finSummaryForm.setPageNo(defaultpage.getPageNo());
        finSummaryForm.setPageSize(defaultpage.getPageSize());
        Page<FinSummaryForm> page = finSummaryService.queryChargeBillList(defaultpage, finSummaryForm);
        model.addAttribute("page", page);
        model.addAttribute("finSummaryForm", finSummaryForm);
        model.addAttribute("office", officeService.findByCode(finSummaryForm.getOffice_id()));
        return "modules/" + "charge/finance/finSummaryList";
    }

    /**
     * 收入明细查看
     * 
     * @param finSummaryForm
     * @param model
     * @return
     */
    @RequestMapping(value = "/viewIncomeDetail")
    public String viewIncomeDetail(FinSummaryForm finSummaryForm, Model model) {
        try {
            // 生成下载用文件预览
            model.addAttribute("detail", finSummaryService.getFinSummaryDetailList(finSummaryForm));
        } catch (Exception e) {
            logger.error("error:", e);
        } finally {
        }
        return "modules/" + "charge/finance/finSummaryForm";
    }

    /**
     * 收入明细确认
     * 
     * @param finSummaryForm
     * @param model
     * @return
     */
    @RequestMapping(value = "/confirmIncomeDetail")
    public String confirmIncomeDetail(FinSummaryForm finSummaryForm, Model model) {
        try {
            // 生成下载用文件预览
            model.addAttribute("detail", finSummaryService.getFinSummaryDetailList(finSummaryForm));
            model.addAttribute("contract_id", finSummaryForm.getContract_id());
        } catch (Exception e) {
            logger.error("error:", e);
        } finally {
        }
        return "modules/" + "charge/finance/confirmIncomeForm";
    }

    /**
     * 确认收入
     * 
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/confirmFinanceIncome")
    public @ResponseBody Map<String, Object> confirmFinanceIncome(FinSummaryForm req, HttpServletRequest request) {
        Map mReturn = new HashMap();
        try {
            finSummaryService.confirmFinanceIncome(req);
            mReturn.put("result", "success");
        } catch (Exception e) {
            mReturn.put("result", "fail");
        }
        return mReturn;
    }

}
