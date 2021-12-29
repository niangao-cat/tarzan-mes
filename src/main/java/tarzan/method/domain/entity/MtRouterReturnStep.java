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
 * 返回步骤
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("返回步骤")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_return_step")
@CustomPrimary
public class MtRouterReturnStep extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_RETURN_STEP_ID = "routerReturnStepId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_RETURN_TYPE = "returnType";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_COMPLETE_ORIGINAL_FLAG = "completeOriginalFlag";
    public static final String FIELD_STEP_NAME = "stepName";
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
    @ApiModelProperty("返回步骤唯一标识")
    @Id
    @Where
    private String routerReturnStepId;
    @ApiModelProperty(value = "工艺路线步骤标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "返回类型", required = true)
    @NotBlank
    @Where
    private String returnType;
    @ApiModelProperty(value = "返回原工艺路线的工艺")
    @Where
    private String operationId;
    @ApiModelProperty(value = "原工艺是否完成标识")
    @Where
    private String completeOriginalFlag;
    @ApiModelProperty(value = "步骤识别码")
    @Where
    private String stepName;
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
     * @return 返回步骤唯一标识
     */
    public String getRouterReturnStepId() {
        return routerReturnStepId;
    }

    public void setRouterReturnStepId(String routerReturnStepId) {
        this.routerReturnStepId = routerReturnStepId;
    }

    /**
     * @return 工艺路线步骤标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 返回类型
     */
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * @return 返回原工艺路线的工艺
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 原工艺是否完成标识
     */
    public String getCompleteOriginalFlag() {
        return completeOriginalFlag;
    }

    public void setCompleteOriginalFlag(String completeOriginalFlag) {
        this.completeOriginalFlag = completeOriginalFlag;
    }

    /**
     * @return 步骤识别码
     */
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
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
