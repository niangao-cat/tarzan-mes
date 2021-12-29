package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO2 implements Serializable {
    private static final long serialVersionUID = 4694889511335357683L;
    private String ncRecordId; // 不良记录
    private String workcellId; // 工作单元唯一标识
    private String parentEventId; // 父事件ID
    private String eventRequestId; // 事件组ID

    public String getNcRecordId() {
        return ncRecordId;
    }

    public void setNcRecordId(String ncRecordId) {
        this.ncRecordId = ncRecordId;
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
