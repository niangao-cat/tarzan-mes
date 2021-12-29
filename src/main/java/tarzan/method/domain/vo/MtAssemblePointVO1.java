package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -520474788296274470L;
    private String assemblePointId;
    private Double qty;

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

}
