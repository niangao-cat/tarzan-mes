package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtInvOnhandQuantityVO implements Serializable {

    private static final long serialVersionUID = -2130192008254516029L;
    private String siteId;
    private String materialId;
    private String locatorId;
    private String lotCode;
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"

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

    public MtInvOnhandQuantityVO(String siteId, String materialId, String locatorId, String lotCode, String ownerType,
                    String ownerId) {
        this.siteId = siteId;
        this.materialId = materialId;
        this.locatorId = locatorId;
        this.lotCode = lotCode;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }

    public MtInvOnhandQuantityVO() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtInvOnhandQuantityVO that = (MtInvOnhandQuantityVO) o;
        return Objects.equals(getSiteId(), that.getSiteId()) && Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getLocatorId(), that.getLocatorId())
                        && Objects.equals(getLotCode(), that.getLotCode())
                        && Objects.equals(getOwnerType(), that.getOwnerType())
                        && Objects.equals(getOwnerId(), that.getOwnerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSiteId(), getMaterialId(), getLocatorId(), getLotCode(), getOwnerType(), getOwnerId());
    }
}
