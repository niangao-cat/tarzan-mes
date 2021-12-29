package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPfepDistributionVO2
 *
 * @author: {xieyiyang}
 * @date: 2020/2/4 17:36
 * @description:
 */
public class MtPfepDistributionVO2 implements Serializable {
    private static final long serialVersionUID = 7424075501298631386L;

    @ApiModelProperty(value = "物料站点ID")
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型")
    private String organizationType;
    @ApiModelProperty(value = "组织ID")
    private String organizationId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "线边库位ID")
    private String locatorId;
    @ApiModelProperty(value = "线边库位容量")
    private Double locatorCapacity;
    @ApiModelProperty(value = "是否采用装配件生产速率")
    private String fromScheduleRateFlag;
    @ApiModelProperty(value = "物料消耗的速率单位ID")
    private String materialConsumeRateUomId;
    @ApiModelProperty(value = "物料消耗的速率")
    private Double materialConsumeRate;
    @ApiModelProperty(value = "安全库存")
    private Double bufferInventory;
    @ApiModelProperty(value = "缓冲时间")
    private Double bufferPeriod;
    @ApiModelProperty(value = "最小库存")
    private Double minInventory;
    @ApiModelProperty(value = "最大库存")
    private Double maxInventory;
    @ApiModelProperty(value = "最小包装数")
    private Double packQty;
    @ApiModelProperty(value = "是否必须整包配送")
    private String multiplesOfPackFlag;
    @ApiModelProperty(value = "关联配送库位ID")
    private String routeLocatorId;
    @ApiModelProperty(value = "配送模式")
    private String distributionMode;
    @ApiModelProperty(value = "是否启用拉动时段")
    private String pullTimeIntervalFlag;
    @ApiModelProperty(value = "配送周期T")
    private Double distributionCycle;
    @ApiModelProperty(value = "指令业务类型")
    private String businessType;
    @ApiModelProperty(value = "指令是否按照EO做拆分标识")
    private String instructCreatedByEo;
    @ApiModelProperty(value = "来源库位")
    private String sourceLocatorId;
    @ApiModelProperty(value = "优先级")
    private Long sequence;
    @ApiModelProperty(value = "触发到送达周期")
    private Double pullToArrive;

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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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

    public String getRouteLocatorId() {
        return routeLocatorId;
    }

    public void setRouteLocatorId(String routeLocatorId) {
        this.routeLocatorId = routeLocatorId;
    }

    public String getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(String distributionMode) {
        this.distributionMode = distributionMode;
    }

    public String getPullTimeIntervalFlag() {
        return pullTimeIntervalFlag;
    }

    public void setPullTimeIntervalFlag(String pullTimeIntervalFlag) {
        this.pullTimeIntervalFlag = pullTimeIntervalFlag;
    }

    public Double getDistributionCycle() {
        return distributionCycle;
    }

    public void setDistributionCycle(Double distributionCycle) {
        this.distributionCycle = distributionCycle;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getInstructCreatedByEo() {
        return instructCreatedByEo;
    }

    public void setInstructCreatedByEo(String instructCreatedByEo) {
        this.instructCreatedByEo = instructCreatedByEo;
    }

    public String getSourceLocatorId() {
        return sourceLocatorId;
    }

    public void setSourceLocatorId(String sourceLocatorId) {
        this.sourceLocatorId = sourceLocatorId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getPullToArrive() {
        return pullToArrive;
    }

    public void setPullToArrive(Double pullToArrive) {
        this.pullToArrive = pullToArrive;
    }
}
