package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/23 10:08
 * @Description:
 */
public class MtPfepInventoryVO3 implements Serializable {
    private static final long serialVersionUID = 7489929035808098975L;

    private String materialId; // 物料ID
    private String siteId; // 站点ID
    private String organizationType; // 组织类型
    private String organizationId; // 组织ID
    private String identifyType; // 标识类型
    private String identifyId;
    private String stockLocatorId;
    private Double packageLength;
    private Double packageWidth;
    private Double packageHeight;
    private String packageSizeUomId;
    private Double packageWeight;
    private String weightUomId;
    private Double maxStockQty;
    private Double minStockQty;
    private String issuedLocatorId;
    private String completionLocatorId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public String getStockLocatorId() {
        return stockLocatorId;
    }

    public void setStockLocatorId(String stockLocatorId) {
        this.stockLocatorId = stockLocatorId;
    }

    public Double getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(Double packageLength) {
        this.packageLength = packageLength;
    }

    public Double getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(Double packageWidth) {
        this.packageWidth = packageWidth;
    }

    public Double getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(Double packageHeight) {
        this.packageHeight = packageHeight;
    }

    public String getPackageSizeUomId() {
        return packageSizeUomId;
    }

    public void setPackageSizeUomId(String packageSizeUomId) {
        this.packageSizeUomId = packageSizeUomId;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    public Double getMaxStockQty() {
        return maxStockQty;
    }

    public void setMaxStockQty(Double maxStockQty) {
        this.maxStockQty = maxStockQty;
    }

    public Double getMinStockQty() {
        return minStockQty;
    }

    public void setMinStockQty(Double minStockQty) {
        this.minStockQty = minStockQty;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }
}
