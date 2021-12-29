package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualHisVO1 implements Serializable {

    private static final long serialVersionUID = -3444184003807256065L;
    private String assmeblePointActualHisId; // 主键ID，标识唯一一条记录
    private String assmeblePointActualId; // 装配点实绩ID，标识唯一一条装配点实际记录
    private String assembleGroupActualId; // 装配组ID
    private String assemblePointId; // 装配点
    private Long feedingSequence; // 装配点上料顺序（装载物料顺序？）
    private String materialId; // 装配点装载物料
    private Double qty; // 装配点当前装载物料数量
    private Double feedingQty; // 装配点上料初始数量
    private Long feedingMaterialLotSequence; // 上料批次顺序（一个点装载多个物料批时的顺序）
    private String materialLotId; // 装配点装载物料批次
    private String eventId; // 事件ID
    private Double trxQty; // 事务数量-当前数量
    private Double trxFeedingQty; // 事务数量-初始装载数量
    private String eventTypeCode;
    private String workcellId;
    private String assembleGroupId;

    public String getAssmeblePointActualHisId() {
        return assmeblePointActualHisId;
    }

    public void setAssmeblePointActualHisId(String assmeblePointActualHisId) {
        this.assmeblePointActualHisId = assmeblePointActualHisId;
    }

    public String getAssmeblePointActualId() {
        return assmeblePointActualId;
    }

    public void setAssmeblePointActualId(String assmeblePointActualId) {
        this.assmeblePointActualId = assmeblePointActualId;
    }

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public Long getFeedingSequence() {
        return feedingSequence;
    }

    public void setFeedingSequence(Long feedingSequence) {
        this.feedingSequence = feedingSequence;
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

    public Double getFeedingQty() {
        return feedingQty;
    }

    public void setFeedingQty(Double feedingQty) {
        this.feedingQty = feedingQty;
    }

    public Long getFeedingMaterialLotSequence() {
        return feedingMaterialLotSequence;
    }

    public void setFeedingMaterialLotSequence(Long feedingMaterialLotSequence) {
        this.feedingMaterialLotSequence = feedingMaterialLotSequence;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    public Double getTrxFeedingQty() {
        return trxFeedingQty;
    }

    public void setTrxFeedingQty(Double trxFeedingQty) {
        this.trxFeedingQty = trxFeedingQty;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }
}
