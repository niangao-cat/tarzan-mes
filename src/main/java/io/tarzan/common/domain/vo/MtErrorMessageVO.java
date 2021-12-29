package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/7/22 15:35
 */
public class MtErrorMessageVO implements Serializable {

    private static final long serialVersionUID = 8374092666147989842L;
    
    @ApiModelProperty("消息主键ID，唯一性标识")
    private String messageId;
    
    @ApiModelProperty(value = "消息编码")
    private String messageCode;
    
    @ApiModelProperty(value = "消息内容")
    private String message;
    
    @ApiModelProperty(value = "服务包")
    private String module;
    
    @ApiModelProperty(value = "服务包")
    private String moduleDesc;
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

}
