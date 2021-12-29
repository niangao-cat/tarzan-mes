package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionDetailVO3
 * @description
 * @date 2020年01月08日 10:31
 */
public class MtInstructionDetailVO3 implements Serializable {
    private static final long serialVersionUID = 8677197361842785786L;

    @ApiModelProperty(value = "指令明细行ID")
    private String instructionDetailId;
    @ApiModelProperty(value = "指令ID")
    private String instructionId;
    @ApiModelProperty(value = "物料批ID列表")
    private String materialLotId;

    public String getInstructionDetailId() {
        return instructionDetailId;
    }

    public void setInstructionDetailId(String instructionDetailId) {
        this.instructionDetailId = instructionDetailId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }
}
