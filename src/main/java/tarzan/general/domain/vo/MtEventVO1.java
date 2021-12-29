package tarzan.general.domain.vo;

import java.io.Serializable;

import tarzan.general.domain.entity.MtEvent;

public class MtEventVO1 extends MtEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4612753118784172873L;
    private String eventTypeCode;
    private String requestTypeId;
    private String requestTypeCode;

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

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

}
