package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO10 implements Serializable {
    private static final long serialVersionUID = 2188264543790820732L;
    private Double unassignQty;
    private Double dispatchableQty;

    public Double getUnassignQty() {
        return unassignQty;
    }

    public void setUnassignQty(Double unassignQty) {
        this.unassignQty = unassignQty;
    }

    public Double getDispatchableQty() {
        return dispatchableQty;
    }

    public void setDispatchableQty(Double dispatchableQty) {
        this.dispatchableQty = dispatchableQty;
    }
}
