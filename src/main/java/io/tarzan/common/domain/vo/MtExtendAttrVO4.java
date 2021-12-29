package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/2 16:12
 * @Author: ${yiyang.xie}
 */
public class MtExtendAttrVO4 implements Serializable {
    private static final long serialVersionUID = -2423159430946870962L;

    @ApiModelProperty("主键Id")
    private String keyId;
    @ApiModelProperty("扩展字段Id")
    private String attrId;
    @ApiModelProperty("扩展字段值")
    private String attrValue;
    @ApiModelProperty("扩展字段名")
    private String attrName;
    @ApiModelProperty("语言")
    private String lang;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}