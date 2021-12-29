package tarzan.calendar.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工作日历
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@ApiModel("工作日历")

@ModifyAudit

@Table(name = "mt_calendar")
@MultiLanguage
@CustomPrimary
public class MtCalendar extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CALENDAR_ID = "calendarId";
    public static final String FIELD_CALENDAR_CODE = "calendarCode";
    public static final String FIELD_CALENDAR_TYPE = "calendarType";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    @ApiModelProperty("作为日历唯一标识，用于其他数据结构引用")
    @Id
    @Where
    private String calendarId;
    @ApiModelProperty(value = "该日历编码", required = true)
    @NotBlank
    @Where
    private String calendarCode;
    @ApiModelProperty(value = "TYPE_GROUP:CALENDAR_TYPE：1.【PLAN】 2.【STANDARD】3.【PURCHASE】", required = true)
    @NotBlank
    @Where
    private String calendarType;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否有效。默认为“N”", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
     * @return 作为日历唯一标识，用于其他数据结构引用
     */
    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * @return 该日历编码
     */
    public String getCalendarCode() {
        return calendarCode;
    }

    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    /**
     * @return TYPE_GROUP:CALENDAR_TYPE：1.【PLAN】 2.【STANDARD】3.【PURCHASE】
     */
    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 是否有效。默认为“N”
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
