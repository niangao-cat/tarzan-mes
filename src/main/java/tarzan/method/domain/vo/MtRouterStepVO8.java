package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/21 20:28
 */
public class MtRouterStepVO8 implements Serializable {

    private static final long serialVersionUID = 4013809722705035101L;
    private String level;
    private String routerStepId;
    private String routerId;
    private String currentLevelFlag;
    private String decisionRouterFlag;
    private String decisionStep;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getCurrentLevelFlag() {
        return currentLevelFlag;
    }

    public void setCurrentLevelFlag(String currentLevelFlag) {
        this.currentLevelFlag = currentLevelFlag;
    }

    public String getDecisionRouterFlag() {
        return decisionRouterFlag;
    }

    public void setDecisionRouterFlag(String decisionRouterFlag) {
        this.decisionRouterFlag = decisionRouterFlag;
    }

    public String getDecisionStep() {
        return decisionStep;
    }

    public void setDecisionStep(String decisionStep) {
        this.decisionStep = decisionStep;
    }
}
