package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtRouterDoneStepVO implements Serializable {

    private static final long serialVersionUID = -6522472514577409048L;
    private String routerStepId;

    private String doneStepFlag;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getDoneStepFlag() {
        return doneStepFlag;
    }

    public void setDoneStepFlag(String doneStepFlag) {
        this.doneStepFlag = doneStepFlag;
    }

    public MtRouterDoneStepVO(String routerStepId, String doneStepFlag) {
        this.routerStepId = routerStepId;
        this.doneStepFlag = doneStepFlag;
    }
}
