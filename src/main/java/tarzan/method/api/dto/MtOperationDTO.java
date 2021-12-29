package tarzan.method.api.dto;

import java.io.Serializable;

public class MtOperationDTO implements Serializable {
    private static final long serialVersionUID = 5881254043995455661L;

    private String operationName;
    private String siteId;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
