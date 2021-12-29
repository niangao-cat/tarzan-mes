package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModProdLineScheduleVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4963868502151580016L;
    private String prodLineScheduleId;
    private String prodLineId; // 生产线ID
    private String rateType; // 速率类型
    private String rateTypeDesc;
    private Double rate; // 默认速率
    private Double activity; // 开动率%
    private Double demandTimeFence; // 需求时间栏（天）
    private Double fixTimeFence; // 固定时间栏（天）
    private Double frozenTimeFence; // 冻结时间栏（天）
    private Double forwardPlanningTimeFence; // 顺排时间栏（天）
    private Double releaseTimeFence; // 下达时间栏（天）
    private Double orderTimeFence; // 订单时间栏（天）

    public String getProdLineScheduleId() {
        return prodLineScheduleId;
    }

    public void setProdLineScheduleId(String prodLineScheduleId) {
        this.prodLineScheduleId = prodLineScheduleId;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRateTypeDesc() {
        return rateTypeDesc;
    }

    public void setRateTypeDesc(String rateTypeDesc) {
        this.rateTypeDesc = rateTypeDesc;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getActivity() {
        return activity;
    }

    public void setActivity(Double activity) {
        this.activity = activity;
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
