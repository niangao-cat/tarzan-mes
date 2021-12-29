package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointVO2 implements Serializable {

    private static final long serialVersionUID = 2049299478176369331L;

    private String assemblePointId;

    private String materialId;

    private String materialLotId;

    private Double trxQty;

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

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }
}
