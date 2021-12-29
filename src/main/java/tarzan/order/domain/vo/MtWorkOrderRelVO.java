package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2019-01-09.
 */
public class MtWorkOrderRelVO implements Serializable {
    private static final long serialVersionUID = -9130990526913736130L;



    private String parentWorkOrderId;
    private String workOrderId;


    public String getParentWorkOrderId() {
        return parentWorkOrderId;
    }

    public void setParentWorkOrderId(String parentWorkOrderId) {
        this.parentWorkOrderId = parentWorkOrderId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }


}
