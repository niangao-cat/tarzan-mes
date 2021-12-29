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
 * 完成步骤历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("完成步骤历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_done_step_his")
@CustomPrimary
public class MtRouterDoneStepHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_DONE_STEP_HIS_ID = "routerDoneStepHisId";
    public static final String FIELD_ROUTER_DONE_STEP_ID = "routerDoneStepId";
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
    @ApiModelProperty("完成步骤历史唯一标识")
    @Id
    @Where
    private String routerDoneStepHisId;
    @ApiModelProperty(value = "完成步骤唯一标识", required = true)
    @NotBlank
    @Where
    private String routerDoneStepId;
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
     * @return 完成步骤历史唯一标识
     */
    public String getRouterDoneStepHisId() {
        return routerDoneStepHisId;
    }

    public void setRouterDoneStepHisId(String routerDoneStepHisId) {
        this.routerDoneStepHisId = routerDoneStepHisId;
    }

    /**
     * @return 完成步骤唯一标识
     */
    public String getRouterDoneStepId() {
        return routerDoneStepId;
    }

    public void setRouterDoneStepId(String routerDoneStepId) {
        this.routerDoneStepId = routerDoneStepId;
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
