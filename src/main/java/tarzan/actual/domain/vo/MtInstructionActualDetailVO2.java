package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionActualDetailVO2
 * @description
 * @date 2020年01月08日 15:40
 */
public class MtInstructionActualDetailVO2 implements Serializable {
    private static final long serialVersionUID = -2956650475502975411L;

    @ApiModelProperty("指令ID")
    private String instructionId;

    @ApiModelProperty("实绩id")
    private String actualDetailId;

    @ApiModelProperty(value = "汇总id", required = true)
    private String actualId;

    @ApiModelProperty(value = "物料批ID", required = true)
    private String materialLotId;

    @ApiModelProperty(value = "单位", required = true)
    private String uomId;

    @ApiModelProperty(value = "实绩数量", required = true)
    private Double actualQty;

    @ApiModelProperty(value = "器具id")
    private String containerId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getActualDetailId() {
        return actualDetailId;
    }

    public void setActualDetailId(String actualDetailId) {
        this.actualDetailId = actualDetailId;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
