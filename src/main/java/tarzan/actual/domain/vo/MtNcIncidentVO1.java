package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcIncidentVO1 implements Serializable {


    private static final long serialVersionUID = -1367489283557891606L;

    private String siteId;
    private String incidentNumber;
    private String eventId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getIncidentNumber() {
        return incidentNumber;
    }

    public void setIncidentNumber(String incidentNumber) {
        this.incidentNumber = incidentNumber;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
