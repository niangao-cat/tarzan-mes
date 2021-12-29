package tarzan.material.domain.vo;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Created by slj on 2018-11-22.
 */
public class MtPfepInventoryVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4050457186307205761L;
    private String materialId;
    private String siteId;
    private String organizationType;
    private String organizationId;

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

    public MtPfepInventoryVO() {}

    public MtPfepInventoryVO(String materialId, String siteId, String organizationType, String organizationId) {
        this.materialId = materialId;
        this.siteId = siteId;
        this.organizationType = organizationType;
        this.organizationId = organizationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtPfepInventoryVO that = (MtPfepInventoryVO) o;
        return Objects.equal(materialId, that.materialId) && Objects.equal(siteId, that.siteId)
                        && Objects.equal(organizationType, that.organizationType)
                        && Objects.equal(organizationId, that.organizationId);

    }

    @Override
    public int hashCode() {

        return Objects.hashCode(materialId, siteId, organizationType, organizationId);
    }
}
