package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtWorkOrderVO25 implements Serializable {

    private static final long serialVersionUID = -3235902435655168359L;
    private String workOrderId;

    private String status;

    private String eventId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
