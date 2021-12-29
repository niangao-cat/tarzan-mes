package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO5 extends MtEoRouterActualVO1 implements Serializable {
    private static final long serialVersionUID = 6313049444588882379L;
    private String subRouterFlag; // 是否为分支工艺路线标识
    private String sourceEoStepActualId;

    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}
