package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtWorkOrderVO19 implements Serializable {

    private static final long serialVersionUID = -1166381364083830665L;
    private String completeControlType;

    private Double completeControlQty;

    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }
}
