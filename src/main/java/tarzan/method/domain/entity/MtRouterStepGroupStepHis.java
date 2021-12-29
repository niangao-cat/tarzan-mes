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
 * 工艺路线步骤组行步骤历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("工艺路线步骤组行步骤历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_step_group_step_his")
@CustomPrimary
public class MtRouterStepGroupStepHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_STEP_GROUP_STEP_HIS_ID = "routerStepGroupStepHisId";
    public static final String FIELD_ROUTER_STEP_GROUP_STEP_ID = "routerStepGroupStepId";
    public static final String FIELD_ROUTER_STEP_GROUP_ID = "routerStepGroupId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
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
    @ApiModelProperty("工艺路线步骤组分配历史唯一性标识")
    @Id
    @Where
    private String routerStepGroupStepHisId;
    @ApiModelProperty(value = "工艺路线步骤组分配唯一标识", required = true)
    @NotBlank
    @Where
    private String routerStepGroupStepId;
    @ApiModelProperty(value = "工艺路线步骤组标识", required = true)
    @NotBlank
    @Where
    private String routerStepGroupId;
    @ApiModelProperty(value = "顺序")
    @Where
    private Long sequence;
    @ApiModelProperty(value = "工艺路线步骤标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
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
     * @return 工艺路线步骤组分配历史唯一性标识
     */
    public String getRouterStepGroupStepHisId() {
        return routerStepGroupStepHisId;
    }

    public void setRouterStepGroupStepHisId(String routerStepGroupStepHisId) {
        this.routerStepGroupStepHisId = routerStepGroupStepHisId;
    }

    /**
     * @return 工艺路线步骤组分配唯一标识
     */
    public String getRouterStepGroupStepId() {
        return routerStepGroupStepId;
    }

    public void setRouterStepGroupStepId(String routerStepGroupStepId) {
        this.routerStepGroupStepId = routerStepGroupStepId;
    }

    /**
     * @return 工艺路线步骤组标识
     */
    public String getRouterStepGroupId() {
        return routerStepGroupId;
    }

    public void setRouterStepGroupId(String routerStepGroupId) {
        this.routerStepGroupId = routerStepGroupId;
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
     * @return 工艺路线步骤标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
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
