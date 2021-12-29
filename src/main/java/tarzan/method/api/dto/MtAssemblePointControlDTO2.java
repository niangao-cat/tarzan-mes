package tarzan.method.api.dto;

import java.io.Serializable;

public class MtAssemblePointControlDTO2 implements Serializable {
    private static final long serialVersionUID = -7498146958737355216L;

    private String assemblePointControlId;
    private String assembleControlId;
    private String assemblePointId;
    private String materialId;
    private String materialLotId;
    private Double unitQty;
    private String referencePoint;
    private String enableFlag;

    public String getAssemblePointControlId() {
        return assemblePointControlId;
    }

    public void setAssemblePointControlId(String assemblePointControlId) {
        this.assemblePointControlId = assemblePointControlId;
    }

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Double getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(Double unitQty) {
        this.unitQty = unitQty;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
