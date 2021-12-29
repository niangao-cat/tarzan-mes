package tarzan.actual.domain.entity;

import java.io.Serializable;

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
 * 指令实绩表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("指令实绩表")

@ModifyAudit

@Table(name = "mt_instruction_actual")
@CustomPrimary
public class MtInstructionActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ACTUAL_ID = "actualId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_SOURCE_ORDER_TYPE = "sourceOrderType";
    public static final String FIELD_SOURCE_ORDER_ID = "sourceOrderId";
    public static final String FIELD_FROM_SITE_ID = "fromSiteId";
    public static final String FIELD_TO_SITE_ID = "toSiteId";
    public static final String FIELD_FROM_LOCATOR_ID = "fromLocatorId";
    public static final String FIELD_TO_LOCATOR_ID = "toLocatorId";
    public static final String FIELD_FROM_OWNER_TYPE = "fromOwnerType";
    public static final String FIELD_TO_OWNER_TYPE = "toOwnerType";
    public static final String FIELD_COST_CENTER_ID = "costCenterId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_ACTUAL_QTY = "actualQty";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 619226813654809852L;

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
    @ApiModelProperty("指令实绩汇总id")
    @Id
    @Where
    private String actualId;
    @ApiModelProperty(value = "来源指令id")
    @Where
    private String instructionId;
    @ApiModelProperty(value = "指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）", required = true)
    @NotBlank
    @Where
    private String instructionType;
    @ApiModelProperty(value = "业务类型", required = true)
    @NotBlank
    @Where
    private String businessType;
    @ApiModelProperty(value = "物料id")
    @Where
    private String materialId;
    @ApiModelProperty(value = "单位")
    @Where
    private String uomId;
    @ApiModelProperty(value = "eoid")
    @Where
    private String eoId;
    @ApiModelProperty(value = "来源订单类型（PO，SO）")
    @Where
    private String sourceOrderType;
    @ApiModelProperty(value = "来源订单ID")
    @Where
    private String sourceOrderId;
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
    @ApiModelProperty(value = "所有者类型")
    @Where
    private String fromOwnerType;
    @ApiModelProperty(value = "目标所有者类型")
    @Where
    private String toOwnerType;
    @ApiModelProperty(value = "成本中心")
    @Where
    private String costCenterId;
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
    @ApiModelProperty(value = "实绩数量", required = true)
    @NotNull
    @Where
    private Double actualQty;
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
     * @return 指令实绩汇总id
     */
    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    /**
     * @return 来源指令id
     */
    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
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
     * @return 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return 物料id
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
     * @return eoid
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 来源订单类型（PO，SO）
     */
    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    /**
     * @return 来源订单ID
     */
    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
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
     * @return 成本中心
     */
    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
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
     * @return 实绩数量
     */
    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
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
