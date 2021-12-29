package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtGenStatusVO implements Serializable {
    private static final long serialVersionUID = -5480619021113963311L;

    private String module;
    private String statusCode;
    private String description;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
