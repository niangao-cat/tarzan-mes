package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 20:08
 * @Description:
 */
public class MtModAreaPurchaseVO implements Serializable {
    private static final long serialVersionUID = -8611555950760855152L;

    private String areaId; // 区域ID
    private String insideFlag; // 是否厂内区域
    private String supplierId; // 厂外区域供应商
    private String supplierSiteId; // 厂外区域供应商地点

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getInsideFlag() {
        return insideFlag;
    }

    public void setInsideFlag(String insideFlag) {
        this.insideFlag = insideFlag;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }
}
