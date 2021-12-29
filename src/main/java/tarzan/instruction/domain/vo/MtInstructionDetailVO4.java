package tarzan.instruction.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtInstructionDetailVO4
 * @description
 * @date 2020年01月08日 10:45
 */
public class MtInstructionDetailVO4 implements Serializable {
    private static final long serialVersionUID = 6265816099383174102L;

    @ApiModelProperty(value = "指令明细行ID")
    private String instructionDetailId;
    @ApiModelProperty(value = "指令ID")
    private String instructionId;
    @ApiModelProperty(value = "物料批ID")
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
