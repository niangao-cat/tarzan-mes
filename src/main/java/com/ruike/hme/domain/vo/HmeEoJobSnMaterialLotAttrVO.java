package com.ruike.hme.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/21 16:52
 * @Description:
 */
public class HmeEoJobSnMaterialLotAttrVO implements Serializable {
    private static final long serialVersionUID = 5752311140551137422L;

    private String materialLotId;
    private String soNum;
    private String soLineNum;
    private String productionVersion;
    private String reworkFlag;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getSoNum() {
        return soNum;
    }

    public void setSoNum(String soNum) {
        this.soNum = soNum;
    }

    public String getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
    }

    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
    }

    public String getReworkFlag() {
        return reworkFlag;
    }

    public void setReworkFlag(String reworkFlag) {
        this.reworkFlag = reworkFlag;
    }
}
