package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/20 18:56
 */
public class MtEoVO23 implements Serializable {
    private static final long serialVersionUID = -4342296210393478730L;

    private String sourceEoId;
    private Double splitQty;

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }
}
