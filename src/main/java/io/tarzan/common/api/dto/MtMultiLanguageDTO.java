package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtMultiLanguageDTO implements Serializable {
    private static final long serialVersionUID = 8881112996042533473L;
    private String tableName;
    private String fieldName;
    private String keyId;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
