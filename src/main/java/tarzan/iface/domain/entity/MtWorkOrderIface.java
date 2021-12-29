package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 工单接口表
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
@ApiModel("工单接口表")

@ModifyAudit

@Table(name = "mt_work_order_iface")
@CustomPrimary
public class MtWorkOrderIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
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
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_COMPLETE_CONTROL_TYPE = "completeControlType";
    public static final String FIELD_COMPLETE_CONTROL_QTY = "completeControlQty";
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
    private static final long serialVersionUID = -6179417841339243428L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "工厂代码", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotBlank
    @Where
    private String itemCode;
    @ApiModelProperty(value = "工单Id")
    @Where
    private Double workOrderId;
    @ApiModelProperty(value = "工单号", required = true)
    @NotBlank
    @Where
    private String workOrderNum;
    @ApiModelProperty(value = "工单数量", required = true)
    @NotNull
    @Where
    private Double quantity;
    @ApiModelProperty(value = "工单类型", required = true)
    @NotBlank
    @Where
    private String workOrderType;
    @ApiModelProperty(value = "工单状态", required = true)
    @NotBlank
    @Where
    private String workOrderStatus;
    @ApiModelProperty(value = "NEW、RELEASED、HOLD、COMPLETED、CLOSED、ABANDON", required = true)
    @NotNull
    @Where
    private Date scheduleStartDate;
    @ApiModelProperty(value = "计划开始时间", required = true)
    @NotNull
    @Where
    private Date scheduleEndDate;
    @ApiModelProperty(value = "完工库位")
    @Where
    private String completeLocator;
    @ApiModelProperty(value = "产线代码")
    @Where
    private String prodLineCode;
    @ApiModelProperty(value = "生产版本")
    @Where
    private String productionVersion;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "ERP创建日期", required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人", required = true)
    @NotNull
    @Where
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人", required = true)
    @NotNull
    @Where
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期", required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
    @ApiModelProperty(value = "完工限制类型")
    private String completeControlType;
    @ApiModelProperty(value = "完工限制值")
    private Double completeControlQty;
    @Cid
    @Where
    private Long cid;
    @ApiModelProperty(value = "")
    @Where
    private String attributeCategory;
    @ApiModelProperty(value = "")
    @Where
    private String attribute1;
    @ApiModelProperty(value = "")
    @Where
    private String attribute2;
    @ApiModelProperty(value = "")
    @Where
    private String attribute3;
    @ApiModelProperty(value = "")
    @Where
    private String attribute4;
    @ApiModelProperty(value = "")
    @Where
    private String attribute5;
    @ApiModelProperty(value = "")
    @Where
    private String attribute6;
    @ApiModelProperty(value = "")
    @Where
    private String attribute7;
    @ApiModelProperty(value = "")
    @Where
    private String attribute8;
    @ApiModelProperty(value = "")
    @Where
    private String attribute9;
    @ApiModelProperty(value = "")
    @Where
    private String attribute10;
    @ApiModelProperty(value = "")
    @Where
    private String attribute11;
    @ApiModelProperty(value = "")
    @Where
    private String attribute12;
    @ApiModelProperty(value = "")
    @Where
    private String attribute13;
    @ApiModelProperty(value = "")
    @Where
    private String attribute14;
    @ApiModelProperty(value = "")
    @Where
    private String attribute15;

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
     * @return 主键
     */
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 工厂代码
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 物料编码
     */
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return 工单Id
     */
    public Double getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Double workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return 工单号
     */
    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    /**
     * @return 工单数量
     */
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return 工单类型
     */
    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    /**
     * @return "工单状态
     */
    public String getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    /**
     * @return NEW、RELEASED、HOLD、COMPLETED、CLOSED、ABANDON"
     */
    public Date getScheduleStartDate() {
        if (scheduleStartDate != null) {
            return (Date) scheduleStartDate.clone();
        } else {
            return null;
        }
    }

    public void setScheduleStartDate(Date scheduleStartDate) {
        if (scheduleStartDate == null) {
            this.scheduleStartDate = null;
        } else {
            this.scheduleStartDate = (Date) scheduleStartDate.clone();
        }
    }

    /**
     * @return 计划开始时间
     */
    public Date getScheduleEndDate() {
        if (scheduleEndDate != null) {
            return (Date) scheduleEndDate.clone();
        } else {
            return null;
        }
    }

    public void setScheduleEndDate(Date scheduleEndDate) {
        if (scheduleEndDate == null) {
            this.scheduleEndDate = null;
        } else {
            this.scheduleEndDate = (Date) scheduleEndDate.clone();
        }
    }

    /**
     * @return 完工库位
     */
    public String getCompleteLocator() {
        return completeLocator;
    }

    public void setCompleteLocator(String completeLocator) {
        this.completeLocator = completeLocator;
    }

    /**
     * @return 产线代码
     */
    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    /**
     * @return 生产版本
     */
    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
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
     * @return ERP创建日期
     */
    public Date getErpCreationDate() {
        if (erpCreationDate != null) {
            return (Date) erpCreationDate.clone();
        } else {
            return null;
        }
    }

    public void setErpCreationDate(Date erpCreationDate) {
        if (erpCreationDate == null) {
            this.erpCreationDate = null;
        } else {
            this.erpCreationDate = (Date) erpCreationDate.clone();
        }
    }

    /**
     * @return ERP创建人
     */
    public Long getErpCreatedBy() {
        return erpCreatedBy;
    }

    public void setErpCreatedBy(Long erpCreatedBy) {
        this.erpCreatedBy = erpCreatedBy;
    }

    /**
     * @return ERP最后更新人
     */
    public Long getErpLastUpdatedBy() {
        return erpLastUpdatedBy;
    }

    public void setErpLastUpdatedBy(Long erpLastUpdatedBy) {
        this.erpLastUpdatedBy = erpLastUpdatedBy;
    }

    /**
     * @return ERP最后更新日期
     */
    public Date getErpLastUpdateDate() {
        if (erpLastUpdateDate != null) {
            return (Date) erpLastUpdateDate.clone();
        } else {
            return null;
        }
    }

    public void setErpLastUpdateDate(Date erpLastUpdateDate) {
        if (erpLastUpdateDate == null) {
            this.erpLastUpdateDate = null;
        } else {
            this.erpLastUpdateDate = (Date) erpLastUpdateDate.clone();
        }
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }
}
