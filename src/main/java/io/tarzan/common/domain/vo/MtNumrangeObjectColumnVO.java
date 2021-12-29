package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年8月14日 下午4:58:49
 *
 */
public class MtNumrangeObjectColumnVO implements Serializable {

    private static final long serialVersionUID = -3118271971196690046L;

    @ApiModelProperty("编码对象列ID")
    private String objectColumnId;

    @ApiModelProperty(value = "编码对象ID", required = true)
    private String objectId;

    @ApiModelProperty(value = "列参数名", required = true)
    private String objectColumnCode;

    @ApiModelProperty(value = "列名称")
    private String objectColumnName;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;

    @ApiModelProperty(value = "类型组")
    private String typeGroup;

    @ApiModelProperty(value = "服务包")
    private String module;

    @ApiModelProperty(value = "服务包（描述）")
    private String moduleDesc;

    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectColumnCode() {
        return objectColumnCode;
    }

    public void setObjectColumnCode(String objectColumnCode) {
        this.objectColumnCode = objectColumnCode;
    }

    public String getObjectColumnName() {
        return objectColumnName;
    }

    public void setObjectColumnName(String objectColumnName) {
        this.objectColumnName = objectColumnName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }
}
