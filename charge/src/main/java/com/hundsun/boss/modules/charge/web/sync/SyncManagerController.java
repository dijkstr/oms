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
import com.hundsun.boss.modules.charge.entity.sync.SyncManager;
import com.hundsun.boss.modules.charge.service.sync.SyncManagerService;
/**
 * 客户经理Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/manager")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncManagerController extends BaseController{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncManagerService syncManagerService;

    @ModelAttribute
    public SyncManager get(@RequestParam(required = false) String customermanagerno) {
        if (StringUtils.isNotBlank(customermanagerno)) {
            return syncManagerService.get(customermanagerno);
        } else {
            return new SyncManager();
        }
    }

    private List<SyncManager> syncManagers = new ArrayList<SyncManager>();

    /**
     * 清除协同客户经理缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的客户经理接口的token信息
        result.put("token", token);
        try {
            //清除客户经理缓存数据
            syncManagers.clear();
            //返回给协同的客户经理接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的客户经理接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 保存协同客户经理数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的客户经理接口的token信息
        result.put("token", token);
        try {
            for (int i = 0; i < syncManagers.size(); i++) {
                //获取数据库中对应的客户经理对象
                SyncManager syncManager = syncManagerService.get(syncManagers.get(i).getCustomermanagerno());
                if (!CommonUtil.isNullorEmpty(syncManager)) {
                    //如果客户经理存在，更新为同步过来的对象
                    BeanUtils.copyProperties(syncManager, syncManagers.get(i));
                } else {
                    //客户经理对象在数据库不存在，直接赋值
                    syncManager = syncManagers.get(i);
                }
                //保存新的客户经理对象
                syncManagerService.save(syncManager);
            }
            //返回给协同的客户经理接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的客户经理接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 新增与编辑协同客户经理数据
     * 
     * @param managers
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String managers, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
      //返回给协同的客户经理接口的token信息
        result.put("token", token);
        logger.info(new Date()+"协同推送的客户经理token信息："+token);
        try {
            //将协同推送的客户经理json字符串转成客户经理对象list
            List<SyncManager> managerslist = FormUtils.getListByJson(managers, SyncManager.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist=objectMapper.writeValueAsString(managerslist);
            logger.info(new Date()+"协同推送的客户经理信息："+reslist);
            //将客户经理对象list添加到缓存对象中
            syncManagers.addAll(managerslist);
            //返回给协同的客户经理接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的客户经理接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

}
