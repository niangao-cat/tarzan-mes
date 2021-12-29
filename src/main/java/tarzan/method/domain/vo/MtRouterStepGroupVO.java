package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.entity.MtRouterStepGroupStep;

public class MtRouterStepGroupVO {

    private MtRouterStepGroup routerStepGroup;
    private List<MtRouterStepGroupStep> routerStepGroupSteps;

    public MtRouterStepGroup getRouterStepGroup() {
        return routerStepGroup;
    }

    public void setRouterStepGroup(MtRouterStepGroup routerStepGroup) {
        this.routerStepGroup = routerStepGroup;
    }

    public List<MtRouterStepGroupStep> getRouterStepGroupSteps() {
        return routerStepGroupSteps;
    }

    public void setRouterStepGroupSteps(List<MtRouterStepGroupStep> routerStepGroupSteps) {
        this.routerStepGroupSteps = routerStepGroupSteps;
    }

}
