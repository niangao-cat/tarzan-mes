package io.tarzan.common.domain.vo;

import java.io.Serializable;


/**
 * @author MrZ
 */
public class MtExtendAttrVO implements Serializable {
    private static final long serialVersionUID = 4958540587611143335L;

    private String attrValue;
    private String attrName;

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
}
