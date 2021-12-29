package tarzan.order.domain.entity;

import java.io.Serializable;

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
 * 执行作业变更记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@ApiModel("执行作业变更记录")

@ModifyAudit

@Table(name = "mt_eo_batch_change_history")
@CustomPrimary
public class MtEoBatchChangeHistory extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_BATCH_CHANGE_HISTORY_ID = "eoBatchChangeHistoryId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_SOURCE_EO_ID = "sourceEoId";
    public static final String FIELD_REASON = "reason";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_SOURCE_TRX_QTY = "sourceTrxQty";
    public static final String FIELD_TRX_QTY = "trxQty";
    public static final String FIELD_SOURCE_EO_STEP_ACTUAL_ID = "sourceEoStepActualId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -4027338139647363754L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String eoBatchChangeHistoryId;
    @ApiModelProperty(value = "变更后EO,EO主键，标识唯一EO", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "变更来源EO，EO主键，标识唯一EO", required = true)
    @NotBlank
    @Where
    private String sourceEoId;
    @ApiModelProperty(value = "变更原因", required = true)
    @NotBlank
    @Where
    private String reason;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "事件ID，用于表示一次变更操作", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "来源变更数量", required = true)
    @NotNull
    @Where
    private Double sourceTrxQty;
    @ApiModelProperty(value = "变更数量", required = true)
    @NotNull
    @Where
    private Double trxQty;
    @ApiModelProperty(value = "步骤实绩ID")
    @Where
    private String sourceEoStepActualId;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getEoBatchChangeHistoryId() {
        return eoBatchChangeHistoryId;
    }

    public void setEoBatchChangeHistoryId(String eoBatchChangeHistoryId) {
        this.eoBatchChangeHistoryId = eoBatchChangeHistoryId;
    }

    /**
     * @return 变更后EO,EO主键，标识唯一EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 变更来源EO，EO主键，标识唯一EO
     */
    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    /**
     * @return 变更原因 R：下达 S：序列化 P：拆分 A：自动拆分 M：合并，合并时可能记录多条记录
     */
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
     * @return 事件ID，用于表示一次变更操作
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return 来源变更数量
     */
    public Double getSourceTrxQty() {
        return sourceTrxQty;
    }

    public void setSourceTrxQty(Double sourceTrxQty) {
        this.sourceTrxQty = sourceTrxQty;
    }

    /**
     * @return 变更数量
     */
    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    /**
     * @return 步骤实绩ID
     */
    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
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
