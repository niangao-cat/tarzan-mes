package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventRequestTypeVO implements Serializable {

    private static final long serialVersionUID = 5525897949866278343L;
    private String requestTypeCode;
    private String description;
    private String enableFlag;

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
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
