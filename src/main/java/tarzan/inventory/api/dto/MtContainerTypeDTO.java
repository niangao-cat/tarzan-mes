package tarzan.inventory.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtContainerTypeDTO implements Serializable {
    private static final long serialVersionUID = -6939089608600982055L;

    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该容器类型")
    private String containerTypeId;

    @ApiModelProperty("容器类型的编码CODE")
    private String containerTypeCode;

    @ApiModelProperty("容器类型的描述")
    private String containerTypeDescription;

    @ApiModelProperty("容器类型的有效状态")
    private String enableFlag;

    @ApiModelProperty("包装等级")
    private String packingLevel;

    @ApiModelProperty("最多允许装入的数量")
    private Double capacityQty;

    @ApiModelProperty("是否允许放入不同的物料")
    private String mixedMaterialFlag;

    @ApiModelProperty("是否允许放入不同执行作业的产品")
    private String mixedEoFlag;

    @ApiModelProperty("是否允许放入不同生产指令的产品或在制品")
    private String mixedWoFlag;

    @ApiModelProperty("容器是否允许放入所有权的产品")
    private String mixedOwnerFlag;

    @ApiModelProperty("容器的长度值")
    private Double length;

    @ApiModelProperty("容器的宽度值")
    private Double width;

    @ApiModelProperty("容器的高度值")
    private Double height;

    @ApiModelProperty("容器的重量值")
    private Double weight;

    @ApiModelProperty("容器长宽高值的数值单位")
    private String sizeUomId;

    @ApiModelProperty("容器最大可承载重量值")
    private Double maxLoadWeight;

    @ApiModelProperty("容器重量、承载重量值的单位")
    private String weightUomId;

    @ApiModelProperty("需要启用容器的位置")
    private String locationEnabledFlag;

    @ApiModelProperty("启用容器位置时定义该类容器的行数")
    private Long locationRow;

    @ApiModelProperty("启用容器位置时定义该类容器的列数")
    private Long locationColumn;

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
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

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    @Override
    public String toString() {
        return "MtContainerTypeDTO{" +
                "containerTypeId='" + containerTypeId + '\'' +
                ", containerTypeCode='" + containerTypeCode + '\'' +
                ", containerTypeDescription='" + containerTypeDescription + '\'' +
                ", enableFlag='" + enableFlag + '\'' +
                ", packingLevel='" + packingLevel + '\'' +
                ", capacityQty=" + capacityQty +
                ", mixedMaterialFlag='" + mixedMaterialFlag + '\'' +
                ", mixedEoFlag='" + mixedEoFlag + '\'' +
                ", mixedWoFlag='" + mixedWoFlag + '\'' +
                ", mixedOwnerFlag='" + mixedOwnerFlag + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", sizeUomId='" + sizeUomId + '\'' +
                ", maxLoadWeight=" + maxLoadWeight +
                ", weightUomId='" + weightUomId + '\'' +
                ", locationEnabledFlag='" + locationEnabledFlag + '\'' +
                ", locationRow=" + locationRow +
                ", locationColumn=" + locationColumn +
                '}';
    }
}
