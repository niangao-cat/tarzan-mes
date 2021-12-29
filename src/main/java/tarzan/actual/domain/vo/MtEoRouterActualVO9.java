package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtEoRouterActualVO9 implements Serializable {
    private static final long serialVersionUID = 3722845136410787253L;

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

    public MtEoRouterActualVO9(String eoRouterActualId, String routerStepId) {
        this.eoRouterActualId = eoRouterActualId;
        this.routerStepId = routerStepId;
    }

    public MtEoRouterActualVO9() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoRouterActualVO9 that = (MtEoRouterActualVO9) o;
        return Objects.equals(getEoRouterActualId(), that.getEoRouterActualId())
                && Objects.equals(getRouterStepId(), that.getRouterStepId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEoRouterActualId(), getRouterStepId());
    }
}
