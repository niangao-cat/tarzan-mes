package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectTypeRelVO4 implements Serializable {
    private static final long serialVersionUID = 6823729732778159889L;

    private String eventTypeId;
    private String eventTypeCode;
    private String enableFlag;

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
