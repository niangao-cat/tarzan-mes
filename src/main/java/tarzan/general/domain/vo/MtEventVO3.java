package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventVO3 implements Serializable {
    private static final long serialVersionUID = -508235844241098639L;

    private String parentEventId;
    private String eventId;

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
