package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointControlVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -273836729242196435L;
    private String assembleControlId;
    private String materialId;
    private String materialLotId;
    private String referencePoint;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
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

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

}
