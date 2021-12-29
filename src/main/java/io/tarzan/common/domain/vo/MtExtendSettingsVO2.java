package io.tarzan.common.domain.vo;


import io.tarzan.common.domain.entity.MtExtendSettings;

public class MtExtendSettingsVO2 extends MtExtendSettings {

    private static final long serialVersionUID = -1633630550350695189L;
    private String attrTable;
    private String attrValue;

    public String getAttrTable() {
        return attrTable;
    }

    public void setAttrTable(String attrTable) {
        this.attrTable = attrTable;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
