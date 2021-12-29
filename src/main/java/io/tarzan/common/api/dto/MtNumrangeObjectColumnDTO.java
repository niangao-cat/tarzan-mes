package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtNumrangeObjectColumnDTO implements Serializable {

    private static final long serialVersionUID = -6898930621502374064L;
    private String objectId;
    private String objectColumnId;
    private String objectColumnCode;
    private String objectColumnName;
    private String enableFlag;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

    public String getObjectColumnCode() {
        return objectColumnCode;
    }

    public void setObjectColumnCode(String objectColumnCode) {
        this.objectColumnCode = objectColumnCode;
    }

    public String getObjectColumnName() {
        return objectColumnName;
    }

    public void setObjectColumnName(String objectColumnName) {
        this.objectColumnName = objectColumnName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
