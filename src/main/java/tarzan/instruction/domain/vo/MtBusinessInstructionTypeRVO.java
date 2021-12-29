package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtBusinessInstructionTypeRVO
 * @description
 * @date 2020年01月03日 10:20
 */
public class MtBusinessInstructionTypeRVO implements Serializable {
    private static final long serialVersionUID = -1529080629254233030L;

    @ApiModelProperty(value = "关系ID")
    private String relationId;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "指令移动类型")
    private String instructionType;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
