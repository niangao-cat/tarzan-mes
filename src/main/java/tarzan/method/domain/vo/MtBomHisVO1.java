package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomHisVO1 implements Serializable {
    private static final long serialVersionUID = -8009624360376972348L;

    private String bomId; // 装配清单ID
    private String eventTypeCode; // 事件类型CODE

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }
}
