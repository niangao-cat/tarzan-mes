package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: changbu  2020/11/4 11:10
 */
public class MtEoStepActualVO64 implements Serializable {

    private static final long serialVersionUID = -6692992137148269400L;

    private String eoRouterActualId;
    private String result;
    private String routerStepId;
    private List<MtEoStepActualVO65> stepActualInfo;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public List<MtEoStepActualVO65> getStepActualInfo() {
        return stepActualInfo;
    }

    public void setStepActualInfo(List<MtEoStepActualVO65> stepActualInfo) {
        this.stepActualInfo = stepActualInfo;
    }
}
