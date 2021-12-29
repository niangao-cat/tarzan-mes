package tarzan.material.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;

public class MtPfepInventoryVO4 implements Serializable  {
    private static final long serialVersionUID = -2770418294777028961L;

    @ApiModelProperty("pfep主键ID")
    private String kid;
    @ApiModelProperty("pfep类型")
    private String keyType;
    @ApiModelProperty("物料站点ID")
    private String materialSiteId;
    @ApiModelProperty("物料类型站点ID")
    private String materialCategorySiteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料类别ID")
    private String materialCategoryId;
    @ApiModelProperty("类别编码")
    private String categoryCode;
    @ApiModelProperty("类别描述")
    private String categoryDesc;
    @ApiModelProperty("类别集描述")
    private String categorySetDesc;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("区域ID")
    private String areaId;
    @ApiModelProperty("区域编码")
    private String areaCode;
    @ApiModelProperty("区域名称")
    private String areaName;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("产线名称")
    private String prodLineName;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元名称")
    private String workcellName;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("库位名称")
    private String locatorName;

    @ApiModelProperty("存储类型标识")
    private String identifyType;
    @ApiModelProperty("标识模板ID")
    private String identifyId;
    @ApiModelProperty("存储包装长")
    private Double packageLength;
    @ApiModelProperty("存储包装宽")
    private Double packageWidth;
    @ApiModelProperty("存储包装高")
    private Double packageHeight;
    @ApiModelProperty("包装尺寸单位ID")
    private String packageSizeUomId;
    @ApiModelProperty("包装尺寸单位编码")
    private String packageSizeUomCode;
    @ApiModelProperty("存储包装重量")
    private Double packageWeight;
    @ApiModelProperty("重量单位ID")
    private String weightUomId;
    @ApiModelProperty("重量单位")
    private String weightUomCode;
    @ApiModelProperty("最大存储库存")
    private Double maxStockQty;
    @ApiModelProperty("最小存储库存")
    private Double minStockQty;
    @ApiModelProperty("默认存储库位ID")
    private String stockLocatorId;
    @ApiModelProperty("默认存储库位编码")
    private String stockLocatorCode;
    @ApiModelProperty("默认存储库位名称")
    private String stockLocatorName;
    @ApiModelProperty("默认发料库位ID")
    private String issuedLocatorId;
    @ApiModelProperty("默认发料库位编码")
    private String issuedLocatorCode;
    @ApiModelProperty("默认发料库位名称")
    private String issuedLocatorName;
    @ApiModelProperty("默认完工库位ID")
    private String completionLocatorId;
    @ApiModelProperty("默认完工库位编码")
    private String completionLocatorCode;
    @ApiModelProperty("默认完工库位名称")
    private String completionLocatorName;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("组织类型")
    private String organizationType;
    @ApiModelProperty("组织ID")
    private String organizationId;
    @ApiModelProperty("组织编码")
    private String organizationCode;
    @ApiModelProperty("组织描述")
    private String organizationDesc;

    private List<MtExtendAttrDTO> mtExtendAttrDTOList;
    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategorySetDesc() {
        return categorySetDesc;
    }

    public void setCategorySetDesc(String categorySetDesc) {
        this.categorySetDesc = categorySetDesc;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
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

    public String getPackageSizeUomCode() {
        return packageSizeUomCode;
    }

    public void setPackageSizeUomCode(String packageSizeUomCode) {
        this.packageSizeUomCode = packageSizeUomCode;
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

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
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

    public String getStockLocatorId() {
        return stockLocatorId;
    }

    public void setStockLocatorId(String stockLocatorId) {
        this.stockLocatorId = stockLocatorId;
    }

    public String getStockLocatorCode() {
        return stockLocatorCode;
    }

    public void setStockLocatorCode(String stockLocatorCode) {
        this.stockLocatorCode = stockLocatorCode;
    }

    public String getStockLocatorName() {
        return stockLocatorName;
    }

    public void setStockLocatorName(String stockLocatorName) {
        this.stockLocatorName = stockLocatorName;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getIssuedLocatorCode() {
        return issuedLocatorCode;
    }

    public void setIssuedLocatorCode(String issuedLocatorCode) {
        this.issuedLocatorCode = issuedLocatorCode;
    }

    public String getIssuedLocatorName() {
        return issuedLocatorName;
    }

    public void setIssuedLocatorName(String issuedLocatorName) {
        this.issuedLocatorName = issuedLocatorName;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }

    public String getCompletionLocatorCode() {
        return completionLocatorCode;
    }

    public void setCompletionLocatorCode(String completionLocatorCode) {
        this.completionLocatorCode = completionLocatorCode;
    }

    public String getCompletionLocatorName() {
        return completionLocatorName;
    }

    public void setCompletionLocatorName(String completionLocatorName) {
        this.completionLocatorName = completionLocatorName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public List<MtExtendAttrDTO> getMtExtendAttrDTOList() {
        return mtExtendAttrDTOList;
    }

    public void setMtExtendAttrDTOList(List<MtExtendAttrDTO> mtExtendAttrDTOList) {
        this.mtExtendAttrDTOList = mtExtendAttrDTOList;
    }
}
