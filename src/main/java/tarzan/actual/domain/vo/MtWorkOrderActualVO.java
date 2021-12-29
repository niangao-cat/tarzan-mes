package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtWorkOrderActualVO implements Serializable {

    private static final long serialVersionUID = -6975297782439938625L;
    private String workOrderId;
    private String workOrderActualId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

}
