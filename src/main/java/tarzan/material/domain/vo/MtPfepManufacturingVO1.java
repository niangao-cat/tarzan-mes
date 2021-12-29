package tarzan.material.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by slj on 2018-11-22.
 */
public class MtPfepManufacturingVO1 implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtPfepManufacturingVO1 that = (MtPfepManufacturingVO1) o;
        return Objects.equals(getMaterialId(), that.getMaterialId()) && Objects.equals(getSiteId(), that.getSiteId())
                        && Objects.equals(getOrganizationType(), that.getOrganizationType())
                        && Objects.equals(getOrganizationId(), that.getOrganizationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getSiteId(), getOrganizationType(), getOrganizationId());
    }
}


