package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO18 implements Serializable {
    private static final long serialVersionUID = -8503357576594427584L;

    private String eoId; // 执行作业唯一标识
    private Double qty; // 拆分数量

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
