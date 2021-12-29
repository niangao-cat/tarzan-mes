package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtMaterialLotVO24 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6636294982769718328L;
    private String materialLotId;
    private String materialLotCode;
    private String siteId;
    private String enableFlag;
    private String materialId;
    private String primaryUomId;
    private Double primaryUomQty;
    private String secondaryUomId;
    private Double secondaryUomQty;
    private String locatorId;
    private String ownerType;
    private String ownerId;
    private String reservedFlag;
    private String reservedObjectType;
    private String reservedObjectId;
    private Date inLocatorTime;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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

    public String getReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    public Date getInLocatorTime() {
        if (inLocatorTime != null) {
            return (Date) inLocatorTime.clone();
        } else {
            return null;
        }
    }

    public void setInLocatorTime(Date inLocatorTime) {
        if (inLocatorTime == null) {
            this.inLocatorTime = null;
        } else {
            this.inLocatorTime = (Date) inLocatorTime.clone();
        }
    }


}
