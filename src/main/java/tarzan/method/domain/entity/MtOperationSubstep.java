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
 * 工艺子步骤
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@ApiModel("工艺子步骤")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_operation_substep")
@CustomPrimary
public class MtOperationSubstep extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_SUBSTEP_ID = "operationSubstepId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_SUBSTEP_ID = "substepId";
    public static final String FIELD_SEQUENCE = "sequence";
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
    @ApiModelProperty("工艺子步骤关系唯一标识")
    @Id
    @Where
    private String operationSubstepId;
    @ApiModelProperty(value = "工艺标识", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "子步骤标识", required = true)
    @NotBlank
    @Where
    private String substepId;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
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
     * @return 工艺子步骤关系唯一标识
     */
    public String getOperationSubstepId() {
        return operationSubstepId;
    }

    public void setOperationSubstepId(String operationSubstepId) {
        this.operationSubstepId = operationSubstepId;
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
     * @return 子步骤标识
     */
    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
