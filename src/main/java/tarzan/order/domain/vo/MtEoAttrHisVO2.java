package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/4/16 20:27
 */
public class MtEoAttrHisVO2 implements Serializable {
    private static final long serialVersionUID = -3957700816659508941L;

    private String eoId;
    private String attrName;
    private String eventId;
    private String eventBy;
    private Date eventTimeFrom;
    private Date eventTimeTo;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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
