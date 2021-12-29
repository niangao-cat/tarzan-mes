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
 * 保留实绩
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("保留实绩")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_hold_actual")
@CustomPrimary
public class MtHoldActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_HOLD_ID = "holdId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_HOLD_REASON_CODE = "holdReasonCode";
    public static final String FIELD_COMMENTS = "comments";
    public static final String FIELD_EXPIRED_RELEASE_TIME = "expiredReleaseTime";
    public static final String FIELD_HOLD_TYPE = "holdType";
    public static final String FIELD_HOLD_BY = "holdBy";
    public static final String FIELD_HOLD_TIME = "holdTime";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6475142843708168364L;

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
    @ApiModelProperty("保留ID，主键，供其他表做外键")
    @Id
    @Where
    private String holdId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "保留原因代码")
    @Where
    private String holdReasonCode;
    @ApiModelProperty(value = "保留备注")
    @Where
    private String comments;
    @ApiModelProperty(value = "保留备注")
    @Where
    private Date expiredReleaseTime;
    @ApiModelProperty(value = "保留类型（包括immediate, future)", required = true)
    @NotBlank
    @Where
    private String holdType;
    @ApiModelProperty(value = "保留人", required = true)
    @NotNull
    @Where
    private Long holdBy;
    @ApiModelProperty(value = "保留时间", required = true)
    @NotNull
    @Where
    private Date holdTime;
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
     * @return 保留ID，主键，供其他表做外键
     */
    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
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
     * @return 保留原因代码
     */
    public String getHoldReasonCode() {
        return holdReasonCode;
    }

    public void setHoldReasonCode(String holdReasonCode) {
        this.holdReasonCode = holdReasonCode;
    }

    /**
     * @return 保留备注
     */
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return 保留备注
     */
    public Date getExpiredReleaseTime() {
        if (expiredReleaseTime != null) {
            return (Date) expiredReleaseTime.clone();
        } else {
            return null;
        }
    }

    public void setExpiredReleaseTime(Date expiredReleaseTime) {
        if (expiredReleaseTime == null) {
            this.expiredReleaseTime = null;
        } else {
            this.expiredReleaseTime = (Date) expiredReleaseTime.clone();
        }
    }

    /**
     * @return 保留类型（包括immediate, future)
     */
    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    /**
     * @return 保留人
     */
    public Long getHoldBy() {
        return holdBy;
    }

    public void setHoldBy(Long holdBy) {
        this.holdBy = holdBy;
    }

    /**
     * @return 保留时间
     */

    public Date getHoldTime() {
        if (holdTime != null) {
            return (Date) holdTime.clone();
        } else {
            return null;
        }
    }

    public void setHoldTime(Date holdTime) {
        if (holdTime == null) {
            this.holdTime = null;
        } else {
            this.holdTime = (Date) holdTime.clone();
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
