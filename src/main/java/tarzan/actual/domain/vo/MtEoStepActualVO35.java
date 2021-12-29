package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/17 15:19
 * @Author: ${yiyang.xie}
 */
public class MtEoStepActualVO35 implements Serializable {
    private static final long serialVersionUID = 8779082637058606035L;
    @ApiModelProperty("执行作业步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("排队数量")
    private Double queueQty;
    @ApiModelProperty("正在加工的数量")
    private Double workingQty;
    @ApiModelProperty("完成的数量")
    private Double completedQty;
    @ApiModelProperty("报废的数量")
    private Double scrappedQty;
    @ApiModelProperty("完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("状态")
    private String status;


    private String eoRouterActualId;
    private String routerStepId;
    @ApiModelProperty("EO步骤名称")
    private String stepName;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
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

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
}
