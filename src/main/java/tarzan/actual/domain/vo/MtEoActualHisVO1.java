package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoActualHisVO1 implements Serializable {
    private static final long serialVersionUID = -1860897519210476774L;

    private String eoActualId; // 外键
    private String eoId; // EO主键，标识唯一EO
    private String eventId; // 事件ID
    private Date eventTimeFrom; // 事件时间从
    private Date eventTimeTo; // 事件时间到
    private Long eventBy; // 创建人

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }
}
