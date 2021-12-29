package tarzan.method.api.dto;

import java.io.Serializable;

public class MtRouterStepDTO3 implements Serializable {
    private static final long serialVersionUID = 8711868469498911221L;

    private String routerId;
    private String stepName;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
}
