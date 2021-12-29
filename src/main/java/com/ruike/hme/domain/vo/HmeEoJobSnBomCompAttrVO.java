package com.ruike.hme.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/21 14:30
 * @Description:
 */
public class HmeEoJobSnBomCompAttrVO implements Serializable {
    private static final long serialVersionUID = -6026215753944676814L;

    private String bomComponentId;
    private Boolean virtualComponentFlag;
    private String bomReserveNum;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Boolean isVirtualComponentFlag() {
        return virtualComponentFlag;
    }

    public void setVirtualComponentFlag(boolean virtualComponentFlag) {
        this.virtualComponentFlag = virtualComponentFlag;
    }

    public String getBomReserveNum() {
        return bomReserveNum;
    }

    public void setBomReserveNum(String bomReserveNum) {
        this.bomReserveNum = bomReserveNum;
    }
}
