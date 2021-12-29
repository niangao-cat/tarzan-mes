package tarzan.method.domain.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtBomSubstituteGroupVO5 implements Serializable {

    private static final long serialVersionUID = -7995563734981406362L;
    
    @ApiModelProperty(value = "主键")
    private String bomSubstituteGroupId;
    
    @ApiModelProperty(value = "组件行主键",required = true)
    @NotBlank
    private String bomComponentId;
    
    @ApiModelProperty(value = "替代组编码",required = true)
    @NotBlank
    private String substituteGroup;
    
    @ApiModelProperty(value = "替代策略",required = true)
    @NotBlank
    private String substitutePolicy;
    
    @ApiModelProperty(value = "替代策略描述")
    private String substitutePolicyDesc;
    
    @ApiModelProperty(value = "是否启用",required = true)
    @NotBlank
    private String enableFlag;

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getSubstituteGroup() {
        return substituteGroup;
    }

    public void setSubstituteGroup(String substituteGroup) {
        this.substituteGroup = substituteGroup;
    }

    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }

    public String getSubstitutePolicyDesc() {
        return substitutePolicyDesc;
    }

    public void setSubstitutePolicyDesc(String substitutePolicyDesc) {
        this.substitutePolicyDesc = substitutePolicyDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
