package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/7/22 15:35
 */
public class MtErrorMessageVO2 implements Serializable {

    private static final long serialVersionUID = -2344968553313386756L;

    @ApiModelProperty(value = "消息主键ID，唯一性标识",required = true)
    private String messageId;
    
    @ApiModelProperty(value = "消息编码",required = true)
    private String messageCode;

    @ApiModelProperty(value = "服务包", required = true)
    private String module;
    
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
