package com.hundsun.boss.modules.charge.web.income;

import java.net.URLEncoder;
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
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomePeriodInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomePeriodInterfaceForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomePeriodInterfaceService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 收入期间接口Controller
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
public class ChargeIncomePeriodInterfaceController extends BaseController {

    @Autowired
    private OfficeService officeService;

    @Autowired
    private ChargeIncomePeriodInterfaceService chargeIncomePeriodInterfaceService;

    /**
     * 获取计费期间系统
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public ChargeIncomePeriodInterface get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return chargeIncomePeriodInterfaceService.get(id);
        } else {
            return new ChargeIncomePeriodInterface();
        }
    }

    /**
     * 展示计费期间列表
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:income:chargeIncomePeriodInterface:view")
    @RequestMapping(value = "/${adminPath}/charge/income/chargeIncomePeriodInterface/list")
    public String list(ChargeIncomePeriodInterfaceForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ChargeIncomePeriodInterface> page = chargeIncomePeriodInterfaceService.find(new Page<ChargeIncomePeriodInterface>(request, response), form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/income/chargeIncomePeriodInterfaceList";
    }

    /**
     * 展示合同级的计费期间列表
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:income:chargeIncomePeriodInterface:edit")
    @RequestMapping(value = "/${adminPath}/charge/income/chargeIncomePeriodInterface/form")
    public String form(ChargeIncomePeriodInterfaceForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 默认查询新建与待发送记录
        Page<ChargeIncomePeriodInterface> page = chargeIncomePeriodInterfaceService.find(new Page<ChargeIncomePeriodInterface>(request, response, -1), form);
        model.addAttribute("incomeSettingForm", form);
        model.addAttribute("page", page);
        return "modules/" + "charge/income/chargeIncomePeriodInterfaceForm";
    }

    /**
     * 确认计费期间系统，准备推送给NC系统
     * 
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("charge:income:chargeIncomePeriodInterface:edit")
    @RequestMapping(value = "/${adminPath}/charge/income/chargeIncomePeriodInterface/confirm")
    public String confirm(ChargeIncomePeriodInterfaceForm form, Model model, RedirectAttributes redirectAttributes) throws Exception {
        chargeIncomePeriodInterfaceService.confirm(form.getIds());
        addMessage(model, "收入期间记录确认成功");
        // 如果是在收入期间接口画面确认，返回同画面
        if (CommonUtil.isNullorEmpty(form.getBacktype())) {
            return "redirect:" + Global.getAdminPath() + "/charge/income/chargeIncomePeriodInterface/list?repage";
        } else {
            // 如果在合同收入期间接口画面确认，返回同画面
            return "redirect:" + Global.getAdminPath() + "/charge/income/chargeIncomePeriodInterface/form?contract_id=" + URLEncoder.encode(form.getContract_id(), "UTF-8");
        }
    }

    /**
     * 查询计费期间列表给NC系统
     * 
     * @param incomes
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/${frontPath}/charge/incomedate/refresh")
    public @ResponseBody Map refresh(String incomes, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        List<Map> retValues = new ArrayList<Map>();

        //返回给接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的收入期间token信息：" + token);
        try {
            //将协同推送的json字符串转成对象list
            List<ChargeIncomePeriodInterfaceForm> incomeslist = FormUtils.getListByJson(incomes, ChargeIncomePeriodInterfaceForm.class);
            ChargeIncomePeriodInterfaceForm form = null;
            if (incomeslist.size() > 0) {
                form = incomeslist.get(0);
            }
            form.setSend_flag(ChargeConstant.SEND_FLAG_PENDING);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(incomeslist);
            logger.info(new Date() + "协同推送的收入期间查询信息：" + reslist);
            //根据收入条件查询计费给财务的收入期间
            List<ChargeIncomePeriodInterface> chargeIncomePeriodInterfaces = chargeIncomePeriodInterfaceService.getUnsendPageData(form.getOffset(), form.getPagesize());
            for (int i = 0; i < chargeIncomePeriodInterfaces.size(); i++) {
                Map retValue = new HashMap();
                retValue.put("detailid", chargeIncomePeriodInterfaces.get(i).getDetailid());
                retValue.put("accountidentity", "10932");
                retValue.put("con_id", chargeIncomePeriodInterfaces.get(i).getCon_id());
                retValue.put("contract_id", chargeIncomePeriodInterfaces.get(i).getContract_id());
                retValue.put("ex_product_id", chargeIncomePeriodInterfaces.get(i).getEx_product_id());
                retValue.put("product_id", chargeIncomePeriodInterfaces.get(i).getProduct_id());
                retValue.put("payment_type", chargeIncomePeriodInterfaces.get(i).getPayment_type());
                retValue.put("servicestartdate", chargeIncomePeriodInterfaces.get(i).getServicestartdate());
                retValue.put("serviceenddate", chargeIncomePeriodInterfaces.get(i).getServiceenddate());
                retValue.put("income_begin_date", chargeIncomePeriodInterfaces.get(i).getIncome_begin_date());
                retValue.put("income_end_date", chargeIncomePeriodInterfaces.get(i).getIncome_end_date());
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
     * 更新计费收入期间接口表中的更新标志
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/${frontPath}/charge/incomedate/submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回收入期间接口的token信息
        result.put("token", token);
        try {
            // 更新已推送收入
            chargeIncomePeriodInterfaceService.setIncomePeriodSent();
            //返回收入期间接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回给收入期间接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }
}
