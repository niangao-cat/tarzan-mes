package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomReferencePointVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6255273021348819558L;
    private String bomId;
    private String bomComponentId;
    private Double qty;
    private String referencePoint;
    private String onlyAvailableFlag;
    private String enableFlag;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getOnlyAvailableFlag() {
        return onlyAvailableFlag;
    }

    public void setOnlyAvailableFlag(String onlyAvailableFlag) {
        this.onlyAvailableFlag = onlyAvailableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
