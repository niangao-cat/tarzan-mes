package com.ruike.itf.domain.entity;

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
import lombok.Data;

/**
 * 内部订单接口表
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:29:25
 */
@ApiModel("内部订单接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_internal_order_iface")
@Data
public class ItfInternalOrderIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INTERNAL_ORDER_ID = "internalOrderId";
    public static final String FIELD_INTERNAL_ORDER = "internalOrder";
    public static final String FIELD_INTERNAL_ORDER_TYPE = "internalOrderType";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_INTERNAL_ORDER_STATUS = "internalOrderStatus";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_MESSAGE = "message";
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
    private String siteCode;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    private String internalOrderStatus;
    @ApiModelProperty(value = "批次ID", required = true)
    @NotBlank
    private String batchId;
    @ApiModelProperty(value = "批次时间", required = true)
    @NotBlank
    private String batchDate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "错误信息")
    private String message;
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


}
