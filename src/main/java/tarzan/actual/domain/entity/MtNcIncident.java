package tarzan.actual.domain.entity;

import java.io.Serializable;
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
 * 不良事故
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
@ApiModel("不良事故")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_nc_incident")
@CustomPrimary
public class MtNcIncident extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_INCIDENT_ID = "ncIncidentId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_INCIDENT_NUMBER = "incidentNumber";
    public static final String FIELD_NC_INCIDENT_STATUS = "ncIncidentStatus";
    public static final String FIELD_INCIDENT_DATE_TIME = "incidentDateTime";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 2846757489440546450L;

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
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @Where
    private String ncIncidentId;
    @ApiModelProperty(value = "生产站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "不良事故编码", required = true)
    @NotBlank
    @Where
    private String incidentNumber;
    @ApiModelProperty(value = "不良事故状态：", required = true)
    @NotBlank
    @Where
    private String ncIncidentStatus;
    @ApiModelProperty(value = "事故发生日期", required = true)
    @NotNull
    @Where
    private Date incidentDateTime;
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
     * @return 表ID，主键，供其他表做外键
     */
    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    /**
     * @return 生产站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 不良事故编码
     */
    public String getIncidentNumber() {
        return incidentNumber;
    }

    public void setIncidentNumber(String incidentNumber) {
        this.incidentNumber = incidentNumber;
    }

    /**
     * @return 不良事故状态：
     */
    public String getNcIncidentStatus() {
        return ncIncidentStatus;
    }

    public void setNcIncidentStatus(String ncIncidentStatus) {
        this.ncIncidentStatus = ncIncidentStatus;
    }

    /**
     * @return 事故发生日期
     */
    public Date getIncidentDateTime() {
        if (incidentDateTime != null) {
            return (Date) incidentDateTime.clone();
        } else {
            return null;
        }
    }

    public void setIncidentDateTime(Date incidentDateTime) {
        if (incidentDateTime == null) {
            this.incidentDateTime = null;
        } else {
            this.incidentDateTime = (Date) incidentDateTime.clone();
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
