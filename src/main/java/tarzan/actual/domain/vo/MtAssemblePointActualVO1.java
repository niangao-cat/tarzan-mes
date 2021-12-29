package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -109425814331598972L;
    private String assmeblePointActualId;
    private String assembleGroupActualId;
    private String assemblePointId;
    private Long feedingSequence;
    private String materialId;
    private Double qty;
    private Double feedingQty;
    private Long feedingMaterialLotSequence;
    private String materialLotId;
    private String workcellId;
    private String assembleGroupId;

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
