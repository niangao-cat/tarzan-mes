package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Date;

import tarzan.modeling.domain.entity.MtModSite;

/**
 * Created by slj on 2018-11-29.
 */
public class MtModSiteVO1 extends MtModSite implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7567224334557071319L;
    private String siteScheduleId;
    private Date planStartTime;
    private Double demandTimeFence;
    private Double fixTimeFence;
    private Double frozenTimeFence;
    private Double forwardPlanningTimeFence;
    private Double releaseTimeFence;
    private Double orderTimeFence;
    private String typeDesc;
    private String materialId;
    private String materialCategoryId;
    private String attritionCalculateStrategy;// 损耗计算策略

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteScheduleId() {
        return siteScheduleId;
    }

    public void setSiteScheduleId(String siteScheduleId) {
        this.siteScheduleId = siteScheduleId;
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

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getAttritionCalculateStrategy() {
        return attritionCalculateStrategy;
    }

    public void setAttritionCalculateStrategy(String attritionCalculateStrategy) {
        this.attritionCalculateStrategy = attritionCalculateStrategy;
    }
}
