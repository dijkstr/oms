package com.hundsun.boss.modules.charge.web.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;
import com.hundsun.boss.modules.charge.service.sync.SyncCustomerService;

/**
 * 客户Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/customer")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncCustomerController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncCustomerService syncCustomerService;

    @ModelAttribute
    public SyncCustomer get(@RequestParam(required = false) String customerId) {
        if (StringUtils.isNotBlank(customerId)) {
            return syncCustomerService.get(customerId);
        } else {
            return new SyncCustomer();
        }
    }
 
    private List<SyncCustomer> syncCustomers = new ArrayList<SyncCustomer>();

    /**
     * 清除协同客户缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的客户接口的token信息
        result.put("token", token);
        try {
            //清除客户缓存数据
            syncCustomers.clear();
            //返回给协同的客户接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的客户接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 保存协同客户数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的客户接口的token信息
        result.put("token", token);
        try {
            for (int i = 0; i < syncCustomers.size(); i++) {
                //获取数据库中对应的客户编号的客户对象
                SyncCustomer syncCustomer = syncCustomerService.get(syncCustomers.get(i).getCustomerid());
                if (!CommonUtil.isNullorEmpty(syncCustomer)) {
                    //如果客户存在，更新为同步过来的对象
                    BeanUtils.copyProperties(syncCustomer, syncCustomers.get(i));
                } else {
                    //客户对象在数据库不存在，直接赋值
                    syncCustomer = syncCustomers.get(i);
                }
                //保存新的客户对象
                syncCustomerService.save(syncCustomer);
            }
            //返回给协同的客户接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的客户接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 新增与编辑协同客户数据
     * 
     * @param customers
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String customers, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
      //返回给协同的客户接口的token信息
        result.put("token", token);
        logger.info(new Date()+"协同推送的客户token信息："+token);
        try {
            //将协同推送的客户json字符串转成客户对象list
            List<SyncCustomer> customerslist = FormUtils.getListByJson(customers, SyncCustomer.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist=objectMapper.writeValueAsString(customerslist);
            logger.info(new Date()+"协同推送的客户信息："+reslist);
            //将客户对象list添加到缓存对象中
            syncCustomers.addAll(customerslist);
            //返回给协同的客户接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的客户接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }
}
