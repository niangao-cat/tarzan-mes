package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/19.
 */
public class MtWorkOrderVO12 implements Serializable {

    private static final long serialVersionUID = -7445491799217119311L;

    private String workOrderId;

    private String routerId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
