package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterStepVO9 implements Serializable {

    private static final long serialVersionUID = 5145740128059841189L;
    private String childStepId;
    private String childType;

    public String getChildStepId() {
        return childStepId;
    }

    public void setChildStepId(String childStepId) {
        this.childStepId = childStepId;
    }

    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }

}
