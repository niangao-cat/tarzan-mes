package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoStepActualVO23 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7290928245432841554L;

    private String routerStepId;
    private List<MtEoStepActualVO42> eoStepActualList;
    private String eventRequestId;
    private String workcellId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public List<MtEoStepActualVO42> getEoStepActualList() {
        return eoStepActualList;
    }

    public void setEoStepActualList(List<MtEoStepActualVO42> eoStepActualList) {
        this.eoStepActualList = eoStepActualList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

}


