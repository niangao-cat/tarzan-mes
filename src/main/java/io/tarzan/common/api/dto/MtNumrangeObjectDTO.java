package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtNumrangeObjectDTO implements Serializable {
    private static final long serialVersionUID = -3055256619839093368L;
    private String objectCode;
    private String objectName;
    private String description;
    private String enableFlag;

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
