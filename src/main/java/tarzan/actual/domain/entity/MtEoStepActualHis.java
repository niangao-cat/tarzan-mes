package tarzan.actual.domain.entity;

import java.io.Serializable;
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
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 执行作业-工艺路线步骤执行实绩
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("执行作业-工艺路线步骤执行实绩")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_step_actual_his")
@CustomPrimary
public class MtEoStepActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_STEP_ACTUAL_HIS_ID = "eoStepActualHisId";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_EO_ROUTER_ACTUAL_ID = "eoRouterActualId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_STEP_NAME = "stepName";
    public static final String FIELD_QUEUE_QTY = "queueQty";
    public static final String FIELD_WORKING_QTY = "workingQty";
    public static final String FIELD_COMPLETE_PENDING_QTY = "completePendingQty";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_HOLD_QTY = "holdQty";
    public static final String FIELD_BYPASSED_FLAG = "bypassedFlag";
    public static final String FIELD_REWORK_STEP_FLAG = "reworkStepFlag";
    public static final String FIELD_LOCAL_REWORK_FLAG = "localReworkFlag";
    public static final String FIELD_MAX_PROCESS_TIMES = "maxProcessTimes";
    public static final String FIELD_TIMES_PROCESSED = "timesProcessed";
    public static final String FIELD_PREVIOUS_STEP_ID = "previousStepId";
    public static final String FIELD_QUEUE_DATE = "queueDate";
    public static final String FIELD_WORKING_DATE = "workingDate";
    public static final String FIELD_COMPLETED_DATE = "completedDate";
    public static final String FIELD_COMPLETE_PENDING_DATE = "completePendingDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_SPECIAL_INSTRUCTION = "specialInstruction";
    public static final String FIELD_HOLD_COUNT = "holdCount";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_QUEUE_QTY = "trxQueueQty";
    public static final String FIELD_TRX_WORKING_QTY = "trxWorkingQty";
    public static final String FIELD_TRX_COMPLETED_QTY = "trxCompletedQty";
    public static final String FIELD_TRX_SCRAPPED_QTY = "trxScrappedQty";
    public static final String FIELD_TRX_HOLD_QTY = "trxHoldQty";
    public static final String FIELD_TRX_COMPLETE_PENDING_QTY = "trxCompletePendingQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3318882441147196918L;

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
    private String eoStepActualHisId;
    @ApiModelProperty(value = "主表主键ID", required = true)
    @NotBlank
    @Where
    private String eoStepActualId;
    @ApiModelProperty(value = "EO工艺路线主键，表示唯一EO路径", required = true)
    @NotBlank
    @Where
    private String eoRouterActualId;
    @ApiModelProperty(value = "EO步骤实际执行顺序，生成时递增", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "步骤ID（对于特殊操作步骤ID就是操作ID）", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "标准作业（工艺）主键，表示唯一标准作业（工艺）", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "EO步骤名称", required = true)
    @NotBlank
    @Where
    private String stepName;
    @ApiModelProperty(value = "排队数量", required = true)
    @NotNull
    @Where
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量", required = true)
    @NotNull
    @Where
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存数量", required = true)
    @NotNull
    @Where
    private Double completePendingQty;
    @ApiModelProperty(value = "完成的数量", required = true)
    @NotNull
    @Where
    private Double completedQty;
    @ApiModelProperty(value = "报废数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
    @ApiModelProperty(value = "保留数量", required = true)
    @NotNull
    @Where
    private Double holdQty;
    @ApiModelProperty(value = "步骤是否被跳过标识")
    @Where
    private String bypassedFlag;
    @ApiModelProperty(value = "是否用于返工")
    @Where
    private String reworkStepFlag;
    @ApiModelProperty(value = "是否为此路径的返工步骤")
    @Where
    private String localReworkFlag;
    @ApiModelProperty(value = "步骤最大允许加工次数（0意味着没有限制）")
    @Where
    private Long maxProcessTimes;
    @ApiModelProperty(value = "已加工次数（EO已通过该步骤的次数）")
    @Where
    private Long timesProcessed;
    @ApiModelProperty(value = "前道完工步骤（用于控制步骤间的移动，步骤排队数量的来源）")
    @Where
    private String previousStepId;
    @ApiModelProperty(value = "EO最近一次置于排队的时间")
    @Where
    private Date queueDate;
    @ApiModelProperty(value = "EO最近一次置于运行的时间")
    @Where
    private Date workingDate;
    @ApiModelProperty(value = "EO最近一次置于完成的时间")
    @Where
    private Date completedDate;
    @ApiModelProperty(value = "EO最近一次置于完成暂存的时间")
    @Where
    private Date completePendingDate;
    @ApiModelProperty(value = "执行作业在工艺路线步骤上的状态，包括：")
    @Where
    private String status;
    @ApiModelProperty(value = "特殊操作说明")
    @Where
    private String specialInstruction;
    @ApiModelProperty(value = "步骤预留次数，每次步骤增加预留时增加")
    @Where
    private Long holdCount;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "事务数量-排队数量")
    @Where
    private Double trxQueueQty;
    @ApiModelProperty(value = "事务数量-正在加工的数量")
    @Where
    private Double trxWorkingQty;
    @ApiModelProperty(value = "事务数量-完成的数量")
    @Where
    private Double trxCompletedQty;
    @ApiModelProperty(value = "事务数量-报废数量")
    @Where
    private Double trxScrappedQty;
    @ApiModelProperty(value = "事务数量-保留数量")
    @Where
    private Double trxHoldQty;
    @ApiModelProperty(value = "事务数量-完成暂存数量")
    @Where
    private Double trxCompletePendingQty;
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
    public String getEoStepActualHisId() {
        return eoStepActualHisId;
    }

    public void setEoStepActualHisId(String eoStepActualHisId) {
        this.eoStepActualHisId = eoStepActualHisId;
    }

    /**
     * @return 主表主键ID
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    /**
     * @return EO工艺路线主键，表示唯一EO路径
     */
    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    /**
     * @return EO步骤实际执行顺序，生成时递增
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 步骤ID（对于特殊操作步骤ID就是操作ID）
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 标准作业（工艺）主键，表示唯一标准作业（工艺）
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return EO步骤名称
     */
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * @return 排队数量
     */
    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    /**
     * @return 正在加工的数量
     */
    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    /**
     * @return 完成暂存数量
     */
    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    /**
     * @return 完成的数量
     */
    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    /**
     * @return 报废数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    /**
     * @return 保留数量
     */
    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    /**
     * @return 步骤是否被跳过标识
     */
    public String getBypassedFlag() {
        return bypassedFlag;
    }

    public void setBypassedFlag(String bypassedFlag) {
        this.bypassedFlag = bypassedFlag;
    }

    /**
     * @return 是否用于返工
     */
    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    /**
     * @return 是否为此路径的返工步骤
     */
    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    /**
     * @return 步骤最大允许加工次数（0意味着没有限制）
     */
    public Long getMaxProcessTimes() {
        return maxProcessTimes;
    }

    public void setMaxProcessTimes(Long maxProcessTimes) {
        this.maxProcessTimes = maxProcessTimes;
    }

    /**
     * @return 已加工次数（EO已通过该步骤的次数）
     */
    public Long getTimesProcessed() {
        return timesProcessed;
    }

    public void setTimesProcessed(Long timesProcessed) {
        this.timesProcessed = timesProcessed;
    }

    /**
     * @return 前道完工步骤（用于控制步骤间的移动，步骤排队数量的来源）
     */
    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    /**
     * @return EO最近一次置于排队的时间
     */


    public Date getQueueDate() {
        if (queueDate != null) {
            return (Date) queueDate.clone();
        } else {
            return null;
        }
    }

    public void setQueueDate(Date queueDate) {
        if (queueDate == null) {
            this.queueDate = null;
        } else {
            this.queueDate = (Date) queueDate.clone();
        }
    }

    public Date getWorkingDate() {
        if (workingDate != null) {
            return (Date) workingDate.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDate(Date workingDate) {
        if (workingDate == null) {
            this.workingDate = null;
        } else {
            this.workingDate = (Date) workingDate.clone();
        }
    }

    public Date getCompletedDate() {
        if (completedDate != null) {
            return (Date) completedDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDate(Date completedDate) {
        if (completedDate == null) {
            this.completedDate = null;
        } else {
            this.completedDate = (Date) completedDate.clone();
        }
    }

    public Date getCompletePendingDate() {
        if (completePendingDate != null) {
            return (Date) completePendingDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDate(Date completePendingDate) {
        if (completePendingDate == null) {
            this.completePendingDate = null;
        } else {
            this.completePendingDate = (Date) completePendingDate.clone();
        }
    }

    /**
     * @return 执行作业在工艺路线步骤上的状态，包括：
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 特殊操作说明
     */
    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    /**
     * @return 步骤预留次数，每次步骤增加预留时增加
     */
    public Long getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(Long holdCount) {
        this.holdCount = holdCount;
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
     * @return 事务数量-排队数量
     */
    public Double getTrxQueueQty() {
        return trxQueueQty;
    }

    public void setTrxQueueQty(Double trxQueueQty) {
        this.trxQueueQty = trxQueueQty;
    }

    /**
     * @return 事务数量-正在加工的数量
     */
    public Double getTrxWorkingQty() {
        return trxWorkingQty;
    }

    public void setTrxWorkingQty(Double trxWorkingQty) {
        this.trxWorkingQty = trxWorkingQty;
    }

    /**
     * @return 事务数量-完成的数量
     */
    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
    }

    /**
     * @return 事务数量-报废数量
     */
    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
    }

    /**
     * @return 事务数量-保留数量
     */
    public Double getTrxHoldQty() {
        return trxHoldQty;
    }

    public void setTrxHoldQty(Double trxHoldQty) {
        this.trxHoldQty = trxHoldQty;
    }

    /**
     * @return 事务数量-完成暂存数量
     */
    public Double getTrxCompletePendingQty() {
        return trxCompletePendingQty;
    }

    public void setTrxCompletePendingQty(Double trxCompletePendingQty) {
        this.trxCompletePendingQty = trxCompletePendingQty;
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
