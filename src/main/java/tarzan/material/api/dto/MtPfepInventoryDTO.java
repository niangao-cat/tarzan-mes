package tarzan.material.api.dto;

import java.io.Serializable;

public class MtPfepInventoryDTO implements Serializable {
    private static final long serialVersionUID = 2837056402143984077L;
    private String materialId;
    private String siteId;
    private String organizationId;
    private String organizationType;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }
}
