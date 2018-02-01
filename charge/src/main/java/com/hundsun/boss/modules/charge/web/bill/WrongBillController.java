package com.hundsun.boss.modules.charge.web.bill;

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
import com.hundsun.boss.modules.charge.form.bill.WrongBillForm;
import com.hundsun.boss.modules.charge.service.bill.WrongBillService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 错单管理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/bill/wrongBill")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WrongBillController extends BaseController {

    @Autowired
    private WrongBillService wrongBillService;

    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:bill:wrongBill:view")
    @RequestMapping(value = { "list", "" })
    public String list(WrongBillForm wrongBill, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<WrongBillForm>(request, response);
        wrongBill.setPageNo(defaultpage.getPageNo());
        wrongBill.setPageSize(defaultpage.getPageSize());
        Page<WrongBillForm> page = wrongBillService.queryWrongBillList(defaultpage, wrongBill);
        model.addAttribute("page", page);
        model.addAttribute("wrongBillForm", wrongBill);
        model.addAttribute("office", officeService.findByCode(wrongBill.getOffice_id()));
        return "modules/" + "charge/bill/wrongBillList";
    }

    /**
     * 批量关闭错单
     * 
     * @param req
     * @return @
     */
    @RequestMapping(value = "/updateStatus")
    public @ResponseBody Map<String, Object> updateStatus(WrongBillForm req, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        int count = 0;
        String[] ids = req.getId().split(",");
        for (String id : ids) {
            req.setId(id);
            int num = wrongBillService.updateStatus(req);
            count += num;
        }
        if (count > 0) {
            data.put("result", "success");
            data.put("message", "错单关闭成功");
        } else {
            data.put("result", "fail");
            data.put("message", "错单关闭失败：");
        }
        return data;
    }
}
