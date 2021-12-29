package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 17:06
 * @Description:
 */
public class MtEoStepActualVO39 implements Serializable {
    private static final long serialVersionUID = -4370549784604509610L;

    @ApiModelProperty("主键")
    private String eoStepActualId;
    @ApiModelProperty(value = "EO工艺路线主键，表示唯一EO路径")
    private String eoRouterActualId;
    @ApiModelProperty(value = "步骤ID（对于特殊操作步骤ID就是操作ID）")
    private String routerStepId;
    @ApiModelProperty(value = "是否用于返工")
    private String reworkStepFlag;
    @ApiModelProperty(value = "是否为此路径的返工步骤")
    private String localReworkFlag;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("排队变更数量")
    private Double queueQty;
    @ApiModelProperty("工作中变更数量")
    private Double workingQty;
    @ApiModelProperty("完成暂存变更数量")
    private Double completePendingQty;
    @ApiModelProperty("完成的变更数量")
    private Double completedQty;
    @ApiModelProperty("报废变更数量")
    private Double scrappedQty;
    @ApiModelProperty("保留变更数量")
    private Double holdQty;
    @ApiModelProperty("前道步骤")
    private String previousStepId;
    @ApiModelProperty("已加工次数（EO已通过该步骤的次数）")
    private Long timesProcessed;
    @ApiModelProperty("步骤预留次数，每次步骤增加预留时增加")
    private Long holdCount;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
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

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public Long getTimesProcessed() {
        return timesProcessed;
    }

    public void setTimesProcessed(Long timesProcessed) {
        this.timesProcessed = timesProcessed;
    }

    public Long getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(Long holdCount) {
        this.holdCount = holdCount;
    }
}
