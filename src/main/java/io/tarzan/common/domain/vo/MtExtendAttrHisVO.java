package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtExtendAttrHisVO implements Serializable {
    private static final long serialVersionUID = -3742732771746281739L;
    private String attrHisId;
    private String attrId;
    private String kid;
    private String attrName;
    private String attrValue;
    private String lang;
    private String eventId;
    private Long eventBy;
    private Date eventTime;

    public String getAttrHisId() {
        return attrHisId;
    }

    public void setAttrHisId(String attrHisId) {
        this.attrHisId = attrHisId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
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
        }
        return (Date) eventTime.clone();
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }
}
