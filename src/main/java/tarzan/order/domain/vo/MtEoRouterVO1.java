package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/20 18:40
 */
public class MtEoRouterVO1 implements Serializable {

    private static final long serialVersionUID = -4665034847671543253L;
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
