package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeVO3 implements Serializable {
    
    private static final long serialVersionUID = -4423329665962953350L;
    @ApiModelProperty(value = "类型组")
    private String typeGroup;
    
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    
    @ApiModelProperty(value =  "服务包")
    private String module;
    
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
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }


}
