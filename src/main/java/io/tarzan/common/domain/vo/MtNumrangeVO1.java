package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeVO1 implements Serializable {

    private static final long serialVersionUID = -3330136923208388141L;

    @ApiModelProperty(value = "编码对象Id",required = true)
    private String objectId;
    
    private String objectCode;

    private String objectDesc;

    private String module;
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectDesc() {
        return objectDesc;
    }

    public void setObjectDesc(String objectDesc) {
        this.objectDesc = objectDesc;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
