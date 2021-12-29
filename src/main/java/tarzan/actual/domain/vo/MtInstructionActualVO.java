package tarzan.actual.domain.vo;


import java.io.Serializable;
import java.util.Date;

import tarzan.actual.domain.entity.MtInstructionActual;

/**
 * @author Leeloing
 * @date 2019/6/19 14:14
 */
public class MtInstructionActualVO extends MtInstructionActual implements Serializable {
    private static final long serialVersionUID = -1782707005552206381L;
    private String eventId;
    private Date eventTime;
    private Long eventBy;
    private String connectEventId;
    private Date connectEventTime;
    private Long connectEventBy;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        } else {
            return (Date) eventTime.clone();
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public String getConnectEventId() {
        return connectEventId;
    }

    public void setConnectEventId(String connectEventId) {
        this.connectEventId = connectEventId;
    }

    public Date getConnectEventTime() {
        if (connectEventTime == null) {
            return null;
        } else {
            return (Date) connectEventTime.clone();
        }
    }

    public void setConnectEventTime(Date connectEventTime) {
        if (connectEventTime == null) {
            this.connectEventTime = null;
        } else {
            this.connectEventTime = (Date) connectEventTime.clone();
        }
    }

    public Long getConnectEventBy() {
        return connectEventBy;
    }

    public void setConnectEventBy(Long connectEventBy) {
        this.connectEventBy = connectEventBy;
    }
}
