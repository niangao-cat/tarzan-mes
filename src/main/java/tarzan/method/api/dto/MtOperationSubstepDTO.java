package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtOperationSubstepDTO implements Serializable {
    private static final long serialVersionUID = 430995105508713162L;

    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("工艺子步骤Id")
    private String operationSubstepId;
    @ApiModelProperty("工艺Id")
    private String operationId;
    @ApiModelProperty("子步骤Id")
    private String substepId;
    @ApiModelProperty("子步骤编码")
    private String substepName;
    @ApiModelProperty("子步骤描述")
    private String substepDescription;
    @ApiModelProperty("子步骤详细描述")
    private String substepLongDescription;

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getOperationSubstepId() {
        return operationSubstepId;
    }

    public void setOperationSubstepId(String operationSubstepId) {
        this.operationSubstepId = operationSubstepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getSubstepName() {
        return substepName;
    }

    public void setSubstepName(String substepName) {
        this.substepName = substepName;
    }

    public String getSubstepDescription() {
        return substepDescription;
    }

    public void setSubstepDescription(String substepDescription) {
        this.substepDescription = substepDescription;
    }

    public String getSubstepLongDescription() {
        return substepLongDescription;
    }

    public void setSubstepLongDescription(String substepLongDescription) {
        this.substepLongDescription = substepLongDescription;
    }
}
