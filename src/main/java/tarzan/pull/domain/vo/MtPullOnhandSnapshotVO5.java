package tarzan.pull.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPullOnhandSnapshotVO5
 *
 * @author: {xieyiyang}
 * @date: 2020/2/6 10:04
 * @description:
 */
public class MtPullOnhandSnapshotVO5 implements Serializable {
    private static final long serialVersionUID = 8353092841782154319L;

    @ApiModelProperty(value = "快照ID")
    private String onhandSnapshotId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "快照版本")
    private String snapshotRevision;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;
    @ApiModelProperty(value = "当前版本标识")
    private String latestRevisionFlag;

    public String getOnhandSnapshotId() {
        return onhandSnapshotId;
    }

    public void setOnhandSnapshotId(String onhandSnapshotId) {
        this.onhandSnapshotId = onhandSnapshotId;
    }

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

    public String getLatestRevisionFlag() {
        return latestRevisionFlag;
    }

    public void setLatestRevisionFlag(String latestRevisionFlag) {
        this.latestRevisionFlag = latestRevisionFlag;
    }
}
