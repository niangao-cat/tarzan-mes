package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtMaterialLotHisVO1 implements Serializable {
    private static final long serialVersionUID = 6137512469904493821L;

    private String eventRequestId; // 事件请求ID
    private String eventTypeCode; // 事件类型编码

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
