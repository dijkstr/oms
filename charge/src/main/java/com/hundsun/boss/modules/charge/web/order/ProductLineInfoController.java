package com.hundsun.boss.modules.charge.web.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.modules.charge.entity.order.ProductLineInfo;
import com.hundsun.boss.modules.charge.service.order.ProductLineInfoService;

/**
 * 计费合同Controller
 */
@Controller
@RequestMapping(value = "${frontPath}/charge/order/productLine")
public class ProductLineInfoController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductLineInfoService productLineInfoService;

    /**
     * 接收保存数据
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "saveData")
    public @ResponseBody Map<String, Object> saveData(String token,String cusBusList) {
        Map<String, Object> data = new HashMap<String, Object>();
        if(!Global.getConfig("productLineToken").equals(token)){
            logger.error(new Date()+"推送的token信息错误");
            data.put("result", "fail");
            data.put("message", "接收保存数据失败！");
            return data;
        }
        try{
            List<ProductLineInfo> productLineInfoList = new ArrayList<ProductLineInfo>();
            //将推送的json字符串转成产品上线对象list
            cusBusList = HtmlUtils.htmlUnescape(cusBusList);
            logger.info(cusBusList);
            List<Map<String, String>> mapList = FormUtils.getListByJson(cusBusList, HashMap.class);
            if(!CommonUtil.isNullorEmpty(mapList)){
                for(int i=0;i<mapList.size();i++){
                    Map<String, String> map = mapList.get(i);
                    if(map.get("contractNo").contains("\\")){
                        String[] contractIdArray =  map.get("contractNo").split("\\\\");
                        for(int j=0;j<contractIdArray.length;j++){
                            String contractId = contractIdArray[j].trim();
                            ProductLineInfo productLine = new ProductLineInfo();
                            productLine.setContract_id(contractId);
                            productLine.setCustomer_id(map.get("customerNo"));
                            productLine.setProduct_id(map.get("businessSystemCode"));
                            productLine.setProduct_line_date(map.get("onLineDate"));
                            productLineInfoList.add(productLine);
                        }
                    }else{
                        ProductLineInfo productLine = new ProductLineInfo();
                        productLine.setContract_id(map.get("contractNo"));
                        productLine.setCustomer_id(map.get("customerNo"));
                        productLine.setProduct_id(map.get("businessSystemCode"));
                        productLine.setProduct_line_date(map.get("onLineDate"));
                        productLineInfoList.add(productLine); 
                    }
                }
            }
            productLineInfoService.productLineSave(productLineInfoList);
            data.put("result", "success");
            data.put("message", "接收保存数据成功！");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            data.put("result", "fail");
            data.put("message", "接收保存数据失败！");
        }
        return data;
    }
}
