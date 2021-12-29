package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/25 4:25 下午
 */
public class MtEoRouterDTO5 implements Serializable {
    private static final long serialVersionUID = 5139995510172225976L;
    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;
    @ApiModelProperty(value = "步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "步骤识别码")
    private String step;
    @ApiModelProperty(value = "步骤类型")
    private String routerStepType;
    @ApiModelProperty(value = "步骤类型描述")
    private String routerStepTypeDesc;
    @ApiModelProperty(value = "步骤描述")
    private String description;
    @ApiModelProperty(value = "入口步骤")
    private String entryStepFlag;
    @ApiModelProperty(value = "步骤选择策略")
    private String queueDecisionType;
    @ApiModelProperty(value = "步骤选择策略描述")
    private String queueDecisionTypeDesc;
    @ApiModelProperty(value = "关键步骤")
    private String keyStepFlag;
    @ApiModelProperty(value = "完成步骤")
    private String doneStepFlag;
    @ApiModelProperty(value = "下一步骤")
    private List<String> nextStepName;

    @ApiModelProperty(value = "子步骤")
    private List<MtEoRouterDTO5> children;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public String getRouterStepTypeDesc() {
        return routerStepTypeDesc;
    }

    public void setRouterStepTypeDesc(String routerStepTypeDesc) {
        this.routerStepTypeDesc = routerStepTypeDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryStepFlag() {
        return entryStepFlag;
    }

    public void setEntryStepFlag(String entryStepFlag) {
        this.entryStepFlag = entryStepFlag;
    }

    public String getKeyStepFlag() {
        return keyStepFlag;
    }

    public void setKeyStepFlag(String keyStepFlag) {
        this.keyStepFlag = keyStepFlag;
    }

    public String getDoneStepFlag() {
        return doneStepFlag;
    }

    public void setDoneStepFlag(String doneStepFlag) {
        this.doneStepFlag = doneStepFlag;
    }

    public List<String> getNextStepName() {
        return nextStepName;
    }

    public void setNextStepName(List<String> nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getQueueDecisionType() {
        return queueDecisionType;
    }

    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
    }

    public List<MtEoRouterDTO5> getChildren() {
        return children;
    }

    public void setChildren(List<MtEoRouterDTO5> children) {
        this.children = children;
    }

    public String getQueueDecisionTypeDesc() {
        return queueDecisionTypeDesc;
    }

    public void setQueueDecisionTypeDesc(String queueDecisionTypeDesc) {
        this.queueDecisionTypeDesc = queueDecisionTypeDesc;
    }
}
