package com.hundsun.boss.modules.charge.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;

/**
 * 异步出账
 * 
 * @author whl
 * @version 创建时间：2016年8月12日 上午9:14:54
 */
public class GenerateBillThread extends Thread {
    protected final Log logger = LogFactory.getLog(GenerateBillThread.class);

    private ChargeBillService chargeFinService;
    
    ChargeBillSearchForm chargeBillSearchForm = new ChargeBillSearchForm();

    public GenerateBillThread() {
    }

    public GenerateBillThread(ChargeBillSearchForm chargeBillSearchForm, ChargeBillService chargeFinService) {
        this.chargeBillSearchForm = chargeBillSearchForm;
        this.chargeFinService = chargeFinService;
    }

    // 计费线程
    public void run() {
        try {
            // 计费逻辑
            chargeFinService.accountReceitp(chargeBillSearchForm);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
