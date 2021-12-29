package tarzan.pull.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 拉动线边库存快照
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@ApiModel("拉动线边库存快照")
@ModifyAudit
@Table(name = "mt_pull_onhand_snapshot")
@CustomPrimary
public class MtPullOnhandSnapshot extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ONHAND_SNAPSHOT_ID = "onhandSnapshotId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_ONHAND_QTY = "onhandQty";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_SNAPSHOT_REVISION = "snapshotRevision";
    public static final String FIELD_LATEST_REVISION_FLAG = "latestRevisionFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String onhandSnapshotId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "库位ID", required = true)
    @NotBlank
    @Where
    private String locatorId;
    @ApiModelProperty(value = "现有量")
    @Where
    private Double onhandQty;
    @ApiModelProperty(value = "配送路线", required = true)
    @NotBlank
    @Where
    private String areaId;
    @ApiModelProperty(value = "快照版本：配送路线+YYYYMMDDHHMMSS")
    @Where
    private String snapshotRevision;
    @ApiModelProperty(value = "是否最新版本，Y/N", required = true)
    @NotBlank
    @Where
    private String latestRevisionFlag;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键，唯一标识
     */
    public String getOnhandSnapshotId() {
        return onhandSnapshotId;
    }

    public void setOnhandSnapshotId(String onhandSnapshotId) {
        this.onhandSnapshotId = onhandSnapshotId;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 库位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 现有量
     */
    public Double getOnhandQty() {
        return onhandQty;
    }

    public void setOnhandQty(Double onhandQty) {
        this.onhandQty = onhandQty;
    }

    /**
     * @return 配送路线
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 快照版本：配送路线+YYYYMMDDHHMMSS
     */
    public String getSnapshotRevision() {
        return snapshotRevision;
    }

    public void setSnapshotRevision(String snapshotRevision) {
        this.snapshotRevision = snapshotRevision;
    }

    /**
     * @return 是否最新版本，Y/N
     */
    public String getLatestRevisionFlag() {
        return latestRevisionFlag;
    }

    public void setLatestRevisionFlag(String latestRevisionFlag) {
        this.latestRevisionFlag = latestRevisionFlag;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
