package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO10 implements Serializable {
    private static final long serialVersionUID = 3822675580543729688L;
    private String entryStepFlag; // 入口步骤标识
    private String primaryEntryStepFlag; // 主入口步骤标识
    private String stepType; // 步骤类型
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

    public String getPrimaryEntryStepFlag() {
        return primaryEntryStepFlag;
    }

    public void setPrimaryEntryStepFlag(String primaryEntryStepFlag) {
        this.primaryEntryStepFlag = primaryEntryStepFlag;
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
}
