package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoRouterActualVO11 implements Serializable {
    private static final long serialVersionUID = 8304179691673558246L;

    private List<String> returnSteps; // 返回步骤
    private String eoRouterActualId; // 返回步骤所在工艺实绩

    public List<String> getReturnStep() {
        return returnSteps;
    }

    public void setReturnStep(List<String> returnSteps) {
        this.returnSteps = returnSteps;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }
}
