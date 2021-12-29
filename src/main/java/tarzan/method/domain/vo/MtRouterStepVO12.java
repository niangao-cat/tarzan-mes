package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtRouterStepVO12 implements Serializable {

    private static final long serialVersionUID = 4541591281422904758L;
    @ApiModelProperty("工艺路线ID")
    private String routerStepId;
    @ApiModelProperty("工艺路线名称")
    private String routerId;
    @ApiModelProperty("工艺路线类型")
    private String routerName;
    @ApiModelProperty("工艺路线版本")
    private String routerDescription;
    @ApiModelProperty("工艺路线描述")
    private String stepName;
    @ApiModelProperty("是否为当前版本")
    private String routerStepType;
    @ApiModelProperty("工艺路线状态")
    private String sequence;
    @ApiModelProperty("来源状态")
    private String description;
    @ApiModelProperty("生效时间")
    private String coproductFlag;
    @ApiModelProperty("失效时间")
    private String queueDecisionType;
    @ApiModelProperty("工艺路线引用BOMID")
    private String entryStepFlag;
    @ApiModelProperty("工艺路线引用BOM名称")
    private String keyStepFlag;
    @ApiModelProperty("工艺路线引用BOM描述")
    private String copiedFromRouterStepId;

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

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterDescription() {
        return routerDescription;
    }

    public void setRouterDescription(String routerDescription) {
        this.routerDescription = routerDescription;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoproductFlag() {
        return coproductFlag;
    }

    public void setCoproductFlag(String coproductFlag) {
        this.coproductFlag = coproductFlag;
    }

    public String getQueueDecisionType() {
        return queueDecisionType;
    }

    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
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

    public String getCopideFromRouterStepId() {
        return copiedFromRouterStepId;
    }

    public void setCopiedFromRouterStepId(String copyFromRouterStepId) {
        this.copiedFromRouterStepId = copyFromRouterStepId;
    }

}
