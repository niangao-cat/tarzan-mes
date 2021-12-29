package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO12 implements Serializable {
    private static final long serialVersionUID = 4559657916285591887L;

    private String eoId;
    private String routerId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
