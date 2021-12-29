package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtErrorMessageDTO2 implements Serializable {
    private static final long serialVersionUID = 4024040483900401355L;
    private String messageCode;
    private String module;

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
