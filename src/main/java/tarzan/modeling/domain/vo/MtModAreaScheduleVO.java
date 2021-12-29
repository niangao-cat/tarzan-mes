package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xiao.tang02@hand-china.com
 * @Date: 2019/8/1 20:23
 * @Description:
 */
public class MtModAreaScheduleVO implements Serializable {
    private static final long serialVersionUID = -8431000863868844271L;

    private String areaId; // 区域ID
    private Date planStartTime; // 计划滚动起始时间
    private Double demandTimeFence; // 需求时间栏（天）
    private Double fixTimeFence; // 固定时间栏（天）
    private Double frozenTimeFence; // 冻结时间栏（天）
    private Double forwardPlanningTimeFence; // 顺排时间栏（天）
    private Double releaseTimeFence; // 下达时间栏（天）
    private Double orderTimeFence; // 订单时间栏（天）
    private String basicAlgorithm; // 基础排程算法
    private String followAreaId; // 跟随区域
    private String prodLineRule; // 选线规则
    private String phaseType; // 区间类型
    private String planningBase; // 排程类型
    private Double delayTimeFence; // 实绩延迟时间
    private String releaseConcurrentRule; // 关联下达策略

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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
}
