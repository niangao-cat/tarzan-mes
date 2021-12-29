package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtExtendVO4 implements Serializable {
    private static final long serialVersionUID = -6984384140754120142L;
    private String attrName;
    private String lang;
    private String tlFlag;
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

    public String getTlFlag() {
        return tlFlag;
    }

    public void setTlFlag(String tlFlag) {
        this.tlFlag = tlFlag;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
