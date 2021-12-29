package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class MtInstructionVO13 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 4033079007468840106L;

    @ApiModelProperty("指令类型")
    private String instructionType;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("物料批ID列表")
    private List<String> materialLotIdList;

    @ApiModelProperty("容器ID列表")
    private List<String> containerIdList;

    @ApiModelProperty("目标所有者类型")
    private String toOwnerType;

    @ApiModelProperty("目标库位ID")
    private String toLocatorId;

    @ApiModelProperty("成本中心")
    private String costCenterId;

    @ApiModelProperty("事件组Id")
    private String eventRequestId;

    @ApiModelProperty("供应商Id")
    private String supplierId;

    @ApiModelProperty("供应商地点Id")
    private String supplierSiteId;

    @ApiModelProperty("客户Id")
    private String customerId;

    @ApiModelProperty("客户地点Id")
    private String customerSiteId;

    @ApiModelProperty("来源订单类型")
    private String sourceOrderType;

    @ApiModelProperty("来源订单Id")
    private String sourceOrderId;


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

    public List<String> getMaterialLotIdList() {
        return materialLotIdList;
    }

    public void setMaterialLotIdList(List<String> materialLotIdList) {
        this.materialLotIdList = materialLotIdList;
    }

    public List<String> getContainerIdList() {
        return containerIdList;
    }

    public void setContainerIdList(List<String> containerIdList) {
        this.containerIdList = containerIdList;
    }

    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
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

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
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

}
