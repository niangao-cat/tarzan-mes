package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtWorkOrderVO48 implements Serializable {

    private static final long serialVersionUID = 3340059808682628878L;
    
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "生产指令类型")
    private String workOrderType;
    @ApiModelProperty(value = "生产指令指令")
    private String workOrderNum;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "来源制造订单ID")
    private String makeOrderId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    @ApiModelProperty(value = "计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "客户ID")
    private String customerId;
    @ApiModelProperty(value = "完工控制类型")
    private String completeControlType;
    @ApiModelProperty(value = "完工控制数量")
    private String completeControlQty;
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getWorkOrderType() {
        return workOrderType;
    }
    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getProductionLineId() {
        return productionLineId;
    }
    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }
    public String getMakeOrderId() {
        return makeOrderId;
    }
    public void setMakeOrderId(String makeOrderId) {
        this.makeOrderId = makeOrderId;
    }
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    public Double getQty() {
        return qty;
    }
    public void setQty(Double qty) {
        this.qty = qty;
    }
    public String getUomId() {
        return uomId;
    }
    public void setUomId(String uomId) {
        this.uomId = uomId;
    }
    public Date getPlanStartTime() {
        return planStartTime == null ? null : (Date)planStartTime.clone();
    }
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime == null ? null : (Date)planStartTime.clone();
    }
    public Date getPlanEndTime() {
        return  planEndTime == null ? null : (Date)planEndTime.clone();
    }
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime == null ? null : (Date)planEndTime.clone();
    }
    public String getLocatorId() {
        return locatorId;
    }
    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getRouterId() {
        return routerId;
    }
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getCompleteControlType() {
        return completeControlType;
    }
    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }
    public String getCompleteControlQty() {
        return completeControlQty;
    }
    public void setCompleteControlQty(String completeControlQty) {
        this.completeControlQty = completeControlQty;
    }
    public String getWorkOrderNum() {
        return workOrderNum;
    }
    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    
}
