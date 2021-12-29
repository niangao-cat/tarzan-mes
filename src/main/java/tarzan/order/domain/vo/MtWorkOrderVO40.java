package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO40 implements Serializable {

    private static final long serialVersionUID = 3596895167543183569L;

    // 基本属性
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "生产指令编码")
    private String workOrderNum;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "生产指令类型")
    private String workOrderType;
    @ApiModelProperty(value = "生产指令类型描述")
    private String workOrderTypeDesc;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "备注")
    private String remark;

    // 需求属性
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位描述")
    private String uomName;
    @ApiModelProperty(value = "生产指令对应客户")
    private String customerId;
    @ApiModelProperty(value = "生产指令对应客户编码")
    private String customerCode;
    @ApiModelProperty(value = "生产指令对应客户名称")
    private String customerName;

    // 生产属性
    @ApiModelProperty(value = "计划结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "计划开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "生产线编码")
    private String productionLineCode;
    @ApiModelProperty(value = "生产线名称")
    private String productionLineName;
    @ApiModelProperty(value = "默认完工库位ID，表示唯一货位")
    private String locatorId;
    @ApiModelProperty(value = "默认完工库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "默认完工库位名称")
    private String locatorName;
    @ApiModelProperty(value = "完工限制类型")
    private String completeControlType;
    @ApiModelProperty(value = "完工限制类型描述")
    private String completeControlTypeDesc;
    @ApiModelProperty(value = "完工限制值")
    private Double completeControlQty;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单编码")
    private String bomName;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;

    // 实绩属性
    @ApiModelProperty(value = "实绩开始时间")
    private Date actualStartTime;
    @ApiModelProperty(value = "实绩结束时间")
    private Date actualEndDate;
    @ApiModelProperty(value = "累计下达数量")
    private Double releasedQty;
    @ApiModelProperty(value = "累计完成数量")
    private Double completedQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "保留数量")
    private Double holdQty;

    @ApiModelProperty(value = "来源制造订单ID")
    private String makeOrderId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getWorkOrderTypeDesc() {
        return workOrderTypeDesc;
    }

    public void setWorkOrderTypeDesc(String workOrderTypeDesc) {
        this.workOrderTypeDesc = workOrderTypeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getPlanEndTime() {
        return planEndTime == null ? null : (Date) planEndTime.clone();
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime == null ? null : (Date) planEndTime.clone();
    }

    public Date getPlanStartTime() {
        return planStartTime == null ? null : (Date) planStartTime.clone();
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime == null ? null : (Date) planStartTime.clone();
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    public String getCompleteControlTypeDesc() {
        return completeControlTypeDesc;
    }

    public void setCompleteControlTypeDesc(String completeControlTypeDesc) {
        this.completeControlTypeDesc = completeControlTypeDesc;
    }

    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public Date getActualStartTime() {
        return actualStartTime == null ? null : (Date) actualStartTime.clone();
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime == null ? null : (Date) actualStartTime.clone();
    }

    public Date getActualEndDate() {
        return actualEndDate == null ? null : (Date) actualEndDate.clone();
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate == null ? null : (Date) actualEndDate.clone();
    }

    public Double getReleasedQty() {
        return releasedQty;
    }

    public void setReleasedQty(Double releasedQty) {
        this.releasedQty = releasedQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    public String getMakeOrderId() {
        return makeOrderId;
    }

    public void setMakeOrderId(String makeOrderId) {
        this.makeOrderId = makeOrderId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
    
}
