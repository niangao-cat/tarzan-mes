package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterStepGroupStepVO3 implements Serializable {

    private static final long serialVersionUID = -3331434083007013080L;
    private String routerStepGroupStepId;
    private String routerStepGroupId;
    private String routerStepId;
    private Long sequence;

    public String getRouterStepGroupStepId() {
        return routerStepGroupStepId;
    }

    public void setRouterStepGroupStepId(String routerStepGroupStepId) {
        this.routerStepGroupStepId = routerStepGroupStepId;
    }

    public String getRouterStepGroupId() {
        return routerStepGroupId;
    }

    public void setRouterStepGroupId(String routerStepGroupId) {
        this.routerStepGroupId = routerStepGroupId;
    }

    
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

}
