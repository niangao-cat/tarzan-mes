package com.ruike.itf.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

/**
 * 工单接口表
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@ApiModel("工单接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_work_order_iface")
@CustomPrimary
public class ItfWorkOrderIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_WORK_ORDER_TYPE = "workOrderType";
    public static final String FIELD_WORK_ORDER_STATUS = "workOrderStatus";
    public static final String FIELD_SCHEDULE_START_DATE = "scheduleStartDate";
    public static final String FIELD_SCHEDULE_END_DATE = "scheduleEndDate";
    public static final String FIELD_COMPLETE_LOCATOR = "completeLocator";
    public static final String FIELD_PROD_LINE_CODE = "prodLineCode";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_COMPLETE_CONTROL_TYPE = "completeControlType";
    public static final String FIELD_COMPLETE_CONTROL_QTY = "completeControlQty";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

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
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "工厂代码")
    private String plantCode;
    @ApiModelProperty(value = "工单Id")
    private Double workOrderId;
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "工单数量")
    private Double quantity;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;
    @ApiModelProperty(value = "NEW、RELEASED、HOLD、COMPLETED、CLOSED、ABANDON")
    private Date scheduleStartDate;
    @ApiModelProperty(value = "计划开始时间")
    private Date scheduleEndDate;
    @ApiModelProperty(value = "完工库位")
    private String completeLocator;
    @ApiModelProperty(value = "产线代码")
    private String prodLineCode;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "ERP创建日期")
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Double batchId;
    @ApiModelProperty(value = "完工限制类型")
    private String completeControlType;
    @ApiModelProperty(value = "完工限制值")
    private Double completeControlQty;
    @ApiModelProperty(value = "数据批次时间")
    private String batchDate;

    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
