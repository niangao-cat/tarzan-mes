package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtInstructionDetailVO1 implements Serializable {

    private static final long serialVersionUID = 147547561268856195L;
    @ApiModelProperty("指令明细ID,唯一标识")
    private String instructionDetailId;
    @ApiModelProperty(value = "指令ID", required = true)
    private String instructionId;
    @ApiModelProperty(value = "物料批ID", required = true)
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
