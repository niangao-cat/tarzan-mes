package tarzan.order.domain.vo;


import java.io.Serializable;
import java.util.Date;


public class MtWorkOrderAttrHisVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4539667479203724105L;
    private String workOrderId;
    private String attrName;
    private String eventId;
    private String eventBy;
    private Date eventTimeFrom;
    private Date eventTimeTo;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
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
        if (eventTimeFrom != null) {
            return (Date) eventTimeFrom.clone();
        } else {
            return null;
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
        if (eventTimeTo != null) {
            return (Date) eventTimeTo.clone();
        } else {
            return null;
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
