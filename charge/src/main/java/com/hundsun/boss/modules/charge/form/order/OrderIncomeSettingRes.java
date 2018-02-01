package com.hundsun.boss.modules.charge.form.order;

import java.util.List;

/**
 * 订单收入设置对象
 * 
 */
public class OrderIncomeSettingRes extends OrderInfoForm {

    /**
     * 收入设置列表.
     */
    private List<OrderIncomeSettingForm> listOrderIncomeSetting;

    public List<OrderIncomeSettingForm> getListOrderIncomeSetting() {
        return listOrderIncomeSetting;
    }

    public void setListOrderIncomeSetting(List<OrderIncomeSettingForm> listOrderIncomeSetting) {
        this.listOrderIncomeSetting = listOrderIncomeSetting;
    }

}
