package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class
MtEoStepActualVO1 implements Serializable {
    private static final long serialVersionUID = -7550755499152465950L;

    @ApiModelProperty("eo步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("EO主键，标识唯一EO")
    private String eoId;
    @ApiModelProperty("eo状态")
    private String eoStatus;
    @ApiModelProperty("eo 执行作业需求数量")
    private Double eoQty;
    @ApiModelProperty("执行作业工艺路线实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("此工艺路线加工数量（即进入首工序排队的数量）")
    private Double routerQty;
    @ApiModelProperty("步骤ID（对于特殊操作步骤ID就是操作ID）")
    private String routerStepId;
    @ApiModelProperty("标准作业（工艺）主键，表示唯一标准作业（工艺")
    private String operationId;
    @ApiModelProperty("EO步骤名称")
    private String stepName;
    @ApiModelProperty("woID")
    private String workOrderId;


    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoStatus() {
        return eoStatus;
    }

    public void setEoStatus(String eoStatus) {
        this.eoStatus = eoStatus;
    }

    public Double getEoQty() {
        return eoQty;
    }

    public void setEoQty(Double eoQty) {
        this.eoQty = eoQty;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public Double getRouterQty() {
        return routerQty;
    }

    public void setRouterQty(Double routerQty) {
        this.routerQty = routerQty;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
}
