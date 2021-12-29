package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcIncidentVO2 implements Serializable {

    private static final long serialVersionUID = 532510140487600171L;

    private String ncIncidentId;
    private String status;
    private String eventId;

    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
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
