package com.hundsun.boss.modules.forecast.bean;

import java.util.Date;

/**
 * 根据时间序列计算内容的临时类
 */
public class PeriodSequence {
    private Date yearlyBeginDate;
    private Date curBeginDate;
    private Date curEndDate;
    private Date yearlyEndDate;

    public Date getYearlyBeginDate() {
        return yearlyBeginDate;
    }

    public void setYearlyBeginDate(Date yearlyBeginDate) {
        this.yearlyBeginDate = yearlyBeginDate;
    }

    public Date getCurBeginDate() {
        return curBeginDate;
    }

    public void setCurBeginDate(Date curBeginDate) {
        this.curBeginDate = curBeginDate;
    }

    public Date getCurEndDate() {
        return curEndDate;
    }

    public void setCurEndDate(Date curEndDate) {
        this.curEndDate = curEndDate;
    }

    public Date getYearlyEndDate() {
        return yearlyEndDate;
    }

    public void setYearlyEndDate(Date yearlyEndDate) {
        this.yearlyEndDate = yearlyEndDate;
    }

}
