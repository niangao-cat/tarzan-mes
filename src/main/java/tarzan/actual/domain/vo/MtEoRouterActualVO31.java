package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtEoRouterActualVO31
 * @description
 * @date 2019年10月21日 10:19
 */
public class MtEoRouterActualVO31 implements Serializable {
    private static final long serialVersionUID = 2315392691968512117L;
    private String routerStepId; // 步骤Id
    private String entryStepFlag; // 入口步骤标识
    private String stepType; // 步骤类型
    private String stepName; // 步骤名称
    private String groupStep; // 组步骤
    private String operation; // 工艺
    private String routerId; // 工艺路线
    private String routerStepGroupType; // 步骤组类型

    public String getEntryStepFlag() {
        return entryStepFlag;
    }

    public void setEntryStepFlag(String entryStepFlag) {
        this.entryStepFlag = entryStepFlag;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getGroupStep() {
        return groupStep;
    }

    public void setGroupStep(String groupStep) {
        this.groupStep = groupStep;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepGroupType() {
        return routerStepGroupType;
    }

    public void setRouterStepGroupType(String routerStepGroupType) {
        this.routerStepGroupType = routerStepGroupType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    
    
}
