package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO6 implements Serializable {
    private static final long serialVersionUID = -8371383475011242682L;

    private String eventTypeCode; // 事件类型CODE
    private String workcellId; // 工作单元唯一标识
    private String parentEventId; // 父事件ID
    private String eventRequestId; // 事件组ID

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
