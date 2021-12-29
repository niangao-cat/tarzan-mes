package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author peng.yuan
 * @ClassName MtWorkOrderVO36
 * @description
 * @date 2019年11月29日 14:20
 */
public class MtWorkOrderVO36 implements Serializable {
    private static final long serialVersionUID = 4136831949080273491L;

    private String workOrderId;
    private String workOrderNum;
    private String workOrderType;
    private String siteId;
    private String productionLineId;
    private String workcellId;
    private String makeOrderId;
    private String productionVersion;
    private String materialId;
    private Double qty;
    private String uomId;
    private Long priority;
    private String status;
    private String lastWoStatus;
    private Date planStartTime;
    private Date planEndTime;
    private String locatorId;
    private String bomId;
    private String routerId;
    private String validateFlag;
    private String remark;
    private String opportunityId;
    private String customerId;
    private String completeControlType;
    private String completeControlQty;
    private Double sourceIdentificationId;
    private String outsideNum;
    private List<String> incomingValueList;

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

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getMakeOrderId() {
        return makeOrderId;
    }

    public void setMakeOrderId(String makeOrderId) {
        this.makeOrderId = makeOrderId;
    }

    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
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

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastWoStatus() {
        return lastWoStatus;
    }

    public void setLastWoStatus(String lastWoStatus) {
        this.lastWoStatus = lastWoStatus;
    }

    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
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

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
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

    public Double getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(Double sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
