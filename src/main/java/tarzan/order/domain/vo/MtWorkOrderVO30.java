package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/29 15:29
 * @Author: ${yiyang.xie}
 */
public class MtWorkOrderVO30 implements Serializable {
    private static final long serialVersionUID = -7068372215055724644L;
    /**
     * 生产指令ID
     */
    private String workOrderId;
    /**
     * 生产指令编码
     */
    private String workOrderNum;
    /**
     * 生产指令类型
     */
    private String workOrderType;
    /**
     * 生产指令状态
     */
    private String status;
    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 生产线ID
     */
    private String productionLineId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 装配清单ID
     */
    private String bomId;
    /**
     * 工艺路线ID
     */
    private String routerId;
    /**
     * 机会订单ID
     */
    private String opportunityId;
    /**
     * WO验证通过标记
     */
    private String validateFlag;
    /**
     * 备注
     */
    private String remark;
    /**
     * 默认完工库位ID
     */
    private String locatorId;
    /**
     * 计划开始时间从
     */
    private Date planStartTimeFrom;
    /**
     * 计划开始时间至
     */
    private Date planStartTimeTo;
    /**
     * 计划结束时间从
     */
    private Date planEndTimeFrom;
    /**
     * 计划结束时间至
     */
    private Date planEndTimeTo;

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

    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom != null) {
            return (Date) planStartTimeFrom.clone();
        } else {
            return null;
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
        if (planStartTimeTo != null) {
            return (Date) planStartTimeTo.clone();
        } else {
            return null;
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
        if (planEndTimeFrom != null) {
            return (Date) planEndTimeFrom.clone();
        } else {
            return null;
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
        if (planEndTimeTo != null) {
            return (Date) planEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeTo(Date planEndTimeTo) {
        if (planEndTimeTo == null) {
            this.planEndTimeTo = null;
        } else {
            this.planEndTimeTo = (Date) planEndTimeTo.clone();
        }
    }
}
