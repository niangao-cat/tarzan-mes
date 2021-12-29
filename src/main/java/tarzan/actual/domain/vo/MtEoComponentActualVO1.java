package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoComponentActualVO1 implements Serializable {
    private static final long serialVersionUID = -4327516187610666573L;
    private Long lineNumber;
    private String referencePoint;
    private Double qty;
    private String operationId;

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
