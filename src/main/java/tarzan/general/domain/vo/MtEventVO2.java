package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventVO2 implements Serializable {
    private static final long serialVersionUID = -4036476776534316406L;

    private String eventRequestId;
    private String eventTypeCode;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }
}
