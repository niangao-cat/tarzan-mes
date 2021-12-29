package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/9 10:47
 * @Author: ${yiyang.xie}
 */
public class MtBomComponentVO18 implements Serializable {
    private static final long serialVersionUID = 1713810310872854026L;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("步骤名称")
    private String stepName;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("组件清单行ID")
    private String bomComponentId;
    @ApiModelProperty("需求数量")
    private Double qty;
    @ApiModelProperty("是否按步骤划分组件数量")
    private String dividedByStep;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getDividedByStep() {
        return dividedByStep;
    }

    public void setDividedByStep(String dividedByStep) {
        this.dividedByStep = dividedByStep;
    }
}
