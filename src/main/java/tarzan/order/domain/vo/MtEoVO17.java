package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoVO17 implements Serializable {
    private static final long serialVersionUID = 6846906285378808906L;

    private String bomId;
    private String routerId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
