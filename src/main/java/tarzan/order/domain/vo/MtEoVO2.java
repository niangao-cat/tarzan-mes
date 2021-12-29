package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slj on 2019-01-08.
 */
public class MtEoVO2 implements Serializable {

    private static final long serialVersionUID = -6921549057489511892L;

    private List<String> eoType;
    private List<String> status;
    private String workOrderId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<String> getEoType() {
        return eoType;
    }

    public void setEoType(List<String> eoType) {
        this.eoType = eoType;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}
