package tarzan.general.domain.entity;

import java.util.Date;

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
 * 事件请求记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@ApiModel("事件请求记录")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_event_request")
@CustomPrimary
public class MtEventRequest extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_REQUEST_ID = "eventRequestId";
    public static final String FIELD_REQUEST_TYPE_ID = "requestTypeId";
    public static final String FIELD_REQUEST_BY = "requestBy";
    public static final String FIELD_REQUEST_TIME = "requestTime";
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
    @ApiModelProperty("事件请求记录ID")
    @Id
    @Where
    private String eventRequestId;
    @ApiModelProperty(value = "事件请求类型ID", required = true)
    @NotBlank
    @Where
    private String requestTypeId;
    @ApiModelProperty(value = "事件请求创建人", required = true)
    @NotNull
    @Where
    private Long requestBy;
    @ApiModelProperty(value = "事件请求创建时间", required = true)
    @NotNull
    @Where
    private Date requestTime;
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
     * @return 事件请求记录ID
     */
    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    /**
     * @return 事件请求类型ID
     */
    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    /**
     * @return 事件请求创建人
     */
    public Long getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    /**
     * @return 事件请求创建时间
     */
    public Date getRequestTime() {
        if (requestTime != null) {
            return (Date) requestTime.clone();
        } else {
            return null;
        }
    }

    public void setRequestTime(Date requestTime) {
        if (requestTime == null) {
            this.requestTime = null;
        } else {
            this.requestTime = (Date) requestTime.clone();
        }
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
