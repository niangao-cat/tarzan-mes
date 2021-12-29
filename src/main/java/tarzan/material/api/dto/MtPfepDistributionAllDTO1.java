package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/11 19:37
 */
public class MtPfepDistributionAllDTO1 implements Serializable {
    private static final long serialVersionUID = -4387650209077634130L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty("主键，唯一标识")
    private String pfepDistributionId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为生产站点")
    private String materialSiteId;
    @ApiModelProperty(value = "限定类型为工作单元")
    private String organizationType;
    @ApiModelProperty(value = "工作单元ID")
    private String organizationId;
    @ApiModelProperty(value = "线边库位")
    private String locatorId;
    @ApiModelProperty(value = "物料在线边库位的容量")
    private Double locatorCapacity;
    @ApiModelProperty(value = "是否采用装配件生产速率（按WO补货模式和按顺序补货模式可以采取装配件街拍，其它模式只能采用组件节拍）")
    private String fromScheduleRateFlag;
    @ApiModelProperty(value = "物料消耗的速率单位，统一用per/h单位，计算总数时，要将时间换算成h再进行计算")
    private String materialConsumeRateUomId;
    @ApiModelProperty(value = "物料消耗的速率@")
    private Double materialConsumeRate;
    @ApiModelProperty(value = "安全库存a")
    private Double bufferInventory;
    @ApiModelProperty(value = "缓冲时间A,单位小时（h）")
    private Double bufferPeriod;
    @ApiModelProperty(value = "最小库存")
    private Double minInventory;
    @ApiModelProperty(value = "最大库存")
    private Double maxInventory;
    @ApiModelProperty(value = "配送包装数")
    private Double packQty;
    @ApiModelProperty(value = "配送数量是否为包装数的倍数")
    private String multiplesOfPackFlag;
    @ApiModelProperty(value = "关联配送库位")
    private String areaLocatorId;
    @ApiModelProperty(value = "关联配送路线，必输")
    private String areaId;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
    @ApiModelProperty(value = "分类集组织关系id")
    private String materialCategorySiteId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "生产站点ID/物料站点ID/分类集站点ID")
    private String siteId;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPfepDistributionId() {
        return pfepDistributionId;
    }

    public void setPfepDistributionId(String pfepDistributionId) {
        this.pfepDistributionId = pfepDistributionId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
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

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public Double getLocatorCapacity() {
        return locatorCapacity;
    }

    public void setLocatorCapacity(Double locatorCapacity) {
        this.locatorCapacity = locatorCapacity;
    }

    public String getFromScheduleRateFlag() {
        return fromScheduleRateFlag;
    }

    public void setFromScheduleRateFlag(String fromScheduleRateFlag) {
        this.fromScheduleRateFlag = fromScheduleRateFlag;
    }

    public String getMaterialConsumeRateUomId() {
        return materialConsumeRateUomId;
    }

    public void setMaterialConsumeRateUomId(String materialConsumeRateUomId) {
        this.materialConsumeRateUomId = materialConsumeRateUomId;
    }

    public Double getMaterialConsumeRate() {
        return materialConsumeRate;
    }

    public void setMaterialConsumeRate(Double materialConsumeRate) {
        this.materialConsumeRate = materialConsumeRate;
    }

    public Double getBufferInventory() {
        return bufferInventory;
    }

    public void setBufferInventory(Double bufferInventory) {
        this.bufferInventory = bufferInventory;
    }

    public Double getBufferPeriod() {
        return bufferPeriod;
    }

    public void setBufferPeriod(Double bufferPeriod) {
        this.bufferPeriod = bufferPeriod;
    }

    public Double getMinInventory() {
        return minInventory;
    }

    public void setMinInventory(Double minInventory) {
        this.minInventory = minInventory;
    }

    public Double getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(Double maxInventory) {
        this.maxInventory = maxInventory;
    }

    public Double getPackQty() {
        return packQty;
    }

    public void setPackQty(Double packQty) {
        this.packQty = packQty;
    }

    public String getMultiplesOfPackFlag() {
        return multiplesOfPackFlag;
    }

    public void setMultiplesOfPackFlag(String multiplesOfPackFlag) {
        this.multiplesOfPackFlag = multiplesOfPackFlag;
    }

    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

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

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
}
