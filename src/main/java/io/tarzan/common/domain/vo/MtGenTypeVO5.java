package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeVO5 implements Serializable {
    
    private static final long serialVersionUID = -2786965637352195586L;

    @ApiModelProperty(value = "服务包描述")
    private String description;
    
    @ApiModelProperty(value =  "服务包")
    private String module;

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
