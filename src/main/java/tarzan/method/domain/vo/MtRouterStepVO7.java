package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterStepVO7 implements Serializable {

    private static final long serialVersionUID = 6908507209933575797L;
    private String parentStepId;
    private String parentType;

    public String getParentStepId() {
        return parentStepId;
    }

    public void setParentStepId(String parentStepId) {
        this.parentStepId = parentStepId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

}
