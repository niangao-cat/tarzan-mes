package tarzan.method.domain.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MtRouterVO4 implements Serializable {
    private static final long serialVersionUID = 3241685678103293125L;
    @JsonIgnore
    private String eoId;
    @JsonIgnore
    private String routerStepId;
    private String eoStepActualId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
