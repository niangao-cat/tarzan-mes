package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtNcIncidentDTO implements Serializable {
    private static final long serialVersionUID = 1945546066423335858L;

    private String siteId;

    private String incidentNumber;

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
}
