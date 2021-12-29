package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtWorkOrderVO3 implements Serializable {

    private static final long serialVersionUID = -4028669520010704354L;
    /**
     * 
     */

    private String workOrderId;
    private Double trxReleasedQty;
    private Double trxCompletedQty;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getTrxReleasedQty() {
        return trxReleasedQty;
    }

    public void setTrxReleasedQty(Double trxReleasedQty) {
        this.trxReleasedQty = trxReleasedQty;
    }

    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
    }

}
