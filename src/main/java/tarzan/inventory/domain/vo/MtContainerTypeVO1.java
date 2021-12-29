package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/18 16:01
 */
public class MtContainerTypeVO1 implements Serializable {

    private static final long serialVersionUID = 7670399547932642976L;
    @ApiModelProperty("容器类型")
    private String containerTypeId;
    @ApiModelProperty("容器类型编码")
    private String containerTypeCode;
    @ApiModelProperty("容器类型描述")
    private String containerTypeDescription;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("包装等级")
    private String packingLevel;
    @ApiModelProperty("最大装入数量")
    private Double capacityQty;
    @ApiModelProperty("是否允许放入不同物料")
    private String mixedMaterialFlag;
    @ApiModelProperty("是否允许放入不同EO的物料")
    private String mixedEoFlag;
    @ApiModelProperty("是否允许放入不同WO的物料")
    private String mixedWoFlag;
    @ApiModelProperty("是否允许放入不同所有权的物料")
    private String mixedOwnerFlag;
    @ApiModelProperty("长度")
    private Double length;
    @ApiModelProperty("宽度")
    private Double width;
    @ApiModelProperty("高度")
    private Double height;
    @ApiModelProperty("尺寸单位id")
    private String sizeUomId;
    @ApiModelProperty("尺寸单位编码")
    private String sizeUomCode;
    @ApiModelProperty("尺寸单位名称")
    private String sizeUomName;
    @ApiModelProperty("重量")
    private Double weight;
    @ApiModelProperty("最大装入重量")
    private Double maxLoadWeight;
    @ApiModelProperty("重量单位ID")
    private String weightUomId;
    @ApiModelProperty("重量单位编码")
    private String weightUomCode;
    @ApiModelProperty("重量单位名称")
    private String weightUomName;
    @ApiModelProperty("是否启用位置管理")
    private String locationEnabledFlag;
    @ApiModelProperty("总行数")
    private Long locationRow;
    @ApiModelProperty("总列数")
    private Long locationColume;

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getContainerTypeCode() {
        return containerTypeCode;
    }

    public void setContainerTypeCode(String containerTypeCode) {
        this.containerTypeCode = containerTypeCode;
    }

    public String getContainerTypeDescription() {
        return containerTypeDescription;
    }

    public void setContainerTypeDescription(String containerTypeDescription) {
        this.containerTypeDescription = containerTypeDescription;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getPackingLevel() {
        return packingLevel;
    }

    public void setPackingLevel(String packingLevel) {
        this.packingLevel = packingLevel;
    }

    public Double getCapacityQty() {
        return capacityQty;
    }

    public void setCapacityQty(Double capacityQty) {
        this.capacityQty = capacityQty;
    }

    public String getMixedMaterialFlag() {
        return mixedMaterialFlag;
    }

    public void setMixedMaterialFlag(String mixedMaterialFlag) {
        this.mixedMaterialFlag = mixedMaterialFlag;
    }

    public String getMixedEoFlag() {
        return mixedEoFlag;
    }

    public void setMixedEoFlag(String mixedEoFlag) {
        this.mixedEoFlag = mixedEoFlag;
    }

    public String getMixedWoFlag() {
        return mixedWoFlag;
    }

    public void setMixedWoFlag(String mixedWoFlag) {
        this.mixedWoFlag = mixedWoFlag;
    }

    public String getMixedOwnerFlag() {
        return mixedOwnerFlag;
    }

    public void setMixedOwnerFlag(String mixedOwnerFlag) {
        this.mixedOwnerFlag = mixedOwnerFlag;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getSizeUomName() {
        return sizeUomName;
    }

    public void setSizeUomName(String sizeUomName) {
        this.sizeUomName = sizeUomName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
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

    public String getWeightUomName() {
        return weightUomName;
    }

    public void setWeightUomName(String weightUomName) {
        this.weightUomName = weightUomName;
    }

    public String getLocationEnabledFlag() {
        return locationEnabledFlag;
    }

    public void setLocationEnabledFlag(String locationEnabledFlag) {
        this.locationEnabledFlag = locationEnabledFlag;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColume() {
        return locationColume;
    }

    public void setLocationColume(Long locationColume) {
        this.locationColume = locationColume;
    }


}
