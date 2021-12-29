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
import lombok.Getter;
import lombok.Setter;

/**
 * 生产领退料单接口
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-06-30 13:48:47
 */
@ApiModel("生产领退料单接口")
@VersionAudit
@ModifyAudit
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_costcenter_doc_iface")
public class CostcenterDocIface extends AuditDomain {

    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_PERSON = "person";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_SOURCE_SYSTEM = "sourceSystem";
    public static final String FIELD_INSTRUCTION_NUM = "instructionNum";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_FROM_SITE_CODE = "fromSiteCode";
    public static final String FIELD_TO_SITE_CODE = "toSiteCode";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_FROM_LOCATOR_CODE = "fromLocatorCode";
    public static final String FIELD_TO_LOCATOR_CODE = "toLocatorCode";
    public static final String FIELD_EXCESS_SETTING = "excessSetting";
    public static final String FIELD_REMARK1 = "remark1";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_BOM_RESERVE_NUM = "bomReserveNum";
    public static final String FIELD_BOM_RESERVE_LINE_NUM = "bomReserveLineNum";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_SPEC_STOCK_FLAG = "specStockFlag";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_TENANT_ID = "tenantId";
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
    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;
    @ApiModelProperty(value = "工厂")
    private String siteCode;
    @ApiModelProperty(value = "状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "申请人/领料人")
    private String person;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "生产订单号")
    private String workOrderNum;
    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;
    @ApiModelProperty(value = "指令编号")
    private String instructionNum;
    @ApiModelProperty(value = "行号")
    private String number;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "指令类型")
    private String instructionType;
    @ApiModelProperty(value = "来源工厂")
    private String fromSiteCode;
    @ApiModelProperty(value = "目标工厂")
    private String toSiteCode;
    @ApiModelProperty(value = "状态")
    private String instructionStatus;
    @ApiModelProperty(value = "来源仓库")
    private String fromLocatorCode;
    @ApiModelProperty(value = "目标仓库")
    private String toLocatorCode;
    @ApiModelProperty(value = "超发设置")
    private String excessSetting;
    @ApiModelProperty(value = "备注")
    private String remark1;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "预留项目号")
    private String bomReserveNum;
    @ApiModelProperty(value = "预留项目行号")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "特殊库存，销售订单库存")
    private String specStockFlag;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Long batchId;
    @ApiModelProperty(value = "数据处理状态")
    private String status;
    @ApiModelProperty(value = "数据处理消息")
    private String message;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long cid;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
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

    public CostcenterDocIface setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
        return this;
    }

    /**
     * @return 单据号
     */
    public String getInstructionDocNum() {
        return instructionDocNum;
    }

    public CostcenterDocIface setInstructionDocNum(String instructionDocNum) {
        this.instructionDocNum = instructionDocNum;
        return this;
    }

    /**
     * @return 工厂
     */
    public String getSiteCode() {
        return siteCode;
    }

    public CostcenterDocIface setSiteCode(String siteCode) {
        this.siteCode = siteCode;
        return this;
    }

    /**
     * @return 状态
     */
    public String getInstructionDocStatus() {
        return instructionDocStatus;
    }

    public CostcenterDocIface setInstructionDocStatus(String instructionDocStatus) {
        this.instructionDocStatus = instructionDocStatus;
        return this;
    }

    /**
     * @return 单据类型
     */
    public String getInstructionDocType() {
        return instructionDocType;
    }

    public CostcenterDocIface setInstructionDocType(String instructionDocType) {
        this.instructionDocType = instructionDocType;
        return this;
    }

    /**
     * @return 申请人/领料人
     */
    public String getPerson() {
        return person;
    }

    public CostcenterDocIface setPerson(String person) {
        this.person = person;
        return this;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public CostcenterDocIface setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * @return 生产订单号
     */
    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public CostcenterDocIface setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
        return this;
    }

    /**
     * @return 来源系统
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    public CostcenterDocIface setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
        return this;
    }

    /**
     * @return 指令编号
     */
    public String getInstructionNum() {
        return instructionNum;
    }

    public CostcenterDocIface setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
        return this;
    }

    /**
     * @return 行号
     */
    public String getNumber() {
        return number;
    }

    public CostcenterDocIface setNumber(String number) {
        this.number = number;
        return this;
    }

    /**
     * @return 物料
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public CostcenterDocIface setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    /**
     * @return 需求数量
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    public CostcenterDocIface setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * @return 单位
     */
    public String getUomCode() {
        return uomCode;
    }

    public CostcenterDocIface setUomCode(String uomCode) {
        this.uomCode = uomCode;
        return this;
    }

    /**
     * @return 指令类型
     */
    public String getInstructionType() {
        return instructionType;
    }

    public CostcenterDocIface setInstructionType(String instructionType) {
        this.instructionType = instructionType;
        return this;
    }

    /**
     * @return 来源工厂
     */
    public String getFromSiteCode() {
        return fromSiteCode;
    }

    public CostcenterDocIface setFromSiteCode(String fromSiteCode) {
        this.fromSiteCode = fromSiteCode;
        return this;
    }

    /**
     * @return 目标工厂
     */
    public String getToSiteCode() {
        return toSiteCode;
    }

    public CostcenterDocIface setToSiteCode(String toSiteCode) {
        this.toSiteCode = toSiteCode;
        return this;
    }

    /**
     * @return 状态
     */
    public String getInstructionStatus() {
        return instructionStatus;
    }

    public CostcenterDocIface setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
        return this;
    }

    /**
     * @return 来源仓库
     */
    public String getFromLocatorCode() {
        return fromLocatorCode;
    }

    public CostcenterDocIface setFromLocatorCode(String fromLocatorCode) {
        this.fromLocatorCode = fromLocatorCode;
        return this;
    }

    /**
     * @return 目标仓库
     */
    public String getToLocatorCode() {
        return toLocatorCode;
    }

    public CostcenterDocIface setToLocatorCode(String toLocatorCode) {
        this.toLocatorCode = toLocatorCode;
        return this;
    }

    /**
     * @return 超发设置
     */
    public String getExcessSetting() {
        return excessSetting;
    }

    public CostcenterDocIface setExcessSetting(String excessSetting) {
        this.excessSetting = excessSetting;
        return this;
    }

    /**
     * @return 备注
     */
    public String getRemark1() {
        return remark1;
    }

    public CostcenterDocIface setRemark1(String remark1) {
        this.remark1 = remark1;
        return this;
    }

    /**
     * @return 销售订单
     */
    public String getSoNum() {
        return soNum;
    }

    public CostcenterDocIface setSoNum(String soNum) {
        this.soNum = soNum;
        return this;
    }

    /**
     * @return 销售订单行号
     */
    public String getSoLineNum() {
        return soLineNum;
    }

    public CostcenterDocIface setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
        return this;
    }

    /**
     * @return 预留项目号
     */
    public String getBomReserveNum() {
        return bomReserveNum;
    }

    public CostcenterDocIface setBomReserveNum(String bomReserveNum) {
        this.bomReserveNum = bomReserveNum;
        return this;
    }

    /**
     * @return 预留项目行号
     */
    public String getBomReserveLineNum() {
        return bomReserveLineNum;
    }

    public CostcenterDocIface setBomReserveLineNum(String bomReserveLineNum) {
        this.bomReserveLineNum = bomReserveLineNum;
        return this;
    }

    /**
     * @return 物料版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public CostcenterDocIface setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
        return this;
    }

    /**
     * @return 特殊库存，销售订单库存
     */
    public String getSpecStockFlag() {
        return specStockFlag;
    }

    public CostcenterDocIface setSpecStockFlag(String specStockFlag) {
        this.specStockFlag = specStockFlag;
        return this;
    }

    /**
     * @return 数据批次ID
     */
    public Long getBatchId() {
        return batchId;
    }

    public CostcenterDocIface setBatchId(Long batchId) {
        this.batchId = batchId;
        return this;
    }

    /**
     * @return 数据处理状态
     */
    public String getStatus() {
        return status;
    }

    public CostcenterDocIface setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return 数据处理消息
     */
    public String getMessage() {
        return message;
    }

    public CostcenterDocIface setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public CostcenterDocIface setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public CostcenterDocIface setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public CostcenterDocIface setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public CostcenterDocIface setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public CostcenterDocIface setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public CostcenterDocIface setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public CostcenterDocIface setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public CostcenterDocIface setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public CostcenterDocIface setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public CostcenterDocIface setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public CostcenterDocIface setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public CostcenterDocIface setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public CostcenterDocIface setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public CostcenterDocIface setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public CostcenterDocIface setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public CostcenterDocIface setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public CostcenterDocIface setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public CostcenterDocIface setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
