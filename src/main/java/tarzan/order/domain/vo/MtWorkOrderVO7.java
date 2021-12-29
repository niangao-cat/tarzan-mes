package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtWorkOrderVO7 implements Serializable {
    private static final long serialVersionUID = 5444673459390308288L;

    @ApiModelProperty(value = "生产指令")
    private String workOrderId;
    @ApiModelProperty(value = "标准作业（工艺）主键，表示唯一标准作业（工艺）")
    private String operationId;
    @ApiModelProperty(value = "EO步骤名称")
    private String stepName;
    @ApiModelProperty(value = "步骤ID（对于特殊操作步骤ID就是操作ID）")
    private String routerStepId;
    @ApiModelProperty(value = "BOM组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "需求数量")
    private Double qty;
    @ApiModelProperty(value = "是否按步骤划分组件数量")
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
