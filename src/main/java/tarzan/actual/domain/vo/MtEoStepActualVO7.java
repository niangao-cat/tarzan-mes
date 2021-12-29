package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO7 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6450304121315584878L;
    private String status;
    private String previousStepId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }


}
