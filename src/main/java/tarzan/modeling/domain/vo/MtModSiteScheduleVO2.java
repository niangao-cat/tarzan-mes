package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtModSiteScheduleVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5052352369271476971L;
    private String siteScheduleId;
    private String siteId; // 站点ID
    private Date planStartTime; // 计划滚动起始时间
    private Double demandTimeFence; // 需求时间栏（天）
    private Double fixTimeFence; // 固定时间栏（天）
    private Double frozenTimeFence; // 冻结时间栏（天）
    private Double forwardPlanningTimeFence; // 顺排时间栏（天）
    private Double releaseTimeFence; // 下达时间栏（天）
    private Double orderTimeFence; // 订单时间栏（天）

    public String getSiteScheduleId() {
        return siteScheduleId;
    }

    public void setSiteScheduleId(String siteScheduleId) {
        this.siteScheduleId = siteScheduleId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Date getPlanStartTime() {
        if (planStartTime == null) {
            return null;
        }
        return (Date) planStartTime.clone();
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

}
