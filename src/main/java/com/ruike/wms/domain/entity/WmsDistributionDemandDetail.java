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
 * 配送需求明细表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 21:05:19
 */
@ApiModel("配送需求明细表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_distribution_demand_detail")
@CustomPrimary
public class WmsDistributionDemandDetail extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DEMAND_DETAIL_ID = "demandDetailId";
    public static final String FIELD_DIST_DEMAND_ID = "distDemandId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_DISPATCH_QTY = "dispatchQty";
    public static final String FIELD_USAGE_QTY = "usageQty";
    public static final String FIELD_REQUIREMENT_QTY = "requirementQty";
    public static final String FIELD_SUBSTITUTE_FLAG = "substituteFlag";
    public static final String FIELD_INSTRUCTION_QTY = "instructionQty";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_DEMAND_RECORD_ID = "demandRecordId";
    public static final String FIELD_SOURCE_DETAIL_ID = "sourceDetailId";
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

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    private String demandDetailId;
    @ApiModelProperty(value = "配送需求ID")
    private String distDemandId;
    @ApiModelProperty(value = "物料标识")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工单ID", required = true)
    @NotBlank
    private String workOrderId;
    @ApiModelProperty(value = "工段ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;
    @ApiModelProperty(value = "标准用量")
    private BigDecimal usageQty;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "替代标识")
    private String substituteFlag;
    @ApiModelProperty(value = "配送单生成数量")
    private BigDecimal instructionQty;
    @ApiModelProperty(value = "配送单ID")
    private String instructionDocId;
    @ApiModelProperty(value = "配送单行ID")
    private String instructionId;
    @ApiModelProperty(value = "组件需求ID")
    private String demandRecordId;
    @ApiModelProperty(value = "来源配送需求明细ID")
    private String sourceDetailId;
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

    @Transient
    @ApiModelProperty("工单号")
    private String workOrderNum;

    @Transient
    @ApiModelProperty("物料编码")
    private String materialCode;

    @Transient
    @ApiModelProperty("组件需求数量")
    private BigDecimal componentDemandQty;

    @Transient
    @ApiModelProperty(value = "配送单号")
    private String instructionDocNum;

    @Transient
    @ApiModelProperty(value = "配送单行号")
    private String instructionLineNum;

    @Transient
    @ApiModelProperty("行号")
    private Integer lineNum;

    @Transient
    @ApiModelProperty("来源行号")
    private Integer sourceLineNum;

    @Transient
    @ApiModelProperty("损耗率系数")
    private BigDecimal attritionChance;


    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键ID，标识唯一一条记录
     */
    public String getDemandDetailId() {
        return demandDetailId;
    }

    public void setDemandDetailId(String demandDetailId) {
        this.demandDetailId = demandDetailId;
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

    /**
     * @return 物料标识
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
     * @return 派工数量
     */
    public BigDecimal getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(BigDecimal dispatchQty) {
        this.dispatchQty = dispatchQty;
    }

    /**
     * @return 标准用量
     */
    public BigDecimal getUsageQty() {
        return usageQty;
    }

    public void setUsageQty(BigDecimal usageQty) {
        this.usageQty = usageQty;
    }

    /**
     * @return 需求数量
     */
    public BigDecimal getRequirementQty() {
        return requirementQty;
    }

    public void setRequirementQty(BigDecimal requirementQty) {
        this.requirementQty = requirementQty;
    }

    /**
     * @return 替代标识
     */
    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    /**
     * @return 配送单生成数量
     */
    public BigDecimal getInstructionQty() {
        return instructionQty;
    }

    public void setInstructionQty(BigDecimal instructionQty) {
        this.instructionQty = instructionQty;
    }

    /**
     * @return 配送单ID
     */
    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    /**
     * @return 配送单行ID
     */
    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    /**
     * @return 组件需求ID
     */
    public String getDemandRecordId() {
        return demandRecordId;
    }

    public void setDemandRecordId(String demandRecordId) {
        this.demandRecordId = demandRecordId;
    }

    /**
     * @return 来源配送需求明细ID
     */
    public String getSourceDetailId() {
        return sourceDetailId;
    }

    public void setSourceDetailId(String sourceDetailId) {
        this.sourceDetailId = sourceDetailId;
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

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public BigDecimal getComponentDemandQty() {
        return componentDemandQty;
    }

    public void setComponentDemandQty(BigDecimal componentDemandQty) {
        this.componentDemandQty = componentDemandQty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
    }

    public String getInstructionDocNum() {
        return instructionDocNum;
    }

    public void setInstructionDocNum(String instructionDocNum) {
        this.instructionDocNum = instructionDocNum;
    }

    public String getInstructionLineNum() {
        return instructionLineNum;
    }

    public void setInstructionLineNum(String instructionLineNum) {
        this.instructionLineNum = instructionLineNum;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public WmsDistributionDemandDetail setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
        return this;
    }

    public Integer getSourceLineNum() {
        return sourceLineNum;
    }

    public WmsDistributionDemandDetail setSourceLineNum(Integer sourceLineNum) {
        this.sourceLineNum = sourceLineNum;
        return this;
    }

    public BigDecimal getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(BigDecimal attritionChance) {
        this.attritionChance = attritionChance;
    }
}
