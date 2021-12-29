package com.ruike.wms.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组件需求记录表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
@ApiModel("组件需求记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_component_demand_record")
@CustomPrimary
public class WmsComponentDemandRecord extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DEMAND_RECORD_ID = "demandRecordId";
    public static final String FIELD_WO_DISPATCH_ID = "woDispatchId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_ROUTER_OPERATION_ID = "routerOperationId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_CALENDAR_SHIFT_ID = "calendarShiftId";
    public static final String FIELD_DISTRIBUTION_BASIC_ID = "distributionBasicId";
    public static final String FIELD_DISTRIBUTION_TYPE = "distributionType";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_WO_QUANTITY = "woQuantity";
    public static final String FIELD_USAGE_QUANTITY = "usageQuantity";
    public static final String FIELD_DISPATCH_QUANTITY = "dispatchQuantity";
    public static final String FIELD_REQUIREMENT_QUANTITY = "requirementQuantity";
    public static final String FIELD_DIST_DEMAND_ID = "distDemandId";
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
    public static final String FIELD_ATTRIBUTE16 = "attribute16";
    public static final String FIELD_ATTRIBUTE17 = "attribute17";
    public static final String FIELD_ATTRIBUTE18 = "attribute18";
    public static final String FIELD_ATTRIBUTE19 = "attribute19";
    public static final String FIELD_ATTRIBUTE20 = "attribute20";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 汇总生成配送需求
     *
     * @return com.ruike.wms.domain.entity.WmsDistributionDemand
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/2 03:50:18
     */
    public WmsDistributionDemand summaryDistributionDemand() {
        WmsDistributionDemand distDemand = new WmsDistributionDemand();
        distDemand.setTenantId(this.getTenantId());
        distDemand.setMaterialId(this.getMaterialId());
        distDemand.setMaterialVersion(this.getMaterialVersion());
        distDemand.setProdLineId(this.getProdLineId());
        distDemand.setWorkcellId(this.getWorkcellId());
        // 去掉基础数据的汇总 会导致基础数据不同 配送需求会重复
        // distDemand.setDistributionBasicId(this.getDistributionBasicId());
        // distDemand.setDistributionType(this.getDistributionType());
        distDemand.setUomId(this.getUomId());
        distDemand.setCalendarShiftId(this.getCalendarShiftId());
        distDemand.setShiftCode(this.getShiftCode());
        distDemand.setSiteId(this.getSiteId());
        distDemand.setSoNum(this.getSoNum());
        distDemand.setSoLineNum(this.getSoLineNum());
        return distDemand;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID")
    @Id
    private String demandRecordId;
    @ApiModelProperty(value = "工单-工段派工ID", required = true)
    @NotBlank
    private String woDispatchId;
    @ApiModelProperty(value = "工单ID", required = true)
    @NotBlank
    private String workOrderId;
    @ApiModelProperty(value = "BOM组件ID", required = true)
    @NotBlank
    private String bomComponentId;
    @ApiModelProperty(value = "路线ID", required = true)
    @NotBlank
    private String routerId;
    @ApiModelProperty(value = "路线步骤ID", required = true)
    @NotBlank
    private String routerStepId;
    @ApiModelProperty(value = "路线工艺ID", required = true)
    @NotBlank
    private String routerOperationId;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    private String operationId;
    @ApiModelProperty(value = "组件物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "单位ID", required = true)
    @NotBlank
    private String uomId;
    @ApiModelProperty(value = "bomID", required = true)
    @NotBlank
    private String bomId;
    @ApiModelProperty(value = "生产线ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "生产线ID", required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工段（工作单元）ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "工序（工作单元）ID", required = true)
    @NotBlank
    private String processId;
    @ApiModelProperty(value = "班次ID", required = true)
    @NotBlank
    private String calendarShiftId;
    @ApiModelProperty(value = "班次编码", required = true)
    private String shiftCode;
    @ApiModelProperty(value = "配送基础属性ID")
    private String distributionBasicId;
    @ApiModelProperty(value = "策略类型")
    private String distributionType;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "工单数量", required = true)
    @NotNull
    private BigDecimal woQuantity;
    @ApiModelProperty(value = "组件数量", required = true)
    @NotNull
    private BigDecimal usageQuantity;
    @ApiModelProperty(value = "派工数量", required = true)
    @NotNull
    private BigDecimal dispatchQuantity;
    @ApiModelProperty(value = "需求数量", required = true)
    @NotNull
    private BigDecimal requirementQuantity;
    @ApiModelProperty(value = "配送需求ID")
    private String distDemandId;
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
    @ApiModelProperty(value = "")
    private String attribute16;
    @ApiModelProperty(value = "")
    private String attribute17;
    @ApiModelProperty(value = "")
    private String attribute18;
    @ApiModelProperty(value = "")
    private String attribute19;
    @ApiModelProperty(value = "")
    private String attribute20;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "损耗率系数")
    @Transient
    private BigDecimal attritionRate;

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
     * @return 主键ID
     */
    public String getDemandRecordId() {
        return demandRecordId;
    }

    public void setDemandRecordId(String demandRecordId) {
        this.demandRecordId = demandRecordId;
    }

    /**
     * @return 工单-工段派工ID
     */
    public String getWoDispatchId() {
        return woDispatchId;
    }

    public void setWoDispatchId(String woDispatchId) {
        this.woDispatchId = woDispatchId;
    }

    /**
     * @return 工单ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return BOM组件ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 路线ID
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 路线步骤ID
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 路线工艺ID
     */
    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    /**
     * @return 工艺ID
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 组件物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 产品版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
    }

    /**
     * @return bomID
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 生产线ID
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 工段（工作单元）ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 工序（工作单元）ID
     */
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * @return 班次ID
     */
    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    /**
     * @return 策略类型
     */
    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    /**
     * @return 工单数量
     */
    public BigDecimal getWoQuantity() {
        return woQuantity;
    }

    public void setWoQuantity(BigDecimal woQuantity) {
        this.woQuantity = woQuantity;
    }

    /**
     * @return 组件数量
     */
    public BigDecimal getUsageQuantity() {
        return usageQuantity;
    }

    public void setUsageQuantity(BigDecimal usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    /**
     * @return 配送需求ID
     */
    public String getDistDemandId() {
        return distDemandId;
    }

    public void setDistDemandId(String distDemandId) {
        this.distDemandId = distDemandId;
    }

    public BigDecimal getDispatchQuantity() {
        return dispatchQuantity;
    }

    public WmsComponentDemandRecord setDispatchQuantity(BigDecimal dispatchQuantity) {
        this.dispatchQuantity = dispatchQuantity;
        return this;
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

    /**
     * @return
     */
    public String getAttribute16() {
        return attribute16;
    }

    public void setAttribute16(String attribute16) {
        this.attribute16 = attribute16;
    }

    /**
     * @return
     */
    public String getAttribute17() {
        return attribute17;
    }

    public void setAttribute17(String attribute17) {
        this.attribute17 = attribute17;
    }

    /**
     * @return
     */
    public String getAttribute18() {
        return attribute18;
    }

    public void setAttribute18(String attribute18) {
        this.attribute18 = attribute18;
    }

    /**
     * @return
     */
    public String getAttribute19() {
        return attribute19;
    }

    public void setAttribute19(String attribute19) {
        this.attribute19 = attribute19;
    }

    /**
     * @return
     */
    public String getAttribute20() {
        return attribute20;
    }

    public void setAttribute20(String attribute20) {
        this.attribute20 = attribute20;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public BigDecimal getRequirementQuantity() {
        return requirementQuantity;
    }

    public void setRequirementQuantity(BigDecimal requirementQuantity) {
        this.requirementQuantity = requirementQuantity;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getSoNum() {
        return soNum;
    }

    public void setSoNum(String soNum) {
        this.soNum = soNum;
    }

    public String getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
    }

    public String getDistributionBasicId() {
        return distributionBasicId;
    }

    public void setDistributionBasicId(String distributionBasicId) {
        this.distributionBasicId = distributionBasicId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public BigDecimal getAttritionRate() {
        return attritionRate;
    }

    public WmsComponentDemandRecord setAttritionRate(BigDecimal attritionRate) {
        this.attritionRate = attritionRate;
        return this;
    }

    public WmsComponentDemandRecord setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
        return this;
    }
}
