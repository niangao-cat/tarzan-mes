package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by slj on 2019-02-13.
 */
public class MtWorkOrderActualHisVO implements Serializable {

    private static final long serialVersionUID = -9180558738353504994L;
    private String workOrderId;
    private String workOrderActualId;
    private String eventId;
    private Date eventTimeFrom;
    private Date eventTimeTo;
    private String eventBy;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getEventTimeFrom() {
        if (eventTimeFrom == null) {
            return null;
        }
        return (Date) eventTimeFrom.clone();
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
        }
        return (Date) eventTimeTo.clone();
    }

    public void setEventTimeTo(Date eventTimeTo) {
        if (eventTimeTo == null) {
            this.eventTimeTo = null;
        } else {
            this.eventTimeTo = (Date) eventTimeTo.clone();
        }
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }



}
