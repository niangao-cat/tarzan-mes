package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO42 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 4017522006250633008L;
    private String eoRouterActualId;
    private Double qty;
    private String previousStepId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

}


