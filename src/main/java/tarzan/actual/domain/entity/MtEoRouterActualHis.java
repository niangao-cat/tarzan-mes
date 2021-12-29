package tarzan.actual.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * EO工艺路线实绩历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("EO工艺路线实绩历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_router_actual_his")
@CustomPrimary
public class MtEoRouterActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_ROUTER_ACTUAL_HIS_ID = "eoRouterActualHisId";
    public static final String FIELD_EO_ROUTER_ACTUAL_ID = "eoRouterActualId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_SUB_ROUTER_FLAG = "subRouterFlag";
    public static final String FIELD_SOURCE_EO_STEP_ACTUAL_ID = "sourceEoStepActualId";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_QTY = "trxQty";
    public static final String FIELD_TRX_COMPLETED_QTY = "trxCompletedQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6388679959339333942L;

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
    private String eoRouterActualHisId;
    @ApiModelProperty(value = "主键ID，标识唯一一条执行作业工艺路线实绩记录", required = true)
    @NotBlank
    @Where
    private String eoRouterActualId;
    @ApiModelProperty(value = "EO主键，标识唯一EO", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "顺序(使用路径的顺序）", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "工艺路线ID", required = true)
    @NotBlank
    @Where
    private String routerId;
    @ApiModelProperty(value = "状态（值集：未开始、运行中（排队）、部分完成、已完成）", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "此工艺路线加工数量（即进入首工序排队的数量） ", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "是否为分支工艺路线标识")
    @Where
    private String subRouterFlag;
    @ApiModelProperty(value = "来源实绩步骤")
    @Where
    private String sourceEoStepActualId;
    @ApiModelProperty(value = "此工艺路线已完成数量")
    @Where
    private Double completedQty;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "事务数量", required = true)
    @NotNull
    @Where
    private Double trxQty;
    @ApiModelProperty(value = "完成事务数量")
    @Where
    private Double trxCompletedQty;
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
    public String getEoRouterActualHisId() {
        return eoRouterActualHisId;
    }

    public void setEoRouterActualHisId(String eoRouterActualHisId) {
        this.eoRouterActualHisId = eoRouterActualHisId;
    }

    /**
     * @return 主键ID，标识唯一一条执行作业工艺路线实绩记录
     */
    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    /**
     * @return EO主键，标识唯一EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 顺序(使用路径的顺序）
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 工艺路线ID
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 状态（值集：未开始、运行中（排队）、部分完成、已完成）
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 此工艺路线加工数量（即进入首工序排队的数量）
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 是否为分支工艺路线标识
     */
    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    /**
     * @return 来源实绩步骤
     */
    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    /**
     * @return 此工艺路线已完成数量
     */
    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return 事务数量
     */
    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    /**
     * @return 完成事务数量
     */
    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
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
