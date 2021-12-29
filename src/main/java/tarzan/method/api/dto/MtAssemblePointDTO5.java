package tarzan.method.api.dto;

import java.io.Serializable;

public class MtAssemblePointDTO5 implements Serializable {
    private static final long serialVersionUID = -6031648257732723091L;

    private String assemblePointId;
    private String assembleGroupId;
    private String assemblePointCode;
    private String description;
    private String uniqueMaterialLotFlag;
    private Long sequence;
    private Double maxQty;
    private String enableFlag;

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueMaterialLotFlag() {
        return uniqueMaterialLotFlag;
    }

    public void setUniqueMaterialLotFlag(String uniqueMaterialLotFlag) {
        this.uniqueMaterialLotFlag = uniqueMaterialLotFlag;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
