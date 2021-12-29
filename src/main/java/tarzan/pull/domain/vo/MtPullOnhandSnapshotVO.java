package tarzan.pull.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtPullOnhandSnapshotVO
 * @description
 * @date 2020年02月05日 11:16
 */
public class MtPullOnhandSnapshotVO implements Serializable {
    private static final long serialVersionUID = 8219138101080644290L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String SiteId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "快照版本")
    private String snapshotRevision;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        this.SiteId = siteId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getSnapshotRevision() {
        return snapshotRevision;
    }

    public void setSnapshotRevision(String snapshotRevision) {
        this.snapshotRevision = snapshotRevision;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtPullOnhandSnapshotVO that = (MtPullOnhandSnapshotVO) o;
        return Objects.equals(materialId, that.materialId) && Objects.equals(SiteId, that.SiteId)
                        && Objects.equals(areaId, that.areaId) && Objects.equals(locatorId, that.locatorId)
                        && Objects.equals(snapshotRevision, that.snapshotRevision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, SiteId, areaId, locatorId, snapshotRevision);
    }
}
