package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtWorkOrderVO22 implements Serializable {

    private static final long serialVersionUID = 4632311817159996441L;
    private String workOrderId;
    private Double targetQty;
    private String eventId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(Double targetQty) {
        this.targetQty = targetQty;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
