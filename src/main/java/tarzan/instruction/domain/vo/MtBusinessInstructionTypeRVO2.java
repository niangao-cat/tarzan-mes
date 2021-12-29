package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtBusinessInstructionTypeRVO2
 * @description
 * @date 2020年01月03日 14:08
 */
public class MtBusinessInstructionTypeRVO2 implements Serializable {
    private static final long serialVersionUID = -6621336032503060428L;

    @ApiModelProperty(value = "关系ID")
    private String relationId;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "业务类型描述")
    private String businessTypeDesc;
    @ApiModelProperty(value = "指令移动类型")
    private String instructionType;
    @ApiModelProperty(value = "指令移动类型描述")
    private String instructionTypeDesc;
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

    public String getBusinessTypeDesc() {
        return businessTypeDesc;
    }

    public void setBusinessTypeDesc(String businessTypeDesc) {
        this.businessTypeDesc = businessTypeDesc;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getInstructionTypeDesc() {
        return instructionTypeDesc;
    }

    public void setInstructionTypeDesc(String instructionTypeDesc) {
        this.instructionTypeDesc = instructionTypeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
