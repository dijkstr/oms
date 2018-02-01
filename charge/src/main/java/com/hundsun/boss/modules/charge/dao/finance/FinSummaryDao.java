package com.hundsun.boss.modules.charge.dao.finance;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;

/**
 * 财务汇总表DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface FinSummaryDao {
    /**
     * 财务汇总列表
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> queryFinSummaryList(FinSummaryForm req);

    /**
     * 财务汇总表明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<FinSummaryForm> getFinSummaryDetailList(FinSummaryForm req);

    /**
     * 导出财务汇总表明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> exportDetailList(FinSummaryForm req);

    /**
     * 导出财务汇总表最新明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> exportLastDetail(FinSummaryForm req);

    /**
     * 导出财务汇总表产品级明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> exportProductDetail(FinSummaryForm req);

    /**
     * 导出财务汇总表合同级明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> exportContractDetail(FinSummaryForm req);

    /**
     * 导出财务汇总表公司级明细
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> exportBUDetail(FinSummaryForm req);

    /**
     * 确认收入
     * 
     * @param req
     */
    public void insertIncomeInterface(Map map);

    /**
     * 财务收入接口列表
     * 
     * @param req .
     * @return 返回.
     */
    public List<Map> queryFinanceIncomeList(FinSummaryForm req);

    /**
     * 获取技术服务费用收入预测结果
     * 
     * @param contract_ids
     * @return
     */
    public List<IncomeForecastDataBean> getFinsummaryForForecast(String contract_ids);

}
