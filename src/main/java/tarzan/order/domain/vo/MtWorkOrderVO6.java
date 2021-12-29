package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtWorkOrderVO6 implements Serializable {
    private static final long serialVersionUID = 8231857155022670920L;

    private String workOrderId;
    private Double updateQty;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getUpdateQty() {
        return updateQty;
    }

    public void setUpdateQty(Double updateQty) {
        this.updateQty = updateQty;
    }
}
