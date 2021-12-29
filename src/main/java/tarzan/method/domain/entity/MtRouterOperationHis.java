package tarzan.method.domain.entity;

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
 * 工艺路线步骤对应工序历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("工艺路线步骤对应工序历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_operation_his")
@CustomPrimary
public class MtRouterOperationHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_OPERATION_HIS_ID = "routerOperationHisId";
    public static final String FIELD_ROUTER_OPERATION_ID = "routerOperationId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_MAX_LOOP = "maxLoop";
    public static final String FIELD_SPECIAL_INTRUCTION = "specialIntruction";
    public static final String FIELD_REQUIRED_TIME_IN_PROCESS = "requiredTimeInProcess";
    public static final String FIELD_EVENT_ID = "eventId";
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
    @ApiModelProperty("历史表主键ID，唯一性标识")
    @Id
    @Where
    private String routerOperationHisId;
    @ApiModelProperty(value = "工艺路线步骤唯一标识", required = true)
    @NotBlank
    @Where
    private String routerOperationId;
    @ApiModelProperty(value = "工艺路线标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工艺标识", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "最大循环次数")
    @Where
    private Long maxLoop;
    @ApiModelProperty(value = "特殊指令，展示在前台的文本")
    @Where
    private String specialIntruction;
    @ApiModelProperty(value = "步骤处理所需时间（分钟）")
    @Where
    private Double requiredTimeInProcess;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 历史表主键ID，唯一性标识
     */
    public String getRouterOperationHisId() {
        return routerOperationHisId;
    }

    public void setRouterOperationHisId(String routerOperationHisId) {
        this.routerOperationHisId = routerOperationHisId;
    }

    /**
     * @return 工艺路线步骤唯一标识
     */
    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    /**
     * @return 工艺路线标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工艺标识
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 最大循环次数
     */
    public Long getMaxLoop() {
        return maxLoop;
    }

    public void setMaxLoop(Long maxLoop) {
        this.maxLoop = maxLoop;
    }

    /**
     * @return 特殊指令，展示在前台的文本
     */
    public String getSpecialIntruction() {
        return specialIntruction;
    }

    public void setSpecialIntruction(String specialIntruction) {
        this.specialIntruction = specialIntruction;
    }

    /**
     * @return 步骤处理所需时间（分钟）
     */
    public Double getRequiredTimeInProcess() {
        return requiredTimeInProcess;
    }

    public void setRequiredTimeInProcess(Double requiredTimeInProcess) {
        this.requiredTimeInProcess = requiredTimeInProcess;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
