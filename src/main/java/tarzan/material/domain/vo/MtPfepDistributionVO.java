package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtPfepDistributionVO
 * @description
 * @date 2020年02月04日 15:12
 */
public class MtPfepDistributionVO implements Serializable {
    private static final long serialVersionUID = -4367828650461199759L;


    @ApiModelProperty(value = "物料配送属性ID")
    private String pfepDistributionId;
    @ApiModelProperty(value = "物料站点主键")
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型")
    private String organizationType;
    @ApiModelProperty(value = "工作单元ID")
    private String organizationId;
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
    private String areaLocatorId;
    @ApiModelProperty(value = "关联配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "有效性标识")
    private String enableFlag;

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
}
