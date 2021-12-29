package io.tarzan.common.domain.entity;

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
 * 号码段分配历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@ApiModel("号码段分配历史表")
@ModifyAudit

@Table(name = "mt_numrange_assign_his")
@CustomPrimary
public class MtNumrangeAssignHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NUMRANGE_ASSIGN_HIS_ID = "numrangeAssignHisId";
    public static final String FIELD_NUMRANGE_ASSIGN_ID = "numrangeAssignId";
    public static final String FIELD_NUMRANGE_ID = "numrangeId";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_OBJECT_TYPE_ID = "objectTypeId";
    public static final String FIELD_OBJECT_TYPE_CODE = "objectTypeCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SITE = "site";
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
    @ApiModelProperty("号码段分配历史表主键")
    @Id
    @Where
    private String numrangeAssignHisId;
    @ApiModelProperty(value = "号码段分配表主键", required = true)
    @NotBlank
    @Where
    private String numrangeAssignId;
    @ApiModelProperty(value = "号码段定义表主键", required = true)
    @NotBlank
    @Where
    private String numrangeId;
    @ApiModelProperty(value = "编码对象ID", required = true)
    @NotBlank
    @Where
    private String objectId;
    @ApiModelProperty(value = "对象类型ID")
    @Where
    private String objectTypeId;
    @ApiModelProperty(value = "对象类型编码")
    @Where
    private String objectTypeCode;
    @ApiModelProperty(value = "站点ID")
    @Where
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    @Where
    private String site;
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
     * @return 号码段分配历史表主键
     */
    public String getNumrangeAssignHisId() {
        return numrangeAssignHisId;
    }

    public void setNumrangeAssignHisId(String numrangeAssignHisId) {
        this.numrangeAssignHisId = numrangeAssignHisId;
    }

    /**
     * @return 号码段分配表主键
     */
    public String getNumrangeAssignId() {
        return numrangeAssignId;
    }

    public void setNumrangeAssignId(String numrangeAssignId) {
        this.numrangeAssignId = numrangeAssignId;
    }

    /**
     * @return 号码段定义表主键
     */
    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    /**
     * @return 编码对象ID
     */
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return 对象类型ID
     */
    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    /**
     * @return 对象类型编码
     */
    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
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
     * @return 站点编码
     */
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
