package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.*;

public class MtRouterStepVO {

    private MtRouterStep routerStep;
    private MtRouterLink routerLink;
    private MtRouterDoneStep routerDoneStep;
    private MtRouterReturnStep routerReturnStep;
    private MtRouterStepGroupVO routerStepGroup;
    private MtRouterOperationVO routerOperation;
    private List<MtRouterSubstepVO> routerSubsteps;
    private List<MtRouterNextStep> routerNextSteps;
    private String processFlag;

    public String getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(String processFlag) {
        this.processFlag = processFlag;
    }

    public MtRouterStep getRouterStep() {
        return routerStep;
    }

    public void setRouterStep(MtRouterStep routerStep) {
        this.routerStep = routerStep;
    }

    public MtRouterLink getRouterLink() {
        return routerLink;
    }

    public void setRouterLink(MtRouterLink routerLink) {
        this.routerLink = routerLink;
    }

    public MtRouterOperationVO getRouterOperation() {
        return routerOperation;
    }

    public void setRouterOperation(MtRouterOperationVO routerOperation) {
        this.routerOperation = routerOperation;
    }

    public List<MtRouterSubstepVO> getRouterSubsteps() {
        return routerSubsteps;
    }

    public void setRouterSubsteps(List<MtRouterSubstepVO> routerSubsteps) {
        this.routerSubsteps = routerSubsteps;
    }

    public List<MtRouterNextStep> getRouterNextSteps() {
        return routerNextSteps;
    }

    public void setRouterNextSteps(List<MtRouterNextStep> routerNextSteps) {
        this.routerNextSteps = routerNextSteps;
    }

    public MtRouterDoneStep getRouterDoneStep() {
        return routerDoneStep;
    }

    public void setRouterDoneStep(MtRouterDoneStep routerDoneStep) {
        this.routerDoneStep = routerDoneStep;
    }

    public MtRouterReturnStep getRouterReturnStep() {
        return routerReturnStep;
    }

    public void setRouterReturnStep(MtRouterReturnStep routerReturnStep) {
        this.routerReturnStep = routerReturnStep;
    }

    public MtRouterStepGroupVO getRouterStepGroup() {
        return routerStepGroup;
    }

    public void setRouterStepGroup(MtRouterStepGroupVO routerStepGroup) {
        this.routerStepGroup = routerStepGroup;
    }

}
