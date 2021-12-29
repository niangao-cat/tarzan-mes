package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/6/24 15:36
 * @Description:
 */
public class MtRouterStepVO5 implements Serializable {
    private static final long serialVersionUID = 3348303022747484104L;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("步骤名称")
    private String stepName;
    @ApiModelProperty("步骤顺序")
    private Long sequence;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺名称")
    private String operationName;
    @ApiModelProperty("工艺描述")
    private String description;
    @ApiModelProperty("步骤类型")
    private String routerStepType;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

}
