package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.tarzan.common.domain.vo.MtExtendAttrHisVO2;

public class MtExtendAttrHisDTO2 implements Serializable {
    private static final long serialVersionUID = -8669879254013140276L;
    private String tableName;
    private MtExtendAttrHisVO2 condition;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public MtExtendAttrHisVO2 getCondition() {
        return condition;
    }

    public void setCondition(MtExtendAttrHisVO2 condition) {
        this.condition = condition;
    }
}
