package com.hundsun.boss.modules.charge.web.income;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeService;

/**
 * 计费财务收入接口Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/charge/income")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeIncomeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ChargeIncomeController.class);
    @Autowired
    private ChargeIncomeService chargeIncomeService;

    private List<ChargeIncomeInterface> syncIncomes = new ArrayList<ChargeIncomeInterface>();

    /**
     * 查询计费给财务的收入
     * 
     * @param receives
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String incomes, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的财务收入token信息：" + token);
        try {
            //将协同推送的json字符串转成对象list
            List<ChargeIncomeForm> incomeslist = FormUtils.getListByJson(incomes, ChargeIncomeForm.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(incomeslist);
            logger.info(new Date() + "协同推送的财务收入查询信息：" + reslist);
            //根据收入条件查询计费给财务的收入,同时数据备份到本地
            syncIncomes = chargeIncomeService.get(incomeslist.get(0));
            //设置分页查询的数据
            result.put("income", syncIncomes);

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
     * 更新计费财务收入接口表中的更新标志
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, String incomemonths, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回财务收入接口的token信息
        result.put("token", token);
        try {
            // 备份财务固化收入
            chargeIncomeService.backup(incomemonths);
            // 更新已推送收入
            chargeIncomeService.update(incomemonths);
            //返回财务收入接口的处理成功信息
            result.put("result", "success");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回给财务收入接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 清除财务接口缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回财务收入接口的token信息
        result.put("token", token);
        try {
            //清除财务收入缓存数据
            syncIncomes.clear();
            //返回财务收入接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回财务接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

}
