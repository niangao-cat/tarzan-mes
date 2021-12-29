package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNcValidOperDTO implements Serializable {
    private static final long serialVersionUID = -7994118527983022486L;

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private String ncValidOperId;
    @ApiModelProperty(value = "不良代码或不良代码组", required = true)
    @NotBlank
    private String ncObjectId;
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    private String ncObjectType;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    private String operationId;
    @ApiModelProperty("工艺名称")
    private String operationName;
    @ApiModelProperty("工艺描述")
    private String operationDesc;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "处置组", required = true)
    @NotBlank
    private String dispositionGroupId;
    @ApiModelProperty("处置组")
    private String dispositionGroup;
    @ApiModelProperty("处置组描述")
    private String dispositionGroupDesc;

    public String getNcValidOperId() {
        return ncValidOperId;
    }

    public void setNcValidOperId(String ncValidOperId) {
        this.ncValidOperId = ncValidOperId;
    }

    public String getNcObjectId() {
        return ncObjectId;
    }

    public void setNcObjectId(String ncObjectId) {
        this.ncObjectId = ncObjectId;
    }

    public String getNcObjectType() {
        return ncObjectType;
    }

    public void setNcObjectType(String ncObjectType) {
        this.ncObjectType = ncObjectType;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public String getDispositionGroup() {
        return dispositionGroup;
    }

    public void setDispositionGroup(String dispositionGroup) {
        this.dispositionGroup = dispositionGroup;
    }

    public String getDispositionGroupDesc() {
        return dispositionGroupDesc;
    }

    public void setDispositionGroupDesc(String dispositionGroupDesc) {
        this.dispositionGroupDesc = dispositionGroupDesc;
    }
}
