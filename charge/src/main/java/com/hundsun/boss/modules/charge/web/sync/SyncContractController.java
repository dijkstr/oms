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
import com.hundsun.boss.modules.charge.entity.sync.SyncAggContract;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.sync.SyncContdetailService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;

/**
 * 合同主表Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/sync/contract")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyncContractController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SyncContractService syncContractService;
    @Autowired
    private SyncContdetailService SyncContdetailService;
    @Autowired
    private OrderInfoService orderInfoService;

    @ModelAttribute
    public SyncContract get(@RequestParam(required = false) String con_id) {
        if (StringUtils.isNotBlank(con_id)) {
            return syncContractService.get(con_id);
        } else {
            return new SyncContract();
        }
    }

    //合同主表集合
    private List<SyncContract> syncContracts = new ArrayList<SyncContract>();
    //合同子表集合
    private List<SyncContdetail> syncDetails = new ArrayList<SyncContdetail>();

    /**
     * 清除协同合同主子表缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "cancel")
    public @ResponseBody Map cancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的接口的token信息
        result.put("token", token);
        try {
            //清除合同主表缓存数据
            syncContracts.clear();
            //清除合同子表数据
            syncDetails.clear();
            //返回给协同的接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 保存协同合同主子表数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "submit")
    public @ResponseBody Map submit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的接口的token信息
        result.put("token", token);

        try {
            for (int i = 0; i < syncContracts.size(); i++) {
                //获取数据库中对应的合同主表对象
                SyncContract syncContract = syncContractService.get(syncContracts.get(i).getCon_id());
                if (!CommonUtil.isNullorEmpty(syncContract)) {
                    //如果合同主表存在，更新为同步过来的对象
                    BeanUtils.copyProperties(syncContract, syncContracts.get(i));
                } else {
                    //合同主表对象在数据库不存在，直接赋值
                    syncContract = syncContracts.get(i);
                }
                //保存新的合同主表对象,由于级联，子表会删除
                syncContractService.save(syncContract);
                       
            }
            
            
            //-------子表数据删除后，将本次的表体数据全部插入--------------
            SyncContdetailService.saveAll(syncDetails);
            //-----------------------
            

            //返回给协同的接口的处理成功信息
            result.put("result", "success");
            
            //调用存储过程校验合同信息
            for(int k = 0; k < syncContracts.size(); k++){
                //校验合同信息
                syncContractService.validateContract(syncContracts.get(k).getContractid());
            }    
            
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 新增与编辑协同合同主子表数据
     * 
     * @param contracts
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "refresh")
    public @ResponseBody Map refresh(String contracts, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的合同token信息：" + token);
        try {
            //将协同推送的json字符串转成对象list
            List<SyncAggContract> contractslist = FormUtils.getListByJson(contracts, SyncAggContract.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(contractslist);
            logger.info(new Date() + "协同推送的合同信息：" + reslist);
            for (int i = 0; i < contractslist.size(); i++) {
                //将合同主表对象list添加到缓存对象中
                syncContracts.add(contractslist.get(i).getContract());
                //将合同子表对象添加到集合
                syncDetails.addAll(contractslist.get(i).getDetail());
            }
            //返回给协同的接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    private List<SyncContract> removeContracts = new ArrayList<SyncContract>();

    /**
     * 删除协同合同主表数据
     * 
     * @param contracts
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "remove")
    public @ResponseBody Map remove(String removecontracts, String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同主表接口的token信息
        result.put("token", token);
        logger.info(new Date() + "协同推送的合同主表token信息：" + token);
        try {
            //将协同推送的客户json字符串转成客户对象list
            List<SyncContract> contractslist = FormUtils.getListByJson(removecontracts, SyncContract.class);
            //把对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String reslist = objectMapper.writeValueAsString(contractslist);
            logger.info(new Date() + "协同推送的合同主表信息：" + reslist);
            //将合同主表对象list添加到缓存对象中
            removeContracts.addAll(contractslist);
            //返回给协同的合同主表接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同主表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 提交删除 合同主表数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "removesubmit")
    public @ResponseBody Map removesubmit(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同主表接口的token信息
        result.put("token", token);
        try {
            //存在标志
            boolean isexist=false;
            for (int i = 0; i < removeContracts.size(); i++) {
                //获取数据库中对应的合同主表对象
                SyncContract syncContract = syncContractService.get(removeContracts.get(i).getCon_id());
                if (!CommonUtil.isNullorEmpty(syncContract)) {
                    isexist=true;
                    //如果合同主表存在，删除合同主表
                    syncContractService.delete(syncContract.getCon_id());
                }
            }
            //返回给协同的合同主表接口的处理成功信息
            result.put("result", "success");
            if(isexist){
                //调用存储过程校验合同信息
                  //有合同主表数据删除，则调用校验存储过程全部校验
               syncContractService.validateContract(null);
              }
            
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同主表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

    /**
     * 清除协同合同主表缓存数据
     * 
     * @param token
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "removecancel")
    public @ResponseBody Map removecancel(String token, RedirectAttributes redirectAttributes) {
        Map result = new HashMap();
        //返回给协同的合同主表接口的token信息
        result.put("token", token);
        try {
            //清除合同主表缓存数据
            removeContracts.clear();
            //返回给协同的合同主表接口的处理成功信息
            result.put("result", "success");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //返回给协同的合同主表接口的处理失败信息
            result.put("result", "fail");
            result.put("reason", e.getMessage());
        }
        return result;
    }

}
