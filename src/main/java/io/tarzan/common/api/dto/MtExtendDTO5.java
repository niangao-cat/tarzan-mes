package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtExtendDTO5 implements Serializable {

    private static final long serialVersionUID = -4997651875426159793L;
    private String attrTable;
    private String enableFlag;

    public String getAttrTable() {
        return attrTable;
    }

    public void setAttrTable(String attrTable) {
        this.attrTable = attrTable;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
