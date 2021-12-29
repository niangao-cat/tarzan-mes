package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/22 16:20
 */
public class MtExtendAttrVO1 implements Serializable {

    private static final long serialVersionUID = 9045401128009219224L;
    private String attrId;
    private String keyId;
    private String attrName;
    private String attrValue;
    private String lang;


    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
