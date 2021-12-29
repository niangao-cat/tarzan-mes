package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtErrorMessageDTO4 implements Serializable {
    
    private static final long serialVersionUID = 4353810162963800309L;

    @ApiModelProperty(value = "消息Id")
    private String messageId;
    
    @ApiModelProperty(value = "消息编码",required = true)
    @NotBlank
    private String messageCode;
    
    @ApiModelProperty(value = "消息内容",required = true)
    @NotBlank
    private String message;
    
    @ApiModelProperty(value = "服务包",required = true)
    @NotBlank
    private String module;

    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;
    
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

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
