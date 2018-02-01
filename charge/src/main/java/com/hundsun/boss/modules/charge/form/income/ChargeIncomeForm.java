package com.hundsun.boss.modules.charge.form.income;

import java.io.Serializable;

/**
 * 财务收入接口查询form
 * @author feigq
 *
 */
public class ChargeIncomeForm implements Serializable{
    private static final long serialVersionUID = 1L;
    //财务请求的收入年月
    private String incomemonths;
    //财务请求的数据偏移量
    private int offset;
    //财务请求的数据分页数
    private int pagesize;
   
    public String getIncomemonths() {
        return incomemonths;
    }
    public void setIncomemonths(String incomemonths) {
        this.incomemonths = incomemonths;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public int getPagesize() {
        return pagesize;
    }
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
    
}
