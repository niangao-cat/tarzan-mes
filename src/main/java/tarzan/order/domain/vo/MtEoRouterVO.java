package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoRouterVO implements Serializable {

    private static final long serialVersionUID = 1820080734302349097L;
    private String eoId;
    private String routerId;
    private String eventId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
