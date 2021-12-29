package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterSiteAssign;

public class MtRouterVO10 {

    private MtRouter router;
    private List<MtRouterSiteAssign> routerSiteAssigns;
    private List<MtRouterStepVO> routerSteps;

    public MtRouter getRouter() {
        return router;
    }

    public void setRouter(MtRouter router) {
        this.router = router;
    }

    public List<MtRouterStepVO> getRouterSteps() {
        return routerSteps;
    }

    public void setRouterSteps(List<MtRouterStepVO> routerSteps) {
        this.routerSteps = routerSteps;
    }

    public List<MtRouterSiteAssign> getRouterSiteAssigns() {
        return routerSiteAssigns;
    }

    public void setRouterSiteAssigns(List<MtRouterSiteAssign> routerSiteAssigns) {
        this.routerSiteAssigns = routerSiteAssigns;
    }

}
