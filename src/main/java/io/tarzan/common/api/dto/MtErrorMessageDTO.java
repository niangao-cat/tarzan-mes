package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtErrorMessageDTO implements Serializable {
    private static final long serialVersionUID = -5330756568601261011L;
    private String module;
    private String message;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
