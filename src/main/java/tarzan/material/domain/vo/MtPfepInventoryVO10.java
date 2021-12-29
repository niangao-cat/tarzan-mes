package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2020/10/31 10:23
 */
public class MtPfepInventoryVO10 implements Serializable {
    private static final long serialVersionUID = 2853644031358652616L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "组织ID")
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

    public MtPfepInventoryVO10(String materialId, String siteId, String organizationType, String organizationId) {
        this.materialId = materialId;
        this.siteId = siteId;
        this.organizationType = organizationType;
        this.organizationId = organizationId;
    }

    public MtPfepInventoryVO10() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtPfepInventoryVO10 that = (MtPfepInventoryVO10) o;
        return Objects.equals(getMaterialId(), that.getMaterialId()) && Objects.equals(getSiteId(), that.getSiteId())
                        && Objects.equals(getOrganizationType(), that.getOrganizationType())
                        && Objects.equals(getOrganizationId(), that.getOrganizationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getSiteId(), getOrganizationType(), getOrganizationId());
    }
}
