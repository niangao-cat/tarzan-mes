package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionActualVO3
 * @description
 * @date 2020年01月08日 15:18
 */
public class MtInstructionActualVO3 implements Serializable {
    private static final long serialVersionUID = -6097532196405757777L;

    @ApiModelProperty(value = "指令实绩id")
    private String actualId;
    @ApiModelProperty(value = "来源指令id")
    private String instructionId;
    @ApiModelProperty(value = "指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）", required = true)
    private String instructionType;
    @ApiModelProperty(value = "业务类型", required = true)
    private String businessType;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "单位")
    private String uomId;
    @ApiModelProperty(value = "eoid")
    private String eoId;
    @ApiModelProperty(value = "来源订单类型（PO，SO）")
    private String sourceOrderType;
    @ApiModelProperty(value = "来源订单ID")
    private String sourceOrderId;
    @ApiModelProperty(value = "来源站点id")
    private String fromSiteId;
    @ApiModelProperty(value = "目标站点id")
    private String toSiteId;
    @ApiModelProperty(value = "来源库位id")
    private String fromLocatorId;
    @ApiModelProperty(value = "目标库位id")
    private String toLocatorId;
    @ApiModelProperty(value = "成本中心")
    private String costCenterId;
    @ApiModelProperty(value = "供应商（指令指定供应商时）")
    private String supplierId;
    @ApiModelProperty(value = "供应商地点id")
    private String supplierSiteId;
    @ApiModelProperty(value = "客户（指令指定客户）")
    private String customerId;
    @ApiModelProperty(value = "客户地点id")
    private String customerSiteId;
    @ApiModelProperty(value = "实绩数量", required = true)
    private Double actualQty;

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getFromSiteId() {
        return fromSiteId;
    }

    public void setFromSiteId(String fromSiteId) {
        this.fromSiteId = fromSiteId;
    }

    public String getToSiteId() {
        return toSiteId;
    }

    public void setToSiteId(String toSiteId) {
        this.toSiteId = toSiteId;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }
}
