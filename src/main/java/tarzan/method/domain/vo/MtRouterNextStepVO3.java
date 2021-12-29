package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterNextStepVO3 implements Serializable {

    private static final long serialVersionUID = 2938330801166215817L;
    private String routerStepId; // 工艺路线步骤标识
    private String nextDecisionType; // 选择策略类型(TYPE_GROUP:DECISION_TYPE)
    private String nextDecisionValue; // 选择策略对应值(MAIN时无值，PRODUCT时为物料ID，NC时为编码)

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getNextDecisionType() {
        return nextDecisionType;
    }

    public void setNextDecisionType(String nextDecisionType) {
        this.nextDecisionType = nextDecisionType;
    }

    public String getNextDecisionValue() {
        return nextDecisionValue;
    }

    public void setNextDecisionValue(String nextDecisionValue) {
        this.nextDecisionValue = nextDecisionValue;
    }
}
