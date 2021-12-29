package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeVO6 implements Serializable {
    
    private static final long serialVersionUID = -3804428387274786790L;

    @ApiModelProperty("通用类型主键")
    private String genTypeId;
    
    @ApiModelProperty(value = "类型组")
    private String typeGroup;
    
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    
    @ApiModelProperty(value = "备注")
    private String description;
    
    @ApiModelProperty(value =  "服务包")
    private String module;
    
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

}
