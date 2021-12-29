package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoBomHisVO implements Serializable {

    private static final long serialVersionUID = -3114652915832305466L;
    private String eoBomId;
    private String eventId;
    private String eventTimeFrom;
    private String eventTimeTo;
    private String eventBy;

    public String getEoBomId() {
        return eoBomId;
    }

    public void setEoBomId(String eoBomId) {
        this.eoBomId = eoBomId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTimeFrom() {
        return eventTimeFrom;
    }

    public void setEventTimeFrom(String eventTimeFrom) {
        this.eventTimeFrom = eventTimeFrom;
    }

    public String getEventTimeTo() {
        return eventTimeTo;
    }

    public void setEventTimeTo(String eventTimeTo) {
        this.eventTimeTo = eventTimeTo;
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

}
