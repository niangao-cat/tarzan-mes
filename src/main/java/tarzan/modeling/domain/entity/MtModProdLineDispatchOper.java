package tarzan.modeling.domain.entity;

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
 * 生产线调度指定工艺
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("生产线调度指定工艺")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_prod_line_dispatch_oper")
@CustomPrimary
public class MtModProdLineDispatchOper extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISPATCH_OPERATION_ID = "dispatchOperationId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6930786662435242997L;

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
    @ApiModelProperty("生产线调度指定工艺ID")
    @Id
    @Where
    private String dispatchOperationId;
    @ApiModelProperty(value = "生产线ID", required = true)
    @NotBlank
    @Where
    private String prodLineId;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    @Where
    private String operationId;
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
     * @return 生产线调度指定工艺ID
     */
    public String getDispatchOperationId() {
        return dispatchOperationId;
    }

    public void setDispatchOperationId(String dispatchOperationId) {
        this.dispatchOperationId = dispatchOperationId;
    }

    /**
     * @return 生产线ID
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
