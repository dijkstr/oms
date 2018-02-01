package com.hundsun.boss.modules.charge.dao.bill;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargePeriodTemp;
import com.hundsun.boss.modules.charge.form.bill.ChargeProductMonthlyCharge;
import com.hundsun.boss.modules.charge.form.bill.ChargeTransDetail;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.bill.OrderAdvPaymentForm;
import com.hundsun.boss.modules.charge.form.bill.OrderCombineForm;
import com.hundsun.boss.modules.charge.form.bill.OrderInfoForm;
import com.hundsun.boss.modules.charge.form.bill.OrderModelForm;
import com.hundsun.boss.modules.charge.form.bill.OrderPriceForm;
import com.hundsun.boss.modules.charge.form.bill.OrderProductForm;

/**
 * 账单DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface ChargeBillDao {
    /**
     * 获取符合条件的协同产品信息.
     * 
     * @param 参数.
     * @return 返回值.
     */
    public List queryChargeBillList(ChargeBillSearchForm chargeBillSearchForm);

    /**
     * 出账(计算技术服务费以及各种年度累计用于显示与控制画面)
     * 
     * @param datas
     * @throws Exception
     */
    public void accountReceitp(Map mCriteria);

    /**
     * 出账(计算合同级累计费用)
     * 
     * @param datas
     * @throws Exception
     */
    public void accountBill(Map mCriteria);

    /**
     * 产品级明细
     * 
     * @param datas
     * @throws Exception
     */
    public void financeDetail(Map mCriteria);

    /**
     * 产品级
     * 
     * @param datas
     * @throws Exception
     */
    public void countYearlyProductIncome(Map mCriteria);

    /**
     * 合同级
     * 
     * @param datas
     * @throws Exception
     */
    public void countYearlyContractIncome(Map mCriteria);

    /**
     * 获取出账用的账单id
     * 
     * @param queryChargeBillReq
     * @throws Exception
     */
    public List<Map> getBillIdForAccountReceipt(ChargeBillSearchForm chargeBillSearchForm);

    /**
     * 根据统一接口临时表计费产品id，协同合同号，协同客户号，进行错单判断，并将错误信息插入错单表
     * 
     * @param mCriteria
     * @return
     * @throws Exception
     */
    public void genChargeWrong(Map mCriteria);

    /**
     * 根据错单表内容，删除统一接口表的错误记录(没做) 对合同内不同期间(月份)的数据进行拆分，插入计费子账单期间临时表
     * 
     * @param mCriteria
     */
    public void genChargePeriodTemp(Map mCriteria);

    /**
     * 计算各个区间的实际终了时间（可能会发生年度末月拆分）
     * 
     * @param mCriteria
     */
    public void genPeriodEndTime(Map mCriteria);

    /**
     * 删除本期间内出过的账单
     * 
     * @param mCriteria
     */
    public void chargeDeleteOldRecord(Map mCriteria);

    /**
     * 按批次号，合同号，期间的开始、终了日，按照现有合同内容拍快照
     * 
     * @param mCriteria
     */
    public void genChargeSnapshot(Map mCriteria);

    /**
     * 进行计费，生成产品级计费信息,组合级计费信息，合同级计费信息
     * 
     * @param mCriteria
     */
    public void genChargeFee(Map mCriteria);

    /**
     * 存储统一接口传来的业务数据
     * 
     * @param unifyData
     */
    public void insertUnifyDatas(Map unifyData);

    /**
     * 如果数据中心没有数据，而存在有效账单，生成空账单
     * 
     * @param emptyBillData
     */
    public void insertEmptyBillDatas(Map emptyBillData);

    /**
     * 获取账单详情.
     * 
     * @param req 请求参数.
     * @return 返回某条账单的详情信息.
     */
    public OrderInfoForm getChargeDetail(ChargeBillSearchForm req);

    /**
     * 获取计费合同详情.
     * 
     * @param req 请求参数.
     * @return 返回某条账单的详情信息.
     */
    public ChargeOrderInfo getChargeOrderInfo(ChargeOrderInfo chargeOrderInfo);

    /**
     * 查询计费信息.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public ChargeBillInfo queryChargeBillInfo(ChargeBillInfo req);

    /**
     * 根据合同号账单组合列表.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public List<OrderCombineForm> queryChargeCombine(OrderCombineForm req);

    /**
     * 查询账单组合明细列表.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public List<OrderProductForm> queryChargeCombineProduct(OrderProductForm req);

    /**
     * 查询交易明细列表.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public List<ChargeTransDetail> queryChargeTransDetail(ChargeTransDetail req);

    /**
     * 查询预付信息.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public List<OrderAdvPaymentForm> querySSOrderPaymentCycle(OrderAdvPaymentForm req);

    /**
     * 根据计费模式代码查询计费定价.
     * 
     * @param req 请求参数.
     * @return 返回影响的行数.
     */
    public List<OrderPriceForm> querySSChargePrice(OrderPriceForm req);

    /**
     * 查询计费模式.
     * 
     * @param req 请求参数.
     * @return 返回影响的行数.
     */
    public OrderModelForm getSSChargeModel(OrderModelForm req);

    /**
     * 更新发送状态
     * 
     * @param map bill_id
     */
    public void updateSendStatus(Map map);

    /**
     * 更新审核状态
     * 
     * @param map bill_id
     */
    public int checkBill(Map map);

    /**
     * 删除账单
     * 
     * @param map bill_id
     */
    public void deleteBill(Map map);

    /**
     * 查询部门信息.
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public ChargeBillForm getBillAccount(ChargeBillSearchForm req);

    /**
     * 合同计费列表查询
     * 
     * @param req
     * @return
     */
    public List<Map> queryBillChargingList(ChargeBillSearchForm req);

    /**
     * 插入临时表
     * 
     * @param mCriteria
     */
    public void insertChargePeriodTemp(List<ChargePeriodTemp> list);

    /**
     * 插入拆分临时表
     * 
     * @param mCriteria
     */
    public void insertSplitTemp(Map map);

    /**
     * 合同是否计费
     */
    public Integer countChargeOrder(String string);

    /**
     * 获取下载部门
     * 
     * @param downloadForm
     * @return
     */
    public List<Map> getOrderSource(DownloadForm downloadForm);

    /**
     * 获取账单id
     * 
     * @param downloadForm
     * @return
     */
    public List<String> getBillIds(DownloadForm downloadForm);

    /**
     * 获取账单id
     * 
     * @param downloadForm
     * @return
     */
    public Map getMaxBillInfo(Map params);

    /**
     * 获取每月计费金额合计
     * 
     * @param downloadForm
     * @return
     */
    public List<ChargeProductMonthlyCharge> getChargeProductMonthlyCharge(ChargeTransDetail req);

    /**
     * 补足统一接口日保底数据
     * 
     * @param mCriteria
     */
    public void genInsertDayMinConsumeUnifyInterface(Map mCriteria);
    
    /**
     * 查询是否超保底
     * 
     * @param req 请求参数.
     * @return 返回账单列表
     */
    public ChargeBillInfo getBillConsume(ChargeBillSearchForm req);
}
