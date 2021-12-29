package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectTypeRelVO7 implements Serializable {
    private static final long serialVersionUID = 2654229702614871753L;

    private String eventRequestId;
    private String eventId;
    private String objectTypeCode;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }
}
