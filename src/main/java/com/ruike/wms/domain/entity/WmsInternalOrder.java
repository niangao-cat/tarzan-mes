package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 内部订单表
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:30:34
 */
@ApiModel("内部订单表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_internal_order")
public class WmsInternalOrder extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INTERNAL_ORDER_ID = "internalOrderId";
    public static final String FIELD_INTERNAL_ORDER = "internalOrder";
    public static final String FIELD_INTERNAL_ORDER_TYPE = "internalOrderType";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_INTERNAL_ORDER_STATUS = "internalOrderStatus";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_MOVE_TYPE = "moveType";
    public static final String FIELD_MOVE_REASON = "moveReason";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE_FROM_TO = "dateFromTo";
    public static final String FIELD_DATE_END_TO = "dateEndTo";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String internalOrderId;
    @ApiModelProperty(value = "内部订单", required = true)
    @NotBlank
    private String internalOrder;
    @ApiModelProperty(value = "订单类型")
    private String internalOrderType;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    private String internalOrderStatus;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "移动原因")
    private String moveReason;
    @ApiModelProperty(value = "内部订单描述")
    private String description;
    @ApiModelProperty(value = "生效时间")
    private Date dateFromTo;
    @ApiModelProperty(value = "失效时间")
    private Date dateEndTo;
    @ApiModelProperty(value = "来源标识ID（Oracle同步写入账户别名ID）")
    private BigDecimal sourceIdentificationId;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    private Long cid;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long objectVersionNumber;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long createdBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date creationDate;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date lastUpdateDate;

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
     * @return
     */
    public String getInternalOrderId() {
        return internalOrderId;
    }

    public void setInternalOrderId(String internalOrderId) {
        this.internalOrderId = internalOrderId;
    }

    /**
     * @return 内部订单
     */
    public String getInternalOrder() {
        return internalOrder;
    }

    public void setInternalOrder(String internalOrder) {
        this.internalOrder = internalOrder;
    }

    /**
     * @return 订单类型
     */
    public String getInternalOrderType() {
        return internalOrderType;
    }

    public void setInternalOrderType(String internalOrderType) {
        this.internalOrderType = internalOrderType;
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
     * @return 状态
     */
    public String getInternalOrderStatus() {
        return internalOrderStatus;
    }

    public void setInternalOrderStatus(String internalOrderStatus) {
        this.internalOrderStatus = internalOrderStatus;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 移动类型
     */
    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    /**
     * @return 移动原因
     */
    public String getMoveReason() {
        return moveReason;
    }

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
    }

    /**
     * @return 内部订单描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 生效时间
     */
    public Date getDateFromTo() {
        return dateFromTo;
    }

    public void setDateFromTo(Date dateFromTo) {
        this.dateFromTo = dateFromTo;
    }

    /**
     * @return 失效时间
     */
    public Date getDateEndTo() {
        return dateEndTo;
    }

    public void setDateEndTo(Date dateEndTo) {
        this.dateEndTo = dateEndTo;
    }

    /**
     * @return 来源标识ID（Oracle同步写入账户别名ID）
     */
    public BigDecimal getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(BigDecimal sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
