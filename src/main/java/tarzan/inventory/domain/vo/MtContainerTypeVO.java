package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @Author peng.yuan
 * @Date 2019/10/18 16:01
 */
public class MtContainerTypeVO implements Serializable {

    private static final long serialVersionUID = 2175692947398094513L;
    @ApiModelProperty("容器类型ID")
    private String containerTypeId;
    @ApiModelProperty("容器类型编码")
    private String containerTypeCode;
    @ApiModelProperty("容器类型描述")
    private String containerTypeDescription;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("包装等级")
    private String packingLevel;
    @ApiModelProperty("是否允许放入不同物料")
    private String mixedMaterialFlag;
    @ApiModelProperty("是否允许放入不同EO的物料")
    private String mixedEoFlag;
    @ApiModelProperty("是否允许放入不同WO的物料")
    private String mixedWoFlag;
    @ApiModelProperty("是否允许放入不同所有权的物料")
    private String mixedOwnerFlag;
    @ApiModelProperty("是否启用位置管理")
    private String locationEnabledFlag;

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

    public String getLocationEnabledFlag() {
        return locationEnabledFlag;
    }

    public void setLocationEnabledFlag(String locationEnabledFlag) {
        this.locationEnabledFlag = locationEnabledFlag;
    }

    @Override
    public String toString() {
        return "MtContainerTypeVO{" +
                "containerTypeId='" + containerTypeId + '\'' +
                ", containerTypeCode='" + containerTypeCode + '\'' +
                ", containerTypeDescription='" + containerTypeDescription + '\'' +
                ", enableFlag='" + enableFlag + '\'' +
                ", packingLevel='" + packingLevel + '\'' +
                ", mixedMaterialFlag='" + mixedMaterialFlag + '\'' +
                ", mixedEoFlag='" + mixedEoFlag + '\'' +
                ", mixedWoFlag='" + mixedWoFlag + '\'' +
                ", mixedOwnerFlag='" + mixedOwnerFlag + '\'' +
                ", locationEnabledFlag='" + locationEnabledFlag + '\'' +
                '}';
    }
}
