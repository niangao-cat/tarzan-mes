package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO39 implements Serializable {

    private static final long serialVersionUID = -7772355819513625390L;
    
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
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "生产指令类型")
    private String workOrderType;
    @ApiModelProperty(value = "生产指令类型描述")
    private String workOrderTypeDesc;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "生产线编码")
    private String productionLineCode;
    @ApiModelProperty(value = "生产线名称")
    private String productionLineName;
    @ApiModelProperty(value = "计划结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "计划开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "数量")
    private String qty;
    @ApiModelProperty(value = "下达数量")
    private Double releasedQty;
    @ApiModelProperty(value = "完成数量")
    private Double completedQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "投料套数")
    private Double kitQty;
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
    public Date getPlanEndTime() {
        return planEndTime == null ? null  : (Date)planEndTime.clone();
    }
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime == null ? null  : (Date)planEndTime.clone();
    }
    public Date getPlanStartTime() {
        return  planStartTime == null ? null  : (Date)planStartTime.clone();
    }
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime == null ? null  : (Date)planStartTime.clone();
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
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

    public Double getKitQty() {
        return kitQty;
    }

    public void setKitQty(Double kitQty) {
        this.kitQty = kitQty;
    }
}
