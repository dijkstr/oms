package com.hundsun.boss.modules.charge.dao.receipt;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.receipt.ChargeReceipt;
import com.hundsun.boss.modules.charge.form.receipt.ChargeReceiptForm;

/**
 * 计费到款业务DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface ChargeReceiptDao {
    /**
     * 插入到款业务表
     * 
     * @param syncReceive
     */
    public void save(ChargeReceipt chargeReceipt);

    /**
     * 获取到款业务表对象
     * 
     * @param receiveid
     * @return
     */
    public ChargeReceipt get(String receiveid);

    /**
     * 更新到款业务表对象
     * 
     * @param receiveid
     * @return
     */
    public void update(ChargeReceipt chargeReceipt);

    /**
     * 获取条件的查询的到款
     * 
     * @param chargeReceipt
     * @return
     */

    public List queryChargeReceiptList(ChargeReceiptForm chargeReceiptForm);

    /**
     * 到款导出清单
     * 
     * @param chargeReceiptForm
     * @return
     */
    public List exportChargeReceipt(ChargeReceiptForm chargeReceiptForm);

    /**
     * 根据协同合同号获取累计到款
     * 
     * @param contract_id
     * @return
     */
    public String getReceiptByContractId(String contract_id);
}
