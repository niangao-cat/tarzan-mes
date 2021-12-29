package tarzan.pull.domain.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 拉动驱动指令快照
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@ApiModel("拉动驱动指令快照")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pull_instruction_snapshot")
@CustomPrimary
public class MtPullInstructionSnapshot extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_SNAPSHOT_ID = "instructionSnapshotId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_SOURCE_ORDER_TYPE = "sourceOrderType";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_TO_LOCATOR_ID = "toLocatorId";
    public static final String FIELD_WAVE_SEQUENCE = "waveSequence";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_COVER_QTY = "coverQty";
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
    private String instructionSnapshotId;
    @ApiModelProperty(value = "指令ID", required = true)
    @NotBlank
    @Where
    private String instructionId;
    @ApiModelProperty(value = "指令状态", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "指令数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "来源单据类型")
    @Where
    private String sourceOrderType;
    @ApiModelProperty(value = "EO")
    @Where
    private String eoId;
    @ApiModelProperty(value = "目标库位")
    @Where
    private String toLocatorId;
    @ApiModelProperty(value = "波次")
    @Where
    private String waveSequence;
    @ApiModelProperty(value = "顺序")
    @Where
    private Long sequence;
    @ApiModelProperty(value = "日期")
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次")
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "覆盖数量")
    @Where
    private Double coverQty;
    @ApiModelProperty(value = "配送路线")
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
    public String getInstructionSnapshotId() {
        return instructionSnapshotId;
    }

    public void setInstructionSnapshotId(String instructionSnapshotId) {
        this.instructionSnapshotId = instructionSnapshotId;
    }

    /**
     * @return 指令ID
     */
    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    /**
     * @return 指令状态
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
     * @return 指令数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 来源单据类型
     */
    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    /**
     * @return EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 目标库位
     */
    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }

    /**
     * @return 波次
     */
    public String getWaveSequence() {
        return waveSequence;
    }

    public void setWaveSequence(String waveSequence) {
        this.waveSequence = waveSequence;
    }

    /**
     * @return 顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 日期
     */
    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    /**
     * @return 班次
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 覆盖数量
     */
    public Double getCoverQty() {
        return coverQty;
    }

    public void setCoverQty(Double coverQty) {
        this.coverQty = coverQty;
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
