package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;


public class MtPfepInventoryDTO2 implements Serializable {

    private static final long serialVersionUID = 1917140899892253153L;

    @ApiModelProperty(value="主键ID")
    private String kid;
    @ApiModelProperty(value="物料ID(当保存物料PFEP时必输)")
    private String materialId;
    @ApiModelProperty(value = "物料类别ID(当保存物料类别PFEP时必输)")
    private String materialCategoryId;
    @ApiModelProperty(value = "站点ID",required = true)
    private String siteId;
    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    private String organizationType;
    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    private String organizationId;
    @ApiModelProperty(value = "存储标识类型，如无标识、容器标识、物料批标识等")
    private String identifyType;
    @ApiModelProperty(value = "标识模板，关联物料默认使用的标识模板")
    private String identifyId;
    @ApiModelProperty(value = "默认存储库位")
    private String stockLocatorId;
    @ApiModelProperty(value = "存储包装长")
    private Double packageLength;
    @ApiModelProperty(value = "存储包装宽")
    private Double packageWidth;
    @ApiModelProperty(value = "存储包装高")
    private Double packageHeight;
    @ApiModelProperty(value = "包装尺寸单位，如MMCMM等，与单位维护保持一致")
    private String packageSizeUomId;
    @ApiModelProperty(value = "存储包装重量")
    private Double packageWeight;
    @ApiModelProperty(value = "重量单位，如KG/G/T等")
    private String weightUomId;
    @ApiModelProperty(value = "最大存储库存")
    private Double maxStockQty;
    @ApiModelProperty(value = "最小存储库存")
    private Double minStockQty;
    @ApiModelProperty(value = "默认发料库位")
    private String issuedLocatorId;
    @ApiModelProperty(value = "默认完工库位")
    private String completionLocatorId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty("扩展属性集合")
    private List<MtExtendAttrDTO3> mtPfepInventoryAttrs;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtExtendAttrDTO3> getMtPfepInventoryAttrs() {
        return mtPfepInventoryAttrs;
    }

    public void setMtPfepInventoryAttrs(List<MtExtendAttrDTO3> mtPfepInventoryAttrs) {
        this.mtPfepInventoryAttrs = mtPfepInventoryAttrs;
    }
}
