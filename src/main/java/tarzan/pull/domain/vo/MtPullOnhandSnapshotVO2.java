package tarzan.pull.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtPullOnhandSnapshotVO2
 * @description
 * @date 2020年02月05日 11:17
 */
public class MtPullOnhandSnapshotVO2 implements Serializable {
    private static final long serialVersionUID = -2895404082389050076L;
    @ApiModelProperty(value = "快照ID")
    private String onhandSnapshotId;
    @ApiModelProperty(value = "快照版本")
    private String snapshotRevision;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String SiteId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty("现有量")
    private Double onhandQty;

    public String getOnhandSnapshotId() {
        return onhandSnapshotId;
    }

    public void setOnhandSnapshotId(String onhandSnapshotId) {
        this.onhandSnapshotId = onhandSnapshotId;
    }

    public String getSnapshotRevision() {
        return snapshotRevision;
    }

    public void setSnapshotRevision(String snapshotRevision) {
        this.snapshotRevision = snapshotRevision;
    }

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

    public Double getOnhandQty() {
        return onhandQty;
    }

    public void setOnhandQty(Double onhandQty) {
        this.onhandQty = onhandQty;
    }
}
