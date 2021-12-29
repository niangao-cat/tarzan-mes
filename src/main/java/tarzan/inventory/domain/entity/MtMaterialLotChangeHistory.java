package tarzan.inventory.domain.entity;

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
 * 物料批变更历史，记录物料批拆分合并的变更情况
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@ApiModel("物料批变更历史，记录物料批拆分合并的变更情况")

@ModifyAudit

@Table(name = "mt_material_lot_change_history")
@CustomPrimary
public class MtMaterialLotChangeHistory extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_LOT_CHANGE_HISTORY_ID = "materialLotChangeHistoryId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_MATERIAL_LOT_HIS_ID = "materialLotHisId";
    public static final String FIELD_SOURCE_MATERIAL_LOT_ID = "sourceMaterialLotId";
    public static final String FIELD_REASON = "reason";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_QTY = "trxQty";
    public static final String FIELD_SOURCE_TRX_QTY = "sourceTrxQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -1510357165755246175L;

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
    @ApiModelProperty("作为物料批变更更历史唯一标识，用于其他数据结构引用")
    @Id
    @Where
    private String materialLotChangeHistoryId;
    @ApiModelProperty(value = "表示发生拆分合并变更后的物料批唯一标识ID", required = true)
    @NotBlank
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "物料批历史ID", hidden = true)
    @NotBlank
    @Where
    private String materialLotHisId;
    @ApiModelProperty(value = "作为一次变更来源物料批，如拆分合并的来源物料批", required = true)
    @NotBlank
    @Where
    private String sourceMaterialLotId;
    @ApiModelProperty(value = "表示发生此次变更的原因：P：拆分;M：合并，合并时可能记录多条记录", required = true)
    @NotBlank
    @Where
    private String reason;
    @ApiModelProperty(value = "物料批存在多次变更记录，此处记录物料批变更顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "事务ID，用于表示一次变更操作", required = true)
    @NotBlank
    @Where
    private String eventId;

    @ApiModelProperty(value = "变更数量", required = true)
    @NotNull
    @Where
    private Double trxQty;

    @ApiModelProperty(value = "来源执行作业变更数量", required = true)
    @NotNull
    @Where
    private Double sourceTrxQty;
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
     * @return 作为物料批变更更历史唯一标识，用于其他数据结构引用
     */
    public String getMaterialLotChangeHistoryId() {
        return materialLotChangeHistoryId;
    }

    public void setMaterialLotChangeHistoryId(String materialLotChangeHistoryId) {
        this.materialLotChangeHistoryId = materialLotChangeHistoryId;
    }

    /**
     * @return 表示发生拆分合并变更后的物料批唯一标识ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 作为一次变更来源物料批，如拆分合并的来源物料批
     */
    public String getSourceMaterialLotId() {
        return sourceMaterialLotId;
    }

    public void setSourceMaterialLotId(String sourceMaterialLotId) {
        this.sourceMaterialLotId = sourceMaterialLotId;
    }

    /**
     * @return 表示发生此次变更的原因：P：拆分;M：合并，合并时可能记录多条记录
     */
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return 物料批存在多次变更记录，此处记录物料批变更顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 事务ID，用于表示一次变更操作
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
     * @return 来源执行作业变更数量
     */
    public Double getSourceTrxQty() {
        return sourceTrxQty;
    }

    public void setSourceTrxQty(Double sourceTrxQty) {
        this.sourceTrxQty = sourceTrxQty;
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

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }
}
