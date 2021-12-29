package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectTypeRelVO5 implements Serializable {
    private static final long serialVersionUID = 135342397173349385L;

    private String eventId;
    private String objectTypeCode;

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
