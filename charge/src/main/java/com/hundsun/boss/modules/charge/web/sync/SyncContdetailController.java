package com.hundsun.boss.modules.charge.web.sync;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;
import com.hundsun.boss.modules.charge.entity.sync.SyncDeleteDetail;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.sync.SyncContdetailService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;

/**
 * 合同子表Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/contdetail")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncContdetailController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncContdetailService syncContdetailService;
    @Autowired
    private SyncContractService syncContractService;
    @Autowired
    private OrderInfoService orderInfoService;

    @ModelAttribute
    public SyncContdetail get(@RequestParam(required = false) String detailid) {
        if (StringUtils.isNotBlank(detailid)) {
            return syncContdetailService.get(detailid);
        } else {
            return new SyncContdetail();
        }
    }
    
   //合同子表删除集合
    private List<SyncDeleteDetail> removeDetails = new ArrayList<SyncDeleteDetail>();

    /**
     * 删除协同合同子表数据
     * 
     * @param contracts
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "remove")
    public @ResponseBody Map remove(String removedetails, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同子表接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的合同子表token信息：" + token);
        try {
            //将协同推送的json字符串转成客户对象list
            List<SyncDeleteDetail> removeslist = FormUtils.getListByJson(removedetails, SyncDeleteDetail.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(removeslist);
            logger.info(new Date() + "协同推送的合同子表信息：" + reslist);
            //将合同子表对象list添加到缓存对象中
            removeDetails.addAll(removeslist);
            //返回给协同的合同子表接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同子表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 提交删除 合同子表数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "removesubmit")
    public @ResponseBody Map removesubmit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同子表接口的token信息
        result.put("token", token);
        try {
            //存在标志
            boolean isexist=false;
            for (int i = 0; i < removeDetails.size(); i++) {
                //获取数据库中对应的合同子表对象
                SyncContdetail syncContdetail = syncContdetailService.get(removeDetails.get(i).getDetailid());
                if (!CommonUtil.isNullorEmpty(syncContdetail)) {
                    isexist=true;
                    //如果合同子表存在，删除合同子表
                    syncContdetailService.delete(syncContdetail.getDetailid());
                }
            }
            //返回给协同的合同子表接口的处理成功信息
            result.put("result", "success");
            if(isexist){
              //调用存储过程校验合同信息
                //有合同表体数据删除，则调用校验存储过程全部校验
             syncContractService.validateContract(null);
            }
            
            
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同子表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 清除协同合同子表缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "removecancel")
    public @ResponseBody Map removecancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同子表接口的token信息
        result.put("token", token);
        try {
            //清除合同子表缓存数据
            removeDetails.clear();
            //返回给协同的合同子表接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同子表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

}
