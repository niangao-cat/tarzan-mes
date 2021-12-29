package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4742200669093961009L;
    private String assemblePointActualId; // 主键ID，标识唯一一条记录
    private String assemblePointId; // 装配点
    private Long feedingSequence; // 装配点上料顺序（装载物料顺序？）
    private String materialId; // 装配点装载物料
    private Double qty; // 装配点当前装载物料数量
    private Double feedingQty; // 装配点上料初始数量
    private Long feedingMaterialLotSequence; // 上料批次顺序（一个点装载多个物料批时的顺序）
    private String materialLotId; // 装配点装载物料批次
    private String eventId;
    private Double trxQty;
    private Double trxFeedingQty;

    public String getAssemblePointActualId() {
        return assemblePointActualId;
    }

    public void setAssemblePointActualId(String assemblePointActualId) {
        this.assemblePointActualId = assemblePointActualId;
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

}
