package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoVO13 implements Serializable {
    private static final long serialVersionUID = 3668734157527092878L;
    private String eoId;
    private String eventId;
    private Double targetQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(Double targetQty) {
        this.targetQty = targetQty;
    }
}
