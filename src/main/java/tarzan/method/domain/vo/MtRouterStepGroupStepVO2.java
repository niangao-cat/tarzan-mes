package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtRouterStepGroupStepVO2 implements Serializable {

    private static final long serialVersionUID = -3331434083007013080L;
    private String routerStepId;
    private List<MtRouterStepGroupStepVO3> groupStepList;
    
    public String getRouterStepId() {
        return routerStepId;
    }
    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
    public List<MtRouterStepGroupStepVO3> getGroupStepList() {
        return groupStepList;
    }
    public void setGroupStepList(List<MtRouterStepGroupStepVO3> groupStepList) {
        this.groupStepList = groupStepList;
    }
    
}
