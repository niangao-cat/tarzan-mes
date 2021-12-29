package tarzan.material.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtPfepScheduleVO2
 * @description
 * @date 2020年03月16日 14:35
 */
public class MtPfepScheduleVO2 implements Serializable {
    private static final long serialVersionUID = -3366271147787656099L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtPfepScheduleVO2 that = (MtPfepScheduleVO2) o;
        return Objects.equals(getMaterialId(), that.getMaterialId()) && Objects.equals(getSiteId(), that.getSiteId())
                && Objects.equals(getOrganizationType(), that.getOrganizationType())
                && Objects.equals(getOrganizationId(), that.getOrganizationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getSiteId(), getOrganizationType(), getOrganizationId());
    }
}


