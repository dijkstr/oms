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
import com.hundsun.boss.modules.charge.entity.receipt.ChargeReceipt;
import com.hundsun.boss.modules.charge.entity.sync.SyncReceive;
import com.hundsun.boss.modules.charge.service.receipt.ChargeReceiptService;
import com.hundsun.boss.modules.charge.service.sync.SyncReceiveService;

/**
 * 合同到款Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/receive")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncReceiveController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncReceiveService syncReceiveService;
    @Autowired
    private ChargeReceiptService chargeReceiptService;

    @ModelAttribute
    public SyncReceive get(@RequestParam(required = false) String receiveid) {
        if (StringUtils.isNotBlank(receiveid)) {
            return syncReceiveService.get(receiveid);
        } else {
            return new SyncReceive();
        }
    }

    private List<SyncReceive> syncReceives = new ArrayList<SyncReceive>();

    /**
     * 清除到款缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回到款接口的token信息
        result.put("token", token);
        try {
            //清除到款缓存数据
            syncReceives.clear();
            //返回到款接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回到款接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 保存到款数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回到款接口的token信息
        result.put("token", token);
        try {
            for (int i = 0; i < syncReceives.size(); i++) {
                //-----到款备份处理---------
                //获取数据库中对应的到款备份对象
                SyncReceive syncReceive = syncReceiveService.get(syncReceives.get(i).getReceiveid());
                if (!CommonUtil.isNullorEmpty(syncReceive)) {
                    //如果存在，更新为同步过来的对象
//                    BeanUtils.copyProperties(syncReceive, syncReceives.get(i));
                    //更新对象
                    syncReceiveService.update(syncReceives.get(i));
                } else {
                    //对象在数据库不存在，直接赋值
                    syncReceive = syncReceives.get(i);
                    //保存新的对象
                    syncReceiveService.save(syncReceive);
                }
               
                //----------------------
                
                //----到款业务表处理-------
                //获取数据库中对应的到款业务对象
                ChargeReceipt chargeReceipt=chargeReceiptService.get(syncReceives.get(i).getReceiveid());
                //转化成业务对象 
                if (!CommonUtil.isNullorEmpty(chargeReceipt)) {
                    ChargeReceipt receipt=new  ChargeReceipt();
                    receipt.setReceiveid(chargeReceipt.getReceiveid());
                    receipt.setBankreceipt_serialno(syncReceives.get(i).getVbillcode());
                    receipt.setDepartment(syncReceives.get(i).getCompany());
                    receipt.setContract_id(syncReceives.get(i).getContractid());
                    receipt.setEx_product_id(syncReceives.get(i).getProductid());                   
                    receipt.setProduct_name(syncReceives.get(i).getProductname());
                    receipt.setBankreceipt_amount(syncReceives.get(i).getReceivemny());
                    receipt.setBankreceipt_month(syncReceives.get(i).getDbilldate());
                    receipt.setBankreceipt_date(syncReceives.get(i).getTs());                   
                    //如果存在，更新为同步过来的对象
//                    BeanUtils.copyProperties(chargeReceipt, receipt);
                    chargeReceiptService.update(receipt);
                } else {
                    //对象在数据库不存在，直接赋值
                    ChargeReceipt receipt=new  ChargeReceipt();
                    receipt.setReceiveid(syncReceives.get(i).getReceiveid());
                    receipt.setBankreceipt_serialno(syncReceives.get(i).getVbillcode());
                    receipt.setDepartment(syncReceives.get(i).getCompany());
                    receipt.setContract_id(syncReceives.get(i).getContractid());
                    receipt.setEx_product_id(syncReceives.get(i).getProductid());                   
                    receipt.setProduct_name(syncReceives.get(i).getProductname());
                    receipt.setBankreceipt_amount(syncReceives.get(i).getReceivemny());
                    receipt.setBankreceipt_month(syncReceives.get(i).getDbilldate());
                    receipt.setBankreceipt_date(syncReceives.get(i).getTs());
                    
                    chargeReceipt=receipt;
                    //保存到款业务表对象
                    chargeReceiptService.save(chargeReceipt);
                }
               
                //--------------   
            }
            //返回到款接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给到款接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 新增与编辑到款数据
     * 
     * @param receives
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String receives, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
      //返回给接口的token信息
        result.put("token", token);
        logger.info(new Date()+"协同推送的到款token信息："+token);
        try {
            //将协同推送的json字符串转成对象list
            List<SyncReceive> receiveslist = FormUtils.getListByJson(receives, SyncReceive.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist=objectMapper.writeValueAsString(receiveslist);
            logger.info(new Date()+"协同推送的到款信息："+reslist);
            //将对象list添加到缓存对象中
            syncReceives.addAll(receiveslist);
            //返回给接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
          //返回给接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

}
