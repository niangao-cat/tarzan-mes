package tarzan.dispatch.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/10 10:00
 * @Author: ${yiyang.xie}
 */
public class MtOpWkcDispatchRelVO8 implements Serializable {
    private static final long serialVersionUID = 2462951068371681351L;

    @ApiModelProperty("关系ID，唯一标识")
    private String operationWkcDispatchRelId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("步骤名称")
    private String stepName;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("优先级")
    private Long priority;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元名称")
    private String workcellName;
    @ApiModelProperty("工艺名称")
    private String operationName;
    @ApiModelProperty("工艺描述")
    private String description;

    public String getOperationWkcDispatchRelId() {
        return operationWkcDispatchRelId;
    }

    public void setOperationWkcDispatchRelId(String operationWkcDispatchRelId) {
        this.operationWkcDispatchRelId = operationWkcDispatchRelId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
