package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;

public class SyncDeleteDetail implements Serializable{
    private static final long serialVersionUID = 1L;
    //子表32位id
    private String detailid;
    public String getDetailid() {
        return detailid;
    }
    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }
    

}
