package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/7 16:11
 * @Author: ${yiyang.xie}
 */
public class MtEoStepWipDTO2 implements Serializable {
    private static final long serialVersionUID = 2755418454750501359L;

    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty(value = "工艺Id")
    private String routerId;
    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;
    @ApiModelProperty(value = "步骤识别码")
    private String stepName;
    @ApiModelProperty(value = "工艺编码")
    private String operationName;
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty("运行数量")
    private Double workingQty;
    @ApiModelProperty("完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("完成数量")
    private Double completedQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;
    @ApiModelProperty("保留数量")
    private Double holdQty;

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
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

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
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

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }
}
