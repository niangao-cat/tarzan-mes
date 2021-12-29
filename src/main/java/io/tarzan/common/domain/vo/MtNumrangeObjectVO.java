package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年8月14日 下午4:58:49
 *
 */
public class MtNumrangeObjectVO implements Serializable {
    
    private static final long serialVersionUID = 1899296610617733096L;

    @ApiModelProperty("编码对象ID")
    private String objectId;
    
    @ApiModelProperty(value = "编码对象编码")
    private String objectCode;
    
    @ApiModelProperty(value = "编码对象名称")
    private String objectName;
    
    @ApiModelProperty(value = "编码对象描述")
    private String description;
    
    @ApiModelProperty(value = "类型属性名")
    private String typeColumn;
    
    @ApiModelProperty(value = "类型组")
    private String typeGroup;
    
    @ApiModelProperty(value = "服务包")
    private String module;
    
    @ApiModelProperty(value = "服务包（描述）")
    private String moduleDesc;
    
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeColumn() {
        return typeColumn;
    }

    public void setTypeColumn(String typeColumn) {
        this.typeColumn = typeColumn;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
    
}
