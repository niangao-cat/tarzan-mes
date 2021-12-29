package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/8/25 18:57
 */
public class MtWorkOrderVO59 implements Serializable {
    private static final long serialVersionUID = -8655873972755833304L;
    @ApiModelProperty("生产指令ID")
    private String workOrderId;

    @ApiModelProperty("生产指令编码")
    private List<String> workOrderNums;

    @ApiModelProperty("生产指令类型")
    private String workOrderType;

    @ApiModelProperty("生产指令状态")
    private String status;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("生产线ID")
    private String productionLineId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("装配清单ID")
    private String bomId;

    @ApiModelProperty("工艺路线ID")
    private String routerId;

    @ApiModelProperty("机会订单ID")
    private String opportunityId;

    @ApiModelProperty("WO验证通过标记")
    private String validateFlag;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("默认完工库位ID")
    private String locatorId;

    @ApiModelProperty("计划开始时间从")
    private Date planStartTimeFrom;

    @ApiModelProperty("计划开始时间至")
    private Date planStartTimeTo;

    @ApiModelProperty("计划结束时间从")
    private Date planEndTimeFrom;

    @ApiModelProperty("计划结束时间至")
    private Date planEndTimeTo;



    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom == null) {
            return null;
        } else {
            return (Date) planStartTimeFrom.clone();
        }
    }

    public void setPlanStartTimeFrom(Date planStartTimeFrom) {
        if (planStartTimeFrom == null) {
            this.planStartTimeFrom = null;
        } else {
            this.planStartTimeFrom = (Date) planStartTimeFrom.clone();
        }
    }

    public Date getPlanStartTimeTo() {
        if (planStartTimeTo == null) {
            return null;
        } else {
            return (Date) planStartTimeTo.clone();
        }
    }

    public void setPlanStartTimeTo(Date planStartTimeTo) {
        if (planStartTimeTo == null) {
            this.planStartTimeTo = null;
        } else {
            this.planStartTimeTo = (Date) planStartTimeTo.clone();
        }
    }

    public Date getPlanEndTimeFrom() {
        if (planEndTimeFrom == null) {
            return null;
        } else {
            return (Date) planEndTimeFrom.clone();
        }
    }

    public void setPlanEndTimeFrom(Date planEndTimeFrom) {
        if (planEndTimeFrom == null) {
            this.planEndTimeFrom = null;
        } else {
            this.planEndTimeFrom = (Date) planEndTimeFrom.clone();
        }
    }

    public Date getPlanEndTimeTo() {
        if (planEndTimeTo == null) {
            return null;
        } else {
            return (Date) planEndTimeTo.clone();
        }
    }

    public void setPlanEndTimeTo(Date planEndTimeTo) {
        if (planEndTimeTo == null) {
            this.planEndTimeTo = null;
        } else {
            this.planEndTimeTo = (Date) planEndTimeTo.clone();
        }
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<String> getWorkOrderNums() {
        return workOrderNums;
    }

    public void setWorkOrderNums(List<String> workOrderNums) {
        this.workOrderNums = workOrderNums;
    }

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getValidateFlag() {
        return validateFlag;
    }

    public void setValidateFlag(String validateFlag) {
        this.validateFlag = validateFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
}
