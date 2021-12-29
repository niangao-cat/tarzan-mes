package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO8 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1344898732243696969L;
    private Double qty;
    private Double completedQty;
    private String status;

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
