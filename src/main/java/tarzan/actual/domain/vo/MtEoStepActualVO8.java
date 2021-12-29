package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO8 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6719380367990054334L;
    private String status;
    private Long holdCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(Long holdCount) {
        this.holdCount = holdCount;
    }


}
