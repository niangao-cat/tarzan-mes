package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BOM接口表
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
@ApiModel("BOM接口表")
@ModifyAudit
@Table(name = "mt_bom_component_iface")
@CustomPrimary
public class MtBomComponentIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_BOM_CODE = "bomCode";
    public static final String FIELD_BOM_ALTERNATE = "bomAlternate";
    public static final String FIELD_BOM_DESCRIPTION = "bomDescription";
    public static final String FIELD_BOM_START_DATE = "bomStartDate";
    public static final String FIELD_BOM_END_DATE = "bomEndDate";
    public static final String FIELD_BOM_STATUS = "bomStatus";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_BOM_OBJECT_TYPE = "bomObjectType";
    public static final String FIELD_BOM_OBJECT_CODE = "bomObjectCode";
    public static final String FIELD_STANDARD_QTY = "standardQty";
    public static final String FIELD_COMPONENT_LINE_NUM = "componentLineNum";
    public static final String FIELD_COMPONENT_ITEM_CODE = "componentItemCode";
    public static final String FIELD_OPERATION_SEQUENCE = "operationSequence";
    public static final String FIELD_BOM_USAGE = "bomUsage";
    public static final String FIELD_COMPONENT_SHRINKAGE = "componentShrinkage";
    public static final String FIELD_WIP_SUPPLY_TYPE = "wipSupplyType";
    public static final String FIELD_COMPONENT_START_DATE = "componentStartDate";
    public static final String FIELD_COMPONENT_END_DATE = "componentEndDate";
    public static final String FIELD_SUBSTITUTE_ITEM_CODE = "substituteItemCode";
    public static final String FIELD_SUBSTITUTE_ITEM_USAGE = "substituteItemUsage";
    public static final String FIELD_SUBSTITUTE_GROUP = "substituteGroup";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_ISSUE_LOCATOR_CODE = "issueLocatorCode";
    public static final String FIELD_ASSEMBLE_METHOD = "assembleMethod";
    public static final String FIELD_BOM_COMPONENT_TYPE = "bomComponentType";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_HEAD_ATTRIBUTE1 = "headAttribute1";
    public static final String FIELD_HEAD_ATTRIBUTE2 = "headAttribute2";
    public static final String FIELD_HEAD_ATTRIBUTE3 = "headAttribute3";
    public static final String FIELD_HEAD_ATTRIBUTE4 = "headAttribute4";
    public static final String FIELD_HEAD_ATTRIBUTE5 = "headAttribute5";
    public static final String FIELD_HEAD_ATTRIBUTE6 = "headAttribute6";
    public static final String FIELD_HEAD_ATTRIBUTE7 = "headAttribute7";
    public static final String FIELD_HEAD_ATTRIBUTE8 = "headAttribute8";
    public static final String FIELD_HEAD_ATTRIBUTE9 = "headAttribute9";
    public static final String FIELD_HEAD_ATTRIBUTE10 = "headAttribute10";
    public static final String FIELD_HEAD_ATTRIBUTE11 = "headAttribute11";
    public static final String FIELD_HEAD_ATTRIBUTE12 = "headAttribute12";
    public static final String FIELD_HEAD_ATTRIBUTE13 = "headAttribute13";
    public static final String FIELD_HEAD_ATTRIBUTE14 = "headAttribute14";
    public static final String FIELD_HEAD_ATTRIBUTE15 = "headAttribute15";
    public static final String FIELD_LINE_ATTRIBUTE1 = "lineAttribute1";
    public static final String FIELD_LINE_ATTRIBUTE2 = "lineAttribute2";
    public static final String FIELD_LINE_ATTRIBUTE3 = "lineAttribute3";
    public static final String FIELD_LINE_ATTRIBUTE4 = "lineAttribute4";
    public static final String FIELD_LINE_ATTRIBUTE5 = "lineAttribute5";
    public static final String FIELD_LINE_ATTRIBUTE6 = "lineAttribute6";
    public static final String FIELD_LINE_ATTRIBUTE7 = "lineAttribute7";
    public static final String FIELD_LINE_ATTRIBUTE8 = "lineAttribute8";
    public static final String FIELD_LINE_ATTRIBUTE9 = "lineAttribute9";
    public static final String FIELD_LINE_ATTRIBUTE10 = "lineAttribute10";
    public static final String FIELD_LINE_ATTRIBUTE11 = "lineAttribute11";
    public static final String FIELD_LINE_ATTRIBUTE12 = "lineAttribute12";
    public static final String FIELD_LINE_ATTRIBUTE13 = "lineAttribute13";
    public static final String FIELD_LINE_ATTRIBUTE14 = "lineAttribute14";
    public static final String FIELD_LINE_ATTRIBUTE15 = "lineAttribute15";
    public static final String FIELD_LINE_ATTRIBUTE16 = "lineAttribute16";
    public static final String FIELD_LINE_ATTRIBUTE17 = "lineAttribute17";
    public static final String FIELD_LINE_ATTRIBUTE18 = "lineAttribute18";
    public static final String FIELD_LINE_ATTRIBUTE19 = "lineAttribute19";
    public static final String FIELD_LINE_ATTRIBUTE20 = "lineAttribute20";
    public static final String FIELD_LINE_ATTRIBUTE21 = "lineAttribute21";
    public static final String FIELD_LINE_ATTRIBUTE22 = "lineAttribute22";
    public static final String FIELD_LINE_ATTRIBUTE23 = "lineAttribute23";
    public static final String FIELD_LINE_ATTRIBUTE24 = "lineAttribute24";
    public static final String FIELD_LINE_ATTRIBUTE25 = "lineAttribute25";
    public static final String FIELD_LINE_ATTRIBUTE26 = "lineAttribute26";
    public static final String FIELD_LINE_ATTRIBUTE27 = "lineAttribute27";
    public static final String FIELD_LINE_ATTRIBUTE28 = "lineAttribute28";
    public static final String FIELD_LINE_ATTRIBUTE29 = "lineAttribute29";
    public static final String FIELD_LINE_ATTRIBUTE30 = "lineAttribute30";
    private static final long serialVersionUID = -4589055419212248789L;


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
    @ApiModelProperty(value = "工厂CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "BOM代码（SAP传值）", required = true)
    @NotBlank
    @Where
    private String bomCode;
    @ApiModelProperty(value = "替代项（考虑SAP将计数器放在此字段）")
    @Where
    private String bomAlternate;
    @ApiModelProperty(value = "BOM说明（Oracle 直接将装配件描述存入该字段）", required = true)
    @NotBlank
    @Where
    private String bomDescription;
    @ApiModelProperty(value = "生效日期", required = true)
    @NotNull
    @Where
    private Date bomStartDate;
    @ApiModelProperty(value = "失效日期")
    @Where
    private Date bomEndDate;
    @ApiModelProperty(value = "BOM状态（SAP状态）")
    @Where
    private String bomStatus;
    @ApiModelProperty(value = "是否有效（SAP删除标记）")
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "物料：MATERIAL，工单:WO", required = true)
    @NotBlank
    @Where
    private String bomObjectType;
    @ApiModelProperty(value = "如果BOM_OBJECT_TYPE=MATERIAL，则该字段写入物料编码，如果BOM_OBJECT_TYPE=WO，则该字段写入WO号", required = true)
    @NotBlank
    @Where
    private String bomObjectCode;
    @ApiModelProperty(value = "基准数量（oracle 写1）", required = true)
    @NotNull
    @Where
    private Double standardQty;
    @ApiModelProperty(value = "物料序号", required = true)
    @NotNull
    @Where
    private Long componentLineNum;
    @ApiModelProperty(value = "组件物料编码（Oracle为空）", required = true)
    @NotBlank
    @Where
    private String componentItemCode;
    @ApiModelProperty(value = "工序号（SAP为空）")
    @Where
    private String operationSequence;
    @ApiModelProperty(value = "组件用量", required = true)
    @NotNull
    @Where
    private Double bomUsage;
    @ApiModelProperty(value = "损耗率")
    @Where
    private Double componentShrinkage;
    @ApiModelProperty(value = "供应类型 （SAP为空）")
    @Where
    private String wipSupplyType;
    @ApiModelProperty(value = "组件生效日期", required = true)
    @NotNull
    @Where
    private Date componentStartDate;
    @ApiModelProperty(value = "组件失效日期")
    @Where
    private Date componentEndDate;
    @ApiModelProperty(value = "替代组件物料编码")
    @Where
    private String substituteItemCode;
    @ApiModelProperty(value = "替代组件用量")
    @Where
    private Double substituteItemUsage;
    @ApiModelProperty(value = "替代组（ Oracle 为空）")
    @Where
    private String substituteGroup;
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
    @ApiModelProperty(value = "更新方式（ 允许有两个值UPDATE，ALL)")
    @Where
    private String updateMethod;
    @ApiModelProperty(value = "发库位编码")
    @Where
    private String issueLocatorCode;
    @ApiModelProperty(value = "")
    @Where
    private String assembleMethod;
    @ApiModelProperty(value = "")
    @Where
    private String bomComponentType;
    @Cid
    @Where
    private Long cid;
    @ApiModelProperty(value = "")
    @Where
    private String attributeCategory;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute15;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute15;
    @ApiModelProperty(value = "")
    private String lineAttribute16;
    @ApiModelProperty(value = "")
    private String lineAttribute17;
    @ApiModelProperty(value = "")
    private String lineAttribute18;
    @ApiModelProperty(value = "")
    private String lineAttribute19;
    @ApiModelProperty(value = "")
    private String lineAttribute20;
    @ApiModelProperty(value = "")
    private String lineAttribute21;
    @ApiModelProperty(value = "")
    private String lineAttribute22;
    @ApiModelProperty(value = "")
    private String lineAttribute23;
    @ApiModelProperty(value = "")
    private String lineAttribute24;
    @ApiModelProperty(value = "")
    private String lineAttribute25;
    @ApiModelProperty(value = "")
    private String lineAttribute26;
    @ApiModelProperty(value = "")
    private String lineAttribute27;
    @ApiModelProperty(value = "")
    private String lineAttribute28;
    @ApiModelProperty(value = "")
    private String lineAttribute29;
    @ApiModelProperty(value = "")
    private String lineAttribute30;

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
     * @return 工厂CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return BOM代码（SAP传值）
     */
    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    /**
     * @return 替代项（考虑SAP将计数器放在此字段）
     */
    public String getBomAlternate() {
        return bomAlternate;
    }

    public void setBomAlternate(String bomAlternate) {
        this.bomAlternate = bomAlternate;
    }

    /**
     * @return BOM说明（Oracle 直接将装配件描述存入该字段）
     */
    public String getBomDescription() {
        return bomDescription;
    }

    public void setBomDescription(String bomDescription) {
        this.bomDescription = bomDescription;
    }

    /**
     * @return 生效日期
     */
    public Date getBomStartDate() {
        if (bomStartDate != null) {
            return (Date) bomStartDate.clone();
        } else {
            return null;
        }
    }

    public void setBomStartDate(Date bomStartDate) {
        if (bomStartDate == null) {
            this.bomStartDate = null;
        } else {
            this.bomStartDate = (Date) bomStartDate.clone();
        }
    }

    /**
     * @return 失效日期
     */
    public Date getBomEndDate() {
        if (bomEndDate != null) {
            return (Date) bomEndDate.clone();
        } else {
            return null;
        }
    }

    public void setBomEndDate(Date bomEndDate) {
        if (bomEndDate == null) {
            this.bomEndDate = null;
        } else {
            this.bomEndDate = (Date) bomEndDate.clone();
        }
    }

    /**
     * @return BOM状态（SAP状态）
     */
    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
    }

    /**
     * @return 是否有效（SAP删除标记）
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 物料：MATERIAL，工单:WO
     */
    public String getBomObjectType() {
        return bomObjectType;
    }

    public void setBomObjectType(String bomObjectType) {
        this.bomObjectType = bomObjectType;
    }

    /**
     * @return 如果BOM_OBJECT_TYPE=MATERIAL，则该字段写入物料编码，如果BOM_OBJECT_TYPE=WO，则该字段写入WO号
     */
    public String getBomObjectCode() {
        return bomObjectCode;
    }

    public void setBomObjectCode(String bomObjectCode) {
        this.bomObjectCode = bomObjectCode;
    }

    /**
     * @return 基准数量（oracle 写1）
     */
    public Double getStandardQty() {
        return standardQty;
    }

    public void setStandardQty(Double standardQty) {
        this.standardQty = standardQty;
    }

    /**
     * @return 物料序号
     */
    public Long getComponentLineNum() {
        return componentLineNum;
    }

    public void setComponentLineNum(Long componentLineNum) {
        this.componentLineNum = componentLineNum;
    }

    /**
     * @return 组件物料编码（Oracle为空）
     */
    public String getComponentItemCode() {
        return componentItemCode;
    }

    public void setComponentItemCode(String componentItemCode) {
        this.componentItemCode = componentItemCode;
    }

    /**
     * @return 工序号（SAP为空）
     */
    public String getOperationSequence() {
        return operationSequence;
    }

    public void setOperationSequence(String operationSequence) {
        this.operationSequence = operationSequence;
    }

    /**
     * @return 组件用量
     */
    public Double getBomUsage() {
        return bomUsage;
    }

    public void setBomUsage(Double bomUsage) {
        this.bomUsage = bomUsage;
    }

    /**
     * @return 损耗率
     */
    public Double getComponentShrinkage() {
        return componentShrinkage;
    }

    public void setComponentShrinkage(Double componentShrinkage) {
        this.componentShrinkage = componentShrinkage;
    }

    /**
     * @return 供应类型 （SAP为空）
     */
    public String getWipSupplyType() {
        return wipSupplyType;
    }

    public void setWipSupplyType(String wipSupplyType) {
        this.wipSupplyType = wipSupplyType;
    }

    /**
     * @return 组件生效日期
     */
    public Date getComponentStartDate() {
        if (componentStartDate != null) {
            return (Date) componentStartDate.clone();
        } else {
            return null;
        }
    }

    public void setComponentStartDate(Date componentStartDate) {
        if (componentStartDate == null) {
            this.componentStartDate = null;
        } else {
            this.componentStartDate = (Date) componentStartDate.clone();
        }
    }

    /**
     * @return 组件失效日期
     */
    public Date getComponentEndDate() {
        if (componentEndDate != null) {
            return (Date) componentEndDate.clone();
        } else {
            return null;
        }
    }

    public void setComponentEndDate(Date componentEndDate) {
        if (componentEndDate == null) {
            this.componentEndDate = null;
        } else {
            this.componentEndDate = (Date) componentEndDate.clone();
        }
    }

    /**
     * @return 替代组件物料编码
     */
    public String getSubstituteItemCode() {
        return substituteItemCode;
    }

    public void setSubstituteItemCode(String substituteItemCode) {
        this.substituteItemCode = substituteItemCode;
    }

    /**
     * @return 替代组件用量
     */
    public Double getSubstituteItemUsage() {
        return substituteItemUsage;
    }

    public void setSubstituteItemUsage(Double substituteItemUsage) {
        this.substituteItemUsage = substituteItemUsage;
    }

    /**
     * @return 替代组（ Oracle 为空）
     */
    public String getSubstituteGroup() {
        return substituteGroup;
    }

    public void setSubstituteGroup(String substituteGroup) {
        this.substituteGroup = substituteGroup;
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

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
    }

    public String getIssueLocatorCode() {
        return issueLocatorCode;
    }

    public void setIssueLocatorCode(String issueLocatorCode) {
        this.issueLocatorCode = issueLocatorCode;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
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
    public String getHeadAttribute1() {
        return headAttribute1;
    }

    public void setHeadAttribute1(String headAttribute1) {
        this.headAttribute1 = headAttribute1;
    }

    /**
     * @return
     */
    public String getHeadAttribute2() {
        return headAttribute2;
    }

    public void setHeadAttribute2(String headAttribute2) {
        this.headAttribute2 = headAttribute2;
    }

    /**
     * @return
     */
    public String getHeadAttribute3() {
        return headAttribute3;
    }

    public void setHeadAttribute3(String headAttribute3) {
        this.headAttribute3 = headAttribute3;
    }

    /**
     * @return
     */
    public String getHeadAttribute4() {
        return headAttribute4;
    }

    public void setHeadAttribute4(String headAttribute4) {
        this.headAttribute4 = headAttribute4;
    }

    /**
     * @return
     */
    public String getHeadAttribute5() {
        return headAttribute5;
    }

    public void setHeadAttribute5(String headAttribute5) {
        this.headAttribute5 = headAttribute5;
    }

    /**
     * @return
     */
    public String getHeadAttribute6() {
        return headAttribute6;
    }

    public void setHeadAttribute6(String headAttribute6) {
        this.headAttribute6 = headAttribute6;
    }

    /**
     * @return
     */
    public String getHeadAttribute7() {
        return headAttribute7;
    }

    public void setHeadAttribute7(String headAttribute7) {
        this.headAttribute7 = headAttribute7;
    }

    /**
     * @return
     */
    public String getHeadAttribute8() {
        return headAttribute8;
    }

    public void setHeadAttribute8(String headAttribute8) {
        this.headAttribute8 = headAttribute8;
    }

    /**
     * @return
     */
    public String getHeadAttribute9() {
        return headAttribute9;
    }

    public void setHeadAttribute9(String headAttribute9) {
        this.headAttribute9 = headAttribute9;
    }

    /**
     * @return
     */
    public String getHeadAttribute10() {
        return headAttribute10;
    }

    public void setHeadAttribute10(String headAttribute10) {
        this.headAttribute10 = headAttribute10;
    }

    /**
     * @return
     */
    public String getHeadAttribute11() {
        return headAttribute11;
    }

    public void setHeadAttribute11(String headAttribute11) {
        this.headAttribute11 = headAttribute11;
    }

    /**
     * @return
     */
    public String getHeadAttribute12() {
        return headAttribute12;
    }

    public void setHeadAttribute12(String headAttribute12) {
        this.headAttribute12 = headAttribute12;
    }

    /**
     * @return
     */
    public String getHeadAttribute13() {
        return headAttribute13;
    }

    public void setHeadAttribute13(String headAttribute13) {
        this.headAttribute13 = headAttribute13;
    }

    /**
     * @return
     */
    public String getHeadAttribute14() {
        return headAttribute14;
    }

    public void setHeadAttribute14(String headAttribute14) {
        this.headAttribute14 = headAttribute14;
    }

    /**
     * @return
     */
    public String getHeadAttribute15() {
        return headAttribute15;
    }

    public void setHeadAttribute15(String headAttribute15) {
        this.headAttribute15 = headAttribute15;
    }

    /**
     * @return
     */
    public String getLineAttribute1() {
        return lineAttribute1;
    }

    public void setLineAttribute1(String lineAttribute1) {
        this.lineAttribute1 = lineAttribute1;
    }

    /**
     * @return
     */
    public String getLineAttribute2() {
        return lineAttribute2;
    }

    public void setLineAttribute2(String lineAttribute2) {
        this.lineAttribute2 = lineAttribute2;
    }

    /**
     * @return
     */
    public String getLineAttribute3() {
        return lineAttribute3;
    }

    public void setLineAttribute3(String lineAttribute3) {
        this.lineAttribute3 = lineAttribute3;
    }

    /**
     * @return
     */
    public String getLineAttribute4() {
        return lineAttribute4;
    }

    public void setLineAttribute4(String lineAttribute4) {
        this.lineAttribute4 = lineAttribute4;
    }

    /**
     * @return
     */
    public String getLineAttribute5() {
        return lineAttribute5;
    }

    public void setLineAttribute5(String lineAttribute5) {
        this.lineAttribute5 = lineAttribute5;
    }

    /**
     * @return
     */
    public String getLineAttribute6() {
        return lineAttribute6;
    }

    public void setLineAttribute6(String lineAttribute6) {
        this.lineAttribute6 = lineAttribute6;
    }

    /**
     * @return
     */
    public String getLineAttribute7() {
        return lineAttribute7;
    }

    public void setLineAttribute7(String lineAttribute7) {
        this.lineAttribute7 = lineAttribute7;
    }

    /**
     * @return
     */
    public String getLineAttribute8() {
        return lineAttribute8;
    }

    public void setLineAttribute8(String lineAttribute8) {
        this.lineAttribute8 = lineAttribute8;
    }

    /**
     * @return
     */
    public String getLineAttribute9() {
        return lineAttribute9;
    }

    public void setLineAttribute9(String lineAttribute9) {
        this.lineAttribute9 = lineAttribute9;
    }

    /**
     * @return
     */
    public String getLineAttribute10() {
        return lineAttribute10;
    }

    public void setLineAttribute10(String lineAttribute10) {
        this.lineAttribute10 = lineAttribute10;
    }

    /**
     * @return
     */
    public String getLineAttribute11() {
        return lineAttribute11;
    }

    public void setLineAttribute11(String lineAttribute11) {
        this.lineAttribute11 = lineAttribute11;
    }

    /**
     * @return
     */
    public String getLineAttribute12() {
        return lineAttribute12;
    }

    public void setLineAttribute12(String lineAttribute12) {
        this.lineAttribute12 = lineAttribute12;
    }

    /**
     * @return
     */
    public String getLineAttribute13() {
        return lineAttribute13;
    }

    public void setLineAttribute13(String lineAttribute13) {
        this.lineAttribute13 = lineAttribute13;
    }

    /**
     * @return
     */
    public String getLineAttribute14() {
        return lineAttribute14;
    }

    public void setLineAttribute14(String lineAttribute14) {
        this.lineAttribute14 = lineAttribute14;
    }

    /**
     * @return
     */
    public String getLineAttribute15() {
        return lineAttribute15;
    }

    public void setLineAttribute15(String lineAttribute15) {
        this.lineAttribute15 = lineAttribute15;
    }

    public String getLineAttribute16() {
        return lineAttribute16;
    }

    public void setLineAttribute16(String lineAttribute16) {
        this.lineAttribute16 = lineAttribute16;
    }

    public String getLineAttribute17() {
        return lineAttribute17;
    }

    public void setLineAttribute17(String lineAttribute17) {
        this.lineAttribute17 = lineAttribute17;
    }

    public String getLineAttribute18() {
        return lineAttribute18;
    }

    public void setLineAttribute18(String lineAttribute18) {
        this.lineAttribute18 = lineAttribute18;
    }

    public String getLineAttribute19() {
        return lineAttribute19;
    }

    public void setLineAttribute19(String lineAttribute19) {
        this.lineAttribute19 = lineAttribute19;
    }

    public String getLineAttribute20() {
        return lineAttribute20;
    }

    public void setLineAttribute20(String lineAttribute20) {
        this.lineAttribute20 = lineAttribute20;
    }

    public String getLineAttribute21() {
        return lineAttribute21;
    }

    public void setLineAttribute21(String lineAttribute21) {
        this.lineAttribute21 = lineAttribute21;
    }

    public String getLineAttribute22() {
        return lineAttribute22;
    }

    public void setLineAttribute22(String lineAttribute22) {
        this.lineAttribute22 = lineAttribute22;
    }

    public String getLineAttribute23() {
        return lineAttribute23;
    }

    public void setLineAttribute23(String lineAttribute23) {
        this.lineAttribute23 = lineAttribute23;
    }

    public String getLineAttribute24() {
        return lineAttribute24;
    }

    public void setLineAttribute24(String lineAttribute24) {
        this.lineAttribute24 = lineAttribute24;
    }

    public String getLineAttribute25() {
        return lineAttribute25;
    }

    public void setLineAttribute25(String lineAttribute25) {
        this.lineAttribute25 = lineAttribute25;
    }

    public String getLineAttribute26() {
        return lineAttribute26;
    }

    public void setLineAttribute26(String lineAttribute26) {
        this.lineAttribute26 = lineAttribute26;
    }

    public String getLineAttribute27() {
        return lineAttribute27;
    }

    public void setLineAttribute27(String lineAttribute27) {
        this.lineAttribute27 = lineAttribute27;
    }

    public String getLineAttribute28() {
        return lineAttribute28;
    }

    public void setLineAttribute28(String lineAttribute28) {
        this.lineAttribute28 = lineAttribute28;
    }

    public String getLineAttribute29() {
        return lineAttribute29;
    }

    public void setLineAttribute29(String lineAttribute29) {
        this.lineAttribute29 = lineAttribute29;
    }

    public String getLineAttribute30() {
        return lineAttribute30;
    }

    public void setLineAttribute30(String lineAttribute30) {
        this.lineAttribute30 = lineAttribute30;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtBomComponentIface that = (MtBomComponentIface) o;
        return Objects.equals(getTenantId(), that.getTenantId()) && Objects.equals(getIfaceId(), that.getIfaceId())
                && Objects.equals(getPlantCode(), that.getPlantCode())
                && Objects.equals(getBomCode(), that.getBomCode())
                && Objects.equals(getBomAlternate(), that.getBomAlternate())
                && Objects.equals(getBomDescription(), that.getBomDescription())
                && Objects.equals(getBomStartDate(), that.getBomStartDate())
                && Objects.equals(getBomEndDate(), that.getBomEndDate())
                && Objects.equals(getBomStatus(), that.getBomStatus())
                && Objects.equals(getEnableFlag(), that.getEnableFlag())
                && Objects.equals(getBomObjectType(), that.getBomObjectType())
                && Objects.equals(getBomObjectCode(), that.getBomObjectCode())
                && Objects.equals(getStandardQty(), that.getStandardQty())
                && Objects.equals(getComponentLineNum(), that.getComponentLineNum())
                && Objects.equals(getComponentItemCode(), that.getComponentItemCode())
                && Objects.equals(getOperationSequence(), that.getOperationSequence())
                && Objects.equals(getBomUsage(), that.getBomUsage())
                && Objects.equals(getComponentShrinkage(), that.getComponentShrinkage())
                && Objects.equals(getWipSupplyType(), that.getWipSupplyType())
                && Objects.equals(getComponentStartDate(), that.getComponentStartDate())
                && Objects.equals(getComponentEndDate(), that.getComponentEndDate())
                && Objects.equals(getSubstituteItemCode(), that.getSubstituteItemCode())
                && Objects.equals(getSubstituteItemUsage(), that.getSubstituteItemUsage())
                && Objects.equals(getSubstituteGroup(), that.getSubstituteGroup())
                && Objects.equals(getErpCreationDate(), that.getErpCreationDate())
                && Objects.equals(getErpCreatedBy(), that.getErpCreatedBy())
                && Objects.equals(getErpLastUpdatedBy(), that.getErpLastUpdatedBy())
                && Objects.equals(getErpLastUpdateDate(), that.getErpLastUpdateDate())
                && Objects.equals(getBatchId(), that.getBatchId())
                && Objects.equals(getStatus(), that.getStatus())
                && Objects.equals(getMessage(), that.getMessage())
                && Objects.equals(getUpdateMethod(), that.getUpdateMethod())
                && Objects.equals(getIssueLocatorCode(), that.getIssueLocatorCode())
                && Objects.equals(getCid(), that.getCid())
                && Objects.equals(getAttributeCategory(), that.getAttributeCategory())
                && Objects.equals(getHeadAttribute1(), that.getHeadAttribute1())
                && Objects.equals(getHeadAttribute2(), that.getHeadAttribute2())
                && Objects.equals(getHeadAttribute3(), that.getHeadAttribute3())
                && Objects.equals(getHeadAttribute4(), that.getHeadAttribute4())
                && Objects.equals(getHeadAttribute5(), that.getHeadAttribute5())
                && Objects.equals(getHeadAttribute6(), that.getHeadAttribute6())
                && Objects.equals(getHeadAttribute7(), that.getHeadAttribute7())
                && Objects.equals(getHeadAttribute8(), that.getHeadAttribute8())
                && Objects.equals(getHeadAttribute9(), that.getHeadAttribute9())
                && Objects.equals(getHeadAttribute10(), that.getHeadAttribute10())
                && Objects.equals(getHeadAttribute11(), that.getHeadAttribute11())
                && Objects.equals(getHeadAttribute12(), that.getHeadAttribute12())
                && Objects.equals(getHeadAttribute13(), that.getHeadAttribute13())
                && Objects.equals(getHeadAttribute14(), that.getHeadAttribute14())
                && Objects.equals(getHeadAttribute15(), that.getHeadAttribute15())
                && Objects.equals(getLineAttribute1(), that.getLineAttribute1())
                && Objects.equals(getLineAttribute2(), that.getLineAttribute2())
                && Objects.equals(getLineAttribute3(), that.getLineAttribute3())
                && Objects.equals(getLineAttribute4(), that.getLineAttribute4())
                && Objects.equals(getLineAttribute5(), that.getLineAttribute5())
                && Objects.equals(getLineAttribute6(), that.getLineAttribute6())
                && Objects.equals(getLineAttribute7(), that.getLineAttribute7())
                && Objects.equals(getLineAttribute8(), that.getLineAttribute8())
                && Objects.equals(getLineAttribute9(), that.getLineAttribute9())
                && Objects.equals(getLineAttribute10(), that.getLineAttribute10())
                && Objects.equals(getLineAttribute11(), that.getLineAttribute11())
                && Objects.equals(getLineAttribute12(), that.getLineAttribute12())
                && Objects.equals(getLineAttribute13(), that.getLineAttribute13())
                && Objects.equals(getLineAttribute14(), that.getLineAttribute14())
                && Objects.equals(getLineAttribute15(), that.getLineAttribute15())
                && Objects.equals(getLineAttribute16(), that.getLineAttribute16())
                && Objects.equals(getLineAttribute17(), that.getLineAttribute17())
                && Objects.equals(getLineAttribute18(), that.getLineAttribute18())
                && Objects.equals(getLineAttribute19(), that.getLineAttribute19())
                && Objects.equals(getLineAttribute20(), that.getLineAttribute20())
                && Objects.equals(getLineAttribute21(), that.getLineAttribute21())
                && Objects.equals(getLineAttribute22(), that.getLineAttribute22())
                && Objects.equals(getLineAttribute23(), that.getLineAttribute23())
                && Objects.equals(getLineAttribute24(), that.getLineAttribute24())
                && Objects.equals(getLineAttribute25(), that.getLineAttribute25())
                && Objects.equals(getLineAttribute26(), that.getLineAttribute26())
                && Objects.equals(getLineAttribute27(), that.getLineAttribute27())
                && Objects.equals(getLineAttribute28(), that.getLineAttribute28())
                && Objects.equals(getLineAttribute29(), that.getLineAttribute29())
                && Objects.equals(getLineAttribute30(), that.getLineAttribute30());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getIfaceId(), getPlantCode(), getBomCode(), getBomAlternate(),
                getBomDescription(), getBomStartDate(), getBomEndDate(), getBomStatus(), getEnableFlag(),
                getBomObjectType(), getBomObjectCode(), getStandardQty(), getComponentLineNum(),
                getComponentItemCode(), getOperationSequence(), getBomUsage(), getComponentShrinkage(),
                getWipSupplyType(), getComponentStartDate(), getComponentEndDate(), getSubstituteItemCode(),
                getSubstituteItemUsage(), getSubstituteGroup(), getErpCreationDate(), getErpCreatedBy(),
                getErpLastUpdatedBy(), getErpLastUpdateDate(), getBatchId(), getStatus(), getMessage(),
                getUpdateMethod(), getIssueLocatorCode(), getCid(), getAttributeCategory(), getHeadAttribute1(),
                getHeadAttribute2(), getHeadAttribute3(), getHeadAttribute4(), getHeadAttribute5(),
                getHeadAttribute6(), getHeadAttribute7(), getHeadAttribute8(), getHeadAttribute9(),
                getHeadAttribute10(), getHeadAttribute11(), getHeadAttribute12(), getHeadAttribute13(),
                getHeadAttribute14(), getHeadAttribute15(), getLineAttribute1(), getLineAttribute2(),
                getLineAttribute3(), getLineAttribute4(), getLineAttribute5(), getLineAttribute6(),
                getLineAttribute7(), getLineAttribute8(), getLineAttribute9(), getLineAttribute10(),
                getLineAttribute11(), getLineAttribute12(), getLineAttribute13(), getLineAttribute14(),
                getLineAttribute15(),getLineAttribute16(),getLineAttribute17(),getLineAttribute18(),
                getLineAttribute19(),getLineAttribute20(),getLineAttribute21(),getLineAttribute22(),
                getLineAttribute23(),getLineAttribute24(),getLineAttribute25(),getLineAttribute26(),
                getLineAttribute27(),getLineAttribute28(),getLineAttribute29(),getLineAttribute30()
        );
    }


}
