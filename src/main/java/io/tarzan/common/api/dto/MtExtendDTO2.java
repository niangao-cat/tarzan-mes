package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

public class MtExtendDTO2 implements Serializable {
    private static final long serialVersionUID = 1059636567037210741L;
    private String tableName;
    private Map<String, String> attrs;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
