package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualHisVO2 implements Serializable {

    private static final long serialVersionUID = 2690198290004431467L;
    private String assembleGroupActualId;
    private String eventId;
    private String eventTypeCode;
    private String assembleGroupId;
    private String workcellId;

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
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

    @Override
    public String toString() {
        return "MtAssembleGroupActualHisVO2{" + "assembleGroupActualId='" + assembleGroupActualId + '\'' + ", eventId='"
                        + eventId + '\'' + ", eventTypeCode='" + eventTypeCode + '\'' + ", assembleGroupId='"
                        + assembleGroupId + '\'' + ", workcellId='" + workcellId + '\'' + '}';
    }
}
