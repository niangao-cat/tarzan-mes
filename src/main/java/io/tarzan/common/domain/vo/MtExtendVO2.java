package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtExtendVO2 implements Serializable {
    private static final long serialVersionUID = -8497204461386507834L;
    private String tableName;
    private String attrName;
    private String attrValue;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
}
