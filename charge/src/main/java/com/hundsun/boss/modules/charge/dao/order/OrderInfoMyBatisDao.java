package com.hundsun.boss.modules.charge.dao.order;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.order.ImportContracts;
import com.hundsun.boss.modules.charge.entity.order.ProductLineInfo;
import com.hundsun.boss.modules.charge.form.audit.OrderAuditSearchForm;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.order.OrderIncomeSettingForm;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.charge.form.order.ProductLineInfoForm;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;
import com.hundsun.boss.modules.forecast.bean.MinMaxConsumeDataBean;

/**
 * 账单DAO接口
 */
@SuppressWarnings("rawtypes")
@MyBatisDao
public interface OrderInfoMyBatisDao {
    /**
     * 查看订单产品拆分信息
     * 
     * @param req
     * @return
     */
    public List<OrderIncomeSettingForm> queryOrderIncomeSetting(OrderIncomeSettingForm req);

    /**
     * 添加订单产品拆分数据
     * 
     * @param req
     * @return
     */
    public Integer updateOrderIncomeSetting(OrderIncomeSettingForm req);

    /**
     * 修改订单收入状态信息.
     * 
     * @param req 请求参数.
     * @return 返回.
     */
    public Integer updateOrderStatus(OrderInfoForm req);

    /**
     * 调设置订单收入的存储过程
     * 
     * @param req
     */
    public void setOrderIncome(OrderIncomeSettingForm req);

    /**
     * 查看收入设置信息
     * 
     * @param req
     * @return
     */
    public List<Map> queryIncomeSettingList(OrderInfoForm req);

    /**
     * 查看合同列表信息
     * 
     * @param req
     * @return
     */
    public List<Map> queryContractList(OrderInfoForm req);

    /**
     * 根据合同查询产品
     */
    public List<Map> queryProdList(String string);

    /**
     * 根据合同查询产品
     */
    public List<Map> queryCombineList(String string);

    /**
     * 查询合同到期提醒信息
     * 
     * @return
     */
    public List<Map> queryOrderWarningList();

    /**
     * 云毅合同到期提醒信息
     * 
     * @return
     */
    public List<Map> queryOrderWarningListOnYunYi();

    /**
     * 获取产品个数
     * 
     * @param map
     * @return
     */
    public int queryProductCount(Map map);

    /**
     * 查询上线产品列表
     * 
     * @param form
     * @return
     */
    public List<Map> queryProductLineList(ProductLineInfoForm form);

    /**
     * 获取合同联系人列表
     * 
     * @param map
     * @return
     */
    public List<Map> queryOrderRelationList(Map map);

    /**
     * 按部门获取销售
     * 
     * @return
     * @throws Exception
     */
    public List<Map> getSalers(DownloadForm downloadForm);

    /**
     * 删除产品线数据
     */
    public void deleteProductLine(ProductLineInfo productLineInfo);

    /**
     * 删除产品线数据
     */
    public void insertProductLine(ProductLineInfo productLineInfo);

    /**
     * 计费专用合同列表信息
     * 
     * @param req
     * @return
     */
    public List<Map> chargeContractList(OrderInfoForm req);

    /**
     * 查询审核合同列表
     * 
     * @param form
     * @return
     */
    public List<Map> queryAuditOrderInfoList(OrderAuditSearchForm form);

    /**
     * 查询当年度的保底累计
     * 
     * @param contract_id
     * @param nowDate
     * @return
     */
    public Double queryOrderMinCharge(Map params);

    /**
     * 获取固定费用收入类型的内容
     * 
     * @param contract_id
     * @return
     */
    public List<IncomeForecastDataBean> queryFixedIncomeSettingList(String contract_id);

    /**
     * 获取保底封顶值
     * 
     * @param bean
     * @return
     */
    public List<MinMaxConsumeDataBean> queryMinMaxConsumeList(IncomeForecastDataBean bean);

    /**
     * 从库中查询出一条记录,用于下载模板使用(续签合同)
     * 
     * @param imExcelDemo
     * @return
     */
    public ImportContracts queryImportContractData();

    /**
     * 删除已有合同
     * 
     * @param importContracts
     */
    public void deleteImportContractsData(ImportContracts importContracts);

    /**
     * 插入新合同
     * 
     * @param importContracts
     */
    public void insertImportContractsData(List<ImportContracts> importContractLists);
}
