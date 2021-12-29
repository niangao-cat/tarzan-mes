package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtRouterSubstep;
import tarzan.method.domain.entity.MtRouterSubstepComponent;

public class MtRouterSubstepVO {

    private MtRouterSubstep routerSubstep;
    private String substepName;
    private String description;
    private List<MtRouterSubstepComponent> routerSubstepComponents;

    public MtRouterSubstep getRouterSubstep() {
        return routerSubstep;
    }

    public void setRouterSubstep(MtRouterSubstep routerSubstep) {
        this.routerSubstep = routerSubstep;
    }

    public List<MtRouterSubstepComponent> getRouterSubstepComponents() {
        return routerSubstepComponents;
    }

    public void setRouterSubstepComponents(List<MtRouterSubstepComponent> routerSubstepComponents) {
        this.routerSubstepComponents = routerSubstepComponents;
    }

    public String getSubstepName() {
        return substepName;
    }

    public void setSubstepName(String substepName) {
        this.substepName = substepName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
