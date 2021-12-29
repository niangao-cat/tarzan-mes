package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenStatusVO2 implements Serializable {
    private static final long serialVersionUID = -3974107833644152077L;

    @ApiModelProperty("服务包代码")
    private String module;
    @ApiModelProperty("状态组编码")
    private String statusGroup;
    @ApiModelProperty("状态编码")
    private String statusCode;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatusGroup() {
        return statusGroup;
    }

    public void setStatusGroup(String statusGroup) {
        this.statusGroup = statusGroup;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
