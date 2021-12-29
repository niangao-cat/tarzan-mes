package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 18:57
 * @Author: ${yiyang.xie}
 */
public class MtEoStepActualVO31 implements Serializable {
    private static final long serialVersionUID = -3116621075944046853L;
    @ApiModelProperty("排队数量")
    private Double queueQty;
    @ApiModelProperty("工作中数量")
    private Double workingQty;
    @ApiModelProperty("完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("已完成数量")
    private Double completedQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;
    @ApiModelProperty("保留数量")
    private Double holdQty;
    @ApiModelProperty("执行作业步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("工艺路线实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺描述")
    private String operationName;
    @ApiModelProperty("步骤识别码")
    private String stepName;
    @ApiModelProperty("方式标识")
    private String bypassedFlag;
    @ApiModelProperty("返工标识")
    private String reworkStepFlag;
    @ApiModelProperty("重做标识")
    private String localReworkFlag;
    @ApiModelProperty("最大经过次数")
    private Long maxProcessTimes;
    @ApiModelProperty("经过次数")
    private Long timesProcessed;
    @ApiModelProperty("前道步骤ID")
    private String previousStepId;
    @ApiModelProperty("排队时间")
    private Date queueDate;
    @ApiModelProperty("运行时间")
    private Date workingDate;
    @ApiModelProperty("完成时间")
    private Date completedDate;
    @ApiModelProperty("完成暂存时间")
    private Date completePendingDate;
    @ApiModelProperty("说明")
    private String specialInstruction;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("保留次数")
    private Long holdCount;

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

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {

        this.sequence = sequence;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getBypassedFlag() {
        return bypassedFlag;
    }

    public void setBypassedFlag(String bypassedFlag) {
        this.bypassedFlag = bypassedFlag;
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

    public Long getMaxProcessTimes() {
        return maxProcessTimes;
    }

    public void setMaxProcessTimes(Long maxProcessTimes) {
        this.maxProcessTimes = maxProcessTimes;
    }

    public Long getTimesProcessed() {
        return timesProcessed;
    }

    public void setTimesProcessed(Long timesProcessed) {

        this.timesProcessed = timesProcessed;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public Date getQueueDate() {
        if (queueDate != null) {
            return (Date) queueDate.clone();
        } else {
            return null;
        }
    }

    public void setQueueDate(Date queueDate) {
        if (queueDate == null) {
            this.queueDate = null;
        } else {
            this.queueDate = (Date) queueDate.clone();
        }
    }

    public Date getWorkingDate() {
        if (workingDate != null) {
            return (Date) workingDate.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDate(Date workingDate) {
        if (workingDate == null) {
            this.workingDate = null;
        } else {
            this.workingDate = (Date) workingDate.clone();
        }
    }

    public Date getCompletedDate() {
        if (completedDate != null) {
            return (Date) completedDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDate(Date completedDate) {
        if (completedDate == null) {
            this.completedDate = null;
        } else {
            this.completedDate = (Date) completedDate.clone();
        }
    }

    public Date getCompletePendingDate() {
        if (completePendingDate != null) {
            return (Date) completePendingDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDate(Date completePendingDate) {
        if (completePendingDate == null) {
            this.completePendingDate = null;
        } else {
            this.completePendingDate = (Date) completePendingDate.clone();
        }
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(Long holdCount) {
        this.holdCount = holdCount;
    }
}
