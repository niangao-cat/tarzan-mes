package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoVO12 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3978049918239847909L;
    private String eoId;
    private Double targetQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Double getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(Double targetQty) {
        this.targetQty = targetQty;
    }

}
