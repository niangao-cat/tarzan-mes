package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomComponentVO28 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4724176434540851623L;
    private String bomId;
    private Double qty;
    private List<MtBomComponentVO29> bomComponentList;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public List<MtBomComponentVO29> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtBomComponentVO29> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }

}
