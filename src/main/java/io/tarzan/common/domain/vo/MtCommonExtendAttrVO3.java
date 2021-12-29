package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/11 3:46 上午
 */
public class MtCommonExtendAttrVO3 implements Serializable {
    private static final long serialVersionUID = 1621911793360874398L;
    @ApiModelProperty("主键Id")
    private String keyId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtCommonExtendAttrVO3 that = (MtCommonExtendAttrVO3) o;
        return Objects.equals(keyId, that.keyId) && Objects.equals(attrValue, that.attrValue)
                        && Objects.equals(attrName, that.attrName) && Objects.equals(lang, that.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, attrValue, attrName, lang);
    }
}
