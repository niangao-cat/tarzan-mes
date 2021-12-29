package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtExtendAttrVO2 implements Serializable {
    private static final long serialVersionUID = 4530932435729553581L;
    private String attrId;
    private String attrValue;
    private String attrName;
    private String lang;

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
