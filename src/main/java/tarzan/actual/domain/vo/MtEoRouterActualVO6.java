package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO6 implements Serializable {
    private static final long serialVersionUID = -3869669569546292764L;
    private String eoRouterActualId;
    private String status;
    private Double qty;
    private String eventId;
    private Double completedQty;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }
}
