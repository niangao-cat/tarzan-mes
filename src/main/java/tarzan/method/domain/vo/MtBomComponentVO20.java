package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/10 10:52
 * @Author: ${yiyang.xie}
 */
public class MtBomComponentVO20 implements Serializable {
    private static final long serialVersionUID = -890009042978988498L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
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

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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