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
 * 执行作业-工艺路线步骤执行明细历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("执行作业-工艺路线步骤执行明细历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_step_workcell_actual_his")
@CustomPrimary
public class MtEoStepWorkcellActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_STEP_WORKCELL_ACTUAL_HIS_ID = "eoStepWorkcellActualHisId";
    public static final String FIELD_EO_STEP_WORKCELL_ACTUAL_ID = "eoStepWorkcellActualId";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_QUEUE_QTY = "queueQty";
    public static final String FIELD_WORKING_QTY = "workingQty";
    public static final String FIELD_COMPLETE_PENDING_QTY = "completePendingQty";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_QUEUE_DATE = "queueDate";
    public static final String FIELD_WORKING_DATE = "workingDate";
    public static final String FIELD_COMPLETED_DATE = "completedDate";
    public static final String FIELD_COMPLETE_PENDING_DATE = "completePendingDate";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_QUEUE_QTY = "trxQueueQty";
    public static final String FIELD_TRX_WORKING_QTY = "trxWorkingQty";
    public static final String FIELD_TRX_COMPLETED_QTY = "trxCompletedQty";
    public static final String FIELD_TRX_SCRAPPED_QTY = "trxScrappedQty";
    public static final String FIELD_TRX_COMPLETE_PENDING_QTY = "trxCompletePendingQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 1451057319562782424L;

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
    @ApiModelProperty("主键ID，标识唯一一条历史记录")
    @Id
    @Where
    private String eoStepWorkcellActualHisId;
    @ApiModelProperty(value = "主键ID，标识唯一一条执行作业步骤明细实绩记录", required = true)
    @NotBlank
    @Where
    private String eoStepWorkcellActualId;
    @ApiModelProperty(value = "EO步骤实绩主键，表示唯一EO工艺路线步骤执行实绩", required = true)
    @NotBlank
    @Where
    private String eoStepActualId;
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
    @ApiModelProperty(value = "报废的数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
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
    @ApiModelProperty(value = "EO在此步骤的工作单元", required = true)
    @NotBlank
    @Where
    private String workcellId;
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
     * @return 主键ID，标识唯一一条历史记录
     */
    public String getEoStepWorkcellActualHisId() {
        return eoStepWorkcellActualHisId;
    }

    public void setEoStepWorkcellActualHisId(String eoStepWorkcellActualHisId) {
        this.eoStepWorkcellActualHisId = eoStepWorkcellActualHisId;
    }

    /**
     * @return 主键ID，标识唯一一条执行作业步骤明细实绩记录
     */
    public String getEoStepWorkcellActualId() {
        return eoStepWorkcellActualId;
    }

    public void setEoStepWorkcellActualId(String eoStepWorkcellActualId) {
        this.eoStepWorkcellActualId = eoStepWorkcellActualId;
    }

    /**
     * @return EO步骤实绩主键，表示唯一EO工艺路线步骤执行实绩
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
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
     * @return 报废的数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
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
     * @return EO在此步骤的工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
