package com.hundsun.boss.modules.charge.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.IdGen;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.charge.dao.order.ProductLineInfoDao;
import com.hundsun.boss.modules.charge.entity.order.ProductLineInfo;
import com.hundsun.boss.modules.charge.form.order.ProductLineInfoForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同Service
 */
@Component
@Transactional(readOnly = false)
public class ProductLineInfoService extends BaseService {

    @Autowired
    private ProductLineInfoDao productLineInfoDao;
    
    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;
    
    public ProductLineInfo get(String id) {
        return productLineInfoDao.get(id);
    }

    public Page<ProductLineInfo> find(Page<ProductLineInfo> page, ProductLineInfoForm productLineInfo) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = productLineInfoDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(productLineInfo.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + productLineInfo.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(productLineInfo.getCustomer_id())) {
            dc.add(Restrictions.like("customer_id", "%" + productLineInfo.getCustomer_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(productLineInfo.getProduct_id())) {
            dc.add(Restrictions.like("product_id", "%" + productLineInfo.getProduct_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(productLineInfo.getOnline_status())) {
            dc.add(Restrictions.eq("online_status",productLineInfo.getOnline_status()));
        }
        
        if (!CommonUtil.isNullorEmpty(productLineInfo.getHs_customername())) {
            dc.createAlias("syncCustomer", "syncCustomer");
            if (!CommonUtil.isNullorEmpty(productLineInfo.getHs_customername())) {
                dc.add(Restrictions.like("syncCustomer.chinesename", "%" + productLineInfo.getHs_customername() + "%"));
            }
        }
        dc.createAlias("orderInfo", "orderInfo");
        dc.createAlias("orderInfo.office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        
        dc.addOrder(Order.desc("contract_id"));
        return productLineInfoDao.find(page, dc);
    }


    public void save(ProductLineInfo productLineInfo) {
        productLineInfoDao.save(productLineInfo);
    }
    
    
    public void delete(ProductLineInfo productLineInfo) {
        productLineInfoDao.delete(productLineInfo);
    }
    
    /**
     * 接收数据的处理
     * @param productLineInfoList
     */
    public void productLineSave(List<ProductLineInfo> productLineInfoList){
        if(!CommonUtil.isNullorEmpty(productLineInfoList)){
            for(int i=0;i<productLineInfoList.size();i++){
                Map<String, String> map = new HashMap<String, String>();
                map.put("contractid", productLineInfoList.get(i).getContract_id());
                map.put("customerid", productLineInfoList.get(i).getCustomer_id());
                map.put("productid", productLineInfoList.get(i).getProduct_id());
                map.put("accountidentity", "10931");//计费填报标识-未上报
                map.put("reporttype_id", "10572");//计费起点上报类型-上线日
                int count = orderInfoMyBatisDao.queryProductCount(map);
                //存在合同产品满足计费填报标识是未上报/上报类型是上线日的数据
                if(count>0){
                    //接收的数据如果已经存在，则删除已存在的数据再插入新的数据
                    orderInfoMyBatisDao.deleteProductLine(productLineInfoList.get(i));
                    productLineInfoList.get(i).setId(IdGen.uuid());
                    productLineInfoList.get(i).setOnline_status("0");
                    orderInfoMyBatisDao.insertProductLine(productLineInfoList.get(i));
                }
            }
        }
    }
   
    @SuppressWarnings("rawtypes")
    public List<Map> queryProductLineList(ProductLineInfoForm form){
        
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        
        return orderInfoMyBatisDao.queryProductLineList(form);
    }
}
