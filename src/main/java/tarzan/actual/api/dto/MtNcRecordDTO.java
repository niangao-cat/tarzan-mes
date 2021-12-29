package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtNcRecordDTO implements Serializable {
    private static final long serialVersionUID = -8057172393673403600L;
    private String eoId;
    private String ncIncidentId;
    private String ncCodeId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }
}
