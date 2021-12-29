package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtInvJournalVO implements Serializable {
    private static final long serialVersionUID = -4309370449649104270L;
    private String siteId; // 站点ID
    private String materialId; // 物料ID
    private String locatorId; // 库位ID
    private String lotCode; // 批次CODE
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"
    private String eventId; // 事件ID
    private Long eventBy; // 创建人
    private Date eventStartTime; // 事件时间从
    private Date eventEndTime; // 事件时间从

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public Date getEventStartTime() {
        if (eventStartTime == null) {
            return null;
        }
        return (Date) eventStartTime.clone();
    }

    public void setEventStartTime(Date eventStartTime) {
        if (eventStartTime == null) {
            this.eventStartTime = null;
        } else {
            this.eventStartTime = (Date) eventStartTime.clone();
        }
    }

    public Date getEventEndTime() {
        if (eventEndTime == null) {
            return null;
        }
        return (Date) eventEndTime.clone();
    }

    public void setEventEndTime(Date eventEndTime) {
        if (eventEndTime == null) {
            this.eventEndTime = null;
        } else {
            this.eventEndTime = (Date) eventEndTime.clone();
        }
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}
