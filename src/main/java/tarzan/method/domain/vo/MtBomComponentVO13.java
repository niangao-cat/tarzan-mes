package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtBomComponent;

/**
 * @author Leeloing
 * @date 2019/4/30 15:34
 */
public class MtBomComponentVO13 extends MtBomComponent implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5710386563825494976L;
    private Double perQty;

    public Double getPerQty() {
        return perQty;
    }

    public void setPerQty(Double perQty) {
        this.perQty = perQty;
    }
}
