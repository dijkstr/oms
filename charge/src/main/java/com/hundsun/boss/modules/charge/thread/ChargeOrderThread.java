package com.hundsun.boss.modules.charge.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;

/**
 * 异步计费
 * 
 * @author whl
 * @version 创建时间：2016年8月9日 上午11:01:13
 */
public class ChargeOrderThread extends Thread {
    protected final Log logger = LogFactory.getLog(ChargeOrderThread.class);

    private ChargeBillService chargeFinService;
    private ChargeBillSearchForm chargeBillSearchForm = new ChargeBillSearchForm();

    public ChargeOrderThread() {
    }

    public ChargeOrderThread(ChargeBillSearchForm chargeBillSearchForm, ChargeBillService chargeFinService) {
        this.chargeBillSearchForm = chargeBillSearchForm;
        this.chargeFinService = chargeFinService;
    }

    // 计费线程
    public void run() {
        try {
            // 计费逻辑
            chargeFinService.doCharge(chargeBillSearchForm);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
