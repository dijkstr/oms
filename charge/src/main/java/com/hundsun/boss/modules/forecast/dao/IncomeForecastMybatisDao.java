package com.hundsun.boss.modules.forecast.dao;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.common.utils.echart.CommonChartData;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;
import com.hundsun.boss.modules.forecast.form.income.IncomeForecastForm;

/**
 * 收入预测查询结果DAO接口
 */
@SuppressWarnings("rawtypes")
@MyBatisDao
public interface IncomeForecastMybatisDao {

    /**
     * 获取通用报表查询数据
     * 
     * @param form
     * @return
     */
    public List<CommonChartData> getForecastResult(IncomeForecastForm form);

    /**
     * 获取待预测的合同列表
     * 
     * @param incomeForecastForm
     * @return
     */
    public List<String> getRefreshContractIds(IncomeForecastForm incomeForecastForm);

    /**
     * 保存收入预测结果
     * 
     * @param serviceChargeForecastResults
     */
    public void saveBatchForeCastResult(List<IncomeForecastDataBean> serviceChargeForecastResults);

    /**
     * 删除之前的预测结果
     * 
     * @param incomeForecastForm
     */
    public void deletePreviousForecastResultsByCondition(IncomeForecastForm incomeForecastForm);

    /**
     * 删除之前的预测结果
     * 
     * @param contract_ids
     */
    public void deletePreviousForecastResultsByContractIds(String contract_ids);
    
    /**
     * 删除之前的预测结果
     * 
     * @param contract_ids
     */
    public void deletePreviousForecastResultsByContractId(String contract_id);

    /**
     * 查询收入预测明细
     * 
     * @param incomeForecastForm
     * @return
     */
    public List<Map> getIncomeForecastDetail(IncomeForecastForm incomeForecastForm);
}
