package tarzan.order.domain.entity;

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
 * EO工艺路线历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@ApiModel("EO工艺路线历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_router_his")
@CustomPrimary
public class MtEoRouterHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_ROUTER_HIS_ID = "eoRouterHisId";
    public static final String FIELD_EO_ROUTER_ID = "eoRouterId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 8353487262693306454L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String eoRouterHisId;
    @ApiModelProperty(value = "执行作业工艺路线ID", required = true)
    @NotBlank
    @Where
    private String eoRouterId;
    @ApiModelProperty(value = "工艺路线ID", required = true)
    @NotBlank
    @Where
    private String routerId;
    @ApiModelProperty(value = "EO主键，标识唯一EO", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "事件ID，关联唯一事件以获取同时影响的其他所有对象", required = true)
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getEoRouterHisId() {
        return eoRouterHisId;
    }

    public void setEoRouterHisId(String eoRouterHisId) {
        this.eoRouterHisId = eoRouterHisId;
    }

    /**
     * @return 执行作业工艺路线ID
     */
    public String getEoRouterId() {
        return eoRouterId;
    }

    public void setEoRouterId(String eoRouterId) {
        this.eoRouterId = eoRouterId;
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
     * @return EO主键，标识唯一EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 事件ID，关联唯一事件以获取同时影响的其他所有对象
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
