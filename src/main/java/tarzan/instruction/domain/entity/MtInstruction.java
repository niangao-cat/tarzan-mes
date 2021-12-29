package tarzan.instruction.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓储物流指令内容表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@ApiModel("仓储物流指令内容表")

@ModifyAudit

@Table(name = "mt_instruction")
@CustomPrimary
public class MtInstruction extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_INSTRUCTION_NUM = "instructionNum";
    public static final String FIELD_SOURCE_INSTRUCTION_ID = "sourceInstructionId";
    public static final String FIELD_SOURCE_DOC_ID = "sourceDocId";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_DIS_ROUTER_ID = "disRouterId";
    public static final String FIELD_ORDER_TYPE = "orderType";
    public static final String FIELD_ORDER_ID = "orderId";
    public static final String FIELD_SOURCE_ORDER_TYPE = "sourceOrderType";
    public static final String FIELD_SOURCE_ORDER_ID = "sourceOrderId";
    public static final String FIELD_SOURCE_ORDER_LINE_ID = "sourceOrderLineId";
    public static final String FIELD_SOURCE_ORDER_LINE_LOCATION_ID = "sourceOrderLineLocationId";
    public static final String FIELD_SOURCE_ORDER_LINE_DIST_ID = "sourceOrderLineDistId";
    public static final String FIELD_SOURCE_OUTSIDE_COMP_LINE_ID = "sourceOutsideCompLineId";
    public static final String FIELD_FROM_SITE_ID = "fromSiteId";
    public static final String FIELD_TO_SITE_ID = "toSiteId";
    public static final String FIELD_FROM_LOCATOR_ID = "fromLocatorId";
    public static final String FIELD_TO_LOCATOR_ID = "toLocatorId";
    public static final String FIELD_COST_CENTER_ID = "costCenterId";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_DEMAND_TIME = "demandTime";
    public static final String FIELD_WAVE_SEQUENCE = "waveSequence";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_COVER_QTY = "coverQty";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_FROM_OWNER_TYPE = "fromOwnerType";
    public static final String FIELD_TO_OWNER_TYPE = "toOwnerType";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7394856912066938573L;

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
    @ApiModelProperty("主键ID ,表示唯一一条记录")
    @Id
    @Where
    private String instructionId;
    @ApiModelProperty(value = "指令编号", required = true)
    @NotBlank
    @Where
    private String instructionNum;
    @ApiModelProperty(value = "来源指令ID")
    @Where
    private String sourceInstructionId;
    @ApiModelProperty(value = "单据id，当业务过程中用到的是单据指令时使用。或者驱动控制组id")
    @Where
    private String sourceDocId;
    @ApiModelProperty(value = "指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）", required = true)
    @NotBlank
    @Where
    private String instructionType;
    @ApiModelProperty(value = "指令状态（NEW，RELEASED，CANCEL，COMPLETED，COMPLETE_CANCEL）", required = true)
    @NotBlank
    @Where
    @LovValue(value = "WMS.COST_CENTER_DOCUMENT_LINE.STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "站点id", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "单位")
    @Where
    private String uomId;
    @ApiModelProperty(value = "在制品 驱动对象是eo，没有物料id")
    @Where
    private String eoId;
    @ApiModelProperty(value = "配送路线")
    @Where
    private String disRouterId;
    @ApiModelProperty(value = "订单类型（EO, NOTIFICATION,STOCKTAKE)")
    @Where
    private String orderType;
    @ApiModelProperty(value = "订单ID")
    @Where
    private String orderId;
    @ApiModelProperty(value = "ERP订单类型（PO，SO）")
    @Where
    private String sourceOrderType;
    @ApiModelProperty(value = "ERP订单ID")
    @Where
    private String sourceOrderId;
    @ApiModelProperty(value = "订单行id")
    @Where
    private String sourceOrderLineId;
    @ApiModelProperty(value = "订单发运行id（oracle的采购订单发运行id）")
    @Where
    private String sourceOrderLineLocationId;
    @ApiModelProperty(value = "订单分配行id（oracle的分配行id）")
    @Where
    private String sourceOrderLineDistId;
    @ApiModelProperty(value = "外协组件行id")
    @Where
    private String sourceOutsideCompLineId;
    @ApiModelProperty(value = "来源站点id")
    @Where
    private String fromSiteId;
    @ApiModelProperty(value = "目标站点id")
    @Where
    private String toSiteId;
    @ApiModelProperty(value = "来源库位id")
    @Where
    private String fromLocatorId;
    @ApiModelProperty(value = "目标库位id")
    @Where
    private String toLocatorId;
    @ApiModelProperty(value = "成本中心")
    @Where
    private String costCenterId;
    @ApiModelProperty(value = "指令数量", required = true)
    @NotNull
    @Where
    private Double quantity;
    @ApiModelProperty(value = "供应商（指令指定供应商时）")
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点id")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "客户（指令指定客户）")
    @Where
    private String customerId;
    @ApiModelProperty(value = "客户地点id")
    @Where
    private String customerSiteId;
    @ApiModelProperty(value = "需求日期")
    @Where
    private Date demandTime;
    @ApiModelProperty(value = "波次")
    @Where
    private String waveSequence;
    @ApiModelProperty(value = "班次日期")
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次代码")
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "覆盖数量")
    @Where
    private Double coverQty;
    @ApiModelProperty(value = "业务类型", required = true)
    @NotBlank
    @Where
    private String businessType;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "实际业务需要的单据编号")
    @Where
    private String identification;
    @ApiModelProperty(value = "所有者类型")
    @Where
    private String fromOwnerType;
    @ApiModelProperty(value = "目标所有者类型")
    @Where
    private String toOwnerType;
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    @Where
    private String latestHisId;
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
     * @return 主键ID ,表示唯一一条记录
     */
    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    /**
     * @return 指令编号
     */
    public String getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
    }

    /**
     * @return 来源指令ID
     */
    public String getSourceInstructionId() {
        return sourceInstructionId;
    }

    public void setSourceInstructionId(String sourceInstructionId) {
        this.sourceInstructionId = sourceInstructionId;
    }

    /**
     * @return 单据id，当业务过程中用到的是单据指令时使用。或者驱动控制组id
     */
    public String getSourceDocId() {
        return sourceDocId;
    }

    public void setSourceDocId(String sourceDocId) {
        this.sourceDocId = sourceDocId;
    }

    /**
     * @return 指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）
     */
    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    /**
     * @return 指令状态（NEW，RELEASED，CANCEL，COMPLETED，COMPLETE_CANCEL）
     */
    public String getInstructionStatus() {
        return instructionStatus;
    }

    public void setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
    }

    /**
     * @return 站点id
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 单位
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 在制品 驱动对象是eo，没有物料id
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 配送路线
     */
    public String getDisRouterId() {
        return disRouterId;
    }

    public void setDisRouterId(String disRouterId) {
        this.disRouterId = disRouterId;
    }

    /**
     * @return 订单类型（EO, NOTIFICATION,STOCKTAKE)
     */
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * @return 订单ID
     */
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return ERP订单类型（PO，SO）
     */
    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    /**
     * @return ERP订单ID
     */
    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    /**
     * @return 订单行id
     */
    public String getSourceOrderLineId() {
        return sourceOrderLineId;
    }

    public void setSourceOrderLineId(String sourceOrderLineId) {
        this.sourceOrderLineId = sourceOrderLineId;
    }

    /**
     * @return 订单发运行id（oracle的采购订单发运行id）
     */
    public String getSourceOrderLineLocationId() {
        return sourceOrderLineLocationId;
    }

    public void setSourceOrderLineLocationId(String sourceOrderLineLocationId) {
        this.sourceOrderLineLocationId = sourceOrderLineLocationId;
    }

    /**
     * @return 订单分配行id（oracle的分配行id）
     */
    public String getSourceOrderLineDistId() {
        return sourceOrderLineDistId;
    }

    public void setSourceOrderLineDistId(String sourceOrderLineDistId) {
        this.sourceOrderLineDistId = sourceOrderLineDistId;
    }

    /**
     * @return 外协组件行id
     */
    public String getSourceOutsideCompLineId() {
        return sourceOutsideCompLineId;
    }

    public void setSourceOutsideCompLineId(String sourceOutsideCompLineId) {
        this.sourceOutsideCompLineId = sourceOutsideCompLineId;
    }

    /**
     * @return 来源站点id
     */
    public String getFromSiteId() {
        return fromSiteId;
    }

    public void setFromSiteId(String fromSiteId) {
        this.fromSiteId = fromSiteId;
    }

    /**
     * @return 目标站点id
     */
    public String getToSiteId() {
        return toSiteId;
    }

    public void setToSiteId(String toSiteId) {
        this.toSiteId = toSiteId;
    }

    /**
     * @return 来源库位id
     */
    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    /**
     * @return 目标库位id
     */
    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }

    /**
     * @return 成本中心
     */
    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return 指令数量
     */
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return 供应商（指令指定供应商时）
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商地点id
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 客户（指令指定客户）
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户地点id
     */
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    /**
     * @return 需求日期
     */
    public Date getDemandTime() {
        if (demandTime != null) {
            return (Date) demandTime.clone();
        } else {
            return null;
        }
    }

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }
    }

    /**
     * @return 波次
     */
    public String getWaveSequence() {
        return waveSequence;
    }

    public void setWaveSequence(String waveSequence) {
        this.waveSequence = waveSequence;
    }

    /**
     * @return 班次日期
     */
    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    /**
     * @return 班次代码
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 覆盖数量
     */
    public Double getCoverQty() {
        return coverQty;
    }

    public void setCoverQty(Double coverQty) {
        this.coverQty = coverQty;
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
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 实际业务需要的单据编号
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return 所有者类型
     */
    public String getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(String fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    /**
     * @return 目标所有者类型
     */
    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    /**
     * @return 最新一次新增历史表的主键
     */
    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
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
