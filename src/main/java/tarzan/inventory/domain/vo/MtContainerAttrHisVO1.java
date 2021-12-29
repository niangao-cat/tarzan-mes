package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtContainerAttrHisVO1 implements Serializable {

    private static final long serialVersionUID = -1521981123157338463L;
    private String containerId;
    private String attrName;
    private String attrValue;
    private String lang;
    private String eventId;
    private Long eventBy;
    private Date eventTime;

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

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
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
}
