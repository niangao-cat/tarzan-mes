package tarzan.modeling.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtModOrganizationDTO7 implements Serializable {
    private static final long serialVersionUID = -4360889361952895085L;
    @ApiModelProperty(value = "关系ID")
    @NotBlank
    private String relId;
    @ApiModelProperty(value = "类型")
    @NotBlank
    private String relType;
    @ApiModelProperty(value = "排序号")
    private Long sequence;

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
