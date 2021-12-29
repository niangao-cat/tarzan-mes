package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/10 14:56
 * @Author: ${yiyang.xie}
 */
public class MtOpWkcDispatchRelDTO implements Serializable {
    private static final long serialVersionUID = -4806617930215655907L;

    @ApiModelProperty("关系ID，标识唯一一条数据")
    private String operationWkcDispatchRelId;
    @ApiModelProperty("工艺ID，标识唯一工艺，表示关系所属工艺")
    private String operationId;
    @ApiModelProperty("步骤名称， 用于在工艺路线中多次出现同一标准工艺时区分唯一工艺")
    private String stepName;
    @ApiModelProperty("工作单元ID，标识在当前工艺和步骤下允许调度分派时选择的WKC")
    private String workcellId;
    @ApiModelProperty("优先级，标识当前工艺步骤存在多个可选WKC时，推荐的使用顺序")
    private Long priority;

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
}
