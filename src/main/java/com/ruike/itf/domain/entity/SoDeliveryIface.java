package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 销售发退货单接口表
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-02 13:41:40
 */
@ApiModel("销售发退货单接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_so_delivery_iface")
public class SoDeliveryIface extends AuditDomain {

    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_SAP_DOC_TYPE = "sapDocType";
    public static final String FIELD_SAP_DOC_TYPE_DES = "sapDocTypeDes";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_DEMAND_TIME = "demandTime";
    public static final String FIELD_SO_ORGANIZATION_CODE = "soOrganizationCode";
    public static final String FIELD_SOURCE_SYSTEM = "sourceSystem";
    public static final String FIELD_INSTRUCTION_NUM = "instructionNum";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_LOT_FLAG = "lotFlag";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_FROM_SITE_CODE = "fromSiteCode";
    public static final String FIELD_FROM_LOCATOR_CODE = "fromLocatorCode";
    public static final String FIELD_TO_SITE_CODE = "toSiteCode";
    public static final String FIELD_TO_LOCATOR_CODE = "toLocatorCode";
    public static final String FIELD_REMARK_L = "remarkL";
    public static final String FIELD_REMARK_SO = "remarkSo";
    public static final String FIELD_SOURCE_ORDER_NUM = "sourceOrderNum";
    public static final String FIELD_SOURCE_ORDER_LINE_NUM = "sourceOrderLineNum";
    public static final String FIELD_INSTRUCTION_LINE_NUM = "instructionLineNum";
    public static final String FIELD_SPEC_STOCK_FLAG = "specStockFlag";
    public static final String FIELD_SAP_DOC_LINE_TYPE = "sapDocLineType";
    public static final String FIELD_SAP_DOC_LINE_TYPE_DES = "sapDocLineTypeDes";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
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

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "SAP单据类型")
    private String sapDocType;
    @ApiModelProperty(value = "单据类型描述")
    private String sapDocTypeDes;
    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "客户编码")
    private String customerCode;
    @ApiModelProperty(value = "实际发货日期")
    private String demandTime;
    @ApiModelProperty(value = "销售组织编码")
    private String soOrganizationCode;
    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;
    @ApiModelProperty(value = "指令编号")
    private String instructionNum;
    @ApiModelProperty(value = "指令类型")
    private String instructionType;
    @ApiModelProperty(value = "指令状态")
    private String instructionStatus;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料批次管理标识")
    private String lotFlag;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "发货工厂")
    private String fromSiteCode;
    @ApiModelProperty(value = "发货仓库")
    private String fromLocatorCode;
    @ApiModelProperty(value = "收货工厂")
    private String toSiteCode;
    @ApiModelProperty(value = "收货仓库")
    private String toLocatorCode;
    @ApiModelProperty(value = "行备注")
    private String remarkL;
    @ApiModelProperty(value = "销售订单备注")
    private String remarkSo;
    @ApiModelProperty(value = "销售订单号")
    private String sourceOrderNum;
    @ApiModelProperty(value = "销售订单行号")
    private String sourceOrderLineNum;
    @ApiModelProperty(value = "行号")
    private String instructionLineNum;
    @ApiModelProperty(value = "特殊库存标识")
    private String specStockFlag;
    @ApiModelProperty(value = "SAP行类型")
    private String sapDocLineType;
    @ApiModelProperty(value = "SAP行类型描述")
    private String sapDocLineTypeDes;
    @ApiModelProperty(value = "单据指定SN")
    private String sn;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Long batchId;
    @ApiModelProperty(value = "数据处理状态")
    private String status;
    @ApiModelProperty(value = "数据处理消息")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
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
    public String getIfaceId() {
        return ifaceId;
    }

    public SoDeliveryIface setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
        return this;
    }

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public SoDeliveryIface setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 单据编号
     */
    public String getInstructionDocNum() {
        return instructionDocNum;
    }

    public SoDeliveryIface setInstructionDocNum(String instructionDocNum) {
        this.instructionDocNum = instructionDocNum;
        return this;
    }

    /**
     * @return 单据类型
     */
    public String getInstructionDocType() {
        return instructionDocType;
    }

    public SoDeliveryIface setInstructionDocType(String instructionDocType) {
        this.instructionDocType = instructionDocType;
        return this;
    }

    /**
     * @return SAP单据类型
     */
    public String getSapDocType() {
        return sapDocType;
    }

    public SoDeliveryIface setSapDocType(String sapDocType) {
        this.sapDocType = sapDocType;
        return this;
    }

    /**
     * @return 单据类型描述
     */
    public String getSapDocTypeDes() {
        return sapDocTypeDes;
    }

    public SoDeliveryIface setSapDocTypeDes(String sapDocTypeDes) {
        this.sapDocTypeDes = sapDocTypeDes;
        return this;
    }

    /**
     * @return 单据状态
     */
    public String getInstructionDocStatus() {
        return instructionDocStatus;
    }

    public SoDeliveryIface setInstructionDocStatus(String instructionDocStatus) {
        this.instructionDocStatus = instructionDocStatus;
        return this;
    }

    /**
     * @return 工厂编码
     */
    public String getPlantCode() {
        return plantCode;
    }

    public SoDeliveryIface setPlantCode(String plantCode) {
        this.plantCode = plantCode;
        return this;
    }

    /**
     * @return 客户编码
     */
    public String getCustomerCode() {
        return customerCode;
    }

    public SoDeliveryIface setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    /**
     * @return 实际发货日期
     */
    public String getDemandTime() {
        return demandTime;
    }

    public SoDeliveryIface setDemandTime(String demandTime) {
        this.demandTime = demandTime;
        return this;
    }

    /**
     * @return 销售组织编码
     */
    public String getSoOrganizationCode() {
        return soOrganizationCode;
    }

    public SoDeliveryIface setSoOrganizationCode(String soOrganizationCode) {
        this.soOrganizationCode = soOrganizationCode;
        return this;
    }

    /**
     * @return 来源系统
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    public SoDeliveryIface setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
        return this;
    }

    /**
     * @return 指令编号
     */
    public String getInstructionNum() {
        return instructionNum;
    }

    public SoDeliveryIface setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
        return this;
    }

    /**
     * @return 指令类型
     */
    public String getInstructionType() {
        return instructionType;
    }

    public SoDeliveryIface setInstructionType(String instructionType) {
        this.instructionType = instructionType;
        return this;
    }

    /**
     * @return 指令状态
     */
    public String getInstructionStatus() {
        return instructionStatus;
    }

    public SoDeliveryIface setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
        return this;
    }

    /**
     * @return 物料编码
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public SoDeliveryIface setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    /**
     * @return 物料批次管理标识
     */
    public String getLotFlag() {
        return lotFlag;
    }

    public SoDeliveryIface setLotFlag(String lotFlag) {
        this.lotFlag = lotFlag;
        return this;
    }

    /**
     * @return 物料版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public SoDeliveryIface setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
        return this;
    }

    /**
     * @return 需求数量
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    public SoDeliveryIface setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * @return 单位编码
     */
    public String getUomCode() {
        return uomCode;
    }

    public SoDeliveryIface setUomCode(String uomCode) {
        this.uomCode = uomCode;
        return this;
    }

    /**
     * @return 发货工厂
     */
    public String getFromSiteCode() {
        return fromSiteCode;
    }

    public SoDeliveryIface setFromSiteCode(String fromSiteCode) {
        this.fromSiteCode = fromSiteCode;
        return this;
    }

    /**
     * @return 发货仓库
     */
    public String getFromLocatorCode() {
        return fromLocatorCode;
    }

    public SoDeliveryIface setFromLocatorCode(String fromLocatorCode) {
        this.fromLocatorCode = fromLocatorCode;
        return this;
    }

    /**
     * @return 收货工厂
     */
    public String getToSiteCode() {
        return toSiteCode;
    }

    public SoDeliveryIface setToSiteCode(String toSiteCode) {
        this.toSiteCode = toSiteCode;
        return this;
    }

    /**
     * @return 收货仓库
     */
    public String getToLocatorCode() {
        return toLocatorCode;
    }

    public SoDeliveryIface setToLocatorCode(String toLocatorCode) {
        this.toLocatorCode = toLocatorCode;
        return this;
    }

    /**
     * @return 行备注
     */
    public String getRemarkL() {
        return remarkL;
    }

    public SoDeliveryIface setRemarkL(String remarkL) {
        this.remarkL = remarkL;
        return this;
    }

    /**
     * @return 销售订单备注
     */
    public String getRemarkSo() {
        return remarkSo;
    }

    public SoDeliveryIface setRemarkSo(String remarkSo) {
        this.remarkSo = remarkSo;
        return this;
    }

    /**
     * @return 销售订单号
     */
    public String getSourceOrderNum() {
        return sourceOrderNum;
    }

    public SoDeliveryIface setSourceOrderNum(String sourceOrderNum) {
        this.sourceOrderNum = sourceOrderNum;
        return this;
    }

    /**
     * @return 销售订单行号
     */
    public String getSourceOrderLineNum() {
        return sourceOrderLineNum;
    }

    public SoDeliveryIface setSourceOrderLineNum(String sourceOrderLineNum) {
        this.sourceOrderLineNum = sourceOrderLineNum;
        return this;
    }

    /**
     * @return 行号
     */
    public String getInstructionLineNum() {
        return instructionLineNum;
    }

    public SoDeliveryIface setInstructionLineNum(String instructionLineNum) {
        this.instructionLineNum = instructionLineNum;
        return this;
    }

    /**
     * @return 特殊库存标识
     */
    public String getSpecStockFlag() {
        return specStockFlag;
    }

    public SoDeliveryIface setSpecStockFlag(String specStockFlag) {
        this.specStockFlag = specStockFlag;
        return this;
    }

    /**
     * @return SAP行类型
     */
    public String getSapDocLineType() {
        return sapDocLineType;
    }

    public SoDeliveryIface setSapDocLineType(String sapDocLineType) {
        this.sapDocLineType = sapDocLineType;
        return this;
    }

    /**
     * @return SAP行类型描述
     */
    public String getSapDocLineTypeDes() {
        return sapDocLineTypeDes;
    }

    public SoDeliveryIface setSapDocLineTypeDes(String sapDocLineTypeDes) {
        this.sapDocLineTypeDes = sapDocLineTypeDes;
        return this;
    }

    /**
     * @return 单据指定SN
     */
    public String getSn() {
        return sn;
    }

    public SoDeliveryIface setSn(String sn) {
        this.sn = sn;
        return this;
    }

    /**
     * @return 数据批次ID
     */
    public Long getBatchId() {
        return batchId;
    }

    public SoDeliveryIface setBatchId(Long batchId) {
        this.batchId = batchId;
        return this;
    }

    /**
     * @return 数据处理状态
     */
    public String getStatus() {
        return status;
    }

    public SoDeliveryIface setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return 数据处理消息
     */
    public String getMessage() {
        return message;
    }

    public SoDeliveryIface setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public SoDeliveryIface setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public SoDeliveryIface setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public SoDeliveryIface setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public SoDeliveryIface setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public SoDeliveryIface setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public SoDeliveryIface setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public SoDeliveryIface setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public SoDeliveryIface setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public SoDeliveryIface setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public SoDeliveryIface setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public SoDeliveryIface setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public SoDeliveryIface setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public SoDeliveryIface setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public SoDeliveryIface setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public SoDeliveryIface setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public SoDeliveryIface setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public SoDeliveryIface setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
