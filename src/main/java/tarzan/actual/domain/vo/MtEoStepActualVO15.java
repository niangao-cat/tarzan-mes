package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO15 implements Serializable {
    private static final long serialVersionUID = -2294247845585579576L;

    private String eoStepActualId;
    private String eventId;
    private String updateType;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
}
