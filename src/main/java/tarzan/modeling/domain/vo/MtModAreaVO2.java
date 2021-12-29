package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Date;

import tarzan.modeling.domain.entity.MtModArea;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年8月1日 上午11:32:24
 *
 */
public class MtModAreaVO2 extends MtModArea implements Serializable {

    private static final long serialVersionUID = -2988349083687261924L;
    private String areaPurchaseId;
    private String insideFlag; // 是否厂内区域，主要在采购站点下使用，如区分厂内厂外送货区域
    private String supplierId; // 厂外区域供应商
    private String supplierSiteId; // 厂外区域供应商地点
    private String areaScheduleId;
    private Date planStartTime; // 计划滚动起始时间
    private Double demandTimeFence; // 需求时间栏(天)
    private Double fixTimeFence; // 固定时间栏(天)
    private Double frozenTimeFence; // 冻结时间栏(天)
    private Double forwardPlanningTimeFence; // 顺排时间栏(天)
    private Double releaseTimeFence; // 下达时间栏(天)
    private Double orderTimeFence; // 订单时间栏(天)
    private String basicAlgorithm; // 基础排程算法
    private String followAreaId; // 跟随区域
    private String prodLineRule; // 选线规则
    private String phaseType; // 区间类型
    private String planningBase; // 排程类型
    private Double delayTimeFence; // 实际延迟时间
    private String releaseConcurrentRule; // 关联下达策略

    private String supplierCode;
    private String supplierName;
    private String supplierSiteCode;
    private String supplierSiteName;
    private String followAreaCode;
    private String followAreaName;


    public String getAreaPurchaseId() {
        return areaPurchaseId;
    }

    public void setAreaPurchaseId(String areaPurchaseId) {
        this.areaPurchaseId = areaPurchaseId;
    }

    public String getInsideFlag() {
        return insideFlag;
    }

    public void setInsideFlag(String insideFlag) {
        this.insideFlag = insideFlag;
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

    public String getAreaScheduleId() {
        return areaScheduleId;
    }

    public void setAreaScheduleId(String areaScheduleId) {
        this.areaScheduleId = areaScheduleId;
    }

    public Date getPlanStartTime() {
        if (this.planStartTime == null) {
            return null;
        }
        return (Date) this.planStartTime.clone();
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Double getDemandTimeFence() {
        return demandTimeFence;
    }

    public void setDemandTimeFence(Double demandTimeFence) {
        this.demandTimeFence = demandTimeFence;
    }

    public Double getFixTimeFence() {
        return fixTimeFence;
    }

    public void setFixTimeFence(Double fixTimeFence) {
        this.fixTimeFence = fixTimeFence;
    }

    public Double getFrozenTimeFence() {
        return frozenTimeFence;
    }

    public void setFrozenTimeFence(Double frozenTimeFence) {
        this.frozenTimeFence = frozenTimeFence;
    }

    public Double getForwardPlanningTimeFence() {
        return forwardPlanningTimeFence;
    }

    public void setForwardPlanningTimeFence(Double forwardPlanningTimeFence) {
        this.forwardPlanningTimeFence = forwardPlanningTimeFence;
    }

    public Double getReleaseTimeFence() {
        return releaseTimeFence;
    }

    public void setReleaseTimeFence(Double releaseTimeFence) {
        this.releaseTimeFence = releaseTimeFence;
    }

    public Double getOrderTimeFence() {
        return orderTimeFence;
    }

    public void setOrderTimeFence(Double orderTimeFence) {
        this.orderTimeFence = orderTimeFence;
    }

    public String getBasicAlgorithm() {
        return basicAlgorithm;
    }

    public void setBasicAlgorithm(String basicAlgorithm) {
        this.basicAlgorithm = basicAlgorithm;
    }

    public String getFollowAreaId() {
        return followAreaId;
    }

    public void setFollowAreaId(String followAreaId) {
        this.followAreaId = followAreaId;
    }

    public String getProdLineRule() {
        return prodLineRule;
    }

    public void setProdLineRule(String prodLineRule) {
        this.prodLineRule = prodLineRule;
    }

    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    public String getPlanningBase() {
        return planningBase;
    }

    public void setPlanningBase(String planningBase) {
        this.planningBase = planningBase;
    }

    public Double getDelayTimeFence() {
        return delayTimeFence;
    }

    public void setDelayTimeFence(Double delayTimeFence) {
        this.delayTimeFence = delayTimeFence;
    }

    public String getReleaseConcurrentRule() {
        return releaseConcurrentRule;
    }

    public void setReleaseConcurrentRule(String releaseConcurrentRule) {
        this.releaseConcurrentRule = releaseConcurrentRule;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    public String getFollowAreaCode() {
        return followAreaCode;
    }

    public void setFollowAreaCode(String followAreaCode) {
        this.followAreaCode = followAreaCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getFollowAreaName() {
        return followAreaName;
    }

    public void setFollowAreaName(String followAreaName) {
        this.followAreaName = followAreaName;
    }

    public String getSupplierSiteName() {
        return supplierSiteName;
    }

    public void setSupplierSiteName(String supplierSiteName) {
        this.supplierSiteName = supplierSiteName;
    }

}
