package tarzan.method.api.dto;

import java.io.Serializable;

public class MtOperationDTO2 implements Serializable {
    private static final long serialVersionUID = 8608870831313713221L;

    private String operationType;
    private String siteId;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
