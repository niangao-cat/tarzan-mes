package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 14:32
 */
public class MtEoRouterActualVO49 implements Serializable {
    private static final long serialVersionUID = -1557810010362525615L;

    @ApiModelProperty("入口步骤标识")
    private String entryStepFlag;
    @ApiModelProperty("主入口步骤标识")
    private String primaryEntryStepFlag;
    @ApiModelProperty("步骤类型")
    private String stepType;
    @ApiModelProperty("组步骤")
    private String groupStep;
    @ApiModelProperty("工艺")
    private String operation;
    @ApiModelProperty("工艺路线")
    private String routerId;
    @ApiModelProperty("步骤组类型")
    private String routerStepGroupType;
    @ApiModelProperty("工艺路线实绩")
    private String eoRouterActualId;
    @ApiModelProperty("步骤ID")
    private String routerStepId;

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

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
