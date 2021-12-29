package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/4/8 19:07
 */
public class MtContainerHisVO1 implements Serializable {
    private static final long serialVersionUID = -9081264241458155067L;

    private String containerId;
    private String eventId;
    private String eventBy;
    private Date eventTimeFrom;
    private Date eventTimeTo;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

    public Date getEventTimeFrom() {
        if (eventTimeFrom == null) {
            return null;
        } else {
            return (Date) eventTimeFrom.clone();
        }
    }

    public void setEventTimeFrom(Date eventTimeFrom) {
        if (eventTimeFrom == null) {
            this.eventTimeFrom = null;
        } else {
            this.eventTimeFrom = (Date) eventTimeFrom.clone();
        }
    }

    public Date getEventTimeTo() {
        if (eventTimeTo == null) {
            return null;
        } else {
            return (Date) eventTimeTo.clone();
        }
    }

    public void setEventTimeTo(Date eventTimeTo) {
        if (eventTimeTo == null) {
            this.eventTimeTo = null;
        } else {
            this.eventTimeTo = (Date) eventTimeTo.clone();
        }
    }
}
