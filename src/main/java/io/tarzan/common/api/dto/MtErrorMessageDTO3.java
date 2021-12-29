package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtErrorMessageDTO3 implements Serializable {
    
    private static final long serialVersionUID = 8276259579615623822L;
    
    @ApiModelProperty(value = "消息编码")
    private String messageCode;
    
    @ApiModelProperty(value = "消息内容")
    private String message;
    
    @ApiModelProperty(value = "服务包")
    private String module;
    
    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
    
}
