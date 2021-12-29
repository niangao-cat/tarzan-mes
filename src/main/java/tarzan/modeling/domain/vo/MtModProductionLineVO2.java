package tarzan.modeling.domain.vo;

import java.io.Serializable;

import tarzan.modeling.domain.entity.MtModProductionLine;

/**
 * Created by slj on 2018-12-02.
 */
public class MtModProductionLineVO2 extends MtModProductionLine implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6087580953833549942L;
    private String prodLineManufacturingId; // 主键
    private String issuedLocatorId; // 默认发料库位
    private String completionLocatorId; // 默认完工库位
    private String inventoryLocatorId; // 默认入库库位
    private String prodLineScheduleId; // 主键
    private String rateType; // 速率类型
    private Double rate; // 默认速率
    private Double activity; // 开动率
    private Double demandTimeFence; // 需求时间栏(天)
    private Double fixTimeFence; // 固定时间栏(天)
    private Double frozenTimeFence; // 冻结时间栏(天)
    private Double forwardPlanningTimeFence; // 顺排时间栏(天)
    private Double releaseTimeFence; // 下达时间栏(天)
    private Double orderTimeFence; // 订单时间栏(天)
    private String issuedLocatorCode;
    private String completionLocatorCode;
    private String inventoryLocatorCode;

    private String issuedLocatorName;
    private String completionLocatorName;
    private String inventoryLocatorName;
    private String supplierName;
    private String supplierSiteCode;
    private String supplierSiteName;
    private String supplierCode;

    private String dispatchMethod;

    public String getProdLineManufacturingId() {
        return prodLineManufacturingId;
    }

    public void setProdLineManufacturingId(String prodLineManufacturingId) {
        this.prodLineManufacturingId = prodLineManufacturingId;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }

    public String getInventoryLocatorId() {
        return inventoryLocatorId;
    }

    public void setInventoryLocatorId(String inventoryLocatorId) {
        this.inventoryLocatorId = inventoryLocatorId;
    }

    public String getProdLineScheduleId() {
        return prodLineScheduleId;
    }

    public void setProdLineScheduleId(String prodLineScheduleId) {
        this.prodLineScheduleId = prodLineScheduleId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
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

    public String getIssuedLocatorCode() {
        return issuedLocatorCode;
    }

    public void setIssuedLocatorCode(String issuedLocatorCode) {
        this.issuedLocatorCode = issuedLocatorCode;
    }

    public String getCompletionLocatorCode() {
        return completionLocatorCode;
    }

    public void setCompletionLocatorCode(String completionLocatorCode) {
        this.completionLocatorCode = completionLocatorCode;
    }

    public String getInventoryLocatorCode() {
        return inventoryLocatorCode;
    }

    public void setInventoryLocatorCode(String inventoryLocatorCode) {
        this.inventoryLocatorCode = inventoryLocatorCode;
    }


    public String getIssuedLocatorName() {
        return issuedLocatorName;
    }

    public void setIssuedLocatorName(String issuedLocatorName) {
        this.issuedLocatorName = issuedLocatorName;
    }

    public String getCompletionLocatorName() {
        return completionLocatorName;
    }

    public void setCompletionLocatorName(String completionLocatorName) {
        this.completionLocatorName = completionLocatorName;
    }

    public String getInventoryLocatorName() {
        return inventoryLocatorName;
    }

    public void setInventoryLocatorName(String inventoryLocatorName) {
        this.inventoryLocatorName = inventoryLocatorName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    public String getSupplierSiteName() {
        return supplierSiteName;
    }

    public void setSupplierSiteName(String supplierSiteName) {
        this.supplierSiteName = supplierSiteName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getDispatchMethod() {
        return dispatchMethod;
    }

    public void setDispatchMethod(String dispatchMethod) {
        this.dispatchMethod = dispatchMethod;
    }
}
