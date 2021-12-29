package io.tarzan.common.domain.vo;

import java.io.Serializable;


/**
 * @author MrZ
 */
public class MtExtendVO implements Serializable {
    private static final long serialVersionUID = -8008892045655651995L;

    private String tableName;
    private String keyId;
    private String attrName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public MtExtendVO() {
    }

    public MtExtendVO(String tableName, String keyId, String attrName) {
        this.tableName = tableName;
        this.keyId = keyId;
        this.attrName = attrName;
    }
}
