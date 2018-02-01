package com.hundsun.boss.modules.charge.web.income;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeSourceInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeSourceInterfaceForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeSourceInterfaceService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 收入来源接口Controller
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
public class ChargeIncomeSourceInterfaceController extends BaseController {

    @Autowired
    private OfficeService officeService;

    @Autowired
    private ChargeIncomeSourceInterfaceService chargeIncomeSourceInterfaceService;

    /**
     * 获取计费来源系统
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public ChargeIncomeSourceInterface get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return chargeIncomeSourceInterfaceService.get(id);
        } else {
            return new ChargeIncomeSourceInterface();
        }
    }

    /**
     * 展示计费来源列表
     * 
     * @param chargeIncomeSourceInterface
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:income:chargeIncomeSourceInterface:view")
    @RequestMapping(value = "/${adminPath}/charge/income/chargeIncomeSourceInterface/list")
    public String list(ChargeIncomeSourceInterfaceForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ChargeIncomeSourceInterface> page = chargeIncomeSourceInterfaceService.find(new Page<ChargeIncomeSourceInterface>(request, response), form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/income/chargeIncomeSourceInterfaceList";
    }

    /**
     * 确认计费来源系统，准备推送给NC系统
     * 
     * @param chargeIncomeSourceInterface
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:income:chargeIncomeSourceInterface:edit")
    @RequestMapping(value = "/${adminPath}/charge/income/chargeIncomeSourceInterface/confirm")
    public String save(ChargeIncomeSourceInterfaceForm form, Model model, RedirectAttributes redirectAttributes) {
        chargeIncomeSourceInterfaceService.confirm(form.getIds());
        addMessage(model, "收入来源记录确认成功");
        return "redirect:" + Global.getAdminPath() + "/charge/income/chargeIncomeSourceInterface/list?repage";
    }

    /**
     * 查询计费来源列表给NC系统
     * 
     * @param receives
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/${frontPath}/charge/incomebycharge/refresh")
    public @ResponseBody Map refresh(String incomes, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        List<Map> retValues = new ArrayList<Map>();

        //返回给接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的收入来源token信息：" + token);
        try {
            //将协同推送的json字符串转成对象list
            List<ChargeIncomeSourceInterfaceForm> incomeslist = FormUtils.getListByJson(incomes, ChargeIncomeSourceInterfaceForm.class);
            ChargeIncomeSourceInterfaceForm form = null;
            if (incomeslist.size() > 0) {
                form = incomeslist.get(0);
            }
            form.setSend_flag(ChargeConstant.SEND_FLAG_PENDING);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(incomeslist);
            logger.info(new Date() + "协同推送的收入来源查询信息：" + reslist);
            //根据收入条件查询计费给财务的收入来源
            List<ChargeIncomeSourceInterface> chargeIncomeSourceInterfaces = chargeIncomeSourceInterfaceService.getUnsendPageData(form.getOffset(), form.getPagesize());
            for (int i = 0; i < chargeIncomeSourceInterfaces.size(); i++) {
                Map retValue = new HashMap();
                retValue.put("contractid", chargeIncomeSourceInterfaces.get(i).getContract_id());
                retValue.put("con_id", chargeIncomeSourceInterfaces.get(i).getCon_id());
                retValue.put("income_source", chargeIncomeSourceInterfaces.get(i).getIncome_source());
                retValues.add(retValue);
            }

            //设置分页查询的数据
            result.put("income", retValues);
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回给接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 更新计费收入来源接口表中的更新标志
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/${frontPath}/charge/incomebycharge/submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回收入来源接口的token信息
        result.put("token", token);
        try {
            // 更新已推送收入
            chargeIncomeSourceInterfaceService.setIncomeSourceSent();
            //返回收入来源接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回给收入来源接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }
}
