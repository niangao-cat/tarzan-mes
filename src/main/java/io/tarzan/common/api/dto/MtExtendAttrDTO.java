package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtExtendAttrDTO implements Serializable {
    private static final long serialVersionUID = -6087471466910156281L;

    @ApiModelProperty(value = "原表主键")
    private String kid;
    @ApiModelProperty(value = "扩展属性列名（保存时需要使用）")
    private String attrName;
    @ApiModelProperty(value = "拓展字段属性名称")
    private String attrMeaning;
    @ApiModelProperty(value = "值")
    private String attrValue;
    @ApiModelProperty(value = "是否多语言字段（决定attrValue是否可以输入多语言）")
    private String tlFlag;


    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrMeaning() {
        return attrMeaning;
    }

    public void setAttrMeaning(String attrMeaning) {
        this.attrMeaning = attrMeaning;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getTlFlag() {
        return tlFlag;
    }

    public void setTlFlag(String tlFlag) {
        this.tlFlag = tlFlag;
    }
}
