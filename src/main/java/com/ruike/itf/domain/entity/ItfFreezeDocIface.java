package com.ruike.itf.domain.entity;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.domain.repository.ItfFreezeDocIfaceRepository;
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

import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.NEW;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.SUCCESS;

/**
 * 条码冻结接口表
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
@ApiModel("条码冻结接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_freeze_doc_iface")
@CustomPrimary
public class ItfFreezeDocIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
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
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
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
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "金锡比")
    private BigDecimal ausnRatio;

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("主键")
    @Id
    private String interfaceId;
    @ApiModelProperty(value = "冻结单ID", required = true)
    @NotBlank
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
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkNum;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "筛选规则")
    private String cosRuleId;

    public ItfFreezeDocIface() {
    }

    public ItfFreezeDocIface(@NotBlank String freezeDocId, @NotNull Long sequence) {
        this.freezeDocId = freezeDocId;
        this.sequence = sequence;
    }

    public static ItfFreezeDocIface toInterface(HmeFreezeDoc entity, ItfFreezeDocIfaceRepository freezeDocIfaceRepository) {
        ItfFreezeDocIface iface = new ItfFreezeDocIface();
        BeanCopierUtil.copy(entity, iface);
        iface.setSequence(freezeDocIfaceRepository.select(new ItfFreezeDocIface() {{
            setFreezeDocId(entity.getFreezeDocId());
        }}).stream().mapToLong(ItfFreezeDocIface::getSequence).max().orElse(0L) + 1);
        return iface;
    }

    public void processInit() {
        this.setProcessDate(DateUtil.date());
        this.setProcessStatus(NEW);
        this.setProcessMessage("");
    }

    public ItfFreezeDocIface updateStatus(String returnStatus) {
        this.setStatus(returnStatus);
        this.setProcessStatus(SUCCESS);
        this.setProcessDate(DateUtil.date());
        this.setProcessMessage("");
        return this;
    }

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
    @ApiModelProperty(value = "审批版本", required = true)
    @NotNull
    private Long sequence;
    @ApiModelProperty(value = "返回审批状态")
    private String status;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
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
    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * @return 冻结单ID
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
     * @return 审批版本
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 返回审批状态
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 处理时间
     */
    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    /**
     * @return 处理消息
     */
    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    /**
     * @return 处理状态(N / P / E / S : 正常 / 处理中 / 错误 / 成功)
     */
    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
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
