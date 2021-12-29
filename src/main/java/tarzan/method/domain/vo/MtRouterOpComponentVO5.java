package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/28 15:08
 */
public class MtRouterOpComponentVO5 implements Serializable {
    @ApiModelProperty("工艺路线步骤组件唯一标识")
    private String routerOperationComponentId;

    @ApiModelProperty(value = "工艺路线步骤唯一标识")
    private String routerOperationId;

    @ApiModelProperty(value = "组件")
    private String bomComponentId;

    @ApiModelProperty(value = "顺序")
    private Long sequence;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;

    public String getRouterOperationComponentId() {
        return routerOperationComponentId;
    }

    public void setRouterOperationComponentId(String routerOperationComponentId) {
        this.routerOperationComponentId = routerOperationComponentId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
