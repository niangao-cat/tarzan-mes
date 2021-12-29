package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Map;

public class MtEventRequestTypeVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5572104570441437105L;
    private String requestTypeId;
    private String requestTypeCode;
    private String description;
    private String enableFlag;
    private Map<String, Map<String, String>> _tls;

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
