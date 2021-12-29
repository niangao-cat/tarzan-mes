package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/13 22:29
 */
public class MtPfepDistributionDTO6 implements Serializable {
    private static final long serialVersionUID = 6999289080953760551L;

    @ApiModelProperty(value = "主键ID")
    private String pfepDistributionId;

    @ApiModelProperty(value = "组织id")
    private String siteId;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "组织名称")
    private String siteName;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;

    @ApiModelProperty(value = "物料类别编码")
    private String materialCategoryCode;

    @ApiModelProperty(value = "配送路线ID")
    private String areaId;

    @ApiModelProperty(value = "配送路线编码")
    private String areaCode;

    @ApiModelProperty(value = "线边库位ID")
    private String locatorId;

    @ApiModelProperty(value = "线边库位编码")
    private String locatorCode;

    @ApiModelProperty(value = "配送库位id")
    private String areaLocatorId;

    @ApiModelProperty(value = "配送库位编码")
    private String areaLocatorCode;

    @ApiModelProperty(value = "工作单元ID")
    private String organizationId;

    @ApiModelProperty(value = "工作单元编码")
    private String organizationCode;

    @ApiModelProperty(value = "线边库位容量")
    private Double locatorCapacity;

    @ApiModelProperty(value = "最小库存")
    private Double minInventory;

    @ApiModelProperty(value = "最大库存")
    private Double maxInventory;

    @ApiModelProperty(value = "物料消耗速率(per/h)")
    private Double materialConsumeRate;

    @ApiModelProperty(value = "配送包装数")
    private Double packQty;

    @ApiModelProperty(value = "安全库存")
    private Double bufferInventory;

    @ApiModelProperty(value = "缓冲时间(h)")
    private Double bufferPeriod;

    @ApiModelProperty(value = "整包配送")
    private String multiplesOfPackFlag;

    @ApiModelProperty(value = "采用装配件生产速率")
    private String fromScheduleRateFlag;

    @ApiModelProperty(value = "生效标记")
    private String enableFlag;


    public String getPfepDistributionId() {
        return pfepDistributionId;
    }

    public void setPfepDistributionId(String pfepDistributionId) {
        this.pfepDistributionId = pfepDistributionId;
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

    public String getMaterialCategoryCode() {
        return materialCategoryCode;
    }

    public void setMaterialCategoryCode(String materialCategoryCode) {
        this.materialCategoryCode = materialCategoryCode;
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

    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    public String getAreaLocatorCode() {
        return areaLocatorCode;
    }

    public void setAreaLocatorCode(String areaLocatorCode) {
        this.areaLocatorCode = areaLocatorCode;
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

    public Double getLocatorCapacity() {
        return locatorCapacity;
    }

    public void setLocatorCapacity(Double locatorCapacity) {
        this.locatorCapacity = locatorCapacity;
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

    public Double getMaterialConsumeRate() {
        return materialConsumeRate;
    }

    public void setMaterialConsumeRate(Double materialConsumeRate) {
        this.materialConsumeRate = materialConsumeRate;
    }

    public Double getPackQty() {
        return packQty;
    }

    public void setPackQty(Double packQty) {
        this.packQty = packQty;
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
}
