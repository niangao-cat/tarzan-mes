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
 * 保留实绩明细
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("保留实绩明细")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_hold_actual_detail")
@CustomPrimary
public class MtHoldActualDetail extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_HOLD_DETAIL_ID = "holdDetailId";
    public static final String FIELD_HOLD_ID = "holdId";
    public static final String FIELD_OBJECT_TYPE = "objectType";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_ORIGINAL_STATUS = "originalStatus";
    public static final String FIELD_FUTURE_HOLD_ROUTER_STEP_ID = "futureHoldRouterStepId";
    public static final String FIELD_FUTURE_HOLD_STATUS = "futureHoldStatus";
    public static final String FIELD_HOLD_EVENT_ID = "holdEventId";
    public static final String FIELD_RELEASE_FLAG = "releaseFlag";
    public static final String FIELD_RELEASE_COMMENT = "releaseComment";
    public static final String FIELD_RELEASE_TIME = "releaseTime";
    public static final String FIELD_RELEASE_BY = "releaseBy";
    public static final String FIELD_RELEASE_REASON_CODE = "releaseReasonCode";
    public static final String FIELD_RELEASE_EVENT_ID = "releaseEventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -4869823095459326855L;

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
    @ApiModelProperty("保留实绩明细ID，主键，供其他表做外键")
    @Id
    @Where
    private String holdDetailId;
    @ApiModelProperty(value = "保留实绩ID", required = true)
    @NotBlank
    @Where
    private String holdId;
    @ApiModelProperty(value = "保留对象类型，包括装配清单、工艺路线、WO、EO、装配组", required = true)
    @NotBlank
    @Where
    private String objectType;
    @ApiModelProperty(value = "保留对象值", required = true)
    @NotBlank
    @Where
    private String objectId;
    @ApiModelProperty(value = "保留所在的EO步骤实绩ID")
    @Where
    private String eoStepActualId;
    @ApiModelProperty(value = "保留前的初始状态", required = true)
    @NotBlank
    @Where
    private String originalStatus;
    @ApiModelProperty(value = "将来保留的工艺路线步骤ID")
    @Where
    private String futureHoldRouterStepId;
    @ApiModelProperty(value = "将来保留发生所在的步骤状态")
    @Where
    private String futureHoldStatus;
    @ApiModelProperty(value = "保留事件ID")
    @Where
    private String holdEventId;
    @ApiModelProperty(value = "保留是否释放的标识")
    @Where
    private String releaseFlag;
    @ApiModelProperty(value = "保留释放的备注")
    @Where
    private String releaseComment;
    @ApiModelProperty(value = "保留备注")
    @Where
    private Date releaseTime;
    @ApiModelProperty(value = "保留释放的人")
    @Where
    private Long releaseBy;
    @ApiModelProperty(value = "保留释放的原因代码")
    @Where
    private String releaseReasonCode;
    @ApiModelProperty(value = "保留释放事件ID")
    @Where
    private String releaseEventId;
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
     * @return 保留实绩明细ID，主键，供其他表做外键
     */
    public String getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(String holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

    /**
     * @return 保留实绩ID
     */
    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    /**
     * @return 保留对象类型，包括装配清单、工艺路线、WO、EO、装配组
     */
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * @return 保留对象值
     */
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return 保留所在的EO步骤实绩ID
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    /**
     * @return 保留前的初始状态
     */
    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    /**
     * @return 将来保留的工艺路线步骤ID
     */
    public String getFutureHoldRouterStepId() {
        return futureHoldRouterStepId;
    }

    public void setFutureHoldRouterStepId(String futureHoldRouterStepId) {
        this.futureHoldRouterStepId = futureHoldRouterStepId;
    }

    /**
     * @return 将来保留发生所在的步骤状态
     */
    public String getFutureHoldStatus() {
        return futureHoldStatus;
    }

    public void setFutureHoldStatus(String futureHoldStatus) {
        this.futureHoldStatus = futureHoldStatus;
    }

    /**
     * @return 保留事件ID
     */
    public String getHoldEventId() {
        return holdEventId;
    }

    public void setHoldEventId(String holdEventId) {
        this.holdEventId = holdEventId;
    }

    /**
     * @return 保留是否释放的标识
     */
    public String getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(String releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    /**
     * @return 保留释放的备注
     */
    public String getReleaseComment() {
        return releaseComment;
    }

    public void setReleaseComment(String releaseComment) {
        this.releaseComment = releaseComment;
    }

    /**
     * @return 保留备注
     */
    public Date getReleaseTime() {
        if (releaseTime != null) {
            return (Date) releaseTime.clone();
        } else {
            return null;
        }
    }

    public void setReleaseTime(Date releaseTime) {
        if (releaseTime == null) {
            this.releaseTime = null;
        } else {
            this.releaseTime = (Date) releaseTime.clone();
        }
    }

    /**
     * @return 保留释放的人
     */
    public Long getReleaseBy() {
        return releaseBy;
    }

    public void setReleaseBy(Long releaseBy) {
        this.releaseBy = releaseBy;
    }

    /**
     * @return 保留释放的原因代码
     */
    public String getReleaseReasonCode() {
        return releaseReasonCode;
    }

    public void setReleaseReasonCode(String releaseReasonCode) {
        this.releaseReasonCode = releaseReasonCode;
    }

    /**
     * @return 保留释放事件ID
     */
    public String getReleaseEventId() {
        return releaseEventId;
    }

    public void setReleaseEventId(String releaseEventId) {
        this.releaseEventId = releaseEventId;
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
