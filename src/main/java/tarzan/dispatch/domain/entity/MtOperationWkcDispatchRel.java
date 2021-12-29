package tarzan.dispatch.domain.entity;

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
 * 工艺和工作单元调度关系表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
@ApiModel("工艺和工作单元调度关系表")
@ModifyAudit
@Table(name = "mt_operation_wkc_dispatch_rel")
@CustomPrimary
public class MtOperationWkcDispatchRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_WKC_DISPATCH_REL_ID = "operationWkcDispatchRelId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_STEP_NAME = "stepName";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_PRIORITY = "priority";
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
    @ApiModelProperty("关系ID，标识唯一一条数据")
    @Id
    @Where
    private String operationWkcDispatchRelId;
    @ApiModelProperty(value = "工艺ID，标识唯一工艺，表示关系所属工艺", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "步骤名称， 用于在工艺路线中多次出现同一标准工艺时区分唯一工艺")
    @Where
    private String stepName;
    @ApiModelProperty(value = "工作单元ID，标识在当前工艺和步骤下允许调度分派时选择的WKC", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "优先级，标识当前工艺步骤存在多个可选WKC时，推荐的使用顺序", required = true)
    @NotNull
    @Where
    private Long priority;
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
     * @return 关系ID，标识唯一一条数据
     */
    public String getOperationWkcDispatchRelId() {
        return operationWkcDispatchRelId;
    }

    public void setOperationWkcDispatchRelId(String operationWkcDispatchRelId) {
        this.operationWkcDispatchRelId = operationWkcDispatchRelId;
    }

    /**
     * @return 工艺ID，标识唯一工艺，表示关系所属工艺
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 步骤名称， 用于在工艺路线中多次出现同一标准工艺时区分唯一工艺
     */
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * @return 工作单元ID，标识在当前工艺和步骤下允许调度分派时选择的WKC
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 优先级，标识当前工艺步骤存在多个可选WKC时，推荐的使用顺序
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
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
