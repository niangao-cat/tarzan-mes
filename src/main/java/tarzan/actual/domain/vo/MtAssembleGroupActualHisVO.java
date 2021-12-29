package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualHisVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6258565994545743792L;
    private String assembleGroupActualId;
    private String eventId;
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
        return "MtAssembleGroupActualHisVO{" + "assembleGroupActualId='" + assembleGroupActualId + '\'' + ", eventId='"
                        + eventId + '\'' + ", assembleGroupId='" + assembleGroupId + '\'' + ", workcellId='"
                        + workcellId + '\'' + '}';
    }
}
