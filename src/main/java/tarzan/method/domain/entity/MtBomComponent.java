package tarzan.method.domain.entity;

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
 * 装配清单行
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单行")

@ModifyAudit

@Table(name = "mt_bom_component")
@CustomPrimary
public class MtBomComponent extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_LINE_NUMBER = "lineNumber";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_BOM_COMPONENT_TYPE = "bomComponentType";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_KEY_MATERIAL_FLAG = "keyMaterialFlag";
    public static final String FIELD_ASSEMBLE_METHOD = "assembleMethod";
    public static final String FIELD_ASSEMBLE_AS_REQ_FLAG = "assembleAsReqFlag";
    public static final String FIELD_ATTRITION_POLICY = "attritionPolicy";
    public static final String FIELD_ATTRITION_CHANCE = "attritionChance";
    public static final String FIELD_ATTRITION_QTY = "attritionQty";
    public static final String FIELD_COPIED_FROM_COMPONENT_ID = "copiedFromComponentId";
    public static final String FIELD_ISSUED_LOCATOR_ID = "issuedLocatorId";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_CID = "cid";

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
    @ApiModelProperty("物料清单行ID")
    @Id
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "物料清单头ID", required = true)
    @NotBlank
    @Where
    private String bomId;
    @ApiModelProperty(value = "序号", required = true)
    @NotNull
    @Where
    private Long lineNumber;
    @ApiModelProperty(value = "组件物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "组件类型，如装配、拆卸、联产品等", required = true)
    @NotBlank
    @Where
    private String bomComponentType;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效生产")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "数量，六位小数", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "关键物料标识")
    @Where
    private String keyMaterialFlag;
    @ApiModelProperty(value = "装配方式(投料/上料位反冲/库存反冲)")
    @Where
    private String assembleMethod;
    @ApiModelProperty(value = "是否按需求数量装配")
    @Where
    private String assembleAsReqFlag;
    @ApiModelProperty(value = "损耗策略，1按固定值，2按百分比，3固定值+百分比")
    @Where
    private String attritionPolicy;
    @ApiModelProperty(value = "损耗百分比，两位小数")
    @Where
    private Double attritionChance;
    @ApiModelProperty(value = "固定损耗值，六位小数")
    @Where
    private Double attritionQty;
    @ApiModelProperty(value = "复制的来源装配清单行ID")
    @Where
    private String copiedFromComponentId;
    @ApiModelProperty(value = "发料库位ID")
    @Where
    private String issuedLocatorId;
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
     * @return 物料清单行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 物料清单头ID
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 序号
     */
    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
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
     * @return 组件类型，如装配、拆卸、联产品等
     */
    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    /**
     * @return 数量，六位小数
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 关键物料标识
     */
    public String getKeyMaterialFlag() {
        return keyMaterialFlag;
    }

    public void setKeyMaterialFlag(String keyMaterialFlag) {
        this.keyMaterialFlag = keyMaterialFlag;
    }

    /**
     * @return 装配方式(投料/上料位反冲/库存反冲)
     */
    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    /**
     * @return 是否按需求数量装配
     */
    public String getAssembleAsReqFlag() {
        return assembleAsReqFlag;
    }

    public void setAssembleAsReqFlag(String assembleAsReqFlag) {
        this.assembleAsReqFlag = assembleAsReqFlag;
    }

    /**
     * @return 损耗策略，1按固定值，2按百分比，3固定值+百分比
     */
    public String getAttritionPolicy() {
        return attritionPolicy;
    }

    public void setAttritionPolicy(String attritionPolicy) {
        this.attritionPolicy = attritionPolicy;
    }

    /**
     * @return 损耗百分比，两位小数
     */
    public Double getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(Double attritionChance) {
        this.attritionChance = attritionChance;
    }

    /**
     * @return 固定损耗值，六位小数
     */
    public Double getAttritionQty() {
        return attritionQty;
    }

    public void setAttritionQty(Double attritionQty) {
        this.attritionQty = attritionQty;
    }

    /**
     * @return 复制的来源装配清单行ID
     */
    public String getCopiedFromComponentId() {
        return copiedFromComponentId;
    }

    public void setCopiedFromComponentId(String copiedFromComponentId) {
        this.copiedFromComponentId = copiedFromComponentId;
    }

    /**
     * @return 发料库位ID
     */
    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
