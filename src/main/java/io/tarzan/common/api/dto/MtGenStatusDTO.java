package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenStatusDTO implements Serializable {

    private static final long serialVersionUID = -2914170538205235537L;

    @ApiModelProperty(value = "状态编码")
    private String statusCode;

    @ApiModelProperty(value = "状态组")
    private String statusGroup;

    @ApiModelProperty(value = "服务包")
    private String module;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusGroup() {
        return statusGroup;
    }

    public void setStatusGroup(String statusGroup) {
        this.statusGroup = statusGroup;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
