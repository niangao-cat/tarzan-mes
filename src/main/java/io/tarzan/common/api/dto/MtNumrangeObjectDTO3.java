package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeObjectDTO3 implements Serializable {
    private static final long serialVersionUID = -3055256619839093368L;
    @ApiModelProperty(value = "对象Id")
    private String objectId;
    
    @ApiModelProperty(value = "对象编码",required = true)
    @NotBlank
    private String objectCode;
    
    @ApiModelProperty(value = "对象短描述",required = false)
    @NotBlank
    private String objectName;
    
    @ApiModelProperty(value = "对象长描述")
    private String description;
    
    @ApiModelProperty(value = "类型组")
    private String typeGroup;
    
    @ApiModelProperty(value = "服务包")
    private String module;
    
    @ApiModelProperty(value = "启用状态")
    @NotBlank
    private String enableFlag;
    
    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;
    
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
    public String getEnableFlag() {
        return enableFlag;
    }
    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }
    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
