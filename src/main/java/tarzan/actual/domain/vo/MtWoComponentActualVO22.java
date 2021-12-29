package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/14.
 */
public class MtWoComponentActualVO22 implements Serializable {

    private static final long serialVersionUID = 923772150751573917L;

    private String workOrderId;

    private String materialId;

    private String operationId;

    private String bomId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
}
