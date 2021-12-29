package tarzan.dispatch.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Date;
import io.choerodon.mybatis.annotation.*;
import org.hzero.mybatis.common.query.Where;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调度执行实绩表
 *
 * @author xiao.tang02@hand-china.com 2020-02-25 11:50:47
 */
@ApiModel("调度执行实绩")
@ModifyAudit
@Table(name = "mt_eo_dispatch_execute_actual")
@CustomPrimary
public class MtEoDispatchExecuteActual extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_DISPATCH_EXECUTE_ACTUAL_ID = "eoDispatchExecuteActualId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_ASSIGN_QTY = "assignQty";
    public static final String FIELD_QUEUE_QTY = "queueQty";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录",required = true)
    @NotBlank
    @Where
    private String eoDispatchExecuteActualId;
    @ApiModelProperty("执行作业主键")
    @Id
    @Where
    private String eoId;
    @ApiModelProperty(value = "步骤主键",required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "步骤执行工作单元，工作单元主键，标识唯一工作单元",required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "日期",required = true)
    @NotNull
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次",required = true)
    @NotBlank
    @Where
    private String shiftCode;
   @ApiModelProperty(value = "优先级")    
    @Where
    private Long priority;
   @ApiModelProperty(value = "调度数量")    
    @Where
    private Double assignQty;
   @ApiModelProperty(value = "排队数量")    
    @Where
    private Double queueQty;
   @ApiModelProperty(value = "完成数量")    
    @Where
    private Double completedQty;
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
	public String getEoDispatchExecuteActualId() {
		return eoDispatchExecuteActualId;
	}

	public void setEoDispatchExecuteActualId(String eoDispatchExecuteActualId) {
		this.eoDispatchExecuteActualId = eoDispatchExecuteActualId;
	}
    /**
     * @return 执行作业主键
     */
	public String getEoId() {
		return eoId;
	}

	public void setEoId(String eoId) {
		this.eoId = eoId;
	}
    /**
     * @return 步骤主键
     */
	public String getRouterStepId() {
		return routerStepId;
	}

	public void setRouterStepId(String routerStepId) {
		this.routerStepId = routerStepId;
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
     * @return 日期
     */
	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
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
     * @return 优先级
     */
	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}
    /**
     * @return 调度数量
     */
	public Double getAssignQty() {
		return assignQty;
	}

	public void setAssignQty(Double assignQty) {
		this.assignQty = assignQty;
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
     * @return 完成数量
     */
	public Double getCompletedQty() {
		return completedQty;
	}

	public void setCompletedQty(Double completedQty) {
		this.completedQty = completedQty;
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
