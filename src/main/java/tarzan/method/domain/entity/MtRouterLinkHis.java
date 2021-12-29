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
 * 嵌套工艺路线步骤历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("嵌套工艺路线步骤历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_link_his")
@CustomPrimary
public class MtRouterLinkHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_LINK_HIS_ID = "routerLinkHisId";
    public static final String FIELD_ROUTER_LINK_ID = "routerLinkId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_ROUTER_ID = "routerId";
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
    private String routerLinkHisId;
    @ApiModelProperty(value = "主表主键ID", required = true)
    @NotBlank
    @Where
    private String routerLinkId;
    @ApiModelProperty(value = "工艺路线步骤唯一标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工艺路线标识", required = true)
    @NotBlank
    @Where
    private String routerId;
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
    public String getRouterLinkHisId() {
        return routerLinkHisId;
    }

    public void setRouterLinkHisId(String routerLinkHisId) {
        this.routerLinkHisId = routerLinkHisId;
    }

    /**
     * @return 主表主键ID
     */
    public String getRouterLinkId() {
        return routerLinkId;
    }

    public void setRouterLinkId(String routerLinkId) {
        this.routerLinkId = routerLinkId;
    }

    /**
     * @return 工艺路线步骤唯一标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工艺路线标识
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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
