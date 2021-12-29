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
 * 子步骤组件
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@ApiModel("子步骤组件")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_substep_component")
@CustomPrimary
public class MtRouterSubstepComponent extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_SUBSTEP_COMPONENT_ID = "routerSubstepComponentId";
    public static final String FIELD_ROUTER_SUBSTEP_ID = "routerSubstepId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_QTY = "qty";
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
    @ApiModelProperty("工艺路线子步骤组件标识")
    @Id
    @Where
    private String routerSubstepComponentId;
    @ApiModelProperty(value = "工艺路线子步骤标识", required = true)
    @NotBlank
    @Where
    private String routerSubstepId;
    @ApiModelProperty(value = "BOM组件标识", required = true)
    @NotBlank
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "子步骤顺序")
    @Where
    private Long sequence;
    @ApiModelProperty(value = "组件使用数量", required = true)
    @NotNull
    @Where
    private Double qty;
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
     * @return 工艺路线子步骤组件标识
     */
    public String getRouterSubstepComponentId() {
        return routerSubstepComponentId;
    }

    public void setRouterSubstepComponentId(String routerSubstepComponentId) {
        this.routerSubstepComponentId = routerSubstepComponentId;
    }

    /**
     * @return 工艺路线子步骤标识
     */
    public String getRouterSubstepId() {
        return routerSubstepId;
    }

    public void setRouterSubstepId(String routerSubstepId) {
        this.routerSubstepId = routerSubstepId;
    }

    /**
     * @return BOM组件标识
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 子步骤顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 组件使用数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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
