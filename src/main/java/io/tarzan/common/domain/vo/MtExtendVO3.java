package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtExtendVO3 implements Serializable {

    private static final long serialVersionUID = -2580835381669410340L;
    private String attrName;
    private String attrValue;
    private String tlFlag;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
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
