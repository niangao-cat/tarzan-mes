package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 售后返品拆机表
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
@ApiModel("售后返品拆机表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_service_split_record")
@CustomPrimary
public class HmeServiceSplitRecord extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPLIT_RECORD_ID = "splitRecordId";
    public static final String FIELD_SERVICE_RECEIVE_ID = "serviceReceiveId";
    public static final String FIELD_AFTER_SALES_REPAIR_ID = "afterSalesRepairId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_TOP_SPLIT_RECORD_ID = "topSplitRecordId";
    public static final String FIELD_PARENT_SPLIT_RECORD_ID = "parentSplitRecordId";
    public static final String FIELD_INTERNAL_ORDER_NUM = "internalOrderNum";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_BACK_TYPE = "backType";
    public static final String FIELD_IS_REPAIR = "isRepair";
    public static final String FIELD_IS_ONHAND = "isOnhand";
    public static final String FIELD_SPLIT_STATUS = "splitStatus";
    public static final String FIELD_SPLIT_BY = "splitBy";
    public static final String FIELD_SPLIT_TIME = "splitTime";
    public static final String FIELD_WKC_SHIFT_ID = "wkcShiftId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_CID = "cid";
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
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键id")
    @Id
    @GeneratedValue
    private String splitRecordId;
    @ApiModelProperty(value = "售后接收信息id")
    private String serviceReceiveId;
    @ApiModelProperty(value = "售后登记大仓业务表ID", required = true)
    @NotBlank
    private String afterSalesRepairId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "SN编码", required = true)
    @NotBlank
    private String snNum;
    @ApiModelProperty(value = "顶层售后返品拆机主键ID", required = true)
    @NotBlank
    private String topSplitRecordId;
    @ApiModelProperty(value = "父级售后返品拆机主键ID", required = true)
    @NotBlank
    private String parentSplitRecordId;
    @ApiModelProperty(value = "内部订单号")
    private String internalOrderNum;
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "生产指令编号")
    private String workOrderNum;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "实物返回属性")
    private String backType;
    @ApiModelProperty(value = "是否维修", required = true)
    @NotBlank
    private String isRepair;
    @ApiModelProperty(value = "是否库存管理", required = true)
    @NotBlank
    private String isOnhand;
    @ApiModelProperty(value = "拆分状态", required = true)
    @NotBlank
    private String splitStatus;
    @ApiModelProperty(value = "拆机人", required = true)
    @NotNull
    private Long splitBy;
    @ApiModelProperty(value = "拆机时间", required = true)
    @NotNull
    private Date splitTime;
    @ApiModelProperty(value = "班次ID", required = true)
    @NotBlank
    private String wkcShiftId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    private String operationId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
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

}
