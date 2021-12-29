package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/20.
 */
public class MtWoComponentActualVO25 implements Serializable {

    private static final long serialVersionUID = -1765924571434319650L;

    private String workOrderId;

    private String bomComponentId;

    private Double qty;

    private String materialId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
