package com.hundsun.boss.modules.forecast.thread.income;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hundsun.boss.modules.forecast.form.income.IncomeForecastForm;
import com.hundsun.boss.modules.forecast.service.income.IncomeForecastService;

/**
 * 异步收入预测
 */
public class IncomeForecastThread extends Thread {
    protected final Log logger = LogFactory.getLog(IncomeForecastThread.class);

    private IncomeForecastService incomeForecastService;
    private IncomeForecastForm incomeForecastForm = new IncomeForecastForm();

    public IncomeForecastThread() {
    }

    /**
     * 根据查询条件进行收入预测
     * 
     * @param form
     * @param incomeForecastService
     */
    public IncomeForecastThread(IncomeForecastForm form, IncomeForecastService incomeForecastService) {
        this.incomeForecastForm = form;
        this.incomeForecastService = incomeForecastService;
    }

    /**
     * 开始进行收入预测
     */
    public void run() {
        try {
            incomeForecastService.refresh(incomeForecastForm);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
