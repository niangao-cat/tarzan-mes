package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.hme.domain.service.HmeFreezeDocDomainService;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeApprovalType.*;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeDocStatus.NUFREEZED;

/**
 * 条码冻结单
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:42
 */
@ApiModel("条码冻结单")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_freeze_doc")
@CustomPrimary
public class HmeFreezeDoc extends AuditDomain {

    public static final String FIELD_FREEZE_DOC_ID = "freezeDocId";
    public static final String FIELD_FREEZE_DOC_NUM = "freezeDocNum";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_FREEZE_TYPE = "freezeType";
    public static final String FIELD_FREEZE_DOC_STATUS = "freezeDocStatus";
    public static final String FIELD_APPROVAL_STATUS = "approvalStatus";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_INVENTORY_LOT = "inventoryLot";
    public static final String FIELD_SUPPLIER_LOT = "supplierLot";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_TEST_CODE = "testCode";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_STATION_ID = "stationId";
    public static final String FIELD_OPERATED_BY = "operatedBy";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_VIRTUAL_NUM = "virtualNum";
    public static final String FIELD_AUSN_RATIO = "ausnRatio";
    public static final String FIELD_HOT_SINK_NUM = "hotSinkNum";
    public static final String FIELD_COS_RULE_ID = "cosRuleId";
    public static final String FIELD_SHIFT_ID = "shiftId";
    public static final String FIELD_PRODUCTION_DATE_FROM = "productionDateFrom";
    public static final String FIELD_PRODUCTION_DATE_TO = "productionDateTo";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_TENANT_ID = "tenantId";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public HmeFreezeDoc changeApprovalStatus(String returnStatus) {
        this.setApprovalStatus(YES.equals(returnStatus) ? APPROVALED : NO_PASS);
        return this;
    }

    public HmeFreezeDoc applyApproval() {
        WmsCommonUtils.processValidateMessage(DetailsHelper.getUserDetails().getTenantId(), !StringCommonUtils.contains(this.getApprovalStatus(), UNAPPROVAL, NO_PASS), "HME_FREEZE_001", "HME");
        this.setApprovalStatus(APPROVALING);
        return this;
    }

    public void unfreezeValidation(HmeFreezeDocDomainService docDomainService, HmeFreezePrivilege privilege) {
        // 验证解冻权限
//        WmsCommonUtils.processValidateMessage(tenantId, !docDomainService.isUserUnfreezePrivileged(this, privilege), "HME_FREEZE_002", "HME");
        // 审核状态验证
        WmsCommonUtils.processValidateMessage(tenantId, !APPROVALED.equals(this.getApprovalStatus()), "HME_FREEZE_003", "HME");
        // 单据状态验证
        WmsCommonUtils.processValidateMessage(tenantId, NUFREEZED.equals(this.getFreezeDocStatus()), "HME_FREEZE_004", "HME");
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    private String freezeDocId;
    @ApiModelProperty(value = "冻结单号", required = true)
    @NotBlank
    private String freezeDocNum;
    @ApiModelProperty(value = "工厂ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "冻结类型", required = true)
    @NotBlank
    private String freezeType;
    @ApiModelProperty(value = "冻结状态", required = true)
    @NotBlank
    private String freezeDocStatus;
    @ApiModelProperty(value = "审批状态", required = true)
    @NotBlank
    private String approvalStatus;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "库存批次")
    private String inventoryLot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "工单")
    private String workOrderId;
    @ApiModelProperty(value = "实验代码")
    private String testCode;
    @ApiModelProperty(value = "设备")
    private String equipmentId;
    @ApiModelProperty(value = "生产线")
    private String prodLineId;
    @ApiModelProperty(value = "工段")
    private String workcellId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "金锡比")
    private BigDecimal ausnRatio;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkNum;
    @ApiModelProperty(value = "筛选规则")
    private String cosRuleId;
    @ApiModelProperty(value = "班次")
    private String shiftId;
    @ApiModelProperty(value = "生产时间从")
    private Date productionDateFrom;
    @ApiModelProperty(value = "生产时间至")
    private Date productionDateTo;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
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

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 主键
     */
    public String getFreezeDocId() {
        return freezeDocId;
    }

    public void setFreezeDocId(String freezeDocId) {
        this.freezeDocId = freezeDocId;
    }

    /**
     * @return 冻结单号
     */
    public String getFreezeDocNum() {
        return freezeDocNum;
    }

    public void setFreezeDocNum(String freezeDocNum) {
        this.freezeDocNum = freezeDocNum;
    }

    /**
     * @return 工厂ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 冻结类型
     */
    public String getFreezeType() {
        return freezeType;
    }

    public void setFreezeType(String freezeType) {
        this.freezeType = freezeType;
    }

    /**
     * @return 冻结状态
     */
    public String getFreezeDocStatus() {
        return freezeDocStatus;
    }

    public void setFreezeDocStatus(String freezeDocStatus) {
        this.freezeDocStatus = freezeDocStatus;
    }

    /**
     * @return 审批状态
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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
     * @return 物料版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
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
     * @return 仓库ID
     */
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * @return 货位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 供应商ID
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 库存批次
     */
    public String getInventoryLot() {
        return inventoryLot;
    }

    public void setInventoryLot(String inventoryLot) {
        this.inventoryLot = inventoryLot;
    }

    /**
     * @return 供应商批次
     */
    public String getSupplierLot() {
        return supplierLot;
    }

    public void setSupplierLot(String supplierLot) {
        this.supplierLot = supplierLot;
    }

    /**
     * @return 工单
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return 实验代码
     */
    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    /**
     * @return 设备
     */
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * @return 生产线
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 工段
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 工位
     */
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * @return 操作人ID
     */
    public Long getOperatedBy() {
        return operatedBy;
    }

    public void setOperatedBy(Long operatedBy) {
        this.operatedBy = operatedBy;
    }

    /**
     * @return 班次
     */
    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return 生产时间从
     */
    public Date getProductionDateFrom() {
        return productionDateFrom;
    }

    public void setProductionDateFrom(Date productionDateFrom) {
        this.productionDateFrom = productionDateFrom;
    }

    /**
     * @return 生产时间至
     */
    public Date getProductionDateTo() {
        return productionDateTo;
    }

    public void setProductionDateTo(Date productionDateTo) {
        this.productionDateTo = productionDateTo;
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
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getCosType() {
        return cosType;
    }

    public void setCosType(String cosType) {
        this.cosType = cosType;
    }

    public String getWafer() {
        return wafer;
    }

    public void setWafer(String wafer) {
        this.wafer = wafer;
    }

    public String getVirtualNum() {
        return virtualNum;
    }

    public void setVirtualNum(String virtualNum) {
        this.virtualNum = virtualNum;
    }

    public BigDecimal getAusnRatio() {
        return ausnRatio;
    }

    public void setAusnRatio(BigDecimal ausnRatio) {
        this.ausnRatio = ausnRatio;
    }

    public String getHotSinkNum() {
        return hotSinkNum;
    }

    public void setHotSinkNum(String hotSinkNum) {
        this.hotSinkNum = hotSinkNum;
    }

    public String getCosRuleId() {
        return cosRuleId;
    }

    public void setCosRuleId(String cosRuleId) {
        this.cosRuleId = cosRuleId;
    }
}
