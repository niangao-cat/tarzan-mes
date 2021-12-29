package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeDTO implements Serializable {

    private static final long serialVersionUID = -7803683297417654830L;

    @ApiModelProperty(value = "类型Id")
    private String genTypeId;
    
    @ApiModelProperty(value = "类型组",required = true)
    @NotBlank
    private String typeGroup;
    
    @ApiModelProperty(value = "类型编码",required = true)
    @NotBlank
    private String typeCode;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "服务包",required = true)
    @NotBlank
    private String module;
    
    @ApiModelProperty(value = "顺序",required = true)
    @NotNull
    private Double sequence;
    
    @ApiModelProperty(value = "默认状态，Y/N",required = true)
    @NotBlank
    private String defaultFlag;
    
    @ApiModelProperty(value = "初始数据标识",required = true)
    @NotBlank
    private String initialFlag;
    
    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;

    public String getGenTypeId() {
        return genTypeId;
    }

    public void setGenTypeId(String genTypeId) {
        this.genTypeId = genTypeId;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
    
    
}
