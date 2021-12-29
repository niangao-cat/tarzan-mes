package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtPfepScheduleVO3
 * @description
 * @date 2020年03月16日 14:39
 */
public class MtPfepScheduleVO3 implements Serializable {
    private static final long serialVersionUID = 7412397059913713176L;
    @ApiModelProperty(value = "关键组件标识")
    private String keyComponentFlag;
    @ApiModelProperty(value = "参与排产标识")
    private String scheduleFlag;
    @ApiModelProperty(value = "按单生产标识")
    private String makeToOrderFlag;
    @ApiModelProperty(value = "选线规则")
    private String prodLineRule;
    @ApiModelProperty(value = "需求时间栏")
    private Double demandTimeFence;
    @ApiModelProperty(value = "下达时间栏")
    private Double releaseTimeFence;
    @ApiModelProperty(value = "订单时间栏")
    private Double orderTimeFence;
    @ApiModelProperty(value = "需求合并时间栏")
    private Double demandMergeTimeFence;
    @ApiModelProperty(value = "供应合并时间栏")
    private Double supplyMergeTimeFence;
    @ApiModelProperty(value = "安全库存方法")
    private String safetyStockMethod;
    @ApiModelProperty(value = "安全库存周期")
    private Double safetyStockPeriod;
    @ApiModelProperty(value = "安全库存值")
    private Double safetyStockValue;
    @ApiModelProperty(value = "经济批量")
    private Double economicLotSize;
    @ApiModelProperty(value = "批量舍入比例")
    private Double economicSplitParameter;
    @ApiModelProperty(value = "最小订单数量")
    private Double minOrderQty;
    @ApiModelProperty(value = "圆整批量")
    private Double fixedLotMultiple;
    @ApiModelProperty(value = "处理批量")
    private Double processBatch;
    @ApiModelProperty(value = "计划自动分配")
    private String autoAssignFlag;
    @ApiModelProperty(value = "默认装配清单ID")
    private String defaultBomId;
    @ApiModelProperty(value = "默认工艺路线ID")
    private String defaultRouterId;

    public String getKeyComponentFlag() {
        return keyComponentFlag;
    }

    public void setKeyComponentFlag(String keyComponentFlag) {
        this.keyComponentFlag = keyComponentFlag;
    }

    public String getScheduleFlag() {
        return scheduleFlag;
    }

    public void setScheduleFlag(String scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    public String getMakeToOrderFlag() {
        return makeToOrderFlag;
    }

    public void setMakeToOrderFlag(String makeToOrderFlag) {
        this.makeToOrderFlag = makeToOrderFlag;
    }

    public String getProdLineRule() {
        return prodLineRule;
    }

    public void setProdLineRule(String prodLineRule) {
        this.prodLineRule = prodLineRule;
    }

    public Double getDemandTimeFence() {
        return demandTimeFence;
    }

    public void setDemandTimeFence(Double demandTimeFence) {
        this.demandTimeFence = demandTimeFence;
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

    public Double getDemandMergeTimeFence() {
        return demandMergeTimeFence;
    }

    public void setDemandMergeTimeFence(Double demandMergeTimeFence) {
        this.demandMergeTimeFence = demandMergeTimeFence;
    }

    public Double getSupplyMergeTimeFence() {
        return supplyMergeTimeFence;
    }

    public void setSupplyMergeTimeFence(Double supplyMergeTimeFence) {
        this.supplyMergeTimeFence = supplyMergeTimeFence;
    }

    public String getSafetyStockMethod() {
        return safetyStockMethod;
    }

    public void setSafetyStockMethod(String safetyStockMethod) {
        this.safetyStockMethod = safetyStockMethod;
    }

    public Double getSafetyStockPeriod() {
        return safetyStockPeriod;
    }

    public void setSafetyStockPeriod(Double safetyStockPeriod) {
        this.safetyStockPeriod = safetyStockPeriod;
    }

    public Double getSafetyStockValue() {
        return safetyStockValue;
    }

    public void setSafetyStockValue(Double safetyStockValue) {
        this.safetyStockValue = safetyStockValue;
    }

    public Double getEconomicLotSize() {
        return economicLotSize;
    }

    public void setEconomicLotSize(Double economicLotSize) {
        this.economicLotSize = economicLotSize;
    }

    public Double getEconomicSplitParameter() {
        return economicSplitParameter;
    }

    public void setEconomicSplitParameter(Double economicSplitParameter) {
        this.economicSplitParameter = economicSplitParameter;
    }

    public Double getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Double minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public Double getFixedLotMultiple() {
        return fixedLotMultiple;
    }

    public void setFixedLotMultiple(Double fixedLotMultiple) {
        this.fixedLotMultiple = fixedLotMultiple;
    }

    public Double getProcessBatch() {
        return processBatch;
    }

    public void setProcessBatch(Double processBatch) {
        this.processBatch = processBatch;
    }

    public String getAutoAssignFlag() {
        return autoAssignFlag;
    }

    public void setAutoAssignFlag(String autoAssignFlag) {
        this.autoAssignFlag = autoAssignFlag;
    }

    public String getDefaultBomId() {
        return defaultBomId;
    }

    public void setDefaultBomId(String defaultBomId) {
        this.defaultBomId = defaultBomId;
    }

    public String getDefaultRouterId() {
        return defaultRouterId;
    }

    public void setDefaultRouterId(String defaultRouterId) {
        this.defaultRouterId = defaultRouterId;
    }
}


