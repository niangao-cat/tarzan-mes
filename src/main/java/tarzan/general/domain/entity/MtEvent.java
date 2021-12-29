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
 * 事件记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@ApiModel("事件记录")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_event")
@CustomPrimary
public class MtEvent extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_EVENT_TYPE_ID = "eventTypeId";
    public static final String FIELD_EVENT_BY = "eventBy";
    public static final String FIELD_EVENT_TIME = "eventTime";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_PARENT_EVENT_ID = "parentEventId";
    public static final String FIELD_EVENT_REQUEST_ID = "eventRequestId";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
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
    @ApiModelProperty("事件ID，唯一性标识")
    @Id
    @Where
    private String eventId;
    @ApiModelProperty(value = "事件类型ID", required = true)
    @NotBlank
    @Where
    private String eventTypeId;
    @ApiModelProperty(value = "事件记录创建人", required = true)
    @NotNull
    @Where
    private Long eventBy;
    @ApiModelProperty(value = "事件记录创建时间")
    @Where
    private Date eventTime;
    @ApiModelProperty(value = "事件发生WKC ID")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "事件发生库位ID")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "父事件ID")
    @Where
    private String parentEventId;
    @ApiModelProperty(value = "事件组ID")
    @Where
    private String eventRequestId;
    @ApiModelProperty(value = "事件所属班次日期")
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "事件班次代码")
    @Where
    private String shiftCode;
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
     * @return 事件ID，唯一性标识
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return 事件类型ID
     */
    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    /**
     * @return 事件记录创建人
     */
    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    /**
     * @return 事件记录创建时间
     */
    public Date getEventTime() {
        if (eventTime != null) {
            return (Date) eventTime.clone();
        } else {
            return null;
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    /**
     * @return 事件发生WKC ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 事件发生库位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 父事件ID
     */
    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    /**
     * @return 事件组ID
     */
    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    /**
     * @return 事件所属班次日期
     */
    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    /**
     * @return 事件班次代码
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
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
