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
 * 工艺路线站点分配历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("工艺路线站点分配历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_site_assign_his")
@CustomPrimary
public class MtRouterSiteAssignHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_SITE_ASSIGN_HIS_ID = "routerSiteAssignHisId";
    public static final String FIELD_ROUTER_SITE_ASSIGN_ID = "routerSiteAssignId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String routerSiteAssignHisId;
    @ApiModelProperty(value = "工艺路线站点分配ID", required = true)
    @NotBlank
    @Where
    private String routerSiteAssignId;
    @ApiModelProperty(value = "工艺路线ID", required = true)
    @NotBlank
    @Where
    private String routerId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
     * @return 主键
     */
    public String getRouterSiteAssignHisId() {
        return routerSiteAssignHisId;
    }

    public void setRouterSiteAssignHisId(String routerSiteAssignHisId) {
        this.routerSiteAssignHisId = routerSiteAssignHisId;
    }

    /**
     * @return 工艺路线站点分配ID
     */
    public String getRouterSiteAssignId() {
        return routerSiteAssignId;
    }

    public void setRouterSiteAssignId(String routerSiteAssignId) {
        this.routerSiteAssignId = routerSiteAssignId;
    }

    /**
     * @return 工艺路线ID
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
