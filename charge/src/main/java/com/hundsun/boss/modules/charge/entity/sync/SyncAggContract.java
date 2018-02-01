package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 合同主子表集合
 * @author feigq
 *
 */
public class SyncAggContract implements Serializable{
    private static final long serialVersionUID = 1L;
    private SyncContract contract;
    private ArrayList<SyncContdetail> detail;
    public SyncContract getContract() {
        return contract;
    }
    public void setContract(SyncContract contract) {
        this.contract = contract;
    }
    public ArrayList<SyncContdetail> getDetail() {
        return detail;
    }
    public void setDetail(ArrayList<SyncContdetail> detail) {
        this.detail = detail;
    }
    
    

}
