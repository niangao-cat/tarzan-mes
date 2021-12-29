package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtBomComponent;

public class MtBomComponentVO8 extends MtBomComponent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -940685592573337615L;
    private Double preQty;

    public Double getPreQty() {
        return preQty;
    }

    public void setPreQty(Double preQty) {
        this.preQty = preQty;
    }

}
