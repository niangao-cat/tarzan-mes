package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtRouterNextStep;

/**
 * Created by slj on 2019-02-13.
 */
public class MtRouterNextStepVO2 extends MtRouterNextStep implements Serializable {
    private static final long serialVersionUID = -1650007321490761813L;

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    private String routerStepType;

}
