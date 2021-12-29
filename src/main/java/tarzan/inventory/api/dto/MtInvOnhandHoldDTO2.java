package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MtInvOnhandHoldDTO2 implements Serializable {

    private static final long serialVersionUID = -2191525397507902536L;
    private String siteId;
    private String materialId;
    private String locatorId;
    private String lotCode;
    private Double holdQuantity;
    private String holdType;
    private String orderType;
    private String orderId;
    private String eventRequestId;
    private Date shiftDate;
    private String shiftCode;
    private String ownerType;
    private String ownerId;
    private String eventId;
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
    public Double getHoldQuantity() {
        return holdQuantity;
    }
    public void setHoldQuantity(Double holdQuantity) {
        this.holdQuantity = holdQuantity;
    }
    public String getHoldType() {
        return holdType;
    }
    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }
    public String getOrderType() {
        return orderType;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getEventRequestId() {
        return eventRequestId;
    }
    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }
    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }
    public String getShiftCode() {
        return shiftCode;
    }
    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
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
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
}
