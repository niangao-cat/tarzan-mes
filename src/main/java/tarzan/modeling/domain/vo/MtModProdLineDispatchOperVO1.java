package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModProdLineDispatchOperVO1 implements Serializable {
    private static final long serialVersionUID = 5555869779500760951L;

    private String prodLineId; // 生产线ID
    private String operationId; // 指定的调度工艺ID


    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
