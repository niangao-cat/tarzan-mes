package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomReferencePointVO9 implements Serializable {

    private static final long serialVersionUID = -6189795804659379191L;
    private String bomReferencePointId;
    private String bomComponentId;
    private Long lineNumber;
    private String referencePoint;
    private Double qty;
    private String enableFlag;
    private String copiedFromPointId;

    public String getBomReferencePointId() {
        return bomReferencePointId;
    }

    public void setBomReferencePointId(String bomReferencePointId) {
        this.bomReferencePointId = bomReferencePointId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getCopiedFromPointId() {
        return copiedFromPointId;
    }

    public void setCopiedFromPointId(String copiedFromPointId) {
        this.copiedFromPointId = copiedFromPointId;
    }
}
