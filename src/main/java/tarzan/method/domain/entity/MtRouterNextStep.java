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
 * 下一步骤
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@ApiModel("下一步骤")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_next_step")
@CustomPrimary
public class MtRouterNextStep extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_NEXT_STEP_ID = "routerNextStepId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_NEXT_STEP_ID = "nextStepId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_NEXT_DECISION_TYPE = "nextDecisionType";
    public static final String FIELD_NEXT_DECISION_VALUE = "nextDecisionValue";
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
    @ApiModelProperty("步骤关系唯一标识")
    @Id
    @Where
    private String routerNextStepId;
    @ApiModelProperty(value = "当前步骤标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "下一步骤标识", required = true)
    @NotBlank
    @Where
    private String nextStepId;
    @ApiModelProperty(value = "顺序")
    @Where
    private Long sequence;
    @ApiModelProperty(value = "选择策略类型", required = true)
    @NotBlank
    @Where
    private String nextDecisionType;
    @ApiModelProperty(value = "选择策略对应值(MAIN时无值，PRODUCT时为物料ID，NC时为编码)")
    @Where
    private String nextDecisionValue;
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
     * @return 步骤关系唯一标识
     */
    public String getRouterNextStepId() {
        return routerNextStepId;
    }

    public void setRouterNextStepId(String routerNextStepId) {
        this.routerNextStepId = routerNextStepId;
    }

    /**
     * @return 当前步骤标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 下一步骤标识
     */
    public String getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(String nextStepId) {
        this.nextStepId = nextStepId;
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
     * @return 选择策略类型
     */
    public String getNextDecisionType() {
        return nextDecisionType;
    }

    public void setNextDecisionType(String nextDecisionType) {
        this.nextDecisionType = nextDecisionType;
    }

    /**
     * @return 选择策略对应值(MAIN时无值，PRODUCT时为物料ID，NC时为编码)
     */
    public String getNextDecisionValue() {
        return nextDecisionValue;
    }

    public void setNextDecisionValue(String nextDecisionValue) {
        this.nextDecisionValue = nextDecisionValue;
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
