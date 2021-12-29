package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtEoStepActualVO10 implements Serializable {
    private static final long serialVersionUID = -7001331152370685862L;

    private String eoRouterActualId;
    private String routerStepId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public MtEoStepActualVO10(String eoRouterActualId, String routerStepId) {
        this.eoRouterActualId = eoRouterActualId;
        this.routerStepId = routerStepId;
    }

    public MtEoStepActualVO10() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepActualVO10 that = (MtEoStepActualVO10) o;
        return Objects.equals(getEoRouterActualId(), that.getEoRouterActualId())
                        && Objects.equals(getRouterStepId(), that.getRouterStepId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEoRouterActualId(), getRouterStepId());
    }
}
