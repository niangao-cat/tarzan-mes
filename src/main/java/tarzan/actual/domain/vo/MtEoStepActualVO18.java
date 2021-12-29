package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO18 implements Serializable {
    private static final long serialVersionUID = -8985057935421387216L;

    private String eoStepActualId;
    private String eoRouterActualId;
    private Long sequence;
    private String routerStepId;
    private Long sequence2;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public Long getSequence2() {
        return sequence2;
    }

    public void setSequence2(Long sequence2) {
        this.sequence2 = sequence2;
    }
}
