package tarzan.general.domain.entity;

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
 * 数据收集组历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@ApiModel("数据收集组历史表")

@ModifyAudit

@Table(name = "mt_tag_group_his")
@CustomPrimary
public class MtTagGroupHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_GROUP_HIS_ID = "tagGroupHisId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_GROUP_CODE = "tagGroupCode";
    public static final String FIELD_TAG_GROUP_DESCRIPTION = "tagGroupDescription";
    public static final String FIELD_TAG_GROUP_TYPE = "tagGroupType";
    public static final String FIELD_SOURCE_GROUP_ID = "sourceGroupId";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_COLLECTION_TIME_CONTROL = "collectionTimeControl";
    public static final String FIELD_USER_VERIFICATION = "userVerification";
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
    @ApiModelProperty("数据收集组历史表ID")
    @Id
    @Where
    private String tagGroupHisId;
    @ApiModelProperty(value = "数据收集组ID", required = true)
    @NotBlank
    @Where
    private String tagGroupId;
    @ApiModelProperty(value = "数据收集组编码", required = true)
    @NotBlank
    @Where
    private String tagGroupCode;
    @ApiModelProperty(value = "数据收集组描述")
    @Where
    private String tagGroupDescription;
    @ApiModelProperty(value = "收集组类型", required = true)
    @NotBlank
    @Where
    private String tagGroupType;
    @ApiModelProperty(value = "来源数据收集组ID")
    @Where
    private String sourceGroupId;
    @ApiModelProperty(value = "业务类型")
    @Where
    private String businessType;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据收集时点", required = true)
    @NotBlank
    @Where
    private String collectionTimeControl;
    @ApiModelProperty(value = "需要用户验证")
    @Where
    private String userVerification;
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
     * @return 数据收集组历史表ID
     */
    public String getTagGroupHisId() {
        return tagGroupHisId;
    }

    public void setTagGroupHisId(String tagGroupHisId) {
        this.tagGroupHisId = tagGroupHisId;
    }

    /**
     * @return 数据收集组ID
     */
    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    /**
     * @return 数据收集组编码
     */
    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    /**
     * @return 数据收集组描述
     */
    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    /**
     * @return 收集组类型
     */
    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    /**
     * @return 来源数据收集组ID
     */
    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    /**
     * @return 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据收集时点
     */
    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    /**
     * @return 需要用户验证
     */
    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
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
