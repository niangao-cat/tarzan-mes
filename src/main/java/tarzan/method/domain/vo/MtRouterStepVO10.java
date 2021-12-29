package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MtRouterStepVO10 implements Serializable {

    private static final long serialVersionUID = -2613132270214456722L;

    private boolean childExistFlag;
    private String groupType;
    private String routerStepType;
    private List<String> childStepId;
    private List<Map<String, String>> childSteps;

    public boolean isChildExistFlag() {
        return childExistFlag;
    }

    public void setChildExistFlag(boolean childExistFlag) {
        this.childExistFlag = childExistFlag;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public List<Map<String, String>> getChildSteps() {
        return childSteps;
    }

    public void setChildSteps(List<Map<String, String>> childSteps) {
        this.childSteps = childSteps;
    }

    public List<String> getChildStepId() {
        return childStepId;
    }

    public void setChildStepId(List<String> childStepId) {
        this.childStepId = childStepId;
    }

}
