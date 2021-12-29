package tarzan.method.api.dto;

import java.io.Serializable;

public class MtRouterHisDTO implements Serializable {
    private static final long serialVersionUID = -2480406991595164798L;

    private String routerId;
    private String eventId;

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
