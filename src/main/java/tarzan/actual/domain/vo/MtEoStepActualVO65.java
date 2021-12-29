package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Author: changbu  2020/11/4 11:10
 */
public class MtEoStepActualVO65 implements Serializable {


    private static final long serialVersionUID = -8358913772711692062L;

    private String routerStepId;
    private String eoStepActualId;

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
