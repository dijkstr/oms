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
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.charge.service.finance.FinIncomeInterfaceService;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 财务收入接口Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/finance/finIncomeInterface")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FinIncomeInterfaceController extends BaseController {

    @Autowired
    private FinIncomeInterfaceService finIncomeInterfaceService;
    
    @Autowired
    private ChargeIncomeService chargeIncomeService;

    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:finance:finIncomeInterface:view")
    @RequestMapping(value = { "list", "" })
    public String list(FinSummaryForm finSummaryForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<FinSummaryForm>(request, response);
        finSummaryForm.setPageNo(defaultpage.getPageNo());
        finSummaryForm.setPageSize(defaultpage.getPageSize());
        Page<FinSummaryForm> page = finIncomeInterfaceService.queryFinanceIncomeList(defaultpage, finSummaryForm);
        model.addAttribute("page", page);
        model.addAttribute("finSummaryForm", finSummaryForm);
        model.addAttribute("office", officeService.findByCode(finSummaryForm.getOffice_id()));
        return "modules/" + "charge/finance/finIncomeInterfaceList";
    }
    
    /**
     * 显示删除条件
     * 
     * @return
     */
    @RequiresPermissions("charge:finance:finIncomeInterface:delete")
    @RequestMapping(value = "/contractIdForDeleteShow")
    public String contractIdForDeleteShow() {
        return "modules/" + "charge/finance/contractIdForDeleteShow";
    }
    
    /**
     * 根据协同合同编号删除历史收入记录
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequiresPermissions("charge:finance:finIncomeInterface:delete")
    @RequestMapping(value = "/deleteIncomeByContractId")
    public @ResponseBody Map<String, Object> deleteBill(DownloadForm downloadForm, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            chargeIncomeService.deleteIncomeByContractId(downloadForm.getContract_id());
            data.put("result", "success");
            data.put("message", "收入删除成功");
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", "收入删除失败");
        }
        return data;
    }
    
    /**
     * 显示导出条件
     * 
     * @return
     */
    @RequiresPermissions("charge:finance:finIncomeInterface:view")
    @RequestMapping(value = "/dateForNCIncomeShow")
    public String dateForNCIncomeShow() {
        return "modules/" + "charge/finance/dateForNCIncomeShow";
    }
}
