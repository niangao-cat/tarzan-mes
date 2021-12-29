package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeVO2 implements Serializable {
    private static final long serialVersionUID = -36728545200003631L;
    @ApiModelProperty("服务包代码")
    private String module;
    @ApiModelProperty("类型组编码")
    private String typeGroup;
    @ApiModelProperty("类型编码")
    private String typeCode;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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
}
