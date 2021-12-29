package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO41 implements Serializable {

    private static final long serialVersionUID = 4426830041225185740L;

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
    @ApiModelProperty(value = "关键步骤")
    private String keyStepFlag;
    @ApiModelProperty(value = "完成步骤")
    private String doneStepFlag;
    @ApiModelProperty(value = "完成数量")
    private Double completedQty;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty(value = "工作中数量")
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("报废保留数量")
    private Double scrapHoldQty;
    @ApiModelProperty(value = "步骤选择策略")
    private String queueDecisionType;
    @ApiModelProperty(value = "步骤选择策略描述")
    private String queueDecisionTypeDesc;

    @ApiModelProperty(value = "子步骤")
    private List<String> children = new ArrayList<String>();

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
    public Double getCompletedQty() {
        return completedQty;
    }
    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }
    public Double getQueueQty() {
        return queueQty;
    }
    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }
    public Double getWorkingQty() {
        return workingQty;
    }
    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }
    public Double getCompletePendingQty() {
        return completePendingQty;
    }
    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }
    public String getRouterStepId() {
        return routerStepId;
    }
    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
    public List<String> getChildren() {
        return children;
    }
    public void setChildren(List<String> children) {
        this.children = children;
    }
    public String getQueueDecisionType() {
        return queueDecisionType;
    }
    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
    }
    public String getQueueDecisionTypeDesc() {
        return queueDecisionTypeDesc;
    }
    public void setQueueDecisionTypeDesc(String queueDecisionTypeDesc) {
        this.queueDecisionTypeDesc = queueDecisionTypeDesc;
    }
    public Double getScrapHoldQty() { return scrapHoldQty; }
    public void setScrapHoldQty(Double scrapHoldQty) { this.scrapHoldQty = scrapHoldQty; }
}
