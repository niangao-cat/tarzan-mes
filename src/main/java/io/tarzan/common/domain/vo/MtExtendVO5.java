package io.tarzan.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtExtendVO5 implements Serializable {
    private static final long serialVersionUID = 4958400270653663943L;
    @ApiModelProperty(value = "扩展属性")
    private String attrName;
    @ApiModelProperty(value = "语言")
    private String lang;
    @ApiModelProperty(value = "扩展属性值")
    private String attrValue;

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

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public MtExtendVO5() {
    }

    public MtExtendVO5(String attrName) {
        this.attrName = attrName;
    }
}
