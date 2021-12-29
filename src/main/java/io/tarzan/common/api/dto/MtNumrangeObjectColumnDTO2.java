package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtNumrangeObjectColumnDTO2 implements Serializable {

    private static final long serialVersionUID = -8160082792707296252L;
    private String objectId;
    private String objectColumnCode;
    private String objectColumnName;
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtNumrangeObjectColumnDTO2 [objectId=");
        builder.append(objectId);
        builder.append(", objectColumnCode=");
        builder.append(objectColumnCode);
        builder.append(", objectColumnName=");
        builder.append(objectColumnName);
        builder.append("]");
        return builder.toString();
    }
   
}
