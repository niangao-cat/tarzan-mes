package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeObjectDTO2 implements Serializable {
    private static final long serialVersionUID = 7115903060080724861L;
    @ApiModelProperty(value = "编码对象编码",required = false)
    private String objectCode;
    @ApiModelProperty(value = "编码对象短描述",required = false)
    private String objectName;
    public String getObjectCode() {
        return objectCode;
    }
    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }
    public String getObjectName() {
        return objectName;
    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtNumrangeObjectDTO2 [objectCode=");
        builder.append(objectCode);
        builder.append(", objectName=");
        builder.append(objectName);
        builder.append("]");
        return builder.toString();
    }
    
}
