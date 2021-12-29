package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoVO11 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1197321783594483700L;
    private String eoId;
    private Double trxCompletedQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
    }

}
