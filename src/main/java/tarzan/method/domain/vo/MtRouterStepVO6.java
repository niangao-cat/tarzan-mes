package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MtRouterStepVO6 implements Serializable {

    private static final long serialVersionUID = -7336749944525255061L;
    private boolean parentExistFlag;
    private String groupType;
    private String parentStepType;
    private List<Map<String, String>> parentSteps;

    public boolean isParentExistFlag() {
        return parentExistFlag;
    }

    public void setParentExistFlag(boolean parentExistFlag) {
        this.parentExistFlag = parentExistFlag;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getParentStepType() {
        return parentStepType;
    }

    public void setParentStepType(String parentStepType) {
        this.parentStepType = parentStepType;
    }

    public List<Map<String, String>> getParentSteps() {
        return parentSteps;
    }

    public void setParentSteps(List<Map<String, String>> parentSteps) {
        this.parentSteps = parentSteps;
    }

}
