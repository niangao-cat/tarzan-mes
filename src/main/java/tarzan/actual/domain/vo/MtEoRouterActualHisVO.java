package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualHisVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5828870689113739435L;
    private String eventId;
    private String eoId;
    private String routerId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

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

}
