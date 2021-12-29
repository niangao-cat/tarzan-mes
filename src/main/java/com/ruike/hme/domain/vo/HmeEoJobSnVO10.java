package com.ruike.hme.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/21 12:45
 * @Description:
 */
public class HmeEoJobSnVO10 implements Serializable {
    private static final long serialVersionUID = -6922529611123909717L;

    private String materialId;

    private String materialType;

    private boolean isBackFlush;

    private boolean isIssuedFlag;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public boolean isBackFlush() {
        return isBackFlush;
    }

    public void setBackFlush(boolean backFlush) {
        this.isBackFlush = backFlush;
    }

    public boolean isIssuedFlag() {
        return isIssuedFlag;
    }

    public void setIssuedFlag(boolean issuedFlag) {
        this.isIssuedFlag = issuedFlag;
    }
}
