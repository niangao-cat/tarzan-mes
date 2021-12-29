package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.Date;

import tarzan.inventory.domain.entity.MtContainer;

/**
 * @author Leeloing
 * @date 2019/4/8 19:07
 */
public class MtContainerHisVO2 extends MtContainer implements Serializable {

    private static final long serialVersionUID = 646085393539174731L;
    private String eventId;
    private String eventTypeCode;
    private String requestTypeCode;
    private Date eventTime;
    private Long eventBy;

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

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
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
}
