package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtOperationDTO3 implements Serializable {
    private static final long serialVersionUID = -4069805379590640926L;

    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("工艺Id")
    private String operationId;
    @ApiModelProperty("工艺类型")
    private String operationType;
    @ApiModelProperty("工艺短编码")
    private String operationName;

    @ApiModelProperty("工艺短描述")
    private String operationDesc;

    @ApiModelProperty("版本")
    private String revision;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }
}
