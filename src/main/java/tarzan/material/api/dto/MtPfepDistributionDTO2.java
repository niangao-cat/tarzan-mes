package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/11 14:29
 */
public class MtPfepDistributionDTO2 implements Serializable {
    private static final long serialVersionUID = 7040877758344650142L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "生产站点ID/物料站点ID/分类集站点ID")
    private String siteId;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;

    @ApiModelProperty(value = "配送路线ID")
    private String areaId;

    @ApiModelProperty(value = "线边库位ID")
    private String locatorId;

    @ApiModelProperty(value = "配送库位ID")
    private String areaLocatorid;

    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;

    @ApiModelProperty(value = "线边库位容量")
    private double locatorCapacity;

    @ApiModelProperty(value = "最小库存")
    private double minInventory;

    @ApiModelProperty(value = "最大库存")
    private double maxInventory;

    @ApiModelProperty(value = "物料消耗速率(per/h)")
    private double materialConsumeRate;

    @ApiModelProperty(value = "配送包装数")
    private double packQty;

    @ApiModelProperty(value = "安全库存")
    private double bufferInventory;

    @ApiModelProperty(value = "缓冲时间(h)")
    private double bufferPeriod;

    @ApiModelProperty(value = "整包配送")
    private String multiplesOfPackFlag;

    @ApiModelProperty(value = "采用装配件生产速率")
    private String fromScheduleRateFlag;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

    @ApiModelProperty(value = "主键ID")
    private String pfepDistributionId;

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

    public String getAreaLocatorid() {
        return areaLocatorid;
    }

    public void setAreaLocatorid(String areaLocatorid) {
        this.areaLocatorid = areaLocatorid;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public double getLocatorCapacity() {
        return locatorCapacity;
    }

    public void setLocatorCapacity(double locatorCapacity) {
        this.locatorCapacity = locatorCapacity;
    }

    public double getMinInventory() {
        return minInventory;
    }

    public void setMinInventory(double minInventory) {
        this.minInventory = minInventory;
    }

    public double getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(double maxInventory) {
        this.maxInventory = maxInventory;
    }

    public double getMaterialConsumeRate() {
        return materialConsumeRate;
    }

    public void setMaterialConsumeRate(double materialConsumeRate) {
        this.materialConsumeRate = materialConsumeRate;
    }

    public double getPackQty() {
        return packQty;
    }

    public void setPackQty(double packQty) {
        this.packQty = packQty;
    }

    public double getBufferInventory() {
        return bufferInventory;
    }

    public void setBufferInventory(double bufferInventory) {
        this.bufferInventory = bufferInventory;
    }

    public double getBufferPeriod() {
        return bufferPeriod;
    }

    public void setBufferPeriod(double bufferPeriod) {
        this.bufferPeriod = bufferPeriod;
    }

    public String getMultiplesOfPackFlag() {
        return multiplesOfPackFlag;
    }

    public void setMultiplesOfPackFlag(String multiplesOfPackFlag) {
        this.multiplesOfPackFlag = multiplesOfPackFlag;
    }

    public String getFromScheduleRateFlag() {
        return fromScheduleRateFlag;
    }

    public void setFromScheduleRateFlag(String fromScheduleRateFlag) {
        this.fromScheduleRateFlag = fromScheduleRateFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getPfepDistributionId() {
        return pfepDistributionId;
    }

    public void setPfepDistributionId(String pfepDistributionId) {
        this.pfepDistributionId = pfepDistributionId;
    }
}
