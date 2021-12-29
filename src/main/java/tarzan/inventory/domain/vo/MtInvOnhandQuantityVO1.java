package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtInvOnhandQuantityVO1 implements Serializable {

    private static final long serialVersionUID = 5847444215125007614L;
    private List<String> siteId;
    private List<String> materialId;
    private List<String> locatorId;
    private List<String> lotCode;
    private List<String> ownerType;
    private List<String> ownerId;

    public List<String> getSiteId() {
        return siteId;
    }

    public void setSiteId(List<String> siteId) {
        this.siteId = siteId;
    }

    public List<String> getMaterialId() {
        return materialId;
    }

    public void setMaterialId(List<String> materialId) {
        this.materialId = materialId;
    }

    public List<String> getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(List<String> locatorId) {
        this.locatorId = locatorId;
    }

    public List<String> getLotCode() {
        return lotCode;
    }

    public void setLotCode(List<String> lotCode) {
        this.lotCode = lotCode;
    }

    public List<String> getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(List<String> ownerType) {
        this.ownerType = ownerType;
    }

    public List<String> getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(List<String> ownerId) {
        this.ownerId = ownerId;
    }

}
