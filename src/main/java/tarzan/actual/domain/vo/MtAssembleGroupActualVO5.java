package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtAssembleGroupActual;

public class MtAssembleGroupActualVO5 extends MtAssembleGroupActual implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6326214300542694854L;
    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
