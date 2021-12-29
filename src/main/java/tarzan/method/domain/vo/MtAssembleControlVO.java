package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleControlVO implements Serializable {

    private static final long serialVersionUID = -3370685262324217670L;
    private String assembleControlId;
    private String assembleGroupId;
    private String assemblePointId;
    private String materialId;
    private String materialLotId;
    private Double demandQty;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
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

    public Double getDemandQty() {
        return demandQty;
    }

    public void setDemandQty(Double demandQty) {
        this.demandQty = demandQty;
    }

}
