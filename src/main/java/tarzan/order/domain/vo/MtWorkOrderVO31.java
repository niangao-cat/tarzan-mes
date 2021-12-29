package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/29 16:13
 * @Author: ${yiyang.xie}
 */
public class MtWorkOrderVO31 implements Serializable {
    private static final long serialVersionUID = 7976055055864334936L;
    /**
     * 生产指令编码
     */
    private String workOrderNum;
    /**
     * 生产指令类型
     */
    private String workOrderType;
    /**
     * 生产指令ID
     */
    private String workOrderId;
    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 所在站点编码
     */
    private String siteCode;
    /**
     * 站点描述
     */
    private String siteName;
    /**
     * 生产线ID
     */
    private String productionLineId;
    /**
     * 生产线描述
     */
    private String productionLineName;
    /**
     * 生产线编码
     */
    private String productionLineCode;
    /**
     * 工作单元ID
     */
    private String workcellId;
    /**
     * 制造订单ID
     */
    private String makeOrderId;
    /**
     * 生产版本
     */
    private String productionVersion;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 物料描述
     */
    private String materialName;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 数量
     */
    private Double qty;
    /**
     * 单位
     */
    private String uomId;
    /**
     * 单位描述
     */
    private String uomName;
    /**
     * 单位编码
     */
    private String uomCode;
    /**
     * 优先级
     */
    private Long priority;
    /**
     * 生产指令状态
     */
    private String status;
    /**
     * 前次指令状态
     */
    private String lastWoStatus;
    /**
     * 计划开始时间
     */
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    private Date planEndTime;
    /**
     * 默认完工库位ID
     */
    private String locatorId;
    /**
     * 默认完工库位编码
     */
    private String locatorCode;
    /**
     * 默认完工库位描述
     */
    private String locatorName;
    /**
     * 装配清单ID
     */
    private String bomId;
    /**
     * 工艺路线ID
     */
    private String routerId;
    /**
     * WO验证通过标记
     */
    private String validateFlag;
    /**
     * 备注
     */
    private String remark;
    /**
     * 机会订单ID
     */
    private String opportunityId;
    /**
     * 生产指令对应客户ID
     */
    private String customerId;
    /**
     * 完工限制类型
     */
    private String completeControlType;
    /**
     * 完工限制值
     */
    private String completeControlQty;

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

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
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

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
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
}
