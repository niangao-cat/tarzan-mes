package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 区域维护-新增 使用DTO
 * 
 * @author benjamin
 */
public class MtModAreaPurchaseDTO implements Serializable {
    private static final long serialVersionUID = -7650329942425786940L;

    @ApiModelProperty("主键ID，标示唯一一条记录")
    private String areaPurchaseId;

    @ApiModelProperty(value = "是否厂内区域")
    private String insideFlag;

    @ApiModelProperty(value = "厂外区域供应商Id")
    private String supplierId;

    @ApiModelProperty(value = "厂外区域供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "厂外区域供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "厂外区域供应商地点Id")
    private String supplierSiteId;

    @ApiModelProperty(value = "厂外区域供应商地点编码")
    private String supplierSiteCode;

    @ApiModelProperty(value = "厂外区域供应商地点名称")
    private String supplierSiteName;

    public String getAreaPurchaseId() {
        return areaPurchaseId;
    }

    public void setAreaPurchaseId(String areaPurchaseId) {
        this.areaPurchaseId = areaPurchaseId;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    public String getSupplierSiteName() {
        return supplierSiteName;
    }

    public void setSupplierSiteName(String supplierSiteName) {
        this.supplierSiteName = supplierSiteName;
    }

    @Override
    public String toString() {
        return "MtModAreaPurchaseDTO{" + "areaPurchaseId='" + areaPurchaseId + '\'' + ", insideFlag='" + insideFlag
                        + '\'' + ", supplierId='" + supplierId + '\'' + ", supplierCode='" + supplierCode + '\''
                        + ", supplierName='" + supplierName + '\'' + ", supplierSiteId='" + supplierSiteId + '\''
                        + ", supplierSiteCode='" + supplierSiteCode + '\'' + ", supplierSiteName='" + supplierSiteName
                        + '\'' + '}';
    }
}
