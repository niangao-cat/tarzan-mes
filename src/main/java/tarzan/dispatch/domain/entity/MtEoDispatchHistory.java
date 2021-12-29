package tarzan.dispatch.domain.entity;

import java.util.Date;

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
 * 调度历史表，记录历史发布的调度结果和版本
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
@ApiModel("调度历史表，记录历史发布的调度结果和版本")
@ModifyAudit
@Table(name = "mt_eo_dispatch_history")
@CustomPrimary
public class MtEoDispatchHistory extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_DISPATCH_HISTORY_ID = "eoDispatchHistoryId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_PLAN_START_TIME = "planStartTime";
    public static final String FIELD_PLAN_END_TIME = "planEndTime";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_ASSIGN_QTY = "assignQty";
    public static final String FIELD_REVISION = "revision";
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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String eoDispatchHistoryId;
    @ApiModelProperty(value = "执行作业主键，标识该步骤分配对应的执行作业结构", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "EO工艺路线步骤主键，用于标识唯一EO工艺路线步骤实绩", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "状态", required = true)
    @Where
    private String status;
    @ApiModelProperty(value = "步骤执行工作单元，工作单元主键，标识唯一工作单元", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "执行顺序", required = true)
    @NotNull
    @Where
    private Long priority;
    @ApiModelProperty(value = "调度计划开始时间")
    @Where
    private Date planStartTime;
    @ApiModelProperty(value = "调度计划结束时间")
    @Where
    private Date planEndTime;
    @ApiModelProperty(value = "日历日期", required = true)
    @NotNull
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次", required = true)
    @NotBlank
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "本次分配数量（分到不同的WKC）", required = true)
    @NotNull
    @Where
    private Double assignQty;
    @ApiModelProperty(value = "版本", required = true)
    @NotNull
    @Where
    private Long revision;
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
    public String getEoDispatchHistoryId() {
        return eoDispatchHistoryId;
    }

    public void setEoDispatchHistoryId(String eoDispatchHistoryId) {
        this.eoDispatchHistoryId = eoDispatchHistoryId;
    }

    /**
     * @return 执行作业主键，标识该步骤分配对应的执行作业结构
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return EO工艺路线步骤主键，用于标识唯一EO工艺路线步骤实绩
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工艺ID
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 步骤执行工作单元，工作单元主键，标识唯一工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 执行顺序
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    /**
     * @return 调度计划开始时间
     */

    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
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
     * @return 本次分配数量（分到不同的WKC）
     */
    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    /**
     * @return 版本
     */
    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
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
