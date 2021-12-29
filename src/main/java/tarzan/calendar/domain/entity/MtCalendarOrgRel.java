package tarzan.calendar.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 日历组织关系表
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
@ApiModel("日历组织关系表")
@ModifyAudit

@Table(name = "mt_calendar_org_rel")
@CustomPrimary
public class MtCalendarOrgRel extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CALENDAR_ORG_REL_ID = "calendarOrgRelId";
    public static final String FIELD_CALENDAR_ID = "calendarId";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
	private static final long serialVersionUID = 7147898215850899662L;

	//
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("作为日历组织关系唯一标识，主键")
    @Id
    @Where
    private String calendarOrgRelId;
    @ApiModelProperty(value = "作为日历唯一标识，用于其他数据结构引用",required = true)
    @NotBlank
    @Where
    private String calendarId;
    @ApiModelProperty(value = "对应的组织代码ID",required = true)
    @NotBlank
    @Where
    private String organizationId;
    @ApiModelProperty(value = "如SITE\\AREA\\PRODUCTIONLINE等，TYPE_GROUP:CALENDAR_ORG_TYPE",required = true)
    @NotBlank
    @Where
    private String organizationType;
    @ApiModelProperty(value = "是否有效。默认为“N”",required = true)
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
     * @return 作为日历组织关系唯一标识，主键
     */
	public String getCalendarOrgRelId() {
		return calendarOrgRelId;
	}

	public void setCalendarOrgRelId(String calendarOrgRelId) {
		this.calendarOrgRelId = calendarOrgRelId;
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
     * @return 对应的组织代码ID
     */
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
    /**
     * @return 如SITE\\AREA\\PRODUCTIONLINE等，TYPE_GROUP:CALENDAR_ORG_TYPE
     */
	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
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
