package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 区域维护-新增 使用DTO
 * 
 * @author benjamin
 */
public class MtModAreaScheduleDTO implements Serializable {
    private static final long serialVersionUID = -723961661265923726L;

    @ApiModelProperty("主键ID，标示唯一一条记录")
    private String areaScheduleId;

    @ApiModelProperty(value = "计划滚动起始时间")
    private Date planStartTime;

    @ApiModelProperty(value = "需求时间栏（天）")
    private Double demandTimeFence;

    @ApiModelProperty(value = "固定时间栏（天）")
    private Double fixTimeFence;

    @ApiModelProperty(value = "冻结时间栏（天）")
    private Double frozenTimeFence;

    @ApiModelProperty(value = "顺排时间栏（天）")
    private Double forwardPlanningTimeFence;

    @ApiModelProperty(value = "下达时间栏（天）")
    private Double releaseTimeFence;

    @ApiModelProperty(value = "订单时间栏（天）")
    private Double orderTimeFence;

    @ApiModelProperty(value = "基础排程算法")
    private String basicAlgorithm;

    @ApiModelProperty(value = "跟随区域")
    private String followAreaId;

    @ApiModelProperty(value = "跟随区域编码")
    private String followAreaCode;

    @ApiModelProperty(value = "跟随区域名称")
    private String followAreaName;

    @ApiModelProperty(value = "选线规则")
    private String prodLineRule;

    @ApiModelProperty(value = "区间类型")
    private String phaseType;

    @ApiModelProperty(value = "排程类型")
    private String planningBase;

    @ApiModelProperty(value = "实绩延迟时间")
    private Double delayTimeFence;

    @ApiModelProperty(value = "关联下达策略")
    private String releaseConcurrentRule;

    public String getAreaScheduleId() {
        return areaScheduleId;
    }

    public void setAreaScheduleId(String areaScheduleId) {
        this.areaScheduleId = areaScheduleId;
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

    public String getFollowAreaCode() {
        return followAreaCode;
    }

    public void setFollowAreaCode(String followAreaCode) {
        this.followAreaCode = followAreaCode;
    }

    public String getFollowAreaName() {
        return followAreaName;
    }

    public void setFollowAreaName(String followAreaName) {
        this.followAreaName = followAreaName;
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

    @Override
    public String toString() {
        return "MtModAreaScheduleDTO{" +
                "areaScheduleId='" + areaScheduleId + '\'' +
                ", planStartTime=" + planStartTime +
                ", demandTimeFence=" + demandTimeFence +
                ", fixTimeFence=" + fixTimeFence +
                ", frozenTimeFence=" + frozenTimeFence +
                ", forwardPlanningTimeFence=" + forwardPlanningTimeFence +
                ", releaseTimeFence=" + releaseTimeFence +
                ", orderTimeFence=" + orderTimeFence +
                ", basicAlgorithm='" + basicAlgorithm + '\'' +
                ", followAreaId='" + followAreaId + '\'' +
                ", followAreaCode='" + followAreaCode + '\'' +
                ", followAreaName='" + followAreaName + '\'' +
                ", prodLineRule='" + prodLineRule + '\'' +
                ", phaseType='" + phaseType + '\'' +
                ", planningBase='" + planningBase + '\'' +
                ", delayTimeFence=" + delayTimeFence +
                ", releaseConcurrentRule='" + releaseConcurrentRule + '\'' +
                '}';
    }
}
