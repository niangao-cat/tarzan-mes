package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionVO18
 * @description
 * @date 2019年12月18日 14:37
 */
public class MtInstructionVO18 implements Serializable {
    private static final long serialVersionUID = 1938541240741452838L;

    @ApiModelProperty(value = "指令ID")
    private String instructionId;
    @ApiModelProperty(value = "来源库位ID")
    private String fromLocatorId;
    @ApiModelProperty(value = "目标库位ID")
    private String toLocatorId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }
}
