package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtInvOnhandQuantityVO7 implements Serializable {
    private static final long serialVersionUID = -3810426472032666075L;
    private String materialId;
    private String lotCode;
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"
    private String organizationType;
    private String organizationId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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
