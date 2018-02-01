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
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.charge.service.sync.SyncProductService;
/**
 * 协同产品Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/product")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncProductController extends BaseController{
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncProductService syncProductService;

    @ModelAttribute
    public SyncProduct get(@RequestParam(required = false) String prdid) {
        if (StringUtils.isNotBlank(prdid)) {
            return syncProductService.get(prdid);
        } else {
            return new SyncProduct();
        }
    }

    private List<SyncProduct> syncProducts = new ArrayList<SyncProduct>();

    /**
     * 清除协同产品缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
      //返回给协同的产品接口的token信息
        result.put("token", token);
        try {
          //清除产品缓存数据
            syncProducts.clear();
            //返回给协同的产品接口的处理成功信息
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
     * 保存协同产品数据
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的产品接口的token信息
        result.put("token", token);
        try {
            for (int i = 0; i < syncProducts.size(); i++) {
                //获取数据库中对应的产品对象
                SyncProduct syncProduct = syncProductService.get(syncProducts.get(i).getPrdid());
                if (!CommonUtil.isNullorEmpty(syncProduct)) {
                    //如果产品存在，更新为同步过来的对象
                    BeanUtils.copyProperties(syncProduct, syncProducts.get(i));
                } else {
                    //产品对象在数据库不存在，直接赋值
                    syncProduct = syncProducts.get(i);
                }
                //保存新的产品对象
                syncProductService.save(syncProduct);
            }
            //返回给协同的产品接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的产品接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 新增与编辑协同产品数据
     * 
     * @param prdsales
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String prdsales, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
      //返回给协同的产品接口的token信息
        result.put("token", token);
        logger.info(new Date()+"协同推送的产品token信息："+token);
        try {
            //将协同推送的产品json字符串转成产品对象list
            List<SyncProduct> prdsaleslist = FormUtils.getListByJson(prdsales, SyncProduct.class);
          //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist=objectMapper.writeValueAsString(prdsaleslist);
            logger.info(new Date()+"协同推送的产品信息："+reslist);
            //将产品对象list添加到缓存对象中
            syncProducts.addAll(prdsaleslist);
            //返回给协同的产品接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给协同的产品接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }
}
