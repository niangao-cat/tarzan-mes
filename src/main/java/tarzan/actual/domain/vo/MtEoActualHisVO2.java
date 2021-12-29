package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtEoActualHis;

public class MtEoActualHisVO2 extends MtEoActualHis implements Serializable {
    private static final long serialVersionUID = -3554054278377138827L;

    private String eventType; // 事件类型

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
