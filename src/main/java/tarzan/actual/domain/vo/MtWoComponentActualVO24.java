package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/15.
 */
public class MtWoComponentActualVO24 implements Serializable {

    private static final long serialVersionUID = 5362472596833648699L;

    private String workOrderComponentActualId;

    private String workOrderId;

    private String materialId;

    private String operationId;

    private String periodUom;

    public String getWorkOrderComponentActualId() {
        return workOrderComponentActualId;
    }

    public void setWorkOrderComponentActualId(String workOrderComponentActualId) {
        this.workOrderComponentActualId = workOrderComponentActualId;
    }

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

    public String getPeriodUom() {
        return periodUom;
    }

    public void setPeriodUom(String periodUom) {
        this.periodUom = periodUom;
    }
}
