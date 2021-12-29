package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtPfepInventoryCategoryVO
 * @description
 * @date 2019年09月18日 20:17
 */
public class MtPfepInventoryCategoryVO implements Serializable {
    private static final long serialVersionUID = 8526028476534148834L;

    private String pfepInventoryCategoryId;
    private String identifyType;
    private String identifyId;
    private String stockLocatorId;
    private Double maxStockQty;
    private Double minStockQty;
    private Double packageWidth;
    private String weightUomId;
    private Double packageLength;
    private Double packageWeight;
    private Double packageHeight;
    private String packageSizeUomId;
    private String materialCategorySiteId;
    private String organizationType;
    private String organizationId;
    private String issuedLocatorId;
    private String completionLocatorId;
    private String enableFlag;
    private String materialCategoryId;
    private String siteId;

    public String getPfepInventoryCategoryId() {
        return pfepInventoryCategoryId;
    }

    public void setPfepInventoryCategoryId(String pfepInventoryCategoryId) {
        this.pfepInventoryCategoryId = pfepInventoryCategoryId;
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

    public Double getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(Double packageWidth) {
        this.packageWidth = packageWidth;
    }

    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    public Double getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(Double packageLength) {
        this.packageLength = packageLength;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
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

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
