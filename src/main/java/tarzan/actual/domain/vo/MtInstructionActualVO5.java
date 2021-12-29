package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionActualVO5
 * @description
 * @date 2020年01月09日 16:37
 */
public class MtInstructionActualVO5 implements Serializable {
    private static final long serialVersionUID = -6065518286519828300L;

    @ApiModelProperty(value = "指令ID")
    private String instructionId;
    @ApiModelProperty(value = "指令实绩ID")
    private String actualId;
    @ApiModelProperty(value = "指令实绩历史ID")
    private String actualHisId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getActualHisId() {
        return actualHisId;
    }

    public void setActualHisId(String actualHisId) {
        this.actualHisId = actualHisId;
    }
}
