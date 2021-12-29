package tarzan.actual.api.dto;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualDTO implements Serializable {
    private static final long serialVersionUID = -9104231092357196042L;
    private String eventId;
    private String assembleGroupActualId;
    private String assembleGroupId;
    private String workcellId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
