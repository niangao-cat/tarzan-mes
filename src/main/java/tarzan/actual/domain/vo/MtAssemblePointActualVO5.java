package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualVO5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7126391869671869603L;
    private String assemblePointId;
    private String materialId;
    private String materialLotId;
    private Double qty;

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

}
